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
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import com.ibm.commons.util.NotImplementedException;
import com.ibm.domino.domsql.sqlite.DomSQLException;
import com.ibm.domino.domsql.sqlite.driver.SQLite;
import com.ibm.domino.domsql.sqlite.driver.jni.DomSQL;

public class DomSQLPreparedStatement extends DomSQLStatement implements PreparedStatement {

    private String sql;
    private int columnCount;
    private Object[] parameters;

    DomSQLPreparedStatement(DomSQLConnection conn, String sql) throws SQLException {
        super(conn);
        this.sql = sql;
        prepare(sql);
        this.columnCount = SQLite.columnCount(getHstmt());
        int paramCount = SQLite.bindParameterCount(getHstmt());
        if(paramCount>0) {
            parameters = new Object[paramCount];
        }
    }

    public void clearParameters() throws SQLException {
        checkStatementOpen();
        SQLite.reset(getHstmt());
        clearBatch();
    }
    
    public Object[] getParameters() {
    	return parameters;
    }

    public boolean execute() throws SQLException {
        checkStatementOpen();
        closeResultSet();
        resetStatement();
        boolean hasRows = executeParams(parameters);
        if(columnCount != 0) {
            initResultSet(!hasRows);
            return true;
        }
        return false;
    }

    public DomSQLResultSet executeQuery() throws SQLException {
        checkStatementOpen();
        if (columnCount == 0) {
            throw DomSQLException.create(null,"executeQuery() must execute a SQL query that returns a result");
        }
        closeResultSet();
        resetStatement();
        boolean hasRows = executeParams(parameters);
        return initResultSet(!hasRows);
    }

    public int executeUpdate() throws SQLException {
        checkStatementOpen();
        if (columnCount != 0) {
            throw DomSQLException.create(null,"executeUpdate() must *not* execute a SQL query that returns a result");
        }
        closeResultSet();
        resetStatement();
        return executeUpdateParams(parameters);
    }


    public int getUpdateCount() throws SQLException {
        if(getHstmt()==0) {
            return -1;
        }
        if(getResultSet()!=null) {
            return -1;
        }
        return SQLite.changes(getSqliteDb());
    }

    public void addBatch() throws SQLException {
        if(parameters!=null) {
            Object[] temp = new Object[parameters.length];
            System.arraycopy(parameters, 0, temp, 0, parameters.length);
            _addBatch(sql, temp);
        } else {
            _addBatch(sql, null);
        }
    }

    
    // ParameterMetaData FUNCTIONS //////////////////////////////////

    public DomSQLParameterMetaData getParameterMetaData() {
        return new DomSQLParameterMetaData(this);
    }

    public Statement getStatement() {
        return this;
    }

    
    // PARAMETER FUNCTIONS //////////////////////////////////////////

    private void setParameter(int pos, Object value) throws SQLException {
        checkStatementOpen();
        pos--;
        if(pos<0 || pos>=parameters.length) {
            throw DomSQLException.create(null,"Invalid parameter index for the SQL query (index={0}, param count={1}",pos,parameters.length);
        }
        parameters[pos] = value;
    }

    public void setNull(int pos, int u1) throws SQLException {
        setParameter(pos, null);
    }

    public void setNull(int pos, int u1, String u2) throws SQLException {
        setParameter(pos, null);
    }

    public void setBoolean(int pos, boolean value) throws SQLException {
        setParameter(pos, value ? 1 : 0);
    }

    public void setByte(int pos, byte value) throws SQLException {
        setParameter(pos, (int) value);
    }

    public void setShort(int pos, short value) throws SQLException {
        setParameter(pos, (int) value);
    }

    public void setInt(int pos, int value) throws SQLException {
        setParameter(pos, new Integer(value));
    }

    public void setLong(int pos, long value) throws SQLException {
        setParameter(pos, new Long(value));
    }

    public void setFloat(int pos, float value) throws SQLException {
        setParameter(pos, new Float(value));
    }

    public void setDouble(int pos, double value) throws SQLException {
        setParameter(pos, new Double(value));
    }

    public void setString(int pos, String value) throws SQLException {
        setParameter(pos, value);
    }

    public void setObject(int pos, Object value) throws SQLException {
        if (value == null) {
            setParameter(pos, null);
        } else if(value instanceof String) {
            setParameter(pos, value.toString());
        } else if(value instanceof Number) {
            if (value instanceof Integer) {
                setParameter(pos, value);
            } else if (value instanceof Double) {
                setParameter(pos, value);
            } else if (value instanceof Long) {
                setParameter(pos, value);
            } else if (value instanceof Byte) {
                setParameter(pos, Integer.valueOf(((Byte) value).intValue()));
            } else if (value instanceof Short) {
                setParameter(pos, Integer.valueOf(((Short) value).intValue()));
            } else if (value instanceof Float) {
                setParameter(pos, value);
            } else {
                // Big Decimal, whatever => assume a double
                setParameter(pos, ((Number)value).doubleValue());
            }
        } else if(value instanceof java.util.Date) {
            setParameter(pos, DomSQL.toSQLiteDate((java.util.Date)value));
        } else if (value instanceof Boolean) {
            setInt(pos, ((Boolean) value).booleanValue()?1:0);
        } else {
            // by default, just convert it to a string
            setParameter(pos, value.toString());
        }
    }

    public void setObject(int p, Object v, int t) throws SQLException {
        setObject(p, v);
    }

    public void setObject(int p, Object v, int t, int s) throws SQLException {
        setObject(p, v);
    }

    public void setDate(int pos, Date value) throws SQLException {
        setParameter(pos, DomSQL.toSQLiteDate(value));
    }

    public void setDate(int pos, Date value, Calendar cal) throws SQLException {
        setParameter(pos, DomSQL.toSQLiteDate(value));
    }

    public void setTime(int pos, Time value) throws SQLException {
        setParameter(pos, DomSQL.toSQLiteDate(value));
    }

    public void setTime(int pos, Time value, Calendar cal) throws SQLException {
        setParameter(pos, DomSQL.toSQLiteDate(value));
    }

    public void setTimestamp(int pos, Timestamp value) throws SQLException {
        setParameter(pos, DomSQL.toSQLiteDate(value));
    }

    public void setTimestamp(int pos, Timestamp value, Calendar cal) throws SQLException {
        setParameter(pos, DomSQL.toSQLiteDate(value));
    }

    public void setBytes(int pos, byte[] value) throws SQLException {
        setParameter(pos, value);
    }

    
    public DomSQLResultSetMetaData getMetaData() throws SQLException {
        checkStatementOpen();
        // Else we need to execute the query as it SQLite can only return the
        // metadata after a successful call to step(). See: http://www.sqlite.org/c3ref/column_blob.html
        // As a result, we should execute the query to get the meta data if this function is called
        // before execute().
        // Can we optimize it so the result set is not executed a second time after a call
        // to the meta data?
        DomSQLResultSet rs = getResultSet();
        if(rs==null) {
        	if(execute()) {
        		rs = getResultSet();
        	}
        }
        if(rs!=null) {
        	return rs.getMetaData();
        }
        // Ok, this is just an undefined set of meta data...
        return new DomSQLResultSetMetaData(getHstmt());
    }

    
    
    // UNUSED ///////////////////////////////////////////////////////

    public boolean execute(String sql) throws SQLException {
        throw new NotImplementedException();
    }

    public int executeUpdate(String sql) throws SQLException {
        throw new NotImplementedException();
    }

    public DomSQLResultSet executeQuery(String sql) throws SQLException {
        throw new NotImplementedException();
    }

    public void addBatch(String sql) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setAsciiStream(int parameterIndex, InputStream x)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setBigDecimal(int parameterIndex, BigDecimal x)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setBinaryStream(int parameterIndex, InputStream x)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setBlob(int parameterIndex, InputStream inputStream)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setCharacterStream(int parameterIndex, Reader reader,
            long length) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setCharacterStream(int parameterIndex, Reader reader)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setClob(int parameterIndex, Reader reader, long length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setNCharacterStream(int parameterIndex, Reader value,
            long length) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setNCharacterStream(int parameterIndex, Reader value)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setNClob(int parameterIndex, Reader reader, long length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setNString(int parameterIndex, String value)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        throw new NotImplementedException();
    }
}
