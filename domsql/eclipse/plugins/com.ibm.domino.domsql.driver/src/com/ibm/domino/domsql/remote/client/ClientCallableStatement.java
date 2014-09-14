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
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import com.ibm.domino.domsql.remote.transport.ICallableStatement;
import com.ibm.domino.domsql.remote.transport.SInputStream;
import com.ibm.domino.domsql.remote.transport.SReader;
import com.ibm.domino.domsql.remote.transport.SerializableObject;

/**
 *
 */
public class ClientCallableStatement extends ClientPreparedStatement implements CallableStatement {

	private ICallableStatement callableStatement;
	
    public ClientCallableStatement(ClientConnection connection, ICallableStatement callableStatement) {
    	super(connection,callableStatement);
        this.callableStatement = callableStatement;
    }
    
    public ICallableStatement getRemoteObject() {
    	return callableStatement;
    }

	public Array getArray(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getArray(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Array getArray(String parameterName) throws SQLException {
		try {
			return callableStatement.getArray(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getBigDecimal(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
		try {
			return callableStatement.getBigDecimal(parameterIndex, scale);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public BigDecimal getBigDecimal(String parameterName) throws SQLException {
		try {
			return callableStatement.getBigDecimal(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Blob getBlob(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getBlob(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Blob getBlob(String parameterName) throws SQLException {
		try {
			return callableStatement.getBlob(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean getBoolean(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getBoolean(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean getBoolean(String parameterName) throws SQLException {
		try {
			return callableStatement.getBoolean(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public byte getByte(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getByte(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public byte getByte(String parameterName) throws SQLException {
		try {
			return callableStatement.getByte(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public byte[] getBytes(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getBytes(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public byte[] getBytes(String parameterName) throws SQLException {
		try {
			return callableStatement.getBytes(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Clob getClob(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getClob(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Clob getClob(String parameterName) throws SQLException {
		try {
			return callableStatement.getClob(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Date getDate(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getDate(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
		try {
			return callableStatement.getDate(parameterIndex, cal);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Date getDate(String parameterName) throws SQLException {
		try {
			return callableStatement.getDate(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Date getDate(String parameterName, Calendar cal) throws SQLException {
		try {
			return callableStatement.getDate(parameterName, cal);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public double getDouble(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getDouble(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public double getDouble(String parameterName) throws SQLException {
		try {
			return callableStatement.getDouble(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public float getFloat(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getFloat(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public float getFloat(String parameterName) throws SQLException {
		try {
			return callableStatement.getFloat(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getInt(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getInt(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getInt(String parameterName) throws SQLException {
		try {
			return callableStatement.getInt(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public long getLong(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getLong(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public long getLong(String parameterName) throws SQLException {
		try {
			return callableStatement.getLong(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Object getObject(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getObject(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
		try {
			return callableStatement.getObject(parameterIndex, map);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Object getObject(String parameterName) throws SQLException {
		try {
			return callableStatement.getObject(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
		try {
			return callableStatement.getObject(parameterName, map);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Ref getRef(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getRef(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Ref getRef(String parameterName) throws SQLException {
		try {
			return callableStatement.getRef(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public short getShort(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getShort(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public short getShort(String parameterName) throws SQLException {
		try {
			return callableStatement.getShort(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getString(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getString(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getString(String parameterName) throws SQLException {
		try {
			return callableStatement.getString(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Time getTime(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getTime(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
		try {
			return callableStatement.getTime(parameterIndex, cal);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Time getTime(String parameterName) throws SQLException {
		try {
			return callableStatement.getTime(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Time getTime(String parameterName, Calendar cal) throws SQLException {
		try {
			return callableStatement.getTime(parameterName, cal);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Timestamp getTimestamp(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getTimestamp(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
		try {
			return callableStatement.getTimestamp(parameterIndex, cal);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Timestamp getTimestamp(String parameterName) throws SQLException {
		try {
			return callableStatement.getTimestamp(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
		try {
			return callableStatement.getTimestamp(parameterName, cal);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public URL getURL(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getURL(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public URL getURL(String parameterName) throws SQLException {
		try {
			return callableStatement.getURL(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
		try {
			callableStatement.registerOutParameter(parameterIndex, sqlType);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
		try {
			callableStatement.registerOutParameter(parameterIndex, sqlType, scale);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void registerOutParameter(int paramIndex, int sqlType, String typeName) throws SQLException {
		try {
			callableStatement.registerOutParameter(paramIndex, sqlType, typeName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
		try {
			callableStatement.registerOutParameter(parameterName, sqlType);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
		try {
			callableStatement.registerOutParameter(parameterName, sqlType, scale);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
		try {
			callableStatement.registerOutParameter(parameterName, sqlType, typeName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setAsciiStream(String parameterName, InputStream theInputStream, int length) throws SQLException {
		try {
			callableStatement.setAsciiStream(parameterName, SInputStream.create(theInputStream), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setBigDecimal(String parameterName, BigDecimal theBigDecimal) throws SQLException {
		try {
			callableStatement.setBigDecimal(parameterName, theBigDecimal);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setBinaryStream(String parameterName, InputStream theInputStream, int length) throws SQLException {
		try {
			callableStatement.setBinaryStream(parameterName, SInputStream.create(theInputStream), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setBoolean(String parameterName, boolean theBoolean) throws SQLException {
		try {
			callableStatement.setBoolean(parameterName, theBoolean);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setByte(String parameterName, byte theByte) throws SQLException {
		try {
			callableStatement.setByte(parameterName, theByte);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setBytes(String parameterName, byte[] theBytes) throws SQLException {
		try {
			callableStatement.setBytes(parameterName, theBytes);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
		try {
			callableStatement.setCharacterStream(parameterName, SReader.create(reader), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setDate(String parameterName, Date theDate) throws SQLException {
		try {
			callableStatement.setDate(parameterName, theDate);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setDate(String parameterName, Date theDate, Calendar cal) throws SQLException {
		try {
			callableStatement.setDate(parameterName, theDate, cal);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setDouble(String parameterName, double theDouble) throws SQLException {
		try {
			callableStatement.setDouble(parameterName, theDouble);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setFloat(String parameterName, float theFloat) throws SQLException {
		try {
			callableStatement.setFloat(parameterName, theFloat);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setInt(String parameterName, int theInt) throws SQLException {
		try {
			callableStatement.setInt(parameterName, theInt);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setLong(String parameterName, long theLong) throws SQLException {
		try {
			callableStatement.setLong(parameterName, theLong);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setNull(String parameterName, int sqlType) throws SQLException {
		try {
			callableStatement.setNull(parameterName, sqlType);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
		try {
			callableStatement.setNull(parameterName, sqlType, typeName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setObject(String parameterName, Object theObject) throws SQLException {
		try {
			callableStatement.setObject(parameterName, SerializableObject.create(theObject));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setObject(String parameterName, Object theObject, int targetSqlType) throws SQLException {
		try {
			callableStatement.setObject(parameterName, SerializableObject.create(theObject), targetSqlType);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setObject(String parameterName, Object theObject, int targetSqlType, int scale) throws SQLException {
		try {
			callableStatement.setObject(parameterName, SerializableObject.create(theObject), targetSqlType, scale);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setShort(String parameterName, short theShort) throws SQLException {
		try {
			callableStatement.setShort(parameterName, theShort);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setString(String parameterName, String theString) throws SQLException {
		try {
			callableStatement.setString(parameterName, theString);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setTime(String parameterName, Time theTime) throws SQLException {
		try {
			callableStatement.setTime(parameterName, theTime);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setTime(String parameterName, Time theTime, Calendar cal) throws SQLException {
		try {
			callableStatement.setTime(parameterName, theTime, cal);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setTimestamp(String parameterName, Timestamp theTimestamp) throws SQLException {
		try {
			callableStatement.setTimestamp(parameterName, theTimestamp);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setTimestamp(String parameterName, Timestamp theTimestamp, Calendar cal) throws SQLException {
		try {
			callableStatement.setTimestamp(parameterName, theTimestamp, cal);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setURL(String parameterName, URL theURL) throws SQLException {
		try {
			callableStatement.setURL(parameterName, theURL);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean wasNull() throws SQLException {
		try {
			return callableStatement.wasNull();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public RowId getRowId(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getRowId(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public RowId getRowId(String parameterName) throws SQLException {
		try {
			return callableStatement.getRowId(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setRowId(String parameterName, RowId x) throws SQLException {
		try {
			throwNotImplementedException();
			//return callableStatement.setRowId(parameterName, new ClientRowId(x));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setNString(String parameterName, String value) throws SQLException {
		try {
			callableStatement.setNString(parameterName, value);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
		try {
			throwNotImplementedException();
			//callableStatement.setNCharacterStream(parameterName, value, length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setNClob(String parameterName, NClob value) throws SQLException {
		try {
			throwNotImplementedException();
			//callableStatement.setNClob(parameterName, value);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setClob(String parameterName, Reader reader, long length) throws SQLException {
		try {
			throwNotImplementedException();
			//return callableStatement.setClob(parameterIndex, reader, length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
		try {
			throwNotImplementedException();
			//return callableStatement.setBlob(parameterName, inputStream, length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
		try {
			throwNotImplementedException();
			//return callableStatement.setNClob(parameterIndex, reader, length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public NClob getNClob(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getNClob(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public NClob getNClob(String parameterName) throws SQLException {
		try {
			return callableStatement.getNClob(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
		try {
			throwNotImplementedException();
			//callableStatement.setSQLXML(parameterName, xmlObject);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public SQLXML getSQLXML(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getSQLXML(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public SQLXML getSQLXML(String parameterName) throws SQLException {
		try {
			return callableStatement.getSQLXML(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getNString(int parameterIndex) throws SQLException {
		try {
			return callableStatement.getNString(parameterIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getNString(String parameterName) throws SQLException {
		try {
			return callableStatement.getNString(parameterName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Reader getNCharacterStream(int parameterIndex) throws SQLException {
		try {
			return SReader.getReader(callableStatement.getNCharacterStream(parameterIndex));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Reader getNCharacterStream(String parameterName) throws SQLException {
		try {
			return SReader.getReader(callableStatement.getNCharacterStream(parameterName));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Reader getCharacterStream(int parameterIndex) throws SQLException {
		try {
			return SReader.getReader(callableStatement.getCharacterStream(parameterIndex));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Reader getCharacterStream(String parameterName) throws SQLException {
		try {
			return SReader.getReader(callableStatement.getCharacterStream(parameterName));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setBlob(String parameterName, Blob x) throws SQLException {
		try {
			throwNotImplementedException();
			//callableStatement.setBlob(parameterName, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setClob(String parameterName, Clob x) throws SQLException {
		try {
			throwNotImplementedException();
			//callableStatement.setClob(parameterName, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
		try {
			throwNotImplementedException();
			//callableStatement.setAsciiStream(parameterName, x, length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
		try {
			throwNotImplementedException();
			//return callableStatement.setBinaryStream(parameterName, x, length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
		try {
			throwNotImplementedException();
			//callableStatement.setCharacterStream(parameterName, reader, length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
		try {
			throwNotImplementedException();
			//callableStatement.setAsciiStream(parameterName, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
		try {
			throwNotImplementedException();
			//callableStatement.setBinaryStream(parameterName, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
		try {
			throwNotImplementedException();
			//return callableStatement.setCharacterStream(parameterName, reader);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
		try {
			throwNotImplementedException();
			//callableStatement.setNCharacterStream(parameterName, value);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setClob(String parameterName, Reader reader) throws SQLException {
		try {
			throwNotImplementedException();
			//callableStatement.setClob(parameterIndex, reader);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
		try {
			throwNotImplementedException();
			//return callableStatement.setBlob(parameterName, inputStream);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setNClob(String parameterName, Reader reader) throws SQLException {
		try {
			throwNotImplementedException();
			//return callableStatement.setNCharacterStream(parameterName, reader);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

}
