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
package com.ibm.domino.domsql;

import java.sql.Driver;
import java.sql.SQLException;

import com.ibm.commons.jdbc.drivers.IJDBCDriverAlias;
import com.ibm.commons.jdbc.drivers.JDBCProvider;



/**
 * JDBC Provider for the DomSQL class. 
 * 
 * @author priand
 */
public class DomSQLDriverProvider implements JDBCProvider {
    
    public DomSQLDriverProvider() {
    }

    public Driver loadDriver(String className) throws SQLException {
		if(className.equals(DomSQLDriver.class.getName())) {
			return new DomSQLDriver();
		}
		return null;
	}
	
	public IJDBCDriverAlias[] getDriverAliases() {
		// No aliases provided...
		return null;
	}
}
