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
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
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
import com.ibm.domino.domsql.remote.transport.ServerOptions;

/**
 *
 */
public class ClientResultSet extends ClientObject implements ResultSet {

	
	private ClientStatement statement;
    private IResultSet resultSet;

    private Object[] oneRowObjects;
    private int currentCol;
    
    public ClientResultSet(ClientStatement statement, IResultSet resultSet) {
    	super(resultSet);
    	this.statement = statement;
    	this.resultSet = resultSet;
    }
    public ClientResultSet(ClientDatabaseMetaData metadata, IResultSet resultSet) {
    	super(resultSet);
    	this.resultSet = resultSet;
    }
    public ClientResultSet(ClientResultSetMetaData metadata, IResultSet resultSet) {
    	super(resultSet);
    	this.resultSet = resultSet;
    }
    
    public IResultSet getRemoteObject() {
    	return resultSet;
    }

	public boolean absolute(int row) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			return resultSet.absolute(row);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void afterLast() throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.afterLast();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void beforeFirst() throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.beforeFirst();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void cancelRowUpdates() throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.cancelRowUpdates();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void clearWarnings() throws SQLException {
		try {
			resultSet.clearWarnings();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void close() throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.close();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void deleteRow() throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.deleteRow();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int findColumn(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return getMetaData().findColumn(columnName);
			}
			return resultSet.findColumn(columnName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean first() throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			return resultSet.first();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Array getArray(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toArray(loadOneObject(columnIndex));
			}
			return resultSet.getArray(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}
	public Array getArray(String colName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toArray(loadOneObject(colName));
			}
			return resultSet.getArray(colName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toInputStream(loadOneObject(columnIndex));
			}
			return SInputStream.getInputStream(resultSet.getAsciiStream(columnIndex));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}
	public InputStream getAsciiStream(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toInputStream(loadOneObject(columnName));
			}
			return SInputStream.getInputStream(resultSet.getAsciiStream(columnName));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toBigDecimal(loadOneObject(columnIndex));
			}
			return resultSet.getBigDecimal(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toBigDecimal(loadOneObject(columnIndex),scale);
			}
			return resultSet.getBigDecimal(columnIndex, scale);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}
	public BigDecimal getBigDecimal(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toBigDecimal(loadOneObject(columnName));
			}
			return resultSet.getBigDecimal(columnName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}
	public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toBigDecimal(loadOneObject(columnName),scale);
			}
			return resultSet.getBigDecimal(columnName, scale);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toInputStream(loadOneObject(columnIndex));
			}
			return SInputStream.getInputStream(resultSet.getBinaryStream(columnIndex));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public InputStream getBinaryStream(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toInputStream(loadOneObject(columnName));
			}
			return SInputStream.getInputStream(resultSet.getBinaryStream(columnName));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Blob getBlob(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toBlob(loadOneObject(columnIndex));
			}
			return resultSet.getBlob(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Blob getBlob(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toBlob(loadOneObject(columnName));
			}
			return resultSet.getBlob(columnName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean getBoolean(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toBoolean(loadOneObject(columnIndex));
			}
			return resultSet.getBoolean(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean getBoolean(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toBoolean(loadOneObject(columnName));
			}
			return resultSet.getBoolean(columnName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public byte getByte(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toByte(loadOneObject(columnIndex));
			}
			return resultSet.getByte(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public byte getByte(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toByte(loadOneObject(columnName));
			}
			return resultSet.getByte(columnName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public byte[] getBytes(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toByteArray(loadOneObject(columnIndex));
			}
			return resultSet.getBytes(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public byte[] getBytes(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toByteArray(loadOneObject(columnName));
			}
			return resultSet.getBytes(columnName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Reader getCharacterStream(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toReader(loadOneObject(columnIndex));
			}
			return SReader.getReader(resultSet.getCharacterStream(columnIndex));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Reader getCharacterStream(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toReader(loadOneObject(columnName));
			}
			return SReader.getReader(resultSet.getCharacterStream(columnName));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Clob getClob(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toClob(loadOneObject(columnIndex));
			}
			return resultSet.getClob(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Clob getClob(String colName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toClob(loadOneObject(colName));
			}
			return resultSet.getClob(colName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	private Integer _concurrency;
	public int getConcurrency() throws SQLException {
		if(_concurrency==null) {
			try {
				_concurrency = resultSet.getConcurrency();
			} catch(RemoteException t) {
				throw newSQLException(t);
			}
		}
		return _concurrency;
	}

	private String _cursorName;
	public String getCursorName() throws SQLException {
		if(_cursorName==null) {
			try {
				_cursorName = resultSet.getCursorName();
			} catch(RemoteException t) {
				throw newSQLException(t);
			}
		}
		return _cursorName;
	}

	public Date getDate(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toDate(loadOneObject(columnIndex));
			}
			return resultSet.getDate(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toDate(loadOneObject(columnIndex),cal);
			}
			return resultSet.getDate(columnIndex, cal);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Date getDate(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toDate(loadOneObject(columnName));
			}
			return resultSet.getDate(columnName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Date getDate(String columnName, Calendar cal) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toDate(loadOneObject(columnName),cal);
			}
			return resultSet.getDate(columnName, cal);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public double getDouble(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toDouble(loadOneObject(columnIndex));
			}
			return resultSet.getDouble(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public double getDouble(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toDouble(loadOneObject(columnName));
			}
			return resultSet.getDouble(columnName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	private Integer _fetchDirection;
	public int getFetchDirection() throws SQLException {
		if(_fetchDirection==null) {
			try {
				_fetchDirection = resultSet.getFetchDirection();
			} catch(RemoteException t) {
				throw newSQLException(t);
			}
		}
		return _fetchDirection;
	}

	private Integer _fetchSize;
	public int getFetchSize() throws SQLException {
		if(_fetchSize==null) {
			try {
				return resultSet.getFetchSize();
			} catch(RemoteException t) {
				throw newSQLException(t);
			}
		}
		return _fetchSize;
	}

	public float getFloat(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toFloat(loadOneObject(columnIndex));
			}
			return resultSet.getFloat(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public float getFloat(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toFloat(loadOneObject(columnName));
			}
			return resultSet.getFloat(columnName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getInt(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toInt(loadOneObject(columnIndex));
			}
			return resultSet.getInt(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getInt(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toInt(loadOneObject(columnName));
			}
			return resultSet.getInt(columnName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public long getLong(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toLong(loadOneObject(columnIndex));
			}
			return resultSet.getLong(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public long getLong(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toLong(loadOneObject(columnName));
			}
			return resultSet.getLong(columnName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	private ClientResultSetMetaData _metaData;
	public ClientResultSetMetaData getMetaData() throws SQLException {
		if(_metaData==null) {
			try {
				_metaData = new ClientResultSetMetaData(resultSet.getMetaData());
			} catch(RemoteException t) {
				throw newSQLException(t);
			}
		}
		return _metaData;
	}

	public Object getObject(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return loadOneObject(columnIndex);
			}
			return resultSet.getObject(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				// TODO: Ignore map???
				return loadOneObject(columnIndex);
			}
			return resultSet.getObject(columnIndex, map);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Object getObject(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return loadOneObject(columnName);
			}
			return resultSet.getObject(columnName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Object getObject(String columnName, Map<String, Class<?>> map) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				// TODO: Ignore map???
				return loadOneObject(columnName);
			}
			return resultSet.getObject(columnName, map);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Ref getRef(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toRef(loadOneObject(columnIndex));
			}
			return resultSet.getRef(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Ref getRef(String colName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toRef(loadOneObject(colName));
			}
			return resultSet.getRef(colName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getRow() throws SQLException {
		try {
			return resultSet.getRow();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public short getShort(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toShort(loadOneObject(columnIndex));
			}
			return resultSet.getShort(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public short getShort(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toShort(loadOneObject(columnName));
			}
			return resultSet.getShort(columnName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Statement getStatement() throws SQLException {
		return statement;
	}

	public String getString(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toString(loadOneObject(columnIndex));
			}
			return resultSet.getString(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getString(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toString(loadOneObject(columnName));
			}
			return resultSet.getString(columnName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Time getTime(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toTime(loadOneObject(columnIndex));
			}
			return resultSet.getTime(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toTime(loadOneObject(columnIndex),cal);
			}
			return resultSet.getTime(columnIndex, cal);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Time getTime(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toTime(loadOneObject(columnName));
			}
			return resultSet.getTime(columnName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Time getTime(String columnName, Calendar cal) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toTime(loadOneObject(columnName),cal);
			}
			return resultSet.getTime(columnName, cal);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toTimestamp(loadOneObject(columnIndex));
			}
			return resultSet.getTimestamp(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toTimestamp(loadOneObject(columnIndex),cal);
			}
			return resultSet.getTimestamp(columnIndex, cal);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Timestamp getTimestamp(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toTimestamp(loadOneObject(columnName));
			}
			return resultSet.getTimestamp(columnName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toTimestamp(loadOneObject(columnName),cal);
			}
			return resultSet.getTimestamp(columnName, cal);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	private Integer _type;
	public int getType() throws SQLException {
		if(_type==null) {
			try {
				_type = resultSet.getType();
			} catch(RemoteException t) {
				throw newSQLException(t);
			}
		}
		return _type;
	}

	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toInputStream(loadOneObject(columnIndex));
			}
			return SInputStream.getInputStream(resultSet.getUnicodeStream(columnIndex));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public InputStream getUnicodeStream(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toInputStream(loadOneObject(columnName));
			}
			return SInputStream.getInputStream(resultSet.getUnicodeStream(columnName));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public URL getURL(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toURL(loadOneObject(columnIndex));
			}
			return resultSet.getURL(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public URL getURL(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toURL(loadOneObject(columnName));
			}
			return resultSet.getURL(columnName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public SQLWarning getWarnings() throws SQLException {
		try {
			return resultSet.getWarnings();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void insertRow() throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.insertRow();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean isAfterLast() throws SQLException {
		try {
			return resultSet.isAfterLast();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean isBeforeFirst() throws SQLException {
		try {
			return resultSet.isBeforeFirst();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean isFirst() throws SQLException {
		try {
			return resultSet.isFirst();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean isLast() throws SQLException {
		try {
			return resultSet.isLast();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean last() throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			return resultSet.last();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void moveToCurrentRow() throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.moveToCurrentRow();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void moveToInsertRow() throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.moveToInsertRow();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean next() throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				if(ServerOptions.options.OPTIMIZATION_NEXT_DATA) {
					Object[] objects = resultSet._next();
					if(objects!=null) {
						loadOneRow(objects);
						return true;
					}
					return false;
				}
				_clearOneRow();
			}
			return resultSet.next();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean previous() throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			return resultSet.previous();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void refreshRow() throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.refreshRow();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean relative(int rows) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			return resultSet.relative(rows);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean rowDeleted() throws SQLException {
		try {
			return resultSet.rowDeleted();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean rowInserted() throws SQLException {
		try {
			return resultSet.rowInserted();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean rowUpdated() throws SQLException {
		try {
			return resultSet.rowUpdated();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setFetchDirection(int direction) throws SQLException {
		try {
			_fetchDirection = direction;
			resultSet.setFetchDirection(direction);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void setFetchSize(int rows) throws SQLException {
		try {
			_fetchSize = rows;
			resultSet.setFetchSize(rows);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateArray(int columnIndex, Array x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateArray(columnIndex, SArray.create(x));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateArray(String columnName, Array x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateArray(columnName, SArray.create(x));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateAsciiStream(columnIndex, SInputStream.create(x), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateAsciiStream(columnName, SInputStream.create(x), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateBigDecimal(columnIndex, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateBigDecimal(columnName, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateBinaryStream(columnIndex, SInputStream.create(x), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateBinaryStream(columnName, SInputStream.create(x), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateBlob(columnIndex, SBlob.create(x));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateBlob(String columnName, Blob x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateBlob(columnName, SBlob.create(x));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateBoolean(columnIndex, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateBoolean(String columnName, boolean x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateBoolean(columnName, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateByte(int columnIndex, byte x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateByte(columnIndex, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateByte(String columnName, byte x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateByte(columnName, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateBytes(columnIndex, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateBytes(String columnName, byte[] x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateBytes(columnName, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateCharacterStream(columnIndex, SReader.create(x), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateCharacterStream(columnName, SReader.create(reader), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateClob(int columnIndex, Clob x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateClob(columnIndex, SClob.create(x));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateClob(String columnName, Clob x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateClob(columnName, SClob.create(x));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateDate(int columnIndex, Date x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateDate(columnIndex, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateDate(String columnName, Date x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateDate(columnName, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateDouble(int columnIndex, double x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateDouble(columnIndex, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateDouble(String columnName, double x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateDouble(columnName, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateFloat(int columnIndex, float x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateFloat(columnIndex, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateFloat(String columnName, float x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateFloat(columnName, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateInt(int columnIndex, int x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateInt(columnIndex, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateInt(String columnName, int x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateInt(columnName, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateLong(int columnIndex, long x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateLong(columnIndex, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateLong(String columnName, long x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateLong(columnName, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateNull(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateNull(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateNull(String columnName) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateNull(columnName);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateObject(int columnIndex, Object x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateObject(columnIndex, SerializableObject.create(x));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateObject(columnIndex, SerializableObject.create(x), scale);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateObject(String columnName, Object x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateObject(columnName, SerializableObject.create(x));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateObject(String columnName, Object x, int scale) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateObject(columnName, SerializableObject.create(x), scale);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateRef(int columnIndex, Ref x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateRef(columnIndex, SRef.create(x));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateRef(String columnName, Ref x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateRef(columnName, SRef.create(x));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateRow() throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateRow();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateShort(int columnIndex, short x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateShort(columnIndex, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateShort(String columnName, short x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateShort(columnName, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateString(int columnIndex, String x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateString(columnIndex, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateString(String columnName, String x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateString(columnName, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateTime(int columnIndex, Time x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateTime(columnIndex, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateTime(String columnName, Time x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateTime(columnName, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateTimestamp(columnIndex, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateTimestamp(columnName, x);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean wasNull() throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				if(oneRowObjects!=null && currentCol>0) {
					// JDBC has 1 based indexes
					return oneRowObjects[currentCol-1]==null;
				}
			}
			return resultSet.wasNull();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public RowId getRowId(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toRowId(loadOneObject(columnIndex));
			}
			return resultSet.getRowId(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public RowId getRowId(String columnLabel) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toRowId(loadOneObject(columnLabel));
			}
			return resultSet.getRowId(columnLabel);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateRowId(columnIndex, SRowId.create(x));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateRowId(columnLabel, SRowId.create(x));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	private Integer _holdability;
	public int getHoldability() throws SQLException {
		if(_holdability==null) {
			try {
				return resultSet.getHoldability();
			} catch(RemoteException t) {
				throw newSQLException(t);
			}
		}
		return _holdability;
	}

	private boolean _closed;
	public boolean isClosed() throws SQLException {
		// Once closed, it cannot change back....
		if(!_closed) {
			try {
				_closed = resultSet.isClosed();
			} catch(RemoteException t) {
				throw newSQLException(t);
			}
		}
		return _closed;
	}

	public void updateNString(int columnIndex, String nString) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateNString(columnIndex, nString);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateNString(String columnLabel, String nString) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateNString(columnLabel, nString);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateNClob(columnIndex, SNClob.create(nClob));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateNClob(columnLabel, SNClob.create(nClob));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public NClob getNClob(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toNClob(loadOneObject(columnIndex));
			}
			return resultSet.getNClob(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public NClob getNClob(String columnLabel) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toNClob(loadOneObject(columnLabel));
			}
			return resultSet.getNClob(columnLabel);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toSQLXML(loadOneObject(columnIndex));
			}
			return resultSet.getSQLXML(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toSQLXML(loadOneObject(columnLabel));
			}
			return resultSet.getSQLXML(columnLabel);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateSQLXML(columnIndex, SSQLXML.create(xmlObject));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateSQLXML(columnLabel, SSQLXML.create(xmlObject));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getNString(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toString(loadOneObject(columnIndex));
			}
			return resultSet.getNString(columnIndex);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getNString(String columnLabel) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toString(loadOneObject(columnLabel));
			}
			return resultSet.getNString(columnLabel);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toReader(loadOneObject(columnIndex));
			}
			return SReader.getReader(resultSet.getNCharacterStream(columnIndex));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				return DataConverter.toReader(loadOneObject(columnLabel));
			}
			return SReader.getReader(resultSet.getNCharacterStream(columnLabel));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateNCharacterStream(columnIndex, SReader.create(x), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateNCharacterStream(columnLabel, SReader.create(reader), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateAsciiStream(columnIndex, SInputStream.create(x), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateBinaryStream(columnIndex, SInputStream.create(x), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateCharacterStream(columnIndex, SReader.create(x), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateAsciiStream(columnLabel, SInputStream.create(x), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateBinaryStream(columnLabel, SInputStream.create(x), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateCharacterStream(columnLabel, SReader.create(reader), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateBlob(columnIndex, SInputStream.create(inputStream), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateBlob(columnLabel, SInputStream.create(inputStream), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateClob(columnIndex, SReader.create(reader), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateClob(columnLabel, SReader.create(reader), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateNClob(columnIndex, SReader.create(reader), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateNClob(columnLabel, SReader.create(reader), length);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateNCharacterStream(columnIndex, SReader.create(x));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateNCharacterStream(columnLabel, SReader.create(reader));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateAsciiStream(columnIndex, SInputStream.create(x));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			 resultSet.updateBinaryStream(columnIndex, SInputStream.create(x));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateCharacterStream(columnIndex, SReader.create(x));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateAsciiStream(columnLabel, SInputStream.create(x));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateBinaryStream(columnLabel, SInputStream.create(x));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateCharacterStream(columnLabel, SReader.create(reader));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateBlob(columnIndex, SInputStream.create(inputStream));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateBlob(columnLabel, SInputStream.create(inputStream));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateClob(columnIndex, SReader.create(reader));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateClob(String columnLabel, Reader reader) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateClob(columnLabel, SReader.create(reader));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateNClob(columnIndex, SReader.create(reader));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public void updateNClob(String columnLabel, Reader reader) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_ONE_ROW) {
				_clearOneRow();
			}
			resultSet.updateNClob(columnLabel, SReader.create(reader));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	
	//
	// One row optimization
	//
	private void _clearOneRow() throws RemoteException, SQLException {
		oneRowObjects = null;
	}
	private boolean loadOneRow() throws RemoteException, SQLException {
		if(oneRowObjects==null) {
			oneRowObjects = resultSet._getObjects();
			return oneRowObjects!=null;
		}
		return true;
	}
	private void loadOneRow(Object[] objects) throws RemoteException, SQLException {
		oneRowObjects = objects;
	}
	private Object loadOneObject(int idx) throws RemoteException, SQLException {
		if(loadOneRow()) {
			idx--; // Object[] is zero based!
			if(idx>=0 && idx<oneRowObjects.length) {
				currentCol = idx+1; // Save it for wasNull()
				return oneRowObjects[idx];
			}
		}
		return null;
	}
	private Object loadOneObject(String columnName) throws RemoteException, SQLException {
		return loadOneObject(findColumn(columnName));
	}
}
