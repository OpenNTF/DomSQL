/*
** (c) Copyright IBM Corp. 2014
** 
** Licensed under the Apache License, Version 2.0 (the "License"); 
** you may not use this file except in compliance with the License. 
** You may obtain a copy of the License at:
** 
** http://www.apache.org/licenses/LICENSE-2.0 
** 
** Unless required by applicable law or agreed to in writing, software 
** distributed under the License is distributed on an "AS IS" BASIS, 
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
** implied. See the License for the specific language governing 
** permissions and limitations under the License.
*/
#include "allinc.h"

/////////////////////////////////////////////////////////////////////
// DomSQL
/////////////////////////////////////////////////////////////////////

// Unknown column data type
#define	VIEW_COL_UNKNOWN	-1


jint		DomSQL::secretCode = 0;

jclass		DomSQL::jniClass = 0;
jmethodID	DomSQL::m_getDatabaseHandle = 0;
jmethodID	DomSQL::m_createView = 0;
jmethodID	DomSQL::m_createViewColumn = 0;
jmethodID	DomSQL::m_createViewIndex = 0;
jmethodID	DomSQL::m_createViewIndexEntry = 0;


jboolean DomSQL::FLAG_DUMP_DESIGN				= false;
jboolean DomSQL::FLAG_DUMP_SECRETCODE			= false;

jboolean DomSQL::FLAG_TRACE_DICTIONARY			= false;
jboolean DomSQL::FLAG_TRACE_NIFCALLS			= false;
jboolean DomSQL::FLAG_TRACE_XFILTER				= false;
jboolean DomSQL::FLAG_TRACE_XNEXT				= false;
jboolean DomSQL::FLAG_TRACE_READDATA			= false;
jboolean DomSQL::FLAG_TRACE_FINDBYKEY			= false;
jboolean DomSQL::FLAG_TRACE_BESTINDEX			= false;
jboolean DomSQL::FLAG_TRACE_PERFORMANCE_HINTS	= false;

jboolean DomSQL::FLAG_TRACE_EOF					= false;

bool DomSQL::init(JNIEnv *env) {
	// Load the class
    if(!JNIFindClass(env,jniClass,"com/ibm/domino/domsql/sqlite/driver/jni/DomSQL")) return false;

	//    public static long getDatabaseHandle(String databasePath) throws SQLException {
	if(!JNIGetStaticMethodID( env, m_getDatabaseHandle, jniClass, "getDatabaseHandle", "(Ljava/lang/String;)J")) return false;
    //    public static Object createView(String name, int noteID)
	if(!JNIGetStaticMethodID( env, m_createView, jniClass, "createView", "(ILjava/lang/String;)Ljava/lang/Object;")) return false;
    //    public static Object createViewColumn(Object parent, String name, String title, int type, int summaryIndex)
    if(!JNIGetStaticMethodID( env, m_createViewColumn, jniClass, "createViewColumn", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;II)Ljava/lang/Object;")) return false;
    //    public static Object createViewIndex(Object parent, int collation)
    if(!JNIGetStaticMethodID( env, m_createViewIndex, jniClass, "createViewIndex", "(Ljava/lang/Object;I)Ljava/lang/Object;")) return false;
    //    public static Object createViewIndexEntry(Object parent, int colIdx, boolean desc)
    if(!JNIGetStaticMethodID( env, m_createViewIndexEntry, jniClass, "createViewIndexEntry", "(Ljava/lang/Object;Ljava/lang/String;Z)Ljava/lang/Object;")) return false;

	return true;
}

void DomSQL::throwex(JNIEnv *env, sqlite3* db) {
	const jchar* msg = (const jchar*)sqlite3_errmsg16(db);
	jstring js=env->NewString(msg,jstrlen(msg));
	JNIUtils::throwex(env,js);
}

void DomSQL::throwex(JNIEnv *env, sqlite3_stmt* stmt) {
	sqlite3* db = sqlite3_db_handle(stmt);
	throwex(env,db);
}

extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_DomSQL_setDebugFlag(
        JNIEnv *env, jclass clazz, jstring flag, jboolean value) 
{
	JStringUTF8 cFlag(env,flag);
	if(cFlag.equals("TRACE")) {
		JNIUtils::FLAG_ENABLE_TRACE = value;
	} else if(cFlag.equals("TRACE_FUNCTION")) {
		JNIUtils::FLAG_TRACE_FUNCTION = value;
	} else if(cFlag.equals("DUMP_DESIGN")) {
		DomSQL::FLAG_DUMP_DESIGN = value;
	} else if(cFlag.equals("DUMP_SECRETCODE")) {
		DomSQL::FLAG_DUMP_SECRETCODE = value;
	} else if(cFlag.equals("TRACE_DICTIONARY")) {
		DomSQL::FLAG_TRACE_DICTIONARY = value;
	} else if(cFlag.equals("TRACE_NIFCALLS")) {
		DomSQL::FLAG_TRACE_NIFCALLS = value;
	} else if(cFlag.equals("TRACE_XFILTER")) {
		DomSQL::FLAG_TRACE_XFILTER = value;
	} else if(cFlag.equals("TRACE_XNEXT")) {
		DomSQL::FLAG_TRACE_XNEXT = value;
	} else if(cFlag.equals("TRACE_READDATA")) {
		DomSQL::FLAG_TRACE_READDATA = value;
	} else if(cFlag.equals("TRACE_FINDBYKEY")) {
		DomSQL::FLAG_TRACE_FINDBYKEY = value;
	} else if(cFlag.equals("TRACE_BESTINDEX")) {
		DomSQL::FLAG_TRACE_BESTINDEX = value;
	} else if(cFlag.equals("TRACE_PERFORMANCE_HINTS")) {
		DomSQL::FLAG_TRACE_PERFORMANCE_HINTS = value;		
	} else if(cFlag.equals("TRACE_EOF")) {
		DomSQL::FLAG_TRACE_EOF = value;
	}
}

extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_DomSQL_setSecretCode(
        JNIEnv *env, jclass clazz, jint secretCode) 
{
	// Only if not already set
	if(DomSQL::secretCode==0) {
		DomSQL::secretCode = secretCode;
	}
}



// ==============================================================================================
// Read the view collection
// ==============================================================================================

extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_DomSQL_readViewList(
        JNIEnv *env, jclass clazz, jlong _hDb, jobject list) 
{
	DBHANDLE hDb = (DBHANDLE)_hDb;
	HCOLLECTION hCollection;
	if(!NCheck(env,  NIFOpenCollection(
				hDb, 
			    hDb, 
				NOTE_ID_SPECIAL+NOTE_CLASS_DESIGN, //+NOTE_CLASS_VIEW,
			    OPEN_DO_NOT_CREATE,
			    NULLHANDLE,
			    &hCollection,
			    NULL, NULL, NULL, NULL))) {
		return;
	}

	COLLECTIONPOSITION CollPosition;
	CollPosition.Level = 0;
	CollPosition.Tumbler[0] = 1; 

	int viewIndex = 0;
	WORD wSignalFlag;
    do {
		DHANDLE hBuffer; DWORD dwEntriesFound;
		if (!NCheck(env,NIFReadEntries( 
					hCollection, 
					&CollPosition, 
                    NAVIGATE_NEXT, 
					1L, 
					NAVIGATE_NEXT, 
					MAXDWORD,
					READ_MASK_NOTECLASS+READ_MASK_SUMMARY, 
					&hBuffer, 
					NULL, 
					NULL,
                    &dwEntriesFound, 
					&wSignalFlag))) {
            break;
        }
        if (hBuffer == NULLHANDLE || dwEntriesFound==0) {
            break;
        }

		BYTE* pBuffer = (BYTE*) OSLockObject (hBuffer);
        for (int i=0; i<dwEntriesFound; i++, viewIndex++) {
			WORD wClass = *(WORD*) pBuffer;
			pBuffer += sizeof (WORD);

			ITEM_TABLE* ItemTable = (ITEM_TABLE*)pBuffer;
			pBuffer += ItemTable->Length;          
			if(wClass & NOTE_CLASS_VIEW) {
				char* pItemValue; WORD Length, Type;
				if (NSFLocateSummaryValue(ItemTable, 
										  ITEM_NAME_TEMPLATE_NAME,
										  &pItemValue, 
										  &Length, 
										  &Type)) {
					jstring js = NULL;
					if (TYPE_TEXT == Type)  {
						js = LMBCStoJavaString(env,pItemValue,Length);
					} else if (TYPE_TEXT_LIST == Type) {
						char* pText;
						ListGetText(pItemValue, FALSE, 0, &pText, &Length );
						js = LMBCStoJavaString(env,pText,Length);
					}
					if(js!=NULL) {
						JNIUtils::addObjectToList(env,list,js);
					}
				}
			}
		}
        OSUnlockObject (hBuffer);

        OSMemFree (hBuffer);
    }  while (wSignalFlag & SIGNAL_MORE_TO_DO);

    NIFCloseCollection(hCollection); 
} 


// ==============================================================================================
// Read the design of a view
// ==============================================================================================

// Specific columns
const int VIEWCOL_INVALID	= -9999;

static bool isValidCollationKeyType(int CollationItems, COLLATE_DESCRIPTOR* pCollateDesc) {
	COLLATE_DESCRIPTOR	CollateDesc;
	for (int wColumn = 1; wColumn <= CollationItems; wColumn++) {
		ODSReadMemory( &pCollateDesc, _COLLATE_DESCRIPTOR, &CollateDesc, 1);
		if (CollateDesc.keytype != COLLATE_TYPE_KEY &&
			CollateDesc.keytype != COLLATE_TYPE_CATEGORY) {
			return false;
		}
	}
	return true;
}

extern "C" JNIEXPORT jobject JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_DomSQL_readViewDesign(
        JNIEnv *env, jclass clazz, jint noteID, jlong _hNote) 
{
	NOTEHANDLE hNote = (NOTEHANDLE)_hNote;

	WORD wDataType;
	DWORD dwLength;

	BLOCKID ValueBlockID2;
    if (!NCheck(env,NSFItemInfo(hNote,
                             ITEM_NAME_TEMPLATE_NAME,
                             sizeof(ITEM_NAME_TEMPLATE_NAME) - 1,
                             NULL,
                             &wDataType,
                             &ValueBlockID2,
                             &dwLength)))

    {
        return NULL;
    }
	BLOCKID_ ValueBlockID2_(ValueBlockID2); // Ensure that it is properly cleaned up
    char* _pData = OSLockBlock(char, ValueBlockID2);
    _pData += sizeof(WORD);
	jobject jniView = DomSQL::createView(env,noteID,LMBCStoJavaString(env,_pData,(int) (dwLength - sizeof(WORD))));

	// Read the columns from the view
	// Get the format item 
	BLOCKID ValueBlockID;
	if(!NCheck(env,NSFItemInfo(hNote,
						VIEW_VIEW_FORMAT_ITEM,
						sizeof(VIEW_VIEW_FORMAT_ITEM)-1,
						NULL,
						&wDataType,
						&ValueBlockID,
						&dwLength))) {
		return NULL;
	}
	BLOCKID_ ValueBlockID_(ValueBlockID); // Ensure that it is properly cleaned up

	if(wDataType!=TYPE_VIEW_FORMAT) {
		return NULL;
	}

	// Lock the block returned, Get the view format and check the version
	char* pData = (char*)OSLockBlock(char, ValueBlockID);
	pData += sizeof(WORD);

	VIEW_FORMAT_HEADER HeaderFormat;
	BYTE* temp = (BYTE*)pData;
	ODSReadMemory( &temp, _VIEW_FORMAT_HEADER, &HeaderFormat, 1);
	if (HeaderFormat.Version != VIEW_FORMAT_VERSION) {
		return NULL;
	}

	VIEW_TABLE_FORMAT TableFormat;
	temp = (BYTE*)pData;
	ODSReadMemory( &temp, _VIEW_TABLE_FORMAT, &TableFormat, 1);

    // point past the table format
	UCHAR* pColumnData = (UCHAR*)pData + ODSLength(_VIEW_TABLE_FORMAT); 

	VIEW_COLUMN_FORMAT	ViewColumnFormat;
	temp = (BYTE*)pColumnData;
	ODSReadMemory( &temp, _VIEW_COLUMN_FORMAT, &ViewColumnFormat, 1);

    // Get a pointer to the packed data, which is located after the 
    // column format structures 
    UCHAR* pPackedData = pColumnData + ( ODSLength(_VIEW_COLUMN_FORMAT) * TableFormat.Columns);

	int ncol = TableFormat.Columns;
	int summaryIndex = 0;
    for (WORD wColumn = 0; wColumn < ncol; wColumn++) {

        // Get the fixed portion of the column descriptor and 
        //   validate the signature 
		VIEW_COLUMN_FORMAT ColumnFormat;
		temp = (BYTE*)pColumnData;
		ODSReadMemory( &temp, _VIEW_COLUMN_FORMAT, &ColumnFormat, 1);
        pColumnData += ODSLength(_VIEW_COLUMN_FORMAT);

        // Get the Item Name, and advance data pointer past it
		jstring colName = LMBCStoJavaString(env,(const char*)pPackedData,ColumnFormat.ItemNameSize);
		jstring colTitle = LMBCStoJavaString(env,(const char*)pPackedData+ColumnFormat.ItemNameSize,ColumnFormat.TitleSize);
		int colType = VIEW_COL_UNKNOWN; // VIEW_COL_NUMBER, VIEW_COL_TIMEDATE, VIEW_COL_TEXT??
		//int colType = ColumnFormat.FormatDataType;

		//col.colName.set((const char*)pPackedData, (size_t)pColumnFormat->ItemNameSize);
		//col.colTitle.set((const char*)pPackedData+pColumnFormat->ItemNameSize, (size_t)pColumnFormat->TitleSize);
		JStringUTF8 _colName(env,colName); JStringUTF8 _colTitle(env,colTitle);

		// Compute the position of the column in the buffer
		int si;
		if(ColumnFormat.ConstantValueSize) {
			si = VIEWCOL_INVALID;
			//jprintln("Col-cste[{0}], index={1}, colType={2}",colName,si,colType);
		} else {
			si = summaryIndex++;
			//jprintln("Col-normal[{0}], index={1}, colType={2}",colName,si,colType);
		}

		// Create the java column.
		jobject jniColumn = DomSQL::createViewColumn(env,jniView,colName,colTitle,colType,si);

        pPackedData += ColumnFormat.ItemNameSize + ColumnFormat.TitleSize + ColumnFormat.FormulaSize + ColumnFormat.ConstantValueSize;
	}


	// Get the collation items
	// there is one item in the note per collation
	// the item are named
	//    $Collation
	//    $Collation1
	//    ...
	//    $Collation>n>
	for(int collIdx=0; ; collIdx++) {
		// Compose the collation item name
		char sCollationName[16];
		STRCPY(sCollationName,VIEW_COLLATION_ITEM);
		if(collIdx>0) {
			_itoa(collIdx,sCollationName+strlen(VIEW_COLLATION_ITEM),10);
		}

		BLOCKID CollationBlockID;
		int error = NSFItemInfo(hNote,
						sCollationName,
						(WORD)strlen(sCollationName),
						NULL,
						&wDataType,
						&CollationBlockID,
						&dwLength);
		if (ERR(error) == ERR_ITEM_NOT_FOUND) {
			// No more collation - break!
			//jprintln("No collation #{0} - break!",collIdx);
			break;			
		} else if ( error || wDataType != TYPE_COLLATION ) {
			return NULL;
		}
		//jprintln("Collation #{0} found, item={1}",collIdx,sCollationName);


		UCHAR* pData = (UCHAR *)OSLockBlock(char, CollationBlockID);
		BLOCKID_ CollationBlockID_(CollationBlockID);
		pData += sizeof(WORD);

		COLLATION Collation;
		ODSReadMemory( &pData, _COLLATION, &Collation, 1);

		if ( Collation.signature != COLLATION_SIGNATURE ) {
			return NULL;
		}

		bool bUniqueIndex = false;
		if ( Collation.Flags == COLLATION_FLAG_UNIQUE ) {
			bUniqueIndex = true;
		}

		// Read the collation item ( which includes the view format header )
		if ( Collation.Items>0 && isValidCollationKeyType(Collation.Items,(COLLATE_DESCRIPTOR*)pData) ){		
			// Ok, the Index is valid - Create the index
			jobject jniIndex = DomSQL::createViewIndex(env,jniView,collIdx);

			// Save the pointer to the first collate descriptor 
			COLLATE_DESCRIPTOR* pCollateDesc = (COLLATE_DESCRIPTOR*)pData;

			// Get a pointer to the packed data of the collation item, 
			// which is located after the collate descriptor structures
			UCHAR* pCollationData = pData + (ODSLength(_COLLATE_DESCRIPTOR) * Collation.Items);			
			for (int wColumn = 0; wColumn<Collation.Items; wColumn++) {
				COLLATE_DESCRIPTOR CollateDesc;
				ODSReadMemory( &pCollateDesc, _COLLATE_DESCRIPTOR, &CollateDesc, 1);
				if ( CollateDesc.signature != COLLATE_DESCRIPTOR_SIGNATURE ) {
					return NULL;
				}

				// Get the sort Item Name, and advance data pointer past it
				jstring colName = LMBCStoJavaString(env,(const char*)pCollationData,CollateDesc.NameLength);

				// Find collation order
				jboolean desc = CollateDesc.Flags & CDF_M_descending;

				// Create the index entry
				jobject jniIndexEntry = DomSQL::createViewIndexEntry(env,jniIndex,colName,desc);

				// Go to the next collate descriptor structure 
				pCollationData += CollateDesc.NameLength;
			} // End of for loop for collation descriptor

		} // End if collation has sort keys 

	}

	// Return the view...
	return jniView;
}



// ===========================================================================================
// Read the database XML definition
// This is used to read the .domsql files
// ===========================================================================================

extern "C" JNIEXPORT jint JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_DomSQL_findDomsql(
        JNIEnv *env, jclass clazz, jlong _hDb, jstring _name) 
{
	DBHANDLE hDb = (DBHANDLE)_hDb;
	JStringLMBCS name(env,_name);

	// Open the collection of design elements
	HCOLLECTION hCollection;
	if(!NCheck(env,  NIFOpenCollection(
				hDb, 
			    hDb, 
				NOTE_ID_SPECIAL+NOTE_CLASS_DESIGN, 
			    OPEN_DO_NOT_CREATE,
			    NULLHANDLE,
			    &hCollection,
			    NULL, NULL, NULL, NULL))) {
		return 0;
	}

	jint resultId = 0;

	COLLECTIONPOSITION CollPosition;
	CollPosition.Level = 0;
	CollPosition.Tumbler[0] = 1; 

	WORD wSignalFlag;
	do {
		// Can we locate the design element using an idenx here, instead of browsing the collection?
		// Anyway, this is cached by the JDBS layer, so it might no be a big issue
		DHANDLE hBuffer; DWORD dwEntriesFound;
		if (!NCheck(env,NIFReadEntries( 
					hCollection, 
					&CollPosition, 
                    NAVIGATE_NEXT, 
					1L, 
					NAVIGATE_NEXT, 
					MAXDWORD,
					READ_MASK_NOTEID+READ_MASK_NOTECLASS+READ_MASK_SUMMARY, 
					&hBuffer, 
					NULL, 
					NULL,
                    &dwEntriesFound, 
					&wSignalFlag))) {
            break;
        }
        if (hBuffer == NULLHANDLE || dwEntriesFound==0) {
            break;
        }

		BYTE* pBuffer = (BYTE*) OSLockObject (hBuffer);
        for (int i=0; i<dwEntriesFound; i++) {
			NOTEID noteId = *(NOTEID*)pBuffer;
			pBuffer += sizeof(DWORD);

			WORD wClass = *(WORD*) pBuffer;
			pBuffer += sizeof (WORD);

			ITEM_TABLE* ItemTable = (ITEM_TABLE*)pBuffer;
			pBuffer += ItemTable->Length;          

			// Get the design flags
			char* pFlags; WORD flagsLength, flagsType;
			if (!NSFLocateSummaryValue(ItemTable, 
									   DESIGN_FLAGS,
									   &pFlags, 
									   &flagsLength, 
									   &flagsType)) {
				continue; // Not interesting, no flags...
			}

			char* pTitle; WORD titleLength, titleType;
			if (!NSFLocateSummaryValue(ItemTable, 
									   ITEM_NAME_TEMPLATE_NAME,
									   &pTitle, 
									   &titleLength, 
									   &titleType)) {
				continue; // Not interesting, no title...
			}
			//char flg[256]; memcpy(flg,pFlags,flagsLength); flg[flagsLength]=0;
			//char tst[256]; memcpy(tst,pTitle,titleLength); tst[titleLength]=0;
			//jprintln("Design Element: '{0}', Flags: '{1}'",tst,flg);

			// Check if this is the desired design element
			if(memicmp(name.getLMBCS(),pTitle,titleLength)==0) {
				//jprintln("FOUND Design Element: '{0}', Flags: '{1}'",tst,flg);
				resultId =  (jint)noteId;
				goto end;
			}
		}
        OSUnlockObject (hBuffer);

        OSMemFree (hBuffer);
    }  while (wSignalFlag & SIGNAL_MORE_TO_DO);

end:
    NIFCloseCollection(hCollection); 
	return resultId;
}

extern "C" JNIEXPORT jstring JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_DomSQL_readItemAsString(
			JNIEnv *env, jclass clazz,
			jlong jNote, jstring itemName) {
	NOTEHANDLE hNote = (NOTEHANDLE)jNote;

	JStringLMBCS fName(env,itemName);

	// The data is contained in an item called ITEM_NAME_FILE_DATA
	// Read the information about this item
	BLOCKID bhItem;
	WORD wItemType;
	BLOCKID bhNextValue;
	DWORD dwItemSize;
	if(!NCheck(env,NSFItemInfo(hNote, fName.getLMBCS(), fName.length(),&bhItem, &wItemType, &bhNextValue, &dwItemSize))) {
		//jniEnv.ThrowError( "File access error: Error while getting information on item {0}", fName );
		return NULL;
	}

	// Next, read the data for this item
	BYTE* pData = OSLockBlock(BYTE, bhNextValue);
	DWORD bytesRead = 0;

	WORD wType;
	ODSReadMemory(&pData, _WORD, &wType, 1);
	bytesRead += sizeof(WORD);

	CDFILEHEADER hdr;
	ODSReadMemory(&pData, _CDFILEHEADER, &hdr, 1 );
	bytesRead += hdr.Header.Length;

	StringBuilder buffer;
	for(unsigned int i=0; i<hdr.SegCount; i++) {
		if (bytesRead >= dwItemSize) {
			OSUnlockBlock(bhNextValue);
			NSFItemInfoNext(hNote, bhItem, fName.getLMBCS(), fName.length(), &bhItem, &wItemType, &bhNextValue, &dwItemSize);
			pData = OSLockBlock(BYTE, bhNextValue);
			ODSReadMemory(&pData, _WORD, &wType, 1);
			bytesRead = sizeof(WORD);
		}

		BYTE* pRecord = pData; // Save pointer for header read

		CDFILESEGMENT seg;
		ODSReadMemory(&pData, _CDFILESEGMENT, &seg, 1);

		//jniEnv.trace("Write bytes, size={0}, segSize={1}",seg.DataSize,seg.SegSize);
		buffer.append((const char*)pData,seg.DataSize);

		bytesRead += seg.Header.Length;

		// PHIL: fixed 2/10/14
		if(seg.Header.Length>seg.SegSize+sizeof(seg)) {
			pData = pRecord + seg.Header.Length;
		} else {
			pData += seg.SegSize;
		}
	}
	OSUnlockBlock(bhNextValue);

	jstring js = LMBCStoJavaString(env,(const char*)buffer.getBytes(),(int)buffer.length());
	return js;
}
