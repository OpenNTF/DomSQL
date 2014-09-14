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
package com.ibm.domino.domsql.remote.server;

import java.lang.Thread.UncaughtExceptionHandler;
import java.rmi.RemoteException;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.Map;
import java.util.Properties;

import com.ibm.domino.domsql.remote.transport.ICallableStatement;
import com.ibm.domino.domsql.remote.transport.IConnection;
import com.ibm.domino.domsql.remote.transport.IDatabaseMetaData;
import com.ibm.domino.domsql.remote.transport.IPreparedStatement;
import com.ibm.domino.domsql.remote.transport.IStatement;
import com.ibm.domino.domsql.remote.transport.SArray;
import com.ibm.domino.domsql.remote.transport.SBlob;
import com.ibm.domino.domsql.remote.transport.SClob;
import com.ibm.domino.domsql.remote.transport.SNClob;
import com.ibm.domino.domsql.remote.transport.SSQLXML;
import com.ibm.domino.domsql.remote.transport.SSavepoint;
import com.ibm.domino.domsql.remote.transport.SStruct;
import com.ibm.domino.domsql.sqlite.driver.Context;
import com.ibm.domino.domsql.sqlite.driver.jdbc.DomSQLConnection;
import com.ibm.dots.session.SessionContext;


/**
 *
 */
public class ServerConnection extends ServerObject implements IConnection {

	private static final long serialVersionUID = 1L;

    private SessionContext sessionContext;
    private DomSQLConnection localConnection;
    private Context context;
    
    // For debug information
    private Object returnValue;
	private UncaughtExceptionHandler oldExceptionHandler;
    
    public ServerConnection(Context context) throws RemoteException {
        super(null);
        this.sessionContext = new SessionContext();
        this.context = context;
    }

    public void unreferenced() {
		try {
			if(localConnection!=null) {
				try {
					if(!localConnection.getAutoCommit()) {
						localConnection.rollback();
					}
				} catch(Exception e) {
					// ignore
				}
				localConnection.close();
				localConnection = null;
			}
		} catch(Exception e) {
			localConnection = null;
		}
	}

	protected void finalize() throws Throwable {
		// Might have been nullified by unreferenced...
		if(localConnection!=null) {
			localConnection.close();
			localConnection = null;
		}
	}

    
    public SessionContext getSessionContext() {
        return sessionContext;
    }
    
    public ServerConnection getRemoteConnection() {
        return this;
    }

    public DomSQLConnection getNative() {
        return localConnection;
    }

    public void setNative(DomSQLConnection connection) {
        this.localConnection = connection;
    }

    public Context getContext() {
        return context;
    }
    
    public void initConnection() throws SQLException {
        sessionContext.initSession();
    }
    
    public void termConnection() throws SQLException {
    	if(sessionContext!=null) {
    		sessionContext.termSession();
    		sessionContext = null;
    	}
    }

    
    // ===========================================================
    // Debug
    // ===========================================================
    
    public Object getReturnValue() {
    	return returnValue;
    }
    
    public void setReturnValue(Object returnValue) {
    	this.returnValue = returnValue;
    }
    
    public UncaughtExceptionHandler getOldUncaughtExceptionHandler() {
    	return oldExceptionHandler;
    }
    
    public void setOldUncaughtExceptionHandler(UncaughtExceptionHandler oldExceptionHandler) {
    	this.oldExceptionHandler = oldExceptionHandler;
    }
    
    
    // ===========================================================
    // Base methods
    // ===========================================================

    public <T> T unwrap(Class<T> iface) throws SQLException, RemoteException {
        return localConnection.unwrap(iface);
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException, RemoteException {
        return localConnection.isWrapperFor(iface);
    }

    
    // ===========================================================
    // Delegation
    // ===========================================================
    
    public void clearWarnings() throws SQLException, RemoteException {
        initContext();
        try {
            localConnection.clearWarnings();
        } finally {
            termContext();
        }
    }

    public void close() throws SQLException, RemoteException {
        try {
            initContext();
            try {
                localConnection.close();
                context.close();
            } finally {
                termContext();
            }
        } finally {
            termConnection();
        }
    }

    public void commit() throws SQLException, RemoteException {
        initContext();
        try {
            localConnection.commit();
        } finally {
            termContext();
        }
    }

    public IStatement createStatement() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.createStatement());
        } finally {
            termContext();
        }
    }

    public IStatement createStatement(int resultSetType, int resultSetConcurrency)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.createStatement(resultSetType,
                    resultSetConcurrency));
        } finally {
            termContext();
        }
    }

    public IStatement createStatement(int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.createStatement(resultSetType,
                    resultSetConcurrency, resultSetHoldability));
        } finally {
            termContext();
        }
    }

    public boolean getAutoCommit() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.getAutoCommit());
        } finally {
            termContext();
        }
    }

    public String getCatalog() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.getCatalog());
        } finally {
            termContext();
        }
    }

    public int getHoldability() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.getHoldability());
        } finally {
            termContext();
        }
    }

    public IDatabaseMetaData getMetaData() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.getMetaData());
        } finally {
            termContext();
        }
    }

    public int getTransactionIsolation() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.getTransactionIsolation());
        } finally {
            termContext();
        }
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.getTypeMap());
        } finally {
            termContext();
        }
    }

    public SQLWarning getWarnings() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.getWarnings());
        } finally {
            termContext();
        }
    }

    public boolean isClosed() throws SQLException, RemoteException {
        // The connection can already be closed at that time
        // so we don't have a context anymore...
        // We then don't deal with contexts here...
        //initContext();
        //try {
            return wrap(localConnection.isClosed());
        //} finally {
        //    termContext();
        //}
    }

    public boolean isReadOnly() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.isReadOnly());
        } finally {
            termContext();
        }
    }

    public String nativeSQL(String sql) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.nativeSQL(sql));
        } finally {
            termContext();
        }
    }

    public ICallableStatement prepareCall(String sql) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.prepareCall(sql));
        } finally {
            termContext();
        }
    }

    public ICallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.prepareCall(sql, resultSetType,
                    resultSetConcurrency));
        } finally {
            termContext();
        }
    }

    public ICallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.prepareCall(sql, resultSetType,
                    resultSetConcurrency, resultSetHoldability));
        } finally {
            termContext();
        }
    }

    public IPreparedStatement prepareStatement(String sql) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.prepareStatement(sql));
        } finally {
            termContext();
        }
    }

    public IPreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.prepareStatement(sql, autoGeneratedKeys));
        } finally {
            termContext();
        }
    }

    public IPreparedStatement prepareStatement(String sql, int[] columnIndexes)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.prepareStatement(sql, columnIndexes));
        } finally {
            termContext();
        }
    }

    public IPreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.prepareStatement(sql, resultSetType,
                    resultSetConcurrency));
        } finally {
            termContext();
        }
    }

    public IPreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.prepareStatement(sql, resultSetType,
                    resultSetConcurrency, resultSetHoldability));
        } finally {
            termContext();
        }
    }

    public IPreparedStatement prepareStatement(String sql, String[] columnNames)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.prepareStatement(sql, columnNames));
        } finally {
            termContext();
        }
    }

    public void releaseSavepoint(SSavepoint savepoint) throws SQLException, RemoteException {
        initContext();
        try {
            localConnection.releaseSavepoint(savepoint);
        } finally {
            termContext();
        }
    }

    public void rollback() throws SQLException, RemoteException {
        initContext();
        try {
            localConnection.rollback();
        } finally {
            termContext();
        }
    }

    public void rollback(SSavepoint savepoint) throws SQLException, RemoteException {
        initContext();
        try {
            localConnection.rollback(savepoint);
        } finally {
            termContext();
        }
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException, RemoteException {
        initContext();
        try {
            localConnection.setAutoCommit(autoCommit);
        } finally {
            termContext();
        }
    }

    public void setCatalog(String catalog) throws SQLException, RemoteException {
        initContext();
        try {
            localConnection.setCatalog(catalog);
        } finally {
            termContext();
        }
    }

    public void setHoldability(int holdability) throws SQLException, RemoteException {
        initContext();
        try {
            localConnection.setHoldability(holdability);
        } finally {
            termContext();
        }
    }

    public void setReadOnly(boolean readOnly) throws SQLException, RemoteException {
        initContext();
        try {
            localConnection.setReadOnly(readOnly);
        } finally {
            termContext();
        }
    }

    public SSavepoint setSavepoint() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SSavepoint.create(localConnection.setSavepoint()));
        } finally {
            termContext();
        }
    }

    public SSavepoint setSavepoint(String name) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SSavepoint.create(localConnection.setSavepoint(name)));
        } finally {
            termContext();
        }
    }

    public void setTransactionIsolation(int level) throws SQLException, RemoteException {
        initContext();
        try {
            localConnection.setTransactionIsolation(level);
        } finally {
            termContext();
        }
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException, RemoteException {
        initContext();
        try {
            localConnection.setTypeMap(map);
        } finally {
            termContext();
        }
    }

    public SClob createClob() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SClob.create(localConnection.createClob()));
        } finally {
            termContext();
        }
    }

    public SBlob createBlob() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SBlob.create(localConnection.createBlob()));
        } finally {
            termContext();
        }
    }

    public SNClob createNClob() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SNClob.create(localConnection.createNClob()));
        } finally {
            termContext();
        }
    }

    public SSQLXML createSQLXML() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SSQLXML.create(localConnection.createSQLXML()));
        } finally {
            termContext();
        }
    }

    public boolean isValid(int timeout) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.isValid(timeout));
        } finally {
            termContext();
        }
    }

    public void setClientInfo(String name, String value)
            throws SQLClientInfoException, RemoteException {
        initContext();
        try {
            localConnection.setClientInfo(name, value);
        } finally {
            termContext();
        }
    }

    public void setClientInfo(Properties properties)
            throws SQLClientInfoException, RemoteException {
        initContext();
        try {
            localConnection.setClientInfo(properties);
        } finally {
            termContext();
        }
    }

    public String getClientInfo(String name) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.getClientInfo(name));
        } finally {
            termContext();
        }
    }

    public Properties getClientInfo() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(localConnection.getClientInfo());
        } finally {
            termContext();
        }
    }

    public SArray createArrayOf(String typeName, Object[] elements)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SArray.create(localConnection.createArrayOf(typeName, elements)));
        } finally {
            termContext();
        }
    }

    public SStruct createStruct(String typeName, Object[] attributes)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SStruct.create(localConnection.createStruct(typeName, attributes)));
        } finally {
            termContext();
        }
    }
}
