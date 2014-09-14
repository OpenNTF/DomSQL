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

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.domsql.sqlite.driver.jni.DomSQL;
import com.ibm.domino.domsql.sqlite.driver.jni.JNIUtils;

/**
 * Holds a dictionary of databases..
 * 
 * @author priand
 */
public class DatabaseFactory {

    private static DatabaseFactory databaseFactory = new DatabaseFactory();
    
    public static DatabaseFactory getDatabaseFactory() {
        return databaseFactory;
    }
    
    
    private HashMap<String,Database> databases = new HashMap<String,Database>();
    
    private Job cleaningJob;
    
    private DatabaseFactory() {
    }

    // ============================================================================
    // Check timeout
    // ============================================================================
    
    private synchronized List<Database> checkTimeout() {
    	long now = System.currentTimeMillis();
    	// Grab the list of expired databases
    	List<Database> dbList = null;
    	for(Database db: databases.values()) {
    		if(db.isExpired(now)) {
    			if(DomSQL.debug.TRACE_DATABASE_LIFECYCLE) {
    				JNIUtils.trace("Database {0} is discarded because it timed out",db.getName());
    			}
    			if(dbList==null) {
    				dbList = new ArrayList<Database>();
    			}
    			dbList.add(db);
    		}
    	}
    	// Remove the list of the databases that no longer in use
    	if(dbList!=null) {
    		int c = dbList.size();
    		for(int i=0; i<c; i++) {
    			databases.remove(dbList.get(i).getName());
    		}
    	}
    	return dbList;
    }
    
    
    // ============================================================================
    // Database Factory
    // ============================================================================
    
    public synchronized void closeAllDatabases() {
		if(DomSQL.debug.TRACE_DATABASE_LIFECYCLE) {
			JNIUtils.trace("All Databases are discarded");
		}
    	for(Map.Entry<String,Database> e: databases.entrySet()) {
    		closeDatabase(e.getValue());
    	}
    	databases.clear(); // Just in case of...
    }
    public synchronized void closeDatabase(Database db) {
		databases.remove(db.getName());
		try {
			if(DomSQL.debug.TRACE_DATABASE_LIFECYCLE) {
				JNIUtils.trace("Closing Database {0}, handle {1}", db.getName(),db.getSqliteDb());
			}
			db.close();
		} catch(Throwable t) {
			t.printStackTrace();
		}
    }
    
    public Database getDatabase(String name) throws SQLException {
        return getDatabase(name,true);
    }
    
    public synchronized Database getDatabase(String name, boolean load) throws SQLException {
        Database db = databases.get(name);
        if(db!=null) {
        	// Seems to be an issue when opening the DB
        	//db.refreshIfNecessary();
        	db.updateLastAccess();
        	return db;
        }
        if(load) {
			if(DomSQL.debug.TRACE_DATABASE_LIFECYCLE) {
				JNIUtils.trace("Creating SQLite Database {0}",name);
			}
        	db = createDatabase(name);
        	databases.put(db.getName(), db);
			if(cleaningJob == null && Constants.TIMEOUT_JOB_DELAY > 0) {
				// Start an Eclipse job that is a Daemon for cleaning the database
				// Create a Job that is executed on a regular basis
				cleaningJob = new Job("DomSQL cleaning thread") { // $NON-NLS-1$
					protected IStatus run(IProgressMonitor monitor) {
						try {
								if(DomSQL.debug.TRACE_DATABASE_LIFECYCLE) {
									System.out.println("DomSQL: cleaning databases job");
								}
							List<Database> dbList = checkTimeout();
							if(dbList!=null) {
								for(Database db: dbList) {
									closeDatabase(db);
								}
							}
							if(!databases.isEmpty()) {
									schedule(Constants.TIMEOUT_JOB_DELAY);
								}
						} catch(Throwable ex) {
						}
						return Status.OK_STATUS;
					}
				};
				cleaningJob.setPriority(Job.BUILD); // Low priority

				// Must be in a privileged block when called from XPages
				AccessController.doPrivileged(new PrivilegedAction<Void>() {
					public Void run() {
						cleaningJob.schedule(Constants.TIMEOUT_JOB_DELAY);
						return null;
					}
				});
			}
        }
        return db;
    }
    
    
    // ============================================================================
    // Database Creation
    // ============================================================================
    
    private static Database createDatabase(String name) throws SQLException {
        // the database name is composed like
        //   server!!databasepath.nsf/database
        // We decompose the parts here
        String url = "jdbc:domsql:"+name;
        int nsfPos = name.indexOf(".nsf/");
        if(nsfPos<0) {
            throw new SQLException("The database does not point to an NSF");
        }
        String nsfPath = name.substring(0,nsfPos+4);
        if(nsfPath.length()<=4) {
            throw new SQLException("The database name (path to nsf) is invalid");
        }
        String domSqlName = name.substring(nsfPos+5);
        if(StringUtil.isEmpty(domSqlName)) {
        	throw new SQLException("Empty DomSQL database name (design element) is not supported");
        }

        // Create the java wrapper and initialize it
        Database db = new Database(name, url, nsfPath, domSqlName);
        db.refresh();
        
        return db;
    }
}
