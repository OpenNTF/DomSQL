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

import static com.ibm.domino.domsql.sqlite.driver.Constants.COLTYPE_INTEGER;
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

import java.util.ArrayList;
import java.util.List;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.domsql.sqlite.driver.jni.NotesAPI;
/**
 * SQLLite table mapping to a view.
 * 
 * @author priand
 */
public class DominoViewTable extends DominoVirtualTable {

    public static class Column {
        private String name;
        private String viewCol;
        private int colType;

        public Column(String name) {
            this.name = name;
            this.viewCol = name;
            if(name.startsWith("@")) {
	            if(name.equalsIgnoreCase(SYSCOL_ID)) {
	                this.colType = COLTYPE_INTEGER;
	            } else if(name.equalsIgnoreCase(SYSCOL_UNID)) {
	                this.colType = COLTYPE_STRING;
	            } else if(name.equalsIgnoreCase(SYSCOL_CLASS)) {
	                this.colType = COLTYPE_INTEGER;
	            } else if(name.equalsIgnoreCase(SYSCOL_SIBLINGS)) {
	                this.colType = COLTYPE_INTEGER;
	            } else if(name.equalsIgnoreCase(SYSCOL_CHILDREN)) {
	                this.colType = COLTYPE_INTEGER;
	            } else if(name.equalsIgnoreCase(SYSCOL_DESCENDANTS)) {
	                this.colType = COLTYPE_INTEGER;
	            } else if(name.equalsIgnoreCase(SYSCOL_ANYUNREAD)) {
	                this.colType = COLTYPE_INTEGER;
	            } else if(name.equalsIgnoreCase(SYSCOL_LEVELS)) {
	                this.colType = COLTYPE_INTEGER;
	            } else if(name.equalsIgnoreCase(SYSCOL_SCORE)) {
	                this.colType = COLTYPE_INTEGER;
	            } else if(name.equalsIgnoreCase(SYSCOL_UNREAD)) {
	                this.colType = COLTYPE_INTEGER;
	            } else if(name.equalsIgnoreCase(SYSCOL_POSITION)) {
	                this.colType = COLTYPE_STRING;
	            } else if(name.equalsIgnoreCase(SYSCOL_ROWID)) {
	                this.colType = COLTYPE_INTEGER;
	            } else {
	                throw new IllegalArgumentException(StringUtil.format("Invalid system column {0}",name));
	            }
            }
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getViewCol() {
            return viewCol;
        }
        public void setViewCol(String viewCol) {
            this.viewCol = viewCol;
        }
        public int getColType() {
            return colType;
        }
        public void setColType(int colType) {
            this.colType = colType;
        }
        
        public String validateColumn() {
            // Force the type of the system column
            if(name.equalsIgnoreCase(SYSCOL_ID)) {
                this.colType = COLTYPE_INTEGER;
            } else if(name.equalsIgnoreCase(SYSCOL_UNID)) {
                this.colType = COLTYPE_STRING;
            } else if(name.equalsIgnoreCase(SYSCOL_CLASS)) {
                this.colType = COLTYPE_INTEGER;
            } else if(name.equalsIgnoreCase(SYSCOL_SIBLINGS)) {
                this.colType = COLTYPE_INTEGER;
            } else if(name.equalsIgnoreCase(SYSCOL_CHILDREN)) {
                this.colType = COLTYPE_INTEGER;
            } else if(name.equalsIgnoreCase(SYSCOL_DESCENDANTS)) {
                this.colType = COLTYPE_INTEGER;
            } else if(name.equalsIgnoreCase(SYSCOL_ANYUNREAD)) {
                this.colType = COLTYPE_INTEGER;
            } else if(name.equalsIgnoreCase(SYSCOL_LEVELS)) {
                this.colType = COLTYPE_INTEGER;
            } else if(name.equalsIgnoreCase(SYSCOL_SCORE)) {
                this.colType = COLTYPE_INTEGER;
            } else if(name.equalsIgnoreCase(SYSCOL_UNREAD)) {
                this.colType = COLTYPE_INTEGER;
            } else if(name.equalsIgnoreCase(SYSCOL_POSITION)) {
                this.colType = COLTYPE_STRING;
            } else if(name.equalsIgnoreCase(SYSCOL_ROWID)) {
                this.colType = COLTYPE_INTEGER;
            }
            return null;
        }
        public int getReturnMask() {
        	if(viewCol.equals(SYSCOL_ID)) {
        		return NotesAPI.READ_MASK_NOTEID;
        	}
        	if(viewCol.equals(SYSCOL_UNID)) {
        		return NotesAPI.READ_MASK_NOTEUNID;
        	}
            if(viewCol.equals(SYSCOL_CLASS)) {
                return NotesAPI.READ_MASK_NOTECLASS;
            }
            if(viewCol.equals(SYSCOL_SIBLINGS)) {
                return NotesAPI.READ_MASK_INDEXSIBLINGS;
            }
            if(viewCol.equals(SYSCOL_CHILDREN)) {
                return NotesAPI.READ_MASK_INDEXCHILDREN;
            }
            if(viewCol.equals(SYSCOL_DESCENDANTS)) {
                return NotesAPI.READ_MASK_INDEXDESCENDANTS;
            }
            if(viewCol.equals(SYSCOL_ANYUNREAD)) {
                return NotesAPI.READ_MASK_INDEXANYUNREAD;
            }
            if(viewCol.equals(SYSCOL_LEVELS)) {
                return NotesAPI.READ_MASK_INDENTLEVELS;
            }
            if(viewCol.equals(SYSCOL_SCORE)) {
                return NotesAPI.READ_MASK_SCORE;
            }
            if(viewCol.equals(SYSCOL_UNREAD)) {
                return NotesAPI.READ_MASK_INDEXUNREAD;
            }
            if(viewCol.equals(SYSCOL_POSITION)) {
                return NotesAPI.READ_MASK_INDEXPOSITION;
            }
            if(viewCol.equals(SYSCOL_ROWID)) {
            	// This is a pseudo column so it doesn't have any particular mask
                return 0;
            }
    		return NotesAPI.READ_MASK_SUMMARYVALUES;
        }
    }

    private DominoNsf nsf;
    private String collectionName;
    private String defaultColumns;
    private String systemColumns;
    private String options;
    private List<Column> columns = new ArrayList<DominoViewTable.Column>();

    public DominoViewTable(DominoNsf nsf, String tableName, String collectionName) {
        super(tableName);
        this.nsf = nsf;
        this.collectionName = collectionName;
    }

    public DominoNsf getDominoNsf() {
        return nsf;
    }
    public void setDominoNsf(DominoNsf nsf) {
        this.nsf = nsf;
    }

    public String getCollectionName() {
        return collectionName;
    }
    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
    
    public String getSystemColumns() {
        return systemColumns;
    }
    public void setSystemColumns(String systemColumns) {
        this.systemColumns = systemColumns;
    }
    
    public String getOptions() {
        return options;
    }
    public void setOptions(String options) {
        this.options = options;
    }
    
    public String getDefaultColumns() {
    	return defaultColumns;
    }
    public void setDefaultColumns(String defaultColumns) {
    	this.defaultColumns = defaultColumns;
    }
    
    public List<Column> getColumns() {
    	return columns;
    }
    
    public Column findDominoColumn(String name) {
    	List<Column> cols = getColumns();
    	int sz = cols.size(); 
    	for(int i=0; i<sz; i++) {
    		if(StringUtil.equalsIgnoreCase(cols.get(i).getViewCol(),name)) {
    			return cols.get(i);
    		}
    	}
    	return null;
    }
}
