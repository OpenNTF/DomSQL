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

import java.io.OutputStream;


/**
 *
 */
public class SOutputStream extends SerializableObject {
	
	private static final long serialVersionUID = 1L;
	
	public static SOutputStream create(OutputStream w) {
		throw new IllegalStateException();
	}
	public static OutputStream getWriter(SOutputStream w) {
		throw new IllegalStateException();
	}
	
	private SOutputStream() {
		// should be public for serialization...
		throw new IllegalStateException();
	}
}
