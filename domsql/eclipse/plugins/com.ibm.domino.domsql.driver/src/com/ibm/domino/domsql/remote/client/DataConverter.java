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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.util.Calendar;

import com.ibm.domino.domsql.remote.transport.SInputStream;
import com.ibm.domino.domsql.remote.transport.SReader;



/**
 * Data conversion utilities.
 */
public class DataConverter {
	
	private static SQLException conversionError() throws SQLException { // Should it be an exception?
		return new SQLException("Cannot convert the column value to the expected type");
	}

	public static boolean toBoolean(Object o) throws SQLException {
		if(o==null) {
			return false;
		}
		if(o instanceof Boolean) {
			return (Boolean)o;
		}
		if(o instanceof Number) {
			return ((Number)o).intValue()!=0 ? true : false;
		}
		throw conversionError();
	}
	public static char toChar(Object o) throws SQLException {
		if(o==null) {
			return 0;
		}
		if(o instanceof Character) {
			return (Character)o;
		}
		String s = null;
		if(o instanceof Number) {
			s = ((Number)o).toString();
		} else {
			s = o.toString();
		}
		if(s!=null) {
			if(s.length()==0) {
				return 0;
			}
			if(s.length()==1) {
				return s.charAt(0);
			}
		}
		throw conversionError();
	}
	public static byte toByte(Object o) throws SQLException {
		if(o==null) {
			return 0;
		}
		if(o instanceof Number) {
			return ((Number)o).byteValue();
		}
		if(o instanceof Character) {
			o = new String(new char[]{(Character)o});
		}
		if(o instanceof String) {
			String s = (String)o;
			if(s.length()>0) {
				try {
					return Byte.parseByte((String)o);
				} catch(NumberFormatException ex) {}
			}
			return 0;
		}
		if(o instanceof Boolean) {
			return ((Boolean)o).booleanValue()?(byte)1:(byte)0;
		}
		throw conversionError();
	}
	public static short toShort(Object o) throws SQLException {
		if(o==null) {
			return 0;
		}
		if(o instanceof Number) {
			return ((Number)o).shortValue();
		}
		if(o instanceof Character) {
			o = new String(new char[]{(Character)o});
		}
		if(o instanceof String) {
			String s = (String)o;
			if(s.length()>0) {
				try {
					return Short.parseShort((String)o);
				} catch(NumberFormatException ex) {}
			}
			return 0;
		}
		if(o instanceof Boolean) {
			return ((Boolean)o).booleanValue()?(short)1:(short)0;
		}
		throw conversionError();
	}
	public static int toInt(Object o) throws SQLException {
		if(o==null) {
			return 0;
		}
		if(o instanceof Number) {
			return ((Number)o).intValue();
		}
		if(o instanceof Character) {
			o = new String(new char[]{(Character)o});
		}
		if(o instanceof String) {
			String s = (String)o;
			if(s.length()>0) {
				try {
					return Integer.parseInt((String)o);
				} catch(NumberFormatException ex) {}
			}
			return 0;
		}
		if(o instanceof Boolean) {
			return ((Boolean)o).booleanValue()?1:0;
		}
		throw conversionError();
	}
	public static long toLong(Object o) throws SQLException {
		if(o==null) {
			return 0;
		}
		if(o instanceof Number) {
			return ((Number)o).longValue();
		}
		if(o instanceof Character) {
			o = new String(new char[]{(Character)o});
		}
		if(o instanceof String) {
			String s = (String)o;
			if(s.length()>0) {
				try {
					return Long.parseLong((String)o);
				} catch(NumberFormatException ex) {}
			}
			return 0;
		}
		if(o instanceof Boolean) {
			return ((Boolean)o).booleanValue()?1:0;
		}
		throw conversionError();
	}
	public static float toFloat(Object o) throws SQLException {
		if(o==null) {
			return 0;
		}
		if(o instanceof Number) {
			return ((Number)o).floatValue();
		}
		if(o instanceof Character) {
			o = new String(new char[]{(Character)o});
		}
		if(o instanceof String) {
			String s = (String)o;
			if(s.length()>0) {
				try {
					return Float.parseFloat((String)o);
				} catch(NumberFormatException ex) {}
			}
			return 0;
		}
		if(o instanceof Boolean) {
			return ((Boolean)o).booleanValue()?1:0;
		}
		throw conversionError();
	}
	public static double toDouble(Object o) throws SQLException {
		if(o==null) {
			return 0;
		}
		if(o instanceof Number) {
			return ((Number)o).doubleValue();
		}
		if(o instanceof Character) {
			o = new String(new char[]{(Character)o});
		}
		if(o instanceof String) {
			String s = (String)o;
			if(s.length()>0) {
				try {
					return Double.parseDouble((String)o);
				} catch(NumberFormatException ex) {}
			}
			return 0;
		}
		if(o instanceof Boolean) {
			return ((Boolean)o).booleanValue()?1:0;
		}
		throw conversionError();
	}
	public static BigDecimal toBigDecimal(Object o, int scale) throws SQLException {
		// TODO?? scale??
		return toBigDecimal(o);
	}
	public static BigDecimal toBigDecimal(Object o) throws SQLException {
		if(o==null) {
			return null;
		}
		if(o instanceof BigDecimal) {
			return (BigDecimal)o;
		}
		if(o instanceof Number) {
			return new BigDecimal(((Number)o).doubleValue());
		}
		if(o instanceof Character) {
			o = new String(new char[]{(Character)o});
		}
		if(o instanceof String) {
			String s = (String)o;
			if(s.length()>0) {
				try {
					return new BigDecimal((String)o);
				} catch(NumberFormatException ex) {}
			}
			return new BigDecimal(0);
		}
		throw conversionError();
	}
	public static String toString(Object o) throws SQLException {
		if(o==null) {
			return null;
		}
		return o.toString();
	}
	public static java.sql.Date toDate(Object o, Calendar cal) throws SQLException {
		// TODO: better?
		return toDate(o);
	}
	public static java.sql.Date toDate(Object o) throws SQLException {
		if(o==null) {
			return null;
		}
		if(o instanceof java.sql.Date) {
			return (java.sql.Date)o;
		}
		if(o instanceof java.util.Date) {
			return new java.sql.Date(((java.util.Date)o).getTime());
		}
		throw conversionError();
	}
	public static java.sql.Time toTime(Object o, Calendar cal) throws SQLException {
		// TODO: better?
		return toTime(o);
	}
	public static java.sql.Time toTime(Object o) throws SQLException {
		if(o==null) {
			return null;
		}
		if(o instanceof java.sql.Time) {
			return (java.sql.Time)o;
		}
		if(o instanceof java.util.Date) {
			return new java.sql.Time(((java.util.Date)o).getTime());
		}
		throw conversionError();
	}
	public static java.sql.Timestamp toTimestamp(Object o, Calendar cal) throws SQLException {
		// TODO: better?
		return toTimestamp(o);
	}
	public static java.sql.Timestamp toTimestamp(Object o) throws SQLException {
		if(o==null) {
			return null;
		}
		if(o instanceof java.sql.Timestamp) {
			return (java.sql.Timestamp)o;
		}
		if(o instanceof java.util.Date) {
			return new java.sql.Timestamp(((java.util.Date)o).getTime());
		}
		throw conversionError();
	}
	public static Array toArray(Object o) throws SQLException {
		if(o==null) {
			return null;
		}
		if(o instanceof Array) {
			return (Array)o;
		}
		throw conversionError();
	}
	public static Blob toBlob(Object o) throws SQLException {
		if(o==null) {
			return null;
		}
		if(o instanceof Blob) {
			return (Blob)o;
		}
		throw conversionError();
	}
	public static Clob toClob(Object o) throws SQLException {
		if(o==null) {
			return null;
		}
		if(o instanceof Clob) {
			return (Clob)o;
		}
		throw conversionError();
	}
	public static NClob toNClob(Object o) throws SQLException {
		if(o==null) {
			return null;
		}
		if(o instanceof NClob) {
			return (NClob)o;
		}
		throw conversionError();
	}
	public static SQLXML toSQLXML(Object o) throws SQLException {
		if(o==null) {
			return null;
		}
		if(o instanceof SQLXML) {
			return (SQLXML)o;
		}
		throw conversionError();
	}
	public static Ref toRef(Object o) throws SQLException {
		if(o==null) {
			return null;
		}
		if(o instanceof Ref) {
			return (Ref)o;
		}
		throw conversionError();
	}
	public static RowId toRowId(Object o) throws SQLException {
		if(o==null) {
			return null;
		}
		if(o instanceof RowId) {
			return (RowId)o;
		}
		throw conversionError();
	}
	public static URL toURL(Object o) throws SQLException {
		if(o==null) {
			return null;
		}
		if(o instanceof URL) {
			return (URL)o;
		}
		throw conversionError();
	}
	public static InputStream toInputStream(Object o) throws SQLException {
		if(o==null) {
			return null;
		}
		if(o instanceof SInputStream) {
			return SInputStream.getInputStream((SInputStream)o);
		}
		throw conversionError();
	}
	public static byte[] toByteArray(Object o) throws SQLException {
		if(o==null) {
			return null;
		}
		if(o instanceof byte[]) {
			return (byte[])o;
		}
		if(o instanceof SInputStream) {
			return SInputStream.getByteArray((SInputStream)o);
		}
		if(o instanceof InputStream) {
			try {
				InputStream is = (InputStream)o;
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				byte[] buffer = new byte[4096];
				int readBytes;
				while( (readBytes = is.read(buffer))>0 ) {
					os.write(buffer, 0, readBytes);
				}
				return os.toByteArray();
			} catch(IOException e) {
				throw new SQLException(e);
			}
		}
		throw conversionError();
	}
	public static Reader toReader(Object o) throws SQLException {
		if(o==null) {
			return null;
		}
		if(o instanceof SReader) {
			return SReader.getReader((SReader)o);
		}
		throw conversionError();
	}
}
