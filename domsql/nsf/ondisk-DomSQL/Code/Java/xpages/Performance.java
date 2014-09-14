package xpages;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;
import lotus.domino.ViewNavigator;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class Performance {
    
	public static class TestParams {
        public String viewName;
        public int loop = 100;
        public int skip = 0;
        public int maxEntries = 30;
        public TestParams(String viewName) {
            this.viewName = viewName;
        }
    }

    public static class TestResult {
    	public TestParams params;
        public long backEndCollection;
        public long backEndNavigator;
        public long sql;
        public TestResult(TestParams params) {
        	this.params = params;
        }
        public void display() {
        	System.out.println(toString());
        }
        public String toString() {
        	StringBuilder b = new StringBuilder();
            b.append("ViewName="+params.viewName+"\n");
            b.append("loop="+params.loop+"\n");
            b.append("\n");
            b.append("Backend time (ViewEntryCollection)="+backEndCollection+"ms\n");
            b.append("Backend time (ViewNavigator)="+backEndNavigator+"ms\n");
            b.append("\n");
            b.append("SQL time="+sql+"ms\n");
            b.append("\n");
            DecimalFormat f = new DecimalFormat("0.##");
            b.append("Ratio (ViewEntryCollection)=~x"+f.format((((double)backEndCollection)/((double)sql)))+"\n");
            b.append("Ratio (ViewNavigator)=~x"+f.format((((double)backEndNavigator)/((double)sql)))+"\n");
            return b.toString();
        }
    }
    
    public static TestResult runTest(String viewName, int loop) throws Exception {
    	TestParams params = new TestParams(viewName);
    	if(loop>0) {
    		params.loop = loop;
    	}
   		Performance p = new Performance();
   		return p.runTest(params);
    }
    
	public TestResult runTest(TestParams params) throws Exception {
	    TestResult result = new TestResult(params);
	    
		// Using ViewEntryCollection
		runBackend1(0,params,result);
		long startBackend0 = System.currentTimeMillis();
		runBackendAll(0,params,result);
		long endBackend0 = System.currentTimeMillis();
		result.backEndCollection = (endBackend0-startBackend0);

		// Using ViewNavigator
		runBackend1(1,params,result);
		long startBackend1 = System.currentTimeMillis();
		runBackendAll(1,params,result);
		long endBackend1 = System.currentTimeMillis();
		result.backEndNavigator = (endBackend1-startBackend1);

        // SQL run
        runSQL1(params,result);
        long startSQL = System.currentTimeMillis();
        runSQLAll(params,result);
        long endSQL = System.currentTimeMillis();
        result.sql = (endSQL-startSQL);
		return result;
	}

	public void runBackend1(int mode, TestParams params, TestResult result) throws Exception{
		runBackend(1,mode,params,result);
	}
	public void runBackendAll(int mode, TestParams params, TestResult result) throws Exception{
		runBackend(params.loop,mode,params,result);
	}
	public void runBackend(int count, int mode, TestParams params, TestResult result) throws Exception{
		Database db = ExtLibUtil.getCurrentDatabase();
		View v = db.getView(params.viewName);
		try {
			v.setAutoUpdate(false);
			for(int i=0; i<count; i++) {
				System.out.println("Backend, index#"+i);
				readBackendView(v,mode,params,result);
			}
		} finally {
			v.recycle();
		}
	}
	public void readBackendView(View view, int mode, TestParams params, TestResult result) throws Exception {
		if(mode==0) {
		    int n = params.maxEntries;
			ViewEntryCollection col = view.getAllEntries();
			try {
				//System.out.println("#:"+col.getCount());
				for(ViewEntry e = col.getFirstEntry(); e!=null; e=col.getNextEntry()) {
					String id = e.getNoteID();
					String unid = e.getUniversalID();
					Vector<?> val = e.getColumnValues();
					//System.out.println(id);
					
					// Doing this breaks the loop!
					//e.recycle();
				}
			} finally {
				col.recycle();
			}
		} else if(mode==1) {
			ViewNavigator col = view.createViewNav();
			try {
	            //col.setBufferMaxEntries(100);
				col.setCacheGuidance(100);
				//System.out.println("#:"+col.getCount());
				for(ViewEntry e = col.getFirst(); e!=null; e=col.getNext()) {
					String id = e.getNoteID();
					String unid = e.getUniversalID();
					Vector<?> val = e.getColumnValues();
					//System.out.println(id);
					
					// Doing this breaks the loop!
					//e.recycle();
				}
			} finally {
				col.recycle();
			}
		}
	}

	public void runSQL1(TestParams params, TestResult result) throws Exception{
		runSQL(1,params,result);
	}
	public void runSQLAll(TestParams params, TestResult result) throws Exception{
		runSQL(params.loop,params,result);
	}
	public void runSQL(int count, TestParams params, TestResult result) throws Exception{
        Connection conn = DBUtil.get().getConnection();
        try {
            Statement stmt = conn.createStatement();
            try {
            	int nCols = -1;
                String sql = "SELECT * from "+params.viewName;
                for(int cc=0; cc<count; cc++) {
    				System.out.println("SQL, index#"+cc);
                    //ResultSet rs = stmt.executeQuery(sql);
                    stmt.execute(sql);
                    ResultSet rs = stmt.getResultSet();
                    try {
                        int max = Integer.MAX_VALUE;
                        if(nCols<0) {
                        	nCols = rs.getMetaData().getColumnCount();
                        }
                        for( int n=0; n<max && rs.next(); n++ ) {
                            ArrayList<Object> v = new ArrayList<Object>(nCols);
                            for(int i=1; i<=nCols; i++) {
                                Object o = rs.getObject(i);
                                v.add(o);
                            }
                        }
                    } finally {
                        rs.close();
                    }
                }
            } finally {
                stmt.close();
            }
        } finally {
            conn.close();
        }
	}
}
