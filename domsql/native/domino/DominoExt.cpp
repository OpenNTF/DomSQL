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


/*
#include "sqlite3ext.h"
SQLITE_EXTENSION_INIT1
#if !SQLITE_CORE
int sqlite3_extension_init(sqlite3 *db, char **pzErrMsg,
                           const sqlite3_api_routines *pApi){
  SQLITE_EXTENSION_INIT2(pApi)

	TRACE("Loading ext");


  return sqlite3DomSqlInit(db);
}
#endif
*/



// ===========================================================================================
// Driver implementation constants
// ===========================================================================================

#define ENABLE_USE_INDEX_SEARCH		1
#define ENABLE_USE_INDEX_ORDERBY	1
#define DATE_AS_STRING				1

const int MAX_KEY_BUFFER_LENGTH = 1024;


// ===========================================================================================
// Notes Utilities
// ===========================================================================================

static jchar HEXCHARS[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
static void hexstr(jchar* s, DWORD n) {
	s[0] = HEXCHARS[ (n>>28) & 0x0F];
	s[1] = HEXCHARS[ (n>>24) & 0x0F];
	s[2] = HEXCHARS[ (n>>20) & 0x0F];
	s[3] = HEXCHARS[ (n>>16) & 0x0F];
	s[4] = HEXCHARS[ (n>>12) & 0x0F];
	s[5] = HEXCHARS[ (n>>8) & 0x0F];
	s[6] = HEXCHARS[ (n>>4) & 0x0F];
	s[7] = HEXCHARS[ (n>>0) & 0x0F];
}

//						 '0','1','2','3','4','5','6','7','8','9',':',';','<','=','>','?','@','A','B','C','D','E','F'
static DWORD HEXVALS[] = {0,1,2,3,4,5,6,7,8,9,0,0,0,0,0,0,0,10,11,12,13,14,15};
static DWORD strhex(const jchar* s, DWORD& d) {
	d  = (HEXVALS[s[0]-'0']<<28);
	d += (HEXVALS[s[1]-'0']<<24);
	d += (HEXVALS[s[2]-'0']<<20);
	d += (HEXVALS[s[3]-'0']<<16);
	d += (HEXVALS[s[4]-'0']<<12);
	d += (HEXVALS[s[5]-'0']<<8);
	d += (HEXVALS[s[6]-'0']<<4);
	d += (HEXVALS[s[7]-'0']<<0);
	return d;
}

static void UNIDToString(jchar* s, const UNID& aUNID) {
	hexstr( s, aUNID.File.Innards[1] );
	hexstr( s+8, aUNID.File.Innards[0] );
	hexstr( s+16, aUNID.Note.Innards[1] );
	hexstr( s+24, aUNID.Note.Innards[0] );
}

static bool StringToUNID(const jchar* s, UNID& aUNID) {
	for(int i=0; i<32; i++) {
		char c = s[i];
		if( !( (c>='0'&&c<='9')||(c>='A'&&c<='F') ) ) {
			return false;
		}
	}
	strhex( s, aUNID.File.Innards[1] );
	strhex( s+8, aUNID.File.Innards[0] );
	strhex( s+16, aUNID.Note.Innards[1] );
	strhex( s+24, aUNID.Note.Innards[0] );
	return true;
}

static void COLLECTIONPOSITIONToString(StringBuilder& b, COLLECTIONPOSITION& pos) {
	for(int i=0; i<=pos.Level; i++) {
		if(i>0) {
			b.append('.');
		}
		char buf[16];
		itoa(pos.Tumbler[i],buf,10);
		b.append(buf);
	}
}

WORD LMBCStoUTF16(const char *str, WORD len, jchar* unicodeBuffer, WORD unicodeBufferSize) {
	if(str) {
		len = len==MAXWORD ? (WORD)strlen(str) : len;
		return OSTranslate(OS_TRANSLATE_LMBCS_TO_UNICODE,str,len,(char *)unicodeBuffer,unicodeBufferSize);
	}

	return 0;
}

WORD UTF16toLMBCS(const jchar *unicodeBuffer, WORD len, char* str, WORD strSize) {
	if(str) {
		len = len==MAXWORD ? (WORD)jstrlen(unicodeBuffer) : len;
		return OSTranslate(OS_TRANSLATE_UNICODE_TO_LMBCS,(const char*)unicodeBuffer,len*2,str,strSize);
	}

	return 0;
}


void toString2(char*& ptr, int value) {
	*(ptr++) = value/10 + '0';
	value = value % 10;
	*(ptr++) = value + '0';
}
void toString4(char*& ptr, int value) {
	*(ptr++) = value/1000 + '0';
	value = value % 1000;
	*(ptr++) = value/100 + '0';
	value = value % 100;
	toString2(ptr,value);
}
//"YYYY-MM-DD HH:MM:SS.SSS"
void toISO8601String(char* ptr, TIMEDATE& dt) {
	TIME t;
	t.GM = dt;
	::OSCurrentTimeZone(&t.zone,&t.dst);
	::TimeGMToLocalZone(&t);
	toString4(ptr,t.year);
	*(ptr++) = '-';
	toString2(ptr,t.month);
	*(ptr++) = '-';
	toString2(ptr,t.day);
	*(ptr++) = ' ';
	toString2(ptr,t.hour);
	*(ptr++) = ':';
	toString2(ptr,t.minute);
	*(ptr++) = ':';
	toString2(ptr,t.second);
	*(ptr++) = '.';
	toString2(ptr,t.hundredth);
	*(ptr++) = '0';
	*ptr = 0;
}

void toString2(jchar*& ptr, int value) {
	*(ptr++) = value/10 + '0';
	value = value % 10;
	*(ptr++) = value + '0';
}
void toString4(jchar*& ptr, int value) {
	*(ptr++) = value/1000 + '0';
	value = value % 1000;
	*(ptr++) = value/100 + '0';
	value = value % 100;
	toString2(ptr,value);
}
//"YYYY-MM-DD HH:MM:SS.SSS"
void toISO8601String(jchar* ptr, TIMEDATE& dt) {
	TIME t;
	t.GM = dt;
	::OSCurrentTimeZone(&t.zone,&t.dst);
	::TimeGMToLocalZone(&t);
	toString4(ptr,t.year);
	*(ptr++) = '-';
	toString2(ptr,t.month);
	*(ptr++) = '-';
	toString2(ptr,t.day);
	*(ptr++) = ' ';
	toString2(ptr,t.hour);
	*(ptr++) = ':';
	toString2(ptr,t.minute);
	*(ptr++) = ':';
	toString2(ptr,t.second);
	*(ptr++) = '.';
	toString2(ptr,t.hundredth);
	*(ptr++) = '0';
	*ptr = 0;
}

// ===========================================================================================
//  SQLite Domino specific objects
// ===========================================================================================

struct domino_vtable;

// Domino SQLite-Module
struct domino_module : sqlite3_module {
	jint dummy;
};


// Domino SQLite-VTable
struct domino_vtable : sqlite3_vtab {
	domino_vtable() {
	}
	virtual ~domino_vtable() {
	}
	virtual int xOpen(sqlite3_vtab_cursor **pc) = 0;	
	virtual int xBestIndex(sqlite3_index_info *pInfo) =0;
};

struct domview_vtab_cursor : sqlite3_vtab_cursor {
	domview_vtab_cursor() {
	}
	virtual ~domview_vtab_cursor() {
	}
	virtual int xEof() =0;
	virtual int xFilter(int idxNum, const char *idxStr, int argc, sqlite3_value **argv) =0;
	virtual int xNext() =0;
	virtual int xColumn(sqlite3_context *pContext, int idxCol) =0;
	char listSeparator() {
		// Hard coded
		// Should this be a SQL parameter?
		return '|';
		//return '\n';
	}
	char listRangeSeparator() {
		// Hard coded
		// Should this be a SQL parameter?
		return '|';
		//return '\n';
	}
};


// ===========================================================================================
//  View based vtable
// ===========================================================================================

// Options
const WORD OPTIONS_SEPARATEMULTI	= 1;
//const WORD OPTIONS_TYPEDCOLUMNS	= 2;

// Column type
const WORD COLTYPE_UNKNOWN		= 0;
const WORD COLTYPE_STRING		= 1;
const WORD COLTYPE_INTEGER		= 2;
const WORD COLTYPE_NUMBER		= 3;
const WORD COLTYPE_BLOB			= 4;
const WORD COLTYPE_BOOLEAN		= 5;
const WORD COLTYPE_DATE			= 6;

// Specific columns
const int COLIDX_ID				= -1;
const int COLIDX_UNID			= -2;
const int COLIDX_CLASS			= -3; 
const int COLIDX_SIBLINGS		= -4; 
const int COLIDX_CHILDREN		= -5; 
const int COLIDX_DESCENDANTS	= -6; 
const int COLIDX_ANYUNREAD		= -7; 
const int COLIDX_LEVELS			= -8; 
const int COLIDX_SCORE			= -9; 
const int COLIDX_UNREAD			= -10; 
const int COLIDX_POSITION		= -11; 
const int COLIDX_ROWID			= -12; 

// Special COLLATIONs
//const WORD INDEX_ID				= 0xFF00;
//const WORD INDEX_UNID			= 0xFF01;
//const WORD INDEX_SPECIAL		= INDEX_ID;


struct domino_vtab_view;
struct domino_vtab_view_column;
struct domino_vtab_view_index;
struct domino_vtab_view_index_entry;

struct domino_vtab_view_column {

	MemString					colName;	// Column Name

	int							summaryIndex;		// Position in the view
	WORD						colType;	// SQLite column type
	
	domino_vtab_view_column() {
	}
	~domino_vtab_view_column() {
	}
	void dump(domino_vtab_view* view);
};

struct domino_vtab_view_index_entry {

	WORD						colIdx;		// Column table definition (not the position in the domino view!)
	bool						desc;		// SQLite column direction
	
	domino_vtab_view_index_entry() {
	}
	void dump(domino_vtab_view* view, domino_vtab_view_index* index);
};

struct domino_vtab_view_index {

	WORD							collation;
	WORD							entryCount;
	domino_vtab_view_index_entry*	entries;
	
	domino_vtab_view_index() : entries(NULL) {
	}
	~domino_vtab_view_index() {
		if(entries) {
			delete[] entries;
		}
	}
	void dump(domino_vtab_view* view);
};

struct domino_vtab_view : domino_vtable {
	MemString					viewName;	// For debugging purposes
	MemString					tableName;	// For debugging purposes

	NOTEID						noteID;		// View note ID
	JNIGlobalRef<jstring>		dbPath;		// Database path
	WORD						colCount;	// Number of columns
	domino_vtab_view_column*	columns;	// Definition of the columns
	WORD						idxCount;	// Number of indexes
	domino_vtab_view_index*		indexes;	// Definition of the indexes
	DWORD						returnMask;	// return value mask for the cursors, computed based on the columns
	DWORD						options;	// View options
	domino_vtab_view(NOTEID noteID, jstring dbPath, DWORD options, DWORD returnMask, WORD colCount, domino_vtab_view_column* columns, WORD idxCount, domino_vtab_view_index* indexes) 
		: noteID(noteID), dbPath(dbPath), options(options), returnMask(returnMask), colCount(colCount), columns(columns), idxCount(idxCount), indexes(indexes) {
	}
	~domino_vtab_view() {
		delete[] columns;
		delete[] indexes;
	}
	virtual DBHANDLE getDatabaseHandle(JNIEnv* env);
	virtual int xOpen(sqlite3_vtab_cursor **pc);
	virtual int xBestIndex(sqlite3_index_info *pInfo);
	int bestIndex(sqlite3_index_info *pInfo);
	bool indexMatchesOrderBy(sqlite3_index_info *pInfo, int idxNum);
	bool hasEqualConstraint(sqlite3_index_info *pInfo, int idxNum);

	void displayConstraints(sqlite3_index_info *pInfo);
	void displayOrderBy(sqlite3_index_info *pInfo);

	void dump() {
		jprintln("************************************");
		jprintln("Dumping Virtual Table: View");
		jprintln("Database path: {0}",(jstring)dbPath);
		jprintln("View Name: {0}",viewName);
		jprintln("Table Name: {0}",tableName);
		jprintln("NoteID: {0}",noteID);
		jprintln("Columns[{0}]",colCount);
		for(int i=0; i<colCount; i++) {
			jprintln("  #[{0}]",i);
			columns[i].dump(this);
		}
		for(int i=0; i<idxCount; i++) {
			jprintln("  #[{0}]",i);
			indexes[i].dump(this);
		}
	}
};

static const int MAX_VIEWCOLUMNS = 256;

static DWORD idCounter = 0; 
struct domview_vtab_view_cursor : domview_vtab_cursor {
	DWORD				cursorID;		// For debugging purposes
	domino_vtab_view*	view;
	// Collection handles
	DBHANDLE			hDB;
	HCOLLECTION			hCollection;
	int					collectionSize;
	int					currentCollation;
	// Collection entry reading
	COLLECTIONPOSITION	CollPosition;  /* position within collection */
	DHANDLE				hBuffer;       /* handle to buffer of note ids */
	DWORD				cachedEntries; /* number of entries found */
	BOOL				eof;		   /* EOF found by xNext */
	WORD				SignalFlag;    /* signal and share warning flags */
	// Cursor method
	DWORD				entriesLeft;   /* number of entries left to read */
	DWORD				currentEntry;  /* current entry # being processed */
	BYTE*				bufferPtr;
	bool				stopAtPosition;/* indicates if the search should stop at this position */

	// Handling multiple fields, when exploded into multiple rows
	int					rowCount;	   /* Max number of multiple fields for the current row */
	int					rowId;         /* Current row for multiple fields */

	DWORD*				noteId;
	UNIVERSALNOTEID*	unid;
	WORD*				noteClass;
	DWORD*				indexSiblings;
	DWORD*				indexChildren;
	DWORD*				indexDescendants;
	WORD*				indexAnyUnread;
	WORD*				indentLevels;
	WORD*				score;
	WORD*				indexUnread;
	COLLECTIONSTATS*	collectionStats;
	COLLECTIONPOSITION*	indexPosition;
	ITEM_VALUE_TABLE*	itemValueTable;
	BYTE*				itemValuePtr[MAX_VIEWCOLUMNS];
	int					itemValuePtrLength;
	ITEM_TABLE*			itemTable;

	// Buffer for holding temporary string conversion
	ByteBuffer			columnBuffer;

	domview_vtab_view_cursor(domino_vtab_view* view, DBHANDLE hdb, HCOLLECTION hcol) {
		this->cursorID = idCounter++;
		this->view = view;
		this->hDB = hdb;
		this->hCollection = hcol;
		this->currentCollation = 0;
		this->hBuffer = NULL;
		// Calculate the ~# of entries in the collection for the optimizer
		// TODO: is there a faster way?
		DHANDLE hCollData;
		if(NIFGetCollectionData(hCollection,&hCollData)==NOERROR) {
			COLLECTIONDATA* pData = OSLock(COLLECTIONDATA,hCollData);
			this->collectionSize = pData->DocCount; // Only consider the docs?
			OSUnlock(hCollData);
		} else {
			this->collectionSize = 1000; // Random...
		}
	}
	~domview_vtab_view_cursor() {
		if(hBuffer) {
			OSUnlockObject(hBuffer); 
			OSMemFree(hBuffer);
		}
		if(hCollection) {
			NIFCloseCollection(hCollection);
		}
	}
	virtual int xEof();
	virtual int xFilter(int idxNum, const char *idxStr, int argc, sqlite3_value **argv);
	virtual int xNext();
	virtual int xColumn(sqlite3_context *pContext, int idxCol);

	int findByKey(domino_vtab_view_index& index, int argc, sqlite3_value **argv);
	int getDominoColumnType(sqlite3_value* v);
	int xNext(bool first, DWORD skip);
	void readSummaryData(int returnMask);
};


void domino_vtab_view_column::dump(domino_vtab_view* view) {
	jprintln("    Name: {0}",colName);
	jprintln("    Summary Index: {0}",summaryIndex);
	jprintln("    Type: {0}",colType);
}

void domino_vtab_view_index_entry::dump(domino_vtab_view* view, domino_vtab_view_index* index) {
	const char* cname = view->columns[colIdx].colName;
	jprintln("        Index: {0} [{1}]",colIdx,cname);
	jprintln("        Type: {0}",desc?"true":"false");
}

void domino_vtab_view_index::dump(domino_vtab_view* view) {
	jprintln("    Collation: {0}",collation);
	jprintln("    Entries[{0}]",entryCount);
	for(int i=0; i<entryCount; i++) {
		jprintln("      #[{0}]",i);
		entries[i].dump(view,this);
	}
}

DBHANDLE domino_vtab_view::getDatabaseHandle(JNIEnv* env) {
	return (DBHANDLE)DomSQL::getDatabaseHandle(env,dbPath);
}

int domino_vtab_view::xOpen(sqlite3_vtab_cursor **pc) {
	int error;

	JNIEnv* env = JNIUtils::getJNIEnv();

	// Open the database
	DBHANDLE hDB = getDatabaseHandle(env);
	if(env->ExceptionOccurred()) {
		jprintln("domino_vtab_view::getDatabaseHandle() generated an exception, dbPath={0}",(jstring)dbPath);
		return SQLITE_ERROR;
	}
	if(!hDB) {
		jprintln("domino_vtab_view::getDatabaseHandle() returned null, dbPath={0}",(jstring)dbPath);
		return SQLITE_ERROR;
	}

	// And open the collection
	HCOLLECTION hCollection;
	if (error = NIFOpenCollection(
            hDB,						/* handle of db with view */
            hDB,						/* handle of db with data */
            noteID,						/* note id of the view */
			0,						    /* collection open flags */
            NULLHANDLE,					/* handle to unread ID list (input and return) */
            &hCollection,				/* collection handle (return) */
            NULLHANDLE,					/* handle to open view note (return) */
            NULL,						/* universal note id of view (return) */
            NULLHANDLE,					/* handle to collapsed list (return) */
            NULLHANDLE))				/* handle to selected list (return) */
		{
			NPrintError(env,error);
			return SQLITE_ERROR;
		}

	if(DomSQL::FLAG_TRACE_NIFCALLS) {
		jprintln("NIFOpenCollection, name={0}, noteID={1}",viewName,noteID);
	}

	*pc = new domview_vtab_view_cursor(this,hDB,hCollection);
	return SQLITE_OK;
}


//
// Contraints that lead to the use of an index, in order of the best one
// 0- OrderBy constraint
// 1- ID constraints
//		noteid ==
// 2- UNID constraints
//		unid ==
// 3- Test for column equality - Can work with multiple columns, for equality
//      col1 == [col2 == ...]
// 4- Test for one column > < >= <=
//      col1 > or col1 >=  [optional col1< or col1<=]
// 5- Test for one column > < >= <=
//      col1 < or col1 <=
// 6- Test for one column match
//		col1 match

static int makeIndex(int type, int idxNum, int nCol) {
	return (type&0x000000FF) + ((idxNum<<8)&0x0000FF00) + ((nCol<<16)&0x00FF0000);
}
static int getIndexType(int idx) {
	return idx & 0x000000FF;
}
static int getIndexNum(int idx) {
	return (idx & 0x0000FF00)>>8;
}
static int getIndexCols(int idx) {
	return (idx & 0x00FF0000)>>16;
}

int domino_vtab_view::xBestIndex(sqlite3_index_info *pInfo) {
	if(DomSQL::FLAG_TRACE_BESTINDEX) {
		jprintln(">>xBestIndex, starting with Constraints/OrderBy for table {0}, nConstraints={1}",tableName,pInfo->nConstraint);
		displayConstraints(pInfo);
		displayOrderBy(pInfo);
	}

	int res = bestIndex(pInfo);

	if(DomSQL::FLAG_TRACE_BESTINDEX) {
		if(pInfo->idxNum>0) {
			int indexType = getIndexType(pInfo->idxNum);
			int indexNum = getIndexNum(pInfo->idxNum);
			int indexCols = getIndexCols(pInfo->idxNum);
			if(indexType==1) {
				jprintln("  xBestIndex, Uses index for ID constraint");
			} else if(indexType==2) {
				jprintln("  xBestIndex, Uses index for UNID constraint");
			} else if(indexType==3) {
				jprintln("  xBestIndex, Uses index with column equality");
				jprintln("    Collation: {0}, #col:{1}",indexNum,indexCols);
				domino_vtab_view_index& idx = indexes[indexNum];
				for(int i=0; i<indexCols; i++) {
					jprintln("      #{0}: {1}",i,idx.entries[i].colIdx);
				}
				jprintln("    Handles OrderBy: {0}",pInfo->orderByConsumed?"true":"false");
			} else if(indexType==4) {
				jprintln("  xBestIndex, Uses index for order by");
				jprintln("    Collation: {0}, #col:{1}",indexNum,indexCols);
				domino_vtab_view_index& idx = indexes[indexNum];
				for(int i=0; i<indexCols; i++) {
					jprintln("      #{0}: {1}",i,idx.entries[i].colIdx);
				}
				jprintln("    Handles OrderBy: {0}",pInfo->orderByConsumed?"true":"false");
			} else {
				jprintln("  xBestIndex, Unknown index type {0}",indexType);
			}
		} else {
			jprintln("  xBestIndex, All table scan, no matching indexes");
		}
	}

	if(DomSQL::FLAG_TRACE_PERFORMANCE_HINTS) {
		if(pInfo->idxNum<=0) {
			bool constraints = false;
			for(int i=0; i<pInfo->nConstraint; i++) {
				sqlite3_index_info::sqlite3_index_constraint& c = pInfo->aConstraint[i];
				if(c.usable) {
					constraints = true;
					break;
				}
			}
			if(constraints) {
				jprintln("!! xBestIndex, Performance improvement, view:{0}, table:{1} : Missing index for the constraints",viewName,tableName);
				displayConstraints(pInfo);
			} else  if(pInfo->nOrderBy>0 && !pInfo->orderByConsumed) {
				jprintln("!! xBestIndex, Performance improvement, view:{0}, table:{1} : Missing index for Order By",viewName,tableName);
				displayOrderBy(pInfo);
			}
		}
	}

	return res;
}

int domino_vtab_view::bestIndex(sqlite3_index_info *pInfo) {
	// By default, we use a table scan
	pInfo->idxNum = 0;
	//pInfo->orderByConsumed = false;
	//pInfo->estimatedCost = 100; // N?

	//pInfo->estimatedCost = pInfo->c->collectionSize; // N?

	// Check for a consraint
	if(pInfo->nConstraint>0) {
		if(ENABLE_USE_INDEX_SEARCH) {
			//for(int ct=0; pInfo->nConstraint; ct++) {
			//	pInfo->aConstraintUsage[ct].argvIndex = 0;
			//}
			// 1- NOTEID constraints
			//		noteid ==
			// 2- UNID constraints
			//		unid ==
			for(int i=0; i<pInfo->nConstraint; i++) {
				sqlite3_index_info::sqlite3_index_constraint& c = pInfo->aConstraint[i];
				if(!c.usable) {
					break;
				}
				if(c.op==SQLITE_INDEX_CONSTRAINT_EQ) {
					domino_vtab_view_column& col = columns[c.iColumn];
					if(col.summaryIndex==COLIDX_ID) {
						// NOTEID
						pInfo->estimatedCost = 1; 
						pInfo->idxNum = makeIndex(1,0,1);
						// There is noindex being used to sort the view
						pInfo->orderByConsumed = false;
						break;
					} else if(col.summaryIndex==COLIDX_UNID) {
						// UNID
						pInfo->estimatedCost = 1;
						pInfo->idxNum = makeIndex(2,0,1);
						// There is noindex being used to sort the view
						pInfo->orderByConsumed = false;
						break;
					}
				}
			}


			// 3- Test for column equality - Can work with multiple columns, for equality
			//      col1 == [col2 == ...]
			// We find the first index that matches the constraint and that also matches
			// the order by
			if(pInfo->idxNum<=0) { // Uniquely if a NoteId/UNID hasn't been found yet
				int usedConstraints[16];
				for(int ii=0; ii<idxCount; ii++) {
					domino_vtab_view_index& idx = indexes[ii];
					int colMatched = 0;
					int colConstraints[16];
					for( int ci=0; ci<idx.entryCount; ci++) {
						domino_vtab_view_index_entry& col = idx.entries[ci];
				
						// Look if there is a valid constraint for this column
						for(int ct=0; ct<pInfo->nConstraint; ct++) {
							sqlite3_index_info::sqlite3_index_constraint& c = pInfo->aConstraint[ct];
							if(!c.usable) {
								break;
							}

							//int viewPos = columns[c.iColumn].summaryIndex;
							int viewPos = c.iColumn;
							if(c.usable && viewPos==col.colIdx) {
								// Ok, on the '==' operator is ok for now
								if(c.op==SQLITE_INDEX_CONSTRAINT_EQ) {
									colConstraints[colMatched] = ct;
									colMatched++;
								} else {
									ci = idx.entryCount; // break inner loop
									break; // And break the current loop
								}
							}
						}
					}

					// If we found an index, then use it
					if(colMatched>0) {
						pInfo->estimatedCost = 10; 
						pInfo->idxNum = makeIndex(3,ii,colMatched);
						memmove(usedConstraints,colConstraints,sizeof(usedConstraints));
						// Moreover, if the index matches the orderby constraint, then this is the one we want!
						if(pInfo->nOrderBy) {
							if(indexMatchesOrderBy(pInfo,ii)) {
								if(ENABLE_USE_INDEX_ORDERBY) {
									// Ok, the index also matches the order by clause, which is the best we can do
									// So we mark it as sorted and we use this index
									pInfo->orderByConsumed = true;
								} else {
									if(DomSQL::FLAG_TRACE_BESTINDEX) {
										jprintln("  xBestIndex: Not enabled for OrderBy, table {0}",tableName);
									}
								}
								break;
							}
						}
					}
				}
			}
		} else {
			if(DomSQL::FLAG_TRACE_BESTINDEX) {
				jprintln("  xBestIndex: Not enabled for contraints, table {0}",tableName);
			}
		}
	}

	// If the index was found, but without a matching order by, then we return it now
	if(pInfo->idxNum>0) {
		// Set the constraints that we need as arguments to 
		int nCol = getIndexCols(pInfo->idxNum);

		for(int ct=0; ct<nCol; ct++) {
			pInfo->aConstraintUsage[ct].argvIndex = ct+1;
			//pInfo->aConstraintUsage[ct].omit = true;
		}

		// We should now notify SQLIte that we need some columns
		return SQLITE_OK;
	}

/*
	// 4-  >  and <
	// 5-  >  and <=
	// 6-  >= and <
	// 7-  >= and <=
	// 8-  >
	// 9-  >=
	// 10- <
	// 11- <=
	// TODO...

	// If an index is found, then fill the output portion of the struture
	bool indexForOrderBy = false;
	if(pInfo->idxNum>=0) {
		int idxFound = getIndexNum(pInfo->idxNum);
		// NOTEID & UNID do not required any parameter to be passed
		if(idxFound>2) {
			int idxMatch = getIndexCols(pInfo->idxNum);
			domino_vtab_view_index& idx = indexes[idxFound];
			for( int ci=0; ci<idxMatch; ci++) {
				domino_vtab_view_index_entry& col = idx.entries[ci];

				// 
				
				// Look for the valid constraint for this column
				for(int ct=0; ct<pInfo->nConstraint; ct++) {
					sqlite3_index_info::sqlite3_index_constraint& c = pInfo->aConstraint[ct];
					//int viewPos = columns[c.iColumn].summaryIndex;
					int viewPos = c.iColumn;
					if(c.usable && viewPos==col.colIdx) {
						// Ok, on the '==' operator is ok for now
						if(c.op==SQLITE_INDEX_CONSTRAINT_EQ) {
							pInfo->aConstraintUsage[ct].argvIndex = ci+1;
							TRACE2("  aConstraintUsage[{0}]={1}",ct,ci+1);
						}
					}
				}
			}

			// In case it asks for an order by, we verify that the index matches it
			if(idx.entryCount>=pInfo->nOrderBy) {
				//TRACE("  Checking for order by {0}, nidx={1}",pInfo->nOrderBy,viewDefinition->nidx);
				bool matchesOrderBy = true;
				for(int i=0; i<pInfo->nOrderBy; i++) {
					domino_vtab_view_index_entry& col = idx.entries[i];
					int viewPos = columns[pInfo->aOrderBy[i].iColumn].summaryIndex;
					if(col.colIdx!=viewPos || (col.desc!=(bool)pInfo->aOrderBy[i].desc)) {
						matchesOrderBy = false;
						break;
					}
				}
				// If the index matches, use it
				if(matchesOrderBy) {
					pInfo->orderByConsumed = true;
				}
			}

			pInfo->estimatedCost = 10; // log(N)
		} else if(idxFound==INDEX_ID) {
			pInfo->aConstraintUsage[firstConstraint].argvIndex = firstConstraint+1;
			pInfo->estimatedCost = 1;
		} else if(idxFound==INDEX_UNID) {
			pInfo->aConstraintUsage[firstConstraint].argvIndex = firstConstraint+1;
			pInfo->estimatedCost = 1;
		}
	} else {
		// If there is no selected for the constraints, then look if one is available for order by
		if(pInfo->nOrderBy) {
			TRACE2("  Checking for order by {0}, nidx={1}",pInfo->nOrderBy,idxCount);
			int idxForOrderBy = -1;
			for(int ii=0; ii<idxCount; ii++) {
				domino_vtab_view_index& idx = indexes[ii];
				if(idx.entryCount>=pInfo->nOrderBy) {
					int idxForOrderBy = ii;
					for(int i=0; i<pInfo->nOrderBy; i++) {
						domino_vtab_view_index_entry& col = idx.entries[i];
						int viewPos = pInfo->aOrderBy[i].iColumn;
						if(col.colIdx!=viewPos || (col.desc!=(bool)pInfo->aOrderBy[i].desc)) {
							idxForOrderBy = -1; // Invalid index...
							break;
						}
					}
					// If an index is found, use it and do not search for another one...
					if(idxForOrderBy>=0) {
						pInfo->idxNum = ii;
						pInfo->orderByConsumed = true;
						break;
					}
				}
			}
		}
	}
*/

	// If there is no selected for the constraints, then look if one is available for order by
	if(pInfo->nOrderBy) {
		if(DomSQL::FLAG_TRACE_BESTINDEX) {
			jprintln("  xBestIndex: Checking for simple OrderBy, table {0}",tableName);
		}
		if(ENABLE_USE_INDEX_ORDERBY) {
			int idxForOrderBy = -1;
			for(int ii=0; ii<idxCount; ii++) {
				domino_vtab_view_index& idx = indexes[ii];
				if(idx.entryCount>=pInfo->nOrderBy) {
					int idxForOrderBy = ii;
					for(int i=0; i<pInfo->nOrderBy; i++) {
						domino_vtab_view_index_entry& col = idx.entries[i];
						int viewPos = pInfo->aOrderBy[i].iColumn;
						if(col.colIdx!=viewPos || (col.desc!=(bool)pInfo->aOrderBy[i].desc)) {
							idxForOrderBy = -1; // Invalid index...
							break;
						}
					}
					// If an index is found, use it and do not search for another one...
					if(idxForOrderBy>=0) {
						pInfo->idxNum = makeIndex(4,idxForOrderBy,0);
						pInfo->orderByConsumed = true;
						break;
					}
				}
			}
		} else {
			if(DomSQL::FLAG_TRACE_BESTINDEX) {
				jprintln("  xBestIndex: Not enabled for contraints, table {0}",tableName);
			}
		}
	}

	return SQLITE_OK;
}

// Check if an index matches the order by constraint
bool domino_vtab_view::indexMatchesOrderBy(sqlite3_index_info *pInfo, int idxNum) {
	domino_vtab_view_index& idx = indexes[idxNum];
	if(idx.entryCount>=pInfo->nOrderBy) {
		int idxPtr = 0;
		for(int i=0; i<pInfo->nOrderBy; i++) {
			int orderByColIdx = columns[pInfo->aOrderBy[i].iColumn].summaryIndex;
			domino_vtab_view_index_entry& col = idx.entries[idxPtr];
			// If the index currently matches it
			if(col.colIdx==orderByColIdx && (col.desc==(bool)pInfo->aOrderBy[i].desc)) {
				continue;
			}
			// If not, and if we have an == constraint on the column that we want to sort by
			// then we can safely skip it
			if(hasEqualConstraint(pInfo,orderByColIdx)) {
				continue;
			}
			// Ok, it doesn't match
			return false;
		}
		return true;
	}
	return false;
}
bool domino_vtab_view::hasEqualConstraint(sqlite3_index_info *pInfo, int colIdx) {
	for(int i=0; i<pInfo->nConstraint; i++) {
		sqlite3_index_info::sqlite3_index_constraint& c = pInfo->aConstraint[i];
		if(c.op==SQLITE_INDEX_CONSTRAINT_EQ) {
			domino_vtab_view_column& col = columns[c.iColumn];
			if(col.summaryIndex==colIdx) {
				return true;
			}
		}
	}
	return false;
}

void domino_vtab_view::displayConstraints(sqlite3_index_info *pInfo) {
	for(int ii=0; ii<pInfo->nConstraint; ii++) {
		sqlite3_index_info::sqlite3_index_constraint& c = pInfo->aConstraint[ii];
		const char* op = "?";
		switch(c.op) {
			case SQLITE_INDEX_CONSTRAINT_EQ:		op="==";	break;
			case SQLITE_INDEX_CONSTRAINT_GT:		op=">";		break;
			case SQLITE_INDEX_CONSTRAINT_LE:		op="<=";	break;
			case SQLITE_INDEX_CONSTRAINT_LT:		op="<";		break;
			case SQLITE_INDEX_CONSTRAINT_GE:		op=">=";	break;
			case SQLITE_INDEX_CONSTRAINT_MATCH:		op="match";	break;
		}
		jprintln("    Column[{0},{1}] {2}, usable={3}",c.iColumn, this->columns[c.iColumn].colName, op, c.usable?"true":"false");
	}
}
void domino_vtab_view::displayOrderBy(sqlite3_index_info *pInfo) {
	if(pInfo->nOrderBy>0) {
		StringBuilder b;
		for(int ii=0; ii<pInfo->nOrderBy; ii++) {
			sqlite3_index_info::sqlite3_index_orderby& o = pInfo->aOrderBy[ii];
			if(ii>0) {
				b.append(", ");
			}
			b.append(", col#");
			char cc[16]; _itoa_s(o.iColumn,cc,10);
			b.append(cc);
			b.append(" [");
			b.append(this->columns[o.iColumn].colName);
			b.append("]");
			if(o.desc) {
				b.append(" DESC");
			} else {
				b.append(" ASC");
			}
		}
		jprintln("    Order By: {0}",b.toString());
	} else {
		jprintln("    No Order By");
	}
}

int domview_vtab_view_cursor::xEof() {
	// XEOF is called right after xNext so xNext might set EOF to true
	TRACE_FUNCTION("domview_vtab_view_cursor::xEof");
	if(eof) {
		if(DomSQL::FLAG_TRACE_EOF) {
			jprintln(">>xEOF=TRUE, view:{0}", view->viewName);
		}
		return TRUE;
	}
	if(DomSQL::FLAG_TRACE_EOF) {
			jprintln(">>xEOF=FALSE, view:{0}", view->viewName);
	}
	return FALSE;
}

int domview_vtab_view_cursor::xFilter(int _idxNum, const char *idxStr, int argc, sqlite3_value **argv) {
	TRACE_FUNCTION("domview_vtab_view_cursor::xFilter");
	if(DomSQL::FLAG_TRACE_XFILTER) {
		int indexType = getIndexType(_idxNum);
		int indexNum = getIndexNum(_idxNum);
		int indexCols = getIndexCols(_idxNum);
		jprintln(">>xFilter, starting indexType={0}, indexNum={1}, indexCols={2} (idxNum={3})",indexType,indexNum,indexCols,_idxNum);
	}

	int error;

	// If there was a buffer, destroy it
	// No buffer means it will be the first request
	if(hBuffer) {
		OSUnlockObject(hBuffer); 
		OSMemFree(hBuffer); 
		hBuffer=NULL;
		bufferPtr=NULL;
	}

	// Initialize the data
	currentEntry=0;
	cachedEntries=0;
	SignalFlag=0;

	if(_idxNum>0) {
		int idxType = getIndexType(_idxNum);

		// 1- NoteId
		if(idxType==1) {
			NOTEID noteID = sqlite3_value_int(argv[0]);
			if(error=NIFLocateNote(hCollection,&CollPosition,noteID)) {
				if(error!=ERR_NOT_FOUND) {
					NCheck(JNIUtils::getJNIEnv(),error);
					return SQLITE_ERROR;
				}
				if(DomSQL::FLAG_TRACE_XFILTER) {
					jprintln("  xFilter, noteid not found -> EOF!");
				}
				return SQLITE_OK;
			}
			if(DomSQL::FLAG_TRACE_XFILTER) {
				jprintln("  xFilter using NOTEID index, table {0}, id={1}",view->tableName,noteID);
			}	
			// Just read one entry...
			entriesLeft = 1;
			return xNext(true,0L);
		}

		// 2- UNID
		if(idxType==2) {
			const jchar* sUNID = (const jchar*)sqlite3_value_text16(argv[0]);
			UNID unid;
			if(!StringToUNID(sUNID,unid)) {
				if(DomSQL::FLAG_TRACE_XFILTER) {
					jprintln("  xFilter, table {0}, Invalid UNID {1} string -> EOF!",view->tableName,sUNID);
				}
				return SQLITE_OK;
			}
			NOTEID noteID; OID oid; TIMEDATE modTime; WORD clazz;
			if(error=NSFDbGetNoteInfoByUNID(hDB,&unid,&noteID,&oid,&modTime,&clazz)) {
				if(error!=ERR_NOTE_DELETED) {
					NCheck(JNIUtils::getJNIEnv(),error);
					return SQLITE_ERROR;
				}
				if(DomSQL::FLAG_TRACE_XFILTER) {
					jprintln("  xFilter, table {0}, UNID {1} not found -> EOF!",view->tableName,sUNID);
				}
				return SQLITE_OK;
			}
			if(error=NIFLocateNote(hCollection,&CollPosition,noteID)) {
				if(error!=ERR_NOT_FOUND) {
					NCheck(JNIUtils::getJNIEnv(),error);
					return SQLITE_ERROR;
				}
				if(DomSQL::FLAG_TRACE_XFILTER) {
					jprintln("  xFilter, table {0}, noteid {1} for UNID {2} not found -> EOF!", view->tableName, noteID, sUNID);
				}
				return SQLITE_OK;
			}
			if(DomSQL::FLAG_TRACE_XFILTER) {
				jprintln("  xFilter using NOTEID index, table {0}, noteid={1}, UNID={2}",view->tableName,noteID,sUNID);
			}	
			// Just read one entry...
			entriesLeft = 1;
			return xNext(true,0L);
		}

		// 3- Indexes with ==
		if(idxType==3) {
			int idxNum = getIndexNum(_idxNum);
			int idxCols = getIndexCols(_idxNum);

			domino_vtab_view_index& index = view->indexes[idxNum];
			int collation = index.collation;
			// Set the right collation, uniquely if it is necessary
			if(collation!=currentCollation) {
				if(DomSQL::FLAG_TRACE_NIFCALLS) {
					domino_vtab_view_index& idx = view->indexes[idxNum];
					StringBuilder b;
					for(int i=0; i<idx.entryCount; i++) {
						if(i>0) {
							b.append(',');
						}
						b.append(view->columns[idx.entries[i].colIdx].colName);
					}
					jprintln("  NIFSetCollation {0}, {1}",collation,b.toString());
				}
				if(error=NIFSetCollation(hCollection,collation)) {
					return SQLITE_ERROR;
				}
				this->currentCollation = collation;
			}
			if(DomSQL::FLAG_TRACE_XFILTER) {
				domino_vtab_view_index& idx = view->indexes[idxNum];
				jprintln("  xFilter using index {0}, table {1} for == constraint",idxNum,view->tableName);
				jprintln("    * Index[{0}]",idxNum);
				for(int i=0; i<idxCols; i++) {
					jprintln("      col[{0}]={1} ({2})",i,idx.entries[i].colIdx,(const char*)view->columns[idx.entries[i].colIdx].colName);
				}
				jprintln("    * Parameters[{0}]",argc);
				for(int i=0; i<idxCols; i++) {
					jprintln("      argv[{0}]={1}",i,(const char*)sqlite3_value_text(argv[i]));
				}
			}

			if(argc>=1) {
				domino_vtab_view_index& index = view->indexes[idxNum];
				int result = findByKey(index,argc,argv);
				if(result==SQLITE_ERROR) {
					return SQLITE_ERROR;
				}
				if(result==SQLITE_DONE) {
					// Ok, but no rows found
					return SQLITE_OK;
				}
				return xNext(true,0L);
			}
		}

		// 4- Indexes for a simple order by
		if(idxType==4) {
			int idxNum = getIndexNum(_idxNum);

			domino_vtab_view_index& index = view->indexes[idxNum];
			int collation = index.collation;
			// Set the right collation, uniquely if it is necessary
			if(collation!=currentCollation) {
				if(DomSQL::FLAG_TRACE_NIFCALLS) {
					domino_vtab_view_index& idx = view->indexes[idxNum];
					StringBuilder b;
					for(int i=0; i<idx.entryCount; i++) {
						if(i>0) {
							b.append(',');
						}
						b.append(view->columns[idx.entries[i].colIdx].colName);
					}
					jprintln("  NIFSetCollation {0}, {1}",collation,b.toString());
				}
				if(error=NIFSetCollation(hCollection,collation)) {
					return SQLITE_ERROR;
				}
				this->currentCollation = collation;
			}
			if(DomSQL::FLAG_TRACE_XFILTER) {
				domino_vtab_view_index& idx = view->indexes[idxNum];
				jprintln("  xFilter using index {0}, table {1} for order by",idxNum,view->tableName);
				jprintln("      * Index[{0}]",idxNum);
			}
		}
	}

	if(DomSQL::FLAG_TRACE_XFILTER) {
		jprintln("  xFilter full table scan, table {0}",view->tableName);
	}	

	// Start from the begining as a full table scan is requested
	CollPosition.Level = 0;
	CollPosition.Tumbler[0] = 0;
	entriesLeft = 0xFFFFFFFF; // Read all the entries
	return xNext(true,1L);
}

int domview_vtab_view_cursor::findByKey(domino_vtab_view_index& index, int argc, sqlite3_value **argv) {
	int op=SQLITE_INDEX_CONSTRAINT_EQ;

	// Build the buffer for the search
	int bufferLength = MAX_KEY_BUFFER_LENGTH;
	char buffer[MAX_KEY_BUFFER_LENGTH];

	ITEM_TABLE* tb = (ITEM_TABLE*)&buffer;
	tb->Items = min(argc,index.entryCount);

	ITEM* items = (ITEM*)((char*)tb+sizeof(ITEM_TABLE));
	char* p = (char*)items+tb->Items*sizeof(ITEM);
	for(int i=0; i<tb->Items; i++) {
		domino_vtab_view_index_entry& entry = index.entries[i];

		char* bufStart = p;

		ITEM* item = &items[i];
		item->NameLength = 0;

		//int type = view->columns[entry.colIdx].colType;
		int type = getDominoColumnType(argv[i]);
		switch(type) {
			case COLTYPE_UNKNOWN:
			case COLTYPE_STRING: {
				*(WORD*)p = TYPE_TEXT; p+=sizeof(WORD);
				const jchar* v = (const jchar*)sqlite3_value_text16(argv[i]);
				int len = v ? UTF16toLMBCS(v,MAXWORD,p,bufferLength-(p-buffer)) : 0;
				p[len]=0;
				p += len + 1;
			} break;
			case COLTYPE_INTEGER: {
				*(WORD*)p = TYPE_NUMBER; p+=sizeof(WORD);
				*(double*)p = sqlite3_value_double(argv[i]);
				p+=sizeof(double);
			} break;
			case COLTYPE_NUMBER: {
				*(WORD*)p = TYPE_NUMBER; p+=sizeof(WORD);
				*(double*)p = sqlite3_value_double(argv[i]);
				p+=sizeof(double);
			} break;
			case COLTYPE_BLOB: {
				// Unsupported for now...
				*(WORD*)p = TYPE_TEXT; p+=sizeof(WORD);
				p[0]=0;
				p += 1;
			} break;
			case COLTYPE_BOOLEAN: {
				// Unsupported for now...
				*(WORD*)p = TYPE_TEXT; p+=sizeof(WORD);
				p[0]=0;
				p += 1;
			} break;
			case COLTYPE_DATE: {
				// Unsupported for now...
				*(WORD*)p = TYPE_TEXT; p+=sizeof(WORD);
				p[0]=0;
				p += 1;
			} break;
			default: {
				if(DomSQL::FLAG_TRACE_FINDBYKEY) {
					jprintln(" findByKey, Internal error: Invalid column type {0}",type);
				}
			}
		}

		item->ValueLength = p-bufStart;
	}
	tb->Length = p-buffer;

	if(DomSQL::FLAG_TRACE_FINDBYKEY) {
		jprintln("::findByKey, ITEM_TABLE");
		jprintln("    Length: {0}",tb->Length);
		jprintln("    Items: {0}",tb->Items);
		
		ITEM* items = (ITEM*)((char*)tb+sizeof(ITEM_TABLE));
		char* p = (char*)items+tb->Items*sizeof(ITEM);
		for(int i=0; i<tb->Items; i++) {
			ITEM* item = &items[i];
			jprintln("    Item[{0}]",i);
			jprintln("        NameLength={0}",item->NameLength);
			jprintln("        ValueLength={0}",item->ValueLength);
			char* itemValue = p;
			int type = *(WORD*)itemValue; itemValue+=sizeof(WORD);
			switch(type) {
				case TYPE_TEXT: {
					jstring js = LMBCStoJavaString(JNIUtils::getJNIEnv(),itemValue);
					jprintln("        Value[STRING]={0}",js);
				} break;
				case TYPE_NUMBER: {
					jprintln("        Value[NUMBER]={0}",*(double*)itemValue);
				} break;
				default: {
					jprintln("        Unknow value type {0}",type);
				} break;
			}
			p += item->ValueLength;
		}
	}

	int error;
	if(error=NIFFindByKey(hCollection,tb,FIND_EQUAL,&CollPosition,&entriesLeft)) {
		if(error!=ERR_NOT_FOUND) {
			NCheck(JNIUtils::getJNIEnv(),error);
			return SQLITE_ERROR;
		}
		if(DomSQL::FLAG_TRACE_FINDBYKEY) {
			jprintln("findByKey, entry not found -> EOF!");
		}
		return SQLITE_DONE;
	}
	if(DomSQL::FLAG_TRACE_FINDBYKEY) {
		jprintln("findByKey, NIFFindByKey, table {0}, result: {1}'",view->tableName,entriesLeft);
	}

	// for now....
	return SQLITE_OK;
}

int domview_vtab_view_cursor::getDominoColumnType(sqlite3_value* v) {
	int sqliteType = sqlite3_value_type(v);
	switch(sqliteType) {
		case SQLITE_INTEGER: {
			return COLTYPE_INTEGER;
		}
		case SQLITE_FLOAT: {
			return COLTYPE_NUMBER;
		}
		case SQLITE_BLOB: {
			return COLTYPE_STRING;
		}
		case SQLITE_NULL: {
			return COLTYPE_STRING;
		}
		case SQLITE3_TEXT: {
			return COLTYPE_STRING;
		}
	}
	return COLTYPE_STRING;
}

int domview_vtab_view_cursor::xNext() {
	return xNext(false,1L);
}

int domview_vtab_view_cursor::xNext(bool first, DWORD skip) {
	TRACE_FUNCTION("domview_vtab_view_cursor::xNext");

	// If we separate the multiple fields into separate entries, then we should see if there are remaining rows
	// Not that when we read the first entry, the rowId and rowCount are not yet initialized, as they 
	// map to the current entry.
	if(view->options & OPTIONS_SEPARATEMULTI) {
		if(!first) {
			// If there is at least an entry left, then return it
			if(rowId<rowCount-1) {
				rowId++;
				return SQLITE_OK;
			}
		}
	}
	
	// Check if we reached the EOF
	if(first) {
		eof = FALSE;
		currentEntry = 0;
	} else {
		currentEntry++; // Move to the next entry
		if(eof || (currentEntry>=cachedEntries && (SignalFlag & SIGNAL_MORE_TO_DO)==0) ) {
			if(DomSQL::FLAG_TRACE_EOF) {
				jprintln(">>xNext, view:{0}, found EOF! ", view->viewName, currentEntry, cachedEntries,(SignalFlag & SIGNAL_MORE_TO_DO)==0);
			}
			eof = TRUE;
			return SQLITE_OK;
		}
	}

	DWORD returnMask = view->returnMask;

	if(DomSQL::FLAG_TRACE_XNEXT) {
		jprintln("xNext {0}, view:{1}, currentEntry={2}, cachedEntries={3}, More={4}, first={5}", cursorID, view->tableName, currentEntry, cachedEntries,(SignalFlag & SIGNAL_MORE_TO_DO)!=0, first);
	}

	// If there is no entry in the buffer, then read it
	if(currentEntry>=cachedEntries) {
		if(DomSQL::FLAG_TRACE_XNEXT) {
			jprintln("xNext {0}, view:{1}, reading NSF entries currentEntry={2}, cachedEntries={3}, first={4}", cursorID, view->tableName, currentEntry, cachedEntries, first);
		}
		// Ok, none is left....
		// Free the current buffer so we'll read another one
		if(hBuffer) {
			OSUnlockObject(hBuffer); 
			OSMemFree(hBuffer); 
			hBuffer=NULL;
			bufferPtr=NULL;
		}
		// Read the new entries
		DWORD entRead = 0;
		if(first || (SignalFlag & SIGNAL_MORE_TO_DO)) {
			int error;
			if ( error = NIFReadEntries(
				hCollection,       /* handle to this collection */
				&CollPosition,     /* where to start in collection */
				NAVIGATE_NEXT,     /* order to use when skipping */
				skip,              /* number to skip */
				NAVIGATE_NEXT,     /* order to use when reading */
				entriesLeft,	   /* max number to read */
				returnMask,		   /* info we want */
				&hBuffer,		   /* handle to info buffer (return)	*/
				NULL,              /* length of info buffer (return) */
				NULL,              /* entries skipped (return) */
				&entRead,		   /* entries read (return) */
				&SignalFlag))	   /* share warning and more signal flag (return) */

			{
				if(DomSQL::FLAG_TRACE_XNEXT) {
					jprintln("xNext, view:{0}, Error reading NIFReadEntries", view->viewName);
				}
				//NCheck(JNIUtils::getJNIEnv(),error);
				//DomSQL::throwex();
				return SQLITE_ERROR;
			}

			if(DomSQL::FLAG_TRACE_XNEXT) {
				jprintln(">>xNext, view:{0}:{1}, NIFReadEntries [#={2}], entries read={3}, more={4}", view->viewName, view->noteID, entriesLeft, entRead,(SignalFlag & SIGNAL_MORE_TO_DO)!=0);
			}
		}

		
		// If no entries had been read, then it is EOF
		if(entRead==0) {
			if(DomSQL::FLAG_TRACE_EOF) {
				jprintln(">>xNext, view:{0}, find EOF", view->viewName);
			}
			eof = TRUE;
			return SQLITE_OK;
		}

		// Decrease the number of entries left to read
		entriesLeft -= entRead;

		// Update the number of entries currently buffered
		if(first) {
			cachedEntries = entRead;
		} else {
			cachedEntries += entRead;
		}
		if(DomSQL::FLAG_TRACE_XNEXT) {
			jprintln("xNext, view:{0}, Entries Read {1}, {2} entries, more={3}",view->tableName,entRead,cachedEntries,(SignalFlag & SIGNAL_MORE_TO_DO)!=0);
		}

		// The buffer contains the next entries...
		bufferPtr = (BYTE*)OSLockObject(hBuffer);

		// Read the collection stats, if requested
		if( (returnMask & READ_MASK_COLLECTIONSTATS)!=0 ) {
			DWORD dwTopLevelEntries = ((COLLECTIONSTATS *)bufferPtr)->TopLevelEntries;
			bufferPtr += sizeof(COLLECTIONSTATS);
		}
	}

	// Read the pointers to the data
	readSummaryData(returnMask);
	return SQLITE_OK;
}

void domview_vtab_view_cursor::readSummaryData(int returnMask) {
	TRACE_FUNCTION("domview_vtab_view_cursor::readSummaryData");

	// Ok, we need to read an new entry from the NSF, so we initialize the row
	rowId = 0;
	rowCount = 1;

	BYTE* ptr = bufferPtr;
	if( (returnMask & READ_MASK_NOTEID)!=0 ) {
		noteId = (DWORD*)ptr;
		ptr += sizeof(DWORD);
	}
	if( (returnMask & READ_MASK_NOTEUNID)!=0 ) {
		unid = (UNIVERSALNOTEID*)ptr;
		ptr += sizeof(UNIVERSALNOTEID);
	}
	if( (returnMask & READ_MASK_NOTECLASS)!=0 ) {
		noteClass = (WORD*)ptr;
		ptr += sizeof(WORD);
	}
	if( (returnMask & READ_MASK_INDEXSIBLINGS)!=0 ) {
		indexSiblings = (DWORD*)ptr;
		ptr += sizeof(DWORD);
	}
	if( (returnMask & READ_MASK_INDEXCHILDREN)!=0 ) {
		indexChildren = (DWORD*)ptr;
		ptr += sizeof(DWORD);
	}
	if( (returnMask & READ_MASK_INDEXDESCENDANTS)!=0 ) {
		indexDescendants = (DWORD*)ptr;
		ptr += sizeof(DWORD);
	}
	if( (returnMask & READ_MASK_INDEXANYUNREAD)!=0 ) {
		indexAnyUnread = (WORD*)ptr;
		ptr += sizeof(WORD);
	}
	if( (returnMask & READ_MASK_INDENTLEVELS)!=0 ) {
		indentLevels = (WORD*)ptr;
		ptr += sizeof(WORD);
	}
	if( (returnMask & READ_MASK_SCORE)!=0 ) {
		score = (WORD*)ptr;
		ptr += sizeof(WORD);
	}
	if( (returnMask & READ_MASK_INDEXUNREAD)!=0 ) {
		indexUnread = (WORD*)ptr;
		ptr += sizeof(WORD);
	}
	if( (returnMask & READ_MASK_INDEXPOSITION)!=0 ) {
		indexPosition = (COLLECTIONPOSITION*)ptr;
		ptr += COLLECTIONPOSITIONSIZE((COLLECTIONPOSITION*) ptr);
	}
	if( (returnMask & READ_MASK_SUMMARYVALUES)!=0 ) {
		itemValueTable = (ITEM_VALUE_TABLE*)ptr;
		// Allocate the buffer of pointers, if it is yet null
		itemValuePtrLength = min(MAX_VIEWCOLUMNS,itemValueTable->Items);
		// Calculate the pointers into the buffer for all the columns values
		USHORT* pLengthTable = (USHORT*)(((BYTE*)itemValueTable)+sizeof(ITEM_VALUE_TABLE));
		BYTE* pSummaryPos = ((BYTE*)pLengthTable)+itemValueTable->Items*sizeof(USHORT);
		for(int i=0; i<itemValuePtrLength; i++) {
			USHORT len = pLengthTable[i];
			if(DomSQL::FLAG_TRACE_READDATA) {
				StringBuilder sb;
				USHORT dataType = *((USHORT*)pSummaryPos);
				BYTE* ptr = pSummaryPos + sizeof(USHORT);
				switch(dataType) {
					case TYPE_TEXT: {
						USHORT ulen = len - sizeof(USHORT);
						sb.append("TEXT:");
						sb.append((const char*)ptr,ulen);
					} break;
					case TYPE_NUMBER: {
						sb.append("NUMBER:");
						char cc[32]; sprintf_s( cc, "%f", *(double*)ptr );
						sb.append(cc);
					} break;
					case TYPE_TIME: {
						char ts[24];
						toISO8601String(ts,*(TIMEDATE*)ptr);
						sb.append("TIME:");
						sb.append(ts);
					} break;
					default: {
						sb.append("<unknown>");
					}
				}
				
				//const char* cname = i<view->colCount ? view->columns[i].colName : "<Invalid column>";
				//jprintln("ReadData: View:{0}, Col#{1}[{2}], value:{3}",view->viewName,i,cname,sb.toString());
				jprintln("ReadData: View:{0}, SummaryValue#{1}, value:{2}",view->viewName,i,sb.toString());
			}

			// Calculate the # of rows if we split the multiple fields into multiple values
			if(view->options & OPTIONS_SEPARATEMULTI) {
				// Note the field should participate into the table in order to take into account the # of entries
				bool participate = false;
				for(int vc=0; vc<view->colCount; vc++) {
					if(view->columns[vc].summaryIndex==i) {
						participate = true;
						break;
					}
				}
				if(participate) {
					USHORT dataType = *((USHORT*)pSummaryPos);
					BYTE* ptr = pSummaryPos + sizeof(USHORT);
					switch(dataType) {
						case TYPE_TEXT_LIST: {
							LIST* pList = (LIST*) ptr;
							rowCount = max(rowCount, pList->ListEntries);
						} break;
						case TYPE_NUMBER_RANGE: {
							RANGE* pRange = (RANGE*) ptr;
							rowCount = max(rowCount, pRange->ListEntries);
						} break;
						case TYPE_TIME_RANGE: {
							RANGE* pRange = (RANGE*) ptr;
							rowCount = max(rowCount, pRange->ListEntries);
						} break;
					}
				}
			}

			itemValuePtr[i] = pSummaryPos;
			pSummaryPos += len;
		}
		ptr += itemValueTable->Length;
	}
	if( (returnMask & READ_MASK_SUMMARY)!=0 ) {
		itemTable = (ITEM_TABLE*)ptr;
		ptr += itemTable->Length;
	}

	// Adjust the global buffer pointer
	bufferPtr = ptr;
}

int domview_vtab_view_cursor::xColumn(sqlite3_context *pContext, int idxCol) {
	TRACE_FUNCTION("domview_vtab_view_cursor::xColumn");

	// Find the actual column # based on the virtual table definition
	int summaryIndex = view->columns[idxCol].summaryIndex;
	if(DomSQL::FLAG_TRACE_READDATA) {
		jprintln("COLIDX: {0}, summaryIndex={1}",idxCol,summaryIndex);
	}

	// Handle the special cases
	// The system columns are always emitted
	if(summaryIndex<0) {
		switch(summaryIndex) {
			case COLIDX_ID: {
				sqlite3_result_int64(pContext,(*noteId)&0xFFFFFFFF);
				return SQLITE_OK;
			}
			case COLIDX_UNID: {
				int len = sizeof(jchar)*32;
				jchar* chars = (jchar*)MemAlloc(len);
				UNIDToString(chars,*unid);
				sqlite3_result_text16(pContext,chars,len,MemFree);
				return SQLITE_OK;
			}
			case COLIDX_CLASS: {
				sqlite3_result_int(pContext,*noteClass);
				return SQLITE_OK;
			}
			case COLIDX_SIBLINGS: {
				sqlite3_result_int(pContext,*indexSiblings);
				return SQLITE_OK;
			}
			case COLIDX_CHILDREN: {
				sqlite3_result_int(pContext,*indexChildren);
				return SQLITE_OK;
			}
			case COLIDX_DESCENDANTS: {
				sqlite3_result_int(pContext,*indexDescendants);
				return SQLITE_OK;
			}
			case COLIDX_ANYUNREAD: {
				sqlite3_result_int(pContext,*indexAnyUnread);
				return SQLITE_OK;
			}
			case COLIDX_LEVELS: {
				sqlite3_result_int(pContext,*indentLevels);
				return SQLITE_OK;
			}
			case COLIDX_SCORE: {
				sqlite3_result_int(pContext,*score);
				return SQLITE_OK;
			}
			case COLIDX_UNREAD: {
				sqlite3_result_int(pContext,*indexUnread);
				return SQLITE_OK;
			}
			case COLIDX_POSITION: {
				StringBuilder b;
				COLLECTIONPOSITIONToString(b,*indexPosition);
				sqlite3_result_text(pContext,b.toString(),(int)b.length(),SQLITE_TRANSIENT);
				return SQLITE_OK;
			}
			case COLIDX_ROWID: {
				sqlite3_result_int(pContext,rowId);
				return SQLITE_OK;
			}
		}
		//VIEWCOL_INVALID and other negative values not handled...
		sqlite3_result_null(pContext);
		return SQLITE_OK;
	}

	// If the index is greater the available one, then it is a 'null'
	if(summaryIndex>=itemValuePtrLength) {
		sqlite3_result_null(pContext);
		return SQLITE_OK;
	}

	// Get the index in the summary buffer for the column
	USHORT* pLengthTable = (USHORT*)(((BYTE*)itemValueTable)+sizeof(ITEM_VALUE_TABLE));
	USHORT len = pLengthTable[summaryIndex];
	if(len!=0) {
		BYTE* pSummaryPos = itemValuePtr[summaryIndex];
		USHORT dataType = *((USHORT*)pSummaryPos);
		pSummaryPos += sizeof(USHORT);
		len -= sizeof(USHORT);
		switch(dataType) {
			case TYPE_TEXT: {
				// In case of the multiple entries separated
				if((view->options & OPTIONS_SEPARATEMULTI) && rowId>0) {
					sqlite3_result_null(pContext);
					return SQLITE_OK;
				}

				// Convert from LMBCS
				// We should create a temporary buffer that holds UTF-16 characters
				// Should be more optimal if read entries can return UTF16 itself
				const char* value = (const char*)pSummaryPos;

				// Ensure that the conversion buffer is big enough
				// As each character is stored as UTF-16, should be at least twice the length
				columnBuffer.init((len+1)*sizeof(jchar));

				if(DomSQL::FLAG_TRACE_READDATA) {
					MemString tmp(value,len); 
					jprintln("xColumn, table {0}, col {1}, text value='{2}'",view->tableName,view->columns[idxCol].colName,tmp);
				}

				// We currently ask SQLite to make a copy of the data using [SQLITE_TRANSIENT]
				// Should we better cache the buffer here?
				WORD utfLength = LMBCStoUTF16(value,len,(jchar*)columnBuffer.getBytes(),(WORD)columnBuffer.size());
				sqlite3_result_text16(pContext,columnBuffer.getBytes(),utfLength,SQLITE_TRANSIENT);
				return SQLITE_OK;
			} 

			case TYPE_TEXT_LIST: {
				LIST* pList = (LIST*) pSummaryPos;
				// In case of the multiple entries separated
				if(view->options & OPTIONS_SEPARATEMULTI) {
					if(rowId<pList->ListEntries) {
						char* pText; WORD wTextSize;
						ListGetText( pList, FALSE, rowId, &pText, &wTextSize );
						columnBuffer.init((wTextSize+1)*sizeof(jchar));
						WORD utfLength = LMBCStoUTF16(pText,wTextSize,(jchar*)columnBuffer.getBytes(),(WORD)columnBuffer.size());
						sqlite3_result_text16(pContext,columnBuffer.getBytes(),utfLength,SQLITE_TRANSIENT);
					} else {
						sqlite3_result_null(pContext);
					}
					return SQLITE_OK;
				}

				// Else, we convert the field values into a single string
				StringBuilder16 sb;
				for (WORD wEntry = 0; wEntry  < pList->ListEntries; wEntry++) {
					char* pText; WORD wTextSize;
					ListGetText( pList, FALSE, wEntry, &pText, &wTextSize );
					if(wEntry>0) {
						sb.append((jchar)listSeparator());
					}
					if(wTextSize>0) {
						// Can we make this performing better by bypassing the temprory columnBuffer?
						// -> in the StringBuilder, we have to divide utfLength by 2 because it represents bytes
						columnBuffer.init((wTextSize+1)*sizeof(jchar));
						WORD utfLength = LMBCStoUTF16(pText,wTextSize,(jchar*)columnBuffer.getBytes(),(WORD)columnBuffer.size());
						sb.append((const jchar*)columnBuffer.getBytes(),utfLength/sizeof(jchar));
					}
				}
				if(DomSQL::FLAG_TRACE_READDATA) {
					jprintln("xColumn, table {0}, col {1}, string value='{2}'",view->tableName,view->columns[idxCol].colName,sb.toString());
				}
				// We currently ask SQLite to make a copy of the data using [SQLITE_TRANSIENT]
				// Should we better cache the buffer here?
				sqlite3_result_text16(pContext,sb.toString(),sb.length()*sizeof(jchar),SQLITE_TRANSIENT);
				return SQLITE_OK;
			}

			case TYPE_TIME: {
				// In case of the multiple entries separated
				if((view->options & OPTIONS_SEPARATEMULTI) && rowId>0) {
					sqlite3_result_null(pContext);
					return SQLITE_OK;
				}

				// Get the column value as a jlong
				// We convert it to seconds
				TIMEDATE* dt = (TIMEDATE*)pSummaryPos;
				#if DATE_AS_STRING
					jchar ts[24];
					toISO8601String(ts,*dt);
					sqlite3_result_text16(pContext,ts,-1,SQLITE_TRANSIENT);
					if(DomSQL::FLAG_TRACE_READDATA) {
						jprintln("xColumn, table {0}, col {1}, date/time value='{2}'",view->tableName,view->columns[idxCol].colName,ts);
					}
				#else
					jlong javaSec = convertTIMEDATEtoJavaSeconds(*dt);
					sqlite3_result_int64(pContext,javaSec);
					if(DomSQL::FLAG_TRACE_READDATA) {
						jprintln("xColumn, table {0}, col {1}, time as number value='{2}'",view->tableName,view->columns[idxCol].colName,javaSec);
					}
				#endif
				return SQLITE_OK;
			} 

			case TYPE_NUMBER: {
				// In case of the multiple entries separated
				if((view->options & OPTIONS_SEPARATEMULTI) && rowId>0) {
					sqlite3_result_null(pContext);
					return SQLITE_OK;
				}

				double d = *(double*)pSummaryPos;
				sqlite3_result_double(pContext,d);
				if(DomSQL::FLAG_TRACE_READDATA) {
					jprintln("xColumn, table {0}, col {1}, number value='{2}'",view->tableName,view->columns[idxCol].colName,d);
				}
				return SQLITE_OK;
			} 

			case TYPE_NUMBER_RANGE: {
				RANGE* pRange = (RANGE*) pSummaryPos;
                pSummaryPos += sizeof(RANGE);

				// In case of the multiple entries separated
				if(view->options & OPTIONS_SEPARATEMULTI) {
					if(rowId<pRange->ListEntries) {
						NUMBER& n = ((NUMBER*)pSummaryPos)[rowId];
						sqlite3_result_double(pContext,n);
						if(DomSQL::FLAG_TRACE_READDATA) {
							jprintln("xColumn, table {0}, col {1}, row {2}, number value='{3}'",view->tableName,view->columns[idxCol].colName,rowId,n);
						}
					} else {
						sqlite3_result_null(pContext);
					}
					return SQLITE_OK;
				}

				// Else, we convert the field values into a single string
				StringBuilder sb;
                for (int i=0; i<pRange->ListEntries; i++) {
					NUMBER& n = *(NUMBER*)pSummaryPos;
					if(sb.length()>0) {
						sb.append((jchar)listSeparator());
					}
					char buffer[128]; sprintf(buffer,"%g",n);
					sb.append(buffer);
                    pSummaryPos += sizeof(NUMBER);
				}
                for (int i=0; i<pRange->RangeEntries; i++) {
					NUMBER_PAIR& n = *(NUMBER_PAIR*)pSummaryPos;
					if(sb.length()>0) {
						sb.append((jchar)listSeparator());
					}
					char buffer[128]; sprintf(buffer,"%g%c%g",n.Lower,listRangeSeparator(),n.Upper);
					sb.append(buffer);
                    pSummaryPos += sizeof(NUMBER_PAIR);
                }

				if(DomSQL::FLAG_TRACE_READDATA) {
					jprintln("xColumn, table {0}, col {1}, time as string value='{2}'",view->tableName,view->columns[idxCol].colName,sb.toString());
				}

				// We currently ask SQLite to make a copy of the data using [SQLITE_TRANSIENT]
				// Should we better cache the buffer here?
				sqlite3_result_text(pContext,sb.toString(),sb.length(),SQLITE_TRANSIENT);
				return SQLITE_OK;
			}

			case TYPE_TIME_RANGE: {
				RANGE* pRange = (RANGE*) pSummaryPos;
                pSummaryPos += sizeof(RANGE);

				// In case of the multiple entries separated
				if(view->options & OPTIONS_SEPARATEMULTI) {
					if(rowId<pRange->ListEntries) {
						TIMEDATE* dt = ((TIMEDATE*)pSummaryPos)+rowId;
						// Get the column value as a jlong
						// We convert it to seconds
						#if DATE_AS_STRING
							jchar ts[24];
							toISO8601String(ts,*dt);
							sqlite3_result_text16(pContext,ts,-1,SQLITE_TRANSIENT);
							if(DomSQL::FLAG_TRACE_READDATA) {
								jprintln("xColumn, table {0}, col {1}, row {2}, date/time value='{3}'",view->tableName,view->columns[idxCol].colName,rowId,ts);
							}
						#else
							jlong javaSec = convertTIMEDATEtoJavaSeconds(*dt);
							sqlite3_result_int64(pContext,javaSec);
							if(DomSQL::FLAG_TRACE_READDATA) {
								jprintln("xColumn, table {0}, col {1}, row {2}, time as number value='{3}'",view->tableName,view->columns[idxCol].colName,rowId,javaSec);
							}
						#endif
					} else {
						sqlite3_result_null(pContext);
					}
					return SQLITE_OK;
				}

				StringBuilder sb;
                for (int i=0; i<pRange->ListEntries; i++) {
					TIMEDATE& dt = *(TIMEDATE*)pSummaryPos;
					if(sb.length()>0) {
						sb.append((jchar)listSeparator());
					}
					char buffer[32]; toISO8601String(buffer,dt);
					sb.append(buffer);
                    pSummaryPos += sizeof(TIMEDATE);
				}
                for (int i=0; i<pRange->RangeEntries; i++) {
					TIMEDATE_PAIR& dt = *(TIMEDATE_PAIR*)pSummaryPos;
					if(sb.length()>0) {
						sb.append((jchar)listSeparator());
					}
					char lower[32]; toISO8601String(lower,dt.Lower);
					char upper[32]; toISO8601String(upper,dt.Upper);
					char buffer[128]; sprintf(buffer,"%s%c%s",lower,listRangeSeparator(),upper);
					sb.append(buffer);
                    pSummaryPos += sizeof(TIMEDATE_PAIR);
                }

				if(DomSQL::FLAG_TRACE_READDATA) {
					jprintln("xColumn, table {0}, col {1}, timerange as string value='{2}'",view->tableName,view->columns[idxCol].colName,"<not implemented>");
				}

				// We currently ask SQLite to make a copy of the data using [SQLITE_TRANSIENT]
				// Should we better cache the buffer here?
				sqlite3_result_text(pContext,sb.toString(),sb.length(),SQLITE_TRANSIENT);
				return SQLITE_OK;
			} break;
		}
	}
	sqlite3_result_null(pContext);
	return SQLITE_OK;
}


// ===========================================================================================
//CREATE VIRTUAL TABLE AllContacts USING domsql
//   (5045,1,*,AllStates,494,8195,4,"ID",0,-1,"UNID",0,-2,"Code",0,0,"Label",0,1,4,1,1,2,A,2,1,2,D,3,1,3,A,4,1,3,D)
// ===========================================================================================

struct ArgParser {
	int argc;
	const char* const*argv;
	char **pzErr;
	int ptr;
	char temp[1024];
	ArgParser(int argc, const char* const*argv, char **pzErr) : argc(argc), argv(argv), pzErr(pzErr),ptr(3) {
	}
	bool hasString(const char* msg) {
		if(ptr<argc) {
			return true;
		}
		size_t len = strlen(msg);
		*pzErr = (char*)sqlite3_malloc((int)len);
		strcpy(*pzErr,msg);
		return false;
	}
	bool hasInt(const char* msg) {
		if(hasString(msg)) {
			return true;
		}
		return false;
	}
	void asString(char* buffer, int size) {
		const char* c = argv[ptr++];
		size_t len = strlen(c);
		len = min(len-2,size-1);
		memcpy(buffer,c+1,len);
		buffer[len] = 0;
	}
	int asInt() {
		const char* c = argv[ptr++];
		return atoi(c);
	}
	DWORD asDWORD() {
		const char* c = argv[ptr++];
		return atol(c);
	}
};


static int dominoCreateViewTable(JNIEnv* env, sqlite3 *db, ArgParser& p, void *pAux,
                          int argc, const char * const *argv,
                          sqlite3_vtab **ppVTab, char **pzErr){
	if(DomSQL::FLAG_DUMP_DESIGN) {
		jprintln("Create virtual table from: Module: {0}, Database {1}, Table Name {2}",argv[0],argv[1],argv[2]);
	}
	StringBuilder b;
	b.append("CREATE TABLE ");
	b.append(argv[2]);
	b.append("(");

	// Read the dbPath
	if(!p.hasString("Invalid DB Path")) { return SQLITE_ERROR; }
	char dbPath[MAXPATH]; p.asString(dbPath,sizeof(dbPath));

	// Read the viewName
	if(!p.hasString("Invalid View Name")) { return SQLITE_ERROR; }
	char viewName[256]; p.asString(viewName,sizeof(viewName));
	
	// Read the note id
	if(!p.hasInt("Invalid View NoteID")) { return SQLITE_ERROR; }
	NOTEID noteId = p.asDWORD();
	
	// Read the some options
	if(!p.hasInt("Invalid Options")) { return SQLITE_ERROR; }
	DWORD options = p.asInt();
	
	// Read the return mask
	if(!p.hasInt("Invalid return mask")) { return SQLITE_ERROR; }
	DWORD returnMask = p.asInt();
	
	// Read the columns
	if(!p.hasInt("Invalid column count")) { return SQLITE_ERROR; }
	int colCount = p.asInt();
	
	domino_vtab_view_column* columns = new domino_vtab_view_column[colCount];
	for(int i=0; i<colCount; i++) {
		if(!p.hasString("Invalid column name")) { return SQLITE_ERROR; }
		char s_colName[256]; p.asString(s_colName,sizeof(s_colName));

		if(!p.hasInt("Invalid column type")) { return SQLITE_ERROR; }
		int colType = p.asInt();

		if(!p.hasInt("Invalid column index")) { return SQLITE_ERROR; }
		int summaryIndex =  p.asInt();
		domino_vtab_view_column& c = columns[i];
		if(FLAG_DEBUG_INFO) {
			c.colName = s_colName;
		}
		c.colType = colType;
		c.summaryIndex = summaryIndex;

		if(i>0) {
			b.append(",");
		}
		b.append('\"');
		b.append(s_colName);
		b.append('\"');

		// For now, we ignore the actual type as it is not properly returned
		switch(colType) {
			case COLTYPE_UNKNOWN: {
				// Not typed
			} break;
			case COLTYPE_STRING: {
				b.append(" TEXT");
			} break;
			case COLTYPE_INTEGER: {
				b.append(" INTEGER");
			} break;
			case COLTYPE_NUMBER: {
				b.append(" NUMERIC");
			} break;
			case COLTYPE_BLOB: {
				b.append(" BLOB");
			} break;
			case COLTYPE_BOOLEAN: {
				b.append(" INTEGER");
			} break;
			case COLTYPE_DATE: {
				b.append(" DATETIME");
			} break;
		}
	}
	b.append(")");

	// Read the indexes
	if(!p.hasInt("Invalid index count")) { return SQLITE_ERROR; }
	int idxCount = p.asInt();

	ReferenceArray<domino_vtab_view_index> indexes(new domino_vtab_view_index[idxCount]);
	if(idxCount>0) {
		for(int i=0; i<idxCount; i++) {
			domino_vtab_view_index& index = indexes[i];

			// Ok, read the collation #
			if(!p.hasInt("Invalid collation # in index")) { return SQLITE_ERROR; }
			index.collation = p.asInt();

			// Read the number of columns in the index
			if(!p.hasInt("Invalid column count in index")) { return SQLITE_ERROR; }
			index.entryCount = p.asInt();
			index.entries = new domino_vtab_view_index_entry[index.entryCount];

			// Read all the entries
			for(int i=0; i<index.entryCount; i++) {
				domino_vtab_view_index_entry& entry = index.entries[i];

				// Read the index of the colum
				if(!p.hasInt("Invalid column index in index entry")) { return SQLITE_ERROR; }
				entry.colIdx = p.asInt();
	
				// Read the number of columns in the index
				if(!p.hasString("Invalid column sort order in index entry")) { return SQLITE_ERROR; }
				char sortOrder[4]; p.asString(sortOrder,sizeof(sortOrder));
				entry.desc = (sortOrder[0]=='D');
			}

		}
	}

	// assign the created object
	if(sqlite3_declare_vtab(db,b.toString())!=SQLITE_OK) {
		//jprintln("FAILED: {0}",sqlite3_errmsg(db));
		//jprintln("SQL: {0}",b.toString());
		return SQLITE_ERROR;
	}

	jstring jDbPath = env->NewStringUTF(dbPath);
	domino_vtab_view* vt = new domino_vtab_view(noteId,jDbPath,options,returnMask,colCount,columns,idxCount,indexes.detach());
	
	if(FLAG_DEBUG_INFO) {
		vt->viewName = viewName;
		vt->tableName = argv[2];
	}

	if(DomSQL::FLAG_DUMP_DESIGN) {
		jprintln("Virtual Table Created, SQL: {0}",b.toString());
		vt->dump();
	}

	*ppVTab = vt;
	vt->zErrMsg = NULL;
	return SQLITE_OK;
}


static int dominoCreateVirtualTable(sqlite3 *db, void *pAux,
                          int argc, const char * const *argv,
                          sqlite3_vtab **ppVTab, char **pzErr){
	TRACE_FUNCTION("dominoCreateVirtualTable");

	JNIEnv* env = JNIUtils::getJNIEnv();

	ArgParser p(argc,argv,pzErr);

	// Read the secret code
	if(!p.hasInt("Invalid secret code")) {return SQLITE_ERROR;}
	int secretCode = p.asInt();
	if(secretCode!=DomSQL::secretCode) {
		if(DomSQL::FLAG_DUMP_SECRETCODE) {
			jprintln("Virtual table, invalid secret code={0},{1}",secretCode,DomSQL::secretCode);
		}
		return SQLITE_ERROR;
	}
	if(DomSQL::FLAG_DUMP_SECRETCODE) {
		jprintln("Virtual table, secret code={0}",secretCode);
	}

	// Read the type
	if(!p.hasInt("Invalid secret code")) {return SQLITE_ERROR;}
	int type = p.asInt();
	if(type==1) {
		return dominoCreateViewTable(env,db,p,pAux,argc,argv,ppVTab,pzErr);
	}

	return SQLITE_ERROR;
}


// ===========================================================================================
//  Implementation
// ===========================================================================================

static int dominoCreate(sqlite3 *db, void *pAux,
                          int argc, const char * const *argv,
                          sqlite3_vtab **ppVTab, char **pzErr){
	TRACE_FUNCTION("dominoCreate");

    // Check the table type being created
	const char* type = argv[3];
	return dominoCreateVirtualTable(db,pAux,argc,argv,ppVTab,pzErr);
}

static int dominoDestroy(sqlite3_vtab *pVTab){
	TRACE_FUNCTION("dominoDestroy");

	//domino_vtable* pt = (domino_vtable*)pVTab;
	//if(pt) {
	//	delete pt;
	//}
	return SQLITE_OK;
}

static int dominoConnect(
  sqlite3 *db,
  void *pAux,
  int argc, const char *const*argv,
  sqlite3_vtab **ppVTab,
  char **pzErr
){
	TRACE_FUNCTION("dominoConnect");
	//return dominoCreate(db,pAux,argc,argv,ppVTab,pzErr);
	return SQLITE_OK;
}

static int dominoDisconnect(sqlite3_vtab *pVTab){
	TRACE_FUNCTION("dominoDisconnect");
	//return dominoDestroy(pVTab);
	return SQLITE_OK;
}


static int dominoBestIndex(sqlite3_vtab *pVTab, sqlite3_index_info *pInfo){
	TRACE_FUNCTION("dominoBestIndex");
	domino_vtable* pt = (domino_vtable*)pVTab;
	return pt->xBestIndex(pInfo);
}

static int dominoOpen(sqlite3_vtab *pVTab, sqlite3_vtab_cursor **ppCursor){
	TRACE_FUNCTION("dominoOpen");
	domino_vtable* pt = (domino_vtable*)pVTab;
	return pt->xOpen(ppCursor);
}

static int dominoClose(sqlite3_vtab_cursor *pCursor){
	TRACE_FUNCTION("dominoClose");
	domview_vtab_cursor* pc = (domview_vtab_cursor*)pCursor;
	delete pc;
	return SQLITE_OK;
}

static int dominoNext(sqlite3_vtab_cursor *pCursor){
	TRACE_FUNCTION("dominoNext");
	domview_vtab_cursor* pc = (domview_vtab_cursor*)pCursor;
	return pc->xNext();
}

static int dominoFilter(
  sqlite3_vtab_cursor *pCursor,     /* The cursor used for this query */
  int idxNum, const char *idxStr,   /* Which indexing scheme to use */
  int argc, sqlite3_value **argv    /* Arguments for the indexing scheme */
){
	TRACE_FUNCTION("dominoFilter");
	domview_vtab_cursor* pc = (domview_vtab_cursor*)pCursor;
	return pc->xFilter(idxNum,idxStr,argc,argv);
}

static int dominoEof(sqlite3_vtab_cursor *pCursor){
	TRACE_FUNCTION("dominoEof");
	domview_vtab_cursor* pc = (domview_vtab_cursor*)pCursor;
	return pc->xEof();
}

static int dominoColumn(sqlite3_vtab_cursor *pCursor,
                          sqlite3_context *pContext, int idxCol){
	TRACE_FUNCTION("dominoColumn");
	domview_vtab_cursor* pc = (domview_vtab_cursor*)pCursor;
	return pc->xColumn(pContext,idxCol);
}

static int dominoRowid(sqlite3_vtab_cursor *pCursor, sqlite_int64 *pRowid){
	TRACE_FUNCTION("dominoRowid");

	return SQLITE_OK;
}

static int dominoUpdate(sqlite3_vtab *pVtab, int nArg, sqlite3_value **ppArg,
                   sqlite_int64 *pRowid){
	TRACE_FUNCTION("dominoUpdate");

	int rc;
	rc = SQLITE_ERROR; 
	return rc;
}

static int dominoFindFunction(
  sqlite3_vtab *pVtab,
  int nArg,
  const char *zName,
  void (**pxFunc)(sqlite3_context*,int,sqlite3_value**),
  void **ppArg
){
	TRACE_FUNCTION("dominoFindFunction");

	return 0;
}

static int dominoRename(
  sqlite3_vtab *pVtab,
  const char *zName
){
	TRACE_FUNCTION("dominoRename");

	int rc = SQLITE_NOMEM;
	return rc;
}

static const sqlite3_module dominoModule = {
  /* iVersion      */ 0,
  /* xCreate       */ dominoCreate,
  /* xConnect      */ dominoConnect,
  /* xBestIndex    */ dominoBestIndex,
  /* xDisconnect   */ dominoDisconnect,
  /* xDestroy      */ dominoDestroy,
  /* xOpen         */ dominoOpen,
  /* xClose        */ dominoClose,
  /* xFilter       */ dominoFilter,
  /* xNext         */ dominoNext,
  /* xEof          */ dominoEof,
  /* xColumn       */ dominoColumn,
  /* xRowid        */ dominoRowid,
  /* xUpdate       */ dominoUpdate,
  /* xBegin        */ 0, 
  /* xSync         */ 0,
  /* xCommit       */ 0,
  /* xRollback     */ 0,
  /* xFindFunction */ dominoFindFunction,
  /* xRename       */ dominoRename,
};

int sqlite3DomSqlInit(sqlite3 *db){
	//sqlite3_enable_shared_cache(true);
	//sqlite3_overload_function(db, "snippet", -1);
	//sqlite3_overload_function(db, "offsets", -1);
	return sqlite3_create_module(db, "domsql", &dominoModule, 0);
}
