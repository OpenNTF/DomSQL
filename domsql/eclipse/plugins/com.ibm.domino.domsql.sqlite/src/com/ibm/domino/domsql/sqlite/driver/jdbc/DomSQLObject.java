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
package com.ibm.domino.domsql.sqlite.driver.jdbc;

import java.sql.SQLException;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.domsql.sqlite.DomSQLException;
import com.ibm.domino.domsql.sqlite.driver.SQLite;

/**
 * Base class for a DomSQL object.
 * @author priand
 */
public abstract class DomSQLObject {
    
	private static int nextId;
	
	private int objectId;
    
	protected DomSQLObject() {
		this.objectId = ++nextId;
	}
	
	public int getObjectId() {
		return objectId;
	}
	
    protected abstract DomSQLConnection getConnection();

    public void updateLastAccess() {
    	getConnection().getDatabase().updateLastAccess();
    }
    
    public void throwex(int sqlCode) throws SQLException {
    	String msg = formatErrorMessage();
        SQLException ex = DomSQLException.create(null,msg);
        DomSQLException.setSqliteCode(ex,sqlCode);
        throw ex;
    }

    public void throwex() throws SQLException {
    	String msg = formatErrorMessage();
        throw DomSQLException.create(null,msg);
    }
    

	protected String formatErrorMessage() throws SQLException {
    	//int errCode = SQLite.errcode(getConnection().getSqliteDb());
    	int errCode = SQLite.extendederrcode(getConnection().getSqliteDb());
    	String message = SQLite.errmsg(getConnection().getSqliteDb());
		String s = StringUtil.format("Code:{0}\n{1}", errCode, message);
		return s;
	}    
}
