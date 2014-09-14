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
package com.ibm.domino.domsql.remote.transport;

import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;


/**
 *
 */
public interface ICallableStatement extends IPreparedStatement {

	public SArray getArray(int parameterIndex) throws SQLException, RemoteException;
	public SArray getArray(String parameterName) throws SQLException, RemoteException;
	public BigDecimal getBigDecimal(int parameterIndex) throws SQLException, RemoteException;
	public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException, RemoteException;
	public BigDecimal getBigDecimal(String parameterName) throws SQLException, RemoteException;
	public SBlob getBlob(int parameterIndex) throws SQLException, RemoteException;
	public SBlob getBlob(String parameterName) throws SQLException, RemoteException;
	public boolean getBoolean(int parameterIndex) throws SQLException, RemoteException;
	public boolean getBoolean(String parameterName) throws SQLException, RemoteException;
	public byte getByte(int parameterIndex) throws SQLException, RemoteException;
	public byte getByte(String parameterName) throws SQLException, RemoteException;
	public byte[] getBytes(int parameterIndex) throws SQLException, RemoteException;
	public byte[] getBytes(String parameterName) throws SQLException, RemoteException;
	public SClob getClob(int parameterIndex) throws SQLException, RemoteException;
	public SClob getClob(String parameterName) throws SQLException, RemoteException;
	public Date getDate(int parameterIndex) throws SQLException, RemoteException;
	public Date getDate(int parameterIndex, Calendar cal) throws SQLException, RemoteException;
	public Date getDate(String parameterName) throws SQLException, RemoteException;
	public Date getDate(String parameterName, Calendar cal) throws SQLException, RemoteException;
	public double getDouble(int parameterIndex) throws SQLException, RemoteException;
	public double getDouble(String parameterName) throws SQLException, RemoteException;
	public float getFloat(int parameterIndex) throws SQLException, RemoteException;
	public float getFloat(String parameterName) throws SQLException, RemoteException;
	public int getInt(int parameterIndex) throws SQLException, RemoteException;
	public int getInt(String parameterName) throws SQLException, RemoteException;
	public long getLong(int parameterIndex) throws SQLException, RemoteException;
	public long getLong(String parameterName) throws SQLException, RemoteException;
	public Object getObject(int parameterIndex) throws SQLException, RemoteException;
	public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException, RemoteException;
	public Object getObject(String parameterName) throws SQLException, RemoteException;
	public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException, RemoteException;
	public SRef getRef(int parameterIndex) throws SQLException, RemoteException;
	public SRef getRef(String parameterName) throws SQLException, RemoteException;
	public short getShort(int parameterIndex) throws SQLException, RemoteException;
	public short getShort(String parameterName) throws SQLException, RemoteException;
	public String getString(int parameterIndex) throws SQLException, RemoteException;
	public String getString(String parameterName) throws SQLException, RemoteException;
	public Time getTime(int parameterIndex) throws SQLException, RemoteException;
	public Time getTime(int parameterIndex, Calendar cal) throws SQLException, RemoteException;
	public Time getTime(String parameterName) throws SQLException, RemoteException;
	public Time getTime(String parameterName, Calendar cal) throws SQLException, RemoteException;
	public Timestamp getTimestamp(int parameterIndex) throws SQLException, RemoteException;
	public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException, RemoteException;
	public Timestamp getTimestamp(String parameterName) throws SQLException, RemoteException;
	public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException, RemoteException;
	public URL getURL(int parameterIndex) throws SQLException, RemoteException;
	public URL getURL(String parameterName) throws SQLException, RemoteException;
	public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException, RemoteException;
	public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException, RemoteException;
	public void registerOutParameter(int paramIndex, int sqlType, String typeName) throws SQLException, RemoteException;
	public void registerOutParameter(String parameterName, int sqlType) throws SQLException, RemoteException;
	public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException, RemoteException;
	public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException, RemoteException;
	public void setAsciiStream(String parameterName, SInputStream theInputStream, int length) throws SQLException, RemoteException;
	public void setBigDecimal(String parameterName, BigDecimal theBigDecimal) throws SQLException, RemoteException;
	public void setBinaryStream(String parameterName, SInputStream theInputStream, int length) throws SQLException, RemoteException;
	public void setBoolean(String parameterName, boolean theBoolean) throws SQLException, RemoteException;
	public void setByte(String parameterName, byte theByte) throws SQLException, RemoteException;
	public void setBytes(String parameterName, byte[] theBytes) throws SQLException, RemoteException;
	public void setCharacterStream(String parameterName, SReader reader, int length) throws SQLException, RemoteException;
	public void setDate(String parameterName, Date theDate) throws SQLException, RemoteException;
	public void setDate(String parameterName, Date theDate, Calendar cal) throws SQLException, RemoteException;
	public void setDouble(String parameterName, double theDouble) throws SQLException, RemoteException;
	public void setFloat(String parameterName, float theFloat) throws SQLException, RemoteException;
	public void setInt(String parameterName, int theInt) throws SQLException, RemoteException;
	public void setLong(String parameterName, long theLong) throws SQLException, RemoteException;
	public void setNull(String parameterName, int sqlType) throws SQLException, RemoteException;
	public void setNull(String parameterName, int sqlType, String typeName) throws SQLException, RemoteException;
	public void setObject(String parameterName, Object theObject) throws SQLException, RemoteException;
	public void setObject(String parameterName, Object theObject, int targetSqlType) throws SQLException, RemoteException;
	public void setObject(String parameterName, Object theObject, int targetSqlType, int scale) throws SQLException, RemoteException;
	public void setShort(String parameterName, short theShort) throws SQLException, RemoteException;
	public void setString(String parameterName, String theString) throws SQLException, RemoteException;
	public void setTime(String parameterName, Time theTime) throws SQLException, RemoteException;
	public void setTime(String parameterName, Time theTime, Calendar cal) throws SQLException, RemoteException;
	public void setTimestamp(String parameterName, Timestamp theTimestamp) throws SQLException, RemoteException;
	public void setTimestamp(String parameterName, Timestamp theTimestamp, Calendar cal) throws SQLException, RemoteException;
	public void setURL(String parameterName, URL theURL) throws SQLException, RemoteException;
	public boolean wasNull() throws SQLException, RemoteException;
	public SRowId getRowId(int parameterIndex) throws SQLException, RemoteException;
	public SRowId getRowId(String parameterName) throws SQLException, RemoteException;
	public void setRowId(String parameterName, SRowId x) throws SQLException, RemoteException;
	public void setNString(String parameterName, String value) throws SQLException, RemoteException;
	public void setNCharacterStream(String parameterName, SReader value, long length) throws SQLException, RemoteException;
	public void setNClob(String parameterName, SNClob value) throws SQLException, RemoteException;
	public void setClob(String parameterName, SReader reader, long length) throws SQLException, RemoteException;
	public void setBlob(String parameterName, SInputStream inputStream, long length) throws SQLException, RemoteException;
	public void setNClob(String parameterName, SReader reader, long length) throws SQLException, RemoteException;
	public SNClob getNClob(int parameterIndex) throws SQLException, RemoteException;
	public SNClob getNClob(String parameterName) throws SQLException, RemoteException;
	public void setSQLXML(String parameterName, SSQLXML xmlObject) throws SQLException, RemoteException;
	public SSQLXML getSQLXML(int parameterIndex) throws SQLException, RemoteException;
	public SSQLXML getSQLXML(String parameterName) throws SQLException, RemoteException;
	public String getNString(int parameterIndex) throws SQLException, RemoteException;
	public String getNString(String parameterName) throws SQLException, RemoteException;
	public SReader getNCharacterStream(int parameterIndex) throws SQLException, RemoteException;
	public SReader getNCharacterStream(String parameterName) throws SQLException, RemoteException;
	public SReader getCharacterStream(int parameterIndex) throws SQLException, RemoteException;
	public SReader getCharacterStream(String parameterName) throws SQLException, RemoteException;
	public void setBlob(String parameterName, SBlob x) throws SQLException, RemoteException;
	public void setClob(String parameterName, SClob x) throws SQLException, RemoteException;
	public void setAsciiStream(String parameterName, SInputStream x, long length) throws SQLException, RemoteException;
	public void setBinaryStream(String parameterName, SInputStream x, long length) throws SQLException, RemoteException;
	public void setCharacterStream(String parameterName, SReader reader, long length) throws SQLException, RemoteException;
	public void setAsciiStream(String parameterName, SInputStream x) throws SQLException, RemoteException;
	public void setBinaryStream(String parameterName, SInputStream x) throws SQLException, RemoteException;
	public void setCharacterStream(String parameterName, SReader reader) throws SQLException, RemoteException;
	public void setNCharacterStream(String parameterName, SReader value) throws SQLException, RemoteException;
	public void setClob(String parameterName, SReader reader) throws SQLException, RemoteException;
	public void setBlob(String parameterName, SInputStream inputStream) throws SQLException, RemoteException;
	public void setNClob(String parameterName, SReader reader) throws SQLException, RemoteException;
}
