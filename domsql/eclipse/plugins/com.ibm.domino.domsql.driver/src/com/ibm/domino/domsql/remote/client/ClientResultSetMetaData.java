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
package com.ibm.domino.domsql.remote.client;

import java.rmi.RemoteException;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;

import com.ibm.domino.domsql.remote.transport.IResultSetMetaData;
import com.ibm.domino.domsql.remote.transport.ServerOptions;

public class ClientResultSetMetaData extends ClientObject implements ResultSetMetaData {

    private IResultSetMetaData metaData;

    
    // ColDef otimization
    //public static final boolean ServerOptions.options.OPTIMIZATION_COL_DEF	= true;
    private IResultSetMetaData.ColumnDef[] colDefs;
    
    public ClientResultSetMetaData(IResultSetMetaData metaData) {
    	super(metaData);
    	this.metaData = metaData;
    }
    
    public IResultSetMetaData getRemoteObject() {
    	return metaData;
    }

    public int findColumn(String col) throws SQLException {
		try {
    		IResultSetMetaData.ColumnDef[] colDefs = loadColDefs();
	        for(int i=0; i<colDefs.length; i++) {
	            if(col.equalsIgnoreCase(colDefs[i].columnName)) {
	                return i+1;
	            }
	        }
	        throw new SQLException(MessageFormat.format("Column {0} is unknown",col));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
    }

	public String getCatalogName(int column) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadOneColDef(column).catalogName;
			}
			return metaData.getCatalogName(column);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getColumnClassName(int column) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadOneColDef(column).columnClassName;
			}
			return metaData.getColumnClassName(column);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getColumnCount() throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadColDefs().length;
			}
			return metaData.getColumnCount();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getColumnDisplaySize(int column) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadOneColDef(column).columnDisplaySize;
			}
			return metaData.getColumnDisplaySize(column);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getColumnLabel(int column) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadOneColDef(column).columnLabel;
			}
			return metaData.getColumnLabel(column);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getColumnName(int column) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadOneColDef(column).columnName;
			}
			return metaData.getColumnName(column);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getColumnType(int column) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadOneColDef(column).columnType;
			}
			return metaData.getColumnType(column);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getColumnTypeName(int column) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadOneColDef(column).columnTypeName;
			}
			return metaData.getColumnTypeName(column);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getPrecision(int column) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadOneColDef(column).precision;
			}
			return metaData.getPrecision(column);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getScale(int column) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadOneColDef(column).scale;
			}
			return metaData.getScale(column);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getSchemaName(int column) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadOneColDef(column).schemaName;
			}
			return metaData.getSchemaName(column);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getTableName(int column) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadOneColDef(column).tableName;
			}
			return metaData.getTableName(column);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean isAutoIncrement(int column) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadOneColDef(column).autoIncrement;
			}
			return metaData.isAutoIncrement(column);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean isCaseSensitive(int column) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadOneColDef(column).caseSensitive;
			}
			return metaData.isCaseSensitive(column);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean isCurrency(int column) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadOneColDef(column).currency;
			}
			return metaData.isCurrency(column);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean isDefinitelyWritable(int column) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadOneColDef(column).definitivelyWritable;
			}
			return metaData.isDefinitelyWritable(column);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int isNullable(int column) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadOneColDef(column).nullable;
			}
			return metaData.isNullable(column);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean isReadOnly(int column) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadOneColDef(column).readOnly;
			}
			return metaData.isReadOnly(column);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean isSearchable(int column) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadOneColDef(column).searchable;
			}
			return metaData.isSearchable(column);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean isSigned(int column) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadOneColDef(column).signed;
			}
			return metaData.isSigned(column);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean isWritable(int column) throws SQLException {
		try {
			if(ServerOptions.options.OPTIMIZATION_COL_DEF) {
				return loadOneColDef(column).writable;
			}
			return metaData.isWritable(column);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	
	//
	// ColDef optimization
	//
	private IResultSetMetaData.ColumnDef[] loadColDefs() throws RemoteException, SQLException {
		if(colDefs==null) {
			colDefs = metaData._getColumnDefs();
		}
		return colDefs;
	}
	private IResultSetMetaData.ColumnDef loadOneColDef(int colIdx) throws RemoteException, SQLException {
		return loadColDefs()[colIdx-1]; // JDBC is one based
	}

}
