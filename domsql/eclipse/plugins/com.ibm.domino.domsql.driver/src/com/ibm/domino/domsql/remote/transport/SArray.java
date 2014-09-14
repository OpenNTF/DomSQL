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

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;


/**
 *
 */
public class SArray extends SerializableObject implements Array {

	private static final long serialVersionUID = 1L;

	public static SArray create(Array w) {
		if(w==null) {
			return null;
		}
		if(w instanceof SArray) {
			return (SArray)w;
		}
		throw new IllegalStateException();
	}
	
	public SArray() {
		throw notImplementedException();
	}

	public Object getArray() throws SQLException {
		throw notImplementedException();
	}

	public Object getArray(long index, int count) throws SQLException {
		throw notImplementedException();
	}

	public Object getArray(long index, int count, Map<String, Class<?>> map) throws SQLException {
		throw notImplementedException();
	}

	public Object getArray(Map<String, Class<?>> map) throws SQLException {
		throw notImplementedException();
	}

	public int getBaseType() throws SQLException {
		throw notImplementedException();
	}

	public String getBaseTypeName() throws SQLException {
		throw notImplementedException();
	}

	public ResultSet getResultSet() throws SQLException {
		throw notImplementedException();
	}

	public ResultSet getResultSet(long index, int count) throws SQLException {
		throw notImplementedException();
	}

	public ResultSet getResultSet(long index, int count, Map<String, Class<?>> map) throws SQLException {
		throw notImplementedException();
	}

	public ResultSet getResultSet(Map<String, Class<?>> map) throws SQLException {
		throw notImplementedException();
	}

	public void free() throws SQLException {
		throw notImplementedException();
	}	
}
