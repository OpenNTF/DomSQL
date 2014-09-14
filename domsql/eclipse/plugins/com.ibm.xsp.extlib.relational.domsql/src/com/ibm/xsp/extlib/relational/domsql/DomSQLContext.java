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
package com.ibm.xsp.extlib.relational.domsql;

import javax.faces.context.FacesContext;

import com.ibm.domino.domsql.sqlite.DomSQLLocalDriver;
import com.ibm.domino.domsql.sqlite.driver.Context;
import com.ibm.domino.domsql.sqlite.driver.DefaultContext;
import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.event.FacesContextListener;



/**
 * Encapsulate a runtime context for accessing the data 
 * 
 * @author priand
 */
public class DomSQLContext extends DefaultContext {
	
	public static void init() {
		DomSQLLocalDriver.initLocal();
		setContextFinder(new DomSQLFinder(getContextFinder()));
	}
	
    public static class DomSQLFinder extends ContextFinder {
    	DomSQLFinder(ContextFinder next) {
    		super(next);
    	}
        public Context find() {
        	NotesContext nc = NotesContext.getCurrentUnchecked();
        	if(nc!=null) {
	        	FacesContextEx facesContext = FacesContextEx.getCurrentInstance();
	        	if(facesContext!=null) {
	            	// Ensure that the context is removed when the thread is gone
	            	facesContext.addRequestListener(new FacesContextListener() {
	        			public void beforeRenderingPhase(FacesContext arg0) {
	        			}
	        			public void beforeContextReleased(FacesContext arg0) {
	        				Context.pop();
	        			}
	        		});
	            	DomSQLContext ctx = new DomSQLContext(nc);
	            	Context.push(ctx);
	            	return ctx;
	        	}
        	}
        	return null;
        }
    }
	

    public DomSQLContext(NotesContext nc) {
    	setHandle(nc.getUserDatabaseHandle());
    }    
}
