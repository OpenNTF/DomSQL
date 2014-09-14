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
package com.ibm.domino.domsql.sqlite.driver;

import com.ibm.domino.domsql.DomSQLDriver;



/**
 * Encapsulate a runtime context for accessing the data 
 * 
 * @author priand
 */
public class Constants {

    // =========================================================
    // Notes.ini entries
    // =========================================================
	
	public static final String NOTESINI_DOMSQLPORT		= "DomSQL_Port";
	public static final String NOTESINI_DOMSQLTIMEOUT	= "DomSQL_Timeout";
	
	public static final int DEFAULT_PORT				= DomSQLDriver.DEFAULT_PORT;
	public static final int DEFAULT_TIMEOUT				= 10 * 60 * 1000; // 10 minutes
	//public static final int DEFAULT_TIMEOUT				= 20 * 1000; // 20 secs, for test
	
	public static final int TIMEOUT_JOB_DELAY			= 1 * 60 * 1000; // 1 minute
	//public static final int TIMEOUT_JOB_DELAY			= 1000; // 1 sec, for test
	
	
    // =========================================================
    // Options
    // =========================================================

	public static final String SOPTIONS_SEPARATEMULTI	= "SeparateMulti";

	// Flag as a parameter
	public static final int OPTIONS_SEPARATEMULTI	= 1;
	//public static final int OPTIONS_TYPEDCOLUMNS	= 2;

	
    // =========================================================
    // Basic SQLite types
    // =========================================================

	// Columns types
    public static final int COLTYPE_UNKNOWN  		= 0;
    public static final int COLTYPE_STRING  		= 1;
    public static final int COLTYPE_INTEGER			= 2;
    public static final int COLTYPE_NUMBER			= 3;
    public static final int COLTYPE_BLOB			= 4;
    
    // Extended types
    public static final int COLTYPE_BOOLEAN			= 5;
    public static final int COLTYPE_DATE			= 6;
    
    
    // =========================================================
    // System column names
    // =========================================================
    
    public static final String SYSCOL_ID            = "@id"; 
    public static final String SYSCOL_UNID          = "@unid"; 
    public static final String SYSCOL_CLASS         = "@class"; 
    public static final String SYSCOL_SIBLINGS      = "@siblings"; 
    public static final String SYSCOL_CHILDREN      = "@children"; 
    public static final String SYSCOL_DESCENDANTS   = "@descendants"; 
    public static final String SYSCOL_ANYUNREAD     = "@anyunread"; 
    public static final String SYSCOL_LEVELS        = "@levels"; 
    public static final String SYSCOL_SCORE         = "@score"; 
    public static final String SYSCOL_UNREAD        = "@unread"; 
    public static final String SYSCOL_POSITION      = "@position";        
    public static final String SYSCOL_ROWID      	= "@rowid";        

    // Pseudo columns
    public static final String SYSCOL_ALL      		= "@all";   
    public static final String SYSCOL_ALLDEF   		= "@id|@unid|@class|@siblings|@children|@descendants|@anyunread|@levels|@score|@unread|@position|@rowid";        
    
}
