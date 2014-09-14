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
import java.sql.Savepoint;


/**
 *
 */
public class SSavepoint extends SerializableObject implements Savepoint {

	private static final long serialVersionUID = 1L;

	public static SSavepoint create(Savepoint w) {
		if(w==null) {
			return null;
		}
		if(w instanceof SSavepoint) {
			return (SSavepoint)w;
		}
		throw new IllegalStateException();
	}
	
	public SSavepoint() {
		throw notImplementedException();
	}
	
	public int getSavepointId() throws SQLException {
		throw notImplementedException();
	}

	public String getSavepointName() throws SQLException {
		throw notImplementedException();
	}
}
