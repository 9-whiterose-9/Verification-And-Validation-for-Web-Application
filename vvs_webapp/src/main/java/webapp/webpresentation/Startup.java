package webapp.webpresentation;

import java.net.URL;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import webapp.persistence.DataSource;
import webapp.persistence.PersistenceException;


/**
 * Application life cycle Listener implementation class 
 *
 */
@WebListener
public class Startup implements ServletContextListener {

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event)  { 
		// Connects to the database
		
		URL f = getClass().getClassLoader().getResource("/data/hsqldb");
		try {
			DataSource.INSTANCE.connect("jdbc:hsqldb:file:" + f.getPath() + "cssdb", "SA", "");
		} catch (PersistenceException e) {
			System.out.println("Error connecting database");
			System.out.println("Application Message: " + e.getMessage());
			System.out.println("SQLException: " + e.getCause().getMessage());
			System.out.println("SQLState: " + ((SQLException) e.getCause()).getSQLState());
			System.out.println("VendorError: " + ((SQLException) e.getCause()).getErrorCode());
			return;
		}
		catch (NullPointerException e){
			System.out.println("Error connecting database");
			System.out.println("Not able to find the resource.");
			return;
		}
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent event)  {
		try {
			System.out.println("closing HSQLDB connection.");
			DataSource.INSTANCE.close();
		} catch (Exception e) {
			System.out.println("bem tentei, mas...");
			e.printStackTrace();
		}
	}


}
