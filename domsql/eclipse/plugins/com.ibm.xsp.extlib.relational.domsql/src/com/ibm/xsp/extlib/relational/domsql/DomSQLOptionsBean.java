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
package com.ibm.xsp.extlib.relational.domsql;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.commons.Platform;
import com.ibm.commons.util.QuickSort;
import com.ibm.commons.util.StringUtil;
import com.ibm.domino.domsql.sqlite.driver.Database;
import com.ibm.domino.domsql.sqlite.driver.jdbc.DomSQLConnection;
import com.ibm.domino.domsql.sqlite.driver.meta.DatabaseDefinition;
import com.ibm.xsp.model.DataObject;



/**
 * Simple bean used to manage the DomSQL options. 
 * @author priand
 */
public class DomSQLOptionsBean implements DataObject, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static Class<?> debugClass;
	private static Object debugObject;
	
	private static List<String> optionsList;
	static {
		optionsList = new ArrayList<String>();
		try {
			debugObject = Class.forName("com.ibm.domino.domsql.sqlite.driver.jni.DomSQL").getField("debug").get(null);
			debugClass = debugObject.getClass();
			
			Field[] fields = debugClass.getFields();
			for(int i=0; i<fields.length; i++) {
				Field f = fields[i];
				if(accept(f)) {
					optionsList.add(f.getName());
				}
			}
			sortFields(optionsList);
		} catch(Throwable ex) {
			Platform.getInstance().log(ex); // Should never happen!
		}
	}
	private static boolean accept(Field f) {
		return true;
	}
	private static void sortFields(List<String> fields) {
		(new QuickSort.JavaList(fields)).sort();
	}

	private Map<String,Boolean>	options = new HashMap<String,Boolean>();
	
	public DomSQLOptionsBean() {
		for(int i=0; i<optionsList.size(); i++) {
			try {
				String name = optionsList.get(i);
				Field f = debugClass.getField((String)name);
				Boolean b = f.getBoolean(debugObject);
				options.put(name, b);
			} catch(Throwable ex) {
				Platform.getInstance().log(ex); // Should never happen!
			}
		}
	}
	
	public void save() {
		for(int i=0; i<optionsList.size(); i++) {
			try {
				String name = optionsList.get(i);
				Field f = debugClass.getField((String)name);
				Boolean b = options.get(name);
				if(b!=null) {
					f.setBoolean(debugObject,b);
				}
			} catch(Throwable ex) {
				Platform.getInstance().log(ex); // Should never happen!
			}
		}
		try {
			debugClass.getMethod("updateNative").invoke(debugObject);
		} catch(Throwable ex) {
			Platform.getInstance().log(ex); // Should never happen!
		}
	}

	// Dynamic access to the options
	public List<String> getOptions() {
		return optionsList;
	}
	
	// Data object
    public Class<?> getType(Object arg0) {
		return Boolean.TYPE;
	}

	public boolean isReadOnly(Object arg0) {
		return false;
	}

	public Object getValue(Object name) {
		return options.get(name);
	}

	public void setValue(Object name, Object value) {
		if(value instanceof String) {
			value = value.equals("true");
		}
		if(value instanceof Boolean) {
			options.put((String)name,(Boolean)value);
		}
	}
	
//	//
//	// Debug utilities
//	//
//	public String getInitializationSQL(Connection c) {
//		DomSQLConnection dc = getDomSQLConnection(c);
//		if(c!=null) {
//			try {
//				Database db = dc.getDatabase();
//				List<String> sql = db.generateInitSql();
//				StringBuilder b = new StringBuilder();
//				for(String s: sql) {
//					b.append(s);
//					b.append("\n");
//				}
//				return b.toString();
//			} catch(SQLException ex) {
//				Platform.getInstance().log(ex);
//				return StringUtil.format("SqlException {0}",ex.toString());
//			}
//		}
//		return StringUtil.format("The Connection {0} is not a DomSQL one", c!=null ? c.getClass() : "<null>");
//	}
//	private DomSQLConnection getDomSQLConnection(Connection c) {
//		if(c==null) {
//			return null;
//		}
//		if(c instanceof DomSQLConnection) {
//			return (DomSQLConnection)c;
//		}
//		try {
//			Method m = c.getClass().getMethod("getConnection");
//			return getDomSQLConnection((Connection)m.invoke(c));
//		} catch(Exception e) {
//		}
//		return null;
//	}
}
