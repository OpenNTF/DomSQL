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
import java.sql.Clob;
import java.sql.SQLException;


/**
 *
 */
public class SClob extends SerializableObject implements Clob {

	private static final long serialVersionUID = 1L;

	public static SClob create(Clob w) {
		if(w==null) {
			return null;
		}
		if(w instanceof SClob) {
			return (SClob)w;
		}
		throw new IllegalStateException();
	}
	
	public SClob() {
		throw notImplementedException();
	}

	public InputStream getAsciiStream() throws SQLException {
		throw notImplementedException();
	}

	public Reader getCharacterStream() throws SQLException {
		throw notImplementedException();
	}

	public String getSubString(long pos, int length) throws SQLException {
		throw notImplementedException();
	}

	public long length() throws SQLException {
		throw notImplementedException();
	}

	public long position(Clob searchstr, long start) throws SQLException {
		throw notImplementedException();
	}

	public long position(String searchstr, long start) throws SQLException {
		throw notImplementedException();
	}

	public OutputStream setAsciiStream(long pos) throws SQLException {
		throw notImplementedException();
	}

	public Writer setCharacterStream(long pos) throws SQLException {
		throw notImplementedException();
	}

	public int setString(long pos, String str) throws SQLException {
		throw notImplementedException();
	}

	public int setString(long pos, String str, int offset, int len) throws SQLException {
		throw notImplementedException();
	}

	public void truncate(long len) throws SQLException {
		throw notImplementedException();
	}

	public void free() throws SQLException {
		throw notImplementedException();
	}

	public Reader getCharacterStream(long pos, long length) throws SQLException {
		throw notImplementedException();
	}	
}
