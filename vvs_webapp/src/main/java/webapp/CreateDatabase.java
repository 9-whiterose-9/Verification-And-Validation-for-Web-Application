package webapp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDatabase {

	public static void main(String[] args) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException {
		new CreateDatabase().createCSSHSQLDB();
	}

	public void createCSSHSQLDB() throws FileNotFoundException, IOException, SQLException, ClassNotFoundException {
		Connection dbc = DriverManager.getConnection ("jdbc:hsqldb:file:src/main/resources/data/hsqldb/cssdb", "SA", "");
		
		runScript(dbc, "src/main/resources/dropDDLHSQLDB.sql");
		runScript(dbc, "src/main/resources/createDDLHSQLDB.sql");
		
		dbc.close();
	}
 

	public static void runScript (Connection connection, String scriptFilename) throws FileNotFoundException, IOException, SQLException {
		try (BufferedReader br = new BufferedReader(new FileReader(scriptFilename))) {
			String command;
			int i = 1;
			while ((command = br.readLine()) != null) {
				System.out.println(i + ": " + command);
				i++;
				Statement statement = connection.createStatement();
				statement.execute(command.toString());
				statement.close();
			}
		}
	}

}
