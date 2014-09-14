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


/**
 *
 */
public interface IPreparedStatement extends IStatement {

	public void addBatch() throws SQLException, RemoteException;
	public void clearParameters() throws SQLException, RemoteException;
	public boolean execute() throws SQLException, RemoteException;
	public IResultSet executeQuery() throws SQLException, RemoteException;
	public int executeUpdate() throws SQLException, RemoteException;
	public IResultSetMetaData getMetaData() throws SQLException, RemoteException;
	public IParameterMetaData getParameterMetaData() throws SQLException, RemoteException;
	public void setArray(int parameterIndex, SArray theArray) throws SQLException, RemoteException;
	public void setAsciiStream(int parameterIndex, SInputStream theInputStream, int length) throws SQLException, RemoteException;
	public void setBigDecimal(int parameterIndex, BigDecimal theBigDecimal) throws SQLException, RemoteException;
	public void setBinaryStream(int parameterIndex, SInputStream theInputStream, int length) throws SQLException, RemoteException;
	public void setBlob(int parameterIndex, SBlob theBlob) throws SQLException, RemoteException;
	public void setBoolean(int parameterIndex, boolean theBoolean) throws SQLException, RemoteException;
	public void setByte(int parameterIndex, byte theByte) throws SQLException, RemoteException;
	public void setBytes(int parameterIndex, byte[] theBytes) throws SQLException, RemoteException;
	public void setCharacterStream(int parameterIndex, SReader reader, int length) throws SQLException, RemoteException;
	public void setClob(int parameterIndex, SClob theClob) throws SQLException, RemoteException;
	public void setDate(int parameterIndex, Date theDate) throws SQLException, RemoteException;
	public void setDate(int parameterIndex, Date theDate, Calendar cal) throws SQLException, RemoteException;
	public void setDouble(int parameterIndex, double theDouble) throws SQLException, RemoteException;
	public void setFloat(int parameterIndex, float theFloat) throws SQLException, RemoteException;
	public void setInt(int parameterIndex, int theInt) throws SQLException, RemoteException;
	public void setLong(int parameterIndex, long theLong) throws SQLException, RemoteException;
	public void setNull(int parameterIndex, int sqlType) throws SQLException, RemoteException;
	public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException, RemoteException;
	public void setObject(int parameterIndex, Object theObject) throws SQLException, RemoteException;
	public void setObject(int parameterIndex, Object theObject, int targetSqlType) throws SQLException, RemoteException;
	public void setObject(int parameterIndex, Object theObject, int targetSqlType, int scale) throws SQLException, RemoteException;
	public void setRef(int parameterIndex, SRef theRef) throws SQLException, RemoteException;
	public void setShort(int parameterIndex, short theShort) throws SQLException, RemoteException;
	public void setString(int parameterIndex, String theString) throws SQLException, RemoteException;
	public void setTime(int parameterIndex, Time theTime) throws SQLException, RemoteException;
	public void setTime(int parameterIndex, Time theTime, Calendar cal) throws SQLException, RemoteException;
	public void setTimestamp(int parameterIndex, Timestamp theTimestamp) throws SQLException, RemoteException;
	public void setTimestamp(int parameterIndex, Timestamp theTimestamp, Calendar cal) throws SQLException, RemoteException;
	public void setUnicodeStream(int parameterIndex, SInputStream theInputStream, int length) throws SQLException, RemoteException;
	public void setURL(int parameterIndex, URL theURL) throws SQLException, RemoteException;
	public void setRowId(int parameterIndex, SRowId x) throws SQLException, RemoteException;
	public void setNString(int parameterIndex, String value) throws SQLException, RemoteException;
	public void setNCharacterStream(int parameterIndex, SReader value, long length) throws SQLException, RemoteException;
	public void setNClob(int parameterIndex, SNClob value) throws SQLException, RemoteException;
	public void setClob(int parameterIndex, SReader reader, long length) throws SQLException, RemoteException;
	public void setBlob(int parameterIndex, SInputStream inputStream, long length) throws SQLException, RemoteException;
	public void setNClob(int parameterIndex, SReader reader, long length) throws SQLException, RemoteException;
	public void setSQLXML(int parameterIndex, SSQLXML xmlObject) throws SQLException, RemoteException;
	public void setAsciiStream(int parameterIndex, SInputStream x, long length) throws SQLException, RemoteException;
	public void setBinaryStream(int parameterIndex, SInputStream x, long length) throws SQLException, RemoteException;
	public void setCharacterStream(int parameterIndex, SReader reader, long length) throws SQLException, RemoteException;
	public void setAsciiStream(int parameterIndex, SInputStream x) throws SQLException, RemoteException;
	public void setBinaryStream(int parameterIndex, SInputStream x) throws SQLException, RemoteException;
	public void setCharacterStream(int parameterIndex, SReader reader) throws SQLException, RemoteException;
	public void setNCharacterStream(int parameterIndex, SReader value) throws SQLException, RemoteException;
	public void setClob(int parameterIndex, SReader reader) throws SQLException, RemoteException;
	public void setBlob(int parameterIndex, SInputStream inputStream) throws SQLException, RemoteException;
	public void setNClob(int parameterIndex, SReader reader) throws SQLException, RemoteException;
}
