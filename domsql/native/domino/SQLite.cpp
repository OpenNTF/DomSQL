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


///////////////////////////////////////////////////////////////////////////////////////////////////////
// Global Methods
///////////////////////////////////////////////////////////////////////////////////////////////////////

extern "C" JNIEXPORT jstring JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_libversion(
        JNIEnv *env, jclass clazz)
{
	TRACE_FUNCTION("SQLite_libversion");
    return env->NewStringUTF(sqlite3_libversion());
}


///////////////////////////////////////////////////////////////////////////////////////////////////////
// Database Methods
///////////////////////////////////////////////////////////////////////////////////////////////////////

extern "C" JNIEXPORT jlong JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_open(
        JNIEnv *env, jclass clazz, jstring _name)
{
	TRACE_FUNCTION("SQLite_open");
	JStringUTF16 name(env,_name);
	sqlite3* db;
    if (sqlite3_open16(name.getChars(), &db)!=SQLITE_OK) {
        DomSQL::throwex(env,db);
        sqlite3_close(db);
        return NULL;
    }
    return toHandle(db);
}

extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_close(
        JNIEnv *env, jclass clazz, jlong hdb)
{
	TRACE_FUNCTION("SQLite_close");
	if (sqlite3_close(toPointer<sqlite3>(hdb)) != SQLITE_OK) {
        DomSQL::throwex(env,toPointer<sqlite3>(hdb));
	}
}

int sqlite3DomSqlInit(sqlite3 *db);
extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_initDominoModule(
        JNIEnv *env, jclass clazz, jlong hdb)
{
	TRACE_FUNCTION("initDominoModule");
	sqlite3DomSqlInit(toPointer<sqlite3>(hdb));
}

extern "C" JNIEXPORT jint JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_errcode(
	JNIEnv *env, jclass clazz, jlong hdb)
{
	TRACE_FUNCTION("SQLite_errcode");
	return (jint)sqlite3_errcode(toPointer<sqlite3>(hdb));
}

extern "C" JNIEXPORT jint JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_extendederrcode(
	JNIEnv *env, jclass clazz, jlong hdb)
{
	TRACE_FUNCTION("SQLite_extendederrcode");
	return (jint)sqlite3_extended_errcode(toPointer<sqlite3>(hdb));
}

extern "C" JNIEXPORT jstring JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_errmsg(
	JNIEnv *env, jclass clazz, jlong hdb)
{
	const jchar* msg = (const jchar*)sqlite3_errmsg16(toPointer<sqlite3>(hdb));
	jstring js=env->NewString(msg,jstrlen(msg));
	return js;
}


extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_busyTimeout(
    JNIEnv *env, jclass clazz, jlong hdb, jint ms)
{
	sqlite3_busy_timeout(toPointer<sqlite3>(hdb), ms);
}

extern "C" JNIEXPORT jint JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_changes(
        JNIEnv *env, jclass clazz, jlong hdb)
{
    return sqlite3_changes(toPointer<sqlite3>(hdb));
}

extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_interrupt(
        JNIEnv *env, jclass clazz, jlong hdb)
{
    sqlite3_interrupt(toPointer<sqlite3>(hdb));
}



///////////////////////////////////////////////////////////////////////////////////////////////////////
// Statements Methods
///////////////////////////////////////////////////////////////////////////////////////////////////////

extern "C" JNIEXPORT jlong JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_prepare(
        JNIEnv *env, jclass clazz, jlong hdb, jstring _sql)
{
	TRACE_FUNCTION1("SQLite_prepare","sql:{0}",_sql);
	JStringUTF16 sql(env,_sql);
	sqlite3_stmt* stmt;
	int length = jstrlen(sql.getChars());
	int status = sqlite3_prepare16_v2(toPointer<sqlite3>(hdb), sql.getChars(), length*2, &stmt, 0);
    if (status != SQLITE_OK) {
        DomSQL::throwex(env,toPointer<sqlite3>(hdb));
        return 0;
    }
    return toHandle(stmt);
}

extern "C" JNIEXPORT jint JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_step(
        JNIEnv *env, jclass clazz, jlong hstmt)
{
	return sqlite3_step(toPointer<sqlite3_stmt>(hstmt));
}
extern "C" JNIEXPORT jint JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_finalize(
        JNIEnv *env, jclass clazz, jlong hstmt)
{
	TRACE_FUNCTION1("SQLite_finalize","hstmt:{0}",hstmt);
    return sqlite3_finalize(toPointer<sqlite3_stmt>(hstmt));
}
extern "C" JNIEXPORT jint JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_reset(
        JNIEnv *env, jclass clazz, jlong hstmt)
{
    return sqlite3_reset(toPointer<sqlite3_stmt>(hstmt));
}

extern "C" JNIEXPORT jint JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_bindParameterCount(
        JNIEnv *env, jclass clazz, jlong hstmt)
{
    return sqlite3_bind_parameter_count(toPointer<sqlite3_stmt>(hstmt));
}

extern "C" JNIEXPORT jint JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_columnCount(
        JNIEnv *env, jclass clazz, jlong hstmt)
{
    return sqlite3_column_count(toPointer<sqlite3_stmt>(hstmt));
}

extern "C" JNIEXPORT jstring JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_columnTableName(
        JNIEnv *env,jclass clazz,  jlong hstmt, jint col)
{
    const jchar *str = (const jchar*)sqlite3_column_table_name16(toPointer<sqlite3_stmt>(hstmt), col);
    return str ? env->NewString(str, jstrlen(str)) : NULL;
}

extern "C" JNIEXPORT jstring JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_columnName(
        JNIEnv *env, jclass clazz, jlong hstmt, jint col)
{
    const jchar *str = (const jchar*)sqlite3_column_name16(toPointer<sqlite3_stmt>(hstmt), col);
    return str ? env->NewString(str, jstrlen(str)) : NULL;
}

extern "C" JNIEXPORT jint JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_columnType(
        JNIEnv *env, jclass clazz, jlong hstmt, jint col)
{
    return sqlite3_column_type(toPointer<sqlite3_stmt>(hstmt), col);
}

extern "C" JNIEXPORT jstring JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_columnDecltype(
        JNIEnv *env, jclass clazz, jlong hstmt, jint col)
{
    const char *str = sqlite3_column_decltype(toPointer<sqlite3_stmt>(hstmt), col);
    return env->NewStringUTF(str);
}

extern "C" JNIEXPORT jstring JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_columnText(
        JNIEnv *env, jclass clazz, jlong hstmt, jint col)
{
	int jlength = sqlite3_column_bytes16(toPointer<sqlite3_stmt>(hstmt), col)/sizeof(jchar); 
	const jchar* jc = (const jchar*)sqlite3_column_text16(toPointer<sqlite3_stmt>(hstmt), col);
	return env->NewString(jc, jlength);
}

extern "C" JNIEXPORT jbyteArray JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_columnBlob(
        JNIEnv *env, jclass clazz, jlong hstmt, jint col)
{
    jsize length;
    jbyteArray jBlob;
    jbyte *a;
    const void *blob = sqlite3_column_blob(toPointer<sqlite3_stmt>(hstmt), col);
    if (!blob) return NULL;

    length = sqlite3_column_bytes(toPointer<sqlite3_stmt>(hstmt), col);
    jBlob = env->NewByteArray(length);

    a = (jbyte*)env->GetPrimitiveArrayCritical(jBlob, 0);
    memcpy(a, blob, length);
    env->ReleasePrimitiveArrayCritical(jBlob, a, 0);

    return jBlob;
}

extern "C" JNIEXPORT jdouble JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_columnDouble(
        JNIEnv *env, jclass clazz, jlong hstmt, jint col)
{
    return sqlite3_column_double(toPointer<sqlite3_stmt>(hstmt), col);
}

extern "C" JNIEXPORT jlong JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_columnInt64(
        JNIEnv *env, jclass clazz, jlong hstmt, jint col)
{
    return sqlite3_column_int64(toPointer<sqlite3_stmt>(hstmt), col);
}

extern "C" JNIEXPORT jint JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_columnInt(
        JNIEnv *env, jclass clazz, jlong hstmt, jint col)
{
    return sqlite3_column_int(toPointer<sqlite3_stmt>(hstmt), col);
}


extern "C" JNIEXPORT jint JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_bindNull(
        JNIEnv *env, jclass clazz, jlong hstmt, jint pos)
{
    return sqlite3_bind_null(toPointer<sqlite3_stmt>(hstmt), pos);
}

extern "C" JNIEXPORT jint JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_bindInt(
        JNIEnv *env, jclass clazz, jlong hstmt, jint pos, jint v)
{
    return sqlite3_bind_int(toPointer<sqlite3_stmt>(hstmt), pos, v);
}

extern "C" JNIEXPORT jint JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_bindInt64(
        JNIEnv *env, jclass clazz, jlong hstmt, jint pos, jlong v)
{
    return sqlite3_bind_int64(toPointer<sqlite3_stmt>(hstmt), pos, v);
}

extern "C" JNIEXPORT jint JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_bindDouble(
        JNIEnv *env, jclass clazz, jlong hstmt, jint pos, jdouble v)
{
    return sqlite3_bind_double(toPointer<sqlite3_stmt>(hstmt), pos, v);
}

extern "C" JNIEXPORT jint JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_bindText(
        JNIEnv *env, jclass clazz, jlong hstmt, jint pos, jstring v)
{
    const char *chars = env->GetStringUTFChars(v, 0);
    int rc = sqlite3_bind_text(toPointer<sqlite3_stmt>(hstmt), pos, chars, -1, SQLITE_TRANSIENT);
    env->ReleaseStringUTFChars(v, chars);
    return rc;
}

extern "C" JNIEXPORT jint JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_bindBlob(
        JNIEnv *env, jclass clazz, jlong hstmt, jint pos, jbyteArray v)
{
    jsize size = env->GetArrayLength(v);
    void* a = env->GetPrimitiveArrayCritical(v, 0);
    jint rc = sqlite3_bind_blob(toPointer<sqlite3_stmt>(hstmt), pos, a, size, SQLITE_TRANSIENT);
    env->ReleasePrimitiveArrayCritical(v, a, JNI_ABORT);
    return rc;
}


extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_resultNull(
        JNIEnv *env, jclass clazz, jlong hContext)
{
	sqlite3_result_null(toPointer<sqlite3_context>(hContext));
}

extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_resultText(
        JNIEnv *env, jclass clazz, jlong hContext, jstring _value)
{
    if (!_value) { 
		sqlite3_result_null(toPointer<sqlite3_context>(hContext)); 
		return;
	}
	
    jsize size = env->GetStringLength(_value)*2; // SQLite require the buffer size in bytes!
	JStringUTF16C value(env,_value);
	sqlite3_result_text16(toPointer<sqlite3_context>(hContext), value.getChars(), size, SQLITE_TRANSIENT);
}

extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_resultBlob(
        JNIEnv *env, jclass clazz, jlong hContext, jarray _value)
{
    if (!_value) { 
		sqlite3_result_null(toPointer<sqlite3_context>(hContext)); 
		return;
	}
    jsize size = env->GetArrayLength(_value);

    // be careful with *Critical => should use a helper here!
    jbyte* bytes = (jbyte*)env->GetPrimitiveArrayCritical(_value, 0);
    sqlite3_result_blob(toPointer<sqlite3_context>(hContext), bytes, size, SQLITE_TRANSIENT);
    env->ReleasePrimitiveArrayCritical(_value, bytes, JNI_ABORT);
}

extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_resultDouble(
        JNIEnv *env, jclass clazz, jlong hContext, jdouble value)
{
    sqlite3_result_double(toPointer<sqlite3_context>(hContext), value);
}

extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_resultInt64(
        JNIEnv *env, jclass clazz, jlong hContext, jlong value)
{
    sqlite3_result_int64(toPointer<sqlite3_context>(hContext), value);
}

extern "C" JNIEXPORT void JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_resultInt(
        JNIEnv *env, jclass clazz, jlong hContext, jint value)
{
    sqlite3_result_int(toPointer<sqlite3_context>(hContext), value);
}



///////////////////////////////////////////////////////////////////////////////////////////////////////
// Utility Methods
///////////////////////////////////////////////////////////////////////////////////////////////////////

extern "C" JNIEXPORT jstring JNICALL Java_com_ibm_domino_domsql_sqlite_driver_SQLite_NescapeSqlString(
        JNIEnv *env, jclass clazz, jstring _str)
{
	char buffer[2048];
	JStringUTF8 str(env,_str);
	sqlite3_snprintf(sizeof(buffer),buffer,"%q",str.getChars());
    return env->NewStringUTF(buffer);
}
