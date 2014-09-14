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

import java.sql.SQLException;

import com.ibm.commons.util.StringUtil;


/**
 * @author priand
 */
public class DomSQLException {

	public static SQLException create(Throwable t, String message) {
		SQLException e = new SQLException(message);
		if(t!=null) {
            e.initCause(t);
		}
		return e;
	}

	public static SQLException create(Throwable t, String message, Object... params) {
        SQLException e = new SQLException(StringUtil.format(message, params));
        if(t!=null) {
            e.initCause(t);
        }
        return e;
	}

	public static void setSqliteCode(SQLException exception, int sqliteCode) {
		//this.sqliteCode = sqliteCode;
	}
}
