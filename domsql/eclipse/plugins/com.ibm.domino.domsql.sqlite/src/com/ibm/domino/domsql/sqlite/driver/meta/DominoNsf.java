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
import java.util.List;

/**
 * SQLLite NSF mapping.
 * 
 * @author priand
 */
public class DominoNsf {

    private String tablePrefix;
    private String dbPath;
    private boolean defaultViews;
    private String systemColumns;
    private List<DominoViewTable> viewTables = new ArrayList<DominoViewTable>();

    public DominoNsf(String tablePrefix, String dbPath, boolean defaultViews, String systemColumns, DominoViewTable[] vts) {
        this.tablePrefix = tablePrefix;
        this.dbPath = dbPath;
        this.defaultViews = defaultViews;
        this.systemColumns = systemColumns;
        if(vts!=null) {
            for(int i=0; i<vts.length; i++) {
                vts[i].setDominoNsf(this);
                viewTables.add(vts[i]);
            }
        }
    }

    public String getTablePrefix() {
        return tablePrefix;
    }
    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public String getDbPath() {
        return dbPath;
    }
    public void setDbPath(String dbPath) {
        this.dbPath = dbPath;
    }

    public boolean isDefaultViews() {
        return defaultViews;
    }
    public void setDefaultViews(boolean defaultViews) {
        this.defaultViews = defaultViews;
    }

    public String getSystemColumns() {
        return systemColumns;
    }
    public void setSystemColumns(String systemColumns) {
        this.systemColumns = systemColumns;
    }

    public List<DominoViewTable> getViewTables() {
        return viewTables;
    }
    public void setViewTables(List<DominoViewTable> virtualTables) {
        this.viewTables = virtualTables;
    }

}
