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
package com.ibm.domino.domsql.remote.server;

import java.sql.SQLException;

import com.ibm.domino.domsql.sqlite.driver.DefaultContext;
import com.ibm.domino.domsql.sqlite.driver.jni.NotesAPI;
import com.ibm.dots.session.User;



/**
 * Encapsulate a runtime context for the server connection. 
 * 
 * @author priand
 */
public class ServerContext extends DefaultContext {

	private User user;
	
    public ServerContext(User user) {
    	this.user = user;
    }
    
    public User getUser() {
    	return user;
    }

    @Override
    public synchronized void close() {
    	if(user!=null) {
    		user.dispose();
    		user = null;
    	}
    	super.close();
    }
    
    @Override
    protected long openDBHandle(String path) throws SQLException {
		try {
	    	if(user!=null) {
	   			// Open the DB with the user credentials
	            return NotesAPI.NSFDBOpenEx(path,user.getUserListHandle());
	    	}
	        return NotesAPI.NSFDBOpen(path);
		} catch(Throwable t) {
			throw new SQLException(t);
		}
    }
}
