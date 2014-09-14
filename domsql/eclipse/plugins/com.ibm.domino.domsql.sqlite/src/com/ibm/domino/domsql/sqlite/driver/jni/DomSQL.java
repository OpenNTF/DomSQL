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
package com.ibm.domino.domsql.sqlite.driver.jni;

import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import com.ibm.domino.domsql.sqlite.driver.Context;
import com.ibm.domino.domsql.sqlite.driver.meta.ViewDesign;


/**
 * JNI class used to access the native DomSQL features.
 * 
 * @author priand
 */
public class DomSQL {
	
	public static final Debug debug = new Debug();
	
	// Used as a base class for bean as well
	public final static class Debug implements Serializable {
		private static final long serialVersionUID = 1L;

		public boolean TRACE_FILE;
		public String  TRACE_FILE_NAME;
		
		public boolean TRACE_JAVA_SQL;
	    public boolean TRACE_JAVA_REMOTE;

	    public boolean TRACE_DATABASE_LIFECYCLE;

	    public boolean NATIVE_TRACE;
	    public boolean NATIVE_TRACE_FUNCTION;
	    
	    public boolean NATIVE_DUMP_DESIGN;
	    public boolean NATIVE_DUMP_SECRETCODE;
	    public boolean NATIVE_TRACE_DICTIONARY;
	    public boolean NATIVE_TRACE_NIFCALLS;
	    public boolean NATIVE_TRACE_XFILTER;
	    public boolean NATIVE_TRACE_XNEXT;
	    public boolean NATIVE_TRACE_READDATA;
	    public boolean NATIVE_TRACE_FINDBYKEY;
	    public boolean NATIVE_TRACE_BESTINDEX;
	    public boolean NATIVE_TRACE_PERFORMANCE_HINTS;

	    public boolean NATIVE_TRACE_EOF;
	    
		private Debug() {
            if(false) {
            	TRACE_FILE = true;

//	    		TRACE_JAVA_SQL = true;
//	    		TRACE_JAVA_REMOTE = true;
//            	TRACE_DATABASE_LIFECYCLE = true;
//
//	    		NATIVE_TRACE = true;
//	    		NATIVE_TRACE_FUNCTION = true;
//	    	    
//	    		NATIVE_DUMP_DESIGN = true;
//	    		NATIVE_DUMP_SECRETCODE = true;
//	    		NATIVE_TRACE_DICTIONARY = true;
//	    		NATIVE_TRACE_NIFCALLS = true;
//	    		NATIVE_TRACE_XFILTER = true;
//	    		NATIVE_TRACE_XNEXT = true;
//	    		NATIVE_TRACE_READDATA = true;
//	    		NATIVE_TRACE_FINDBYKEY = true;
//	    		NATIVE_TRACE_BESTINDEX = true;
            	NATIVE_TRACE_PERFORMANCE_HINTS = true;
//
//	    		NATIVE_TRACE_EOF = true;
            }
            
	    }
		
	    public void updateNative() {
	    	setDebugFlag("TRACE", NATIVE_TRACE);
	    	setDebugFlag("TRACE_FUNCTION", NATIVE_TRACE_FUNCTION);
	    	setDebugFlag("DUMP_DESIGN", NATIVE_DUMP_DESIGN);
	    	setDebugFlag("DUMP_SECRETCODE", NATIVE_DUMP_SECRETCODE);
	    	setDebugFlag("TRACE_DICTIONARY", NATIVE_TRACE_DICTIONARY);
	    	setDebugFlag("TRACE_NIFCALLS", NATIVE_TRACE_NIFCALLS);
	    	setDebugFlag("TRACE_XFILTER", NATIVE_TRACE_XFILTER);
	    	setDebugFlag("TRACE_XNEXT", NATIVE_TRACE_XNEXT);
	    	setDebugFlag("TRACE_READDATA", NATIVE_TRACE_READDATA);
	    	setDebugFlag("TRACE_FINDBYKEY", NATIVE_TRACE_FINDBYKEY);
	    	setDebugFlag("TRACE_BESTINDEX", NATIVE_TRACE_BESTINDEX);
	    	setDebugFlag("TRACE_PERFORMANCE_HINTS", NATIVE_TRACE_PERFORMANCE_HINTS);
	    	setDebugFlag("TRACE_EOF", NATIVE_TRACE_EOF);
	    }
	}
	
	
    // ================================================================================
    // Initialization
    // ================================================================================

    // Set the value of the debug flag in the native layer
    public static native void setDebugFlag(String name, boolean value);
    
    // Virtual table secret code
    // This is used to prevent random creation of DomSQL virtual code
    public static native void setSecretCode(int secretCode);

    
    // ================================================================================
    // Call the native layer
    // ================================================================================
    
    public static native void readViewList(long hDB, List list) throws SQLException;
    public static native ViewDesign readViewDesign(int noteID, long hNote) throws SQLException;
    public static native int findDomsql(long hDB, String name) throws SQLException;
    public static native String readItemAsString(long hDB, String itemName) throws SQLException;    
    
    // ================================================================================
    // Callback functions for the JNI layer
    // We made these functions as simple as possible to call from the "C" code
    // ================================================================================
    
    public static final int VIEW_COL_UNKNOWN    = -1;
    public static final int VIEW_COL_NUMBER     = 0;
    public static final int VIEW_COL_TIMEDATE   = 1;
    public static final int VIEW_COL_TEXT       = 2;

    // Access the runtime context
    public static long getDatabaseHandle(String databasePath) throws SQLException {
        Context ctx = Context.get();
        return ctx.getDBHandle(databasePath);
    }
    
    // Handling design elements
    public static Object createView(int noteID, String title) {
        return new ViewDesign(noteID,title);
    }
    public static Object createViewColumn(Object parent, String name, String title, int type, int summaryIndex) {
        ViewDesign.Column col =  new ViewDesign.Column(name, title, type, summaryIndex);
        ((ViewDesign)parent).getColumns().add(col);
        return col;
    }
    public static Object createViewIndex(Object parent, int collation) {
        ViewDesign.Index idx =  new ViewDesign.Index(collation);
        ((ViewDesign)parent).getIndexes().add(idx);
        return idx;
    }
    public static Object createViewIndexEntry(Object parent, String colName, boolean desc) {
        ViewDesign.IndexEntry ent =  new ViewDesign.IndexEntry(colName,desc);
        ((ViewDesign.Index)parent).getEntries().add(ent);
        return ent;
    }

    
    // ================================================================================
    // Data handling
    // ================================================================================
    
    public static Long toSQLiteDate(java.util.Date value) {
        return Long.valueOf(((java.util.Date) value).getTime())/1000;
    }
    public static Long toSQLiteDate(Date value) {
        return Long.valueOf(((Date) value).getTime())/1000;
    }
    public static Long toSQLiteDate(Time value) {
        return Long.valueOf(((Time) value).getTime())/1000;
    }
    public static Long toSQLiteDate(Timestamp value) {
        return Long.valueOf(((Timestamp) value).getTime())/1000;
    }
}
