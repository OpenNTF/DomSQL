/*
 * � Copyright IBM Corp. 2010
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
package com.ibm.domino.domsql.remote.client;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import com.ibm.domino.domsql.remote.transport.IStatement;

/**
 *
 */
public class ClientStatement extends ClientObject implements Statement {

	private ClientConnection connection;
	private IStatement statement;
	
    public ClientStatement(ClientConnection connection, IStatement statement) {
    	super(statement);
    	this.connection = connection;
        this.statement = statement;
    }
    
    public IStatement getRemoteObject() {
    	return statement;
    }

	public void addBatch(String sql) throws SQLException {
		try {
			statement.addBatch(sql);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void cancel() throws SQLException {
		try {
			statement.cancel();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void clearBatch() throws SQLException {
		try {
			statement.clearBatch();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void clearWarnings() throws SQLException {
		try {
			statement.clearWarnings();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void close() throws SQLException {
		try {
			statement.close();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean execute(String sql) throws SQLException {
		try {
			return statement.execute(sql);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		try {
			return statement.execute(sql, autoGeneratedKeys);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		try {
			return statement.execute(sql, columnIndexes);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean execute(String sql, String[] columnNames) throws SQLException {
		try {
			return statement.execute(sql, columnNames);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int[] executeBatch() throws SQLException {
		try {
			return statement.executeBatch();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		try {
			return new ClientResultSet(this,statement.executeQuery(sql));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int executeUpdate(String sql) throws SQLException {
		try {
			return statement.executeUpdate(sql);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		try {
			return statement.executeUpdate(sql, autoGeneratedKeys);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		try {
			return statement.executeUpdate(sql, columnIndexes);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		try {
			return statement.executeUpdate(sql, columnNames);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Connection getConnection() throws SQLException {
		return connection;
	}

	public int getFetchDirection() throws SQLException {
		try {
			return statement.getFetchDirection();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getFetchSize() throws SQLException {
		try {
			return statement.getFetchSize();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		try {
			return new ClientResultSet(this,statement.getGeneratedKeys());
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxFieldSize() throws SQLException {
		try {
			return statement.getMaxFieldSize();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxRows() throws SQLException {
		try {
			return statement.getMaxRows();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean getMoreResults() throws SQLException {
		try {
			return statement.getMoreResults();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean getMoreResults(int current) throws SQLException {
		try {
			return statement.getMoreResults(current);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getQueryTimeout() throws SQLException {
		try {
			return statement.getQueryTimeout();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getResultSet() throws SQLException {
		try {
			return new ClientResultSet(this,statement.getResultSet());
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getResultSetConcurrency() throws SQLException {
		try {
			return statement.getResultSetConcurrency();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getResultSetHoldability() throws SQLException {
		try {
			return statement.getResultSetHoldability();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getResultSetType() throws SQLException {
		try {
			return statement.getResultSetType();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getUpdateCount() throws SQLException {
		try {
			return statement.getUpdateCount();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public SQLWarning getWarnings() throws SQLException {
		try {
			return statement.getWarnings();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setCursorName(String name) throws SQLException {
		try {
			statement.setCursorName(name);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setEscapeProcessing(boolean enable) throws SQLException {
		try {
			statement.setEscapeProcessing(enable);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setFetchDirection(int direction) throws SQLException {
		try {
			statement.setFetchDirection(direction);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setFetchSize(int rows) throws SQLException {
		try {
			statement.setFetchSize(rows);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setMaxFieldSize(int max) throws SQLException {
		try {
			statement.setMaxFieldSize(max);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setMaxRows(int max) throws SQLException {
		try {
			statement.setMaxRows(max);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setQueryTimeout(int seconds) throws SQLException {
		try {
			statement.setQueryTimeout(seconds);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean isClosed() throws SQLException {
		try {
			return statement.isClosed();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setPoolable(boolean poolable) throws SQLException {
		try {
			statement.setPoolable(poolable);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean isPoolable() throws SQLException {
		try {
			return statement.isPoolable();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}
    
}
