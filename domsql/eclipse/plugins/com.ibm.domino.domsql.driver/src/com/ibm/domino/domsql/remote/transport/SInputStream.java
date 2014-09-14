/*
 * � Copyright IBM Corp. 2010
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



/**
 *
 */
public class SInputStream extends SerializableObject {
	
	private static final long serialVersionUID = 1L;
	
	public static SInputStream create(InputStream w) {
		throw new IllegalStateException();
	}
	public static InputStream getInputStream(SInputStream w) {
		throw new IllegalStateException();
	}
	public static byte[] getByteArray(SInputStream w) {
		throw new IllegalStateException();
	}
	
	private SInputStream() {
		// should be public for serialization...
		throw new IllegalStateException();
	}
}