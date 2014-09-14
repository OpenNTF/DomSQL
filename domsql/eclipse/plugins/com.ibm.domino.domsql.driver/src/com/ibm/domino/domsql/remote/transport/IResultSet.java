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
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;


/**
 *
 */
public interface IResultSet extends IRemoteObject {

	// JDBC ResultSet methods
	public boolean absolute(int row) throws SQLException, RemoteException;
	public void afterLast() throws SQLException, RemoteException;
	public void beforeFirst() throws SQLException, RemoteException;
	public void cancelRowUpdates() throws SQLException, RemoteException;
	public void clearWarnings() throws SQLException, RemoteException;
	public void close() throws SQLException, RemoteException;
	public void deleteRow() throws SQLException, RemoteException;
	public int findColumn(String columnName) throws SQLException, RemoteException;
	public boolean first() throws SQLException, RemoteException;
	public SArray getArray(int columnIndex) throws SQLException, RemoteException;
	public SArray getArray(String colName) throws SQLException, RemoteException;
	public SInputStream getAsciiStream(int columnIndex) throws SQLException, RemoteException;
	public SInputStream getAsciiStream(String columnName) throws SQLException, RemoteException;
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException, RemoteException;
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException, RemoteException;
	public BigDecimal getBigDecimal(String columnName) throws SQLException, RemoteException;
	public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException, RemoteException;
	public SInputStream getBinaryStream(int columnIndex) throws SQLException, RemoteException;
	public SInputStream getBinaryStream(String columnName) throws SQLException, RemoteException;
	public SBlob getBlob(int columnIndex) throws SQLException, RemoteException;
	public SBlob getBlob(String columnName) throws SQLException, RemoteException;
	public boolean getBoolean(int columnIndex) throws SQLException, RemoteException;
	public boolean getBoolean(String columnName) throws SQLException, RemoteException;
	public byte getByte(int columnIndex) throws SQLException, RemoteException;
	public byte getByte(String columnName) throws SQLException, RemoteException;
	public byte[] getBytes(int columnIndex) throws SQLException, RemoteException;
	public byte[] getBytes(String columnName) throws SQLException, RemoteException;
	public SReader getCharacterStream(int columnIndex) throws SQLException, RemoteException;
	public SReader getCharacterStream(String columnName) throws SQLException, RemoteException;
	public SClob getClob(int columnIndex) throws SQLException, RemoteException;
	public SClob getClob(String colName) throws SQLException, RemoteException;
	public int getConcurrency() throws SQLException, RemoteException;
	public String getCursorName() throws SQLException, RemoteException;
	public Date getDate(int columnIndex) throws SQLException, RemoteException;
	public Date getDate(int columnIndex, Calendar cal) throws SQLException, RemoteException;
	public Date getDate(String columnName) throws SQLException, RemoteException;
	public Date getDate(String columnName, Calendar cal) throws SQLException, RemoteException;
	public double getDouble(int columnIndex) throws SQLException, RemoteException;
	public double getDouble(String columnName) throws SQLException, RemoteException;
	public int getFetchDirection() throws SQLException, RemoteException;
	public int getFetchSize() throws SQLException, RemoteException;
	public float getFloat(int columnIndex) throws SQLException, RemoteException;
	public float getFloat(String columnName) throws SQLException, RemoteException;
	public int getInt(int columnIndex) throws SQLException, RemoteException;
	public int getInt(String columnName) throws SQLException, RemoteException;
	public long getLong(int columnIndex) throws SQLException, RemoteException;
	public long getLong(String columnName) throws SQLException, RemoteException;
	public IResultSetMetaData getMetaData() throws SQLException, RemoteException;
	public Object getObject(int columnIndex) throws SQLException, RemoteException;
	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException, RemoteException;
	public Object getObject(String columnName) throws SQLException, RemoteException;
	public Object getObject(String columnName, Map<String, Class<?>> map) throws SQLException, RemoteException;
	public SRef getRef(int columnIndex) throws SQLException, RemoteException;
	public SRef getRef(String colName) throws SQLException, RemoteException;
	public int getRow() throws SQLException, RemoteException;
	public short getShort(int columnIndex) throws SQLException, RemoteException;
	public short getShort(String columnName) throws SQLException, RemoteException;
	public String getString(int columnIndex) throws SQLException, RemoteException;
	public String getString(String columnName) throws SQLException, RemoteException;
	public Time getTime(int columnIndex) throws SQLException, RemoteException;
	public Time getTime(int columnIndex, Calendar cal) throws SQLException, RemoteException;
	public Time getTime(String columnName) throws SQLException, RemoteException;
	public Time getTime(String columnName, Calendar cal) throws SQLException, RemoteException;
	public Timestamp getTimestamp(int columnIndex) throws SQLException, RemoteException;
	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException, RemoteException;
	public Timestamp getTimestamp(String columnName) throws SQLException, RemoteException;
	public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException, RemoteException;
	public int getType() throws SQLException, RemoteException;
	public SInputStream getUnicodeStream(int columnIndex) throws SQLException, RemoteException;
	public SInputStream getUnicodeStream(String columnName) throws SQLException, RemoteException;
	public URL getURL(int columnIndex) throws SQLException, RemoteException;
	public URL getURL(String columnName) throws SQLException, RemoteException;
	public SQLWarning getWarnings() throws SQLException, RemoteException;
	public void insertRow() throws SQLException, RemoteException;
	public boolean isAfterLast() throws SQLException, RemoteException;
	public boolean isBeforeFirst() throws SQLException, RemoteException;
	public boolean isFirst() throws SQLException, RemoteException;
	public boolean isLast() throws SQLException, RemoteException;
	public boolean last() throws SQLException, RemoteException;
	public void moveToCurrentRow() throws SQLException, RemoteException;
	public void moveToInsertRow() throws SQLException, RemoteException;
	public boolean next() throws SQLException, RemoteException;
	public boolean previous() throws SQLException, RemoteException;
	public void refreshRow() throws SQLException, RemoteException;
	public boolean relative(int rows) throws SQLException, RemoteException;
	public boolean rowDeleted() throws SQLException, RemoteException;
	public boolean rowInserted() throws SQLException, RemoteException;
	public boolean rowUpdated() throws SQLException, RemoteException;
	public void setFetchDirection(int direction) throws SQLException, RemoteException;
	public void setFetchSize(int rows) throws SQLException, RemoteException;
	public void updateArray(int columnIndex, SArray x) throws SQLException, RemoteException;
	public void updateArray(String columnName, SArray x) throws SQLException, RemoteException;
	public void updateAsciiStream(int columnIndex, SInputStream x, int length) throws SQLException, RemoteException;
	public void updateAsciiStream(String columnName, SInputStream x, int length) throws SQLException, RemoteException;
	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException, RemoteException;
	public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException, RemoteException;
	public void updateBinaryStream(int columnIndex, SInputStream x, int length) throws SQLException, RemoteException;
	public void updateBinaryStream(String columnName, SInputStream x, int length) throws SQLException, RemoteException;
	public void updateBlob(int columnIndex, SBlob x) throws SQLException, RemoteException;
	public void updateBlob(String columnName, SBlob x) throws SQLException, RemoteException;
	public void updateBoolean(int columnIndex, boolean x) throws SQLException, RemoteException;
	public void updateBoolean(String columnName, boolean x) throws SQLException, RemoteException;
	public void updateByte(int columnIndex, byte x) throws SQLException, RemoteException;
	public void updateByte(String columnName, byte x) throws SQLException, RemoteException;
	public void updateBytes(int columnIndex, byte[] x) throws SQLException, RemoteException;
	public void updateBytes(String columnName, byte[] x) throws SQLException, RemoteException;
	public void updateCharacterStream(int columnIndex, SReader x, int length) throws SQLException, RemoteException;
	public void updateCharacterStream(String columnName, SReader reader, int length) throws SQLException, RemoteException;
	public void updateClob(int columnIndex, SClob x) throws SQLException, RemoteException;
	public void updateClob(String columnName, SClob x) throws SQLException, RemoteException;
	public void updateDate(int columnIndex, Date x) throws SQLException, RemoteException;
	public void updateDate(String columnName, Date x) throws SQLException, RemoteException;
	public void updateDouble(int columnIndex, double x) throws SQLException, RemoteException;
	public void updateDouble(String columnName, double x) throws SQLException, RemoteException;
	public void updateFloat(int columnIndex, float x) throws SQLException, RemoteException;
	public void updateFloat(String columnName, float x) throws SQLException, RemoteException;
	public void updateInt(int columnIndex, int x) throws SQLException, RemoteException;
	public void updateInt(String columnName, int x) throws SQLException, RemoteException;
	public void updateLong(int columnIndex, long x) throws SQLException, RemoteException;
	public void updateLong(String columnName, long x) throws SQLException, RemoteException;
	public void updateNull(int columnIndex) throws SQLException, RemoteException;
	public void updateNull(String columnName) throws SQLException, RemoteException;
	public void updateObject(int columnIndex, Object x) throws SQLException, RemoteException;
	public void updateObject(int columnIndex, Object x, int scale) throws SQLException, RemoteException;
	public void updateObject(String columnName, Object x) throws SQLException, RemoteException;
	public void updateObject(String columnName, Object x, int scale) throws SQLException, RemoteException;
	public void updateRef(int columnIndex, SRef x) throws SQLException, RemoteException;
	public void updateRef(String columnName, SRef x) throws SQLException, RemoteException;
	public void updateRow() throws SQLException, RemoteException;
	public void updateShort(int columnIndex, short x) throws SQLException, RemoteException;
	public void updateShort(String columnName, short x) throws SQLException, RemoteException;
	public void updateString(int columnIndex, String x) throws SQLException, RemoteException;
	public void updateString(String columnName, String x) throws SQLException, RemoteException;
	public void updateTime(int columnIndex, Time x) throws SQLException, RemoteException;
	public void updateTime(String columnName, Time x) throws SQLException, RemoteException;
	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException, RemoteException;
	public void updateTimestamp(String columnName, Timestamp x) throws SQLException, RemoteException;
	public boolean wasNull() throws SQLException, RemoteException;
	public SRowId getRowId(int columnIndex) throws SQLException, RemoteException;
	public SRowId getRowId(String columnLabel) throws SQLException, RemoteException;
	public void updateRowId(int columnIndex, SRowId x) throws SQLException, RemoteException;
	public void updateRowId(String columnLabel, SRowId x) throws SQLException, RemoteException;
	public int getHoldability() throws SQLException, RemoteException;
	public boolean isClosed() throws SQLException, RemoteException;
	public void updateNString(int columnIndex, String nString) throws SQLException, RemoteException;
	public void updateNString(String columnLabel, String nString) throws SQLException, RemoteException;
	public void updateNClob(int columnIndex, SNClob nClob) throws SQLException, RemoteException;
	public void updateNClob(String columnLabel, SNClob nClob) throws SQLException, RemoteException;
	public SNClob getNClob(int columnIndex) throws SQLException, RemoteException;
	public SNClob getNClob(String columnLabel) throws SQLException, RemoteException;
	public SSQLXML getSQLXML(int columnIndex) throws SQLException, RemoteException;
	public SSQLXML getSQLXML(String columnLabel) throws SQLException, RemoteException;
	public void updateSQLXML(int columnIndex, SSQLXML xmlObject) throws SQLException, RemoteException;
	public void updateSQLXML(String columnLabel, SSQLXML xmlObject) throws SQLException, RemoteException;
	public String getNString(int columnIndex) throws SQLException, RemoteException;
	public String getNString(String columnLabel) throws SQLException, RemoteException;
	public SReader getNCharacterStream(int columnIndex) throws SQLException, RemoteException;
	public SReader getNCharacterStream(String columnLabel) throws SQLException, RemoteException;
	public void updateNCharacterStream(int columnIndex, SReader x, long length) throws SQLException, RemoteException;
	public void updateNCharacterStream(String columnLabel, SReader reader, long length) throws SQLException, RemoteException;
	public void updateAsciiStream(int columnIndex, SInputStream x, long length) throws SQLException, RemoteException;
	public void updateBinaryStream(int columnIndex, SInputStream x, long length) throws SQLException, RemoteException;
	public void updateCharacterStream(int columnIndex, SReader x, long length) throws SQLException, RemoteException;
	public void updateAsciiStream(String columnLabel, SInputStream x, long length) throws SQLException, RemoteException;
	public void updateBinaryStream(String columnLabel, SInputStream x, long length) throws SQLException, RemoteException;
	public void updateCharacterStream(String columnLabel, SReader reader, long length) throws SQLException, RemoteException;
	public void updateBlob(int columnIndex, SInputStream inputStream, long length) throws SQLException, RemoteException;
	public void updateBlob(String columnLabel, SInputStream inputStream, long length) throws SQLException, RemoteException;
	public void updateClob(int columnIndex, SReader reader, long length) throws SQLException, RemoteException;
	public void updateClob(String columnLabel, SReader reader, long length) throws SQLException, RemoteException;
	public void updateNClob(int columnIndex, SReader reader, long length) throws SQLException, RemoteException;
	public void updateNClob(String columnLabel, SReader reader, long length) throws SQLException, RemoteException;
	public void updateNCharacterStream(int columnIndex, SReader x) throws SQLException, RemoteException;
	public void updateNCharacterStream(String columnLabel, SReader reader) throws SQLException, RemoteException;
	public void updateAsciiStream(int columnIndex, SInputStream x) throws SQLException, RemoteException;
	public void updateBinaryStream(int columnIndex, SInputStream x) throws SQLException, RemoteException;
	public void updateCharacterStream(int columnIndex, SReader x) throws SQLException, RemoteException;
	public void updateAsciiStream(String columnLabel, SInputStream x) throws SQLException, RemoteException;
	public void updateBinaryStream(String columnLabel, SInputStream x) throws SQLException, RemoteException;
	public void updateCharacterStream(String columnLabel, SReader reader) throws SQLException, RemoteException;
	public void updateBlob(int columnIndex, SInputStream inputStream) throws SQLException, RemoteException;
	public void updateBlob(String columnLabel, SInputStream inputStream) throws SQLException, RemoteException;
	public void updateClob(int columnIndex, SReader reader) throws SQLException, RemoteException;
	public void updateClob(String columnLabel, SReader reader) throws SQLException, RemoteException;
	public void updateNClob(int columnIndex, SReader reader) throws SQLException, RemoteException;
	public void updateNClob(String columnLabel, SReader reader) throws SQLException, RemoteException;

	// Extra methods
	public Object[] _getObjects() throws SQLException, RemoteException;
	public Object[][] _readRows(int count) throws SQLException, RemoteException;
	public Object[] _next() throws SQLException, RemoteException;
}
