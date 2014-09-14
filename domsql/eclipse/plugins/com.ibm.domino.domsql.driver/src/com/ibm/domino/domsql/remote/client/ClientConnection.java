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
package com.ibm.domino.domsql.remote.client;

import java.rmi.RemoteException;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

import com.ibm.domino.domsql.remote.transport.IConnection;


/**
 *
 */
public class ClientConnection extends ClientObject implements Connection {
	
    private IConnection connection;
    
    public ClientConnection(IConnection connection) {
    	super(connection);
    	this.connection = connection;
    }
    
    public IConnection getRemoteObject() {
    	return connection;
    }
    
    
    // ===========================================================
    // Connection Implementation
    // ===========================================================
    
    public void clearWarnings() throws SQLException {
    	try {
    		connection.clearWarnings();
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public void close() throws SQLException {
    	try {
    		connection.close();
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public void commit() throws SQLException {
    	try {
    		connection.commit();
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public Statement createStatement() throws SQLException {
    	try {
    		return new ClientStatement(this,connection.createStatement());
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency)
            throws SQLException {
    	try {
    		return new ClientStatement(this,connection.createStatement(resultSetType,resultSetConcurrency));
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public Statement createStatement(int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
    	try {
    		return new ClientStatement(this,connection.createStatement(resultSetType,resultSetConcurrency,resultSetHoldability));
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public boolean getAutoCommit() throws SQLException {
    	try {
    		return connection.getAutoCommit();
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public String getCatalog() throws SQLException {
    	try {
    		return connection.getCatalog();
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public int getHoldability() throws SQLException {
    	try {
    		return connection.getHoldability();
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public DatabaseMetaData getMetaData() throws SQLException {
    	try {
    		return new ClientDatabaseMetaData(connection.getMetaData());
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public int getTransactionIsolation() throws SQLException {
    	try {
    		return connection.getTransactionIsolation();
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException {
    	try {
    		return connection.getTypeMap();
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public SQLWarning getWarnings() throws SQLException {
    	try {
    		throwNotImplementedException(); return null;
    		//return new ClientS(connection.getWarnings();
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public boolean isClosed() throws SQLException {
    	try {
    		return connection.isClosed();
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public boolean isReadOnly() throws SQLException {
    	try {
    		return connection.isReadOnly();
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public String nativeSQL(String sql) throws SQLException {
    	try {
    		return connection.nativeSQL(sql);
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
    	try {
    		return new ClientCallableStatement(this,connection.prepareCall(sql));
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public CallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
    	try {
    		return new ClientCallableStatement(this,connection.prepareCall(sql, resultSetType, resultSetConcurrency));
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public CallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
    	try {
    		return new ClientCallableStatement(this,connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability));
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
    	try {
    		return new ClientPreparedStatement(this,connection.prepareStatement(sql));
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
            throws SQLException {
    	try {
    		return  new ClientPreparedStatement(this,connection.prepareStatement(sql, autoGeneratedKeys));
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
            throws SQLException {
    	try {
    		return new ClientPreparedStatement(this,connection.prepareStatement(sql, columnIndexes));
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
    	try {
    		return new ClientPreparedStatement(this,connection.prepareStatement(sql, resultSetType, resultSetConcurrency));
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
    	try {
    		return new ClientPreparedStatement(this,connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability));
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames)
            throws SQLException {
    	try {
    		return new ClientPreparedStatement(this,connection.prepareStatement(sql, columnNames));
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    	try {
    		throwNotImplementedException();
    		//connection.releaseSavepoint(savepoint.get);
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public void rollback() throws SQLException {
    	try {
    		connection.rollback();
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public void rollback(Savepoint savepoint) throws SQLException {
    	try {
    		throwNotImplementedException();
    		//connection.
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
    	try {
    		connection.setAutoCommit(autoCommit);
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public void setCatalog(String catalog) throws SQLException {
    	try {
    		connection.setCatalog(catalog);
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public void setHoldability(int holdability) throws SQLException {
    	try {
    		connection.setHoldability(holdability);
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
    	try {
    		connection.setReadOnly(readOnly);
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public Savepoint setSavepoint() throws SQLException {
    	try {
    		return connection.setSavepoint();
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public Savepoint setSavepoint(String name) throws SQLException {
    	try {
    		return connection.setSavepoint(name);
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public void setTransactionIsolation(int level) throws SQLException {
    	try {
    		connection.setTransactionIsolation(level);
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
    	try {
    		connection.setTypeMap(map);
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public Clob createClob() throws SQLException {
    	try {
    		return connection.createClob();
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public Blob createBlob() throws SQLException {
    	try {
    		return connection.createBlob();
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public NClob createNClob() throws SQLException {
    	try {
    		return connection.createNClob();
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public SQLXML createSQLXML() throws SQLException {
    	try {
    		return connection.createSQLXML();
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public boolean isValid(int timeout) throws SQLException {
    	try {
    		return connection.isValid(timeout);
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public void setClientInfo(String name, String value)
            throws SQLClientInfoException {
    	try {
    		connection.setClientInfo(name, value);
    	} catch(RemoteException t) {
    		throw newSQLClientInfoException(t);
    	}
    }

    public void setClientInfo(Properties properties)
            throws SQLClientInfoException {
    	try {
    		connection.setClientInfo(properties);
    	} catch(RemoteException t) {
    		throw newSQLClientInfoException(t);
    	}
    }

    public String getClientInfo(String name) throws SQLException {
    	try {
    		return connection.getClientInfo(name);
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public Properties getClientInfo() throws SQLException {
    	try {
    		return connection.getClientInfo();
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public Array createArrayOf(String typeName, Object[] elements)
            throws SQLException {
    	try {
    		return connection.createArrayOf(typeName, elements);
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }

    public Struct createStruct(String typeName, Object[] attributes)
            throws SQLException {
    	try {
    		return connection.createStruct(typeName, attributes);
    	} catch(RemoteException t) {
    		throw newSQLException(t);
    	}
    }
}
