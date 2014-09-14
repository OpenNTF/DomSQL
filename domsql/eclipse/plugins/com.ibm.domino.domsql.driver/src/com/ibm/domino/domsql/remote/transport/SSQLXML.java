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
import java.io.Writer;
import java.sql.SQLException;
import java.sql.SQLXML;

import javax.xml.transform.Result;
import javax.xml.transform.Source;


/**
 *
 */
public class SSQLXML extends SerializableObject implements SQLXML {

	private static final long serialVersionUID = 1L;
	
	public static SSQLXML create(SQLXML w) {
		if(w==null) {
			return null;
		}
		if(w instanceof SSQLXML) {
			return (SSQLXML)w;
		}
		throw new IllegalStateException();
	}

	public SSQLXML() {
		throw notImplementedException();
	}
	
	public void free() throws SQLException {
		throw notImplementedException();
	}

	public InputStream getBinaryStream() throws SQLException {
		throw notImplementedException();
	}

	public OutputStream setBinaryStream() throws SQLException {
		throw notImplementedException();
	}

	public Reader getCharacterStream() throws SQLException {
		throw notImplementedException();
	}

	public Writer setCharacterStream() throws SQLException {
		throw notImplementedException();
	}

	public String getString() throws SQLException {
		throw notImplementedException();
	}

	public void setString(String value) throws SQLException {
		throw notImplementedException();
	}

	public <T extends Source> T getSource(Class<T> sourceClass) throws SQLException {
		throw notImplementedException();
	}

	public <T extends Result> T setResult(Class<T> resultClass) throws SQLException {
		throw notImplementedException();
	}

}
