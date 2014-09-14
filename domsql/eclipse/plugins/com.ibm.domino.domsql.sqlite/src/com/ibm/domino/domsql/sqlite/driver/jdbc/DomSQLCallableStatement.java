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

import com.ibm.commons.util.NotImplementedException;

/**
 * DomSQL has currently no support for stored procedures.
 * 
 * @author priand
 *
 */
public class DomSQLCallableStatement extends DomSQLPreparedStatement implements CallableStatement {

    public DomSQLCallableStatement(DomSQLConnection conn, String sql) throws SQLException {
        super(conn,sql);
    }

    //@Override
    public Array getArray(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }
    
    //@Override
    public Array getArray(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public BigDecimal getBigDecimal(int parameterIndex, int scale)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Blob getBlob(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Blob getBlob(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public boolean getBoolean(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public boolean getBoolean(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public byte getByte(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public byte getByte(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public byte[] getBytes(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public byte[] getBytes(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Clob getClob(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Clob getClob(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Date getDate(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Date getDate(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Date getDate(String parameterName, Calendar cal) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public double getDouble(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public double getDouble(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public float getFloat(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public float getFloat(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public int getInt(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public int getInt(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public long getLong(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public long getLong(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Object getObject(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Object getObject(int parameterIndex, Map<String, Class<?>> map)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Object getObject(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Object getObject(String parameterName, Map<String, Class<?>> map)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Ref getRef(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Ref getRef(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public short getShort(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public short getShort(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public String getString(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public String getString(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Time getTime(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Time getTime(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Time getTime(String parameterName, Calendar cal) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Timestamp getTimestamp(int parameterIndex, Calendar cal)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Timestamp getTimestamp(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Timestamp getTimestamp(String parameterName, Calendar cal)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public URL getURL(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public URL getURL(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void registerOutParameter(int parameterIndex, int sqlType)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void registerOutParameter(int parameterIndex, int sqlType, int scale)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void registerOutParameter(int paramIndex, int sqlType,
            String typeName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void registerOutParameter(String parameterName, int sqlType)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void registerOutParameter(String parameterName, int sqlType,
            int scale) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void registerOutParameter(String parameterName, int sqlType,
            String typeName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setAsciiStream(String parameterName,
            InputStream theInputStream, int length) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setBigDecimal(String parameterName, BigDecimal theBigDecimal)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setBinaryStream(String parameterName,
            InputStream theInputStream, int length) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setBoolean(String parameterName, boolean theBoolean)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setByte(String parameterName, byte theByte) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setBytes(String parameterName, byte[] theBytes)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setCharacterStream(String parameterName, Reader reader,
            int length) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setDate(String parameterName, Date theDate) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setDate(String parameterName, Date theDate, Calendar cal)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setDouble(String parameterName, double theDouble)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setFloat(String parameterName, float theFloat)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setInt(String parameterName, int theInt) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setLong(String parameterName, long theLong) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setNull(String parameterName, int sqlType) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setNull(String parameterName, int sqlType, String typeName)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setObject(String parameterName, Object theObject)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setObject(String parameterName, Object theObject,
            int targetSqlType) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setObject(String parameterName, Object theObject,
            int targetSqlType, int scale) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setShort(String parameterName, short theShort)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setString(String parameterName, String theString)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setTime(String parameterName, Time theTime) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setTime(String parameterName, Time theTime, Calendar cal)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setTimestamp(String parameterName, Timestamp theTimestamp)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setTimestamp(String parameterName, Timestamp theTimestamp,
            Calendar cal) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setURL(String parameterName, URL theURL) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public boolean wasNull() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public RowId getRowId(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public RowId getRowId(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setRowId(String parameterName, RowId x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setNString(String parameterName, String value)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setNCharacterStream(String parameterName, Reader value,
            long length) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setNClob(String parameterName, NClob value) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setClob(String parameterName, Reader reader, long length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setBlob(String parameterName, InputStream inputStream,
            long length) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setNClob(String parameterName, Reader reader, long length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public NClob getNClob(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public NClob getNClob(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setSQLXML(String parameterName, SQLXML xmlObject)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public SQLXML getSQLXML(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public SQLXML getSQLXML(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public String getNString(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public String getNString(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Reader getNCharacterStream(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Reader getNCharacterStream(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Reader getCharacterStream(int parameterIndex) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public Reader getCharacterStream(String parameterName) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setBlob(String parameterName, Blob x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setClob(String parameterName, Clob x) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setAsciiStream(String parameterName, InputStream x, long length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setBinaryStream(String parameterName, InputStream x, long length)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setCharacterStream(String parameterName, Reader reader,
            long length) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setAsciiStream(String parameterName, InputStream x)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setBinaryStream(String parameterName, InputStream x)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setCharacterStream(String parameterName, Reader reader)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setNCharacterStream(String parameterName, Reader value)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setClob(String parameterName, Reader reader)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setBlob(String parameterName, InputStream inputStream)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public void setNClob(String parameterName, Reader reader)
            throws SQLException {
        throw new NotImplementedException();
    }
    
/*#IF JDBC40        

#ENDIF*/

}
