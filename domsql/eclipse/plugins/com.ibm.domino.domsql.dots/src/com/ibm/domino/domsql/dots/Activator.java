package com.ibm.domino.domsql.dots;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.ibm.domino.domsql.remote.server.rmi.RmiServer;
import com.ibm.domino.domsql.sqlite.DomSQLLocalDriver;
import com.ibm.domino.domsql.sqlite.driver.jni.DomSQL;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		DomSqlServer.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		DomSqlServer.stop();
		
		Activator.context = null;
	}

	//
	// We don't need a task per say
	//
	private static class DomSqlServer {
	    
	    public static final boolean ENABLED = true;
	    
	    public static synchronized void start() {
	        if(!ENABLED) {
	            return;
	        }
	        try {
	            System.out.println("Initializing DomSQL RMI server for Domino");
	            
            	DomSQLLocalDriver.initLocal();

	    		RmiServer.start();
	        } catch(Exception e) {
	            e.printStackTrace();
	        }    
	    }
	    public static synchronized void stop() {
	        try {
	    		RmiServer.stop();
	        } catch(Exception e) {
	            e.printStackTrace();
	        }    
	    }
	    
	    
	}
	
	
/*	
	private static class RmiJdbcServer {
	    
	    public static final boolean ENABLED = false;
	    
	    //private static final String DRIVER_NAME = DominoSqlLiteDriver.class.getName(); 

	    public static synchronized void start() {
	        if(!ENABLED) {
	            return;
	        }
	        try {
	            System.out.println("Initializing RMI-JDBC for Domino");
	            
	            DominoSqlLiteDriver.init();
	            
	            RJJdbcServer.main(new String[]{"com.ibm.domino.domsql.sqlite.DominoSqlLiteDriver"});
	        } catch(Exception e) {
	            e.printStackTrace();
	        }    
	    }
	}
*/	
	
}
