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
package com.ibm.domino.domsql.remote.transport;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.SQLException;


/**
 *
 */
public interface IResultSetMetaData extends IRemoteObject {
	
	// JDBC ResultSetMetaData methods
	public String getCatalogName(int column) throws SQLException, RemoteException;
	public String getColumnClassName(int column) throws SQLException, RemoteException;
	public int getColumnCount() throws SQLException, RemoteException;
	public int getColumnDisplaySize(int column) throws SQLException, RemoteException;
	public String getColumnLabel(int column) throws SQLException, RemoteException;
	public String getColumnName(int column) throws SQLException, RemoteException;
	public int getColumnType(int column) throws SQLException, RemoteException;
	public String getColumnTypeName(int column) throws SQLException, RemoteException;
	public int getPrecision(int column) throws SQLException, RemoteException;
	public int getScale(int column) throws SQLException, RemoteException;
	public String getSchemaName(int column) throws SQLException, RemoteException;
	public String getTableName(int column) throws SQLException, RemoteException;
	public boolean isAutoIncrement(int column) throws SQLException, RemoteException;
	public boolean isCaseSensitive(int column) throws SQLException, RemoteException;
	public boolean isCurrency(int column) throws SQLException, RemoteException;
	public boolean isDefinitelyWritable(int column) throws SQLException, RemoteException;
	public int isNullable(int column) throws SQLException, RemoteException;
	public boolean isReadOnly(int column) throws SQLException, RemoteException;
	public boolean isSearchable(int column) throws SQLException, RemoteException;
	public boolean isSigned(int column) throws SQLException, RemoteException;
	public boolean isWritable(int column) throws SQLException, RemoteException;

	// Extra methods
	public static class ColumnDef implements Serializable {
		private static final long serialVersionUID = 1L;
		public String		catalogName;
		public String		columnClassName;
		public int			columnDisplaySize;
		public String		columnLabel;
		public String		columnName;
		public int			columnType;
		public String		columnTypeName;
		public int			precision;
		public int			scale;
		public String		schemaName;
		public String		tableName;
		public boolean		autoIncrement;
		public boolean		caseSensitive;
		public boolean		currency;
		public boolean		definitivelyWritable;
		public int			nullable;
		public boolean		readOnly;
		public boolean		searchable;
		public boolean		signed;
		public boolean		writable;
	}
	public ColumnDef[] _getColumnDefs() throws SQLException, RemoteException;
}
