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

import java.sql.SQLException;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.domsql.sqlite.DomSQLException;



/**
 * Encapsulate a runtime context for accessing the data 
 * 
 * @author priand
 */
public class DefaultContext extends Context {

	private String defaultDbName;
	private long handle;
	
    public DefaultContext() {
    }

    public String getDefaultDbName() {
        return defaultDbName;
    }

    public void setDefaultDbName(String defaultDbName) {
        this.defaultDbName = defaultDbName;
    }

    public long getHandle() {
        return handle;
    }

    public void setHandle(long handle) {
        this.handle = handle;
    }

    protected boolean shouldClose(long hDb) {
        return hDb!=handle;
    }

    protected long getDefaultDBHandle() throws SQLException {
    	if(handle!=0) {
    		return handle;
    	}
    	if(StringUtil.isNotEmpty(defaultDbName)) {
    		return openDBHandle(defaultDbName);
    	}
    	throw DomSQLException.create(null, "No default database handle available");
    }
}
