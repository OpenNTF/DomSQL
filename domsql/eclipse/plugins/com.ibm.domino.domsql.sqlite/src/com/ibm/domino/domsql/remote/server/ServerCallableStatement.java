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
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import com.ibm.domino.domsql.remote.transport.ICallableStatement;
import com.ibm.domino.domsql.remote.transport.IParameterMetaData;
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
import com.ibm.domino.domsql.remote.transport.SerializableObject;
import com.ibm.domino.domsql.sqlite.driver.jdbc.DomSQLCallableStatement;

/**
 *
 */
public class ServerCallableStatement extends ServerStatement implements
        ICallableStatement {
    
	private static final long serialVersionUID = 1L;
	
    private DomSQLCallableStatement callableStatement;
    
    public ServerCallableStatement(ServerConnection connection, DomSQLCallableStatement callableStatement) throws RemoteException {
        super(connection,callableStatement);
        this.callableStatement = callableStatement;
    }
        
    public DomSQLCallableStatement getNative() {
    	return callableStatement;
    }

    public void unreferenced() {
    	callableStatement = null;
    }

    
    // ===========================================================
    // Delegation
    // ===========================================================
    
    public void addBatch() throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.addBatch();
        } finally {
            termContext();
        }
    }

    public void clearParameters() throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.clearParameters();
        } finally {
            termContext();
        }
    }

    public boolean execute() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.execute());
        } finally {
            termContext();
        }
    }

    public SArray getArray(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SArray.create(callableStatement.getArray(parameterIndex)));
        } finally {
            termContext();
        }
    }

    public void addBatch(String sql) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.addBatch(sql);
        } finally {
            termContext();
        }
    }

    public SArray getArray(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SArray.create(callableStatement.getArray(parameterName)));
        } finally {
            termContext();
        }
    }

    public IResultSet executeQuery() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.executeQuery());
        } finally {
            termContext();
        }
    }

    public void cancel() throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.cancel();
        } finally {
            termContext();
        }
    }

    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getBigDecimal(parameterIndex));
        } finally {
            termContext();
        }
    }

    public int executeUpdate() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.executeUpdate());
        } finally {
            termContext();
        }
    }

    public void clearBatch() throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.clearBatch();
        } finally {
            termContext();
        }
    }

    public IResultSetMetaData getMetaData() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getMetaData());
        } finally {
            termContext();
        }
    }

    public void clearWarnings() throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.clearWarnings();
        } finally {
            termContext();
        }
    }

    public BigDecimal getBigDecimal(int parameterIndex, int scale)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getBigDecimal(parameterIndex, scale));
        } finally {
            termContext();
        }
    }

    public void close() throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.close();
        } finally {
            termContext();
        }
    }

    public IParameterMetaData getParameterMetaData() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getParameterMetaData());
        } finally {
            termContext();
        }
    }

    public boolean execute(String sql) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.execute(sql));
        } finally {
            termContext();
        }
    }

    public BigDecimal getBigDecimal(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getBigDecimal(parameterName));
        } finally {
            termContext();
        }
    }

    public void setArray(int parameterIndex, SArray theArray)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setArray(parameterIndex, theArray);
        } finally {
            termContext();
        }
    }

    public boolean execute(String sql, int autoGeneratedKeys)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.execute(sql, autoGeneratedKeys));
        } finally {
            termContext();
        }
    }

    public SBlob getBlob(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SBlob.create(callableStatement.getBlob(parameterIndex)));
        } finally {
            termContext();
        }
    }

    public void setAsciiStream(int parameterIndex, SInputStream theInputStream,
            int length) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setAsciiStream(parameterIndex, SInputStream.getInputStream(theInputStream),
                    length);
        } finally {
            termContext();
        }
    }

    public SBlob getBlob(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SBlob.create(callableStatement.getBlob(parameterName)));
        } finally {
            termContext();
        }
    }

    public boolean getBoolean(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getBoolean(parameterIndex));
        } finally {
            termContext();
        }
    }

    public void setBigDecimal(int parameterIndex, BigDecimal theBigDecimal)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setBigDecimal(parameterIndex, theBigDecimal);
        } finally {
            termContext();
        }
    }

    public boolean execute(String sql, int[] columnIndexes) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.execute(sql, columnIndexes));
        } finally {
            termContext();
        }
    }

    public boolean getBoolean(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getBoolean(parameterName));
        } finally {
            termContext();
        }
    }

    public void setBinaryStream(int parameterIndex, SInputStream theInputStream,
            int length) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setBinaryStream(parameterIndex, SInputStream.getInputStream(theInputStream),
                    length);
        } finally {
            termContext();
        }
    }

    public byte getByte(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getByte(parameterIndex));
        } finally {
            termContext();
        }
    }

    public boolean execute(String sql, String[] columnNames)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.execute(sql, columnNames));
        } finally {
            termContext();
        }
    }

    public byte getByte(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getByte(parameterName));
        } finally {
            termContext();
        }
    }

    public void setBlob(int parameterIndex, SBlob theBlob) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setBlob(parameterIndex, theBlob);
        } finally {
            termContext();
        }
    }

    public byte[] getBytes(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getBytes(parameterIndex));
        } finally {
            termContext();
        }
    }

    public void setBoolean(int parameterIndex, boolean theBoolean)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setBoolean(parameterIndex, theBoolean);
        } finally {
            termContext();
        }
    }

    public int[] executeBatch() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.executeBatch());
        } finally {
            termContext();
        }
    }

    public void setByte(int parameterIndex, byte theByte) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setByte(parameterIndex, theByte);
        } finally {
            termContext();
        }
    }

    public byte[] getBytes(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getBytes(parameterName));
        } finally {
            termContext();
        }
    }

    public void setBytes(int parameterIndex, byte[] theBytes)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setBytes(parameterIndex, theBytes);
        } finally {
            termContext();
        }
    }

    public SClob getClob(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SClob.create(callableStatement.getClob(parameterIndex)));
        } finally {
            termContext();
        }
    }

    public SClob getClob(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SClob.create(callableStatement.getClob(parameterName)));
        } finally {
            termContext();
        }
    }

    public void setCharacterStream(int parameterIndex, SReader reader, int length)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement
                    .setCharacterStream(parameterIndex, SReader.getReader(reader), length);
        } finally {
            termContext();
        }
    }

    public IResultSet executeQuery(String sql) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.executeQuery(sql));
        } finally {
            termContext();
        }
    }

    public Date getDate(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getDate(parameterIndex));
        } finally {
            termContext();
        }
    }

    public void setClob(int parameterIndex, SClob theClob) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setClob(parameterIndex, theClob);
        } finally {
            termContext();
        }
    }

    public Date getDate(int parameterIndex, Calendar cal) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getDate(parameterIndex, cal));
        } finally {
            termContext();
        }
    }

    public int executeUpdate(String sql) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.executeUpdate(sql));
        } finally {
            termContext();
        }
    }

    public void setDate(int parameterIndex, Date theDate) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setDate(parameterIndex, theDate);
        } finally {
            termContext();
        }
    }

    public int executeUpdate(String sql, int autoGeneratedKeys)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.executeUpdate(sql, autoGeneratedKeys));
        } finally {
            termContext();
        }
    }

    public void setDate(int parameterIndex, Date theDate, Calendar cal)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setDate(parameterIndex, theDate, cal);
        } finally {
            termContext();
        }
    }

    public Date getDate(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getDate(parameterName));
        } finally {
            termContext();
        }
    }

    public Date getDate(String parameterName, Calendar cal) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getDate(parameterName, cal));
        } finally {
            termContext();
        }
    }

    public void setDouble(int parameterIndex, double theDouble)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setDouble(parameterIndex, theDouble);
        } finally {
            termContext();
        }
    }

    public int executeUpdate(String sql, int[] columnIndexes)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.executeUpdate(sql, columnIndexes));
        } finally {
            termContext();
        }
    }

    public double getDouble(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getDouble(parameterIndex));
        } finally {
            termContext();
        }
    }

    public void setFloat(int parameterIndex, float theFloat)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setFloat(parameterIndex, theFloat);
        } finally {
            termContext();
        }
    }

    public double getDouble(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getDouble(parameterName));
        } finally {
            termContext();
        }
    }

    public int executeUpdate(String sql, String[] columnNames)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.executeUpdate(sql, columnNames));
        } finally {
            termContext();
        }
    }

    public void setInt(int parameterIndex, int theInt) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setInt(parameterIndex, theInt);
        } finally {
            termContext();
        }
    }

    public void setLong(int parameterIndex, long theLong) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setLong(parameterIndex, theLong);
        } finally {
            termContext();
        }
    }

    public float getFloat(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getFloat(parameterIndex));
        } finally {
            termContext();
        }
    }

    public Connection getConnection() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getConnection());
        } finally {
            termContext();
        }
    }

    public void setNull(int parameterIndex, int sqlType) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setNull(parameterIndex, sqlType);
        } finally {
            termContext();
        }
    }

    public float getFloat(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getFloat(parameterName));
        } finally {
            termContext();
        }
    }

    public int getFetchDirection() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getFetchDirection());
        } finally {
            termContext();
        }
    }

    public int getInt(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getInt(parameterIndex));
        } finally {
            termContext();
        }
    }

    public void setNull(int paramIndex, int sqlType, String typeName)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setNull(paramIndex, sqlType, typeName);
        } finally {
            termContext();
        }
    }

    public int getFetchSize() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getFetchSize());
        } finally {
            termContext();
        }
    }

    public int getInt(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getInt(parameterName));
        } finally {
            termContext();
        }
    }

    public IResultSet getGeneratedKeys() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getGeneratedKeys());
        } finally {
            termContext();
        }
    }

    public long getLong(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getLong(parameterIndex));
        } finally {
            termContext();
        }
    }

    public int getMaxFieldSize() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getMaxFieldSize());
        } finally {
            termContext();
        }
    }

    public void setObject(int parameterIndex, Object theObject)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setObject(parameterIndex, theObject);
        } finally {
            termContext();
        }
    }

    public long getLong(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getLong(parameterName));
        } finally {
            termContext();
        }
    }

    public int getMaxRows() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getMaxRows());
        } finally {
            termContext();
        }
    }

    public Object getObject(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SerializableObject.create(callableStatement.getObject(parameterIndex)));
        } finally {
            termContext();
        }
    }

    public boolean getMoreResults() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getMoreResults());
        } finally {
            termContext();
        }
    }

    public void setObject(int parameterIndex, Object theObject,
            int targetSqlType) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setObject(parameterIndex, theObject,
                    targetSqlType);
        } finally {
            termContext();
        }
    }

    public Object getObject(int parameterIndex, Map<String, Class<?>> map)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SerializableObject.create(callableStatement.getObject(parameterIndex, map)));
        } finally {
            termContext();
        }
    }

    public boolean getMoreResults(int current) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getMoreResults(current));
        } finally {
            termContext();
        }
    }

    public Object getObject(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SerializableObject.create(callableStatement.getObject(parameterName)));
        } finally {
            termContext();
        }
    }

    public void setObject(int parameterIndex, Object theObject,
            int targetSqlType, int scale) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setObject(parameterIndex, theObject,
                    targetSqlType, scale);
        } finally {
            termContext();
        }
    }

    public int getQueryTimeout() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getQueryTimeout());
        } finally {
            termContext();
        }
    }

    public Object getObject(String parameterName, Map<String, Class<?>> map)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SerializableObject.create(callableStatement.getObject(parameterName, map)));
        } finally {
            termContext();
        }
    }

    public IResultSet getResultSet() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getResultSet());
        } finally {
            termContext();
        }
    }

    public void setRef(int parameterIndex, SRef theRef) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setRef(parameterIndex, theRef);
        } finally {
            termContext();
        }
    }

    public SRef getRef(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SRef.create(callableStatement.getRef(parameterIndex)));
        } finally {
            termContext();
        }
    }

    public int getResultSetConcurrency() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getResultSetConcurrency());
        } finally {
            termContext();
        }
    }

    public void setShort(int parameterIndex, short theShort)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setShort(parameterIndex, theShort);
        } finally {
            termContext();
        }
    }

    public int getResultSetHoldability() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getResultSetHoldability());
        } finally {
            termContext();
        }
    }

    public SRef getRef(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SRef.create(callableStatement.getRef(parameterName)));
        } finally {
            termContext();
        }
    }

    public void setString(int parameterIndex, String theString)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setString(parameterIndex, theString);
        } finally {
            termContext();
        }
    }

    public int getResultSetType() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getResultSetType());
        } finally {
            termContext();
        }
    }

    public short getShort(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getShort(parameterIndex));
        } finally {
            termContext();
        }
    }

    public void setTime(int parameterIndex, Time theTime) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setTime(parameterIndex, theTime);
        } finally {
            termContext();
        }
    }

    public int getUpdateCount() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getUpdateCount());
        } finally {
            termContext();
        }
    }

    public short getShort(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getShort(parameterName));
        } finally {
            termContext();
        }
    }

    public void setTime(int parameterIndex, Time theTime, Calendar cal)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setTime(parameterIndex, theTime, cal);
        } finally {
            termContext();
        }
    }

    public SQLWarning getWarnings() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getWarnings());
        } finally {
            termContext();
        }
    }

    public String getString(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getString(parameterIndex));
        } finally {
            termContext();
        }
    }

    public void setCursorName(String name) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setCursorName(name);
        } finally {
            termContext();
        }
    }

    public void setTimestamp(int parameterIndex, Timestamp theTimestamp)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setTimestamp(parameterIndex, theTimestamp);
        } finally {
            termContext();
        }
    }

    public String getString(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getString(parameterName));
        } finally {
            termContext();
        }
    }

    public void setTimestamp(int parameterIndex, Timestamp theTimestamp,
            Calendar cal) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setTimestamp(parameterIndex, theTimestamp, cal);
        } finally {
            termContext();
        }
    }

    public void setEscapeProcessing(boolean enable) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setEscapeProcessing(enable);
        } finally {
            termContext();
        }
    }

    public Time getTime(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getTime(parameterIndex));
        } finally {
            termContext();
        }
    }

    public void setFetchDirection(int direction) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setFetchDirection(direction);
        } finally {
            termContext();
        }
    }

    public void setUnicodeStream(int parameterIndex,
            SInputStream theInputStream, int length) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setUnicodeStream(parameterIndex, SInputStream.getInputStream(theInputStream),
                    length);
        } finally {
            termContext();
        }
    }

    public Time getTime(int parameterIndex, Calendar cal) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getTime(parameterIndex, cal));
        } finally {
            termContext();
        }
    }

    public void setFetchSize(int rows) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setFetchSize(rows);
        } finally {
            termContext();
        }
    }

    public void setURL(int parameterIndex, URL theURL) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setURL(parameterIndex, theURL);
        } finally {
            termContext();
        }
    }

    public Time getTime(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getTime(parameterName));
        } finally {
            termContext();
        }
    }

    public void setMaxFieldSize(int max) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setMaxFieldSize(max);
        } finally {
            termContext();
        }
    }

    public void setRowId(int parameterIndex, SRowId x) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setRowId(parameterIndex, x);
        } finally {
            termContext();
        }
    }

    public Time getTime(String parameterName, Calendar cal) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getTime(parameterName, cal));
        } finally {
            termContext();
        }
    }

    public void setNString(int parameterIndex, String value)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setNString(parameterIndex, value);
        } finally {
            termContext();
        }
    }

    public void setNCharacterStream(int parameterIndex, SReader value,
            long length) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement
                    .setNCharacterStream(parameterIndex, SReader.getReader(value), length);
        } finally {
            termContext();
        }
    }

    public void setNClob(int parameterIndex, SNClob value) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setNClob(parameterIndex, value);
        } finally {
            termContext();
        }
    }

    public void setClob(int parameterIndex, SReader reader, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setClob(parameterIndex, SReader.getReader(reader), length);
        } finally {
            termContext();
        }
    }

    public void setBlob(int parameterIndex, SInputStream inputStream, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setBlob(parameterIndex, SInputStream.getInputStream(inputStream), length);
        } finally {
            termContext();
        }
    }

    public void setMaxRows(int max) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setMaxRows(max);
        } finally {
            termContext();
        }
    }

    public void setNClob(int parameterIndex, SReader reader, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setNClob(parameterIndex, SReader.getReader(reader), length);
        } finally {
            termContext();
        }
    }

    public void setSQLXML(int parameterIndex, SSQLXML xmlObject)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setSQLXML(parameterIndex, xmlObject);
        } finally {
            termContext();
        }
    }

    public void setAsciiStream(int parameterIndex, SInputStream x, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setAsciiStream(parameterIndex, SInputStream.getInputStream(x), length);
        } finally {
            termContext();
        }
    }

    public Timestamp getTimestamp(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getTimestamp(parameterIndex));
        } finally {
            termContext();
        }
    }

    public void setBinaryStream(int parameterIndex, SInputStream x, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setBinaryStream(parameterIndex, SInputStream.getInputStream(x), length);
        } finally {
            termContext();
        }
    }

    public void setQueryTimeout(int seconds) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setQueryTimeout(seconds);
        } finally {
            termContext();
        }
    }

    public void setCharacterStream(int parameterIndex, SReader reader,
            long length) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement
                    .setCharacterStream(parameterIndex, SReader.getReader(reader), length);
        } finally {
            termContext();
        }
    }

    public void setAsciiStream(int parameterIndex, SInputStream x)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setAsciiStream(parameterIndex, SInputStream.getInputStream(x));
        } finally {
            termContext();
        }
    }

    public void setBinaryStream(int parameterIndex, SInputStream x)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setBinaryStream(parameterIndex, SInputStream.getInputStream(x));
        } finally {
            termContext();
        }
    }

    public void setCharacterStream(int parameterIndex, SReader reader)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setCharacterStream(parameterIndex, SReader.getReader(reader));
        } finally {
            termContext();
        }
    }

    public Timestamp getTimestamp(int parameterIndex, Calendar cal)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getTimestamp(parameterIndex, cal));
        } finally {
            termContext();
        }
    }

    public void setNCharacterStream(int parameterIndex, SReader value)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setNCharacterStream(parameterIndex, SReader.getReader(value));
        } finally {
            termContext();
        }
    }

    public boolean isClosed() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.isClosed());
        } finally {
            termContext();
        }
    }

    public void setClob(int parameterIndex, SReader reader) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setClob(parameterIndex, SReader.getReader(reader));
        } finally {
            termContext();
        }
    }

    public void setPoolable(boolean poolable) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setPoolable(poolable);
        } finally {
            termContext();
        }
    }

    public boolean isPoolable() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.isPoolable());
        } finally {
            termContext();
        }
    }

    public void setBlob(int parameterIndex, SInputStream inputStream)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setBlob(parameterIndex, SInputStream.getInputStream(inputStream));
        } finally {
            termContext();
        }
    }

    public void setNClob(int parameterIndex, SReader reader) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setNClob(parameterIndex, SReader.getReader(reader));
        } finally {
            termContext();
        }
    }

    public Timestamp getTimestamp(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getTimestamp(parameterName));
        } finally {
            termContext();
        }
    }

    public Timestamp getTimestamp(String parameterName, Calendar cal)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getTimestamp(parameterName, cal));
        } finally {
            termContext();
        }
    }

    public URL getURL(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getURL(parameterIndex));
        } finally {
            termContext();
        }
    }

    public URL getURL(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getURL(parameterName));
        } finally {
            termContext();
        }
    }

    public void registerOutParameter(int parameterIndex, int sqlType)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.registerOutParameter(parameterIndex, sqlType);
        } finally {
            termContext();
        }
    }

    public void registerOutParameter(int parameterIndex, int sqlType, int scale)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.registerOutParameter(parameterIndex, sqlType,
                    scale);
        } finally {
            termContext();
        }
    }

    public void registerOutParameter(int paramIndex, int sqlType,
            String typeName) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.registerOutParameter(paramIndex, sqlType,
                    typeName);
        } finally {
            termContext();
        }
    }

    public void registerOutParameter(String parameterName, int sqlType)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.registerOutParameter(parameterName, sqlType);
        } finally {
            termContext();
        }
    }

    public void registerOutParameter(String parameterName, int sqlType,
            int scale) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.registerOutParameter(parameterName, sqlType,
                    scale);
        } finally {
            termContext();
        }
    }

    public void registerOutParameter(String parameterName, int sqlType,
            String typeName) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.registerOutParameter(parameterName, sqlType,
                    typeName);
        } finally {
            termContext();
        }
    }

    public void setAsciiStream(String parameterName,
            SInputStream theInputStream, int length) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setAsciiStream(parameterName, SInputStream.getInputStream(theInputStream),
                    length);
        } finally {
            termContext();
        }
    }

    public void setBigDecimal(String parameterName, BigDecimal theBigDecimal)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setBigDecimal(parameterName, theBigDecimal);
        } finally {
            termContext();
        }
    }

    public void setBinaryStream(String parameterName,
            SInputStream theInputStream, int length) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setBinaryStream(parameterName, SInputStream.getInputStream(theInputStream),
                    length);
        } finally {
            termContext();
        }
    }

    public void setBoolean(String parameterName, boolean theBoolean)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setBoolean(parameterName, theBoolean);
        } finally {
            termContext();
        }
    }

    public void setByte(String parameterName, byte theByte) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setByte(parameterName, theByte);
        } finally {
            termContext();
        }
    }

    public void setBytes(String parameterName, byte[] theBytes)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setBytes(parameterName, theBytes);
        } finally {
            termContext();
        }
    }

    public void setCharacterStream(String parameterName, SReader reader,
            int length) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setCharacterStream(parameterName, SReader.getReader(reader), length);
        } finally {
            termContext();
        }
    }

    public void setDate(String parameterName, Date theDate) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setDate(parameterName, theDate);
        } finally {
            termContext();
        }
    }

    public void setDate(String parameterName, Date theDate, Calendar cal)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setDate(parameterName, theDate, cal);
        } finally {
            termContext();
        }
    }

    public void setDouble(String parameterName, double theDouble)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setDouble(parameterName, theDouble);
        } finally {
            termContext();
        }
    }

    public void setFloat(String parameterName, float theFloat)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setFloat(parameterName, theFloat);
        } finally {
            termContext();
        }
    }

    public void setInt(String parameterName, int theInt) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setInt(parameterName, theInt);
        } finally {
            termContext();
        }
    }

    public void setLong(String parameterName, long theLong) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setLong(parameterName, theLong);
        } finally {
            termContext();
        }
    }

    public void setNull(String parameterName, int sqlType) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setNull(parameterName, sqlType);
        } finally {
            termContext();
        }
    }

    public void setNull(String parameterName, int sqlType, String typeName)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setNull(parameterName, sqlType, typeName);
        } finally {
            termContext();
        }
    }

    public void setObject(String parameterName, Object theObject)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setObject(parameterName, theObject);
        } finally {
            termContext();
        }
    }

    public void setObject(String parameterName, Object theObject,
            int targetSqlType) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement
                    .setObject(parameterName, theObject, targetSqlType);
        } finally {
            termContext();
        }
    }

    public void setObject(String parameterName, Object theObject,
            int targetSqlType, int scale) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setObject(parameterName, theObject,
                    targetSqlType, scale);
        } finally {
            termContext();
        }
    }

    public void setShort(String parameterName, short theShort)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setShort(parameterName, theShort);
        } finally {
            termContext();
        }
    }

    public void setString(String parameterName, String theString)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setString(parameterName, theString);
        } finally {
            termContext();
        }
    }

    public void setTime(String parameterName, Time theTime) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setTime(parameterName, theTime);
        } finally {
            termContext();
        }
    }

    public void setTime(String parameterName, Time theTime, Calendar cal)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setTime(parameterName, theTime, cal);
        } finally {
            termContext();
        }
    }

    public void setTimestamp(String parameterName, Timestamp theTimestamp)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setTimestamp(parameterName, theTimestamp);
        } finally {
            termContext();
        }
    }

    public void setTimestamp(String parameterName, Timestamp theTimestamp,
            Calendar cal) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setTimestamp(parameterName, theTimestamp, cal);
        } finally {
            termContext();
        }
    }

    public void setURL(String parameterName, URL theURL) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setURL(parameterName, theURL);
        } finally {
            termContext();
        }
    }

    public boolean wasNull() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.wasNull());
        } finally {
            termContext();
        }
    }

    public SRowId getRowId(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SRowId.create(callableStatement.getRowId(parameterIndex)));
        } finally {
            termContext();
        }
    }

    public SRowId getRowId(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SRowId.create(callableStatement.getRowId(parameterName)));
        } finally {
            termContext();
        }
    }

    public void setRowId(String parameterName, SRowId x) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setRowId(parameterName, x);
        } finally {
            termContext();
        }
    }

    public void setNString(String parameterName, String value)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setNString(parameterName, value);
        } finally {
            termContext();
        }
    }

    public void setNCharacterStream(String parameterName, SReader value,
            long length) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setNCharacterStream(parameterName, SReader.getReader(value), length);
        } finally {
            termContext();
        }
    }

    public void setNClob(String parameterName, SNClob value) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setNClob(parameterName, value);
        } finally {
            termContext();
        }
    }

    public void setClob(String parameterName, SReader reader, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setClob(parameterName, SReader.getReader(reader), length);
        } finally {
            termContext();
        }
    }

    public void setBlob(String parameterName, SInputStream inputStream,
            long length) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setBlob(parameterName, SInputStream.getInputStream(inputStream), length);
        } finally {
            termContext();
        }
    }

    public void setNClob(String parameterName, SReader reader, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setNClob(parameterName, SReader.getReader(reader), length);
        } finally {
            termContext();
        }
    }

    public SNClob getNClob(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return SNClob.create(callableStatement.getNClob(parameterIndex));
        } finally {
            termContext();
        }
    }

    public SNClob getNClob(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return SNClob.create(callableStatement.getNClob(parameterName));
        } finally {
            termContext();
        }
    }

    public void setSQLXML(String parameterName, SSQLXML xmlObject)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setSQLXML(parameterName, xmlObject);
        } finally {
            termContext();
        }
    }

    public SSQLXML getSQLXML(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SSQLXML.create(callableStatement.getSQLXML(parameterIndex)));
        } finally {
            termContext();
        }
    }

    public SSQLXML getSQLXML(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SSQLXML.create(callableStatement.getSQLXML(parameterName)));
        } finally {
            termContext();
        }
    }

    public String getNString(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getNString(parameterIndex));
        } finally {
            termContext();
        }
    }

    public String getNString(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(callableStatement.getNString(parameterName));
        } finally {
            termContext();
        }
    }

    public SReader getNCharacterStream(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SReader.create(callableStatement.getNCharacterStream(parameterIndex)));
        } finally {
            termContext();
        }
    }

    public SReader getNCharacterStream(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SReader.create(callableStatement.getNCharacterStream(parameterName)));
        } finally {
            termContext();
        }
    }

    public SReader getCharacterStream(int parameterIndex) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SReader.create(callableStatement.getCharacterStream(parameterIndex)));
        } finally {
            termContext();
        }
    }

    public SReader getCharacterStream(String parameterName) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(SReader.create(callableStatement.getCharacterStream(parameterName)));
        } finally {
            termContext();
        }
    }

    public void setBlob(String parameterName, SBlob x) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setBlob(parameterName, x);
        } finally {
            termContext();
        }
    }

    public void setClob(String parameterName, SClob x) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setClob(parameterName, x);
        } finally {
            termContext();
        }
    }

    public void setAsciiStream(String parameterName, SInputStream x, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setAsciiStream(parameterName, SInputStream.getInputStream(x), length);
        } finally {
            termContext();
        }
    }

    public void setBinaryStream(String parameterName, SInputStream x, long length)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setBinaryStream(parameterName, SInputStream.getInputStream(x), length);
        } finally {
            termContext();
        }
    }

    public void setCharacterStream(String parameterName, SReader reader,
            long length) throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setCharacterStream(parameterName, SReader.getReader(reader), length);
        } finally {
            termContext();
        }
    }

    public void setAsciiStream(String parameterName, SInputStream x)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setAsciiStream(parameterName, SInputStream.getInputStream(x));
        } finally {
            termContext();
        }
    }

    public void setBinaryStream(String parameterName, SInputStream x)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setBinaryStream(parameterName, SInputStream.getInputStream(x));
        } finally {
            termContext();
        }
    }

    public void setCharacterStream(String parameterName, SReader reader)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setCharacterStream(parameterName, SReader.getReader(reader));
        } finally {
            termContext();
        }
    }

    public void setNCharacterStream(String parameterName, SReader value)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setNCharacterStream(parameterName, SReader.getReader(value));
        } finally {
            termContext();
        }
    }

    public void setClob(String parameterName, SReader reader)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setClob(parameterName, SReader.getReader(reader));
        } finally {
            termContext();
        }
    }

    public void setBlob(String parameterName, SInputStream inputStream)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setBlob(parameterName, SInputStream.getInputStream(inputStream));
        } finally {
            termContext();
        }
    }

    public void setNClob(String parameterName, SReader reader)
            throws SQLException, RemoteException {
        initContext();
        try {
            callableStatement.setNClob(parameterName, SReader.getReader(reader));
        } finally {
            termContext();
        }
    }

}
