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
import java.sql.Blob;
import java.sql.SQLException;


/**
 *
 */
public class SBlob extends SerializableObject implements Blob {

	private static final long serialVersionUID = 1L;

	public static SBlob create(Blob w) {
		if(w==null) {
			return null;
		}
		if(w instanceof SBlob) {
			return (SBlob)w;
		}
		throw new IllegalStateException();
	}
	
	public SBlob() {
		throw notImplementedException();
	}

	public InputStream getBinaryStream() throws SQLException {
		throw notImplementedException();
	}

	public byte[] getBytes(long pos, int length) throws SQLException {
		throw notImplementedException();
	}

	public long length() throws SQLException {
		throw notImplementedException();
	}

	public long position(Blob pattern, long start) throws SQLException {
		throw notImplementedException();
	}

	public long position(byte[] pattern, long start) throws SQLException {
		throw notImplementedException();
	}

	public OutputStream setBinaryStream(long pos) throws SQLException {
		throw notImplementedException();
	}

	public int setBytes(long pos, byte[] theBytes) throws SQLException {
		throw notImplementedException();
	}

	public int setBytes(long pos, byte[] theBytes, int offset, int len) throws SQLException {
		throw notImplementedException();
	}

	public void truncate(long len) throws SQLException {
		throw notImplementedException();
	}

	public void free() throws SQLException {
		throw notImplementedException();
	}

	public InputStream getBinaryStream(long pos, long length) throws SQLException {
		throw notImplementedException();
	}
}
