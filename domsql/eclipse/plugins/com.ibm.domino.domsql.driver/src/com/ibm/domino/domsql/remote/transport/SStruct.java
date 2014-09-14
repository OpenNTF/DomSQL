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

import java.sql.SQLException;
import java.sql.Struct;
import java.util.Map;


/**
 *
 */
public class SStruct extends SerializableObject implements Struct {

	private static final long serialVersionUID = 1L;

	public static SStruct create(Struct w) {
		if(w==null) {
			return null;
		}
		if(w instanceof SStruct) {
			return (SStruct)w;
		}
		throw new IllegalStateException();
	}
	
	public SStruct() {
		throw notImplementedException();
	}

	public String getSQLTypeName() throws SQLException {
		throw notImplementedException();
	}

	public Object[] getAttributes() throws SQLException {
		throw notImplementedException();
	}

	public Object[] getAttributes(Map<String, Class<?>> theMap) throws SQLException {
		throw notImplementedException();
	}
}
