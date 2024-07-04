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

import webapp.services.ApplicationException;
import webapp.services.CustomerDTO;
import webapp.services.CustomerService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

/**
 * 3 (b)
 * Test class for updating customer contact information in the database.
 * This class uses DbSetup to initialize the database state before each test,
 * ensuring a consistent and isolated environment for testing.
 * 
 * The tests cover scenarios for updating customer phone numbers and handling 
 * invalid inputs.
 */
public class UpdateCustomerContact_Test {
	 // DataSource for connecting to the database
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
     * Tests updating the phone number of an existing customer.
     * 
     * @throws ApplicationException if there is an error during the update operation.
     */
    @Test
    public void testUpdateCustomerPhoneNum() throws ApplicationException {
    	int newPhoneNumber = 914444444;
    	int vat = 197672337;
        // Update customer contact
    	CustomerService.INSTANCE.updateCustomerPhone(vat, newPhoneNumber);
        CustomerDTO updatedCustomer = CustomerService.INSTANCE.getCustomerByVat(vat);
        assertEquals(newPhoneNumber, updatedCustomer.getPhoneNumber(),"Customer contact should be updated.");
    }
    
    /**
     * Tests updating the phone number of a customer with a VAT number that does 
     * not exist in the database.
     * 
     * @throws ApplicationException if there is an error during the update operation.
     */
    @Test
    public void testUpdateCustomerPhoneNumOnNonExistentInDBVAT() throws ApplicationException {
    	int newPhoneNumber = 914444444;
    	int vat = 503183504;
        // Update customer contact
    	CustomerService.INSTANCE.updateCustomerPhone(vat, newPhoneNumber);
        CustomerDTO updatedCustomer = CustomerService.INSTANCE.getCustomerByVat(vat);
        assertEquals(newPhoneNumber, updatedCustomer.getPhoneNumber(),"Customer contact should be updated.");
    }
    
    /**
     * Tests updating the phone number of a customer with an invalid phone number.
     * 
     * @throws ApplicationException if there is an error during the update operation.
     */
    @Test
    public void testUpdateCustomerContactOnInvalidNumber() throws ApplicationException {
    	int newPhoneNumber = 1;
    	int vat = 197672337;
        // Update customer contact
    	CustomerService.INSTANCE.updateCustomerPhone(vat, newPhoneNumber);
        CustomerDTO updatedCustomer = CustomerService.INSTANCE.getCustomerByVat(vat);
        assertEquals(newPhoneNumber, updatedCustomer.getPhoneNumber(),"Customer contact should be updated.");
    }
    
}
