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
import java.util.Calendar;

import com.ibm.domino.domsql.remote.transport.IConnection;
import com.ibm.domino.domsql.remote.transport.IParameterMetaData;
import com.ibm.domino.domsql.remote.transport.IPreparedStatement;
import com.ibm.domino.domsql.remote.transport.IResultSet;
import com.ibm.domino.domsql.remote.transport.IResultSetMetaData;
import com.ibm.domino.domsql.remote.transport.SArray;
import com.ibm.domino.domsql.remote.transport.SBlob;
import com.ibm.domino.domsql.remote.transport.SClob;
import com.ibm.domino.domsql.remote.transport.SInputStream;
import com.ibm.domino.domsql.remote.transport.SNClob;
import com.ibm.domino.domsql.remote.transport.SReader;
import com.ibm.domino.domsql.remote.transport.SRef;
import com.ibm.domino.domsql.remote.transport.SRowId;
import com.ibm.domino.domsql.remote.transport.SSQLXML;
import com.ibm.domino.domsql.sqlite.driver.jdbc.DomSQLPreparedStatement;

/**
 *
 */
public class ServerPreparedStatement extends ServerStatement implements
        IPreparedStatement {

	private static final long serialVersionUID = 1L;
	
    private DomSQLPreparedStatement preparedStatement;
    
    public ServerPreparedStatement(ServerConnection connection, DomSQLPreparedStatement preparedStatement) throws RemoteException {
        super(connection,preparedStatement);
        this.preparedStatement = preparedStatement;
    }
    
    public DomSQLPreparedStatement getNative() {
    	return preparedStatement;
    }

    public void unreferenced() {
    	preparedStatement = null;
    }
    
    
    // ===========================================================
    // Delegation
    // ===========================================================
    
    public <T> T unwrap(Class<T> iface) throws SQLException, RemoteException {
        return preparedStatement.unwrap(iface);
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException, RemoteException {
        return preparedStatement.isWrapperFor(iface);
    }

    
    
    public void addBatch() throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.addBatch();
        } finally {
            termContext();
        }
    }

    public void clearParameters() throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.clearParameters();
        } finally {
            termContext();
        }
    }

    public boolean execute() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.execute());
        } finally {
            termContext();
        }
    }

    public void addBatch(String sql) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.addBatch(sql);
        } finally {
            termContext();
        }
    }

    public IResultSet executeQuery() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.executeQuery());
        } finally {
            termContext();
        }
    }

    public void cancel() throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.cancel();
        } finally {
            termContext();
        }
    }

    public int executeUpdate() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.executeUpdate());
        } finally {
            termContext();
        }
    }

    public void clearBatch() throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.clearBatch();
        } finally {
            termContext();
        }
    }

    public IResultSetMetaData getMetaData() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.getMetaData());
        } finally {
            termContext();
        }
    }

    public void clearWarnings() throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.clearWarnings();
        } finally {
            termContext();
        }
    }

    public void close() throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.close();
        } finally {
            termContext();
        }
    }

    public IParameterMetaData getParameterMetaData() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.getParameterMetaData());
        } finally {
            termContext();
        }
    }

    public boolean execute(String sql) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.execute(sql));
        } finally {
            termContext();
        }
    }

    public void setArray(int parameterIndex, SArray theArray)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setArray(parameterIndex, theArray);
        } finally {
            termContext();
        }
    }

    public boolean execute(String sql, int autoGeneratedKeys)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.execute(sql, autoGeneratedKeys));
        } finally {
            termContext();
        }
    }

    public void setAsciiStream(int parameterIndex, SInputStream theInputStream,
            int length) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setAsciiStream(parameterIndex, SInputStream.getInputStream(theInputStream),
                    length);
        } finally {
            termContext();
        }
    }

    public void setBigDecimal(int parameterIndex, BigDecimal theBigDecimal)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setBigDecimal(parameterIndex, theBigDecimal);
        } finally {
            termContext();
        }
    }

    public boolean execute(String sql, int[] columnIndexes) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.execute(sql, columnIndexes));
        } finally {
            termContext();
        }
    }

    public void setBinaryStream(int parameterIndex, SInputStream theInputStream,
            int length) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setBinaryStream(parameterIndex, SInputStream.getInputStream(theInputStream),
                    length);
        } finally {
            termContext();
        }
    }

    public boolean execute(String sql, String[] columnNames)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.execute(sql, columnNames));
        } finally {
            termContext();
        }
    }

    public void setBlob(int parameterIndex, SBlob theBlob) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setBlob(parameterIndex, theBlob);
        } finally {
            termContext();
        }
    }

    public void setBoolean(int parameterIndex, boolean theBoolean)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setBoolean(parameterIndex, theBoolean);
        } finally {
            termContext();
        }
    }

    public int[] executeBatch() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.executeBatch());
        } finally {
            termContext();
        }
    }

    public void setByte(int parameterIndex, byte theByte) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setByte(parameterIndex, theByte);
        } finally {
            termContext();
        }
    }

    public void setBytes(int parameterIndex, byte[] theBytes)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setBytes(parameterIndex, theBytes);
        } finally {
            termContext();
        }
    }

    public void setCharacterStream(int parameterIndex, SReader reader, int length)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement
                    .setCharacterStream(parameterIndex, SReader.getReader(reader), length);
        } finally {
            termContext();
        }
    }

    public IResultSet executeQuery(String sql) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.executeQuery(sql));
        } finally {
            termContext();
        }
    }

    public void setClob(int parameterIndex, SClob theClob) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setClob(parameterIndex, theClob);
        } finally {
            termContext();
        }
    }

    public int executeUpdate(String sql) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.executeUpdate(sql));
        } finally {
            termContext();
        }
    }

    public void setDate(int parameterIndex, Date theDate) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setDate(parameterIndex, theDate);
        } finally {
            termContext();
        }
    }

    public int executeUpdate(String sql, int autoGeneratedKeys)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.executeUpdate(sql, autoGeneratedKeys));
        } finally {
            termContext();
        }
    }

    public void setDate(int parameterIndex, Date theDate, Calendar cal)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setDate(parameterIndex, theDate, cal);
        } finally {
            termContext();
        }
    }

    public void setDouble(int parameterIndex, double theDouble)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setDouble(parameterIndex, theDouble);
        } finally {
            termContext();
        }
    }

    public int executeUpdate(String sql, int[] columnIndexes)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.executeUpdate(sql, columnIndexes));
        } finally {
            termContext();
        }
    }

    public void setFloat(int parameterIndex, float theFloat)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setFloat(parameterIndex, theFloat);
        } finally {
            termContext();
        }
    }

    public int executeUpdate(String sql, String[] columnNames)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.executeUpdate(sql, columnNames));
        } finally {
            termContext();
        }
    }

    public void setInt(int parameterIndex, int theInt) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setInt(parameterIndex, theInt);
        } finally {
            termContext();
        }
    }

    public void setLong(int parameterIndex, long theLong) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setLong(parameterIndex, theLong);
        } finally {
            termContext();
        }
    }

    public IConnection getConnection() throws SQLException, RemoteException {
        return (ServerConnection)getRemoteConnection();
    }

    public void setNull(int parameterIndex, int sqlType) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setNull(parameterIndex, sqlType);
        } finally {
            termContext();
        }
    }

    public int getFetchDirection() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.getFetchDirection());
        } finally {
            termContext();
        }
    }

    public void setNull(int paramIndex, int sqlType, String typeName)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setNull(paramIndex, sqlType, typeName);
        } finally {
            termContext();
        }
    }

    public int getFetchSize() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.getFetchSize());
        } finally {
            termContext();
        }
    }

    public IResultSet getGeneratedKeys() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.getGeneratedKeys());
        } finally {
            termContext();
        }
    }

    public int getMaxFieldSize() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.getMaxFieldSize());
        } finally {
            termContext();
        }
    }

    public void setObject(int parameterIndex, Object theObject)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setObject(parameterIndex, theObject);
        } finally {
            termContext();
        }
    }

    public int getMaxRows() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.getMaxRows());
        } finally {
            termContext();
        }
    }

    public boolean getMoreResults() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.getMoreResults());
        } finally {
            termContext();
        }
    }

    public void setObject(int parameterIndex, Object theObject,
            int targetSqlType) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setObject(parameterIndex, theObject,
                    targetSqlType);
        } finally {
            termContext();
        }
    }

    public boolean getMoreResults(int current) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.getMoreResults(current));
        } finally {
            termContext();
        }
    }

    public void setObject(int parameterIndex, Object theObject,
            int targetSqlType, int scale) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setObject(parameterIndex, theObject,
                    targetSqlType, scale);
        } finally {
            termContext();
        }
    }

    public int getQueryTimeout() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.getQueryTimeout());
        } finally {
            termContext();
        }
    }

    public IResultSet getResultSet() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.getResultSet());
        } finally {
            termContext();
        }
    }

    public void setRef(int parameterIndex, SRef theRef) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setRef(parameterIndex, theRef);
        } finally {
            termContext();
        }
    }

    public int getResultSetConcurrency() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.getResultSetConcurrency());
        } finally {
            termContext();
        }
    }

    public void setShort(int parameterIndex, short theShort)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setShort(parameterIndex, theShort);
        } finally {
            termContext();
        }
    }

    public int getResultSetHoldability() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.getResultSetHoldability());
        } finally {
            termContext();
        }
    }

    public void setString(int parameterIndex, String theString)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setString(parameterIndex, theString);
        } finally {
            termContext();
        }
    }

    public int getResultSetType() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.getResultSetType());
        } finally {
            termContext();
        }
    }

    public void setTime(int parameterIndex, Time theTime) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setTime(parameterIndex, theTime);
        } finally {
            termContext();
        }
    }

    public int getUpdateCount() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.getUpdateCount());
        } finally {
            termContext();
        }
    }

    public void setTime(int parameterIndex, Time theTime, Calendar cal)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setTime(parameterIndex, theTime, cal);
        } finally {
            termContext();
        }
    }

    public SQLWarning getWarnings() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.getWarnings());
        } finally {
            termContext();
        }
    }

    public void setCursorName(String name) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setCursorName(name);
        } finally {
            termContext();
        }
    }

    public void setTimestamp(int parameterIndex, Timestamp theTimestamp)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setTimestamp(parameterIndex, theTimestamp);
        } finally {
            termContext();
        }
    }

    public void setTimestamp(int parameterIndex, Timestamp theTimestamp,
            Calendar cal) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setTimestamp(parameterIndex, theTimestamp, cal);
        } finally {
            termContext();
        }
    }

    public void setEscapeProcessing(boolean enable) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setEscapeProcessing(enable);
        } finally {
            termContext();
        }
    }

    public void setFetchDirection(int direction) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setFetchDirection(direction);
        } finally {
            termContext();
        }
    }

    public void setUnicodeStream(int parameterIndex,
            SInputStream theInputStream, int length) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setUnicodeStream(parameterIndex, SInputStream.getInputStream(theInputStream),
                    length);
        } finally {
            termContext();
        }
    }

    public void setFetchSize(int rows) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setFetchSize(rows);
        } finally {
            termContext();
        }
    }

    public void setURL(int parameterIndex, URL theURL) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setURL(parameterIndex, theURL);
        } finally {
            termContext();
        }
    }

    public void setMaxFieldSize(int max) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setMaxFieldSize(max);
        } finally {
            termContext();
        }
    }

    public void setRowId(int parameterIndex, SRowId x) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setRowId(parameterIndex, x);
        } finally {
            termContext();
        }
    }

    public void setNString(int parameterIndex, String value)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setNString(parameterIndex, value);
        } finally {
            termContext();
        }
    }

    public void setNCharacterStream(int parameterIndex, SReader value,
            long length) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement
                    .setNCharacterStream(parameterIndex, SReader.getReader(value), length);
        } finally {
            termContext();
        }
    }

    public void setNClob(int parameterIndex, SNClob value) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setNClob(parameterIndex, value);
        } finally {
            termContext();
        }
    }

    public void setClob(int parameterIndex, SReader reader, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setClob(parameterIndex, SReader.getReader(reader), length);
        } finally {
            termContext();
        }
    }

    public void setBlob(int parameterIndex, SInputStream inputStream, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setBlob(parameterIndex, SInputStream.getInputStream(inputStream), length);
        } finally {
            termContext();
        }
    }

    public void setMaxRows(int max) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setMaxRows(max);
        } finally {
            termContext();
        }
    }

    public void setNClob(int parameterIndex, SReader reader, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setNClob(parameterIndex, SReader.getReader(reader), length);
        } finally {
            termContext();
        }
    }

    public void setSQLXML(int parameterIndex, SSQLXML xmlObject)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setSQLXML(parameterIndex, xmlObject);
        } finally {
            termContext();
        }
    }

    public void setAsciiStream(int parameterIndex, SInputStream x, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setAsciiStream(parameterIndex, SInputStream.getInputStream(x), length);
        } finally {
            termContext();
        }
    }

    public void setBinaryStream(int parameterIndex, SInputStream x, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setBinaryStream(parameterIndex, SInputStream.getInputStream(x), length);
        } finally {
            termContext();
        }
    }

    public void setQueryTimeout(int seconds) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setQueryTimeout(seconds);
        } finally {
            termContext();
        }
    }

    public void setCharacterStream(int parameterIndex, SReader reader,
            long length) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement
                    .setCharacterStream(parameterIndex, SReader.getReader(reader), length);
        } finally {
            termContext();
        }
    }

    public void setAsciiStream(int parameterIndex, SInputStream x)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setAsciiStream(parameterIndex, SInputStream.getInputStream(x));
        } finally {
            termContext();
        }
    }

    public void setBinaryStream(int parameterIndex, SInputStream x)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setBinaryStream(parameterIndex, SInputStream.getInputStream(x));
        } finally {
            termContext();
        }
    }

    public void setCharacterStream(int parameterIndex, SReader reader)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setCharacterStream(parameterIndex, SReader.getReader(reader));
        } finally {
            termContext();
        }
    }

    public void setNCharacterStream(int parameterIndex, SReader value)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setNCharacterStream(parameterIndex, SReader.getReader(value));
        } finally {
            termContext();
        }
    }

    public boolean isClosed() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.isClosed());
        } finally {
            termContext();
        }
    }

    public void setClob(int parameterIndex, SReader reader) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setClob(parameterIndex, SReader.getReader(reader));
        } finally {
            termContext();
        }
    }

    public void setPoolable(boolean poolable) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setPoolable(poolable);
        } finally {
            termContext();
        }
    }

    public boolean isPoolable() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(preparedStatement.isPoolable());
        } finally {
            termContext();
        }
    }

    public void setBlob(int parameterIndex, SInputStream inputStream)
            throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setBlob(parameterIndex, SInputStream.getInputStream(inputStream));
        } finally {
            termContext();
        }
    }

    public void setNClob(int parameterIndex, SReader reader) throws SQLException, RemoteException {
        initContext();
        try {
            preparedStatement.setNClob(parameterIndex, SReader.getReader(reader));
        } finally {
            termContext();
        }
    }

}
