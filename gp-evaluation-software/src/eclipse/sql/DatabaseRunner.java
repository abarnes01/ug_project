package eclipse.sql;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.ibatis.jdbc.ScriptRunner;

public class DatabaseRunner {
	
	private String mySQLurl, dburl, dbname, dbpass;
	
	public DatabaseRunner(String mySQLurl, String dbname, String dbpass) {
		this.mySQLurl = mySQLurl;
		this.dburl = mySQLurl+"gp_database";
		this.dbname = dbname;
		this.dbpass = dbpass;
	}
		
	public final String getmySQLurl() {
		return mySQLurl;
	}

	public final void setmySQLurl(String mySQLurl) {
		this.mySQLurl = mySQLurl;
	}

	public final String getDbname() {
		return dbname;
	}

	public final void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public final String getDbpass() {
		return dbpass;
	}

	public final void setDbpass(String dbpass) {
		this.dbpass = dbpass;
	}
	
	public final String getDburl() {
		return dburl;
	}

	public final void setDburl(String dburl) {
		this.dburl = dburl;
	}

	public void createDB() throws Exception {
		
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		
		Connection con = DriverManager.getConnection(mySQLurl, dbname, dbpass);
		System.out.println("Database connection established...");
		
		ScriptRunner sr = new ScriptRunner(con);
		
		InputStream inputStream = DatabaseRunner.class.getResourceAsStream("/Scripts/gpscript.sql");
		
		Reader rd = new InputStreamReader(inputStream);	
		sr.runScript(rd);
		rd.close();
		sr.closeConnection();
		con.close();
		
	}
	
	public void dropDB() throws Exception {
	
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		
		Connection con = DriverManager.getConnection(mySQLurl, dbname, dbpass);
		System.out.println("Database connection established...");
		
		ScriptRunner sr = new ScriptRunner(con);
		
		InputStream inputStream = DatabaseRunner.class.getResourceAsStream("/Scripts/dropdb.sql");
		
		Reader rd = new InputStreamReader(inputStream);
		
		sr.runScript(rd);
		rd.close();
		sr.closeConnection();
		con.close();
		
	}
	
	public Boolean isConnected() {
		try {
			Connection connection = DriverManager.getConnection(mySQLurl, dbname, dbpass);
			connection.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
