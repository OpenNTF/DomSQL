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

import java.lang.Thread.UncaughtExceptionHandler;
import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;
import java.sql.Connection;
import java.sql.Date;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.management.remote.rmi.RMIServer;

import com.ibm.commons.util.QuickSort;
import com.ibm.commons.util.StringUtil;
import com.ibm.domino.domsql.DomSQLDriver;
import com.ibm.domino.domsql.remote.server.rmi.RmiServer;
import com.ibm.domino.domsql.remote.transport.IResultSetMetaData.ColumnDef;
import com.ibm.domino.domsql.remote.transport.SArray;
import com.ibm.domino.domsql.remote.transport.SBlob;
import com.ibm.domino.domsql.remote.transport.SClob;
import com.ibm.domino.domsql.remote.transport.SInputStream;
import com.ibm.domino.domsql.remote.transport.SNClob;
import com.ibm.domino.domsql.remote.transport.SReader;
import com.ibm.domino.domsql.remote.transport.SRef;
import com.ibm.domino.domsql.remote.transport.SRowId;
import com.ibm.domino.domsql.remote.transport.SSQLXML;
import com.ibm.domino.domsql.remote.transport.SSavepoint;
import com.ibm.domino.domsql.remote.transport.SStruct;
import com.ibm.domino.domsql.sqlite.driver.Context;
import com.ibm.domino.domsql.sqlite.driver.jdbc.DomSQLCallableStatement;
import com.ibm.domino.domsql.sqlite.driver.jdbc.DomSQLConnection;
import com.ibm.domino.domsql.sqlite.driver.jdbc.DomSQLDatabaseMetaData;
import com.ibm.domino.domsql.sqlite.driver.jdbc.DomSQLParameterMetaData;
import com.ibm.domino.domsql.sqlite.driver.jdbc.DomSQLPreparedStatement;
import com.ibm.domino.domsql.sqlite.driver.jdbc.DomSQLResultSet;
import com.ibm.domino.domsql.sqlite.driver.jdbc.DomSQLResultSetMetaData;
import com.ibm.domino.domsql.sqlite.driver.jdbc.DomSQLStatement;
import com.ibm.domino.domsql.sqlite.driver.jni.DomSQL;
import com.ibm.domino.domsql.sqlite.driver.jni.JNIUtils;
import com.ibm.dots.session.SessionContext;


/**
 *
 */
public abstract class ServerObject extends UnicastRemoteObject implements Unreferenced {

    private static final boolean TRACK_REMOTE_CALLS	= false;
    
	private static final long serialVersionUID = 1L;
	
	private ServerConnection connection;

	// Debug...
	private UncaughtExceptionHandler oldExceptionHandler;
    
    public ServerObject(ServerConnection connection) throws RemoteException {
    	// TODO: pass the socket factory to support SSL
    	super(RmiServer.getPort());
    	//super(RmiServer.getPort(), RmiServer.rmiClientSocketFactory, RmiServer.rmiServerSocketFactory);
        this.connection = connection;
    }
    
	public void unreferenced() {
		// Nothing by default...
	}

    public ServerConnection getRemoteConnection() {
        return connection;
    }
    
    public void initContext() {
        if(TRACK_REMOTE_CALLS) {
        	startTrackMethod();
        }
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
            getRemoteConnection().setReturnValue(null);
            getRemoteConnection().setOldUncaughtExceptionHandler( Thread.currentThread().getUncaughtExceptionHandler());
            Thread.currentThread().setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				public void uncaughtException(Thread thread, Throwable throwable) {
					throwable.printStackTrace();
				}
			});
        }
        SessionContext sc = getRemoteConnection().getSessionContext();
        if(sc!=null) {
        	sc.initContext();
        	Context.push(getRemoteConnection().getContext());
        }
    }
    
    public void termContext() {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	Thread.currentThread().setUncaughtExceptionHandler(getRemoteConnection().getOldUncaughtExceptionHandler());
            getRemoteConnection().setOldUncaughtExceptionHandler(null);

        	Thread.currentThread().setUncaughtExceptionHandler(oldExceptionHandler);
            StackTraceElement[] elts =  Thread.currentThread().getStackTrace();
            StackTraceElement elt = elts[3];
            String className = elt.getClassName();
            String methodName = elt.getMethodName();
            if(acceptTraceMethod(elt, className, methodName)) {
                long id = Thread.currentThread().getId();
            	Object returnValue = getRemoteConnection().getReturnValue();
            	JNIUtils.println("      termContext["+id+"], "+className+"."+methodName+", Result="+getValueAsString(returnValue));
            }
        }
        SessionContext sc = getRemoteConnection().getSessionContext();
        if(sc!=null) {
            sc.termContext();
            Context.pop();
        }
        if(TRACK_REMOTE_CALLS) {
        	endTrackMethod();
        }
    }
    protected boolean acceptTraceMethod(StackTraceElement elt, String className, String methodName) {
    	if(methodName.equals("_next")) {
    		return false;
    	}
    	return true;
    }
    
    protected String getValueAsString(Object o) {
    	if(o==null) {
    		return StringUtil.toString(o);    	
    	}
    	if(   o instanceof String
    	   || o instanceof Boolean
    	   || o instanceof java.util.Date
     	   || o instanceof Number) {
        	return o.toString()+" ["+o.getClass().getSimpleName()+"]";
    	}
    	if(   o instanceof byte[]
    	   || o instanceof int[] ) {
        	return StringUtil.toString(o)+" ["+o.getClass().getSimpleName()+"]";
    	}
    	return "["+o.getClass().getName()+"]";
    }

    public final Object wrap(Object o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    
    public final boolean wrap(boolean o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final char wrap(char o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final byte wrap(byte o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final short wrap(short o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final int wrap(int o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final long wrap(long o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final float wrap(float o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final double wrap(double o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    
    public final String wrap(String o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final byte[] wrap(byte[] o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final int[] wrap(int[] o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final BigDecimal wrap(BigDecimal o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final Date wrap(Date o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final Time wrap(Time o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final Timestamp wrap(Timestamp o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final URL wrap(URL o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }

    public final Connection wrap(DomSQLConnection o) throws RemoteException {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final ServerDatabaseMetaData wrap(DomSQLDatabaseMetaData o) throws RemoteException {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return new ServerDatabaseMetaData(getRemoteConnection(),o);
    }
    public final ServerResultSet wrap(DomSQLResultSet o) throws RemoteException {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return new ServerResultSet(getRemoteConnection(),o);
    }
    public final ServerResultSetMetaData wrap(DomSQLResultSetMetaData o) throws RemoteException {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return new ServerResultSetMetaData(getRemoteConnection(),o);
    }
    public final ServerParameterMetaData wrap(DomSQLParameterMetaData o) throws RemoteException {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return new ServerParameterMetaData(getRemoteConnection(),o);
    }
    public final ServerStatement wrap(DomSQLStatement o) throws RemoteException {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return new ServerStatement(getRemoteConnection(),o);
    }
    public final ServerPreparedStatement wrap(DomSQLPreparedStatement o) throws RemoteException {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return new ServerPreparedStatement(getRemoteConnection(),o);
    }
    public final ServerCallableStatement wrap(DomSQLCallableStatement o) throws RemoteException {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return new ServerCallableStatement(getRemoteConnection(),o);
    }

    public final SBlob wrap(SBlob o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final SClob wrap(SClob o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final SNClob wrap(SNClob o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final SSavepoint wrap(SSavepoint o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final SSQLXML wrap(SSQLXML o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final SArray wrap(SArray o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final SStruct wrap(SStruct o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final SRowId wrap(SRowId o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final SRef wrap(SRef o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final SInputStream wrap(SInputStream o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final SReader wrap(SReader o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final SQLWarning wrap(SQLWarning o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final RowIdLifetime wrap(RowIdLifetime o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }

    public final ColumnDef wrap(ColumnDef o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final ColumnDef[] wrap(ColumnDef[] o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    
    public final Map<String, Class<?>> wrap(Map<String, Class<?>> o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    public final Properties wrap(Properties o) {
        if(DomSQL.debug.TRACE_JAVA_REMOTE) {
        	getRemoteConnection().setReturnValue(o);
        }
        return o;
    }
    
    
    protected SQLException notImplementedException() throws SQLException {
    	return new SQLException("Method not implemented");
    }

    
    //
    //
    // Remote calls tracking for internal performance measurement
    //
    //	

    private static Long totalCount = 0L;
    private static HashMap<String,Integer> methods = new HashMap<String,Integer>();
    
    
    protected static void startTrackMethod() {
    	synchronized(ServerObject.class) {
	        String name = Thread.currentThread().getStackTrace()[4].getMethodName();
	        Integer l = methods.get(name);
	        int count = l!=null ? (int)l : 0;
	        methods.put(name,Integer.valueOf(count+1));
	        totalCount++;
    	}
    	if((totalCount%100)==0) {
    		JNIUtils.println("Method count="+totalCount);
    	}
    	if((totalCount%1000)==0) {
    		dumpMethods();
    	}
    }

    protected static void endTrackMethod() {
    	
    }
    
    private static void dumpMethods() {
		ArrayList<Map.Entry<String,Integer>> l = null;
    	synchronized(ServerObject.class) {
    		l = new ArrayList<Map.Entry<String,Integer>>(methods.size());
    		l.addAll(methods.entrySet());
    	}
		(new QuickSort.JavaList(l) {
			@Override
			public int compare(Object o1, Object o2) {
				Map.Entry<String,Integer> e1 = (Map.Entry<String,Integer>)o1;
				Map.Entry<String,Integer> e2 = (Map.Entry<String,Integer>)o2;
				return e2.getValue()-e1.getValue();
			}
		}).sort();
		JNIUtils.println("***** Method Count="+totalCount);
		for(int i=0; i<l.size(); i++) {
			Map.Entry<String,Integer> e = l.get(i);
			JNIUtils.println(e.getValue()+": "+e.getKey());
		}
    }
    
}
