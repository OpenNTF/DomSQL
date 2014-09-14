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
package com.ibm.domino.domsql.remote.client;

import java.rmi.RemoteException;
import java.sql.ParameterMetaData;
import java.sql.SQLException;

import com.ibm.domino.domsql.remote.transport.IParameterMetaData;

public class ClientParameterMetaData extends ClientObject implements ParameterMetaData {

    private IParameterMetaData metaData;

    public ClientParameterMetaData(IParameterMetaData metaData) {
    	super(metaData);
    	this.metaData = metaData;
    }
    
    public IParameterMetaData getRemoteObject() {
    	return metaData;
    }

	public String getParameterClassName(int paramIndex) throws SQLException {
		try {
			return metaData.getParameterClassName(paramIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getParameterCount() throws SQLException {
		try {
			return metaData.getParameterCount();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getParameterMode(int paramIndex) throws SQLException {
		try {
			return metaData.getParameterMode(paramIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getParameterType(int paramIndex) throws SQLException {
		try {
			return metaData.getParameterType(paramIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getParameterTypeName(int paramIndex) throws SQLException {
		try {
			return metaData.getParameterTypeName(paramIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getPrecision(int paramIndex) throws SQLException {
		try {
			return metaData.getPrecision(paramIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getScale(int paramIndex) throws SQLException {
		try {
			return metaData.getScale(paramIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int isNullable(int paramIndex) throws SQLException {
		try {
			return metaData.isNullable(paramIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean isSigned(int paramIndex) throws SQLException {
		try {
			return metaData.isSigned(paramIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}
}
