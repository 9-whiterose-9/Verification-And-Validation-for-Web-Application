package dbsetup_tests;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;

import webapp.services.ApplicationException;
import webapp.services.CustomerService;
import webapp.services.SaleService;
import webapp.services.SalesDTO;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

/**
 * 3 (e)
 * This test class verifies the functionality of deleting a customer and ensuring 
 * that all sales associated with the customer are also deleted.
 * It uses DbSetup to initialize the database state before each test, ensuring 
 * a consistent and isolated environment for testing.
 */
public class DeleteCustomerAndCheckSales_Test {
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
     * Tests the deletion of a customer and ensures that all sales associated 
     * with the customer are also deleted.
     * 
     * @throws ApplicationException if there is an error during the delete operation.
     */
    @Test
    public void testDeleteCustomerAndCheckSales() throws ApplicationException {
        int vat = 197672337; // Assuming this VAT number is linked to a customer in the database.

        // Ensure the customer has sales
        SalesDTO initialSales = SaleService.INSTANCE.getSaleByCustomerVat(vat);
        if (initialSales.getSales().isEmpty()) {
            // If no sales, create some
            SaleService.INSTANCE.addSale(vat);
            SaleService.INSTANCE.addSale(vat);
        }

        // Delete the customer
        CustomerService.INSTANCE.removeCustomer(vat);

        // Check if sales associated with the customer are deleted
        SalesDTO salesAfterDeletion = SaleService.INSTANCE.getSaleByCustomerVat(vat);
        assertTrue(salesAfterDeletion.getSales().isEmpty(), "All sales for the customer should be deleted after customer deletion.");
    }
}
