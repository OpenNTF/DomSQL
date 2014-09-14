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
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.Wrapper;

import com.ibm.domino.domsql.remote.transport.IRemoteObject;


/**
 *
 */
public abstract class ClientObject implements Wrapper {
	
	private IRemoteObject object;
	
    public ClientObject(IRemoteObject object) {
    	this.object = object;
    }
    
    public IRemoteObject getRemoteObject() {
    	return object;
    }

    public static SQLException newSQLException(Throwable t) {
    	SQLException e = new SQLException();
    	e.initCause(t);
    	return e;
    }

    public static SQLClientInfoException newSQLClientInfoException(Throwable t) {
    	SQLClientInfoException e = new SQLClientInfoException();
    	e.initCause(t);
    	return e;
    }
    
    protected SQLException notImplementedException() throws SQLException {
    	return new SQLException("Method not implemented");
    }

    protected void throwNotImplementedException() throws SQLException, RemoteException {
    	throw new SQLException("Method not implemented");
    }
    
    
    // ===========================================================
    // Base methods
    // ===========================================================

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
