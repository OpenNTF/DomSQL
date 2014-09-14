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
package com.ibm.domino.domsql.sqlite.driver.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import com.ibm.commons.util.DateTime;
import com.ibm.commons.util.NotImplementedException;
import com.ibm.domino.domsql.remote.client.DataConverter;
import com.ibm.domino.domsql.sqlite.DomSQLException;
import com.ibm.domino.domsql.sqlite.driver.SQLite;


/**
 * Implements a JDBC ResultSet.
 */
public class DomSQLResultSet implements ResultSet {
    
    private DomSQLStatement stmt;

    private boolean closed;
    
    private int columnCount;
    private String[] colNames;

    private int fetchSize;
    private int fetchDirection;
    
    private int currentRow;
    private int currentCol; 
    
    private boolean closeStatementOnClose;

    public DomSQLResultSet(DomSQLStatement stmt,int columnCount, boolean empty) {
        this.stmt = stmt;
        this.columnCount = columnCount;
        this.fetchSize = stmt.getFetchSize();
        this.fetchDirection = stmt.getFetchDirection();
        this.currentRow = empty ? 0 : 1;
    }

    public DomSQLStatement getStatement() {
        return stmt;
    }

    public DomSQLResultSetMetaData getMetaData() throws SQLException {
        return new DomSQLResultSetMetaData(getHstmt());
    }

    public int getColumnCount() throws SQLException {
        return columnCount;
    }


    public DomSQLConnection getConnection() {
        return getStatement().getConnection();
    }
    
    public long getHstmt() {
        return getStatement().getHstmt();
    }
    
    public long getSqliteDb() {
        return getStatement().getSqliteDb();
    }
    
    public boolean isCloseStatementOnClose() {
        return closeStatementOnClose;
    }

    public void setCloseStatementOnClose(boolean closeStatementOnClose) {
        this.closeStatementOnClose = closeStatementOnClose;
    }
    
    

    // INTERNAL FUNCTIONS ///////////////////////////////////////////

    private void checkOpen() throws SQLException {
        if(closed) {
            throw DomSQLException.create(null,"The ResultSet is closed");
        }
    }

    public int jdbcToSqliteCol(int col, boolean store) throws SQLException {
        if(col<1 || col>columnCount) {
            throw DomSQLException.create(null,"Invalid column index {0}",col);
        }
        if(store) {
            currentCol = col;
        }
        return --col;
    }

    public int jdbcToSqliteCol(int col) throws SQLException {
        return jdbcToSqliteCol(col,true);
    }

    
    // ResultSet Functions //////////////////////////////////////////

    public boolean isClosed() throws SQLException {
        return closed;
    }

    public void close() throws SQLException {
        // Close the result set
        if(!closed) {
            release();
            if(stmt!=null) {
                if(closeStatementOnClose) {
                    stmt.close();
                } else {
                    stmt.resetStatement();
                }
                stmt = null;
            }
        }
    }
    public void release() throws SQLException {
        // Close the result set
        if(!closed) {
            closed = true;
            colNames = null;
            currentRow = 1;
            currentCol = -1;
        }
    }

    // But sure that the columns are in the range 1...n
    public int findColumn(String col) throws SQLException {
        checkOpen();
        if(colNames==null) {
            colNames = new String[columnCount];
            for(int i=0; i<columnCount; i++) {
               colNames[i] = SQLite.columnName(getHstmt(), i);
            }
        }
        for(int i=0; i<colNames.length; i++) {
            if(col.equalsIgnoreCase(colNames[i])) {
                return i+1;
            }
        }
        throw DomSQLException.create(null,"Column {0} is unknown", col);
    }

    public boolean next() throws SQLException {
        // The result set had been close, or is empty
        if(closed||currentRow==0) {
            return false;
        }
        currentCol = -1;

        // first row is loaded by execute(), so do not step() again
        if (currentRow == 1) {
            currentRow++;
            return true;
        }

        // check if we are row limited by the statement or the ResultSet
        int maxRows = stmt.getMaxRows();
        if (maxRows != 0 && currentRow > maxRows) {
            return false;
        }

        // do the real work
        switch (SQLite.step(getHstmt())) {
            case SQLite.SQLITE_DONE: {
                close();
                return false;
            }
            case SQLite.SQLITE_ROW: {
                currentRow++;
                return true;
            }
            case SQLite.SQLITE_BUSY: {
                throw DomSQLException.create(null,"Database is locked");
            }
            default: {
                getConnection().throwex();
                return false;
            }
        }
    }

    public int getType() throws SQLException {
        return TYPE_FORWARD_ONLY;
    }

    public int getFetchSize() throws SQLException {
        return fetchSize;
    }

    public void setFetchSize(int fetchSize) throws SQLException {
        if (fetchSize<0) {
            throw DomSQLException.create(null,"Invalid FetchSize value. Must be greater than 0 and less or equals to MaxRow");
        }
        this.fetchSize = fetchSize;
    }
    
    public int getFetchDirection() throws SQLException {
        return fetchDirection;
    }

    public void setFetchDirection(int fetchDirection) throws SQLException {
        if (fetchDirection!=ResultSet.FETCH_FORWARD){
            throw DomSQLException.create(null,"SQLite only supports FETCH_FORWARD");
        }
        this.fetchDirection = fetchDirection;
    }

    public boolean isAfterLast() throws SQLException {
        return closed;
    }

    public boolean isBeforeFirst() throws SQLException {
        return !closed && currentRow<=1;
    }

    public boolean isFirst() throws SQLException {
        return currentRow==2;
    }

    public boolean isLast() throws SQLException { // TODO
        throw new NotImplementedException();
    }

    protected void finalize() throws SQLException {
        close();
    }


    //@Override
    public boolean absolute(int row) throws SQLException {
    	return relative(row-currentRow);
    }

    //@Override
    public boolean relative(int rows) throws SQLException {
    	if(rows>0) {
    		while(rows>0) {
    			if(!next()) {
    				return false;
    			}
    			rows--;
    		}
    		return true;
    	}
    	throw DomSQLException.create(null, "Cannot move a cursor backwards");
    }
    
    //@Override
    public int getRow() throws SQLException {
        return currentRow;
    }

    public boolean hasRow() throws SQLException {
        // The result set had been close, or is empty
        if(closed||currentRow==0) {
            return false;
        }
        return true;
    }
    
    
    // DATA ACCESS FUNCTIONS ////////////////////////////////////////

    public boolean wasNull() throws SQLException {
        return SQLite.columnType(getHstmt(), jdbcToSqliteCol(currentCol)) == SQLite.SQLITE_NULL;
    }
    
    public boolean getBoolean(int col) throws SQLException {
        return getInt(col)!=0;
    }

    public boolean getBoolean(String col) throws SQLException {
        return getBoolean(findColumn(col));
    }

    public byte getByte(int col) throws SQLException {
        return (byte)getInt(col);
    }

    public byte getByte(String col) throws SQLException {
        return getByte(findColumn(col));
    }

    public byte[] getBytes(int col) throws SQLException {
        return SQLite.columnBlob(getHstmt(), jdbcToSqliteCol(col));
    }

    public byte[] getBytes(String col) throws SQLException {
        return getBytes(findColumn(col));
    }

    public Date getDate(int col) throws SQLException {
        if (SQLite.columnType(getHstmt(), jdbcToSqliteCol(col)) == SQLite.SQLITE_NULL) {
            return null;
        }
        long dt = SQLite.columnInt64(getHstmt(), jdbcToSqliteCol(col))*1000;
        return new Date(dt);
    }

    public Date getDate(int col, Calendar cal) throws SQLException {
        if (SQLite.columnType(getHstmt(), jdbcToSqliteCol(col)) == SQLite.SQLITE_NULL) {
            return null;
        }
        if (cal == null) {
            return getDate(col);
        }
        long dt = SQLite.columnInt64(getHstmt(), jdbcToSqliteCol(col))*1000;
        cal.setTimeInMillis(dt);
        return new Date(cal.getTime().getTime());
    }

    public Date getDate(String col) throws SQLException {
        return getDate(findColumn(col), Calendar.getInstance());
    }

    public Date getDate(String col, Calendar cal) throws SQLException {
        return getDate(findColumn(col), cal);
    }

    public double getDouble(int col) throws SQLException {
        if (SQLite.columnType(getHstmt(), jdbcToSqliteCol(col)) == SQLite.SQLITE_NULL) {
            return 0;
        }
        return SQLite.columnDouble(getHstmt(), jdbcToSqliteCol(col));
    }

    public double getDouble(String col) throws SQLException {
        return getDouble(findColumn(col));
    }

    public float getFloat(int col) throws SQLException {
        if (SQLite.columnType(getHstmt(), jdbcToSqliteCol(col)) == SQLite.SQLITE_NULL)
            return 0;
        return (float) SQLite.columnDouble(getHstmt(), jdbcToSqliteCol(col));
    }

    public float getFloat(String col) throws SQLException {
        return getFloat(findColumn(col));
    }

    public int getInt(int col) throws SQLException {
        return SQLite.columnInt(getHstmt(), jdbcToSqliteCol(col));
    }

    public int getInt(String col) throws SQLException {
        return getInt(findColumn(col));
    }

    public long getLong(int col) throws SQLException {
        return SQLite.columnInt64(getHstmt(), jdbcToSqliteCol(col));
    }

    public long getLong(String col) throws SQLException {
        return getLong(findColumn(col));
    }

    public short getShort(int col) throws SQLException {
        return (short) getInt(col);
    }

    public short getShort(String col) throws SQLException {
        return getShort(findColumn(col));
    }

    public String getString(int col) throws SQLException {
        return SQLite.columnText(getHstmt(), jdbcToSqliteCol(col));
    }

    public String getString(String col) throws SQLException {
        return getString(findColumn(col));
    }

    public Time getTime(int col) throws SQLException {
        if (SQLite.columnType(getHstmt(), jdbcToSqliteCol(col)) == SQLite.SQLITE_NULL) {
            return null;
        }
        long dt = SQLite.columnInt64(getHstmt(), jdbcToSqliteCol(col))*1000;
        return new Time(dt);
    }

    public Time getTime(int col, Calendar cal) throws SQLException {
        if (cal == null)
            return getTime(col);
        if (SQLite.columnType(getHstmt(), jdbcToSqliteCol(col)) == SQLite.SQLITE_NULL)
            return null;
        long dt = SQLite.columnInt64(getHstmt(), jdbcToSqliteCol(col))*1000;
        cal.setTimeInMillis(dt);
        return new Time(cal.getTime().getTime());
    }

    public Time getTime(String col) throws SQLException {
        return getTime(findColumn(col));
    }

    public Time getTime(String col, Calendar cal) throws SQLException {
        return getTime(findColumn(col), cal);
    }

    public Timestamp getTimestamp(int col) throws SQLException {
        if (SQLite.columnType(getHstmt(), jdbcToSqliteCol(col)) == SQLite.SQLITE_NULL) {
            return null;
        }
        long dt = SQLite.columnInt64(getHstmt(), jdbcToSqliteCol(col))*1000;
        return new Timestamp(dt);
    }

    public Timestamp getTimestamp(int col, Calendar cal) throws SQLException {
        if (cal == null) {
            return getTimestamp(col);
        }
        if (SQLite.columnType(getHstmt(), jdbcToSqliteCol(col)) == SQLite.SQLITE_NULL) {
            return null;
        }
        long dt = SQLite.columnInt64(getHstmt(), jdbcToSqliteCol(col))*1000;
        cal.setTimeInMillis(dt);
        return new Timestamp(cal.getTime().getTime());
    }

    public Timestamp getTimestamp(String col) throws SQLException {
        return getTimestamp(findColumn(col));
    }

    public Timestamp getTimestamp(String c, Calendar ca) throws SQLException {
        return getTimestamp(findColumn(c), ca);
    }

    public Object getObject(int col) throws SQLException {
        switch (SQLite.columnType(getHstmt(), jdbcToSqliteCol(col))) {
            case SQLite.SQLITE_INTEGER: {
                long val = getLong(col);
                return Long.valueOf(val);
            }
            case SQLite.SQLITE_FLOAT: {
                return Double.valueOf(getDouble(col));
            }
            case SQLite.SQLITE_BLOB: {
                return getBytes(col);
            }
            case SQLite.SQLITE_NULL: {
                return null;
            }
        }
        String s = getString(col);
        // Look if it looks like a ISO8801 date
        //   YYYY-MM-DD HH:MM:SS.SSS
        if(s.length()==23 && s.charAt(4)=='-' && s.charAt(7)=='-' && s.charAt(10)==' ' && s.charAt(13)==':'&& s.charAt(16)==':'&& s.charAt(19)=='.') {
        	int year = parseInt(s, 0, 4);
        	int month = parseInt(s, 5, 2);
        	int day = parseInt(s, 8, 2);
        	int hour = parseInt(s, 11, 2);
        	int minute = parseInt(s, 14, 2);
        	int second = parseInt(s, 17, 2);
        	int millis = parseInt(s, 20, 3);
        	return new java.sql.Timestamp(DateTime.DateStruct.createDateAsLong(year,month,day,hour,minute,second,millis));
        }
        
        return s;
    }
    private int parseInt(String s, int pos, int length) {
    	int value = 0;
    	for(int i=0; i<length; i++) {
    		value = (value*10)+(s.charAt(pos+i)-'0');
    	}
    	return value;
    }
    
    public Object getObject(String col) throws SQLException {
        return getObject(findColumn(col));
    }

    public String getCursorName() throws SQLException {
        return null;
    }

    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    public void clearWarnings() throws SQLException {
    }

    public int getConcurrency() throws SQLException {
        return CONCUR_READ_ONLY;
    }

    public boolean rowDeleted() throws SQLException {
        return false;
    }

    public boolean rowInserted() throws SQLException {
        return false;
    }

    public boolean rowUpdated() throws SQLException {
        return false;
    }

    //@Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void afterLast() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void beforeFirst() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void cancelRowUpdates() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void deleteRow() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public boolean first() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Array getArray(int columnIndex) throws SQLException {
        return DataConverter.toArray(getObject(columnIndex));
    }

    //@Override
    public Array getArray(String columnLabel) throws SQLException {
        return DataConverter.toArray(getObject(columnLabel));
    }

    //@Override
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return DataConverter.toInputStream(getObject(columnIndex));
    }

    //@Override
    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        return DataConverter.toInputStream(getObject(columnLabel));
    }

    //@Override
    public BigDecimal getBigDecimal(int columnIndex, int scale)
            throws SQLException {
        return DataConverter.toBigDecimal(getObject(columnIndex),scale);
    }

    //@Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return DataConverter.toBigDecimal(getObject(columnIndex));
    }

    //@Override
    public BigDecimal getBigDecimal(String columnLabel, int scale)
            throws SQLException {
        return DataConverter.toBigDecimal(getObject(columnLabel),scale);
    }

    //@Override
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return DataConverter.toBigDecimal(getObject(columnLabel));
    }

    //@Override
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return DataConverter.toInputStream(getObject(columnIndex));
    }

    //@Override
    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        return DataConverter.toInputStream(getObject(columnLabel));
    }

    //@Override
    public Blob getBlob(int columnIndex) throws SQLException {
        return DataConverter.toBlob(getObject(columnIndex));
    }

    //@Override
    public Blob getBlob(String columnLabel) throws SQLException {
        return DataConverter.toBlob(getObject(columnLabel));
    }

    //@Override
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return DataConverter.toReader(getObject(columnIndex));
    }

    //@Override
    public Reader getCharacterStream(String columnLabel) throws SQLException {
        return DataConverter.toReader(getObject(columnLabel));
    }

    //@Override
    public Clob getClob(int columnIndex) throws SQLException {
        return DataConverter.toClob(getObject(columnIndex));
    }

    //@Override
    public Clob getClob(String columnLabel) throws SQLException {
        return DataConverter.toClob(getObject(columnLabel));
    }

    //@Override
    public int getHoldability() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        return DataConverter.toReader(getObject(columnIndex));
    }

    //@Override
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        return DataConverter.toReader(getObject(columnLabel));
    }

    //@Override
    public NClob getNClob(int columnIndex) throws SQLException {
        return DataConverter.toNClob(getObject(columnIndex));
    }

    //@Override
    public NClob getNClob(String columnLabel) throws SQLException {
        return DataConverter.toNClob(getObject(columnLabel));
    }

    //@Override
    public String getNString(int columnIndex) throws SQLException {
        return DataConverter.toString(getObject(columnIndex));
    }

    //@Override
    public String getNString(String columnLabel) throws SQLException {
        return DataConverter.toString(getObject(columnLabel));
    }

    //@Override
    public Object getObject(int columnIndex, Map<String, Class<?>> map)
            throws SQLException {
    	// Ignore map...
        return getObject(columnIndex);
    }

    //@Override
    public Object getObject(String columnLabel, Map<String, Class<?>> map)
            throws SQLException {
    	// Ignore map...
        return getObject(columnLabel);
    }

    //@Override
    public Ref getRef(int columnIndex) throws SQLException {
        return DataConverter.toRef(getObject(columnIndex));
    }

    //@Override
    public Ref getRef(String columnLabel) throws SQLException {
        return DataConverter.toRef(getObject(columnLabel));
    }

    //@Override
    public RowId getRowId(int columnIndex) throws SQLException {
        return DataConverter.toRowId(getObject(columnIndex));
    }

    //@Override
    public RowId getRowId(String columnLabel) throws SQLException {
        return DataConverter.toRowId(getObject(columnLabel));
    }

    //@Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        return DataConverter.toSQLXML(getObject(columnIndex));
    }

    //@Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        return DataConverter.toSQLXML(getObject(columnLabel));
    }

    //@Override
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        return DataConverter.toInputStream(getObject(columnIndex));
    }

    //@Override
    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
        return DataConverter.toInputStream(getObject(columnLabel));
    }

    //@Override
    public URL getURL(int columnIndex) throws SQLException {
        return DataConverter.toURL(getObject(columnIndex));
    }

    //@Override
    public URL getURL(String columnLabel) throws SQLException {
        return DataConverter.toURL(getObject(columnLabel));
    }

    //@Override
    public void insertRow() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public boolean last() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void moveToCurrentRow() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void moveToInsertRow() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public boolean previous() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void refreshRow() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateArray(int columnIndex, Array x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateArray(String columnLabel, Array x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateAsciiStream(int columnIndex, InputStream x, int length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateAsciiStream(int columnIndex, InputStream x)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateAsciiStream(String columnLabel, InputStream x, int length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateAsciiStream(String columnLabel, InputStream x, long length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateAsciiStream(String columnLabel, InputStream x)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateBigDecimal(int columnIndex, BigDecimal x)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateBigDecimal(String columnLabel, BigDecimal x)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateBinaryStream(int columnIndex, InputStream x, int length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateBinaryStream(int columnIndex, InputStream x)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateBinaryStream(String columnLabel, InputStream x, int length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateBinaryStream(String columnLabel, InputStream x,
            long length) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateBinaryStream(String columnLabel, InputStream x)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateBlob(int columnIndex, InputStream inputStream)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateBlob(String columnLabel, Blob x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateBlob(String columnLabel, InputStream inputStream,
            long length) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateBlob(String columnLabel, InputStream inputStream)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateBoolean(String columnLabel, boolean x)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateByte(int columnIndex, byte x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateByte(String columnLabel, byte x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateBytes(String columnLabel, byte[] x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateCharacterStream(int columnIndex, Reader x, int length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateCharacterStream(int columnIndex, Reader x, long length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateCharacterStream(int columnIndex, Reader x)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateCharacterStream(String columnLabel, Reader reader,
            int length) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateCharacterStream(String columnLabel, Reader reader,
            long length) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateCharacterStream(String columnLabel, Reader reader)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateClob(int columnIndex, Clob x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateClob(int columnIndex, Reader reader, long length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateClob(String columnLabel, Clob x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateClob(String columnLabel, Reader reader, long length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateClob(String columnLabel, Reader reader)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateDate(int columnIndex, Date x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateDate(String columnLabel, Date x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateDouble(int columnIndex, double x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateDouble(String columnLabel, double x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateFloat(int columnIndex, float x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateFloat(String columnLabel, float x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateInt(int columnIndex, int x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateInt(String columnLabel, int x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateLong(int columnIndex, long x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateLong(String columnLabel, long x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateNCharacterStream(int columnIndex, Reader x)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateNCharacterStream(String columnLabel, Reader reader,
            long length) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateNCharacterStream(String columnLabel, Reader reader)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateNClob(int columnIndex, NClob clob) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateNClob(int columnIndex, Reader reader, long length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateNClob(String columnLabel, NClob clob) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateNClob(String columnLabel, Reader reader, long length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateNClob(String columnLabel, Reader reader)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateNString(int columnIndex, String string)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateNString(String columnLabel, String string)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateNull(int columnIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateNull(String columnLabel) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateObject(int columnIndex, Object x, int scaleOrLength)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateObject(int columnIndex, Object x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateObject(String columnLabel, Object x, int scaleOrLength)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateObject(String columnLabel, Object x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateRef(int columnIndex, Ref x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateRef(String columnLabel, Ref x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateRow() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateShort(int columnIndex, short x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateShort(String columnLabel, short x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateString(int columnIndex, String x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateString(String columnLabel, String x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateTime(int columnIndex, Time x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateTime(String columnLabel, Time x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateTimestamp(int columnIndex, Timestamp x)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void updateTimestamp(String columnLabel, Timestamp x)
            throws SQLException {
        throw new NotImplementedException();
    }
}
