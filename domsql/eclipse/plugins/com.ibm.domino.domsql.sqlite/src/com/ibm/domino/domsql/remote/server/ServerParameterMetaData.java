/*
 * © Copyright IBM Corp. 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); } finally { termContext(); } 
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

import java.rmi.RemoteException;
import java.sql.SQLException;

import com.ibm.domino.domsql.remote.transport.IParameterMetaData;
import com.ibm.domino.domsql.sqlite.driver.jdbc.DomSQLParameterMetaData;

public class ServerParameterMetaData extends ServerObject implements IParameterMetaData {

	private static final long serialVersionUID = 1L;
	
    private DomSQLParameterMetaData metaData;

    public ServerParameterMetaData(ServerConnection connection, DomSQLParameterMetaData metaData) throws RemoteException {
        super(connection);
        this.metaData = metaData;
    }
    
    public DomSQLParameterMetaData getNative() {
    	return metaData;
    }

    public void unreferenced() {
    	metaData = null;
    }

    // ===========================================================
    // Delegation
    // ===========================================================

    public <T> T unwrap(Class<T> iface) throws SQLException, RemoteException {
        return metaData.unwrap(iface);
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException, RemoteException {
        return metaData.isWrapperFor(iface);
    }

	public String getParameterClassName(int paramIndex) throws SQLException, RemoteException {
		throw notImplementedException();
	}

	public int getParameterCount() throws SQLException, RemoteException {
		throw notImplementedException();
	}

	public int getParameterMode(int paramIndex) throws SQLException, RemoteException {
		throw notImplementedException();
	}

	public int getParameterType(int paramIndex) throws SQLException, RemoteException {
		throw notImplementedException();
	}

	public String getParameterTypeName(int paramIndex) throws SQLException, RemoteException {
		throw notImplementedException();
	}

	public int getPrecision(int paramIndex) throws SQLException, RemoteException {
		throw notImplementedException();
	}

	public int getScale(int paramIndex) throws SQLException, RemoteException {
		throw notImplementedException();
	}

	public int isNullable(int paramIndex) throws SQLException, RemoteException {
		throw notImplementedException();
	}

	public boolean isSigned(int paramIndex) throws SQLException, RemoteException {
		throw notImplementedException();
	}

}
