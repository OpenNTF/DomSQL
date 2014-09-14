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

import java.util.ArrayList;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.domsql.sqlite.driver.jni.JNIUtils;

/**
 * Holds the definition of a VIEW design element.
 * 
 * @author priand
 */
public class ViewDesign {
    
    public static class Column {
        private String  columnName;
        private String  columnTitle;
        private int     columnType;
        private int     summaryIndex;
        public Column(String columnName, String columnTitle, int columnType, int summaryIndex) {
            this.columnName = columnName;
            this.columnTitle = columnTitle;
            this.columnType = columnType;
            this.summaryIndex = summaryIndex;
        }
        public String getColumnName() {
            return columnName;
        }
        public String getColumnTitle() {
            return columnTitle;
        }
        public int getColumnType() {
            return columnType;
        }
        public int getSummaryIndex() {
            return summaryIndex;
        }
    }
    
    public static class Index {
        private int                     collation;
        private ArrayList<IndexEntry>   entries = new ArrayList<IndexEntry>();
        public Index(int collation) {
            this.collation = collation;
        }
        public int getCollation() {
            return collation;
        }
        public ArrayList<IndexEntry> getEntries() {
            return entries;
        }
    }
    
    public static class IndexEntry {
        private String      colName;
        private boolean     desc;
        public IndexEntry(String colName, boolean desc) {
            this.colName = colName;
            this.desc = desc;
        }
        public String getColName() {
            return colName;
        }
        public boolean isDesc() {
            return desc;
        }
    }
    
    private int                 noteID;
    private int                 options;
    private String              title;
    private ArrayList<Column>   columns = new ArrayList<Column>();
    private ArrayList<Index>    indexes = new ArrayList<Index>();
    
    public ViewDesign(int noteID, String title) {
        this.noteID = noteID;
        this.title = title;
    }

    public int getNoteID() {
        return noteID;
    }

    public int getOptions() {
        return options;
    }

    public void setOptions(int options) {
        this.options = options;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Column> getColumns() {
        return columns;
    }

    public ArrayList<Index> getIndexes() {
        return indexes;
    }
    
    public void dump() {
        println("noteID={0}",getNoteID());
        
        println("columns[{0}]=[",getColumns().size());
        for(int i=0; i<getColumns().size(); i++) {
            Column c=getColumns().get(i);
            println("  #{0}={",i);
            println("    name={0}",c.getColumnName());
            println("    title={0}",c.getColumnTitle());
            println("  }");
        }
        println("]");

        println("indexes[{0}]=[",getIndexes().size());
        for(int i=0; i<getIndexes().size(); i++) {
            Index c=getIndexes().get(i);
            println("  #{0}={",i);
            println("    collation={0}",c.getCollation());

            println("    entries[{0}]=[",c.getEntries().size());
            for(int j=0; j<c.getEntries().size(); j++) {
                IndexEntry e=c.getEntries().get(j);
                println("      #{0}={",j);
                println("        colName={0}",e.getColName());
                println("        desc={0}",e.isDesc());
                println("      }");
                println("    ]");
            }
            println("  }");
        }
        println("]");
    }
    
    private void println(String s, Object...p) {
        String m = StringUtil.format(s, p);
        JNIUtils.println(m);
    }
}
