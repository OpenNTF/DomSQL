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
package com.ibm.domino.domsql.remote.server.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.domsql.DomSQLDriver;
import com.ibm.domino.domsql.remote.server.ServerDomSQLDriver;
import com.ibm.domino.domsql.remote.transport.IDomSQLDriver;
import com.ibm.domino.domsql.sqlite.driver.Constants;
import com.ibm.domino.domsql.sqlite.driver.DatabaseFactory;
import com.ibm.domino.domsql.sqlite.driver.jni.NotesAPI;


/**
 *
 */
public class RmiServer {
	
	// For specific use...
	public static RMIClientSocketFactory clientSocketFactory = new ClientSocketFactory();
	public static RMIServerSocketFactory serverSocketFactory = new ServerSocketFactory();
	
	
	private static int port = DomSQLDriver.DEFAULT_PORT; 
	private static ServerDomSQLDriver driver;
	private static Registry registry;
	
	private RmiServer() {}

    public static boolean isStarted() {
    	return registry!=null;
    }
    
    public static int getPort() {
    	return port;
    }
	
    public static void start() throws Exception {
    	if(!isStarted()) {
    		try {
	    		port = NotesAPI.OSGetEnvironmentInt(Constants.NOTESINI_DOMSQLPORT);
	    		if(port<=0) {
	    			port = DomSQLDriver.DEFAULT_PORT;
	    		}
		        System.out.println(StringUtil.format("Starting DomSQL RMI server, port: {0}",port));
		    	driver = new ServerDomSQLDriver();
		    	try {
		    		registry = LocateRegistry.createRegistry(port);
		    	} catch(RemoteException ex) {
		    		//registry = LocateRegistry.getRegistry(port);
		    	}
		    	IDomSQLDriver stub = (IDomSQLDriver) UnicastRemoteObject.exportObject(driver, port);
		        registry.rebind(DomSQLDriver.DOMSQL_FACTORY, stub);
		        System.out.println(StringUtil.format("    DomSQL RMI server started, port: {0}",port));
        	} catch(Throwable t) {
    	        System.out.println(StringUtil.format("!! Error while starting the DomSQL server"));
        		t.printStackTrace();
        	}
    	}
    }

    public static void stop() throws Exception {
    	try {
	        System.out.println(StringUtil.format("Stopping DomSQL RMI server"));
	        
	        // We should delete all the databases
	        DatabaseFactory.getDatabaseFactory().closeAllDatabases();
	        
	        // And we unbind the server
	    	registry.unbind(DomSQLDriver.DOMSQL_FACTORY); 
	    	//registry.unbind(new ObjID(ObjID.REGISTRY_ID)); 
	    	UnicastRemoteObject.unexportObject(driver,true); 
	    	UnicastRemoteObject.unexportObject(registry,true); 
	        System.out.println(StringUtil.format("    DomSQL RMI server stopped"));
    	} catch(Throwable t) {
	        System.out.println(StringUtil.format("!! Error while stopping the DomSQL server"));
    		t.printStackTrace();
    	}
    }
}
