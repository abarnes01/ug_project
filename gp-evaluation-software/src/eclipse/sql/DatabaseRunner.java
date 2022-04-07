package eclipse.sql;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.ibatis.jdbc.ScriptRunner;

public class DatabaseRunner {
	
	private String dburl, dbname, dbpass;
	
	public DatabaseRunner(String dburl, String dbname, String dbpass) {
		this.dburl = dburl;
		this.dbname = dbname;
		this.dbpass = dbpass;
	}
		
	public final String getDburl() {
		return dburl;
	}

	public final void setDburl(String dburl) {
		this.dburl = dburl;
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

	public void createDB() throws Exception {
		
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		
		String url = "jdbc:mysql://localhost:3306/";
		String dbname = "root";
		String dbpass = "";
		
		Connection con = DriverManager.getConnection(url, dbname, dbpass);
		System.out.println("Database connection established...");
		
		ScriptRunner sr = new ScriptRunner(con);
		
		Reader rd = new BufferedReader(new FileReader("/Users/adamjbarnes/Documents/gp-project/ab1049/SQL Dependencies/gpscript.sql"));
		
		sr.runScript(rd);
		rd.close();
		sr.closeConnection();
		con.close();
		
	}
	
	public void dropDB() throws Exception {
	
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		
		String url = "jdbc:mysql://localhost:3306/";
		String dbname = "root";
		String dbpass = "";
		
		Connection con = DriverManager.getConnection(url, dbname, dbpass);
		System.out.println("Database connection established...");
		
		ScriptRunner sr = new ScriptRunner(con);
		
		Reader rd = new BufferedReader(new FileReader("/Users/adamjbarnes/Documents/gp-project/ab1049/SQL Dependencies/dropdb.sql"));
		
		sr.runScript(rd);
		rd.close();
		sr.closeConnection();
		con.close();
		
	}
}
