package dbsetup_tests;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import webapp.services.ApplicationException;
import webapp.services.SaleService;
import webapp.services.SalesDTO;

/**
 * 3 (f)
 * Test class to verify the addition of a new sale in the system.
 * This class ensures that the sale insertion operation is functioning correctly.
 */
public class AddNewSale_Test {
	private static Destination dataSource;
    private static DbSetupTracker dbSetupTracker = new DbSetupTracker();
    private int initialSaleCount;
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
     * Test to verify that a new sale is added correctly.
     * This test checks that the total number of sales increases by one after a new sale is added.
     * 
     * @throws ApplicationException if there is an error during the operations.
     */
    @Test
    public void testAddNewSale() throws ApplicationException {
        // Get the initial count of sales before adding a new one
        int customerVat = 197672337;
        SalesDTO initialSales = SaleService.INSTANCE.getAllSales();
        int initialSaleCount = initialSales.getSales().size();

        // Add a new sale for the given customer VAT
        SaleService.INSTANCE.addSale(customerVat);

        // Get the updated count of sales after adding the new sale
        SalesDTO updatedSales = SaleService.INSTANCE.getAllSales();
        assertEquals(initialSaleCount + 1, updatedSales.getSales().size(), "Total number of sales should increase by one.");
    }

}
