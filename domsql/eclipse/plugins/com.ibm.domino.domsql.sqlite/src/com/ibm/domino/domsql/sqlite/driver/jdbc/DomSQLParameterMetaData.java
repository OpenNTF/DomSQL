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

import java.sql.ParameterMetaData;
import java.sql.SQLException;
import java.sql.Types;

public class DomSQLParameterMetaData extends DomSQLObject implements ParameterMetaData {

    private DomSQLPreparedStatement statement;

    DomSQLParameterMetaData(DomSQLPreparedStatement statement) {
        this.statement = statement;
    }

	@Override
	protected DomSQLConnection getConnection() {
		return statement.getConnection();
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

    
    public int getParameterCount() throws SQLException {
        statement.checkStatementOpen();
        return statement.getParameters().length;
    }

    public String getParameterClassName(int param) throws SQLException {
    	statement.checkStatementOpen();
        return "java.lang.String";
    }

    public String getParameterTypeName(int pos) {
        return "VARCHAR";
    }

    public int getParameterType(int pos) {
        return Types.VARCHAR;
    }

    public int getParameterMode(int pos) {
        return parameterModeIn;
    }

    public int getPrecision(int pos) {
        return 0;
    }

    public int getScale(int pos) {
        return 0;
    }

    public int isNullable(int pos) {
        return parameterNullable;
    }

    public boolean isSigned(int pos) {
        return true;
    }
}
