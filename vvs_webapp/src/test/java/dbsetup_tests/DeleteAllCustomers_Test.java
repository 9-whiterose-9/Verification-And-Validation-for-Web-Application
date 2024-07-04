package dbsetup_tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;

import webapp.services.ApplicationException;
import webapp.services.CustomerDTO;
import webapp.services.CustomerService;
import webapp.services.CustomersDTO;

/**
 * 3 (c)
 * This test class verifies the functionality of deleting all customers 
 * in the database. It ensures that all customer records can be removed 
 * and that the database is empty after the deletion operation.
 */
public class DeleteAllCustomers_Test {
	private static Destination dataSource;
    private static DbSetupTracker dbSetupTracker = new DbSetupTracker();
    /**
     * Initializes the database connection and starts the application database 
     * before any tests are run. This method is executed once for the entire test class.
     */
    @BeforeAll
    public static void setupClass() {
    System.out.println("setup Class()... ");
    	
     DBSetupUtils.startApplicationDatabaseForTesting();
		dataSource = DriverManagerDestination.with(DBSetupUtils.DB_URL, DBSetupUtils.DB_USERNAME, DBSetupUtils.DB_PASSWORD);
    }
    
    /**
     * Sets up the database state before each test. This method is executed before 
     * each test method to ensure a consistent database state.
     * 
     * @throws SQLException if there is an error setting up the database.
     */
    @BeforeEach
    public void setup() throws SQLException {
        System.out.println("Setting up database for new test...");
        Operation initDBOperations = Operations.sequenceOf(
            DBSetupUtils.DELETE_ALL, 
            DBSetupUtils.INSERT_CUSTOMER_ADDRESS_DATA
        );

		DbSetup dbSetup = new DbSetup(dataSource, initDBOperations);
		
        // Use the tracker to launch the DBSetup. This will speed-up tests that do not change the DB. 
		// Otherwise, just use dbSetup.launch();
        //dbSetupTracker.launchIfNecessary(dbSetup);
		dbSetup.launch();
		System.out.println("Check if data is present in the database.");
        // After setup to confirm the state of the database
        System.out.println("Database setup completed. Data should now be in the database.");
    }
    
    /**
     * Tests the deletion of all customers in the database.
     * 
     * @throws ApplicationException if there is an error during the operations.
     */
    @Test
    public void testDeleteAllCustomers() throws ApplicationException {
        CustomersDTO customers = CustomerService.INSTANCE.getAllCustomers();
        if (!customers.getCustomers().isEmpty()) {
            for (CustomerDTO customer : customers.getCustomers()) {
                CustomerService.INSTANCE.removeCustomer(customer.getVat());
            }
            // Re-fetch customers to assert that all have been deleted
            customers = CustomerService.INSTANCE.getAllCustomers();
        }
        assertTrue(customers.getCustomers().isEmpty(), "All customers should be deleted.");
    }
}
