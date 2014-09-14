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
package com.ibm.domino.domsql.sqlite.driver;

import java.sql.SQLException;
import java.util.HashMap;

import com.ibm.domino.domsql.sqlite.DomSQLException;
import com.ibm.domino.domsql.sqlite.driver.jni.NotesAPI;


/**
 * Encapsulate a runtime context for accessing the data 
 * 
 * @author priand
 */
public abstract class Context {
	
    private static ContextFinder contextFinder;

    public static abstract class ContextFinder {
    	ContextFinder next;
    	protected ContextFinder(ContextFinder next) {
    		this.next = next;
    	}
        public abstract Context find();
    }

    public static ContextFinder getContextFinder() {
        return contextFinder;
    }

    public static void setContextFinder(ContextFinder contextFinder) {
        Context.contextFinder=contextFinder;
    }
    
    
    private static ThreadLocal<Context> contexts = new ThreadLocal<Context>();
    
    public static Context get() {
        Context c = contexts.get();
        if(c!=null) {
            return c;
        }
        for(ContextFinder f=contextFinder; f!=null; f=f.next) {
        	c = f.find();
        	if(c!=null) {
        		return c;
        	}
        }
        throw new IllegalStateException("No current DomSQL context set");
    }

    public static void push(Context context) {
        contexts.set(context);
    }
    
    public static void pop() {
        contexts.remove();
    }
    
    private HashMap<String, Long> handles = new HashMap<String, Long>();
    
    public Context() {
    }

    public synchronized void close() {
        for(Long hDb: handles.values()) {
            try {
                if(shouldClose(hDb)) {
                    NotesAPI.NSFDBClose(hDb);
                }
            } catch(SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    protected boolean shouldClose(long hDb) {
        return true;
    }
    
    public long getDBHandle(String path) throws SQLException {
        if(path==null) path = "";
        Long l = handles.get(path);
        if(l!=null) {
            return l;
        }
        long h = path.length()==0 ? getDefaultDBHandle() : openDBHandle(path);
        handles.put(path, h);
        return h;
    }
    
    protected long openDBHandle(String path) throws SQLException {
        return NotesAPI.NSFDBOpen(path);
    }

    protected long getDefaultDBHandle() throws SQLException {
        throw DomSQLException.create(null, "No default database handle available");
    }
}
