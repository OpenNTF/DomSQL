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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.domsql.sqlite.DomSQLException;

/**
 * Quick JNI utilities.
 * 
 * @author priand
 */
public class JNIUtils {
	
	private static PrintWriter fileWriter;
	private static PrintWriter getPrintWriter() {
		if(fileWriter==null) {
			try {
				String fileName = DomSQL.debug.TRACE_FILE_NAME;
				if(StringUtil.isEmpty(fileName)) {
					fileName = "c:\\temp\\domsql.log";
				}
				fileWriter = new PrintWriter(new FileWriter(fileName));
			} catch(IOException ex) {
			}
		}
		return fileWriter;
	}
	
	private static boolean beginLine = true;
	
    public static void flush() {
    	if(DomSQL.debug.TRACE_FILE) {
    		PrintWriter pw = getPrintWriter();
    		if(pw!=null) {
    			pw.flush();
    			return;
    		}
    	}
        System.err.flush();
    }

    public static void print(String s) {
    	if(DomSQL.debug.TRACE_FILE) {
    		PrintWriter pw = getPrintWriter();
    		if(pw!=null) {
    			pw.print(format(s));
    			pw.flush();
    			beginLine = false;
    			return;
    		}
    	}
        System.err.print(format(s));
		beginLine = false;
    }

    public static void println(String s) {
    	if(DomSQL.debug.TRACE_FILE) {
    		PrintWriter pw = getPrintWriter();
    		if(pw!=null) {
    			pw.println(format(s));
    			pw.flush();
    			beginLine = true;
    			return;
    		}
    	}
        System.err.println(format(s));
		beginLine = true;
    }

    public static void trace(String s, Object...params) {
        println(format(StringUtil.format(s, params)));
    }
    
    public static void _flush() {
        flush();
    }
    
    public static void _print(String s) {
        print("Native: "+s);
    }

    public static void _println(String s) {
        println("Native: "+s);
    }

    
    public static Object createException(String msg) {
        return DomSQLException.create(null,msg);
    }

    @SuppressWarnings("unchecked")
    public static void addObjectToList(List list, Object v) {
        list.add(v);
    }    
    @SuppressWarnings("unchecked")
    public static void addIntToList(List list, int v) {
        list.add(v);
    }    
    @SuppressWarnings("unchecked")
    public static void addLongToList(List list, long v) {
        list.add(v);
    }
    
    
    private static DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
    private static String format(String s) {
    	if(beginLine) {
    		return df.format(new Date())+": "+s;
    	} else {
    		return s;
    	}
    }
}
