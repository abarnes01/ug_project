package eclipse.sql;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.ibatis.jdbc.ScriptRunner;

public class DatabaseRunner {
	
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
		String dbpass = "Footyclone2001";
		
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
