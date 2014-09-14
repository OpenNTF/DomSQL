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

#ifndef _NOTESHELPER_H_
#define _NOTESHELPER_H_


// ===========================================================================================
// Error checking
// ===========================================================================================
bool NCheck( JNIEnv *env, STATUS err );
void NPrintError( JNIEnv *env, STATUS err );


// ===========================================================================================
// String conversion
// ===========================================================================================

jstring LMBCStoJavaString(JNIEnv *env, const char *str, int len);
jstring LMBCStoJavaString(JNIEnv *env, const char *str);

class JStringLMBCS {
	char localBuffer[128*3];
	char* data;

	void init(JNIEnv* env, const jchar* c, int length);

public:
	JStringLMBCS(JNIEnv* env, jstring s);
	JStringLMBCS(JNIEnv* env, jchar* c);
	JStringLMBCS(JNIEnv* env, jchar* c, int length);
	~JStringLMBCS() {
		if(data!=localBuffer) MemFree(data);
	}
	const char* getLMBCS() { return data; }
	size_t length() { return strlen(data); }
};


// ===========================================================================================
// Handle management
// ===========================================================================================

struct DBHANDLE_ {
	DBHANDLE hDB;
	DBHANDLE_() : hDB(0) {}
	~DBHANDLE_() {if(hDB) {NSFDbClose(hDB);}}
	operator DBHANDLE&() { return hDB; }
	DBHANDLE detach() { DBHANDLE temp=hDB; hDB=NULL; return temp; }
};

struct NOTEHANDLE_ {
	NOTEHANDLE hNote;
	NOTEHANDLE_() : hNote(0) {}
	~NOTEHANDLE_() {if(hNote) {NSFDbClose(hNote);}}
	operator NOTEHANDLE&() { return hNote; }
	NOTEHANDLE detach() { NOTEHANDLE temp=hNote; hNote=NULL; return temp; }
};

struct BLOCKID_ {
	BLOCKID hBlock;
	BLOCKID_(BLOCKID hBlock) : hBlock(hBlock) {}
	~BLOCKID_() { if(!ISNULLBLOCKID(hBlock)){OSUnlockBlock(hBlock);}}
	operator BLOCKID&() { return hBlock; }
};


// ===========================================================================================
// Date helper
// ===========================================================================================

jlong convertTIMEDATEtoJavaMilliSeconds(TIMEDATE& dt);
jlong convertTIMEDATEtoJavaSeconds(TIMEDATE& dt);


#endif
