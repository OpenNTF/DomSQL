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
package com.ibm.domino.domsql.sqlite.driver.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import com.ibm.commons.util.NotImplementedException;
import com.ibm.domino.domsql.sqlite.driver.SQLite;


/**
 * Implements a JDBC ResultSet.
 */
public class DomSQLResultSetMetaData implements ResultSetMetaData {
    
    private long htsmt;

    public DomSQLResultSetMetaData(long htsmt) {
        this.htsmt = htsmt;
    }
    
    public long getHstmt() {
    	return htsmt;
    }
    
    public int jdbcToSqliteCol(int col) throws SQLException {
        return --col;
    }
    
    // ResultSetMetaData Functions //////////////////////////////////

    public String getCatalogName(int col) throws SQLException {
        int sqliteCol = jdbcToSqliteCol(col);
        return SQLite.columnTableName(getHstmt(), sqliteCol);
    }

    public String getColumnClassName(int col) throws SQLException {
        int sqliteCol = jdbcToSqliteCol(col);
        switch (SQLite.columnType(getHstmt(), sqliteCol)) {
            case SQLite.SQLITE_INTEGER:
                return "java.lang.Integer";
            case SQLite.SQLITE_FLOAT:
                return "java.lang.Double";
            case SQLite.SQLITE_BLOB:
                return "java.lang.Object";
            case SQLite.SQLITE_NULL:
                return "java.lang.Object";
            //case SQLite.SQLITE_TEXT:
        }
        return "java.lang.String";
    }

    public int getColumnCount() throws SQLException {
        int nCol = SQLite.columnCount(getHstmt()); 
        return nCol;
    }

    public int getColumnDisplaySize(int col) throws SQLException {
        int sqliteCol = jdbcToSqliteCol(col);
        switch (SQLite.columnType(getHstmt(), sqliteCol)) {
            case SQLite.SQLITE_NULL:
                return 1;
            case SQLite.SQLITE_INTEGER:
                return 15;
            case SQLite.SQLITE_FLOAT:
                return 22;
        }
        return Integer.MAX_VALUE;
    }


    public String getColumnName(int col) throws SQLException {
        int sqliteCol = jdbcToSqliteCol(col);
        return SQLite.columnName(getHstmt(), sqliteCol);
    }

    public String getColumnLabel(int col) throws SQLException {
        return getColumnName(col);
    }

    public int getColumnType(int col) throws SQLException {
        int sqliteCol = jdbcToSqliteCol(col);
        switch (SQLite.columnType(getHstmt(), sqliteCol)) {
            case SQLite.SQLITE_NULL:
                return Types.NULL;
            case SQLite.SQLITE_INTEGER:
                return Types.INTEGER;
            case SQLite.SQLITE_FLOAT:
                return Types.DOUBLE;
            case SQLite.SQLITE_BLOB:
                return Types.BLOB;
            //case SQLite.SQLITE_TEXT:
        }
        return Types.VARCHAR;
    }

    public String getColumnTypeName(int col) throws SQLException {
        int sqliteCol = jdbcToSqliteCol(col);
        switch (SQLite.columnType(getHstmt(), sqliteCol)) {
            case SQLite.SQLITE_NULL:
                return "NULL"; // Shouldn't this be text???
            case SQLite.SQLITE_INTEGER:
                return "INTEGER";
            case SQLite.SQLITE_FLOAT:
                return "FLOAT";
            case SQLite.SQLITE_BLOB:
                return "BLOB";
            //case SQLite.SQLITE_TEXT:
        }
        return "TEXT";
    }

    public int getPrecision(int col) throws SQLException {
        int sqliteCol = jdbcToSqliteCol(col);
        switch (SQLite.columnType(getHstmt(), sqliteCol)) {
            case SQLite.SQLITE_INTEGER:
                return 8;
            case SQLite.SQLITE_FLOAT:
                return 15;
        }
        return 0;
    }

    public int getScale(int col) throws SQLException {
        return 0;
    }

    public String getSchemaName(int col) throws SQLException {
        return "";
    }

    public String getTableName(int col) throws SQLException {
        int sqliteCol = jdbcToSqliteCol(col);
        return SQLite.columnTableName(getHstmt(), sqliteCol);
    }

    public int isNullable(int col) throws SQLException {
        return columnNullable;
    }

    public boolean isAutoIncrement(int col) throws SQLException {
        return false;
    }

    public boolean isCaseSensitive(int col) throws SQLException {
        return true;
    }

    public boolean isCurrency(int col) throws SQLException {
        return false;
    }

    public boolean isDefinitelyWritable(int col) throws SQLException {
        return true;
    } // FIXME: check db file constraints?

    public boolean isReadOnly(int col) throws SQLException {
        return false;
    }

    public boolean isSearchable(int col) throws SQLException {
        return true;
    }

    public boolean isSigned(int col) throws SQLException {
        return false;
    }

    public boolean isWritable(int col) throws SQLException {
        return true;
    }


    //@Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new NotImplementedException();
    }
}
