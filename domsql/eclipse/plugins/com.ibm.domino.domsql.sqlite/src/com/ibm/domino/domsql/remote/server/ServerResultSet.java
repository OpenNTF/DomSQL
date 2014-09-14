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

import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import com.ibm.domino.domsql.remote.transport.IResultSet;
import com.ibm.domino.domsql.remote.transport.SArray;
import com.ibm.domino.domsql.remote.transport.SBlob;
import com.ibm.domino.domsql.remote.transport.SClob;
import com.ibm.domino.domsql.remote.transport.SInputStream;
import com.ibm.domino.domsql.remote.transport.SNClob;
import com.ibm.domino.domsql.remote.transport.SReader;
import com.ibm.domino.domsql.remote.transport.SRef;
import com.ibm.domino.domsql.remote.transport.SRowId;
import com.ibm.domino.domsql.remote.transport.SSQLXML;
import com.ibm.domino.domsql.remote.transport.SerializableObject;
import com.ibm.domino.domsql.sqlite.driver.jdbc.DomSQLResultSet;
import com.ibm.domino.domsql.sqlite.driver.jdbc.DomSQLResultSetMetaData;

/**
 *
 */
public class ServerResultSet extends ServerObject implements IResultSet {

	private static final long serialVersionUID = 1L;
	
    private DomSQLResultSet resultSet;
    
    public ServerResultSet(ServerConnection connection, DomSQLResultSet resultSet) throws RemoteException {
        super(connection);

        this.resultSet = resultSet;
    }
    
    public DomSQLResultSet getNative() {
    	return resultSet;
    }

    public void unreferenced() {
    	resultSet = null;
    }
    
    // ===========================================================
    // Delegation
    // ===========================================================
    
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return resultSet.unwrap(iface);
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return resultSet.isWrapperFor(iface);
    }

    
    
    public boolean absolute(int row) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.absolute(row));
        } finally {
            termContext();
        }
    }

    public void afterLast() throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.afterLast();
        } finally {
            termContext();
        }
    }

    public void beforeFirst() throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.beforeFirst();
        } finally {
            termContext();
        }
    }

    public void cancelRowUpdates() throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.cancelRowUpdates();
        } finally {
            termContext();
        }
    }

    public void clearWarnings() throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.clearWarnings();
        } finally {
            termContext();
        }
    }

    public void close() throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.close();
        } finally {
            termContext();
        }
    }

    public void deleteRow() throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.deleteRow();
        } finally {
            termContext();
        }
    }

    public int findColumn(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.findColumn(columnName));
        } finally {
            termContext();
        }
    }

    public boolean first() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.first());
        } finally {
            termContext();
        }
    }

    public SArray getArray(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SArray.create(resultSet.getArray(columnIndex)));
        } finally {
            termContext();
        }
    }

    public SArray getArray(String colName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SArray.create(resultSet.getArray(colName)));
        } finally {
            termContext();
        }
    }

    public SInputStream getAsciiStream(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SInputStream.create(resultSet.getAsciiStream(columnIndex)));
        } finally {
            termContext();
        }
    }

    public SInputStream getAsciiStream(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SInputStream.create(resultSet.getAsciiStream(columnName)));
        } finally {
            termContext();
        }
    }

    public BigDecimal getBigDecimal(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getBigDecimal(columnIndex));
        } finally {
            termContext();
        }
    }

    public BigDecimal getBigDecimal(int columnIndex, int scale)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getBigDecimal(columnIndex, scale));
        } finally {
            termContext();
        }
    }

    public BigDecimal getBigDecimal(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getBigDecimal(columnName));
        } finally {
            termContext();
        }
    }

    public BigDecimal getBigDecimal(String columnName, int scale)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getBigDecimal(columnName, scale));
        } finally {
            termContext();
        }
    }

    public SInputStream getBinaryStream(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SInputStream.create(resultSet.getBinaryStream(columnIndex)));
        } finally {
            termContext();
        }
    }

    public SInputStream getBinaryStream(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SInputStream.create(resultSet.getBinaryStream(columnName)));
        } finally {
            termContext();
        }
    }

    public SBlob getBlob(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SBlob.create(resultSet.getBlob(columnIndex)));
        } finally {
            termContext();
        }
    }

    public SBlob getBlob(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SBlob.create(resultSet.getBlob(columnName)));
        } finally {
            termContext();
        }
    }

    public boolean getBoolean(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getBoolean(columnIndex));
        } finally {
            termContext();
        }
    }

    public boolean getBoolean(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getBoolean(columnName));
        } finally {
            termContext();
        }
    }

    public byte getByte(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getByte(columnIndex));
        } finally {
            termContext();
        }
    }

    public byte getByte(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getByte(columnName));
        } finally {
            termContext();
        }
    }

    public byte[] getBytes(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getBytes(columnIndex));
        } finally {
            termContext();
        }
    }

    public byte[] getBytes(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getBytes(columnName));
        } finally {
            termContext();
        }
    }

    public SReader getCharacterStream(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SReader.create(resultSet.getCharacterStream(columnIndex)));
        } finally {
            termContext();
        }
    }

    public SReader getCharacterStream(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SReader.create(resultSet.getCharacterStream(columnName)));
        } finally {
            termContext();
        }
    }

    public SClob getClob(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SClob.create(resultSet.getClob(columnIndex)));
        } finally {
            termContext();
        }
    }

    public SClob getClob(String colName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SClob.create(resultSet.getClob(colName)));
        } finally {
            termContext();
        }
    }

    public int getConcurrency() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getConcurrency());
        } finally {
            termContext();
        }
    }

    public String getCursorName() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getCursorName());
        } finally {
            termContext();
        }
    }

    public Date getDate(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getDate(columnIndex));
        } finally {
            termContext();
        }
    }

    public Date getDate(int columnIndex, Calendar cal) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getDate(columnIndex, cal));
        } finally {
            termContext();
        }
    }

    public Date getDate(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getDate(columnName));
        } finally {
            termContext();
        }
    }

    public Date getDate(String columnName, Calendar cal) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getDate(columnName, cal));
        } finally {
            termContext();
        }
    }

    public double getDouble(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getDouble(columnIndex));
        } finally {
            termContext();
        }
    }

    public double getDouble(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getDouble(columnName));
        } finally {
            termContext();
        }
    }

    public int getFetchDirection() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getFetchDirection());
        } finally {
            termContext();
        }
    }

    public int getFetchSize() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getFetchSize());
        } finally {
            termContext();
        }
    }

    public float getFloat(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getFloat(columnIndex));
        } finally {
            termContext();
        }
    }

    public float getFloat(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getFloat(columnName));
        } finally {
            termContext();
        }
    }

    public int getInt(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getInt(columnIndex));
        } finally {
            termContext();
        }
    }

    public int getInt(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getInt(columnName));
        } finally {
            termContext();
        }
    }

    public long getLong(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getLong(columnIndex));
        } finally {
            termContext();
        }
    }

    public long getLong(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getLong(columnName));
        } finally {
            termContext();
        }
    }

    public ServerResultSetMetaData getMetaData() throws SQLException, RemoteException {
    	if(_metaData==null) {
	        initContext();
	        try {
	            _readMetaData();
	        } finally {
	            termContext();
	        }
    	}
    	return wrap(_metaData);
    }

    public Object getObject(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SerializableObject.create(resultSet.getObject(columnIndex)));
        } finally {
            termContext();
        }
    }

    public Object getObject(int columnIndex, Map<String, Class<?>> map)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SerializableObject.create(resultSet.getObject(columnIndex, map)));
        } finally {
            termContext();
        }
    }

    public Object getObject(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SerializableObject.create(resultSet.getObject(columnName)));
        } finally {
            termContext();
        }
    }

    public Object getObject(String columnName, Map<String, Class<?>> map)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SerializableObject.create(resultSet.getObject(columnName, map)));
        } finally {
            termContext();
        }
    }

    public SRef getRef(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SRef.create(resultSet.getRef(columnIndex)));
        } finally {
            termContext();
        }
    }

    public SRef getRef(String colName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SRef.create(resultSet.getRef(colName)));
        } finally {
            termContext();
        }
    }

    public int getRow() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getRow());
        } finally {
            termContext();
        }
    }

    public short getShort(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getShort(columnIndex));
        } finally {
            termContext();
        }
    }

    public short getShort(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getShort(columnName));
        } finally {
            termContext();
        }
    }

    public String getString(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getString(columnIndex));
        } finally {
            termContext();
        }
    }

    public String getString(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getString(columnName));
        } finally {
            termContext();
        }
    }

    public Time getTime(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getTime(columnIndex));
        } finally {
            termContext();
        }
    }

    public Time getTime(int columnIndex, Calendar cal) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getTime(columnIndex, cal));
        } finally {
            termContext();
        }
    }

    public Time getTime(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getTime(columnName));
        } finally {
            termContext();
        }
    }

    public Time getTime(String columnName, Calendar cal) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getTime(columnName, cal));
        } finally {
            termContext();
        }
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getTimestamp(columnIndex));
        } finally {
            termContext();
        }
    }

    public Timestamp getTimestamp(int columnIndex, Calendar cal)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getTimestamp(columnIndex, cal));
        } finally {
            termContext();
        }
    }

    public Timestamp getTimestamp(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getTimestamp(columnName));
        } finally {
            termContext();
        }
    }

    public Timestamp getTimestamp(String columnName, Calendar cal)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getTimestamp(columnName, cal));
        } finally {
            termContext();
        }
    }

    public int getType() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getType());
        } finally {
            termContext();
        }
    }

    public SInputStream getUnicodeStream(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SInputStream.create(resultSet.getUnicodeStream(columnIndex)));
        } finally {
            termContext();
        }
    }

    public SInputStream getUnicodeStream(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SInputStream.create(resultSet.getUnicodeStream(columnName)));
        } finally {
            termContext();
        }
    }

    public URL getURL(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getURL(columnIndex));
        } finally {
            termContext();
        }
    }

    public URL getURL(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getURL(columnName));
        } finally {
            termContext();
        }
    }

    public SQLWarning getWarnings() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getWarnings());
        } finally {
            termContext();
        }
    }

    public void insertRow() throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.insertRow();
        } finally {
            termContext();
        }
    }

    public boolean isAfterLast() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.isAfterLast());
        } finally {
            termContext();
        }
    }

    public boolean isBeforeFirst() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.isBeforeFirst());
        } finally {
            termContext();
        }
    }

    public boolean isFirst() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.isFirst());
        } finally {
            termContext();
        }
    }

    public boolean isLast() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.isLast());
        } finally {
            termContext();
        }
    }

    public boolean last() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.last());
        } finally {
            termContext();
        }
    }

    public void moveToCurrentRow() throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.moveToCurrentRow();
        } finally {
            termContext();
        }
    }

    public void moveToInsertRow() throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.moveToInsertRow();
        } finally {
            termContext();
        }
    }

    public boolean next() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.next());
        } finally {
            termContext();
        }
    }

    public boolean previous() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.previous());
        } finally {
            termContext();
        }
    }

    public void refreshRow() throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.refreshRow();
        } finally {
            termContext();
        }
    }

    public boolean relative(int rows) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.relative(rows));
        } finally {
            termContext();
        }
    }

    public boolean rowDeleted() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.rowDeleted());
        } finally {
            termContext();
        }
    }

    public boolean rowInserted() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.rowInserted());
        } finally {
            termContext();
        }
    }

    public boolean rowUpdated() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.rowUpdated());
        } finally {
            termContext();
        }
    }

    public void setFetchDirection(int direction) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.setFetchDirection(direction);
        } finally {
            termContext();
        }
    }

    public void setFetchSize(int rows) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.setFetchSize(rows);
        } finally {
            termContext();
        }
    }

    public void updateArray(int columnIndex, SArray x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateArray(columnIndex, x);
        } finally {
            termContext();
        }
    }

    public void updateArray(String columnName, SArray x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateArray(columnName, x);
        } finally {
            termContext();
        }
    }

    public void updateAsciiStream(int columnIndex, SInputStream x, int length)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateAsciiStream(columnIndex, SInputStream.getInputStream(x), length);
        } finally {
            termContext();
        }
    }

    public void updateAsciiStream(String columnName, SInputStream x, int length)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateAsciiStream(columnName, SInputStream.getInputStream(x), length);
        } finally {
            termContext();
        }
    }

    public void updateBigDecimal(int columnIndex, BigDecimal x)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateBigDecimal(columnIndex, x);
        } finally {
            termContext();
        }
    }

    public void updateBigDecimal(String columnName, BigDecimal x)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateBigDecimal(columnName, x);
        } finally {
            termContext();
        }
    }

    public void updateBinaryStream(int columnIndex, SInputStream x, int length)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateBinaryStream(columnIndex, SInputStream.getInputStream(x), length);
        } finally {
            termContext();
        }
    }

    public void updateBinaryStream(String columnName, SInputStream x, int length)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateBinaryStream(columnName, SInputStream.getInputStream(x), length);
        } finally {
            termContext();
        }
    }

    public void updateBlob(int columnIndex, SBlob x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateBlob(columnIndex, x);
        } finally {
            termContext();
        }
    }

    public void updateBlob(String columnName, SBlob x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateBlob(columnName, x);
        } finally {
            termContext();
        }
    }

    public void updateBoolean(int columnIndex, boolean x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateBoolean(columnIndex, x);
        } finally {
            termContext();
        }
    }

    public void updateBoolean(String columnName, boolean x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateBoolean(columnName, x);
        } finally {
            termContext();
        }
    }

    public void updateByte(int columnIndex, byte x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateByte(columnIndex, x);
        } finally {
            termContext();
        }
    }

    public void updateByte(String columnName, byte x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateByte(columnName, x);
        } finally {
            termContext();
        }
    }

    public void updateBytes(int columnIndex, byte[] x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateBytes(columnIndex, x);
        } finally {
            termContext();
        }
    }

    public void updateBytes(String columnName, byte[] x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateBytes(columnName, x);
        } finally {
            termContext();
        }
    }

    public void updateCharacterStream(int columnIndex, SReader x, int length)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateCharacterStream(columnIndex, SReader.getReader(x), length);
        } finally {
            termContext();
        }
    }

    public void updateCharacterStream(String columnName, SReader reader,
            int length) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateCharacterStream(columnName, SReader.getReader(reader), length);
        } finally {
            termContext();
        }
    }

    public void updateClob(int columnIndex, SClob x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateClob(columnIndex, x);
        } finally {
            termContext();
        }
    }

    public void updateClob(String columnName, SClob x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateClob(columnName, x);
        } finally {
            termContext();
        }
    }

    public void updateDate(int columnIndex, Date x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateDate(columnIndex, x);
        } finally {
            termContext();
        }
    }

    public void updateDate(String columnName, Date x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateDate(columnName, x);
        } finally {
            termContext();
        }
    }

    public void updateDouble(int columnIndex, double x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateDouble(columnIndex, x);
        } finally {
            termContext();
        }
    }

    public void updateDouble(String columnName, double x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateDouble(columnName, x);
        } finally {
            termContext();
        }
    }

    public void updateFloat(int columnIndex, float x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateFloat(columnIndex, x);
        } finally {
            termContext();
        }
    }

    public void updateFloat(String columnName, float x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateFloat(columnName, x);
        } finally {
            termContext();
        }
    }

    public void updateInt(int columnIndex, int x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateInt(columnIndex, x);
        } finally {
            termContext();
        }
    }

    public void updateInt(String columnName, int x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateInt(columnName, x);
        } finally {
            termContext();
        }
    }

    public void updateLong(int columnIndex, long x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateLong(columnIndex, x);
        } finally {
            termContext();
        }
    }

    public void updateLong(String columnName, long x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateLong(columnName, x);
        } finally {
            termContext();
        }
    }

    public void updateNull(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateNull(columnIndex);
        } finally {
            termContext();
        }
    }

    public void updateNull(String columnName) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateNull(columnName);
        } finally {
            termContext();
        }
    }

    public void updateObject(int columnIndex, Object x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateObject(columnIndex, x);
        } finally {
            termContext();
        }
    }

    public void updateObject(int columnIndex, Object x, int scale)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateObject(columnIndex, x, scale);
        } finally {
            termContext();
        }
    }

    public void updateObject(String columnName, Object x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateObject(columnName, x);
        } finally {
            termContext();
        }
    }

    public void updateObject(String columnName, Object x, int scale)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateObject(columnName, x, scale);
        } finally {
            termContext();
        }
    }

    public void updateRef(int columnIndex, SRef x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateRef(columnIndex, x);
        } finally {
            termContext();
        }
    }

    public void updateRef(String columnName, SRef x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateRef(columnName, x);
        } finally {
            termContext();
        }
    }

    public void updateRow() throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateRow();
        } finally {
            termContext();
        }
    }

    public void updateShort(int columnIndex, short x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateShort(columnIndex, x);
        } finally {
            termContext();
        }
    }

    public void updateShort(String columnName, short x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateShort(columnName, x);
        } finally {
            termContext();
        }
    }

    public void updateString(int columnIndex, String x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateString(columnIndex, x);
        } finally {
            termContext();
        }
    }

    public void updateString(String columnName, String x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateString(columnName, x);
        } finally {
            termContext();
        }
    }

    public void updateTime(int columnIndex, Time x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateTime(columnIndex, x);
        } finally {
            termContext();
        }
    }

    public void updateTime(String columnName, Time x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateTime(columnName, x);
        } finally {
            termContext();
        }
    }

    public void updateTimestamp(int columnIndex, Timestamp x)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateTimestamp(columnIndex, x);
        } finally {
            termContext();
        }
    }

    public void updateTimestamp(String columnName, Timestamp x)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateTimestamp(columnName, x);
        } finally {
            termContext();
        }
    }

    public boolean wasNull() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.wasNull());
        } finally {
            termContext();
        }
    }

    public SRowId getRowId(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SRowId.create(resultSet.getRowId(columnIndex)));
        } finally {
            termContext();
        }
    }

    public SRowId getRowId(String columnLabel) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SRowId.create(resultSet.getRowId(columnLabel)));
        } finally {
            termContext();
        }
    }

    public void updateRowId(int columnIndex, SRowId x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateRowId(columnIndex, x);
        } finally {
            termContext();
        }
    }

    public void updateRowId(String columnLabel, SRowId x) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateRowId(columnLabel, x);
        } finally {
            termContext();
        }
    }

    public int getHoldability() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getHoldability());
        } finally {
            termContext();
        }
    }

    public boolean isClosed() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.isClosed());
        } finally {
            termContext();
        }
    }

    public void updateNString(int columnIndex, String nString)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateNString(columnIndex, nString);
        } finally {
            termContext();
        }
    }

    public void updateNString(String columnLabel, String nString)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateNString(columnLabel, nString);
        } finally {
            termContext();
        }
    }

    public void updateNClob(int columnIndex, SNClob nClob) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateNClob(columnIndex, nClob);
        } finally {
            termContext();
        }
    }

    public void updateNClob(String columnLabel, SNClob nClob)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateNClob(columnLabel, nClob);
        } finally {
            termContext();
        }
    }

    public SNClob getNClob(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SNClob.create(resultSet.getNClob(columnIndex)));
        } finally {
            termContext();
        }
    }

    public SNClob getNClob(String columnLabel) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SNClob.create(resultSet.getNClob(columnLabel)));
        } finally {
            termContext();
        }
    }

    public SSQLXML getSQLXML(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SSQLXML.create(resultSet.getSQLXML(columnIndex)));
        } finally {
            termContext();
        }
    }

    public SSQLXML getSQLXML(String columnLabel) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SSQLXML.create(resultSet.getSQLXML(columnLabel)));
        } finally {
            termContext();
        }
    }

    public void updateSQLXML(int columnIndex, SSQLXML xmlObject)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateSQLXML(columnIndex, xmlObject);
        } finally {
            termContext();
        }
    }

    public void updateSQLXML(String columnLabel, SSQLXML xmlObject)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateSQLXML(columnLabel, xmlObject);
        } finally {
            termContext();
        }
    }

    public String getNString(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getNString(columnIndex));
        } finally {
            termContext();
        }
    }

    public String getNString(String columnLabel) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(resultSet.getNString(columnLabel));
        } finally {
            termContext();
        }
    }

    public SReader getNCharacterStream(int columnIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SReader.create(resultSet.getNCharacterStream(columnIndex)));
        } finally {
            termContext();
        }
    }

    public SReader getNCharacterStream(String columnLabel) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SReader.create(resultSet.getNCharacterStream(columnLabel)));
        } finally {
            termContext();
        }
    }

    public void updateNCharacterStream(int columnIndex, SReader x, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateNCharacterStream(columnIndex, SReader.getReader(x), length);
        } finally {
            termContext();
        }
    }

    public void updateNCharacterStream(String columnLabel, SReader reader,
            long length) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateNCharacterStream(columnLabel, SReader.getReader(reader), length);
        } finally {
            termContext();
        }
    }

    public void updateAsciiStream(int columnIndex, SInputStream x, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateAsciiStream(columnIndex, SInputStream.getInputStream(x), length);
        } finally {
            termContext();
        }
    }

    public void updateBinaryStream(int columnIndex, SInputStream x, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateBinaryStream(columnIndex, SInputStream.getInputStream(x), length);
        } finally {
            termContext();
        }
    }

    public void updateCharacterStream(int columnIndex, SReader x, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateCharacterStream(columnIndex, SReader.getReader(x), length);
        } finally {
            termContext();
        }
    }

    public void updateAsciiStream(String columnLabel, SInputStream x, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateAsciiStream(columnLabel, SInputStream.getInputStream(x), length);
        } finally {
            termContext();
        }
    }

    public void updateBinaryStream(String columnLabel, SInputStream x,
            long length) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateBinaryStream(columnLabel, SInputStream.getInputStream(x), length);
        } finally {
            termContext();
        }
    }

    public void updateCharacterStream(String columnLabel, SReader reader,
            long length) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateCharacterStream(columnLabel, SReader.getReader(reader), length);
        } finally {
            termContext();
        }
    }

    public void updateBlob(int columnIndex, SInputStream inputStream, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateBlob(columnIndex, SInputStream.getInputStream(inputStream), length);
        } finally {
            termContext();
        }
    }

    public void updateBlob(String columnLabel, SInputStream inputStream,
            long length) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateBlob(columnLabel, SInputStream.getInputStream(inputStream), length);
        } finally {
            termContext();
        }
    }

    public void updateClob(int columnIndex, SReader reader, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateClob(columnIndex, SReader.getReader(reader), length);
        } finally {
            termContext();
        }
    }

    public void updateClob(String columnLabel, SReader reader, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateClob(columnLabel, SReader.getReader(reader), length);
        } finally {
            termContext();
        }
    }

    public void updateNClob(int columnIndex, SReader reader, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateNClob(columnIndex, SReader.getReader(reader), length);
        } finally {
            termContext();
        }
    }

    public void updateNClob(String columnLabel, SReader reader, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateNClob(columnLabel, SReader.getReader(reader), length);
        } finally {
            termContext();
        }
    }

    public void updateNCharacterStream(int columnIndex, SReader x)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateNCharacterStream(columnIndex, SReader.getReader(x));
        } finally {
            termContext();
        }
    }

    public void updateNCharacterStream(String columnLabel, SReader reader)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateNCharacterStream(columnLabel, SReader.getReader(reader));
        } finally {
            termContext();
        }
    }

    public void updateAsciiStream(int columnIndex, SInputStream x)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateAsciiStream(columnIndex, SInputStream.getInputStream(x));
        } finally {
            termContext();
        }
    }

    public void updateBinaryStream(int columnIndex, SInputStream x)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateBinaryStream(columnIndex, SInputStream.getInputStream(x));
        } finally {
            termContext();
        }
    }

    public void updateCharacterStream(int columnIndex, SReader x)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateCharacterStream(columnIndex, SReader.getReader(x));
        } finally {
            termContext();
        }
    }

    public void updateAsciiStream(String columnLabel, SInputStream x)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateAsciiStream(columnLabel, SInputStream.getInputStream(x));
        } finally {
            termContext();
        }
    }

    public void updateBinaryStream(String columnLabel, SInputStream x)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateBinaryStream(columnLabel, SInputStream.getInputStream(x));
        } finally {
            termContext();
        }
    }

    public void updateCharacterStream(String columnLabel, SReader reader)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateCharacterStream(columnLabel, SReader.getReader(reader));
        } finally {
            termContext();
        }
    }

    public void updateBlob(int columnIndex, SInputStream inputStream)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateBlob(columnIndex, SInputStream.getInputStream(inputStream));
        } finally {
            termContext();
        }
    }

    public void updateBlob(String columnLabel, SInputStream inputStream)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateBlob(columnLabel, SInputStream.getInputStream(inputStream));
        } finally {
            termContext();
        }
    }

    public void updateClob(int columnIndex, SReader reader) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateClob(columnIndex, SReader.getReader(reader));
        } finally {
            termContext();
        }
    }

    public void updateClob(String columnLabel, SReader reader)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateClob(columnLabel, SReader.getReader(reader));
        } finally {
            termContext();
        }
    }

    public void updateNClob(int columnIndex, SReader reader) throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateNClob(columnIndex, SReader.getReader(reader));
        } finally {
            termContext();
        }
    }

    public void updateNClob(String columnLabel, SReader reader)
            throws SQLException, RemoteException {
        initContext();
        try {
            resultSet.updateNClob(columnLabel, SReader.getReader(reader));
        } finally {
            termContext();
        }
    }

    
    // ===========================================================
    // Cache management
    // ===========================================================

    private DomSQLResultSetMetaData _metaData;
    private DomSQLResultSetMetaData _readMetaData() throws SQLException, RemoteException {
    	if(_metaData==null) {
	        _metaData = resultSet.getMetaData();
    	}
    	return _metaData;
    }
    
    
    // ===========================================================
    // Extra methods
    // ===========================================================
    
	public Object[] _getObjects() throws SQLException, RemoteException {
        initContext();
        try {
        	return __getObjects();
        } finally {
            termContext();
        }
	}
	public Object[] __getObjects() throws SQLException, RemoteException {
    	DomSQLResultSet rs = getNative();
    	DomSQLResultSetMetaData meta = _readMetaData();
    	return _readRowValues(rs,meta);
	}
	
	public Object[][] _readRows(int count) throws SQLException, RemoteException {
        initContext();
        try {
        	DomSQLResultSet rs = getNative();
        	DomSQLResultSetMetaData meta = _readMetaData();
        	ArrayList<Object[]> rows = new ArrayList<Object[]>();
        	for(int i=0; i<count; i++) {
        		if(!rs.hasRow()) {
        			break;
        		}
        		rows.add(_readRowValues(rs, meta));
        	}
        	return rows.toArray(new Object[rows.size()][]);
        } finally {
            termContext();
        }
	}
	
	private Object[] _readRowValues(DomSQLResultSet rs, DomSQLResultSetMetaData meta) throws SQLException {
    	int count = meta.getColumnCount();
    	Object[] result = new Object[count];
    	for(int i=0; i<count; i++) {
    		result[i] = SerializableObject.create(rs.getObject(i+1)); // JDBC is one based..
    	}
    	return result;
	}
	
	public Object[] _next() throws SQLException, RemoteException {
        initContext();
        try {
            if(wrap(resultSet.next())) {
            	return __getObjects();
            }
            return null;
        } finally {
            termContext();
        }
	}	
}
