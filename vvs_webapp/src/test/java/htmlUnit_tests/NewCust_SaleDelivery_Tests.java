package htmlUnit_tests;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import functions_for_tests.Addresses_Aux_Functions_For_Tests;
import functions_for_tests.Customer_Aux_Functions_For_Tests;
import functions_for_tests.Database_Aux_Functions_For_Tests;
import functions_for_tests.Deliveries_Aux_Functions_For_Tests;
import functions_for_tests.Sales_Aux_Functions_For_Tests;
/**
 * 2 (e)
 * This class contains tests for the complete workflow of creating a new customer,
 * creating a new sale for the customer, inserting a delivery for that sale, and 
 * verifying the correct handling and display of this data in the application.
 */
public class NewCust_SaleDelivery_Tests {
	
    private static WebClient webClient;
    private static Sales_Aux_Functions_For_Tests salesFunctions;
    private static Customer_Aux_Functions_For_Tests customerFunctions;
    private static Addresses_Aux_Functions_For_Tests addressesFunctions;
    private static Deliveries_Aux_Functions_For_Tests deliveriesFunctions;
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
        salesFunctions = new Sales_Aux_Functions_For_Tests();
        customerFunctions = new Customer_Aux_Functions_For_Tests();
        addressesFunctions = new Addresses_Aux_Functions_For_Tests();
        deliveriesFunctions = new Deliveries_Aux_Functions_For_Tests();
        dbFunctions = new Database_Aux_Functions_For_Tests();
        dbFunctions.initDatabase(webClient,customerFunctions);
    }
    
    /* ------------------------------------------------ TESTS -----------------------------------------------------------*/
    /**
     * Tests the complete workflow of adding a new customer, creating a sale, 
     * adding a delivery, and verifying the delivery details.
     * @throws Exception If there is an error accessing the web pages or interacting with forms.
     */
    @Test
    public void testNewCustomerSaleAndDelivery() throws Exception {
        String vatNumber = "501964843";
        String customerName = "David Gilmour";
        String phoneNumber = "911234567";

        // Add a new customer and address
        customerFunctions.setupCustomer(webClient, vatNumber, customerName, phoneNumber);
        addressesFunctions.addAddress(webClient, vatNumber, "1 Abbey Road", "1", "NW8 9AY", "London");
        String addressId = addressesFunctions.extractAddressId(webClient, vatNumber);

        // Create a new sale for the customer and extract the sale ID
        salesFunctions.createSaleForCustomer(webClient, vatNumber);
        String saleId = salesFunctions.extractLatestSaleId(webClient, vatNumber);

        // Insert a new delivery for the sale
        deliveriesFunctions.insertDelivery(webClient, vatNumber, saleId, addressId);

        // Check delivery details in the table
        assertTrue(deliveriesFunctions.checkDeliveryInTable(webClient, vatNumber, saleId, addressId), "The delivery details should be correctly shown and match expected details.");
    }
    
    /**
     * Tests that a new delivery is not created if the sale already has a delivery.
     * @throws Exception If there is an error accessing the web pages or interacting with forms.
     */
    @Test
    public void testNewDeliveryNotCreatedInSaleThatAlreadyHasIt() throws Exception {
        String vatNumber = "501964843";
        String customerName = "Olivia Newton";
        String phoneNumber = "911234567";

        // Add a new customer and address
        customerFunctions.setupCustomer(webClient, vatNumber, customerName, phoneNumber);
        addressesFunctions.addAddress(webClient, vatNumber, "1 Abbey Road", "1", "NW8 9AY", "London");
        String addressId = addressesFunctions.extractAddressId(webClient, vatNumber);

        // Create a new sale for the customer and extract the sale ID
        salesFunctions.createSaleForCustomer(webClient, vatNumber);
        String saleId = salesFunctions.extractLatestSaleId(webClient, vatNumber);

        // Insert a new delivery for the sale
        deliveriesFunctions.insertDelivery(webClient, vatNumber, saleId, addressId);

        // Try to insert another delivery for the same sale
        deliveriesFunctions.insertDelivery(webClient, vatNumber, saleId, addressId);

        // Check if the second delivery was not created
        assertFalse(deliveriesFunctions.checkDeliveryInTable(webClient, vatNumber, saleId, addressId), "A new delivery should not be created for a sale that already has one.");
    }
    
    /**
     * Tests the system's handling of invalid customer information during customer creation.
     * @throws Exception If there is an error accessing the web pages or interacting with forms.
     */
    @Test
    public void testInvalidCustomerInformationHandling() throws Exception {
        // Attempt to add a customer with missing VAT number
        HtmlPage addCustomerPageWithMissingVat = webClient.getPage("http://localhost:8080/VVS_webappdemo/addCustomer.html");
        customerFunctions.addCustomer(webClient, "", "Jon Snow", "123456789");
        assertTrue(addCustomerPageWithMissingVat.asText().contains("Error"), "Error message should be displayed for adding a customer with missing VAT number.");

        // Attempt to add a customer with empty designation
        HtmlPage addCustomerPageWithEmptyDesignation = webClient.getPage("http://localhost:8080/VVS_webappdemo/addCustomer.html");
        customerFunctions.addCustomer(webClient, "123456789", "", "123456789");
        assertTrue(addCustomerPageWithEmptyDesignation.asText().contains("Error"), "Error message should be displayed for adding a customer with empty designation.");
    }
    
    /**
     * Tests the system's handling of invalid address information during address creation.
     * @throws Exception If there is an error accessing the web pages or interacting with forms.
     */
    @Test
    public void testInvalidAddressInformationHandling() throws Exception {
        // Attempt to add an address with missing street address
        HtmlPage addAddressPageWithMissingStreet = webClient.getPage("http://localhost:8080/VVS_webappdemo/addAddressToCustomer.html");
        addressesFunctions.addAddress(webClient, "123456789", "", "1", "12345", "City");
        assertTrue(addAddressPageWithMissingStreet.asText().contains("Error"), "Error message should be displayed for adding an address with missing street address.");

        // Attempt to add an address with empty postal code
        HtmlPage addAddressPageWithEmptyPostalCode = webClient.getPage("http://localhost:8080/VVS_webappdemo/addAddressToCustomer.html");
        addressesFunctions.addAddress(webClient, "123456789", "123 Street", "1", "", "City");
        assertTrue(addAddressPageWithEmptyPostalCode.asText().contains("Error"), "Error message should be displayed for adding an address with empty postal code.");
    }

    /**
     * Tests the system's behavior when attempting to create a delivery without associating it with any sale.
     * @throws Exception If there is an error accessing the web pages or interacting with forms.
     */
    @Test
    public void testDeliveryCreationWithoutSale() throws Exception {
        String vatNumber = "501964843";
        String customerName = "Albert Einstein";
        String phoneNumber = "911555567";

        // Add a new customer and address
        customerFunctions.setupCustomer(webClient, vatNumber, customerName, phoneNumber);
        addressesFunctions.addAddress(webClient, vatNumber, "1 Abbey Road", "1", "NW8 9AY", "London");
        String addressId = addressesFunctions.extractAddressId(webClient, vatNumber);

        // Attempt to create a delivery without associating it with any sale
        HtmlPage addDeliveryPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/AddSaleDeliveryPageController?vat=" + vatNumber);
        HtmlForm deliveryForm = addDeliveryPage.getForms().get(0);
        // Do not set sale ID
        deliveryForm.getInputByName("addr_id").setValueAttribute(addressId);
        HtmlPage resultPage = deliveryForm.getInputByValue("Insert").click();

        // Ensure the delivery creation fails gracefully
        assertTrue(resultPage.asText().contains("Error"), "Error message should be displayed for delivery creation without sale.");
        
        // Verify that no delivery was created
        assertFalse(deliveriesFunctions.checkDeliveryInTable(webClient, vatNumber, "", addressId), "No delivery should be created without a sale.");
    }
    
    /**
     * Tests the application's behavior when attempting to create a delivery without associating it with any address.
     * @throws Exception If there is an error accessing the web pages or interacting with forms.
     */
    @Test
    public void testDeliveryCreationWithoutAddress() throws Exception {
        String vatNumber = "501964843";
        String customerName = "John Travolta";
        String phoneNumber = "911777567";

        // Add a new customer
        customerFunctions.setupCustomer(webClient, vatNumber, customerName, phoneNumber);

        // Create a sale for the customer
        HtmlPage newSalePage = webClient.getPage("http://localhost:8080/VVS_webappdemo/addSale.html");
        HtmlForm saleForm = newSalePage.getForms().get(0);
        saleForm.getInputByName("customerVat").setValueAttribute(vatNumber);
        HtmlPage resultSalePage = saleForm.getInputByValue("Add Sale").click();
        String saleId = salesFunctions.extractLatestSaleId(webClient, vatNumber);

        // Attempt to create a delivery without associating it with any address
        HtmlPage addDeliveryPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/AddSaleDeliveryPageController?vat=" + vatNumber);
        HtmlForm deliveryForm = addDeliveryPage.getForms().get(0);
        deliveryForm.getInputByName("sale_id").setValueAttribute(saleId);
        // Do not set address ID
        HtmlPage resultPage = deliveryForm.getInputByValue("Insert").click();

        // Ensure the delivery creation fails gracefully
        assertTrue(resultPage.asText().contains("Error"), "Error message should be displayed for delivery creation without address.");
        
        // Verify that no delivery was created
        assertFalse(deliveriesFunctions.checkDeliveryInTable(webClient, vatNumber, saleId, ""), "No delivery should be created without an address.");
    }
    
    /**
     * Tests the system's behavior when attempting to create a delivery with an invalid address.
     * @throws Exception If there is an error accessing the web pages or interacting with forms.
     */
    @Test
    public void testDeliveryCreationWithInvalidAddress() throws Exception { 
        String vatNumber = "501964843";
        String customerName = "Arya Stark";
        String phoneNumber = "911888999";

        // Add a new customer
        customerFunctions.setupCustomer(webClient, vatNumber, customerName, phoneNumber);

        // Create a sale for the customer
        salesFunctions.createSaleForCustomer(webClient, vatNumber);
        String saleId = salesFunctions.extractLatestSaleId(webClient, vatNumber);

        // Attempt to create a delivery with an invalid address ID
        HtmlPage addDeliveryPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/AddSaleDeliveryPageController?vat=" + vatNumber);
        HtmlForm deliveryForm = addDeliveryPage.getForms().get(0);
        deliveryForm.getInputByName("addr_id").setValueAttribute("9999"); // Assume 9999 is an invalid address ID
        deliveryForm.getInputByName("sale_id").setValueAttribute(saleId);
        HtmlPage resultPage = deliveryForm.getInputByValue("Insert").click();

        // Ensure an error message is displayed for invalid address ID
        assertTrue(resultPage.asText().contains("Error"), "Error message should be displayed for delivery creation with an invalid address.");
        
        // Verify that no delivery was created
        assertFalse(deliveriesFunctions.checkDeliveryInTable(webClient, vatNumber, saleId, "9999"), "No delivery should be created with an invalid address.");
    }

}
