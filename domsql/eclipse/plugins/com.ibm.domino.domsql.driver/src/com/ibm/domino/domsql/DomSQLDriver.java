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

package com.ibm.domino.domsql;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

import com.ibm.domino.domsql.remote.client.ClientConnection;
import com.ibm.domino.domsql.remote.transport.IConnection;
import com.ibm.domino.domsql.remote.transport.IDomSQLDriver;
import com.ibm.domino.domsql.remote.transport.ServerOptions;

public class DomSQLDriver implements Driver {
	
	public interface LocalDriver {
	    public Connection connectLocal(String url, Properties info) throws SQLException;
	}
	protected static LocalDriver localDriver;
	public static void initLocalDriver(LocalDriver driver) {
		localDriver = driver;
	}
	
    public static final String PREFIX       	= "jdbc:domsql:";
    
	public static final String DOMSQL_FACTORY 	= "DomSQLFactory";
    public static final int	   DEFAULT_PORT		= 8089;

    private static boolean driverInitialized;
    
    static {
    	init();
    }

    public static void init() {
        if (!driverInitialized) {
            driverInitialized = true;
            try {
                DriverManager.registerDriver(new DomSQLDriver());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getMajorVersion() {
        return 1;
    }

    public int getMinorVersion() {
        return 1;
    }

    public boolean jdbcCompliant() {
        return false;
    }

    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
            throws SQLException {
//        DriverPropertyInfo sharedCache = new DriverPropertyInfo("shared_cache",
//                "false");
//        sharedCache.choices = new String[] { "true", "false" };
//        sharedCache.description = "Enable SQLite Shared-Cache mode, native driver only.";
//        sharedCache.required = false;
//        return new DriverPropertyInfo[] { sharedCache };
        return new DriverPropertyInfo[] {};
    }

    // Remote URL format
    //    jdbc:domsql://[address[:port]]/[nsf name]/[domsql]    
    //    jdbc:domsql://127.0.0.1/XPagesDomSQL.nsf/All
    // Local URL format
    //    jdbc:domsql:[nsf name]/[domsql]    
    //    jdbc:domsql:XPagesDomSQL.nsf/All
    public Connection connect(String url, Properties info) throws SQLException {
        // Normalize the URL
        url = url.trim();
        
        // Read the URL parameters
        int qsp = url.indexOf('?');
        if(qsp>=0) {
        	String qs = url.substring(qsp+1);
        	String[] params = splitString(qs, '&');
        	for(int i=0; i<params.length; i++) {
        		String s = params[i];
        		int pos = s.indexOf('=');
        		if(pos>=0) {
        			String name = s.substring(0,pos);
        			String value = s.substring(pos+1);
        			info.put(name,value);
        		}
        	}
        }
        
        // Now, ask the dictionary for the database
        if(url.toLowerCase().startsWith(PREFIX)) {
            String path = url.substring(PREFIX.length());
            
        	// If we ask for a remote connection, the 
        	if(path.startsWith("//")) {
        		return createRemoteConnection(path,info);
        	}
        	
        	// Try a delegate (local driver)
        	if(localDriver!=null) {
        		return localDriver.connectLocal(url, info);
        	}
        }
        
        // We do not handle this url
        return null;
    }
    public static String[] splitString( String s, char sep ) {
        return splitString( null, 0, s, 0, sep );
    }
    private static String[] splitString( String[] result, int count, String s, int pos, char sep ) {
        int newPos = s.indexOf(sep,pos);
        if( newPos>=0 ) {
            result = splitString( null, count+1, s, newPos+1, sep );
            result[count] = s.substring( pos, newPos );
        } else {
            result = new String[count+1];
            result[count] = s.substring( pos );
        }
        result[count] = result[count].trim();
        return result;
    }
    
	// Create a connection from a URL
	// The path must be of the form:
	//	//[address[:port]]/[nsf name]/[domsql]
	public static ClientConnection createRemoteConnection(String path, Properties pInfo) throws SQLException {
		// Extract the pieces from the URL
		if(!path.startsWith("//")) {
			throw new SQLException("Remote path should start with //");
		}
		String host = null;
		int port = DomSQLDriver.DEFAULT_PORT;
		int spos = path.indexOf('/',2);
		if(spos>0) {
			int ppos = path.indexOf(':',2);
			if(ppos>0 && ppos<spos) {
				host = path.substring(2,ppos);
				String sport = path.substring(ppos+1,spos);
				try {
					port = Integer.parseInt(sport);
				} catch(NumberFormatException e) {
					throw new SQLException("Invalid port specification");
				}
			} else {
				host = path.substring(2,spos);
			}
		}
		if(host==null) {
			throw new SQLException("Invalid host //server:port/ format");
		}
		
		// Only keep the db path
		String dbPath = path.substring(spos+1);
		if(dbPath.startsWith("//")) {
			throw new SQLException("Invalid database path: a remote connection cannot referemce another remote connection");
		}
		
		// Establish a connection to the RMI server
		try {
			IDomSQLDriver domDriver = loadDriverFromRegistry(host, port);
			if(ServerOptions.options==null) {
				ServerOptions.options = domDriver.getServerOptions();
			}
			IConnection conn = domDriver.createConnection(dbPath, pInfo);
			return new ClientConnection(conn);
		} catch(Exception e) {
			if(e instanceof SQLException) {
				throw (SQLException)e;
			}
			throw new SQLException("Error while connecting to the remote server", e);
		}
	}
	private static IDomSQLDriver loadDriverFromRegistry(String host, int port) throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(host, port);
		IDomSQLDriver domDriver = (IDomSQLDriver)registry.lookup(DOMSQL_FACTORY);
		return domDriver;
	}
	

    public boolean acceptsURL(String url) {
        return url!=null && url.toLowerCase().startsWith(PREFIX);
    }
}
