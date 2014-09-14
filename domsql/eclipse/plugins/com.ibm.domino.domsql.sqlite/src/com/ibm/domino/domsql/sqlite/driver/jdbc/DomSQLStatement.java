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

import java.sql.BatchUpdateException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ibm.commons.util.NotImplementedException;
import com.ibm.commons.util.StringUtil;
import com.ibm.domino.domsql.sqlite.DomSQLException;
import com.ibm.domino.domsql.sqlite.driver.SQLite;
import com.ibm.domino.domsql.sqlite.driver.jni.DomSQL;
import com.ibm.domino.domsql.sqlite.driver.jni.JNIUtils;

public class DomSQLStatement extends DomSQLObject implements Statement {
    
    private DomSQLConnection sqlConnection;
    private DomSQLResultSet resultSet;

    private long hStmt;

    private static class BatchEntry {
        private String      sql;
        private Object[]    parameters;
        public BatchEntry(String sql, Object[] parameters) {
            this.sql = sql;
            this.parameters = parameters;
        }
        public String getSql() {
            return sql;
        }
        public Object[] getParameters() {
            return parameters;
        }
    }
    private List<BatchEntry> batchEntries;

    private int maxRows;
    private int fetchSize;
    private int fetchDirection;
    

    public DomSQLStatement(DomSQLConnection sqlConnection) {
        this.sqlConnection = sqlConnection;
        this.fetchDirection = ResultSet.FETCH_FORWARD;
        // Update the last access timestamp
    	updateLastAccess();
    }
    
    public long getSqliteDb() {
        return getConnection().getSqliteDb();
    }
    
    public DomSQLConnection getConnection() {
        return sqlConnection;
    }
    
    public long getHstmt() {
        return hStmt;
    }
    
    public void prepare(String sql) throws SQLException {
        closeStatement();
        this.hStmt = SQLite.prepare(getSqliteDb(),sql);
        if(DomSQL.debug.TRACE_JAVA_SQL) {
            JNIUtils.trace("Statement {0}, hstmt: {1}, Prepare: {2}",getObjectId(),hStmt,sql);
        }
    }

    protected DomSQLResultSet initResultSet(boolean empty) throws SQLException {
        int nCol = SQLite.columnCount(getHstmt()); 
        if(nCol==0) {
            throw new SQLException("No ResultSet is available");
        }
        resultSet = new DomSQLResultSet(this,nCol,empty);
        return resultSet;
    }

    public void resetStatement() throws SQLException {
        closeResultSet();
        // We reset the statement to bind new values
        if(hStmt!=0) {
            SQLite.reset(hStmt);
        }
    }
    
    public void release() throws SQLException {
        closeResultSet();
        closeStatement();
    }

    public void closeResultSet() throws SQLException {
        if(resultSet!=null) {
            resultSet.release();
            resultSet = null;
        }
    }

    public void closeStatement() throws SQLException {
    	closeStatement(false);
    }
    public void closeStatement(boolean throwException) throws SQLException {
        if(hStmt!=0) {
            if(DomSQL.debug.TRACE_JAVA_SQL) {
                JNIUtils.trace("Statement {0}, Close, hstmt: {1}",getObjectId(),hStmt);
            }
            // Update the last access timestamp
        	updateLastAccess();
        	int status = SQLite.finalize(hStmt);
        	hStmt = 0; // Make sure it is set to empty, whatever happens
        	if(throwException) {
        		if (status != SQLite.SQLITE_OK && status != SQLite.SQLITE_MISUSE) {
        			getConnection().throwex();
        		}
        	}
        } else {
            if(DomSQL.debug.TRACE_JAVA_SQL) {
                JNIUtils.trace("Statement {0}, Close empty statement",getObjectId());
            }
        }
    }

    protected final void checkStatementOpen() throws SQLException {
        if(hStmt==0) {
            throw DomSQLException.create(null,"Statement {0} is closed",getObjectId());
        }
    }

    protected boolean isOpen() throws SQLException {
        return hStmt!=0;
    }

    protected boolean exec() throws SQLException {
        if(resultSet!=null) {
            throw DomSQLException.create(null,"The previous ResultSet is not closed");
        }
        boolean hasRows = executeParams(null);
        
        // We have a result set if we have at least one column returned by SQLite
        if(SQLite.columnCount(getHstmt()) != 0) {
            initResultSet(!hasRows);
            return true;
        }
        return false;
    }

    
    // Internal methods executing SQL ////////////////////////////////

    protected int executeUpdateParams(Object[] parameters) throws SQLException {
        if(executeParams(parameters)) {
            throw DomSQLException.create(null,"executeUpdate() cannot return a result set");
        }
        // Reset won't be done when closing the result set, so do it here
        if(getHstmt()!=0) {
        	SQLite.reset(getHstmt());
        }
        return SQLite.changes(getSqliteDb());
    }
    
    protected boolean executeParams(Object[] parameters) throws SQLException {
    	try {
	        // Ensure that a transaction is properly started
	    	getConnection().ensureTransactionStarted();
	        
	        // Then, bind the parameters
	        if (parameters != null) {
	            int pCount = parameters.length;
	            for (int i = 0; i < pCount; i++) {
	                if (sqlbind(getHstmt(), i, parameters[i]) != SQLite.SQLITE_OK) {
	                    throwex();
	                }
	            }
	        }
	        
	        // And execute the statement
	        int status = SQLite.step(getHstmt()); 
	        switch (status) {
	            case SQLite.SQLITE_DONE:
	                // Reset won't be done when closing the result set, so do it here
	                SQLite.reset(getHstmt());
	                return false;
	            case SQLite.SQLITE_ROW:
	                // Ok, the caller should get the result set
	                return true;
	        }
	        
	        // Terminate the statement...
	        closeStatement(false);
	        throw DomSQLException.create(null,formatErrorMessage());
    	} finally {
	    	if(getConnection().getAutoCommit()) {
	    		//getConnection().commit();
	    	}
    	}
    }
    
    public int sqlbind(long stmt, int pos, Object v) throws SQLException {
        pos++;
        if (v == null) {
            return SQLite.bindNull(stmt, pos);
        } else if(v instanceof String) {
            return SQLite.bindText(stmt, pos, (String) v);
        } else if(v instanceof Number) {
            if(v instanceof Integer) {
                return SQLite.bindInt(stmt, pos, ((Number) v).intValue());
            } else if(v instanceof Double) {
                return SQLite.bindDouble(stmt, pos, ((Number) v).doubleValue());
            } else if(v instanceof Long) {
                return SQLite.bindInt64(stmt, pos, ((Number) v).longValue());
            } else if(v instanceof Byte) {
                return SQLite.bindInt(stmt, pos, ((Number) v).intValue());
            } else if(v instanceof Short) {
                return SQLite.bindInt(stmt, pos, ((Number) v).intValue());
            } else if(v instanceof Float) {
                return SQLite.bindDouble(stmt, pos, ((Number) v).doubleValue());
            } else {
                // other like BigDecimal: assume a double
                return SQLite.bindDouble(stmt, pos, ((Number) v).doubleValue());
            }
        } else if(v instanceof java.util.Date) {
            return SQLite.bindInt64(stmt, pos, DomSQL.toSQLiteDate((java.util.Date)v));
        } else if(v instanceof byte[]) {
            return SQLite.bindBlob(stmt, pos, (byte[]) v);
        } else {
            // Assume a string...
            return SQLite.bindText(stmt, pos, v.toString());
        }
    }
    
    
    // PUBLIC INTERFACE /////////////////////////////////////////////

    public void close() throws SQLException {
        release();
        if(sqlConnection!=null) {
            sqlConnection.removeStatement(this);
        }
    }

    public boolean execute(String sql) throws SQLException {
        if(DomSQL.debug.TRACE_JAVA_SQL) {
            JNIUtils.trace("Statement {0}, Execute: {1}",getObjectId(),sql);
        }
        prepare(sql);
        return exec();
    }

    public DomSQLResultSet executeQuery(String sql) throws SQLException {
        if(DomSQL.debug.TRACE_JAVA_SQL) {
            JNIUtils.trace("Statement {0}, ExecuteQuery: {1}",getObjectId(),sql);
        }
        prepare(sql);
        if(!exec()) {
            closeStatement();
            throw new SQLException("Query does not return ResultSet");
        }
        return getResultSet();
    }

    public int executeUpdate(String sql) throws SQLException {
        try {
            if(DomSQL.debug.TRACE_JAVA_SQL) {
                JNIUtils.trace("Statement {0}, ExecuteUpdate: {1}",getObjectId(),sql);
            }
            prepare(sql);
            return executeUpdateParams(null);
        } finally {
            closeStatement();
        }
    }

    public DomSQLResultSet getResultSet() throws SQLException {
        return resultSet;
    }

    /*
     * This function has a complex behaviour best understood by carefully reading the JavaDoc for getMoreResults() and considering
     * the test StatementTest.execute().
     */
    public int getUpdateCount() throws SQLException {
        if (getHstmt()!=0 
                && resultSet==null 
                && SQLite.columnCount(getHstmt()) == 0) {
            return SQLite.changes(getSqliteDb());
        }
        return -1;
    }

    public void addBatch(String sql) throws SQLException {
        _addBatch(sql, null);
    }

    protected void _addBatch(String sql, Object[] parameters) throws SQLException {
        closeStatement();
        if(batchEntries==null) {
            batchEntries = new ArrayList<BatchEntry>();
        }
        batchEntries.add(new BatchEntry(sql, parameters));
    }
    
    public void clearBatch() throws SQLException {
        if(batchEntries!=null) {
            batchEntries.clear();
        }
    }

    public int[] executeBatch() throws SQLException {
        closeStatement();
        if(batchEntries!=null && !batchEntries.isEmpty()) {
            int[] result = new int[batchEntries.size()];
            try {
                for (int i = 0; i < batchEntries.size(); i++) {
                    BatchEntry batch = batchEntries.get(i);
                    try {
                        if(DomSQL.debug.TRACE_JAVA_SQL) {
                            JNIUtils.trace("Statement ExecuteBatch: {0}",batch.getSql());
                        }
                        this.hStmt = SQLite.prepare(getSqliteDb(),batch.getSql());
                        result[i] = executeUpdateParams(batch.getParameters());
                    } catch (SQLException e) {
                        throw new BatchUpdateException(StringUtil.format("Error while executing batch entry #{0}: {1}", e.getMessage()), result);
                    } finally {
                        closeStatement();
                    }
                }
            } finally {
                clearBatch();
            }
            return result;
        }
        
        return new int[]{};
    }

    public void setCursorName(String name) {
    }

    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    public void clearWarnings() throws SQLException {
    }

    public void cancel() throws SQLException {
        if(resultSet!=null) {
            SQLite.interrupt(getSqliteDb());
            closeResultSet();
        }
    }

    public int getQueryTimeout() throws SQLException {
        return getConnection().getTimeout();
    }

    public void setQueryTimeout(int seconds) throws SQLException {
        if (seconds < 0) {
            throw DomSQLException.create(null,"Invalid QueryTimeout value. Must be greater than 0");
        }
        getConnection().setTimeout(1000 * seconds);
    }

    public int getMaxRows() throws SQLException {
        return maxRows;
    }

    public void setMaxRows(int max) throws SQLException {
        if(max < 0) {
            throw DomSQLException.create(null,"Invalid MaxRows value. Must be greater than 0");
        }
        this.maxRows = max;
    }

    public int getMaxFieldSize() throws SQLException {
        return 0;
    }

    public void setMaxFieldSize(int max) throws SQLException {
        if (max < 0) {
            throw DomSQLException.create(null,"Invalid MaxFieldSize value. Must be greater than 0");
        }
    }

    public int getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(int fetchSize) throws SQLException {
        if (fetchSize<0) {
            throw DomSQLException.create(null,"Invalid FetchSize value. Must be greater than 0.");
        }
        this.fetchSize = fetchSize;
    }

    public int getFetchDirection() {
        return fetchDirection;
    }

    public void setFetchDirection(int fetchDirection) throws SQLException {
        if (fetchDirection!=ResultSet.FETCH_FORWARD){
            throw DomSQLException.create(null,"SQLite only supports FETCH_FORWARD");
        }
        this.fetchDirection = fetchDirection;
    }

    /**
     * As SQLite's last_insert_rowid() function is DB-specific not statement
     * specific, this function introduces a race condition if the same
     * connection is used by two threads and both insert.
     */
    public DomSQLResultSet getGeneratedKeys() throws SQLException {
        return ((DomSQLDatabaseMetaData) getConnection().getMetaData()).getGeneratedKeys();
    }

    /** SQLite does not support multiple results from execute(). */
    public boolean getMoreResults() throws SQLException {
        return getMoreResults(0);
    }

    public boolean getMoreResults(int c) throws SQLException {
        checkStatementOpen();
        close(); // as we never have another result, clean up pointer
        return false;
    }

    public int getResultSetConcurrency() throws SQLException {
        return ResultSet.CONCUR_READ_ONLY;
    }

    public int getResultSetHoldability() throws SQLException {
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }

    public int getResultSetType() throws SQLException {
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    public void setEscapeProcessing(boolean enable) {
    }

    //@Override
    public boolean execute(String sql, int autoGeneratedKeys)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public boolean execute(String sql, String[] columnNames)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public int executeUpdate(String sql, int autoGeneratedKeys)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public int executeUpdate(String sql, int[] columnIndexes)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public int executeUpdate(String sql, String[] columnNames)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public boolean isClosed() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public boolean isPoolable() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setPoolable(boolean poolable) throws SQLException {
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
}
