package dbsetup_tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;

import webapp.services.ApplicationException;
import webapp.services.CustomerService;


/**
 * Question 3 (a)
 * Test class to verify the business rule that prohibits adding a new customer
 * with a VAT number that already exists in the database.
 * It checks that an {@link ApplicationException} is thrown in such cases.
 */

public class AddNewClientWithExistingVAT_Test {
	private static Destination dataSource;
    private static DbSetupTracker dbSetupTracker = new DbSetupTracker();
	
    @BeforeAll
    public static void setupClass() {
    System.out.println("setup Class()... ");
    	
     DBSetupUtils.startApplicationDatabaseForTesting();
		dataSource = DriverManagerDestination.with(DBSetupUtils.DB_URL, DBSetupUtils.DB_USERNAME, DBSetupUtils.DB_PASSWORD);
    }
    
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
     * Tests that adding a new customer with an existing VAT number throws an ApplicationException.
     * @throws Exception if there is an unexpected error during the test execution.
     */
    @Test
    public void testAddNewClientWithExistingVAT() throws Exception {
        int existingVAT = 197672337; // This VAT number should already exist in the database
        System.out.println("Testing with VAT: " + existingVAT);
        
        Executable executable = () -> CustomerService.INSTANCE.addCustomer(existingVAT, "NEW CUSTOMER", 910898989);
        
        System.out.println("Executing addCustomer method...");
        ApplicationException thrown = assertThrows(ApplicationException.class, executable,
                "Expected addCustomer to throw, but it did not");

        
        System.out.println("Test completed without errors.");
    }
}
