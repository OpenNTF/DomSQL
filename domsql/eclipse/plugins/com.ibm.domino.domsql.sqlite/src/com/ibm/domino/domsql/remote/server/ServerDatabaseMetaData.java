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
import java.sql.RowIdLifetime;
import java.sql.SQLException;

import com.ibm.domino.domsql.remote.transport.IDatabaseMetaData;
import com.ibm.domino.domsql.remote.transport.IResultSet;
import com.ibm.domino.domsql.sqlite.driver.jdbc.DomSQLDatabaseMetaData;

public class ServerDatabaseMetaData extends ServerObject implements IDatabaseMetaData {

	private static final long serialVersionUID = 1L;
	
    private DomSQLDatabaseMetaData metaData;

    public ServerDatabaseMetaData(ServerConnection connection, DomSQLDatabaseMetaData metaData) throws RemoteException {
        super(connection);
        this.metaData = metaData;
    }
    
    public DomSQLDatabaseMetaData getNative() {
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

    public boolean allProceduresAreCallable() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.allProceduresAreCallable());
        } finally {
            termContext();
        }
    }

    public boolean allTablesAreSelectable() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.allTablesAreSelectable());
        } finally {
            termContext();
        }
    }

    public boolean dataDefinitionCausesTransactionCommit() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.dataDefinitionCausesTransactionCommit());
        } finally {
            termContext();
        }
    }

    public boolean dataDefinitionIgnoredInTransactions() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.dataDefinitionIgnoredInTransactions());
        } finally {
            termContext();
        }
    }

    public boolean deletesAreDetected(int type) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.deletesAreDetected(type));
        } finally {
            termContext();
        }
    }

    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.doesMaxRowSizeIncludeBlobs());
        } finally {
            termContext();
        }
    }

    public IResultSet getAttributes(String catalog, String schemaPattern,
            String typeNamePattern, String attributeNamePattern)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getAttributes(catalog, schemaPattern,
                    typeNamePattern, attributeNamePattern));
        } finally {
            termContext();
        }
    }

    public IResultSet getBestRowIdentifier(String catalog, String schema,
            String table, int scope, boolean nullable) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getBestRowIdentifier(catalog, schema, table, scope,
                    nullable));
        } finally {
            termContext();
        }
    }

    public IResultSet getCatalogs() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getCatalogs());
        } finally {
            termContext();
        }
    }

    public String getCatalogSeparator() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getCatalogSeparator());
        } finally {
            termContext();
        }
    }

    public String getCatalogTerm() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getCatalogTerm());
        } finally {
            termContext();
        }
    }

    public IResultSet getColumnPrivileges(String catalog, String schema,
            String table, String columnNamePattern) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getColumnPrivileges(catalog, schema, table,
                    columnNamePattern));
        } finally {
            termContext();
        }
    }

    public IResultSet getColumns(String catalog, String schemaPattern,
            String tableNamePattern, String columnNamePattern)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getColumns(catalog, schemaPattern,
                    tableNamePattern, columnNamePattern));
        } finally {
            termContext();
        }
    }

    public IResultSet getCrossReference(String primaryCatalog,
            String primarySchema, String primaryTable, String foreignCatalog,
            String foreignSchema, String foreignTable) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getCrossReference(primaryCatalog, primarySchema,
                    primaryTable, foreignCatalog, foreignSchema, foreignTable));
        } finally {
            termContext();
        }
    }

    public int getDatabaseMajorVersion() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getDatabaseMajorVersion());
        } finally {
            termContext();
        }
    }

    public int getDatabaseMinorVersion() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getDatabaseMinorVersion());
        } finally {
            termContext();
        }
    }

    public String getDatabaseProductName() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getDatabaseProductName());
        } finally {
            termContext();
        }
    }

    public String getDatabaseProductVersion() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getDatabaseProductVersion());
        } finally {
            termContext();
        }
    }

    public int getDefaultTransactionIsolation() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getDefaultTransactionIsolation());
        } finally {
            termContext();
        }
    }

    public int getDriverMajorVersion() {
        initContext();
        try {
            return wrap(metaData.getDriverMajorVersion());
        } finally {
            termContext();
        }
    }

    public int getDriverMinorVersion() {
        initContext();
        try {
            return wrap(metaData.getDriverMinorVersion());
        } finally {
            termContext();
        }
    }

    public String getDriverName() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getDriverName());
        } finally {
            termContext();
        }
    }

    public String getDriverVersion() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getDriverVersion());
        } finally {
            termContext();
        }
    }

    public IResultSet getExportedKeys(String catalog, String schema, String table)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getExportedKeys(catalog, schema, table));
        } finally {
            termContext();
        }
    }

    public String getExtraNameCharacters() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getExtraNameCharacters());
        } finally {
            termContext();
        }
    }

    public String getIdentifierQuoteString() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getIdentifierQuoteString());
        } finally {
            termContext();
        }
    }

    public IResultSet getImportedKeys(String catalog, String schema, String table)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getImportedKeys(catalog, schema, table));
        } finally {
            termContext();
        }
    }

    public IResultSet getIndexInfo(String catalog, String schema, String table,
            boolean unique, boolean approximate) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getIndexInfo(catalog, schema, table, unique,
                    approximate));
        } finally {
            termContext();
        }
    }

    public int getJDBCMajorVersion() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getJDBCMajorVersion());
        } finally {
            termContext();
        }
    }

    public int getJDBCMinorVersion() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getJDBCMinorVersion());
        } finally {
            termContext();
        }
    }

    public int getMaxBinaryLiteralLength() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getMaxBinaryLiteralLength());
        } finally {
            termContext();
        }
    }

    public int getMaxCatalogNameLength() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getMaxCatalogNameLength());
        } finally {
            termContext();
        }
    }

    public int getMaxCharLiteralLength() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getMaxCharLiteralLength());
        } finally {
            termContext();
        }
    }

    public int getMaxColumnNameLength() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getMaxColumnNameLength());
        } finally {
            termContext();
        }
    }

    public int getMaxColumnsInGroupBy() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getMaxColumnsInGroupBy());
        } finally {
            termContext();
        }
    }

    public int getMaxColumnsInIndex() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getMaxColumnsInIndex());
        } finally {
            termContext();
        }
    }

    public int getMaxColumnsInOrderBy() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getMaxColumnsInOrderBy());
        } finally {
            termContext();
        }
    }

    public int getMaxColumnsInSelect() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getMaxColumnsInSelect());
        } finally {
            termContext();
        }
    }

    public int getMaxColumnsInTable() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getMaxColumnsInTable());
        } finally {
            termContext();
        }
    }

    public int getMaxConnections() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getMaxConnections());
        } finally {
            termContext();
        }
    }

    public int getMaxCursorNameLength() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getMaxCursorNameLength());
        } finally {
            termContext();
        }
    }

    public int getMaxIndexLength() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getMaxIndexLength());
        } finally {
            termContext();
        }
    }

    public int getMaxProcedureNameLength() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getMaxProcedureNameLength());
        } finally {
            termContext();
        }
    }

    public int getMaxRowSize() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getMaxRowSize());
        } finally {
            termContext();
        }
    }

    public int getMaxSchemaNameLength() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getMaxSchemaNameLength());
        } finally {
            termContext();
        }
    }

    public int getMaxStatementLength() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getMaxStatementLength());
        } finally {
            termContext();
        }
    }

    public int getMaxStatements() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getMaxStatements());
        } finally {
            termContext();
        }
    }

    public int getMaxTableNameLength() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getMaxTableNameLength());
        } finally {
            termContext();
        }
    }

    public int getMaxTablesInSelect() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getMaxTablesInSelect());
        } finally {
            termContext();
        }
    }

    public int getMaxUserNameLength() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getMaxUserNameLength());
        } finally {
            termContext();
        }
    }

    public String getNumericFunctions() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getNumericFunctions());
        } finally {
            termContext();
        }
    }

    public IResultSet getPrimaryKeys(String catalog, String schema, String table)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getPrimaryKeys(catalog, schema, table));
        } finally {
            termContext();
        }
    }

    public IResultSet getProcedureColumns(String catalog, String schemaPattern,
            String procedureNamePattern, String columnNamePattern)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getProcedureColumns(catalog, schemaPattern,
                    procedureNamePattern, columnNamePattern));
        } finally {
            termContext();
        }
    }

    public IResultSet getProcedures(String catalog, String schemaPattern,
            String procedureNamePattern) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getProcedures(catalog, schemaPattern,
                    procedureNamePattern));
        } finally {
            termContext();
        }
    }

    public String getProcedureTerm() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getProcedureTerm());
        } finally {
            termContext();
        }
    }

    public int getResultSetHoldability() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getResultSetHoldability());
        } finally {
            termContext();
        }
    }

    public IResultSet getSchemas() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getSchemas());
        } finally {
            termContext();
        }
    }

    public String getSchemaTerm() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getSchemaTerm());
        } finally {
            termContext();
        }
    }

    public String getSearchStringEscape() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getSearchStringEscape());
        } finally {
            termContext();
        }
    }

    public String getSQLKeywords() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getSQLKeywords());
        } finally {
            termContext();
        }
    }

    public int getSQLStateType() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getSQLStateType());
        } finally {
            termContext();
        }
    }

    public String getStringFunctions() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getStringFunctions());
        } finally {
            termContext();
        }
    }

    public IResultSet getSuperTables(String catalog, String schemaPattern,
            String tableNamePattern) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getSuperTables(catalog, schemaPattern,
                    tableNamePattern));
        } finally {
            termContext();
        }
    }

    public IResultSet getSuperTypes(String catalog, String schemaPattern,
            String typeNamePattern) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getSuperTypes(catalog, schemaPattern,
                    typeNamePattern));
        } finally {
            termContext();
        }
    }

    public String getSystemFunctions() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getSystemFunctions());
        } finally {
            termContext();
        }
    }

    public IResultSet getTablePrivileges(String catalog, String schemaPattern,
            String tableNamePattern) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getTablePrivileges(catalog, schemaPattern,
                    tableNamePattern));
        } finally {
            termContext();
        }
    }

    public IResultSet getTables(String catalog, String schemaPattern,
            String tableNamePattern, String[] types) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getTables(catalog, schemaPattern, tableNamePattern,
                    types));
        } finally {
            termContext();
        }
    }

    public IResultSet getTableTypes() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getTableTypes());
        } finally {
            termContext();
        }
    }

    public String getTimeDateFunctions() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getTimeDateFunctions());
        } finally {
            termContext();
        }
    }

    public IResultSet getTypeInfo() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getTypeInfo());
        } finally {
            termContext();
        }
    }

    public IResultSet getUDTs(String catalog, String schemaPattern,
            String typeNamePattern, int[] types) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getUDTs(catalog, schemaPattern, typeNamePattern,
                    types));
        } finally {
            termContext();
        }
    }

    public String getURL() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getURL());
        } finally {
            termContext();
        }
    }

    public String getUserName() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getUserName());
        } finally {
            termContext();
        }
    }

    public IResultSet getVersionColumns(String catalog, String schema,
            String table) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getVersionColumns(catalog, schema, table));
        } finally {
            termContext();
        }
    }

    public boolean insertsAreDetected(int type) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.insertsAreDetected(type));
        } finally {
            termContext();
        }
    }

    public boolean isCatalogAtStart() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.isCatalogAtStart());
        } finally {
            termContext();
        }
    }

    public boolean isReadOnly() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.isReadOnly());
        } finally {
            termContext();
        }
    }

    public boolean locatorsUpdateCopy() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.locatorsUpdateCopy());
        } finally {
            termContext();
        }
    }

    public boolean nullPlusNonNullIsNull() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.nullPlusNonNullIsNull());
        } finally {
            termContext();
        }
    }

    public boolean nullsAreSortedAtEnd() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.nullsAreSortedAtEnd());
        } finally {
            termContext();
        }
    }

    public boolean nullsAreSortedAtStart() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.nullsAreSortedAtStart());
        } finally {
            termContext();
        }
    }

    public boolean nullsAreSortedHigh() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.nullsAreSortedHigh());
        } finally {
            termContext();
        }
    }

    public boolean nullsAreSortedLow() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.nullsAreSortedLow());
        } finally {
            termContext();
        }
    }

    public boolean othersDeletesAreVisible(int type) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.othersDeletesAreVisible(type));
        } finally {
            termContext();
        }
    }

    public boolean othersInsertsAreVisible(int type) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.othersInsertsAreVisible(type));
        } finally {
            termContext();
        }
    }

    public boolean othersUpdatesAreVisible(int type) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.othersUpdatesAreVisible(type));
        } finally {
            termContext();
        }
    }

    public boolean ownDeletesAreVisible(int type) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.ownDeletesAreVisible(type));
        } finally {
            termContext();
        }
    }

    public boolean ownInsertsAreVisible(int type) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.ownInsertsAreVisible(type));
        } finally {
            termContext();
        }
    }

    public boolean ownUpdatesAreVisible(int type) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.ownUpdatesAreVisible(type));
        } finally {
            termContext();
        }
    }

    public boolean storesLowerCaseIdentifiers() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.storesLowerCaseIdentifiers());
        } finally {
            termContext();
        }
    }

    public boolean storesLowerCaseQuotedIdentifiers() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.storesLowerCaseQuotedIdentifiers());
        } finally {
            termContext();
        }
    }

    public boolean storesMixedCaseIdentifiers() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.storesMixedCaseIdentifiers());
        } finally {
            termContext();
        }
    }

    public boolean storesMixedCaseQuotedIdentifiers() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.storesMixedCaseQuotedIdentifiers());
        } finally {
            termContext();
        }
    }

    public boolean storesUpperCaseIdentifiers() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.storesUpperCaseIdentifiers());
        } finally {
            termContext();
        }
    }

    public boolean storesUpperCaseQuotedIdentifiers() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.storesUpperCaseQuotedIdentifiers());
        } finally {
            termContext();
        }
    }

    public boolean supportsAlterTableWithAddColumn() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsAlterTableWithAddColumn());
        } finally {
            termContext();
        }
    }

    public boolean supportsAlterTableWithDropColumn() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsAlterTableWithDropColumn());
        } finally {
            termContext();
        }
    }

    public boolean supportsANSI92EntryLevelSQL() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsANSI92EntryLevelSQL());
        } finally {
            termContext();
        }
    }

    public boolean supportsANSI92FullSQL() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsANSI92FullSQL());
        } finally {
            termContext();
        }
    }

    public boolean supportsANSI92IntermediateSQL() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsANSI92IntermediateSQL());
        } finally {
            termContext();
        }
    }

    public boolean supportsBatchUpdates() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsBatchUpdates());
        } finally {
            termContext();
        }
    }

    public boolean supportsCatalogsInDataManipulation() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsCatalogsInDataManipulation());
        } finally {
            termContext();
        }
    }

    public boolean supportsCatalogsInIndexDefinitions() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsCatalogsInIndexDefinitions());
        } finally {
            termContext();
        }
    }

    public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsCatalogsInPrivilegeDefinitions());
        } finally {
            termContext();
        }
    }

    public boolean supportsCatalogsInProcedureCalls() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsCatalogsInProcedureCalls());
        } finally {
            termContext();
        }
    }

    public boolean supportsCatalogsInTableDefinitions() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsCatalogsInTableDefinitions());
        } finally {
            termContext();
        }
    }

    public boolean supportsColumnAliasing() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsColumnAliasing());
        } finally {
            termContext();
        }
    }

    public boolean supportsConvert() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsConvert());
        } finally {
            termContext();
        }
    }

    public boolean supportsConvert(int fromType, int toType)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsConvert(fromType, toType));
        } finally {
            termContext();
        }
    }

    public boolean supportsCoreSQLGrammar() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsCoreSQLGrammar());
        } finally {
            termContext();
        }
    }

    public boolean supportsCorrelatedSubqueries() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsCorrelatedSubqueries());
        } finally {
            termContext();
        }
    }

    public boolean supportsDataDefinitionAndDataManipulationTransactions()
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData
                    .supportsDataDefinitionAndDataManipulationTransactions());
        } finally {
            termContext();
        }
    }

    public boolean supportsDataManipulationTransactionsOnly()
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsDataManipulationTransactionsOnly());
        } finally {
            termContext();
        }
    }

    public boolean supportsDifferentTableCorrelationNames() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsDifferentTableCorrelationNames());
        } finally {
            termContext();
        }
    }

    public boolean supportsExpressionsInOrderBy() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsExpressionsInOrderBy());
        } finally {
            termContext();
        }
    }

    public boolean supportsExtendedSQLGrammar() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsExtendedSQLGrammar());
        } finally {
            termContext();
        }
    }

    public boolean supportsFullOuterJoins() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsFullOuterJoins());
        } finally {
            termContext();
        }
    }

    public boolean supportsGetGeneratedKeys() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsGetGeneratedKeys());
        } finally {
            termContext();
        }
    }

    public boolean supportsGroupBy() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsGroupBy());
        } finally {
            termContext();
        }
    }

    public boolean supportsGroupByBeyondSelect() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsGroupByBeyondSelect());
        } finally {
            termContext();
        }
    }

    public boolean supportsGroupByUnrelated() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsGroupByUnrelated());
        } finally {
            termContext();
        }
    }

    public boolean supportsIntegrityEnhancementFacility() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsIntegrityEnhancementFacility());
        } finally {
            termContext();
        }
    }

    public boolean supportsLikeEscapeClause() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsLikeEscapeClause());
        } finally {
            termContext();
        }
    }

    public boolean supportsLimitedOuterJoins() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsLimitedOuterJoins());
        } finally {
            termContext();
        }
    }

    public boolean supportsMinimumSQLGrammar() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsMinimumSQLGrammar());
        } finally {
            termContext();
        }
    }

    public boolean supportsMixedCaseIdentifiers() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsMixedCaseIdentifiers());
        } finally {
            termContext();
        }
    }

    public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsMixedCaseQuotedIdentifiers());
        } finally {
            termContext();
        }
    }

    public boolean supportsMultipleOpenResults() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsMultipleOpenResults());
        } finally {
            termContext();
        }
    }

    public boolean supportsMultipleResultSets() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsMultipleResultSets());
        } finally {
            termContext();
        }
    }

    public boolean supportsMultipleTransactions() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsMultipleTransactions());
        } finally {
            termContext();
        }
    }

    public boolean supportsNamedParameters() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsNamedParameters());
        } finally {
            termContext();
        }
    }

    public boolean supportsNonNullableColumns() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsNonNullableColumns());
        } finally {
            termContext();
        }
    }

    public boolean supportsOpenCursorsAcrossCommit() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsOpenCursorsAcrossCommit());
        } finally {
            termContext();
        }
    }

    public boolean supportsOpenCursorsAcrossRollback() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsOpenCursorsAcrossRollback());
        } finally {
            termContext();
        }
    }

    public boolean supportsOpenStatementsAcrossCommit() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsOpenStatementsAcrossCommit());
        } finally {
            termContext();
        }
    }

    public boolean supportsOpenStatementsAcrossRollback() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsOpenStatementsAcrossRollback());
        } finally {
            termContext();
        }
    }

    public boolean supportsOrderByUnrelated() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsOrderByUnrelated());
        } finally {
            termContext();
        }
    }

    public boolean supportsOuterJoins() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsOuterJoins());
        } finally {
            termContext();
        }
    }

    public boolean supportsPositionedDelete() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsPositionedDelete());
        } finally {
            termContext();
        }
    }

    public boolean supportsPositionedUpdate() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsPositionedUpdate());
        } finally {
            termContext();
        }
    }

    public boolean supportsResultSetConcurrency(int type, int concurrency)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsResultSetConcurrency(type, concurrency));
        } finally {
            termContext();
        }
    }

    public boolean supportsResultSetHoldability(int holdability)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsResultSetHoldability(holdability));
        } finally {
            termContext();
        }
    }

    public boolean supportsResultSetType(int type) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsResultSetType(type));
        } finally {
            termContext();
        }
    }

    public boolean supportsSavepoints() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsSavepoints());
        } finally {
            termContext();
        }
    }

    public boolean supportsSchemasInDataManipulation() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsSchemasInDataManipulation());
        } finally {
            termContext();
        }
    }

    public boolean supportsSchemasInIndexDefinitions() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsSchemasInIndexDefinitions());
        } finally {
            termContext();
        }
    }

    public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsSchemasInPrivilegeDefinitions());
        } finally {
            termContext();
        }
    }

    public boolean supportsSchemasInProcedureCalls() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsSchemasInProcedureCalls());
        } finally {
            termContext();
        }
    }

    public boolean supportsSchemasInTableDefinitions() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsSchemasInTableDefinitions());
        } finally {
            termContext();
        }
    }

    public boolean supportsSelectForUpdate() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsSelectForUpdate());
        } finally {
            termContext();
        }
    }

    public boolean supportsStatementPooling() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsStatementPooling());
        } finally {
            termContext();
        }
    }

    public boolean supportsStoredProcedures() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsStoredProcedures());
        } finally {
            termContext();
        }
    }

    public boolean supportsSubqueriesInComparisons() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsSubqueriesInComparisons());
        } finally {
            termContext();
        }
    }

    public boolean supportsSubqueriesInExists() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsSubqueriesInExists());
        } finally {
            termContext();
        }
    }

    public boolean supportsSubqueriesInIns() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsSubqueriesInIns());
        } finally {
            termContext();
        }
    }

    public boolean supportsSubqueriesInQuantifieds() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsSubqueriesInQuantifieds());
        } finally {
            termContext();
        }
    }

    public boolean supportsTableCorrelationNames() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsTableCorrelationNames());
        } finally {
            termContext();
        }
    }

    public boolean supportsTransactionIsolationLevel(int level)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsTransactionIsolationLevel(level));
        } finally {
            termContext();
        }
    }

    public boolean supportsTransactions() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsTransactions());
        } finally {
            termContext();
        }
    }

    public boolean supportsUnion() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsUnion());
        } finally {
            termContext();
        }
    }

    public boolean supportsUnionAll() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsUnionAll());
        } finally {
            termContext();
        }
    }

    public boolean updatesAreDetected(int type) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.updatesAreDetected(type));
        } finally {
            termContext();
        }
    }

    public boolean usesLocalFilePerTable() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.usesLocalFilePerTable());
        } finally {
            termContext();
        }
    }

    public boolean usesLocalFiles() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.usesLocalFiles());
        } finally {
            termContext();
        }
    }

    public RowIdLifetime getRowIdLifetime() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getRowIdLifetime());
        } finally {
            termContext();
        }
    }

    public IResultSet getSchemas(String catalog, String schemaPattern)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getSchemas(catalog, schemaPattern));
        } finally {
            termContext();
        }
    }

    public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.supportsStoredFunctionsUsingCallSyntax());
        } finally {
            termContext();
        }
    }

    public boolean autoCommitFailureClosesAllResultSets() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.autoCommitFailureClosesAllResultSets());
        } finally {
            termContext();
        }
    }

    public IResultSet getClientInfoProperties() throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getClientInfoProperties());
        } finally {
            termContext();
        }
    }

    public IResultSet getFunctions(String catalog, String schemaPattern,
            String functionNamePattern) throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getFunctions(catalog, schemaPattern,
                    functionNamePattern));
        } finally {
            termContext();
        }
    }

    public IResultSet getFunctionColumns(String catalog, String schemaPattern,
            String functionNamePattern, String columnNamePattern)
            throws SQLException, RemoteException {
        initContext();
        try {
            return wrap(metaData.getFunctionColumns(catalog, schemaPattern,
                    functionNamePattern, columnNamePattern));
        } finally {
            termContext();
        }
    }
}
