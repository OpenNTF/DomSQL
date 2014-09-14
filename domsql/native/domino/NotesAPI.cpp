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


// ==============================================================================================
// Notes initialization routines
// ==============================================================================================

extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_NotesAPI_NotesInit(
			JNIEnv *env, jclass clazz
			) {
	NCheck(env,NotesInit());
}

extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_NotesAPI_NotesTerm(
			JNIEnv *env, jclass clazz
			) {
	NotesTerm();
}

extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_NotesAPI_NotesInitThread(
			JNIEnv *env, jclass clazz
			) {
	NCheck(env,NotesInitThread());
}

extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_NotesAPI_NotesTermThread(
			JNIEnv *env, jclass clazz
			) {
	NotesTermThread();
}


// ==============================================================================================
// Access to notes.ini
// ==============================================================================================

extern "C" JNIEXPORT jint JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_NotesAPI_OSGetEnvironmentInt(
			JNIEnv *env, jclass clazz,
			jstring _name) {
	JStringLMBCS name(env,_name);
	return OSGetEnvironmentInt(name.getLMBCS());
}

extern "C" JNIEXPORT jlong JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_NotesAPI_OSGetEnvironmentLong(
			JNIEnv *env, jclass clazz,
			jstring _name) {
	JStringLMBCS name(env,_name);
	return (jlong)(((long)OSGetEnvironmentLong(name.getLMBCS())));
}

extern "C" JNIEXPORT jstring JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_NotesAPI_OSGetEnvironmentString(
	JNIEnv *env, jclass clazz,jstring _name) {
	JStringLMBCS name(env,_name);
	char retBuffer[4 * 1024];
	if(OSGetEnvironmentString(name.getLMBCS(),retBuffer,sizeof(retBuffer))) {
		return LMBCStoJavaString(env,retBuffer);
	}
	return NULL;
}

extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_NotesAPI_OSSetEnvironmentInt(
			JNIEnv *env, jclass clazz,
			jstring _name, jint value) {
	JStringLMBCS name(env,_name);
	OSSetEnvironmentInt(name.getLMBCS(),value);
}

extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_NotesAPI_OSSetEnvironmentVariable(
			JNIEnv *env, jclass clazz,
			jstring _name, jstring _value) {
	JStringLMBCS name(env,_name);
	JStringLMBCS value(env,_value);
	OSSetEnvironmentVariable(name.getLMBCS(),value.getLMBCS());
}


// ==============================================================================================
// NSF generic methods
// ==============================================================================================

extern "C" JNIEXPORT jlong JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_NotesAPI_NSFDBOpen(
        JNIEnv *env, jclass clazz, jstring _dbPath) 
{
	DHANDLE hDb;
	JStringLMBCS dbPath(env,_dbPath);
	if(!NCheck(env,NSFDbOpen(dbPath.getLMBCS(),&hDb))) {
		return 0;
	}
	return (jlong)hDb;
}

extern "C" JNIEXPORT jlong JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_NotesAPI_NSFDBOpenEx(
        JNIEnv *env, jclass clazz, jstring _dbPath, jlong hNames) 
{
	DHANDLE hDb;
	JStringLMBCS dbPath(env,_dbPath);
	if(!NCheck(env,NSFDbOpenExtended(dbPath.getLMBCS(),0,(DHANDLE)hNames,NULL,&hDb,NULL,NULL))) {
		return 0;
	}
	return (jlong)hDb;
}

extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_NotesAPI_NSFDBClose(
        JNIEnv *env, jclass clazz, jlong hDb) 
{
	if(!NCheck(env,NSFDbClose((DBHANDLE)hDb))) {
		return;
	}
}

extern "C" JNIEXPORT jlong JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_NotesAPI_NSFNoteOpen(
        JNIEnv *env, jclass clazz, jlong hDb, jint noteID, jint flags) 
{
	DHANDLE hNote;	
	if(!NCheck(env,NSFNoteOpen((DBHANDLE)hDb,noteID,flags,&hNote))) {
		return 0;
	}
	return (jlong)hNote;
}

extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_NotesAPI_NSFNoteClose(
        JNIEnv *env, jclass clazz, jlong hNote) 
{
	if(!NCheck(env,NSFNoteClose((DBHANDLE)hNote))) {
		return;
	}
}

extern "C" JNIEXPORT jint JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_NotesAPI_NIFFindView(
        JNIEnv *env, jclass clazz, jlong hDb, jstring _name) 
{
	NOTEID noteID;
	JStringLMBCS name(env,_name);
	if(!NCheck(env,NIFFindView((DHANDLE)hDb,name.getLMBCS(),&noteID))) {
		return 0;
	}
	return noteID;
}

extern "C" JNIEXPORT jstring JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_NotesAPI_SECKFMGetUserName(
        JNIEnv *env, jclass clazz) 
{
	char szServer[MAXUSERNAME+1];
	SECKFMGetUserName(szServer);
	if(!NCheck(env,SECKFMGetUserName(szServer))) {
		// Should not happen!
		return env->NewStringUTF("<Unknow Server ID>");
	}
    return env->NewStringUTF(szServer);
}

extern "C" JNIEXPORT jlong JNICALL Java_com_ibm_domino_domsql_sqlite_driver_jni_NotesAPI_NSFDbNonDataModifiedTime(
        JNIEnv *env, jclass clazz, jlong hDb) 
{
    TIMEDATE date;
	if(!NCheck(env,NSFDbModifiedTime((DBHANDLE)hDb,NULL,&date) ) ) {
        return 0L;
	}
	return convertTIMEDATEtoJavaMilliSeconds(date);
}
