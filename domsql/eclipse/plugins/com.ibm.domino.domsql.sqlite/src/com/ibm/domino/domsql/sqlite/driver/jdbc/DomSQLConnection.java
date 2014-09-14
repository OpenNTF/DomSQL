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

import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ibm.commons.util.NotImplementedException;
import com.ibm.domino.domsql.sqlite.DomSQLException;
import com.ibm.domino.domsql.sqlite.driver.Database;
import com.ibm.domino.domsql.sqlite.driver.SQLite;
import com.ibm.domino.domsql.sqlite.driver.jni.DomSQL;
import com.ibm.domino.domsql.sqlite.driver.jni.JNIUtils;

public class DomSQLConnection extends DomSQLObject implements Connection {
	
    private Database db;
    private String userName;

    private DomSQLDatabaseMetaData meta;
    private boolean autoCommit = true;
    private int timeout;

    // Indicates if a transaction was started while not in auto-commit mode
    private boolean transactionStarted;

    // List of statements related to this connections
    private List<DomSQLStatement> statements = new ArrayList<DomSQLStatement>();
    

    public DomSQLConnection(Database db) throws SQLException {
        this.db = db;
        db.addConnection(this);
    }

    protected DomSQLConnection getConnection() {
        return this;
    }
    
    public String getUserName() {
        return userName;
    }

    public void close() throws SQLException {
        if(db!=null) {
            synchronized(this) {
                if(db!=null) {
                    closeMetaData();
                    closeStatements();
                    transactionStarted = false;
                    db.removeConnection(this);
                    db = null;
                }
            }
        }
    }
    
    public void removeStatement(DomSQLStatement statement) {
        statements.remove(statement);
    }

    private void closeMetaData() throws SQLException {
        if(meta!=null) {
            meta.close();
        }
    }
    private void closeStatements() throws SQLException {
        for(DomSQLStatement stmt:statements ) {
            stmt.release();
        }
        statements.clear();
    }

    public boolean isClosed() throws SQLException {
        return db==null;
    }
    
    public Database getDatabase() {
        return db;
    }
    
    public long getSqliteDb() {
        return db.getSqliteDb();
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int ms) throws SQLException {
        this.timeout = ms;
        SQLite.busyTimeout(getSqliteDb(),ms);
    }

    public String getUrl() {
        return db.getUrl();
    }

    private void checkConnectionOpen() throws SQLException {
        if(db==null) {
            throw DomSQLException.create(null,"The connection is closed");
        }
    }

    private void checkCursorOptions(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    	// We relax the constraint on the cursor type so a caller can use relative() methods 
        //if (resultSetType != ResultSet.TYPE_FORWARD_ONLY) {
        //    throw DomSQLException.create(null,"DomSQL cursors only supports TYPE_FORWARD_ONLY option");
        //}
        if (resultSetConcurrency != ResultSet.CONCUR_READ_ONLY) {
            throw DomSQLException.create(null,"DomSQL cursors only supports CONCUR_READ_ONLY option");
        }
        if (resultSetHoldability != ResultSet.CLOSE_CURSORS_AT_COMMIT) {
            throw DomSQLException.create(null,"DomSQL cursors only supports CLOSE_CURSORS_AT_COMMIT option");
        }
    }

    public String getCatalog() throws SQLException {
        checkConnectionOpen();
        return null;
    }

    public void setCatalog(String catalog) throws SQLException {
        checkConnectionOpen();
    }

    public int getHoldability() throws SQLException {
        checkConnectionOpen();
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }

    public void setHoldability(int holdability) throws SQLException {
        checkConnectionOpen();
        if (holdability != ResultSet.CLOSE_CURSORS_AT_COMMIT) {
            throw DomSQLException.create(null,"DomSQL cursors only supports CLOSE_CURSORS_AT_COMMIT option");
        }
    }

    public int getTransactionIsolation() {
        return TRANSACTION_SERIALIZABLE;
    }

    public void setTransactionIsolation(int level) throws SQLException {
        if (level != TRANSACTION_SERIALIZABLE) {
            throw DomSQLException.create(null,"DomSQL only supports TRANSACTION_SERIALIZABLE");
        }
    }

    public Map getTypeMap() throws SQLException {
        throw new NotImplementedException();
    }

    public void setTypeMap(Map map) throws SQLException {
        throw new NotImplementedException();
    }

    public boolean isReadOnly() throws SQLException {
        return false;
    }

    public void setReadOnly(boolean ro) throws SQLException {
    }

    public DomSQLDatabaseMetaData getMetaData() {
        if (meta == null) {
            meta = new DomSQLDatabaseMetaData(this);
        }
        return meta;
    }

    public String nativeSQL(String sql) {
        return sql;
    }

    public void clearWarnings() throws SQLException {
    }

    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    public boolean getAutoCommit() throws SQLException {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        if(this.autoCommit!=autoCommit) {
            if(transactionStarted) {
                rollback();
            }
            this.autoCommit = autoCommit;
        }
    }
    
    public void ensureTransactionStarted() throws SQLException {
        if(autoCommit) {
            return;
        }
        if(!transactionStarted) {
            exec("BEGIN TRANSACTION");
            transactionStarted = true;
        }
    }

    public void commit() throws SQLException {
        if(transactionStarted) {
            exec("COMMIT");
            transactionStarted = false;
        }
    }

    public void rollback() throws SQLException {
        if(transactionStarted) {
            exec("ROLLBACK");
            transactionStarted = false;
        }
    }

    public DomSQLStatement createStatement() throws SQLException {
        return createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    public DomSQLStatement createStatement(int resultSetType, int resultSetConcurrency)
            throws SQLException {
        return createStatement(resultSetType, resultSetConcurrency,
                ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    public DomSQLStatement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        checkCursorOptions(resultSetType, resultSetConcurrency, resultSetHoldability);
        DomSQLStatement st = new DomSQLStatement(this);
        statements.add(st);
        if(DomSQL.debug.TRACE_JAVA_SQL) {
            JNIUtils.trace("Connection {0}, Create Statement {1}",getObjectId(),st.getObjectId());
        }
        return st;
    }

    public DomSQLCallableStatement prepareCall(String sql) throws SQLException {
        return prepareCall(sql, ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    public DomSQLCallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException {
        return prepareCall(sql, resultSetType, resultSetConcurrency, ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    public DomSQLCallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        throw new SQLException("DomSQL does not support Stored Procedures");
    }

    public DomSQLPreparedStatement prepareStatement(String sql) throws SQLException {
        return prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
    }

    public DomSQLPreparedStatement prepareStatement(String sql, int autoCommit)
            throws SQLException {
        throw new NotImplementedException();
    }

    public DomSQLPreparedStatement prepareStatement(String sql, int[] colIndexes)
            throws SQLException {
        throw new NotImplementedException();
    }

    public DomSQLPreparedStatement prepareStatement(String sql, String[] colNames)
            throws SQLException {
        throw new NotImplementedException();
    }

    public DomSQLPreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException {
        return prepareStatement(sql, resultSetType, resultSetConcurrency,
                ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    public DomSQLPreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) throws SQLException {
        checkCursorOptions(resultSetType, resultSetConcurrency, resultSetHoldability);
        DomSQLPreparedStatement st = new DomSQLPreparedStatement(this, sql);
        if(DomSQL.debug.TRACE_JAVA_SQL) {
            JNIUtils.trace("Connection {0}, Prepare Statement {1}, SQL {2}",getObjectId(),st.getObjectId(),sql);
        }
        return st;
    }


    // UNUSED FUNCTIONS /////////////////////////////////////////////

    public Savepoint setSavepoint() throws SQLException {
        throw new NotImplementedException();
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        throw new NotImplementedException();
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        throw new NotImplementedException();
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        throw new NotImplementedException();
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        throw new NotImplementedException();
    }

    // JRE 1.5 //////////////////////////////////////////////////////

    //@Override
    public Array createArrayOf(String typeName, Object[] elements)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Blob createBlob() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Clob createClob() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public NClob createNClob() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public SQLXML createSQLXML() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Properties getClientInfo() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public String getClientInfo(String name) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public boolean isValid(int timeout) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setClientInfo(Properties properties)
            throws SQLClientInfoException {
        throw new NotImplementedException();
    }

    //@Override
    public void setClientInfo(String name, String value)
            throws SQLClientInfoException {
        throw new NotImplementedException();
    }

    //@Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new NotImplementedException();
    }
    
    
    // Similar to sqlite3_exec
    // "The sqlite3_exec() interface is a convenience wrapper around sqlite3_prepare_v2(), 
    // sqlite3_step(), and sqlite3_finalize(), that allows an application to run multiple 
    // statements of SQL without having to use a lot of C code."    
    private void exec(String sql) throws SQLException {
        // Update the last access timestamp
    	updateLastAccess();
        if(DomSQL.debug.TRACE_JAVA_SQL) {
            JNIUtils.trace("exec(sql): {0}",sql);
        }
        long hstmt = SQLite.prepare(getSqliteDb(),sql);
        try {
            int code = SQLite.step(hstmt);
            switch (code) {
                case SQLite.SQLITE_DONE:
                case SQLite.SQLITE_ROW:
                        return;
            }
            throwex(code);
        } finally {
            SQLite.finalize(hstmt);
        }
    }
}
