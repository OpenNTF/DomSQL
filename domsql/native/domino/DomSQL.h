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

#ifndef _SQLITEHELPER_H_
#define _SQLITEHELPER_H_

// Define the constants for SQLite

#include "jnihelper.h"

#include "sqlite3.h"


// ---------------------------------------------------------------------------
// Accessing the callback in DomSQL

class DomSQL {
public:
	static jint			secretCode;

	static jclass		jniClass;
	static jmethodID	m_getDatabaseHandle;
	static jmethodID	m_createView;
	static jmethodID	m_createViewColumn;
	static jmethodID	m_createViewIndex;
	static jmethodID	m_createViewIndexEntry;

	static bool init(JNIEnv *env);

	static jlong getDatabaseHandle(JNIEnv* env, jstring dbPath) {
		return env->CallStaticLongMethod(jniClass,m_getDatabaseHandle,dbPath);
	}
	static jobject createView(JNIEnv* env, jlong noteID, jstring title) {
		return env->CallStaticObjectMethod(jniClass,m_createView,noteID,title);
	}
	static jobject createViewColumn(JNIEnv* env, jobject parent, jstring name, jstring title, jint type, jint summaryIndex) {
		return env->CallStaticObjectMethod(jniClass,m_createViewColumn,parent,name,title,type,summaryIndex);
	}
	static jobject createViewIndex(JNIEnv* env, jobject parent, jint collation) {
		return env->CallStaticObjectMethod(jniClass,m_createViewIndex,parent,collation);
	}
	static jobject createViewIndexEntry(JNIEnv* env, jobject parent, jstring colName, jboolean desc) {
		return env->CallStaticObjectMethod(jniClass,m_createViewIndexEntry,parent,colName,desc);
	}

	static void throwex(JNIEnv *env, sqlite3* db);
	static void throwex(JNIEnv *env, sqlite3_stmt* stmt);


	// Flags driving the runtime debug
	static jboolean FLAG_DUMP_DESIGN;
	static jboolean FLAG_DUMP_SECRETCODE;

	static jboolean FLAG_TRACE_DICTIONARY;
	static jboolean FLAG_TRACE_NIFCALLS;
	static jboolean FLAG_TRACE_XFILTER;
	static jboolean FLAG_TRACE_XNEXT;
	static jboolean FLAG_TRACE_READDATA;
	static jboolean FLAG_TRACE_FINDBYKEY;
	static jboolean FLAG_TRACE_BESTINDEX;
	static jboolean FLAG_TRACE_PERFORMANCE_HINTS;
	static jboolean FLAG_TRACE_EOF;
};

// Indicates if debug info, uncessary for the driver to work, are maintained
// This includes, for example, the name of the view columns
#ifdef FULLDEBUG
	#define FLAG_DEBUG_INFO		1
#else
	#define FLAG_DEBUG_INFO		0
#endif

#endif
