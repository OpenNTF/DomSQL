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

import java.rmi.RemoteException;
import java.sql.RowIdLifetime;
import java.sql.SQLException;


/**
 *
 */
public interface IDatabaseMetaData extends IRemoteObject {

	public boolean allProceduresAreCallable() throws SQLException, RemoteException;
	public boolean allTablesAreSelectable() throws SQLException, RemoteException;
	public boolean dataDefinitionCausesTransactionCommit() throws SQLException, RemoteException;
	public boolean dataDefinitionIgnoredInTransactions() throws SQLException, RemoteException;
	public boolean deletesAreDetected(int type) throws SQLException, RemoteException;
	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException, RemoteException;
	public IResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException, RemoteException;
	public IResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException, RemoteException;
	public IResultSet getCatalogs() throws SQLException, RemoteException;
	public String getCatalogSeparator() throws SQLException, RemoteException;
	public String getCatalogTerm() throws SQLException, RemoteException;
	public IResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException, RemoteException;
	public IResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException, RemoteException;
	public IResultSet getCrossReference(String primaryCatalog, String primarySchema, String primaryTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException, RemoteException;
	public int getDatabaseMajorVersion() throws SQLException, RemoteException;
	public int getDatabaseMinorVersion() throws SQLException, RemoteException;
	public String getDatabaseProductName() throws SQLException, RemoteException;
	public String getDatabaseProductVersion() throws SQLException, RemoteException;
	public int getDefaultTransactionIsolation() throws SQLException, RemoteException;
	public int getDriverMajorVersion() throws SQLException, RemoteException;
	public int getDriverMinorVersion() throws SQLException, RemoteException;
	public String getDriverName() throws SQLException, RemoteException;
	public String getDriverVersion() throws SQLException, RemoteException;
	public IResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException, RemoteException;
	public String getExtraNameCharacters() throws SQLException, RemoteException;
	public String getIdentifierQuoteString() throws SQLException, RemoteException;
	public IResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException, RemoteException;
	public IResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException, RemoteException;
	public int getJDBCMajorVersion() throws SQLException, RemoteException;
	public int getJDBCMinorVersion() throws SQLException, RemoteException;
	public int getMaxBinaryLiteralLength() throws SQLException, RemoteException;
	public int getMaxCatalogNameLength() throws SQLException, RemoteException;
	public int getMaxCharLiteralLength() throws SQLException, RemoteException;
	public int getMaxColumnNameLength() throws SQLException, RemoteException;
	public int getMaxColumnsInGroupBy() throws SQLException, RemoteException;
	public int getMaxColumnsInIndex() throws SQLException, RemoteException;
	public int getMaxColumnsInOrderBy() throws SQLException, RemoteException;
	public int getMaxColumnsInSelect() throws SQLException, RemoteException;
	public int getMaxColumnsInTable() throws SQLException, RemoteException;
	public int getMaxConnections() throws SQLException, RemoteException;
	public int getMaxCursorNameLength() throws SQLException, RemoteException;
	public int getMaxIndexLength() throws SQLException, RemoteException;
	public int getMaxProcedureNameLength() throws SQLException, RemoteException;
	public int getMaxRowSize() throws SQLException, RemoteException;
	public int getMaxSchemaNameLength() throws SQLException, RemoteException;
	public int getMaxStatementLength() throws SQLException, RemoteException;
	public int getMaxStatements() throws SQLException, RemoteException;
	public int getMaxTableNameLength() throws SQLException, RemoteException;
	public int getMaxTablesInSelect() throws SQLException, RemoteException;
	public int getMaxUserNameLength() throws SQLException, RemoteException;
	public String getNumericFunctions() throws SQLException, RemoteException;
	public IResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException, RemoteException;
	public IResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException, RemoteException;
	public IResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException, RemoteException;
	public String getProcedureTerm() throws SQLException, RemoteException;
	public int getResultSetHoldability() throws SQLException, RemoteException;
	public IResultSet getSchemas() throws SQLException, RemoteException;
	public String getSchemaTerm() throws SQLException, RemoteException;
	public String getSearchStringEscape() throws SQLException, RemoteException;
	public String getSQLKeywords() throws SQLException, RemoteException;
	public int getSQLStateType() throws SQLException, RemoteException;
	public String getStringFunctions() throws SQLException, RemoteException;
	public IResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException, RemoteException;
	public IResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException, RemoteException;
	public String getSystemFunctions() throws SQLException, RemoteException;
	public IResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException, RemoteException;
	public IResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException, RemoteException;
	public IResultSet getTableTypes() throws SQLException, RemoteException;
	public String getTimeDateFunctions() throws SQLException, RemoteException;
	public IResultSet getTypeInfo() throws SQLException, RemoteException;
	public IResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException, RemoteException;
	public String getURL() throws SQLException, RemoteException;
	public String getUserName() throws SQLException, RemoteException;
	public IResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException, RemoteException;
	public boolean insertsAreDetected(int type) throws SQLException, RemoteException;
	public boolean isCatalogAtStart() throws SQLException, RemoteException;
	public boolean isReadOnly() throws SQLException, RemoteException;
	public boolean locatorsUpdateCopy() throws SQLException, RemoteException;
	public boolean nullPlusNonNullIsNull() throws SQLException, RemoteException;
	public boolean nullsAreSortedAtEnd() throws SQLException, RemoteException;
	public boolean nullsAreSortedAtStart() throws SQLException, RemoteException;
	public boolean nullsAreSortedHigh() throws SQLException, RemoteException;
	public boolean nullsAreSortedLow() throws SQLException, RemoteException;
	public boolean othersDeletesAreVisible(int type) throws SQLException, RemoteException;
	public boolean othersInsertsAreVisible(int type) throws SQLException, RemoteException;
	public boolean othersUpdatesAreVisible(int type) throws SQLException, RemoteException;
	public boolean ownDeletesAreVisible(int type) throws SQLException, RemoteException;
	public boolean ownInsertsAreVisible(int type) throws SQLException, RemoteException;
	public boolean ownUpdatesAreVisible(int type) throws SQLException, RemoteException;
	public boolean storesLowerCaseIdentifiers() throws SQLException, RemoteException;
	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException, RemoteException;
	public boolean storesMixedCaseIdentifiers() throws SQLException, RemoteException;
	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException, RemoteException;
	public boolean storesUpperCaseIdentifiers() throws SQLException, RemoteException;
	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException, RemoteException;
	public boolean supportsAlterTableWithAddColumn() throws SQLException, RemoteException;
	public boolean supportsAlterTableWithDropColumn() throws SQLException, RemoteException;
	public boolean supportsANSI92EntryLevelSQL() throws SQLException, RemoteException;
	public boolean supportsANSI92FullSQL() throws SQLException, RemoteException;
	public boolean supportsANSI92IntermediateSQL() throws SQLException, RemoteException;
	public boolean supportsBatchUpdates() throws SQLException, RemoteException;
	public boolean supportsCatalogsInDataManipulation() throws SQLException, RemoteException;
	public boolean supportsCatalogsInIndexDefinitions() throws SQLException, RemoteException;
	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException, RemoteException;
	public boolean supportsCatalogsInProcedureCalls() throws SQLException, RemoteException;
	public boolean supportsCatalogsInTableDefinitions() throws SQLException, RemoteException;
	public boolean supportsColumnAliasing() throws SQLException, RemoteException;
	public boolean supportsConvert() throws SQLException, RemoteException;
	public boolean supportsConvert(int fromType, int toType) throws SQLException, RemoteException;
	public boolean supportsCoreSQLGrammar() throws SQLException, RemoteException;
	public boolean supportsCorrelatedSubqueries() throws SQLException, RemoteException;
	public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException, RemoteException;
	public boolean supportsDataManipulationTransactionsOnly() throws SQLException, RemoteException;
	public boolean supportsDifferentTableCorrelationNames() throws SQLException, RemoteException;
	public boolean supportsExpressionsInOrderBy() throws SQLException, RemoteException;
	public boolean supportsExtendedSQLGrammar() throws SQLException, RemoteException;
	public boolean supportsFullOuterJoins() throws SQLException, RemoteException;
	public boolean supportsGetGeneratedKeys() throws SQLException, RemoteException;
	public boolean supportsGroupBy() throws SQLException, RemoteException;
	public boolean supportsGroupByBeyondSelect() throws SQLException, RemoteException;
	public boolean supportsGroupByUnrelated() throws SQLException, RemoteException;
	public boolean supportsIntegrityEnhancementFacility() throws SQLException, RemoteException;
	public boolean supportsLikeEscapeClause() throws SQLException, RemoteException;
	public boolean supportsLimitedOuterJoins() throws SQLException, RemoteException;
	public boolean supportsMinimumSQLGrammar() throws SQLException, RemoteException;
	public boolean supportsMixedCaseIdentifiers() throws SQLException, RemoteException;
	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException, RemoteException;
	public boolean supportsMultipleOpenResults() throws SQLException, RemoteException;
	public boolean supportsMultipleResultSets() throws SQLException, RemoteException;
	public boolean supportsMultipleTransactions() throws SQLException, RemoteException;
	public boolean supportsNamedParameters() throws SQLException, RemoteException;
	public boolean supportsNonNullableColumns() throws SQLException, RemoteException;
	public boolean supportsOpenCursorsAcrossCommit() throws SQLException, RemoteException;
	public boolean supportsOpenCursorsAcrossRollback() throws SQLException, RemoteException;
	public boolean supportsOpenStatementsAcrossCommit() throws SQLException, RemoteException;
	public boolean supportsOpenStatementsAcrossRollback() throws SQLException, RemoteException;
	public boolean supportsOrderByUnrelated() throws SQLException, RemoteException;
	public boolean supportsOuterJoins() throws SQLException, RemoteException;
	public boolean supportsPositionedDelete() throws SQLException, RemoteException;
	public boolean supportsPositionedUpdate() throws SQLException, RemoteException;
	public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException, RemoteException;
	public boolean supportsResultSetHoldability(int holdability) throws SQLException, RemoteException;
	public boolean supportsResultSetType(int type) throws SQLException, RemoteException;
	public boolean supportsSavepoints() throws SQLException, RemoteException;
	public boolean supportsSchemasInDataManipulation() throws SQLException, RemoteException;
	public boolean supportsSchemasInIndexDefinitions() throws SQLException, RemoteException;
	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException, RemoteException;
	public boolean supportsSchemasInProcedureCalls() throws SQLException, RemoteException;
	public boolean supportsSchemasInTableDefinitions() throws SQLException, RemoteException;
	public boolean supportsSelectForUpdate() throws SQLException, RemoteException;
	public boolean supportsStatementPooling() throws SQLException, RemoteException;
	public boolean supportsStoredProcedures() throws SQLException, RemoteException;
	public boolean supportsSubqueriesInComparisons() throws SQLException, RemoteException;
	public boolean supportsSubqueriesInExists() throws SQLException, RemoteException;
	public boolean supportsSubqueriesInIns() throws SQLException, RemoteException;
	public boolean supportsSubqueriesInQuantifieds() throws SQLException, RemoteException;
	public boolean supportsTableCorrelationNames() throws SQLException, RemoteException;
	public boolean supportsTransactionIsolationLevel(int level) throws SQLException, RemoteException;
	public boolean supportsTransactions() throws SQLException, RemoteException;
	public boolean supportsUnion() throws SQLException, RemoteException;
	public boolean supportsUnionAll() throws SQLException, RemoteException;
	public boolean updatesAreDetected(int type) throws SQLException, RemoteException;
	public boolean usesLocalFilePerTable() throws SQLException, RemoteException;
	public boolean usesLocalFiles() throws SQLException, RemoteException;
	public RowIdLifetime getRowIdLifetime() throws SQLException, RemoteException;
	public IResultSet getSchemas(String catalog, String schemaPattern) throws SQLException, RemoteException;
	public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException, RemoteException;
	public boolean autoCommitFailureClosesAllResultSets() throws SQLException, RemoteException;
	public IResultSet getClientInfoProperties() throws SQLException, RemoteException;
	public IResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException, RemoteException;
	public IResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern) throws SQLException, RemoteException;
}
