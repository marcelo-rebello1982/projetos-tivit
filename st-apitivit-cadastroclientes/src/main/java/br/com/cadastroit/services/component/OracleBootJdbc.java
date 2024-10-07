package br.com.cadastroit.services.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.Properties;

import org.springframework.stereotype.Component;


@Component
public class OracleBootJdbc {
	
	private String HOST_FILE = "/opt/sfconfig/";
	private String URL;
	private String USER;
	private String PASS;
	private String DRIVER;
	
	private void getProperties() {
		
		if(System.getenv("JDBC_DRIVER") != null) {
			DRIVER 	= System.getenv("JDBC_DRIVER");
		}
		if(System.getenv("SPRING_DATASOURCE_URL") != null) {
			URL  	= new String(Base64.getDecoder().decode(System.getenv("SPRING_DATASOURCE_URL")));
		}
		if(System.getenv("SPRING_DATASOURCE_USERNAME") != null) {
			USER 	= new String(Base64.getDecoder().decode(System.getenv("SPRING_DATASOURCE_USERNAME")));
		}
		if(System.getenv("SPRING_DATASOURCE_PASSWORD") != null) {
			PASS 	= new String(Base64.getDecoder().decode(System.getenv("SPRING_DATASOURCE_PASSWORD")));
		}
		
		if(URL == null) {
			if(OsDetect.OS_NAME().contains("windows"))HOST_FILE="C:\\configdb\\";
			File f = new File(HOST_FILE+"jdbc.properties");
			Properties properties = new Properties();
			try(InputStream is = new FileInputStream(f)){
				properties.load(is);
				
				DRIVER = properties.getProperty("PARAM_DRIVER_PGSQL");
				URL    = properties.getProperty("PARAM_URL_PGSQL");
				USER   = properties.getProperty("PARAM_USER_PGSQL");
				PASS   = properties.getProperty("PARAM_PASS_PGSQL");
				
			}catch(IOException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
	
	public Connection connection() throws ClassNotFoundException,SQLException {
		this.getProperties();
		Class.forName(DRIVER);
		return DriverManager.getConnection(URL,USER,PASS);
	}
	
	public Statement statement(Connection connection) throws SQLException{
		return connection.createStatement();
	}
	
	public PreparedStatement preparedStatement(Connection connection, String sql) throws SQLException{
		return connection.prepareStatement(sql);
	}
	
	public void closeConnection(Connection connection) throws SQLException {
		connection.close();
	}
	
	public void closeStatment(Statement statement) throws SQLException{
		statement.close();
	}
	
	public void closePrepareStament(PreparedStatement preparedStatement) throws SQLException{
		preparedStatement.close();
	}
	
	public void closeResultSet(ResultSet resultSet) throws SQLException {
		resultSet.close();
	}
	
	public void close(Connection connection, Statement statement, PreparedStatement preparedStatement, ResultSet resultSet) throws SQLException {
		if(resultSet != null)resultSet.close();
		if(statement != null)statement.close();
		if(preparedStatement != null)preparedStatement.close();
		if(connection != null)connection.close();
	}
}
