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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.domino.domsql.sqlite.driver.jdbc.DomSQLConnection;
import com.ibm.domino.domsql.sqlite.driver.jdbc.DomSQLStatement;
import com.ibm.domino.domsql.sqlite.driver.jni.DomSQL;
import com.ibm.domino.domsql.sqlite.driver.jni.JNIUtils;
import com.ibm.domino.domsql.sqlite.driver.jni.NotesAPI;
import com.ibm.domino.domsql.sqlite.driver.meta.DatabaseDefinition;


/**
 * Holds the definition a pointer to a database.
 * 
 * @author priand
 */
public class Database {
	
	private String name;
    private String url;
    private String domSqlName;
    private String nsfPath;
    private long sqliteDb;
    
    private long timeout; 
    private long lastAccess; 
    private long designTS; 
    
    private List<DomSQLConnection> connections = new ArrayList<DomSQLConnection>();
    
    public Database(String name, String url, String nsfPath, String domSqlName) {
    	this.name = name;
    	this.url = url;
        this.nsfPath = nsfPath;
        this.domSqlName = domSqlName;
		this.timeout = Constants.DEFAULT_TIMEOUT;
		try {
			int mins = NotesAPI.OSGetEnvironmentInt(Constants.NOTESINI_DOMSQLPORT);
			if(mins>0) {
				this.timeout = mins * 60 *1000; // in ms, from minutes  
			} else if(mins<0) {
				this.timeout = -1;
			}
		} catch(Exception e) {} // Use the defaut...
    }
    
    public String getName() {
    	return name;
    }
    
    public long getLastAccess() {
    	return lastAccess;
    }
    
    public void updateLastAccess() {
    	this.lastAccess = System.currentTimeMillis();
    }
    
    public boolean isExpired(long now) {
    	if(timeout>0) {
    		return lastAccess+timeout<now;
    	}
    	return false;
    }
    
    public String getUrl() {
    	return url;
    }
    
    public String getNsfPath() {
        return nsfPath;
    }
    
    public String getDomSqlName() {
        return domSqlName;
    }
    
    public long getSqliteDb() {
    	return sqliteDb;
    }
    
    public synchronized void close() throws SQLException {
    	// Make a copy as closing a connection removes it from the list
    	DomSQLConnection[] c = connections.toArray(new DomSQLConnection[connections.size()]); 
        for(int i=0; i<c.length; i++) { 
            c[i].close();
        }
        
        // Then close the SQLDB
        if(sqliteDb!=0) {
        	SQLite.close(sqliteDb);
        	sqliteDb = 0;
        }
    }

    
    // ============================================================================
    // Connection management
    // ============================================================================
    
    public synchronized void addConnection(DomSQLConnection c) {
        connections.add(c);
    }
    
    public synchronized void removeConnection(DomSQLConnection c) {
        connections.remove(c);
    }
    

    
    // ============================================================================
    // Check if the database is outdated
    // ============================================================================

    public boolean refreshIfNecessary() throws SQLException { 
    	if(shouldRefresh()) {
    		refresh();
    		return true;
    	}
    	return false;
    }
    
    public boolean shouldRefresh() throws SQLException {
    	// We currently only check the current database
    	// But we should also check all the referenced databases to be exhautive
    	try {
    		long hdb = Context.get().getDBHandle(null);
    		if(hdb!=0) {
    			long ts = NotesAPI.NSFDbNonDataModifiedTime(hdb);
    			if(ts>designTS) {
    				return true;
    			}
    		}
    	} catch(Throwable t) {
    		Platform.getInstance().log(t);
    	}
    	return false;
    }
    
    public synchronized void refresh() throws SQLException {
        DefaultContext context = new DefaultContext();
        context.setDefaultDbName(nsfPath);
        try {
        	// If the SQLite database was already initialized, then close it first
        	if(sqliteDb!=0) {
        		SQLite.close(sqliteDb);
        		sqliteDb = 0;
        	}
        	
        	// Read the database definition
        	DatabaseDefinition dbDef = new DatabaseDefinition(nsfPath,domSqlName);
        	
        	// Read the database definition file
        	dbDef.readDomsql(context);

            // Create the modules for the view table
        	List<String> sql = dbDef.generateInitSql(context);

        	// Read the SQLite file name
        	// It default to a temporary DB
        	String fileName = dbDef.getSqliteFileName();
        	if(fileName==null) {
        		fileName = ""; //":memory:";
        	}
        	
        	// Open the sqlite database
            this.sqliteDb = SQLite.open(fileName);
			if(DomSQL.debug.TRACE_DATABASE_LIFECYCLE) {
				JNIUtils.trace("Opening SQLite Database {0}, fileName {1}, handle {2}",getName(),fileName,this.sqliteDb);
			}
            
            // Get the last change time
            long designTS = NotesAPI.NSFDbNonDataModifiedTime(context.getDBHandle(null));
            this.designTS = designTS;
        	
            // Register the domino module for this database
            SQLite.initDominoModule(sqliteDb);
        	
        	// Then emit the statement to the driver
    		DomSQLConnection c = new DomSQLConnection(this);
    		try {
    		    DomSQLStatement st = c.createStatement();
    		    try {
	                // Clean-up the database if it is using a persistent database
	                if(StringUtil.isNotEmpty(fileName) && !fileName.equals(":memory:")) {
	                	clearDatabase(c,st);
	                }
	                
	                // And then emit the statements
	    			for(int i=0; i<sql.size(); i++) {
	    				String s = sql.get(i);
	    				st.execute(s);
	    			}
        		} finally {
        			st.close();
        		}
    		} finally {
    			c.close();
    		}
        } finally {
            context.close();
        }
    }
    private static void clearDatabase(DomSQLConnection c, Statement st) throws SQLException {
    	// Get the list of our virtual tables
    	List<String> delSql = new ArrayList<String>();
		ResultSet rs = st.executeQuery("select name from sqlite_master where type='table' and sql like '%USING domsql(%'");
		try {
			while(rs.next()) {
				String name = rs.getString(1);
				String sql = "DROP TABLE "+name;
				delSql.add(sql);
			}
		} finally {
			rs.close();
		}
		
		// And emit the deletion
		for(String sql: delSql) {
			st.execute(sql);
		}
    }
    
}
