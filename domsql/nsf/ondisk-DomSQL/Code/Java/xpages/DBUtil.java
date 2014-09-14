package xpages;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.extlib.relational.util.JdbcUtil;

public class DBUtil {
	
	public static final String BEAN_NAME = "dbUtil";
	public static final String DEFAULT_CONNECTION = "all";

	// Look for the managed bean
	public static DBUtil get() {
		FacesContext context = FacesContext.getCurrentInstance();
		DBUtil bean = (DBUtil)context.getApplication().getVariableResolver().resolveVariable(context, BEAN_NAME);
		return bean;
	}
	
	public DBUtil() {
	}
	
	public String getConnectionName() {
		String s = (String)ExtLibUtil.getSessionScope().get("jdbcConnection");
		if(StringUtil.isEmpty(s)) {
			s = DEFAULT_CONNECTION;
		}
		return s;
	}
	
	public Connection getConnection() throws SQLException {
		return JdbcUtil.getConnection(FacesContext.getCurrentInstance(),getConnectionName());
	}
	
	public List<String> listTables() {
		try {
			return JdbcUtil.listTables(getConnection(), "", null, new String[]{"TABLE","VIEW"});
		} catch(Exception ex) {
			Platform.getInstance().log(ex);
			return Collections.<String>emptyList();
		}
	}
}
