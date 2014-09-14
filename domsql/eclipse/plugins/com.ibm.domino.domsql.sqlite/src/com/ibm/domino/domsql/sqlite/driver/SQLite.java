/*
 * © Copyright IBM Corp. 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.domino.domsql.sqlite.driver;

import java.sql.SQLException;


/**
 * JNI interface to SQLite.
 * 
 * @author priand
 */
public class SQLite {
    
    // Error constants
    public static final int SQLITE_OK           = 0;
    public static final int SQLITE_ERROR        = 1;
    public static final int SQLITE_INTERNAL     = 2;
    public static final int SQLITE_PERM         = 3;
    public static final int SQLITE_ABORT        = 4;
    public static final int SQLITE_BUSY         = 5;
    public static final int SQLITE_LOCKED       = 6;
    public static final int SQLITE_NOMEM        = 7;
    public static final int SQLITE_READONLY     = 8;
    public static final int SQLITE_INTERRUPT    = 9;
    public static final int SQLITE_IOERR        = 10;
    public static final int SQLITE_CORRUPT      = 11;
    public static final int SQLITE_NOTFOUND     = 12;
    public static final int SQLITE_FULL         = 13;
    public static final int SQLITE_CANTOPEN     = 14;
    public static final int SQLITE_PROTOCOL     = 15;
    public static final int SQLITE_EMPTY        = 16;
    public static final int SQLITE_SCHEMA       = 17;
    public static final int SQLITE_TOOBIG       = 18;
    public static final int SQLITE_CONSTRAINT   = 19;
    public static final int SQLITE_MISMATCH     = 20;
    public static final int SQLITE_MISUSE       = 21;
    public static final int SQLITE_NOLFS        = 22;
    public static final int SQLITE_AUTH         = 23;
    public static final int SQLITE_ROW          = 100;
    public static final int SQLITE_DONE         = 101;

    // types returned by sqlite3_column_type()
    public static final int SQLITE_INTEGER      = 1;
    public static final int SQLITE_FLOAT        = 2;
    public static final int SQLITE_TEXT         = 3;
    public static final int SQLITE_BLOB         = 4;
    public static final int SQLITE_NULL         = 5;
    

    // Global methods
    public static native String libversion() throws SQLException;
    
    // Database
    public static native long open(String file) throws SQLException;
    public static native void close(long hDB) throws SQLException;
    public static native void initDominoModule(long hDB) throws SQLException;
    public static native int errcode(long hDB) throws SQLException;
    public static native int extendederrcode(long hDB) throws SQLException;
    public static native String errmsg(long hDB) throws SQLException;
    public static native void busyTimeout(long hDB, int ms) throws SQLException;
    public static native int changes(long hDB) throws SQLException;
    public static native void interrupt(long hDB) throws SQLException;
    
    // SQL
    public static native long prepare(long hDb, String sql) throws SQLException;

    // Statements
    public static native int step(long hStmt) throws SQLException;
    public static native int finalize(long hStmt) throws SQLException;
    public static native int reset(long hStmt) throws SQLException;
    
    public static native int bindParameterCount(long hStmt) throws SQLException;
    public static native int columnCount(long hStmt) throws SQLException;
    public static native String columnName(long hStmt, int pos) throws SQLException;
    public static native int columnType(long hStmt, int col);
    public static native String columnDecltype(long hStmt, int col);
    
    public static native String columnTableName(long stmt, int col);
    public static native String columnText(long stmt, int col);
    public static native byte[] columnBlob(long stmt, int col);
    public static native double columnDouble(long stmt, int col);
    public static native long columnInt64(long stmt, int col);
    public static native int columnInt(long stmt, int col);

    public static native int bindNull(long hStmt, int pos);
    public static native int bindInt(long hStmt, int pos, int v);
    public static native int bindInt64(long hStmt, int pos, long v);
    public static native int bindDouble(long hStmt, int pos, double v);
    public static native int bindText(long hStmt, int pos, String v);
    public static native int bindBlob(long hStmt, int pos, byte[] v);

    public static native void resultNull(long hContext);
    public static native void resultText(long hContext, String val);
    public static native void resultBlob(long hContext, byte[] val);
    public static native void resultDouble(long hContext, double val);
    public static native void resultInt64(long hContext, long val);
    public static native void resultInt(long hContext, int val);
    public static native void resultError(long hContext, String err);
    
    // Utilities
    public static String escapeSqlString(String str) {
        int pos = str.indexOf('\'');
        if(pos<0) {
            return str;
        }
        // We should be able to optimize this...
        // Or call the native method bellow, if it is more efficient (although not sure)
        int len = str.length();
        StringBuilder buf = new StringBuilder(str.length()+32);
        buf.append(str,0,pos);
        for (int i = pos; i < len; i++) {
            char c = str.charAt(i); 
            if (c == '\'') {
                buf.append('\'');
            }
            buf.append(c);
        }
        return buf.toString();
    }
    private static native String NescapeSqlString(String str);
}
