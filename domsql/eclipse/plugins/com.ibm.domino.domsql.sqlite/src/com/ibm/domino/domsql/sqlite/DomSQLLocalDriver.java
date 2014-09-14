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

package com.ibm.domino.domsql.sqlite;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.domsql.DomSQLDriver;
import com.ibm.domino.domsql.remote.server.ServerConnection;
import com.ibm.domino.domsql.remote.server.ServerContext;
import com.ibm.domino.domsql.sqlite.driver.Context;
import com.ibm.domino.domsql.sqlite.driver.Database;
import com.ibm.domino.domsql.sqlite.driver.DatabaseFactory;
import com.ibm.domino.domsql.sqlite.driver.jdbc.DomSQLConnection;
import com.ibm.domino.domsql.sqlite.driver.jni.DomSQL;
import com.ibm.domino.domsql.sqlite.driver.jni.JNIUtils;
import com.ibm.dots.session.User;

public class DomSQLLocalDriver implements DomSQLDriver.LocalDriver {
	
    private static boolean nativeInitialized;
    
    static {
    	initLocal();
    	DomSQLDriver.initLocalDriver(new DomSQLLocalDriver());
    }

    public static void initLocal() {
        if (!nativeInitialized) {
        	nativeInitialized = true;
            try {
            	//System.loadLibrary("ndomsql");
            	System.loadLibrary("domsql");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            
            // Initialize the diagnostics flags
            //DomSQL.setFlag("Java:Trace SQL", true);
            //DomSQL.setFlag("Native:Dump Design", true);
            //DomSQL.setFlag("Native:Dump SecretCode", true);
            
            DomSQL.debug.updateNative();
        }
    }

    // Should only be instanciated once statically
    private DomSQLLocalDriver() {
    }

    public boolean acceptsURL(String url) {
        return StringUtil.isNotEmpty(url) && url.toLowerCase().startsWith(DomSQLDriver.PREFIX);
    }

    // Local URL format
    //    jdbc:domsql:[nsf name]/[domsql]    
    //    jdbc:domsql:XPagesDomSQL.nsf/All
    public Connection connectLocal(String url, Properties info) throws SQLException {
        return createLocalConnection(url, info);
    }
    
    public static ServerConnection createServerConnection(String url, User user, Properties info) throws SQLException {
        String dbName = url.substring(DomSQLDriver.PREFIX.length());
        ServerContext context = new ServerContext(user);
        try {
	        ServerConnection rc = new ServerConnection(context);
	        
	        rc.getSessionContext().initThread();
	        try {
	            // Init the connection
	            rc.initConnection();
	            
	            // And execute 
	            rc.initContext();
	            try {
	                Database db = DatabaseFactory.getDatabaseFactory().getDatabase(dbName);
	                context.setDefaultDbName(db.getNsfPath());
	                DomSQLConnection c = new DomSQLConnection(db);
	                rc.setNative(c);
	                return rc;
	            } finally {
	                rc.termContext();
	            }
	        } finally {
	            rc.getSessionContext().termThread();
	        }
        } catch(RemoteException ex) {
        	throw new SQLException(ex);
        }
    }
    public static Connection createLocalConnection(String url, Properties info) throws SQLException {
        String dbName = url.substring(DomSQLDriver.PREFIX.length());
        Database db = DatabaseFactory.getDatabaseFactory().getDatabase(dbName);
        DomSQLConnection c = new DomSQLConnection(db);
        // For test only...
        if(false) {
            long hdb = Context.get().getDBHandle(null);
            List<String> l = new ArrayList<String>();
            DomSQL.readViewList(hdb,l);
            for(int i=0; i<l.size(); i++) {
                JNIUtils.trace("List[#{0}]={1}",i,l.get(i));
            }
        }
        return c;
    }
    
    // Check if the request is a remote request
    private static boolean isFromRmiJdbc(String url, Properties info) {
        // Introspect the call stack and see if it comes from an RMI request
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for(int i=0; i<stack.length; i++) {
            if(stack[i].getClassName().startsWith("sun.rmi.")) {
                return true;
            }
        }
        return false;
    }
}
