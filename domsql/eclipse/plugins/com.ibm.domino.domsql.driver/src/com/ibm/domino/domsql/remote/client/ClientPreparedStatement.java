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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import com.ibm.domino.domsql.remote.transport.IPreparedStatement;
import com.ibm.domino.domsql.remote.transport.SerializableObject;

/**
 *
 */
public class ClientPreparedStatement extends ClientStatement implements PreparedStatement {

	private IPreparedStatement preparedStatement;
	
    public ClientPreparedStatement(ClientConnection connection, IPreparedStatement preparedStatement) {
    	super(connection,preparedStatement);
        this.preparedStatement = preparedStatement;
    }
    
    public IPreparedStatement getRemoteObject() {
    	return preparedStatement;
    }

	public void addBatch() throws SQLException {
		try {
			preparedStatement.addBatch();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void clearParameters() throws SQLException {
		try {
			preparedStatement.clearParameters();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean execute() throws SQLException {
		try {
			return preparedStatement.execute();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet executeQuery() throws SQLException {
		try {
			return new ClientResultSet(this,preparedStatement.executeQuery());
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int executeUpdate() throws SQLException {
		try {
			return preparedStatement.executeUpdate();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		try {
			return new ClientResultSetMetaData(preparedStatement.getMetaData());
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		try {
			return new ClientParameterMetaData(preparedStatement.getParameterMetaData());
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setArray(int parameterIndex, Array theArray) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.setArray(parameterIndex, theArray);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setAsciiStream(int parameterIndex, InputStream theInputStream, int length) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.setA;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setBigDecimal(int parameterIndex, BigDecimal theBigDecimal) throws SQLException {
		try {
			preparedStatement.setBigDecimal(parameterIndex, theBigDecimal);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setBinaryStream(int parameterIndex, InputStream theInputStream, int length) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setBlob(int parameterIndex, Blob theBlob) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setBoolean(int parameterIndex, boolean theBoolean) throws SQLException {
		try {
			preparedStatement.setBoolean(parameterIndex, theBoolean);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setByte(int parameterIndex, byte theByte) throws SQLException {
		try {
			preparedStatement.setByte(parameterIndex, theByte);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setBytes(int parameterIndex, byte[] theBytes) throws SQLException {
		try {
			preparedStatement.setBytes(parameterIndex, theBytes);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setClob(int parameterIndex, Clob theClob) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setDate(int parameterIndex, Date theDate) throws SQLException {
		try {
			preparedStatement.setDate(parameterIndex, theDate);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setDate(int parameterIndex, Date theDate, Calendar cal) throws SQLException {
		try {
			preparedStatement.setDate(parameterIndex, theDate, cal);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setDouble(int parameterIndex, double theDouble) throws SQLException {
		try {
			preparedStatement.setDouble(parameterIndex, theDouble);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setFloat(int parameterIndex, float theFloat) throws SQLException {
		try {
			preparedStatement.setFloat(parameterIndex, theFloat);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setInt(int parameterIndex, int theInt) throws SQLException {
		try {
			preparedStatement.setInt(parameterIndex, theInt);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setLong(int parameterIndex, long theLong) throws SQLException {
		try {
			preparedStatement.setLong(parameterIndex, theLong);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		try {
			preparedStatement.setNull(parameterIndex, sqlType);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
		try {
			preparedStatement.setNull(paramIndex, sqlType, typeName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setObject(int parameterIndex, Object theObject) throws SQLException {
		try {
			preparedStatement.setObject(parameterIndex, SerializableObject.create(theObject));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setObject(int parameterIndex, Object theObject, int targetSqlType) throws SQLException {
		try {
			preparedStatement.setObject(parameterIndex, SerializableObject.create(theObject), targetSqlType);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setObject(int parameterIndex, Object theObject, int targetSqlType, int scale) throws SQLException {
		try {
			preparedStatement.setObject(parameterIndex, SerializableObject.create(theObject), targetSqlType, scale);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setRef(int parameterIndex, Ref theRef) throws SQLException {
		try {
			throwNotImplementedException();
			//preparedStatement.setRef(parameterIndex, theRef);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setShort(int parameterIndex, short theShort) throws SQLException {
		try {
			preparedStatement.setShort(parameterIndex, theShort);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setString(int parameterIndex, String theString) throws SQLException {
		try {
			preparedStatement.setString(parameterIndex, theString);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setTime(int parameterIndex, Time theTime) throws SQLException {
		try {
			preparedStatement.setTime(parameterIndex, theTime);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setTime(int parameterIndex, Time theTime, Calendar cal) throws SQLException {
		try {
			preparedStatement.setTime(parameterIndex, theTime, cal);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setTimestamp(int parameterIndex, Timestamp theTimestamp) throws SQLException {
		try {
			preparedStatement.setTimestamp(parameterIndex, theTimestamp);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setTimestamp(int parameterIndex, Timestamp theTimestamp, Calendar cal) throws SQLException {
		try {
			preparedStatement.setTimestamp(parameterIndex, theTimestamp, cal);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setUnicodeStream(int parameterIndex, InputStream theInputStream, int length) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setURL(int parameterIndex, URL theURL) throws SQLException {
		try {
			preparedStatement.setURL(parameterIndex, theURL);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setNString(int parameterIndex, String value) throws SQLException {
		try {
			preparedStatement.setNString(parameterIndex, value);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		try {
			throwNotImplementedException();
			//preparedStatement.setNCharacterStream(parameterIndex, value, length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		try {
			throwNotImplementedException();
			//preparedStatement.setNClob(parameterIndex, value);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		try {
			throwNotImplementedException();
			//preparedStatement.setClob(parameterIndex, reader, length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		try {
			throwNotImplementedException();
			//return preparedStatement.;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}
    
}
