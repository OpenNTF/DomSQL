/*
 * © Copyright IBM Corp. 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); } finally { termContext(); } 
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

import java.rmi.RemoteException;
import java.sql.SQLException;

import com.ibm.domino.domsql.remote.transport.IResultSetMetaData;
import com.ibm.domino.domsql.sqlite.driver.jdbc.DomSQLResultSetMetaData;

public class ServerResultSetMetaData extends ServerObject implements IResultSetMetaData {

	private static final long serialVersionUID = 1L;
	
    private DomSQLResultSetMetaData metaData;

    public ServerResultSetMetaData(ServerConnection connection, DomSQLResultSetMetaData metaData) throws RemoteException {
        super(connection);
        this.metaData = metaData;
    }
    
    public DomSQLResultSetMetaData getNative() {
    	return metaData;
    }

    public void unreferenced() {
    	metaData = null;
    }

    // ===========================================================
    // Delegation
    // ===========================================================

    public <T> T unwrap(Class<T> iface) throws SQLException, RemoteException {
        return metaData.unwrap(iface);
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException, RemoteException {
        return metaData.isWrapperFor(iface);
    }

	public String getCatalogName(int column) throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.getCatalogName(column));
		} finally {
			termContext();
		}
	}

	public String getColumnClassName(int column) throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.getColumnClassName(column));
		} finally {
			termContext();
		}
	}

	public int getColumnCount() throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.getColumnCount());
		} finally {
			termContext();
		}
	}

	public int getColumnDisplaySize(int column) throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.getColumnDisplaySize(column));
		} finally {
			termContext();
		}
	}

	public String getColumnLabel(int column) throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.getColumnLabel(column));
		} finally {
			termContext();
		}
	}

	public String getColumnName(int column) throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.getColumnName(column));
		} finally {
			termContext();
		}
	}

	public int getColumnType(int column) throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.getColumnType(column));
		} finally {
			termContext();
		}
	}

	public String getColumnTypeName(int column) throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.getColumnTypeName(column));
		} finally {
			termContext();
		}
	}

	public int getPrecision(int column) throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.getPrecision(column));
		} finally {
			termContext();
		}
	}

	public int getScale(int column) throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.getScale(column));
		} finally {
			termContext();
		}
	}

	public String getSchemaName(int column) throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.getSchemaName(column));
		} finally {
			termContext();
		}
	}

	public String getTableName(int column) throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.getTableName(column));
		} finally {
			termContext();
		}
	}

	public boolean isAutoIncrement(int column) throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.isAutoIncrement(column));
		} finally {
			termContext();
		}
	}

	public boolean isCaseSensitive(int column) throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.isCaseSensitive(column));
		} finally {
			termContext();
		}
	}

	public boolean isCurrency(int column) throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.isCurrency(column));
		} finally {
			termContext();
		}
	}

	public boolean isDefinitelyWritable(int column) throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.isDefinitelyWritable(column));
		} finally {
			termContext();
		}
	}

	public int isNullable(int column) throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.isNullable(column));
		} finally {
			termContext();
		}
	}

	public boolean isReadOnly(int column) throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.isReadOnly(column));
		} finally {
			termContext();
		}
	}

	public boolean isSearchable(int column) throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.isSearchable(column));
		} finally {
			termContext();
		}
	}

	public boolean isSigned(int column) throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.isSigned(column));
		} finally {
			termContext();
		}
	}

	public boolean isWritable(int column) throws SQLException, RemoteException {
		initContext();
		try {
			return wrap(metaData.isWritable(column));
		} finally {
			termContext();
		}
	}

    
    // ===========================================================
    // Extra methods
    // ===========================================================
    
	public ColumnDef[] _getColumnDefs() throws SQLException, RemoteException {
		initContext();
		try {
			int count = metaData.getColumnCount();
			ColumnDef[] colDefs = new ColumnDef[count];
			for(int i=0; i<count; i++) {
				int colIdx = i+1; //JDBC is one based....
				ColumnDef d = colDefs[i] = new ColumnDef();
				d.catalogName = metaData.getCatalogName(colIdx);
				d.columnClassName = metaData.getColumnClassName(colIdx);
				d.columnDisplaySize = metaData.getColumnDisplaySize(colIdx);
				d.columnLabel = metaData.getColumnLabel(colIdx);
				d.columnName = metaData.getColumnName(colIdx);
				d.columnType = metaData.getColumnType(colIdx);
				d.columnTypeName = metaData.getColumnTypeName(colIdx);
				d.precision = metaData.getPrecision(colIdx);
				d.scale = metaData.getScale(colIdx);
				d.schemaName = metaData.getSchemaName(colIdx);
				d.tableName = metaData.getTableName(colIdx);
				d.autoIncrement = metaData.isAutoIncrement(colIdx);
				d.caseSensitive = metaData.isCaseSensitive(colIdx);
				d.currency = metaData.isCurrency(colIdx);
				d.definitivelyWritable = metaData.isDefinitelyWritable(colIdx);
				d.nullable = metaData.isNullable(colIdx);
				d.readOnly = metaData.isReadOnly(colIdx);
				d.searchable = metaData.isSearchable(colIdx);
				d.signed = metaData.isSigned(colIdx);
				d.writable = metaData.isWritable(colIdx);
			}
			return wrap(colDefs);
		} finally {
			termContext();
		}
	}
}
