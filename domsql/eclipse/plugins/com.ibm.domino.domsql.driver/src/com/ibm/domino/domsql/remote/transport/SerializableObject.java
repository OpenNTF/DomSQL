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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.rmi.RemoteException;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Struct;
import java.text.MessageFormat;



/**
 *
 */
public class SerializableObject implements ISerializable, Serializable {

	public static Serializable create(Object o) throws SQLException {
		if(o!=null) {
			if( o instanceof Serializable ) {
				return (Serializable)o;
			}
			if( o instanceof Array) {
				return SArray.create((Array)o);
			}
			if( o instanceof Blob) {
				return SBlob.create((Blob)o);
			}
			if( o instanceof Clob) {
				return SClob.create((Clob)o);
			}
			if( o instanceof InputStream) {
				return SInputStream.create((InputStream)o);
			}
			if( o instanceof NClob) {
				return SNClob.create((NClob)o);
			}
			if( o instanceof OutputStream) {
				return SOutputStream.create((OutputStream)o);
			}
			if( o instanceof Reader) {
				return SReader.create((Reader)o);
			}
			if( o instanceof Ref) {
				return SRef.create((Ref)o);
			}
			if( o instanceof RowId) {
				return SRowId.create((RowId)o);
			}
			if( o instanceof Savepoint) {
				return SSavepoint.create((Savepoint)o);
			}
			if( o instanceof SQLXML) {
				return SSQLXML.create((SQLXML)o);
			}
			if( o instanceof Struct) {
				return SStruct.create((Struct)o);
			}
			if( o instanceof Writer) {
				return SWriter.create((Writer)o);
			}
			throw new SQLException(MessageFormat.format("Internal error: Object of class {0} is not a known ISerializableObject",o.getClass()));
			//throw new SQLException(StringUtil.format("Internal error: Object of class {0} is not a known ISerializableObject",o.getClass()));
		}
		return null;
	}
	
	private static final long serialVersionUID = 1L;

	public SerializableObject() {
	}
	

    public static SQLException newSQLException(Throwable t) {
    	SQLException e = new SQLException();
    	e.initCause(t);
    	return e;
    }
    
    protected RuntimeException notImplementedException() {
    	return new RuntimeException("Method not implemented");
    }	
    
    protected void throwNotImplementedException() throws SQLException, RemoteException {
    	throw new SQLException("Method not implemented");
    }	
}
