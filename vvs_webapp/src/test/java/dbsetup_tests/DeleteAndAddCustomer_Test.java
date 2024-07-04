package dbsetup_tests;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
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

/**
 * 3 (d)
 * This test class verifies the functionality of deleting and adding customers
 * in the database. It ensures that customers can be removed and then added 
 * again, and handles cases where an attempt is made to delete a non-existent customer.
 */
public class DeleteAndAddCustomer_Test {
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
     * Tests the deletion and re-addition of a customer in the database.
     * 
     * @throws ApplicationException if there is an error during the operations.
     */
    @Test
    public void testDeleteAndAddCustomer() throws ApplicationException {
        int vat = 197672337;
        CustomerService.INSTANCE.removeCustomer(vat);
        CustomerService.INSTANCE.addCustomer(vat, "Peter Steele", 914276732);
        CustomerDTO customer = CustomerService.INSTANCE.getCustomerByVat(vat);
        assertEquals("Peter Steele", customer.getDesignation(), "Customer should be added again.");
    }
    
    
    /**
     * Tests the deletion of a customer that does not exist in the database and 
     * then adds a new customer with that VAT number.
     * 
     * @throws ApplicationException if there is an error during the operations.
     */
    @Test
    public void testDeleteNotInDBCustomerAndAdd() throws ApplicationException {
        int vat = 509257810; // Non-existing VAT
        ApplicationException thrown = assertThrows(ApplicationException.class,
            () -> CustomerService.INSTANCE.removeCustomer(vat),
            "Expected removeCustomer to throw an exception for non-existing VAT number.");

        assertTrue(thrown.getMessage().contains("Customer with vat number " + vat + " doesn't exist."), "The exception message should confirm that the customer does not exist.");
        CustomerService.INSTANCE.addCustomer(vat, "Jon Snow", 914276732);
        CustomerDTO customer = CustomerService.INSTANCE.getCustomerByVat(vat);
        assertEquals("Jon Snow", customer.getDesignation(), "Customer should be added.");
    }
}
