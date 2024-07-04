package htmlUnit_tests;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import functions_for_tests.Addresses_Aux_Functions_For_Tests ;
import functions_for_tests.Customer_Aux_Functions_For_Tests;
import functions_for_tests.Database_Aux_Functions_For_Tests;

/**
 * 2 (a)
 * Tests for adding new addresses to a customer in the vvs_webapp.
 */
public class NewAddresses_Tests {

    private static WebClient webClient;
    private static Customer_Aux_Functions_For_Tests customerFunctions;
    private static Addresses_Aux_Functions_For_Tests addressesFunctions;
    private static Database_Aux_Functions_For_Tests dbFunctions;
    
    /**
     * Setup for WebClient with disabled CSS and JavaScript for testing purposes.
     * @throws Exception 
     */
    @BeforeAll
    public static void setupClass() throws Exception {
        webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        customerFunctions = new Customer_Aux_Functions_For_Tests();
        addressesFunctions = new Addresses_Aux_Functions_For_Tests();
        dbFunctions = new Database_Aux_Functions_For_Tests();
        dbFunctions.initDatabase(webClient,customerFunctions);
    }
    /* ------------------------------------------------ TESTS ----------------------------------------------------------- */
    /**
     * Test method to verify the addition of two new addresses to a customer.
     * Checks that the total number of addresses increases by two.
     * @throws Exception if the test fails due to errors during execution.
     */
    @Test
    public void testAddTwoAddresses() throws Exception {
        final String vatNumber = "197672337"; // The VAT number to test with

        customerFunctions.deleteCustomer(webClient,vatNumber);
        customerFunctions.addCustomer(webClient,vatNumber, "beatriz", "123456789");

        // Initial fetch of customer details to establish a baseline for address count
        HtmlPage customerPage = customerFunctions.refreshCustomerDetails(webClient,vatNumber);
        int initialCount = addressesFunctions.countAddresses(customerPage);

        // Add addresses
        addressesFunctions.addAddress(webClient,vatNumber,"123 New Street", "101", "1000-100", "Lisbon");
        addressesFunctions.addAddress(webClient,vatNumber,"124 New Street", "102", "1000-101", "Lisbon");

        // Refresh the customer details to verify addresses were added
        customerPage = customerFunctions.refreshCustomerDetails(webClient,vatNumber);
        int finalCount = addressesFunctions.countAddresses(customerPage);

        // Verify that the address count has increased by two
        assertEquals(initialCount + 2, finalCount, "Address count should increase by two.");
    }
    
    /**
     * Tests the integrity of the data for newly added addresses.
     * Ensures that the addresses added through the form appear correctly on the customer details page.
     * This confirms that the data is accurately stored and retrieved in the system.
     */

    @Test
    public void testDataIntegrity() throws Exception {
        final String vatNumber = "197672337";
        final String address = "123 New Street";
        final String door = "101";
        final String postalCode = "1000-100";
        final String locality = "Lisbon";

        customerFunctions.deleteCustomer(webClient,vatNumber);
        customerFunctions.addCustomer(webClient,vatNumber, "beatriz", "123456789");
        addressesFunctions.addAddress(webClient,vatNumber, address, door, postalCode, locality);

        HtmlPage customerPage = customerFunctions.refreshCustomerDetails(webClient,vatNumber);
        HtmlTable addressTable = (HtmlTable) customerPage.getFirstByXPath("//table[@class='w3-table w3-bordered']");
        assertTrue(addressTable.asText().contains(address) && addressTable.asText().contains(door) &&
                   addressTable.asText().contains(postalCode) && addressTable.asText().contains(locality));
    }
    
    /**
     * Tests the idempotence of the address addition functionality.
     * Attempts to add the same address twice and checks how the system handles it.
     * The expected behavior should conform to the application's specification on handling duplicates,
     * which might include ignoring the duplicate, displaying an error, or adding both.
     */
    @Test
    public void testIdempotence() throws Exception {
        final String vatNumber = "197672337";
        final String address = "125 New Street";
        final String door = "102";
        final String postalCode = "1001-101";
        final String locality = "Lisbon";

        customerFunctions.deleteCustomer(webClient,vatNumber);
        customerFunctions.addCustomer(webClient,vatNumber, "Nick mason", "123456789");
        addressesFunctions.addAddress(webClient,vatNumber,address, door, postalCode, locality);
        addressesFunctions.addAddress(webClient,vatNumber,address, door, postalCode, locality); // Attempt to add the same address again

        HtmlPage customerPage = customerFunctions.refreshCustomerDetails(webClient,vatNumber);
        HtmlTable addressTable = (HtmlTable) customerPage.getFirstByXPath("//table[@class='w3-table w3-bordered']");
        int finalCount = addressTable.getRowCount() - 1; // Subtract header row
        assertEquals(1, finalCount); // Assumes the system prevents duplicates
    }

    /**
     * Simulates the concurrent addition of addresses to test the system's behavior under a multi-user scenario.
     * This test helps identify any potential race conditions or database locking issues by adding multiple addresses in rapid succession.
     * Note: Actual concurrency can't be fully tested with HtmlUnit as it runs in a single-threaded environment; this test simulates rapid sequential actions.
     */
    @Test
    public void testConcurrency() throws Exception {
        final String vatNumber = "197672337";

        customerFunctions.deleteCustomer(webClient,vatNumber);
        customerFunctions.addCustomer(webClient,vatNumber, "Jim Morrison", "123456789");

        Thread thread1 = new Thread(() -> {
            try {
            	addressesFunctions.addAddress(webClient,vatNumber,"126 New Street", "103", "1002-102", "Lisbon");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
            	addressesFunctions.addAddress(webClient,vatNumber,"127 New Street", "104", "1003-103", "Lisbon");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        HtmlPage customerPage = customerFunctions.refreshCustomerDetails(webClient,vatNumber);
        HtmlTable addressTable = (HtmlTable) customerPage.getFirstByXPath("//table[@class='w3-table w3-bordered']");
        int finalCount = addressTable.getRowCount() - 1; // Subtract header row
        assertEquals(2, finalCount); // Assumes both addresses are added without issues
    }

    /**
     * Tests the system's robustness in handling invalid address data.
     * Inputs include empty strings, incorrect postal code formats, and other invalid data to see how the system reacts.
     * Ensures that the system can handle such cases gracefully by rejecting them or providing appropriate feedback, which is crucial for maintaining data integrity.
     */
    @Test
    public void testNegativeCases() throws Exception {
        final String vatNumber = "197672337";
        final String address = ""; // Invalid data
        final String door = "105";
        final String postalCode = "abc"; // Invalid format
        final String locality = "";

        customerFunctions.deleteCustomer(webClient,vatNumber);
        customerFunctions.addCustomer(webClient,vatNumber, "beatriz", "123456789");
        addressesFunctions.addAddress(webClient,vatNumber,address, door, postalCode, locality);

        HtmlPage customerPage = customerFunctions.refreshCustomerDetails(webClient,vatNumber);
        HtmlTable addressTable = (HtmlTable) customerPage.getFirstByXPath("//table[@class='w3-table w3-bordered']");
        int finalCount = addressTable.getRowCount() - 1; // Subtract header row
        assertEquals(0, finalCount); // Assumes invalid data is not added
    }





}
