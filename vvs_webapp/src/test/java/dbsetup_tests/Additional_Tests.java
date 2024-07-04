package dbsetup_tests;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;

import webapp.persistence.SaleStatus;
import webapp.services.AddressesDTO;
import webapp.services.ApplicationException;
import webapp.services.CustomerService;
import webapp.services.SaleService;
import webapp.services.SalesDTO;
import webapp.services.SalesDeliveryDTO;

/**
 * Tests additional functionalities related to sales and sales deliveries within the system.
 * This class contains methods to test the creation, updating, and retrieval of sales and their deliveries,
 * ensuring that the business rules and logic are properly enforced.
 * 
 * The tests ensure that:
 * 
 *     - A new sale increases the total count of sales;
 *     - Updating a sale's status reflects correctly in the system;
 *     - Sale deliveries can be added and then accurately retrieved by VAT number.
 * 
 * 
 * These tests use the DBSetup framework to set up a predictable database state before each test.
 * This helps in verifying that the service layer reacts correctly to various scenarios.
 * 
 * @author 
 * @version 1.0
 * 
 */
public class Additional_Tests {
	private static Destination dataSource;
    private static DbSetupTracker dbSetupTracker = new DbSetupTracker();
    private int existingCustomerVAT = 197672337; // Example VAT, replace with actual data
    private int nonExistingCustomerVAT = 509257810 ; // Non-existing VAT for negative test cases

    /**
     * Sets up database connections and ensures that the database is in a known state before all tests are run.
     * This method is executed once before all test methods in the class.
     */
    @BeforeAll
    public static void setupClass() {
    System.out.println("setup Class()... ");
    	
     DBSetupUtils.startApplicationDatabaseForTesting();
		dataSource = DriverManagerDestination.with(DBSetupUtils.DB_URL, DBSetupUtils.DB_USERNAME, DBSetupUtils.DB_PASSWORD);
    }
    
    /**
     * Prepares the database for each test by cleaning and reinserting necessary data.
     * This ensures each test runs against a predictable and isolated database state.
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
     * Test that attempts to create a sale for a VAT number that does not exist in the database.
     * This test should fail, throwing an ApplicationException because no customer corresponds to the provided VAT.
     */
    @Test
    public void testCreateSaleForNonExistentVATInDB() {
        int nonExistentVAT = 503183504; // This VAT does not exist in the database.
        
        // Expect an ApplicationException to be thrown due to the non-existent customer
        ApplicationException thrown = assertThrows(ApplicationException.class, () -> {
            SaleService.INSTANCE.addSale(nonExistentVAT);
        }, "Expected ApplicationException to be thrown when adding a sale for a non-existent VAT");

        assertTrue(thrown.getMessage().contains("Can't add customer with vat number"), "The exception message should indicate that the customer could not be added.");
    }

    /**
     * Tests that updating the status of a sale is reflected correctly in the system.
     * @throws ApplicationException if there is an error during the application execution
     */
    @Test
    public void testUpdateSaleStatus() throws ApplicationException {
        // Test updating the sale status
        SaleService.INSTANCE.addSale(existingCustomerVAT); // Add a sale to update
        SalesDTO sales = SaleService.INSTANCE.getAllSales();
        int saleId = sales.getSales().get(0).getId(); // Get the ID of the first sale

        SaleService.INSTANCE.updateSale(saleId); // Update the sale status
        SalesDTO updatedSales = SaleService.INSTANCE.getAllSales();
        assertEquals("C", updatedSales.getSales().get(0).getStatusId(), "The sale status should be updated to CLOSED.");
    }


    /**
     * Tests adding a sale delivery to a sale and ensuring the VAT is returned correctly.
     * @throws ApplicationException if there is an error during the application execution
     */
    @Test
    public void testSingleDeliveryPerSale() throws ApplicationException {
        // Assume a sale ID that exists, you might want to create this in a reliable setup method or ensure it exists.
        int saleId = createSaleForExistingCustomer(existingCustomerVAT);

        // Check if there is an address for this customer, if not, add one.
        AddressesDTO addresses = CustomerService.INSTANCE.getAllAddresses(existingCustomerVAT);
        int addrId;
        if (addresses.getAddresses().isEmpty()) {
            // Add an address if none exist
            String newAddress = "123 New Street";
            CustomerService.INSTANCE.addAddressToCustomer(existingCustomerVAT, newAddress);
            addresses = CustomerService.INSTANCE.getAllAddresses(existingCustomerVAT);
        }
        addrId = addresses.getAddresses().get(0).getId();  // Get the first available address ID

        // Add the first delivery successfully
        SaleService.INSTANCE.addSaleDelivery(saleId, addrId);

        // Attempt to add another delivery to the same sale, which should not be allowed
        ApplicationException thrown = assertThrows(ApplicationException.class,
            () -> SaleService.INSTANCE.addSaleDelivery(saleId, addrId),
            "Expected an ApplicationException to be thrown for adding multiple deliveries to the same sale.");

        assertTrue(thrown.getMessage().contains("delivery already exists for sale"), "Should report that a delivery already exists.");
    }

    /**
     * Helper method to create a sale for an existing customer to ensure the test setup is reliable.
     * This method assumes that the customer already exists and has a valid VAT.
     */
    private int createSaleForExistingCustomer(int customerVat) throws ApplicationException {
        SaleService.INSTANCE.addSale(customerVat);
        SalesDTO sales = SaleService.INSTANCE.getAllSales();
        return sales.getSales().stream()
                    .filter(s -> s.getCustomerVat() == customerVat)
                    .findFirst()
                    .orElseThrow(() -> new ApplicationException("No sale found for the given customer VAT."))
                    .getId();
    }

    
    /**
     * Tests retrieving sales deliveries by VAT number to verify that deliveries are correctly linked to sales.
     * @throws ApplicationException if there is an error during the application execution
     */

    @Test
    public void testGetSalesDeliveryByVat() throws ApplicationException {
        // Test retrieving sales deliveries by VAT
        SaleService.INSTANCE.addSale(existingCustomerVAT); // Add a sale
        SalesDTO sales = SaleService.INSTANCE.getAllSales();
        int saleId = sales.getSales().get(0).getId(); // Get the ID of the first sale
        int addrId = 1; // Assuming an address ID that exists

        SaleService.INSTANCE.addSaleDelivery(saleId, addrId); // Add delivery
        SalesDeliveryDTO deliveries = SaleService.INSTANCE.getSalesDeliveryByVat(existingCustomerVAT);
        assertNotNull(deliveries, "Sales deliveries should be retrieved by VAT.");
        assertEquals(1, deliveries.getSales_delivery().size(), "There should be exactly one delivery for the VAT.");
    }
    
}
