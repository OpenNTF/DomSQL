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
package com.ibm.domino.domsql.sqlite.driver.jni;

import java.sql.SQLException;


/**
 * JNI class used to access some of the NotesAPI.
 * 
 * @author priand
 */
public class NotesAPI {
	
	public static final int READ_MASK_NOTEID			= 0x00000001;
	public static final int READ_MASK_NOTEUNID			= 0x00000002;
	public static final int READ_MASK_NOTECLASS			= 0x00000004;
	public static final int READ_MASK_INDEXSIBLINGS		= 0x00000008;
	public static final int READ_MASK_INDEXCHILDREN		= 0x00000010;
	public static final int READ_MASK_INDEXDESCENDANTS	= 0x00000020;
	public static final int READ_MASK_INDEXANYUNREAD	= 0x00000040;
	public static final int READ_MASK_INDENTLEVELS		= 0x00000080;
	public static final int READ_MASK_SCORE				= 0x00000200;
	public static final int READ_MASK_INDEXUNREAD		= 0x00000400;
	public static final int READ_MASK_COLLECTIONSTATS	= 0x00000100;
	public static final int READ_MASK_INDEXPOSITION		= 0x00004000;
	public static final int READ_MASK_SUMMARYVALUES		= 0x00002000;
	public static final int READ_MASK_SUMMARY			= 0x00008000;
	

    ///////////////////////////////////////////////////////////
    // Notes initialization routines
    // For standalone apps
    ///////////////////////////////////////////////////////////    
    public static native void NotesInit() throws SQLException;
    public static native void NotesTerm() throws SQLException;
    public static native void NotesInitThread() throws SQLException;
    public static native void NotesTermThread() throws SQLException;

    ///////////////////////////////////////////////////////////
    // Access to notes.ini
    ///////////////////////////////////////////////////////////
	public static final native int OSGetEnvironmentInt(String name) throws SQLException;
	public static final native long OSGetEnvironmentLong(String name) throws SQLException;
	public static final native String OSGetEnvironmentString(String name) throws SQLException;
	public static final native void OSSetEnvironmentInt(String name, int value) throws SQLException;
	public static final native void OSSetEnvironmentVariable(String name, String value) throws SQLException;
    
    
    ///////////////////////////////////////////////////////////
    // Database routines
    ///////////////////////////////////////////////////////////
    public static native long NSFDBOpen(String dbPath) throws SQLException;
    public static native long NSFDBOpenEx(String dbPath, long hNames) throws SQLException;
    public static native void NSFDBClose(long hDb) throws SQLException;
    public static native long NSFDbNonDataModifiedTime(long hDb) throws SQLException;

    ///////////////////////////////////////////////////////////
    // Note routines
    ///////////////////////////////////////////////////////////
    public static native long NSFNoteOpen(long hDb, int noteID, int flags) throws SQLException;
    public static native void NSFNoteClose(long hNote) throws SQLException;
    
    ///////////////////////////////////////////////////////////
    // View routines
    ///////////////////////////////////////////////////////////
    public static native int NIFFindView(long hDb, String viewName) throws SQLException;
    
    ///////////////////////////////////////////////////////////
    // KFM
    ///////////////////////////////////////////////////////////
    public static native String SECKFMGetUserName() throws SQLException;
}
