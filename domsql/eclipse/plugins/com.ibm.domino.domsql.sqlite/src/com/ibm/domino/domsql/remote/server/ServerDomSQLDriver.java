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

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Properties;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.domsql.DomSQLDriver;
import com.ibm.domino.domsql.remote.transport.IConnection;
import com.ibm.domino.domsql.remote.transport.IDomSQLDriver;
import com.ibm.domino.domsql.remote.transport.ServerOptions;
import com.ibm.domino.domsql.sqlite.DomSQLException;
import com.ibm.domino.domsql.sqlite.DomSQLLocalDriver;
import com.ibm.domino.domsql.sqlite.driver.jni.NotesAPI;
import com.ibm.dots.session.User;
import com.ibm.dots.session.UserAuthException;

/**
 *
 */
public class ServerDomSQLDriver implements IDomSQLDriver {

	private static final long serialVersionUID = 1L;

	private static ServerOptions serverOptions = new ServerOptions();
		
    public ServerDomSQLDriver() {
    	super();
    }
    
	public IConnection createConnection(String path, Properties info) throws SQLException, RemoteException {
		// Look for a user/password
		String userid = (String)info.get("user");
		String password = (String)info.get("password");
		
		// Authenticate the user
		User user = null;
		try {
			NotesAPI.NotesInitThread();
			try {
				if(StringUtil.isNotEmpty(userid)) {
					user = User.createUser(userid, password);
				} else {
					user = User.createAnonymousUser();
				}
					
				// Try to open the database.
				// If it fails, it is because the user doesn't have enough access rights
				int nsf = path.indexOf(".nsf");
				String dbPath = path.substring(0,nsf+4);
				long hDb = 0;
				try {
					hDb = NotesAPI.NSFDBOpenEx(dbPath, user.getUserListHandle());
				} catch(SQLException ex) {
					ex.printStackTrace();
				}
				if(hDb==0) {
					user.dispose();
					throw DomSQLException.create(null, "User '{0}' cannot open the database {1}", userid, dbPath );
				} else { 
					NotesAPI.NSFDBClose(hDb);
				}
			} finally {
				NotesAPI.NotesTermThread();
			}
		} catch(UserAuthException ex) {
			// We cannot pass the exception as a parameter as the local driver won't have the class
			// which will lead to a RMI unmarshal exception.
			throw DomSQLException.create(null, "Error while authenticating user '{0}'\n{1}", userid, ex.getMessage());
		}
		
		String url = DomSQLDriver.PREFIX + path;
		ServerConnection c = DomSQLLocalDriver.createServerConnection(url, user, info);
		return c;
	}

	public ServerOptions getServerOptions() throws SQLException, RemoteException {
		return serverOptions;
	}
}
