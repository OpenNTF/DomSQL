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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Types;

import com.ibm.commons.util.NotImplementedException;
import com.ibm.commons.util.StringUtil;
import com.ibm.domino.domsql.sqlite.driver.SQLite;

/**
 * DomSQL database meta data.
 * @author priand
 */
public class DomSQLDatabaseMetaData extends DomSQLObject implements DatabaseMetaData {

    private DomSQLConnection connection;

    public DomSQLDatabaseMetaData(DomSQLConnection connection) {
        this.connection = connection;
    }
    
    public DomSQLConnection getConnection() {
        return connection;
    }
    
    public long getSqliteDb() {
        return connection.getSqliteDb();
    }

    public void checkConnectionOpen() throws SQLException {
        if(connection==null) {
            throw new SQLException("The connection is closed");
        }
    }

    public void close() throws SQLException {
        if(connection!=null) {
            connection = null;
        }
    }

    public String getDatabaseProductName() {
        return "DominoSQLite";
    }

    public String getDriverName() {
        return "DomSQL";
    }

    // Read the DB version directly from SQLite
    //SQLITE_VERSION        "3.7.6.3"
    public String getDatabaseProductVersion() throws SQLException {
        return SQLite.libversion();
    }
    public int getDatabaseMajorVersion() throws SQLException {
        String v = SQLite.libversion();
        int p1 = 0;
        int p2 = v.indexOf('.');
        return Integer.parseInt(v.substring(p1,p2));
        
    }
    public int getDatabaseMinorVersion() throws SQLException {
        String v = SQLite.libversion();
        int p1 = v.indexOf('.')+1;
        int p2 = v.indexOf('.',p1);
        return Integer.parseInt(v.substring(p1,p2));
    }
    
    // Driver version 1.0
    public String getDriverVersion() {
        return "1.0";
    }
    public int getDriverMajorVersion() {
        return 1;
    }
    public int getDriverMinorVersion() {
        return 0;
    }

    // Just JDBC 2.0 for now...
    public int getJDBCMajorVersion() {
        return 2;
    }
    public int getJDBCMinorVersion() {
        return 0;
    }

    public int getDefaultTransactionIsolation() {
        return Connection.TRANSACTION_SERIALIZABLE;
    }

    
    // TODO: fill these constants
    public int getMaxBinaryLiteralLength() {
        return 0;
    }

    public int getMaxCatalogNameLength() {
        return 0;
    }

    public int getMaxCharLiteralLength() {
        return 0;
    }

    public int getMaxColumnNameLength() {
        return 0;
    }

    public int getMaxColumnsInGroupBy() {
        return 0;
    }

    public int getMaxColumnsInIndex() {
        return 0;
    }

    public int getMaxColumnsInOrderBy() {
        return 0;
    }

    public int getMaxColumnsInSelect() {
        return 0;
    }

    public int getMaxColumnsInTable() {
        return 0;
    }

    public int getMaxConnections() {
        return 0;
    }

    public int getMaxCursorNameLength() {
        return 0;
    }

    public int getMaxIndexLength() {
        return 0;
    }

    public int getMaxProcedureNameLength() {
        return 0;
    }

    public int getMaxRowSize() {
        return 0;
    }

    public int getMaxSchemaNameLength() {
        return 0;
    }

    public int getMaxStatementLength() {
        return 0;
    }

    public int getMaxStatements() {
        return 250;
    }

    public int getMaxTableNameLength() {
        return 0;
    }

    public int getMaxTablesInSelect() {
        return 0;
    }

    public int getMaxUserNameLength() {
        return 0;
    }

    public int getResultSetHoldability() {
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }

    public int getSQLStateType() {
        return sqlStateSQL99;
    }


    public String getExtraNameCharacters() {
        return "";
    }

    public String getCatalogSeparator() {
        return ".";
    }

    public String getCatalogTerm() {
        return "catalog";
    }

    public String getSchemaTerm() {
        return "schema";
    }

    public String getProcedureTerm() {
        return "procedure";
    }

    public String getSearchStringEscape() {
        return null;
    }

    public String getIdentifierQuoteString() {
        return " "; // Space means not supported in JDBC JavaDoc
    }

    public String getSQLKeywords() {
        return "";
    }

    public String getNumericFunctions() {
        return "";
    }

    public String getStringFunctions() {
        return "";
    }

    public String getSystemFunctions() {
        return "";
    }

    public String getTimeDateFunctions() {
        return "";
    }

    public String getURL() {
        return connection.getUrl();
    }

    public String getUserName() {
        return connection.getUserName();
    }

    public boolean allProceduresAreCallable() {
        return false;
    }

    public boolean allTablesAreSelectable() {
        return true;
    }

    public boolean dataDefinitionCausesTransactionCommit() {
        return false;
    }

    public boolean dataDefinitionIgnoredInTransactions() {
        return false;
    }

    public boolean doesMaxRowSizeIncludeBlobs() {
        return false;
    }

    public boolean deletesAreDetected(int type) {
        return false;
    }

    public boolean insertsAreDetected(int type) {
        return false;
    }

    public boolean isCatalogAtStart() {
        return true;
    }

    public boolean locatorsUpdateCopy() {
        return false;
    }

    public boolean nullPlusNonNullIsNull() {
        return true;
    }

    public boolean nullsAreSortedAtEnd() {
        return !nullsAreSortedAtStart();
    }

    public boolean nullsAreSortedAtStart() {
        return true;
    }

    public boolean nullsAreSortedHigh() {
        return true;
    }

    public boolean nullsAreSortedLow() {
        return !nullsAreSortedHigh();
    }

    public boolean othersDeletesAreVisible(int type) {
        return false;
    }

    public boolean othersInsertsAreVisible(int type) {
        return false;
    }

    public boolean othersUpdatesAreVisible(int type) {
        return false;
    }

    public boolean ownDeletesAreVisible(int type) {
        return false;
    }

    public boolean ownInsertsAreVisible(int type) {
        return false;
    }

    public boolean ownUpdatesAreVisible(int type) {
        return false;
    }

    public boolean storesLowerCaseIdentifiers() {
        return false;
    }

    public boolean storesLowerCaseQuotedIdentifiers() {
        return false;
    }

    public boolean storesMixedCaseIdentifiers() {
        return true;
    }

    public boolean storesMixedCaseQuotedIdentifiers() {
        return false;
    }

    public boolean storesUpperCaseIdentifiers() {
        return false;
    }

    public boolean storesUpperCaseQuotedIdentifiers() {
        return false;
    }

    public boolean supportsAlterTableWithAddColumn() {
        return false;
    }

    public boolean supportsAlterTableWithDropColumn() {
        return false;
    }

    public boolean supportsANSI92EntryLevelSQL() {
        return false;
    }

    public boolean supportsANSI92FullSQL() {
        return false;
    }

    public boolean supportsANSI92IntermediateSQL() {
        return false;
    }

    public boolean supportsBatchUpdates() {
        return true;
    }

    public boolean supportsCatalogsInDataManipulation() {
        return false;
    }

    public boolean supportsCatalogsInIndexDefinitions() {
        return false;
    }

    public boolean supportsCatalogsInPrivilegeDefinitions() {
        return false;
    }

    public boolean supportsCatalogsInProcedureCalls() {
        return false;
    }

    public boolean supportsCatalogsInTableDefinitions() {
        return false;
    }

    public boolean supportsColumnAliasing() {
        return true;
    }

    public boolean supportsConvert() {
        return false;
    }

    public boolean supportsConvert(int fromType, int toType) {
        return false;
    }

    public boolean supportsCorrelatedSubqueries() {
        return false;
    }

    public boolean supportsDataDefinitionAndDataManipulationTransactions() {
        return true;
    }

    public boolean supportsDataManipulationTransactionsOnly() {
        return false;
    }

    public boolean supportsDifferentTableCorrelationNames() {
        return false;
    }

    public boolean supportsExpressionsInOrderBy() {
        return true;
    }

    public boolean supportsMinimumSQLGrammar() {
        return true;
    }

    public boolean supportsCoreSQLGrammar() {
        return true;
    }

    public boolean supportsExtendedSQLGrammar() {
        return false;
    }

    public boolean supportsLimitedOuterJoins() {
        return true;
    }

    public boolean supportsFullOuterJoins() {
        return false;
    }

    public boolean supportsGetGeneratedKeys() {
        return false;
    }

    public boolean supportsGroupBy() {
        return true;
    }

    public boolean supportsGroupByBeyondSelect() {
        return false;
    }

    public boolean supportsGroupByUnrelated() {
        return false;
    }

    public boolean supportsIntegrityEnhancementFacility() {
        return false;
    }

    public boolean supportsLikeEscapeClause() {
        return false;
    }

    public boolean supportsMixedCaseIdentifiers() {
        return true;
    }

    public boolean supportsMixedCaseQuotedIdentifiers() {
        return false;
    }

    public boolean supportsMultipleOpenResults() {
        return false;
    }

    public boolean supportsMultipleResultSets() {
        return false;
    }

    public boolean supportsMultipleTransactions() {
        return true;
    }

    public boolean supportsNamedParameters() {
        return true;
    }

    public boolean supportsNonNullableColumns() {
        return true;
    }

    public boolean supportsOpenCursorsAcrossCommit() {
        return false;
    }

    public boolean supportsOpenCursorsAcrossRollback() {
        return false;
    }

    public boolean supportsOpenStatementsAcrossCommit() {
        return false;
    }

    public boolean supportsOpenStatementsAcrossRollback() {
        return false;
    }

    public boolean supportsOrderByUnrelated() {
        return false;
    }

    public boolean supportsOuterJoins() {
        return true;
    }

    public boolean supportsPositionedDelete() {
        return false;
    }

    public boolean supportsPositionedUpdate() {
        return false;
    }

    public boolean supportsResultSetConcurrency(int type, int concurrency) {
        return type == ResultSet.TYPE_FORWARD_ONLY
                && concurrency == ResultSet.CONCUR_READ_ONLY;
    }

    public boolean supportsResultSetHoldability(int holdbility) {
        return holdbility == ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }

    public boolean supportsResultSetType(int type) {
        return type == ResultSet.TYPE_FORWARD_ONLY;
    }

    public boolean supportsSavepoints() {
        return false;
    }

    public boolean supportsSchemasInDataManipulation() {
        return false;
    }

    public boolean supportsSchemasInIndexDefinitions() {
        return false;
    }

    public boolean supportsSchemasInPrivilegeDefinitions() {
        return false;
    }

    public boolean supportsSchemasInProcedureCalls() {
        return false;
    }

    public boolean supportsSchemasInTableDefinitions() {
        return false;
    }

    public boolean supportsSelectForUpdate() {
        return false;
    }

    public boolean supportsStatementPooling() {
        return false;
    }

    public boolean supportsStoredProcedures() {
        return false;
    }

    public boolean supportsSubqueriesInComparisons() {
        return false;
    }

    public boolean supportsSubqueriesInExists() {
        return true;
    } 

    public boolean supportsSubqueriesInIns() {
        return true;
    } 

    public boolean supportsSubqueriesInQuantifieds() {
        return false;
    }

    public boolean supportsTableCorrelationNames() {
        return false;
    }

    public boolean supportsTransactionIsolationLevel(int level) {
        return level == Connection.TRANSACTION_SERIALIZABLE;
    }

    public boolean supportsTransactions() {
        return true;
    }

    public boolean supportsUnion() {
        return true;
    }

    public boolean supportsUnionAll() {
        return true;
    }

    public boolean updatesAreDetected(int type) {
        return false;
    }

    public boolean usesLocalFilePerTable() {
        return false;
    }

    public boolean usesLocalFiles() {
        return true;
    }

    public boolean isReadOnly() throws SQLException {
        return connection.isReadOnly();
    }

    public DomSQLResultSet getAttributes(String c, String s, String t, String a)
            throws SQLException {
        DomSQLPreparedStatement st = connection.prepareStatement(
                      "SELECT "
                    + "NULL AS TYPE_CAT," 
                    + "NULL AS TYPE_SCHEM,"
                    + "NULL AS TYPE_NAME," 
                    + "NULL AS ATTR_NAME,"
                    + "NULL AS DATA_TYPE," 
                    + "NULL AS ATTR_TYPE_NAME,"
                    + "NULL AS ATTR_SIZE," 
                    + "NULL AS DECIMAL_DIGITS,"
                    + "NULL AS NUM_PREC_RADIX," 
                    + "NULL AS NULLABLE,"
                    + "NULL AS REMARKS," 
                    + "NULL AS ATTR_DEF,"
                    + "NULL AS SQL_DATA_TYPE," 
                    + "NULL AS SQL_DATETIME_SUB,"
                    + "NULL AS CHAR_OCTET_LENGTH,"
                    + "NULL AS ORDINAL_POSITION," 
                    + "NULL AS IS_NULLABLE,"
                    + "NULL AS SCOPE_CATALOG," 
                    + "NULL AS SCOPE_SCHEMA,"
                    + "NULL AS SCOPE_TABLE,"
                    + "NULL AS SOURCE_DATA_TYPE limit 0;");
        DomSQLResultSet rs = st.executeQuery();
        rs.setCloseStatementOnClose(true);
        return rs;
    }

    public DomSQLResultSet getBestRowIdentifier(String c, String s, String t,
            int scope, boolean n) throws SQLException {
        DomSQLPreparedStatement st = connection.prepareStatement(
                      "SELECT "
                    + "NULL AS SCOPE," 
                    + "NULL AS COLUMN_NAME,"
                    + "NULL AS DATA_TYPE," 
                    + "NULL AS TYPE_NAME,"
                    + "NULL AS COLUMN_SIZE," 
                    + "NULL AS BUFFER_LENGTH,"
                    + "NULL AS DECIMAL_DIGITS,"
                    + "NULL AS PSEUDO_COLUMN limit 0;");
        DomSQLResultSet rs = st.executeQuery();
        rs.setCloseStatementOnClose(true);
        return rs;
    }

    public DomSQLResultSet getColumnPrivileges(String c, String s, String t,
            String colPat) throws SQLException {
        DomSQLPreparedStatement st = connection.prepareStatement(
                      "SELECT "
                    + "NULL AS TABLE_CAT," 
                    + "NULL AS TABLE_SCHEM,"
                    + "NULL AS TABLE_NAME," 
                    + "NULL AS COLUMN_NAME,"
                    + "NULL AS GRANTOR," 
                    + "NULL AS GRANTEE,"
                    + "NULL AS PRIVILEGE," 
                    + "NULL AS IS_GRANTABLE limit 0;");
        DomSQLResultSet rs = st.executeQuery();
        rs.setCloseStatementOnClose(true);
        return rs;
    }

    public DomSQLResultSet getColumns(String c, String s, String tbl, String colPat) throws SQLException {
        checkConnectionOpen();
        
        tbl = tbl.toUpperCase();
        DomSQLStatement stat = connection.createStatement();

        StringBuilder sql = new StringBuilder(
                  "SELECT " 
                + "NULL AS TABLE_CAT," 
                + "NULL AS TABLE_SCHEM," 
                + "'"+ SQLite.escapeSqlString(tbl)+ "' "+ "AS TABLE_NAME,"
                + "cn AS COLUMN_NAME,"
                + "ct AS DATA_TYPE,"
                + "tn AS TYPE_NAME,"
                + "2000000000 AS COLUMN_SIZE,"
                + "2000000000 AS BUFFER_LENGTH,"
                + "10 AS DECIMAL_DIGITS,"
                + "10 AS NUM_PREC_RADIX,"
                + "colnullable AS NULLABLE,"
                + "NULL AS REMARKS,"
                + "NULL AS COLUMN_DEF,"
                + "0 AS SQL_DATA_TYPE,"
                + "0 AS SQL_DATETIME_SUB,"
                + "2000000000 AS CHAR_OCTET_LENGTH,"
                + "ordpos AS ORDINAL_POSITION,"
                + "(case colnullable when 0 then 'N' when 1 then 'Y' else '' end) AS IS_NULLABLE," 
                + "NULL AS SCOPE_CATLOG,"
                + "NULL AS SCOPE_SCHEMA," 
                + "NULL AS SCOPE_TABLE,"
                + "NULL AS SOURCE_DATA_TYPE from (");

        DomSQLResultSet rs = stat.executeQuery("PRAGMA table_info ('" + SQLite.escapeSqlString(tbl) + "');");

        boolean colFound = false;
        for (int i = 0; rs.next(); i++) {
            String colName = rs.getString(2);
            String colType = rs.getString(3);
            String colNotNull = rs.getString(4);

            int colNullable = 2;
            if (colNotNull != null) {
                colNullable = colNotNull.equals("0") ? 1 : 0;
            }
            if (colFound) {
                sql.append(" UNION ALL ");
            }
            colFound = true;

            colType = colType == null ? "TEXT" : colType.toUpperCase();
            int colJavaType = -1;
            if (colType.equals("INT") || colType.equals("INTEGER")) {
                colJavaType = Types.INTEGER;
            } else if (colType.equals("TEXT")) {
                colJavaType = Types.VARCHAR;
            } else if (colType.equals("FLOAT")) {
                colJavaType = Types.FLOAT;
            } else {
                colJavaType = Types.VARCHAR;
            }

            sql.append("SELECT ");
            sql.append(i);
            sql.append(" AS ordpos, ");
            sql.append(colNullable);
            sql.append(" AS colnullable, '");
            sql.append(colJavaType);
            sql.append("' AS ct, '");
            sql.append(SQLite.escapeSqlString(colName));
            sql.append("' AS cn, '");
            sql.append(SQLite.escapeSqlString(colType));
            sql.append("' AS tn");

            if (colPat != null) {
                sql.append(" WHERE upper(cn) like upper('");
                sql.append(SQLite.escapeSqlString(colPat));
                sql.append("')");
            }
        }
        sql.append(colFound ? ");" : "SELECT NULL AS ordpos, NULL AS col, NULL AS cn, NULL AS tn) limit 0;");
        rs.close();

        rs = stat.executeQuery(sql.toString());
        rs.setCloseStatementOnClose(true);
        return rs;
    }

    public DomSQLResultSet getCrossReference(String pc, String ps, String pt,
            String fc, String fs, String ft) throws SQLException {
        DomSQLPreparedStatement st = connection.prepareStatement(
                      "SELECT "
                    + "NULL AS PKTABLE_CAT," 
                    + "NULL AS PKTABLE_SCHEM,"
                    + "NULL AS PKTABLE_NAME," 
                    + "NULL AS PKCOLUMN_NAME,"
                    + "NULL AS FKTABLE_CAT," 
                    + "NULL AS FKTABLE_SCHEM,"
                    + "NULL AS FKTABLE_NAME," 
                    + "NULL AS FKCOLUMN_NAME,"
                    + "NULL AS KEY_SEQ," 
                    + "NULL AS UPDATE_RULE,"
                    + "NULL AS DELETE_RULE," 
                    + "NULL AS FK_NAME,"
                    + "NULL AS PK_NAME," 
                    + "NULL AS DEFERRABILITY"
                    + " limit 0;");
        DomSQLResultSet rs = st.executeQuery();
        rs.setCloseStatementOnClose(true);
        return rs;
    }

    public DomSQLResultSet getSchemas() throws SQLException {
        DomSQLPreparedStatement st = connection.prepareStatement(
                      "SELECT "
                    + "NULL AS TABLE_SCHEM,"
                    + "NULL AS TABLE_CATALOG"
                    + "limit 0;");
        DomSQLResultSet rs = st.executeQuery();
        rs.setCloseStatementOnClose(true);
        return rs;
    }

    public DomSQLResultSet getCatalogs() throws SQLException {
        DomSQLPreparedStatement st = connection.prepareStatement(
                    "SELECT NULL AS TABLE_CAT limit 0;");
        DomSQLResultSet rs = st.executeQuery();
        rs.setCloseStatementOnClose(true);
        return rs;
    }

    public DomSQLResultSet getPrimaryKeys(String c, String s, String table)
            throws SQLException {
        DomSQLStatement st = connection.createStatement();
        DomSQLResultSet rs = st.executeQuery(
                    "pragma table_info('" + SQLite.escapeSqlString(table) + "');");

        StringBuilder b = new StringBuilder(
                  "SELECT " 
                + "NULL AS TABLE_CAT," 
                + "NULL AS TABLE_SCHEM," 
                + "'" + SQLite.escapeSqlString(table) + "'" +" AS TABLE_NAME," 
                + "cn AS COLUMN_NAME,"
                + "0 AS KEY_SEQ," 
                + "NULL AS PK_NAME from (");

        boolean hasCol = false;
        for (int i = 0; rs.next(); i++) {
            String colName = rs.getString(2);
            if (!rs.getBoolean(6)) {
                i--;
                continue;
            }
            if (i > 0) {
                b.append(" union all ");
            }
            b.append("SELECT '");
            b.append(SQLite.escapeSqlString(colName));
            b.append("' AS cn");
            hasCol = true;
        }
        if(hasCol) {
            b.append(");");
        } else {
            b.append("SELECT NULL AS cn) limit 0;");
        }
        rs.close();

        String sql = b.toString();
        rs = st.executeQuery(sql);
        rs.setCloseStatementOnClose(true);
        return rs;
    }

    public DomSQLResultSet getExportedKeys(String c, String s, String t)
            throws SQLException {
        DomSQLPreparedStatement st = connection.prepareStatement(
                      "SELECT "
                    + "NULL AS PKTABLE_CAT," 
                    + "NULL AS PKTABLE_SCHEM,"
                    + "NULL AS PKTABLE_NAME," 
                    + "NULL AS PKCOLUMN_NAME,"
                    + "NULL AS FKTABLE_CAT," 
                    + "NULL AS FKTABLE_SCHEM,"
                    + "NULL AS FKTABLE_NAME," 
                    + "NULL AS FKCOLUMN_NAME,"
                    + "NULL AS KEY_SEQ," 
                    + "NULL AS UPDATE_RULE,"
                    + "NULL AS DELETE_RULE," 
                    + "NULL AS FK_NAME,"
                    + "NULL AS PK_NAME," 
                    + "NULL AS DEFERRABILITY limit 0;");
        DomSQLResultSet rs = st.executeQuery();
        rs.setCloseStatementOnClose(true);
        return rs;
    }

    public DomSQLResultSet getImportedKeys(String c, String s, String t)
            throws SQLException {
        throw new NotImplementedException();
    }

    public DomSQLResultSet getIndexInfo(String c, String s, String t, boolean u,
            boolean approximate) throws SQLException {
        throw new NotImplementedException();
    }

    public DomSQLResultSet getProcedureColumns(String c, String s, String p,
            String colPat) throws SQLException {
        DomSQLPreparedStatement st = connection.prepareStatement(
                      "SELECT "
                    + "NULL AS PROCEDURE_CAT," 
                    + "NULL AS PROCEDURE_SCHEM,"
                    + "NULL AS PROCEDURE_NAME," 
                    + "NULL AS COLUMN_NAME,"
                    + "NULL AS COLUMN_TYPE," 
                    + "NULL AS DATA_TYPE,"
                    + "NULL AS TYPE_NAME," 
                    + "NULL AS PRECISION,"
                    + "NULL AS LENGTH," 
                    + "NULL AS SCALE,"
                    + "NULL AS RADIX," 
                    + "NULL AS NULLABLE,"
                    + "NULL AS REMARKS limit 0;");
        DomSQLResultSet rs = st.executeQuery();
        rs.setCloseStatementOnClose(true);
        return rs;
    }

    public DomSQLResultSet getProcedures(String c, String s, String p)
            throws SQLException {
        DomSQLPreparedStatement st = connection.prepareStatement(
                      "SELECT "
                    + "NULL AS PROCEDURE_CAT," 
                    + "NULL AS PROCEDURE_SCHEM,"
                    + "NULL AS PROCEDURE_NAME," 
                    + "NULL AS UNDEF1,"
                    + "NULL AS UNDEF2," 
                    + "NULL AS UNDEF3,"
                    + "NULL AS REMARKS," 
                    + "NULL AS PROCEDURE_TYPE limit 0;");
        DomSQLResultSet rs = st.executeQuery();
        rs.setCloseStatementOnClose(true);
        return rs;
    }

    public DomSQLResultSet getSuperTables(String c, String s, String t)
            throws SQLException {
        DomSQLPreparedStatement st = connection.prepareStatement(
                      "SELECT "
                    + "NULL AS TABLE_CAT," 
                    + "NULL AS TABLE_SCHEM,"
                    + "NULL AS TABLE_NAME,"
                    + "NULL AS SUPERTABLE_NAME limit 0;");
        DomSQLResultSet rs = st.executeQuery();
        rs.setCloseStatementOnClose(true);
        return rs;
    }

    public DomSQLResultSet getSuperTypes(String c, String s, String t)
            throws SQLException {
        DomSQLPreparedStatement st = connection.prepareStatement(
                      "SELECT "
                    + "NULL AS TYPE_CAT," 
                    + "NULL AS TYPE_SCHEM,"
                    + "NULL AS TYPE_NAME," 
                    + "NULL AS SUPERTYPE_CAT,"
                    + "NULL AS SUPERTYPE_SCHEM,"
                    + "NULL AS SUPERTYPE_NAME limit 0;");
        DomSQLResultSet rs = st.executeQuery();
        rs.setCloseStatementOnClose(true);
        return rs;
    }

    public DomSQLResultSet getTablePrivileges(String c, String s, String t)
            throws SQLException {
        DomSQLPreparedStatement st = connection.prepareStatement(
                      "SELECT "
                    + "NULL AS TABLE_CAT," 
                    + "NULL AS TABLE_SCHEM,"
                    + "NULL AS TABLE_NAME," 
                    + "NULL AS GRANTOR,"
                    + "NULL AS GRANTEE," 
                    + "NULL AS PRIVILEGE,"
                    + "NULL AS IS_GRANTABLE limit 0;");
        DomSQLResultSet rs = st.executeQuery();
        rs.setCloseStatementOnClose(true);
        return rs;
    }

    public DomSQLResultSet getTables(String c, String s, String t, String[] types) throws SQLException {
        checkConnectionOpen();
        StringBuilder b = new StringBuilder();
        b.append( "SELECT " 
                + "NULL AS TABLE_CAT," 
                + "NULL AS TABLE_SCHEM,"
                + "upper(name) AS TABLE_NAME," 
                + "upper(type) AS TABLE_TYPE,"
                + "NULL AS REMARKS," 
                + "NULL AS TYPE_CAT,"
                + "NULL AS TYPE_SCHEM," 
                + "NULL AS TYPE_NAME,"
                + "NULL AS SELF_REFERENCING_COL_NAME,"
                + "NULL AS REF_GENERATION"
                + " FROM (SELECT name, type FROM sqlite_master UNION ALL SELECT name, type FROM sqlite_temp_master)");
        String where = " WHERE ";
        if(StringUtil.isNotEmpty(t)) {
            b.append(where);
            where = " AND ";
            b.append("NAME LIKE '");
            b.append(SQLite.escapeSqlString(t));
            b.append("'");
        }
        if(types != null) {
            b.append(where);
            where = " AND ";
            b.append("TABLE_TYPE in (");
            for (int i = 0; i < types.length; i++) {
                if (i > 0) {
                    b.append(", ");
                }
                b.append("'");
                b.append(SQLite.escapeSqlString(types[i].toUpperCase()));
                b.append("'");
            }
            b.append(")");
        }
        b.append(";");

        String sql = b.toString();
        DomSQLResultSet rs = connection.createStatement().executeQuery(sql);
        rs.setCloseStatementOnClose(true);
        return rs;
    }

    public DomSQLResultSet getTableTypes() throws SQLException {
        checkConnectionOpen();
        DomSQLPreparedStatement st = connection.prepareStatement(
                      "SELECT 'TABLE' AS TABLE_TYPE"
                    + " union select 'VIEW' AS TABLE_TYPE;");
        DomSQLResultSet rs = st.executeQuery();
        rs.setCloseStatementOnClose(true);
        return rs;
    }

    public DomSQLResultSet getTypeInfo() throws SQLException {
        DomSQLPreparedStatement st = connection.prepareStatement(
                      "SELECT " 
                    + "tn AS TYPE_NAME,"
                    + "dt AS DATA_TYPE," 
                    + "0 AS PRECISION,"
                    + "NULL AS LITERAL_PREFIX," 
                    + "NULL AS LITERAL_SUFFIX,"
                    + "NULL AS CREATE_PARAMS,"
                    + typeNullable
                    + " AS NULLABLE,"
                    + "1 AS CASE_SENSITIVE,"
                    + typeSearchable
                    + " AS SEARCHABLE,"
                    + "0 AS UNSIGNED_ATTRIBUTE,"
                    + "0 AS FIXED_PREC_SCALE,"
                    + "0 AS AUTO_INCREMENT,"
                    + "NULL AS LOCAL_TYPE_NAME,"
                    + "0 AS MINIMUM_SCALE,"
                    + "0 AS MAXIMUM_SCALE,"
                    + "0 AS SQL_DATA_TYPE,"
                    + "0 AS SQL_DATETIME_SUB,"
                    + "10 AS NUM_PREC_RADIX from ("
                    + " select 'BLOB' AS tn,"
                    + Types.BLOB
                    + " AS dt union"
                    + " select 'NULL' AS tn,"
                    + Types.NULL
                    + " AS dt union"
                    + " select 'REAL' AS tn,"
                    + Types.REAL
                    + " AS dt union"
                    + " select 'TEXT' AS tn,"
                    + Types.VARCHAR
                    + " AS dt union"
                    + " select 'INTEGER' AS tn,"
                    + Types.INTEGER 
                    + " AS dt" 
                    + ") ORDER BY TYPE_NAME;");
        DomSQLResultSet rs = st.executeQuery();
        rs.setCloseStatementOnClose(true);
        return rs;
    }

    public DomSQLResultSet getUDTs(String c, String s, String t, int[] types)
            throws SQLException {
        DomSQLPreparedStatement st = connection.prepareStatement(
                      "SELECT " 
                    + "NULL AS TYPE_CAT,"
                    + "NULL AS TYPE_SCHEM,"
                    + "NULL AS TYPE_NAME,"
                    + "NULL AS CLASS_NAME," 
                    + "NULL AS DATA_TYPE,"
                    + "NULL AS REMARKS," 
                    + "NULL AS BASE_TYPE limit 0;");
        DomSQLResultSet rs = st.executeQuery();
        rs.setCloseStatementOnClose(true);
        return rs;
    }

    public DomSQLResultSet getVersionColumns(String c, String s, String t)
            throws SQLException {
        DomSQLPreparedStatement st = connection.prepareStatement(
                     "SELECT "
                    + "NULL AS SCOPE," 
                    + "NULL AS COLUMN_NAME,"
                    + "NULL AS DATA_TYPE," 
                    + "NULL AS TYPE_NAME,"
                    + "NULL AS COLUMN_SIZE," 
                    + "NULL AS BUFFER_LENGTH,"
                    + "NULL AS DECIMAL_DIGITS,"
                    + "NULL AS PSEUDO_COLUMN limit 0;");
        DomSQLResultSet rs = st.executeQuery();
        rs.setCloseStatementOnClose(true);
        return rs;
    }

    DomSQLResultSet getGeneratedKeys() throws SQLException {
        DomSQLPreparedStatement st = connection.prepareStatement(
                "SELECT last_insert_rowid();");
        DomSQLResultSet rs = st.executeQuery();
        rs.setCloseStatementOnClose(true);
        return rs;
    }

    public Struct createStruct(String t, Object[] attr) throws SQLException {
        throw new NotImplementedException();
    }

    public DomSQLResultSet getFunctionColumns(String a, String b, String c, String d) throws SQLException {
        throw new NotImplementedException();
    }

    
    // JRE 1.5 //////////////////////////////////////////////////////

    //@Override
    public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public DomSQLResultSet getClientInfoProperties() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public DomSQLResultSet getFunctions(String catalog, String schemaPattern,
            String functionNamePattern) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public RowIdLifetime getRowIdLifetime() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public DomSQLResultSet getSchemas(String catalog, String schemaPattern)
            throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new NotImplementedException();
    }

    //@Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new NotImplementedException();
    }
}
