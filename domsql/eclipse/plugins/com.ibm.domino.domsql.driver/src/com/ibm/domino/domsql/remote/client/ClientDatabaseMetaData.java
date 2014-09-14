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
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;

import com.ibm.domino.domsql.remote.transport.IDatabaseMetaData;

public class ClientDatabaseMetaData extends ClientObject implements DatabaseMetaData {

    private IDatabaseMetaData metaData;

    public ClientDatabaseMetaData(IDatabaseMetaData metaData) {
    	super(metaData);
    	this.metaData = metaData;
    }
    
    public IDatabaseMetaData getRemoteObject() {
    	return metaData;
    }

	public boolean allProceduresAreCallable() throws SQLException {
		try {
			return metaData.allProceduresAreCallable();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean allTablesAreSelectable() throws SQLException {
		try {
			return metaData.allTablesAreSelectable();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
		try {
			return metaData.dataDefinitionCausesTransactionCommit();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
		try {
			return metaData.dataDefinitionIgnoredInTransactions();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean deletesAreDetected(int type) throws SQLException {
		try {
			return metaData.deletesAreDetected(type);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
		try {
			return metaData.doesMaxRowSizeIncludeBlobs();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getAttributes(catalog, schemaPattern, typeNamePattern, attributeNamePattern));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getBestRowIdentifier(catalog, schema, table, scope, nullable));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getCatalogs() throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getCatalogs());
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getCatalogSeparator() throws SQLException {
		try {
			return metaData.getCatalogSeparator();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getCatalogTerm() throws SQLException {
		try {
			return metaData.getCatalogTerm();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getColumnPrivileges(catalog, schema, table, columnNamePattern));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public Connection getConnection() throws SQLException {
		try {
			// TODO!
			throwNotImplementedException(); return null;
			//return metaData.;
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getCrossReference(String primaryCatalog, String primarySchema, String primaryTable, String foreignCatalog, String foreignSchema, String foreignTable)
			throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getCrossReference(primaryCatalog, primarySchema, primaryTable, foreignCatalog, foreignSchema, foreignTable));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getDatabaseMajorVersion() throws SQLException {
		try {
			return metaData.getDatabaseMajorVersion();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getDatabaseMinorVersion() throws SQLException {
		try {
			return metaData.getDatabaseMinorVersion();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getDatabaseProductName() throws SQLException {
		try {
			return metaData.getDatabaseProductName();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getDatabaseProductVersion() throws SQLException {
		try {
			return metaData.getDatabaseProductVersion();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getDefaultTransactionIsolation() throws SQLException {
		try {
			return metaData.getDefaultTransactionIsolation();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getDriverMajorVersion() {
		// TODO: get it from a constant?
		try {
			return metaData.getDriverMajorVersion();
		} catch(Exception t) {
			return 1;
		}
	}

	public int getDriverMinorVersion() {
		// TODO: get it from a constant?
		try {
			return metaData.getDriverMinorVersion();
		} catch(Exception t) {
			return 1;
		}
	}

	public String getDriverName() throws SQLException {
		try {
			return metaData.getDriverName();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getDriverVersion() throws SQLException {
		try {
			return metaData.getDriverVersion();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getExportedKeys(catalog, schema, table));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getExtraNameCharacters() throws SQLException {
		try {
			return metaData.getExtraNameCharacters();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getIdentifierQuoteString() throws SQLException {
		try {
			return metaData.getIdentifierQuoteString();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getImportedKeys(catalog, schema, table));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getIndexInfo(catalog, schema, table, unique, approximate));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getJDBCMajorVersion() throws SQLException {
		try {
			return metaData.getJDBCMajorVersion();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getJDBCMinorVersion() throws SQLException {
		try {
			return metaData.getJDBCMinorVersion();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxBinaryLiteralLength() throws SQLException {
		try {
			return metaData.getMaxBinaryLiteralLength();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxCatalogNameLength() throws SQLException {
		try {
			return metaData.getMaxCatalogNameLength();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxCharLiteralLength() throws SQLException {
		try {
			return metaData.getMaxCharLiteralLength();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxColumnNameLength() throws SQLException {
		try {
			return metaData.getMaxColumnNameLength();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxColumnsInGroupBy() throws SQLException {
		try {
			return metaData.getMaxColumnsInGroupBy();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxColumnsInIndex() throws SQLException {
		try {
			return metaData.getMaxColumnsInIndex();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxColumnsInOrderBy() throws SQLException {
		try {
			return metaData.getMaxColumnsInOrderBy();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxColumnsInSelect() throws SQLException {
		try {
			return metaData.getMaxColumnsInSelect();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxColumnsInTable() throws SQLException {
		try {
			return metaData.getMaxColumnsInTable();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxConnections() throws SQLException {
		try {
			return metaData.getMaxConnections();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxCursorNameLength() throws SQLException {
		try {
			return metaData.getMaxCursorNameLength();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxIndexLength() throws SQLException {
		try {
			return metaData.getMaxIndexLength();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxProcedureNameLength() throws SQLException {
		try {
			return metaData.getMaxProcedureNameLength();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxRowSize() throws SQLException {
		try {
			return metaData.getMaxRowSize();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxSchemaNameLength() throws SQLException {
		try {
			return metaData.getMaxSchemaNameLength();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxStatementLength() throws SQLException {
		try {
			return metaData.getMaxStatementLength();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxStatements() throws SQLException {
		try {
			return metaData.getMaxStatements();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxTableNameLength() throws SQLException {
		try {
			return metaData.getMaxTableNameLength();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxTablesInSelect() throws SQLException {
		try {
			return metaData.getMaxTablesInSelect();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getMaxUserNameLength() throws SQLException {
		try {
			return metaData.getMaxUserNameLength();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getNumericFunctions() throws SQLException {
		try {
			return metaData.getNumericFunctions();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getPrimaryKeys(catalog, schema, table));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getProcedures(catalog, schemaPattern, procedureNamePattern));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getProcedureTerm() throws SQLException {
		try {
			return metaData.getProcedureTerm();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getResultSetHoldability() throws SQLException {
		try {
			return metaData.getResultSetHoldability();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getSchemas() throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getSchemas());
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getSchemaTerm() throws SQLException {
		try {
			return metaData.getSchemaTerm();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getSearchStringEscape() throws SQLException {
		try {
			return metaData.getSearchStringEscape();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getSQLKeywords() throws SQLException {
		try {
			return metaData.getSQLKeywords();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public int getSQLStateType() throws SQLException {
		try {
			return metaData.getSQLStateType();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getStringFunctions() throws SQLException {
		try {
			return metaData.getStringFunctions();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getSuperTables(catalog, schemaPattern, tableNamePattern));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getSuperTypes(catalog, schemaPattern, typeNamePattern));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getSystemFunctions() throws SQLException {
		try {
			return metaData.getSystemFunctions();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getTablePrivileges(catalog, schemaPattern, tableNamePattern));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getTables(catalog, schemaPattern, tableNamePattern, types));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getTableTypes() throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getTableTypes());
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getTimeDateFunctions() throws SQLException {
		try {
			return metaData.getTimeDateFunctions();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getTypeInfo() throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getTypeInfo());
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getUDTs(catalog, schemaPattern, typeNamePattern, types));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getURL() throws SQLException {
		try {
			return metaData.getURL();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public String getUserName() throws SQLException {
		try {
			return metaData.getUserName();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getVersionColumns(catalog, schema, table));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean insertsAreDetected(int type) throws SQLException {
		try {
			return metaData.insertsAreDetected(type);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean isCatalogAtStart() throws SQLException {
		try {
			return metaData.isCatalogAtStart();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean isReadOnly() throws SQLException {
		try {
			return metaData.isReadOnly();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean locatorsUpdateCopy() throws SQLException {
		try {
			return metaData.locatorsUpdateCopy();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean nullPlusNonNullIsNull() throws SQLException {
		try {
			return metaData.nullPlusNonNullIsNull();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean nullsAreSortedAtEnd() throws SQLException {
		try {
			return metaData.nullsAreSortedAtEnd();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean nullsAreSortedAtStart() throws SQLException {
		try {
			return metaData.nullsAreSortedAtStart();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean nullsAreSortedHigh() throws SQLException {
		try {
			return metaData.nullsAreSortedHigh();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean nullsAreSortedLow() throws SQLException {
		try {
			return metaData.nullsAreSortedLow();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean othersDeletesAreVisible(int type) throws SQLException {
		try {
			return metaData.othersDeletesAreVisible(type);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean othersInsertsAreVisible(int type) throws SQLException {
		try {
			return metaData.othersInsertsAreVisible(type);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean othersUpdatesAreVisible(int type) throws SQLException {
		try {
			return metaData.othersUpdatesAreVisible(type);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean ownDeletesAreVisible(int type) throws SQLException {
		try {
			return metaData.ownDeletesAreVisible(type);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean ownInsertsAreVisible(int type) throws SQLException {
		try {
			return metaData.ownInsertsAreVisible(type);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean ownUpdatesAreVisible(int type) throws SQLException {
		try {
			return metaData.ownUpdatesAreVisible(type);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean storesLowerCaseIdentifiers() throws SQLException {
		try {
			return metaData.storesLowerCaseIdentifiers();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
		try {
			return metaData.storesLowerCaseQuotedIdentifiers();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean storesMixedCaseIdentifiers() throws SQLException {
		try {
			return metaData.storesMixedCaseIdentifiers();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
		try {
			return metaData.storesMixedCaseQuotedIdentifiers();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean storesUpperCaseIdentifiers() throws SQLException {
		try {
			return metaData.storesUpperCaseIdentifiers();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
		try {
			return metaData.storesUpperCaseQuotedIdentifiers();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsAlterTableWithAddColumn() throws SQLException {
		try {
			return metaData.supportsAlterTableWithAddColumn();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsAlterTableWithDropColumn() throws SQLException {
		try {
			return metaData.supportsAlterTableWithDropColumn();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsANSI92EntryLevelSQL() throws SQLException {
		try {
			return metaData.supportsANSI92EntryLevelSQL();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsANSI92FullSQL() throws SQLException {
		try {
			return metaData.supportsANSI92FullSQL();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsANSI92IntermediateSQL() throws SQLException {
		try {
			return metaData.supportsANSI92IntermediateSQL();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsBatchUpdates() throws SQLException {
		try {
			return metaData.supportsBatchUpdates();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsCatalogsInDataManipulation() throws SQLException {
		try {
			return metaData.supportsCatalogsInDataManipulation();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
		try {
			return metaData.supportsCatalogsInIndexDefinitions();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
		try {
			return metaData.supportsCatalogsInPrivilegeDefinitions();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsCatalogsInProcedureCalls() throws SQLException {
		try {
			return metaData.supportsCatalogsInProcedureCalls();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsCatalogsInTableDefinitions() throws SQLException {
		try {
			return metaData.supportsCatalogsInTableDefinitions();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsColumnAliasing() throws SQLException {
		try {
			return metaData.supportsColumnAliasing();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsConvert() throws SQLException {
		try {
			return metaData.supportsConvert();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsConvert(int fromType, int toType) throws SQLException {
		try {
			return metaData.supportsConvert(fromType, toType);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsCoreSQLGrammar() throws SQLException {
		try {
			return metaData.supportsCoreSQLGrammar();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsCorrelatedSubqueries() throws SQLException {
		try {
			return metaData.supportsCorrelatedSubqueries();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
		try {
			return metaData.supportsDataDefinitionAndDataManipulationTransactions();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
		try {
			return metaData.supportsDataManipulationTransactionsOnly();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsDifferentTableCorrelationNames() throws SQLException {
		try {
			return metaData.supportsDifferentTableCorrelationNames();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsExpressionsInOrderBy() throws SQLException {
		try {
			return metaData.supportsExpressionsInOrderBy();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsExtendedSQLGrammar() throws SQLException {
		try {
			return metaData.supportsExtendedSQLGrammar();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsFullOuterJoins() throws SQLException {
		try {
			return metaData.supportsFullOuterJoins();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsGetGeneratedKeys() throws SQLException {
		try {
			return metaData.supportsGetGeneratedKeys();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsGroupBy() throws SQLException {
		try {
			return metaData.supportsGroupBy();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsGroupByBeyondSelect() throws SQLException {
		try {
			return metaData.supportsGroupByBeyondSelect();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsGroupByUnrelated() throws SQLException {
		try {
			return metaData.supportsGroupByUnrelated();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsIntegrityEnhancementFacility() throws SQLException {
		try {
			return metaData.supportsIntegrityEnhancementFacility();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsLikeEscapeClause() throws SQLException {
		try {
			return metaData.supportsLikeEscapeClause();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsLimitedOuterJoins() throws SQLException {
		try {
			return metaData.supportsLimitedOuterJoins();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsMinimumSQLGrammar() throws SQLException {
		try {
			return metaData.supportsMinimumSQLGrammar();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsMixedCaseIdentifiers() throws SQLException {
		try {
			return metaData.supportsMixedCaseIdentifiers();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
		try {
			return metaData.supportsMixedCaseQuotedIdentifiers();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsMultipleOpenResults() throws SQLException {
		try {
			return metaData.supportsMultipleOpenResults();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsMultipleResultSets() throws SQLException {
		try {
			return metaData.supportsMultipleResultSets();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsMultipleTransactions() throws SQLException {
		try {
			return metaData.supportsMultipleTransactions();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsNamedParameters() throws SQLException {
		try {
			return metaData.supportsNamedParameters();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsNonNullableColumns() throws SQLException {
		try {
			return metaData.supportsNonNullableColumns();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
		try {
			return metaData.supportsOpenCursorsAcrossCommit();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
		try {
			return metaData.supportsOpenCursorsAcrossRollback();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
		try {
			return metaData.supportsOpenStatementsAcrossCommit();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
		try {
			return metaData.supportsOpenStatementsAcrossRollback();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsOrderByUnrelated() throws SQLException {
		try {
			return metaData.supportsOrderByUnrelated();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsOuterJoins() throws SQLException {
		try {
			return metaData.supportsOuterJoins();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsPositionedDelete() throws SQLException {
		try {
			return metaData.supportsPositionedDelete();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsPositionedUpdate() throws SQLException {
		try {
			return metaData.supportsPositionedUpdate();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
		try {
			return metaData.supportsResultSetConcurrency(type, concurrency);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsResultSetHoldability(int holdability) throws SQLException {
		try {
			return metaData.supportsResultSetHoldability(holdability);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsResultSetType(int type) throws SQLException {
		try {
			return metaData.supportsResultSetType(type);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsSavepoints() throws SQLException {
		try {
			return metaData.supportsSavepoints();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsSchemasInDataManipulation() throws SQLException {
		try {
			return metaData.supportsSchemasInDataManipulation();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsSchemasInIndexDefinitions() throws SQLException {
		try {
			return metaData.supportsSchemasInIndexDefinitions();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
		try {
			return metaData.supportsSchemasInPrivilegeDefinitions();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsSchemasInProcedureCalls() throws SQLException {
		try {
			return metaData.supportsSchemasInProcedureCalls();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsSchemasInTableDefinitions() throws SQLException {
		try {
			return metaData.supportsSchemasInTableDefinitions();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsSelectForUpdate() throws SQLException {
		try {
			return metaData.supportsSelectForUpdate();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsStatementPooling() throws SQLException {
		try {
			return metaData.supportsStatementPooling();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsStoredProcedures() throws SQLException {
		try {
			return metaData.supportsStoredProcedures();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsSubqueriesInComparisons() throws SQLException {
		try {
			return metaData.supportsSubqueriesInComparisons();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsSubqueriesInExists() throws SQLException {
		try {
			return metaData.supportsSubqueriesInExists();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsSubqueriesInIns() throws SQLException {
		try {
			return metaData.supportsSubqueriesInIns();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsSubqueriesInQuantifieds() throws SQLException {
		try {
			return metaData.supportsSubqueriesInQuantifieds();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsTableCorrelationNames() throws SQLException {
		try {
			return metaData.supportsTableCorrelationNames();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
		try {
			return metaData.supportsTransactionIsolationLevel(level);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsTransactions() throws SQLException {
		try {
			return metaData.supportsTransactions();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsUnion() throws SQLException {
		try {
			return metaData.supportsUnion();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsUnionAll() throws SQLException {
		try {
			return metaData.supportsUnionAll();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean updatesAreDetected(int type) throws SQLException {
		try {
			return metaData.updatesAreDetected(type);
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean usesLocalFilePerTable() throws SQLException {
		try {
			return metaData.usesLocalFilePerTable();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean usesLocalFiles() throws SQLException {
		try {
			return metaData.usesLocalFiles();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public RowIdLifetime getRowIdLifetime() throws SQLException {
		try {
			return metaData.getRowIdLifetime();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getSchemas(catalog, schemaPattern));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
		try {
			return metaData.supportsStoredFunctionsUsingCallSyntax();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
		try {
			return metaData.autoCommitFailureClosesAllResultSets();
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getClientInfoProperties() throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getClientInfoProperties());
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getFunctions(catalog, schemaPattern, functionNamePattern));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

	public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern) throws SQLException {
		try {
			return new ClientResultSet(this,metaData.getFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern));
		} catch(RemoteException t) {
			throw newSQLException(t);
		}
	}

}
