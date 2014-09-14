package birt;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;

import com.ibm.commons.util.StringUtil;


public class DataMigration {
	
	public DataMigration() {
	}
	
	// ======================================================
	// Data migration
	// ======================================================

	protected class Context {
		private Database db;
		private Connection c;
		private List<DbTable> tables;
		protected Context(Database db, Connection c, List<DbTable> tables) {
			this.db = db;
			this.c = c;
			this.tables = tables;
		}
		public Database getDatabase() {
			return db;
		}
		public Connection getConnection() {
			return c;
		}
		public List<DbTable> getDbTables() {
			return tables;
		}
	}

	public void migrate(Database db, Connection c) throws SQLException, NotesException {
		Context ctx = createContext(db, c);
		moveData(ctx);
	}
	
	protected Context createContext(Database db, Connection c) throws SQLException, NotesException {
		List<DbTable> tables = readTables(c);
		return new Context(db, c, tables);
	}
	
	protected void moveData(Context context) throws SQLException, NotesException {
		List<DbTable> tables = context.getDbTables();
		for(DbTable tb: tables) {
			moveData(context, tb);
		}
	}
	protected void moveData(Context context, DbTable table) throws SQLException, NotesException {
		Database db = context.getDatabase();
		Statement st = context.getConnection().createStatement();
		try {
			ResultSet rs = st.executeQuery("SELECT * FROM "+getTableName(table));
			try {
				ResultSetMetaData meta = rs.getMetaData();
				while(rs.next()) {
					Document doc = db.createDocument();
					try {
						doc.replaceItemValue("Form", table.name);
						for(int i=1; i<=meta.getColumnCount(); i++) {
							String name = meta.getColumnName(i);
							Object o = getDominoValue(db,rs.getObject(i));
							if(o!=null) {
								doc.replaceItemValue(name, o);
							}
						}
						doc.save();
					} finally {
						doc.recycle();
					}
				}
			} finally {
				rs.close();
			}
		} finally {
			st.close();
		}
	}
	private String getTableName(DbTable tb) throws SQLException, NotesException {
		StringBuilder b = new StringBuilder();
		if(StringUtil.isNotEmpty(tb.schema)) {
			b.append(tb.schema);
			b.append('.');
		}
		b.append(tb.name);
		return b.toString();
	}
	private Object getDominoValue(Database db, Object o) throws SQLException, NotesException {
		if(o==null) {
			return null;
		}
		if(o instanceof Number) {
			return ((Number)o).doubleValue();
		}
		if(o instanceof Date) {
			return db.getParent().createDateTime((Date)o);
		}
		return o.toString();
	}

	
	
	
	
	//
	// Read a database from a JDBC connection
	//
	public class DbTable {
		public String schema;
		public String name;
		public String type;
	}
	protected List<DbTable> readTables(Connection c) throws SQLException {
		List<DbTable> tables = new ArrayList<DbTable>();
		final String SCHEMA_NAME = "CLASSICMODELS"; 

		DatabaseMetaData m = c.getMetaData();

		// Get the list of tables
		ResultSet rs = m.getTables(null,SCHEMA_NAME,null,null);
		try {
			while(rs.next()) {
				String tbName = rs.getString(3);
				String tbType = rs.getString(4);
				DbTable tb = new DbTable();
				tb.schema = SCHEMA_NAME;
				tb.name = tbName;
				tb.type = tbType;
				tables.add(tb);
			}
		} finally {
			rs.close();
		}
		
		return tables;
	}
}
