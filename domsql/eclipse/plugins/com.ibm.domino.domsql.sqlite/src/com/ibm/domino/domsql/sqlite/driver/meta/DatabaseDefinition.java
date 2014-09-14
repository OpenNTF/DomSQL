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
package com.ibm.domino.domsql.sqlite.driver.meta;

import static com.ibm.domino.domsql.sqlite.driver.Constants.COLTYPE_UNKNOWN;
import static com.ibm.domino.domsql.sqlite.driver.Constants.COLTYPE_DATE;
import static com.ibm.domino.domsql.sqlite.driver.Constants.COLTYPE_NUMBER;
import static com.ibm.domino.domsql.sqlite.driver.Constants.COLTYPE_INTEGER;
import static com.ibm.domino.domsql.sqlite.driver.Constants.COLTYPE_BOOLEAN;
import static com.ibm.domino.domsql.sqlite.driver.Constants.COLTYPE_STRING;
import static com.ibm.domino.domsql.sqlite.driver.Constants.SYSCOL_ANYUNREAD;
import static com.ibm.domino.domsql.sqlite.driver.Constants.SYSCOL_CHILDREN;
import static com.ibm.domino.domsql.sqlite.driver.Constants.SYSCOL_CLASS;
import static com.ibm.domino.domsql.sqlite.driver.Constants.SYSCOL_DESCENDANTS;
import static com.ibm.domino.domsql.sqlite.driver.Constants.SYSCOL_ID;
import static com.ibm.domino.domsql.sqlite.driver.Constants.SYSCOL_LEVELS;
import static com.ibm.domino.domsql.sqlite.driver.Constants.SYSCOL_POSITION;
import static com.ibm.domino.domsql.sqlite.driver.Constants.SYSCOL_ROWID;
import static com.ibm.domino.domsql.sqlite.driver.Constants.SYSCOL_SCORE;
import static com.ibm.domino.domsql.sqlite.driver.Constants.SYSCOL_SIBLINGS;
import static com.ibm.domino.domsql.sqlite.driver.Constants.SYSCOL_UNID;
import static com.ibm.domino.domsql.sqlite.driver.Constants.SYSCOL_UNREAD;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.ibm.commons.Platform;
import com.ibm.commons.util.QuickSort;
import com.ibm.commons.util.StringUtil;
import com.ibm.domino.domsql.sqlite.DomSQLException;
import com.ibm.domino.domsql.sqlite.driver.Constants;
import com.ibm.domino.domsql.sqlite.driver.Context;
import com.ibm.domino.domsql.sqlite.driver.jni.DomSQL;
import com.ibm.domino.domsql.sqlite.driver.jni.JNIUtils;
import com.ibm.domino.domsql.sqlite.driver.jni.NotesAPI;
import com.ibm.domino.domsql.sqlite.driver.meta.DominoViewTable.Column;
import com.ibm.domino.domsql.sqlite.driver.meta.ViewDesign.Index;
import com.ibm.domino.domsql.sqlite.driver.meta.ViewDesign.IndexEntry;

/**
 * SQLLite database definition and virtual table generation.
 * 
 * @author priand
 */
public class DatabaseDefinition {

    public static final String DEFAULT_SYSTEM_COLUMNS = SYSCOL_ID;
    //public static final String DEFAULT_SYSTEM_COLUMNS = SYSCOL_ID + "|" + SYSCOL_UNID;
    
    
	private static final int SECRET_CODE = 7934; //(int)(Math.random()*10000);
	static {
		DomSQL.setSecretCode(SECRET_CODE);
	}
    
    public static final String DOMSQL_MODULE = "domsql";
    
    public static final int COLIDX_ID           = -1;
    public static final int COLIDX_UNID         = -2;
    public static final int COLIDX_CLASS        = -3; 
    public static final int COLIDX_SIBLINGS     = -4; 
    public static final int COLIDX_CHILDREN     = -5; 
    public static final int COLIDX_DESCENDANTS  = -6; 
    public static final int COLIDX_ANYUNREAD    = -7; 
    public static final int COLIDX_LEVELS       = -8; 
    public static final int COLIDX_SCORE        = -9; 
    public static final int COLIDX_UNREAD       = -10; 
    public static final int COLIDX_POSITION     = -11; 
    public static final int COLIDX_ROWID     	= -12; 

    public static final int COLIDX_INVALID      = -9999;
    
    private String nsfPath;
    private String domSqlName;
    private List<DominoNsf> nsfs = new ArrayList<DominoNsf>();
    private List<String> sqlStatements = new ArrayList<String>();
    
    private String sqliteFileName;
    
	//
	// SQLite generation constants
	//
	private static final String DEF_COLPREFIX = "col";
	private static final int MAX_TABLENAMELENGTH = 128;
	private static final int MAX_COLUMNNAMELENGTH = 128;

	// For debugging only
	private static final int MAX_DEFVIEWS = Integer.MAX_VALUE;
	
    public DatabaseDefinition(String nsfPath, String domSqlName) {
        this.nsfPath = nsfPath;
        this.domSqlName = domSqlName;
    }

    public String getDomSqlName() {
        return domSqlName;
    }
    public void setDomSqlName(String domSqlName) {
        this.domSqlName = domSqlName;
    }

    public String getNsfPath() {
        return nsfPath;
    }
    public void setNsfPath(String nsfPath) {
        this.nsfPath = nsfPath;
    }

    public List<DominoNsf> getNsfs() {
        return nsfs;
    }
    public void setNsfs(List<DominoNsf> nsfs) {
        this.nsfs = nsfs;
    }

    public List<String> getSqlStatements() {
        return sqlStatements;
    }
    public void setSqlStatements(List<String> sqlStatements) {
        this.sqlStatements = sqlStatements;
    }

    public String getSqliteFileName() {
        return sqliteFileName;
    }
    public void setSqliteFileName(String sqliteFileName) {
        this.sqliteFileName = sqliteFileName;
    }

    public void readDomsql(Context context) throws SQLException {
        readDominoSchema(context);
    }

    public List<String> generateInitSql(Context context) throws SQLException {
        ArrayList<String> tables = new ArrayList<String>();
        Map<String,DominoVirtualTable> vts = new HashMap<String,DominoVirtualTable>(); 
        
        for(int i=0; i<getNsfs().size(); i++) {
            DominoNsf nsf = getNsfs().get(i);

            // Ensure that the database is available
			long hDB = context.getDBHandle(nsf.getDbPath());
			if(hDB==0) {
				JNIUtils.println(StringUtil.format("Cannot access database {0} probably because of security settings. Please ensure that this database exists and can be accessed by this user ({1})", nsf.getDbPath(), NotesAPI.SECKFMGetUserName() ));
				continue;
			}

			// First add the explicit tables
			List<DominoViewTable> ets = nsf.getViewTables();
			if(ets != null && !ets.isEmpty()) {
				for(DominoViewTable vt : ets) {
					String tb = vt.getTableName().toUpperCase();
					vts.put(tb, vt);
				}
			}

			// Create the default tables, if requested
			// Also ensure that the generated name are not colliding
			if(nsf.isDefaultViews()) {
				// Browse all the views
				List<String> views = new ArrayList<String>();
				DomSQL.readViewList(hDB, views);
				
				int nDefViews = 0;					
				for(String view : views) {
					if((nDefViews++)>=MAX_DEFVIEWS) {
						break;
					}
					String viewName = viewName(view);
					if(isValidDefaultView(viewName)) {
						String tableName = findTableName(vts, tableNameFromViewName(nsf, viewName));
						DominoViewTable vt = new DominoViewTable(nsf, tableName, viewName);
						vts.put(tableName, vt);
					}
				}
			}
        }
            
		// Now, generate the SQL, sorted by table name for better readability
        ArrayList<DominoVirtualTable> vtList = new ArrayList<DominoVirtualTable>(vts.values());
        (new QuickSort.JavaList(vtList) {
            @Override
            public int compare(Object o1, Object o2) {
                DominoVirtualTable vt1 = (DominoVirtualTable)o1;
                DominoVirtualTable vt2 = (DominoVirtualTable)o2;
                return vt1.getTableName().compareTo(vt2.getTableName());
            }
            
        }).sort();
        for(DominoVirtualTable vt: vtList) {
            String s = generateTable(context, vt);
            if(StringUtil.isNotEmpty(s)) {
                tables.add(s);
            }
        }
        
        // Once the tables are created, add the initialization statement
        List<String> sqlStatements = getSqlStatements();
        if(sqlStatements!=null) {
        	for(String s: sqlStatements) {
                tables.add(s);
        	}
        }
        
        return tables;
    }
    
    protected String generateTable(Context context, DominoVirtualTable vt) throws SQLException {
        StringBuilder b = new StringBuilder();
        if(vt instanceof DominoViewTable) {
            generateViewTable(context,b, (DominoViewTable)vt);
        } else {
            throw DomSQLException.create(null,"Invalid table definition type {0}",vt.getClass());
        }
        return b.toString();
    }

    /**
     * Initializing the SQLite virtual table
     *  CREATE VIRTUAL TABLE tablename USING domsql (1,[dbpath],[vid],[columns],[indexes]);
     *  1: means it comes from a view
     *  [dbpath]: the database path. Empty if the current db  
     *  [vid]: noteID of the view in the database  
     *  [options]: options of the view in the database  
     *  [columns]: list of columns separated by a |
     *      name:type:colIdx
     *  [indexes]: list of indexes separated by a |
     *      idx:[A/D]col1:[A/D]col2...
     *          idx: index of the collation from a NIF standpoint
     *          A/D: A or D, for Ascending or Descending
     *          col1:col2...: list of column indexes, in the table (0..n-1)
     */
    protected void generateViewTable(Context context, StringBuilder b, DominoViewTable vt) throws SQLException {
        ArrayList<Column> cols = new ArrayList<Column>();
        DominoNsf nsf = vt.getDominoNsf();
        
        // Get the view design 
        String dbPath = nsf.getDbPath();
        String viewName = vt.getCollectionName();
        if(StringUtil.isEmpty(viewName)) {
			throw DomSQLException.create(null, "Empty view name");
        }

		try {
	        ViewDesign vd = readView(context,dbPath,viewName);
			if(vd != null) {
				// Add the default columns
				String systemCols = vt.getSystemColumns();
				if(systemCols==null) {
					systemCols = nsf.getSystemColumns();
					if(systemCols==null) {
						systemCols = DEFAULT_SYSTEM_COLUMNS;
					}
				}
				addViewSystemColumns(cols,systemCols);
				
				// Add the options
				addViewOptions(vd, vt.getOptions());
	        
		        List<ViewDesign.Column> viewCols = vd.getColumns();
		        
		        addViewColumns(vt,cols,viewCols);
		        
		        b.append("CREATE VIRTUAL TABLE ");
		        b.append(vt.getTableName());
		        b.append(" USING ");
		        b.append(DOMSQL_MODULE);
		        b.append("(");
		        
		        // Emit a secret code
		        b.append(Integer.toString(SECRET_CODE));
		                
		        // Make a view
		        b.append(",1");
		        
		        // Database path
				b.append(",\"");
				if(dbPath!=null) {
					b.append(dbPath);
				}
			    b.append("\"");
		        
		        // View Name
				b.append(",\"");
		        b.append(viewName);
				b.append("\"");
		        
		        // View note ID
		        b.append(",");
		        int noteID = vd.getNoteID();
		        b.append(Long.toString(noteID&0xFFFFFFFFL));
		        
		        // Options
		        b.append(",");
		        int options = vd.getOptions();
		        b.append(Long.toString(options&0xFFFFFFFFL));
		        
		        // Compute the return mask
		        int retMask = 0;
		        for(Column col: cols) {
		            retMask = retMask | col.getReturnMask();
		        }
		        b.append(",");
		        b.append(Long.toString(retMask&0xFFFFFFFFL));
		        
		        // Columns
		        b.append(",");
		        b.append(Integer.toString(cols.size()));
				Set<String> colNames = new HashSet<String>();
		        for(Column col: cols) {
		            b.append(",\"");
					String colName = findColumnName(colNames, col.getName());
					colNames.add(colName);
					b.append(colName);
		            b.append("\",");
		            b.append(Integer.toString(getColType(vd,viewName,col)));
		            b.append(",");
		            b.append(getColumnIndexByName(vd,cols,viewName,col));
		        }
		        
		        // Indexes
		        int indexCount = 0;
		        StringBuilder idxBuilder = new StringBuilder();
		        ArrayList<Index> idxs = vd.getIndexes();
		        for(Index idx: idxs) {
		            StringBuilder ib = new StringBuilder();
		            int colCount = 0;
		            for(IndexEntry e: idx.getEntries()) {
		                int cidx = getColumnIndex(vt,cols,e.getColName());
		                if(cidx<0) {
		                    break;
		                }
		                colCount++;
		                ib.append(",");
		                ib.append(Integer.toString(cidx));
		                ib.append(",\"");
		                ib.append(e.isDesc()?'D':'A');
		        	    ib.append("\"");
		            }
		            if(colCount>0) {
		                indexCount++;
		                idxBuilder.append(",");
		                idxBuilder.append(idx.getCollation());
		                idxBuilder.append(",");
		                idxBuilder.append(Integer.toString(colCount));
		                idxBuilder.append(ib);
		            }
		        }
		        
		        // We should browse the indexes for this view
		        b.append(",");
		        b.append(Integer.toString(indexCount));
		        if(indexCount>0) {
		            b.append(idxBuilder);
		        }
		        
		        // End of statement
		        b.append(")");
		    }
		} catch(Exception e) {
			Platform.getInstance().log(e);
		}
	}

    private void addViewColumns(DominoViewTable vt, List<Column> tableColumns, List<ViewDesign.Column> viewCols) {
		// Look if the default columns are filtered
    	String[] filteredCols = null;
		String defaultColumns = vt.getDefaultColumns();
		if(defaultColumns!=null) {
	    	filteredCols = StringUtil.splitString(defaultColumns, '|', true);
		}
		
		for(ViewDesign.Column vc : viewCols) {
			String name = vc.getColumnName();
			String colPos = vc.getColumnName();
			// Find if a column is predefined
			Column c = vt.findDominoColumn(name);
			if(c==null) {
				// The column doesn't exist in the list of columns explicitly set
				// Only add it if it is part of the list of columns
				if(filteredCols!=null && !isColumnAvailable(filteredCols, name)) {
					continue;
				}
				// Ok, we create a new col object
				c = new Column(name);
			} else {
				// Ok the column exists, we should just add it
			}
			int colType = c.getColType();
			if(colType==COLTYPE_UNKNOWN) { // Already set by the XML file...
				switch(vc.getColumnType()) {
					case DomSQL.VIEW_COL_TEXT:
						colType = COLTYPE_STRING;
						break;
					case DomSQL.VIEW_COL_NUMBER:
						colType = COLTYPE_NUMBER;
						break;
					case DomSQL.VIEW_COL_TIMEDATE:
						colType = COLTYPE_DATE;
						break;
				}
			}
			c.setViewCol(colPos);
			tableColumns.add(c);
		}
    }
    
    private boolean isColumnAvailable(String[] filteredCols, String colName) {
		for(int i=0; i<filteredCols.length; i++) {
			if(StringUtil.endsWithIgnoreCase(filteredCols[i], colName)) {
				return true;
			}
		}
		return false;
    }

    private void addViewSystemColumns(List<Column> tableColumns, String systemColumns) {
        if(StringUtil.isNotEmpty(systemColumns)) {
        	if(systemColumns.equals(Constants.SYSCOL_ALL)) {
        		systemColumns = Constants.SYSCOL_ALLDEF;
        	}
            String[] cols = StringUtil.splitString(systemColumns, '|', true);
            for(int i=0; i<cols.length; i++) {
                String s = cols[i];
                tableColumns.add(new Column(s));
            }
        }
    }

    private void addViewOptions(ViewDesign vd, String options) {
    	int opt = 0;
        if(StringUtil.isNotEmpty(options)) {
        	if(StringUtil.indexOfIgnoreCase(options,Constants.SOPTIONS_SEPARATEMULTI)>=0) {
        		opt |= Constants.OPTIONS_SEPARATEMULTI;
        	}
//       	if(options.indexOf(Constants.XML_TYPEDCOLUMNS)>=0) {
//    			opt |= Constants.OPTIONS_TYPEDCOLUMNS;
//    		}
        }
        vd.setOptions(opt);
    }
    
    private ViewDesign readView(Context context, String dbPath, String viewName) throws SQLException {
        if(StringUtil.isEmpty(dbPath)) {
            dbPath = nsfPath;
        }
        long hDb = context.getDBHandle(dbPath);
        int noteID = NotesAPI.NIFFindView(hDb,viewName);
		if(noteID != 0) {
            long hNote = NotesAPI.NSFNoteOpen(hDb, noteID, 0);
            try {
                ViewDesign vd = DomSQL.readViewDesign(noteID,hNote);
                if(vd==null) {
                    throw DomSQLException.create(null,"Cannot read view {0} from database {1}",viewName,dbPath);
                }
                return vd;
            } finally {
                NotesAPI.NSFNoteClose(hNote);
            }
		} else {
			JNIUtils.println(StringUtil.format("Unable to find view {0}",viewName));
		}
		return null;
    }

    private int getColType(ViewDesign vd, String viewName, Column col) throws SQLException {
        int colType = col.getColType();
        return colType;
    }

    private int getColumnIndexByName(ViewDesign vd, List<Column> cols, String viewName, Column col) throws SQLException {
        String name = col.getViewCol();
        // Pseudo columns
        if(name.startsWith("@")) {
            if(StringUtil.equalsIgnoreCase(name,SYSCOL_ID)) {
                return COLIDX_ID; 
            }
            if(StringUtil.equalsIgnoreCase(name,SYSCOL_UNID)) {
                return COLIDX_UNID; 
            }
            if(StringUtil.equalsIgnoreCase(name,SYSCOL_CLASS)) {
                return COLIDX_CLASS; 
            }
            if(StringUtil.equalsIgnoreCase(name,SYSCOL_SIBLINGS)) {
                return COLIDX_SIBLINGS; 
            }
            if(StringUtil.equalsIgnoreCase(name,SYSCOL_CHILDREN)) {
                return COLIDX_CHILDREN; 
            }
            if(StringUtil.equalsIgnoreCase(name,SYSCOL_DESCENDANTS)) {
                return COLIDX_DESCENDANTS; 
            }
            if(StringUtil.equalsIgnoreCase(name,SYSCOL_ANYUNREAD)) {
                return COLIDX_ANYUNREAD; 
            }
            if(StringUtil.equalsIgnoreCase(name,SYSCOL_LEVELS)) {
                return COLIDX_LEVELS; 
            }
            if(StringUtil.equalsIgnoreCase(name,SYSCOL_SCORE)) {
                return COLIDX_SCORE; 
            }
            if(StringUtil.equalsIgnoreCase(name,SYSCOL_UNREAD)) {
                return COLIDX_UNREAD; 
            }
            if(StringUtil.equalsIgnoreCase(name,SYSCOL_POSITION)) {
                return COLIDX_POSITION; 
            }
            if(StringUtil.equalsIgnoreCase(name,SYSCOL_ROWID)) {
                return COLIDX_ROWID; 
            }
        }
        // View columns names
        for( ViewDesign.Column c: vd.getColumns() ) {
            if(StringUtil.equalsIgnoreCase(c.getColumnName(),name)) {
                return c.getSummaryIndex();
            }
        }
        // View columns title
        for( ViewDesign.Column c: vd.getColumns() ) {
            if(StringUtil.equalsIgnoreCase(c.getColumnTitle(),name)) {
                return c.getSummaryIndex();
            }
        }
        throw DomSQLException.create(null, "Invalid column name {0} in view {1}", name, viewName); 
    }

    private int getColumnIndex(DominoViewTable vd, List<Column> cols, String name) {
        int count = cols.size();
        for(int i=0; i<count; i++) {
            Column col = cols.get(i);
            if(StringUtil.equalsIgnoreCase(col.getViewCol(), name)) {
                return i;
            }
        }
        return -1;
    }

    protected String getSystemColumns(DominoNsf nsf, DominoViewTable vt) {
        String syscols = vt.getSystemColumns();
        if(syscols==null) {
            syscols = nsf.getSystemColumns();
        }
        return syscols;
    }

    protected boolean isValidDefaultView(String viewName) {
        if(viewName.startsWith("$")) {
            return false;
        }
        return true;
    }

    protected String viewName(String viewName) {
        int pos = viewName.lastIndexOf('|');
        if(pos>=0) {
            return viewName.substring(pos+1);
        }
        return viewName;
    }

    protected String tableNameFromViewName(DominoNsf nsf, String viewName) {
        String prefix = nsf.getTablePrefix();
        if(StringUtil.isEmpty(prefix)) {
			return normalizeName(viewName).toUpperCase();
        }
		return normalizeName(prefix + viewName).toUpperCase();
    }

	protected String findTableName(Map<String, DominoVirtualTable> vts, String tblName) {
		// 1- normalize the name by removing any character not supported by SQLite
		// Also ensure that this name is not empty
		String name = normalizeName(tblName);
		if(StringUtil.isEmpty(name)) {
			name = DEF_COLPREFIX;
		}

		// 2. Shrink the table name
		if(name.length() > MAX_TABLENAMELENGTH) {
			name = name.substring(0, MAX_TABLENAMELENGTH);
		}
    
		// 3. Ensure that the name is unique
		if(vts.containsKey(name)) {
			for(int i = 2;; i++) {
				String newName = name + "_" + i;
				if(!vts.containsKey(newName)) {
					name = newName;
					break;
				}
			}
		}
		return name;
	}

	protected String findColumnName(Set<String> cols, String colName) {
		// 1- normalize the name by removing any character not supported by SQLite
		// Also ensure that this name is not empty
		String name = normalizeName(colName);
		if(StringUtil.isEmpty(name)) {
			name = DEF_COLPREFIX;
		}

		// 2. Shrink the column name
		if(name.length() > MAX_COLUMNNAMELENGTH) {
			name = name.substring(0, MAX_COLUMNNAMELENGTH);
		}

		// 3- ensure that the column name does not already exist
		if(cols.contains(name)) {
			for(int i = 2;; i++) {
				String newName = name + "_" + i;
				if(!cols.contains(newName)) {
					name = newName;
					break;
				}
			}
		}

		return name;
	}

	protected String normalizeName(String s) {
		StringBuilder b = new StringBuilder(s.length() + 16);
		int count = s.length();
		for(int i = 0; i < count; i++) {
			char c = s.charAt(i);
			if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
				// SQL is case insensitive, so we convert to UPPER CASE to avoid duplicates
				b.append(Character.toUpperCase(c));
			} else {
				if((c >= '0' && c <= '9') && b.length() > 0) {
					b.append(c);
				}
				if(c == '$' || c == '@' || c == '_') {
					b.append('_');
				} else {
					// just ignore for now...
				}
			}
		}
		String name = b.toString();
		return name;
	}

	
	
	
    ////////////////////////////////////////////////////////////////////////////////////////
    //
    // Read a schema from a design element
    //
    // <DomSQL [sqliteFile='db file name'] [systemColumns='@xxx[|@yyy]']>
    //    <Nsf [database='database'] [defaultViews='true/false'] [systemColumns='@xxx[|@yyy]']>
    //      <View table='sqlTableName' view='viewName' [systemColumns='@xxx[|@yyy]']> 
    //        <Column name='dominoColName' type='int|double|date|text|boolean' /> 
    //      </View>
    //    </Nsf>
    //    <Init>
    //      <Sql>...statement...</Sql>
    //    </Init>
    // </DomSQL>
    //
    ////////////////////////////////////////////////////////////////////////////////////////
    
    private void readDominoSchema(Context context) throws SQLException {
        // Open the NSF database
        long hDB = context.getDBHandle(null);
        
        String domSqlName = getDomSqlName();
        if(!domSqlName.endsWith(".domsql")) {
            domSqlName = domSqlName + ".domsql"; 
        }
        
        // Ensure that the file is located in WEB-INF/jdbc
        domSqlName = "WEB-INF/jdbc/"+domSqlName; 
        
        String text = null;
        int noteId = DomSQL.findDomsql(hDB, domSqlName);
        if(noteId==0) {
            throw DomSQLException.create(null, "DomSQL database XML file {0} does not exist",domSqlName);
        }
        long hNote = NotesAPI.NSFNoteOpen(hDB, noteId, 0);
        try {
            text = DomSQL.readItemAsString(hNote, "$FileData");
            //System.out.println("TEST:\n"+text);
        } finally {
            NotesAPI.NSFNoteClose(hNote);
        }
        if(StringUtil.isEmpty(text)) {
            throw DomSQLException.create(null, "Error while reading DomSQL database XML file, {0}",domSqlName);
        }
        
        Document doc;
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(new InputSource(new StringReader(text)));
        } catch(Exception e) {
            throw DomSQLException.create(e, "Error while parsing DomSQL database XML file, {0}",domSqlName);
        }
      
        Element root = doc.getDocumentElement();
        checkElement(root,"DomSQL",StringUtil.format("Invalid root element - must be DomSQL, {0}",domSqlName));

        // Read the global option
        readGlobalOptions(context, root);
        
        // Read the children
        NodeList l1 = root.getChildNodes();
        if(l1!=null) {
            for(int i=0; i<l1.getLength(); i++) {
                Node n = l1.item(i);
                if(n.getNodeType()==Node.ELEMENT_NODE) {
                    String name = n.getNodeName();
                    if(name.equals("Nsf")) { 
                        readNsf(context,(Element)n);
                    } else if(name.equals("Init")) {
                        readInit(context,(Element)n);
                    } else {
                        xmlError("Invalid tag {0} as a root child",name);
                    }
                }
            }
        }
    }
    
    private void readGlobalOptions(Context context, Element elt) throws SQLException {
        // Read the attributes
        String v1 = attrString(elt,"sqliteFile",null);
        if(StringUtil.isNotEmpty(v1)) {
        	setSqliteFileName(v1);
        }
    }

    private void readNsf(Context context, Element elt) throws SQLException {
        DominoNsf nsf = new DominoNsf(null,null,false,null,null);
        getNsfs().add(nsf);
        
        // Read the attributes
        nsf.setTablePrefix(attrString(elt,"prefix",null));
        nsf.setDbPath(attrString(elt,"database",null));
        nsf.setDefaultViews(attrBoolean(elt,"defaultViews",false));
        nsf.setSystemColumns(attrString(elt,"systemColumns",null));
        
        // Read the children
        NodeList l1 = elt.getChildNodes();
        if(l1!=null) {
            for(int i=0; i<l1.getLength(); i++) {
                Node n = l1.item(i);
                if(n.getNodeType()==Node.ELEMENT_NODE) {
                    String name = n.getNodeName();
                    if(name.equals("View")) {
                        readView(context,nsf,(Element)n);
                    } else {
                        xmlError("Invalid tag {0} as a <Nsf> child",name);
                    }
                }
            }
        }
    }
    
    private void readView(Context context, DominoNsf nsf, Element elt) throws SQLException {
        String viewName = attrString(elt,"view",null);
        if(StringUtil.isEmpty(viewName)) {
            throw DomSQLException.create(null, "Empty view name in the definition of the database {0}",getDomSqlName());
        }
        DominoViewTable vt = new DominoViewTable(nsf,null,null);
        nsf.getViewTables().add(vt);
        
        // Read the attributes
        vt.setCollectionName(viewName);
        vt.setTableName(attrString(elt,"table",tableNameFromViewName(nsf,viewName)));
        vt.setSystemColumns(attrString(elt,"systemColumns",null));
        vt.setDefaultColumns(attrString(elt,"defaultColumns",null));
        vt.setOptions(attrString(elt,"options",null));
        
        // Read the children
        NodeList l1 = elt.getChildNodes();
        if(l1!=null) {
            for(int i=0; i<l1.getLength(); i++) {
                Node n = l1.item(i);
                if(n.getNodeType()==Node.ELEMENT_NODE) {
                    String name = n.getNodeName();
                    if(name.equals("Column")) {
                        readColumn(context,nsf,vt, (Element)n);
                    } else {
                        xmlError("Invalid tag {0} as a <View> child",name);
                    }
                }
            }
        }
    }
    
    private void readColumn(Context context, DominoNsf nsf, DominoViewTable vt, Element elt) throws SQLException {
        String colName = attrString(elt,"name",null);
        if(StringUtil.isEmpty(colName)) {
            throw DomSQLException.create(null, "Empty column name in the definition of the database {0}, view {1}",getDomSqlName(),vt.getCollectionName());
        }
        DominoViewTable.Column c = new DominoViewTable.Column(colName);

        String sqlName = attrString(elt,"sqlName",null);
        if(StringUtil.isNotEmpty(sqlName)) {
        	c.setName(sqlName);
        }
        
        // Read the attributes
        int colType = COLTYPE_UNKNOWN;
        String type = attrString(elt,"type",null);
        if(StringUtil.isNotEmpty(type)) {
            if(StringUtil.equalsIgnoreCase(type,"int")) {
            	colType = COLTYPE_INTEGER;
            } else if(StringUtil.equalsIgnoreCase(type,"double")) {
            	colType = COLTYPE_NUMBER;
            } else if(StringUtil.equalsIgnoreCase(type,"boolean")) {
            	colType = COLTYPE_BOOLEAN;
            } else if(StringUtil.equalsIgnoreCase(type,"date")) {
            	colType = COLTYPE_DATE;
            } else if(StringUtil.equalsIgnoreCase(type,"string")) {
            	colType = COLTYPE_STRING;
            }
        }
    	c.setColType(colType);

    	vt.getColumns().add(c);
    }

    private void readInit(Context context, Element elt) throws SQLException {
    	List<String> sqlStatements = null;
    	
        // Read the initial SQL statements to execute.
        NodeList l1 = elt.getChildNodes();
        if(l1!=null) {
            for(int i=0; i<l1.getLength(); i++) {
                Node n = l1.item(i);
                if(n.getNodeType()==Node.ELEMENT_NODE) {
                    String name = n.getNodeName();
                    if(name.equals("Sql")) {
                    	String sql = getTextValue(n);
                    	if(sql!=null) {
                    		sql.trim();
                    		if(StringUtil.isNotEmpty(sql)) {
                    			if(sqlStatements==null) {
                    				sqlStatements = new ArrayList<String>();
                    			}
                    			sqlStatements.add(sql);
                    		}
                    	}
                    } else {
                        xmlError("Invalid tag {0} as a <Init> child",name);
                    }
                }
            }
        }
        
        if(sqlStatements!=null) {
        	setSqlStatements(sqlStatements);
        }
    }
    
    private String attrString(Element elt, String attrName, String defaultValue) throws SQLException {
        if(elt.hasAttribute(attrName)) {
            String s = elt.getAttribute(attrName);
            return s;
        }
        return defaultValue;
    }

    private boolean attrBoolean(Element elt, String attrName, boolean defaultValue) throws SQLException {
        if(elt.hasAttribute(attrName)) {
            String s = elt.getAttribute(attrName);
            if(s.equals("true")) {
                return true;
            }
            if(s.equals("false")) {
                return false;
            }
            xmlError("Invalid boolean value {0} for attribute {1}", attrName);
        }
        return defaultValue;
    }
    
    private void checkElement(Element element, String name, String msg) throws SQLException { 
        if(name==null || !element.getNodeName().equals(name)) {
            xmlError(msg);
        }
    }
    
    private String getTextValue(Node node) {
        if(node!=null) {
            if(node.getNodeType()==Node.ELEMENT_NODE) {
                if(node.hasChildNodes()) {
                    NodeList l = node.getChildNodes();
                    int len = l.getLength(); 
                    if(len==1) {
                        Node child = l.item(0);
                        if( child.getNodeType()==Node.TEXT_NODE || child.getNodeType()==Node.CDATA_SECTION_NODE ) {
                            return child.getNodeValue();
                        }
                        return null;
                    } else {
                        StringBuilder b = new StringBuilder(128);
                        for( int i=0; i<len; i++ ) {
                            Node child = l.item(i);
                            if( child.getNodeType()==Node.TEXT_NODE || child.getNodeType()==Node.CDATA_SECTION_NODE ) {
                                String s = child.getNodeValue();
                                if(s!=null) {
                                    b.append(s);
                                }
                            }
                        }
                        return b.toString();
                    }
                }
            } 
            else if (node.getNodeType()==Node.TEXT_NODE || 
                    node.getNodeType()==Node.CDATA_SECTION_NODE ||
                    node.getNodeType()==Node.ATTRIBUTE_NODE) {
                return node.getNodeValue();
            }
        }
        return null;
    }

    private static void xmlError(String msg, Object ... p) throws SQLException {
        throw DomSQLException.create(null,msg,p);
    }
}
