package htmlUnit_tests;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import functions_for_tests.Addresses_Aux_Functions_For_Tests;
import functions_for_tests.Customer_Aux_Functions_For_Tests;
import functions_for_tests.Database_Aux_Functions_For_Tests;
import functions_for_tests.Deliveries_Aux_Functions_For_Tests;
import functions_for_tests.Sales_Aux_Functions_For_Tests;

/**
 * 2 (b)
 */
public class NewCustomers_Tests {
    
    private static WebClient webClient;
    private static Customer_Aux_Functions_For_Tests customerFunctions;
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
        dbFunctions = new Database_Aux_Functions_For_Tests();
        dbFunctions.initDatabase(webClient,customerFunctions);
    }
	/* ------------------------------------------------ Insertion TESTS -----------------------------------------------------------*/
    /**
     * Verifies the correct insertion and listing of two new customers.
     * This method tests the functionality of adding new customers and ensures
     * that these customers are correctly listed in the "List All Customers" use case.
     * @throws Exception If there is an error accessing the web pages or interacting with forms.
     */
	@Test
	public void testInsertionOfTwoCustomers() throws Exception {
	    // First, ensure no customers exist with the provided VAT numbers or delete if exists.
		customerFunctions.deleteCustomer(webClient,"123456789");
		customerFunctions.deleteCustomer(webClient,"240509072");

	    // Add two new customers
		customerFunctions.addCustomer(webClient,"123456789", "Furio", "111222333");
		customerFunctions.addCustomer(webClient,"240509072", "Tony Soprano", "123456789");
	    
	    // Verify that all information is listed correctly
	    HtmlPage allCustomersPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/GetAllCustomersPageController");
	    assertTrue(allCustomersPage.asText().contains("Furio"));
	    assertTrue(allCustomersPage.asText().contains("Tony Soprano"));
	    assertTrue(allCustomersPage.asText().contains("123456789"));
	    assertTrue(allCustomersPage.asText().contains("240509072"));
	}
//	
//    /**
//     * Tests that a duplicate VAT number cannot be used to add a second customer.
//     * @throws Exception if the page cannot be loaded or the form interaction fails.
//     */
    @Test
    public void testInsertionOfTwoCustomersDuplicateVAT() throws Exception {
        String vat = "501964843";
        customerFunctions.deleteCustomer(webClient,vat);

        customerFunctions.addCustomer(webClient,vat, "Michael Jackson", "913456789");
        customerFunctions.addCustomer(webClient,vat, "Amy Winehouse", "913456790"); // Attempt to add a second customer with the same VAT

        HtmlPage listPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/GetAllCustomersPageController");
        assertFalse(listPage.asText().contains("Amy Winehouse"), "Duplicate VAT should not allow adding a second customer.");
    }
//	/* ------------------------------------------------ VAT TESTS -----------------------------------------------------------*/
	@Test
	public void testInvalidVATType() throws Exception {
	    String invalidVAT = "invalidVAT"; // Simulated invalid VAT
	    String expectedErrorMessage = "It was not possible to fulfill the request: Invalid VAT number";

	    // Ensure no existing customer with this simulated invalid VAT number
	    customerFunctions.deleteCustomer(webClient,invalidVAT);

	    // Navigate to the add customer page and fill the form
	    HtmlPage addCustomerPage = (HtmlPage) webClient.getPage("http://localhost:8080/VVS_webappdemo/addCustomer.html");
	    HtmlForm addForm = addCustomerPage.getForms().get(0);
	    addForm.getInputByName("vat").setValueAttribute(invalidVAT);
	    addForm.getInputByName("designation").setValueAttribute("Test User");
	    addForm.getInputByName("phone").setValueAttribute("123456789");
	    HtmlPage responsePage = (HtmlPage) addForm.getInputByValue("Get Customer").click();

	    // Use HtmlAnchor to navigate to the error handling page if not redirected automatically
	    if (!responsePage.asText().contains(expectedErrorMessage)) {
	        HtmlAnchor errorPageLink = responsePage.getAnchorByHref("AddCustomerPageController");
	        responsePage = (HtmlPage) errorPageLink.click();
	    }

	    // Check for the expected error message in the response page
	    assertTrue(responsePage.asText().contains(expectedErrorMessage), "Error message for invalid VAT should be displayed.");
	}
	
	@Test
	public void testEmptyVAT() throws Exception {
	    String emptyVAT = ""; // Empty VAT number
	    String expectedCustomerName = "Test User";
	    String phoneNumber = "923456789";

	    // Attempt to add a customer with an empty VAT number
	    customerFunctions.addCustomer(webClient,emptyVAT, expectedCustomerName, phoneNumber);

	    // Navigate to the list all customers page to verify if the customer was not added
	    HtmlPage allCustomersPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/GetAllCustomersPageController");

	    // Check if the page content does not contain the customer name and phone number
	    assertFalse(allCustomersPage.asText().contains(expectedCustomerName), "Customer with empty VAT should not be listed");
	    assertFalse(allCustomersPage.asText().contains(phoneNumber), "Phone number should not appear for customer with empty VAT");
	}
	
	@Test
	public void testVATCorrectSizeButInvalid() throws Exception {
	    String invalidButCorrectSizeVAT = "123456782"; // VAT that is structurally correct but logically incorrect
	    String expectedErrorMessage = "Invalid VAT number: " + invalidButCorrectSizeVAT;

	    // Ensure no existing customer with this VAT number
	    customerFunctions.deleteCustomer(webClient,invalidButCorrectSizeVAT);

	    // Navigate to the add customer page and fill the form
	    HtmlPage addCustomerPage = (HtmlPage) webClient.getPage("http://localhost:8080/VVS_webappdemo/addCustomer.html");
	    HtmlForm addForm = addCustomerPage.getForms().get(0);
	    addForm.getInputByName("vat").setValueAttribute(invalidButCorrectSizeVAT);
	    addForm.getInputByName("designation").setValueAttribute("Test User");
	    addForm.getInputByName("phone").setValueAttribute("123456789");
	    HtmlPage responsePage = (HtmlPage) addForm.getInputByValue("Get Customer").click();

	    // Use HtmlAnchor to navigate to the error handling page if not redirected automatically
	    if (!(responsePage.asText().contains(expectedErrorMessage))) {
	        HtmlAnchor errorPageLink = responsePage.getAnchorByHref("AddCustomerPageController");
	        responsePage = (HtmlPage) errorPageLink.click();
	    }

	    // Check for the expected error message in the response page
	    boolean errorDisplayed = responsePage.asText().contains(expectedErrorMessage);
	    assertTrue(errorDisplayed, "Error message for invalid VAT should be displayed.");
	}


	@Test
	public void testVATTooLong() throws Exception {
	    String longVAT = "1234567890123"; // VAT number longer than valid size
	    String expectedErrorMessage = "Invalid VAT number";

	    // Ensure no existing customer with this VAT number
	    customerFunctions.deleteCustomer(webClient,longVAT);

	    // Navigate to the add customer page and fill the form
	    HtmlPage addCustomerPage = (HtmlPage) webClient.getPage("http://localhost:8080/VVS_webappdemo/addCustomer.html");
	    HtmlForm addForm = addCustomerPage.getForms().get(0);
	    addForm.getInputByName("vat").setValueAttribute(longVAT);
	    addForm.getInputByName("designation").setValueAttribute("Test User");
	    addForm.getInputByName("phone").setValueAttribute("123456789");
	    HtmlPage responsePage = (HtmlPage) addForm.getInputByValue("Get Customer").click();

	    // Check for the expected error message in the response page using HtmlAnchor
	    if (!responsePage.asText().contains(expectedErrorMessage)) {
	        HtmlAnchor errorPageLink = responsePage.getAnchorByHref("AddCustomerPageController");
	        responsePage = (HtmlPage) errorPageLink.click();
	    }

	    assertTrue(responsePage.asText().contains(expectedErrorMessage), "Error message for long VAT should be displayed.");
	}

	@Test
	public void testVATTooShort() throws Exception {
	    String shortVAT = "12345"; // VAT number shorter than valid size
	    String expectedErrorMessage = "Invalid VAT number";

	    // Ensure no existing customer with this VAT number
	    customerFunctions.deleteCustomer(webClient,shortVAT);

	    // Navigate to the add customer page and fill the form
	    HtmlPage addCustomerPage = (HtmlPage) webClient.getPage("http://localhost:8080/VVS_webappdemo/addCustomer.html");
	    HtmlForm addForm = addCustomerPage.getForms().get(0);
	    addForm.getInputByName("vat").setValueAttribute(shortVAT);
	    addForm.getInputByName("designation").setValueAttribute("Test User");
	    addForm.getInputByName("phone").setValueAttribute("123456789");
	    HtmlPage responsePage = (HtmlPage) addForm.getInputByValue("Get Customer").click();

	    // Check for the expected error message in the response page using HtmlAnchor
	    if (!responsePage.asText().contains(expectedErrorMessage)) {
	        HtmlAnchor errorPageLink = responsePage.getAnchorByHref("AddCustomerPageController");
	        responsePage = (HtmlPage) errorPageLink.click();
	    }

	    assertTrue(responsePage.asText().contains(expectedErrorMessage), "Error message for short VAT should be displayed.");
	}
	
	@Test
	public void testVATWithMixedCharacters() throws Exception {
	    String mixedVAT = "I23456789"; // VAT number with invalid characters
	    String expectedErrorMessage = "Invalid VAT number: " + mixedVAT;

	    // Ensure no existing customer with this mixed character VAT number
	    customerFunctions.deleteCustomer(webClient,mixedVAT);

	    // Navigate to the add customer page and fill the form
	    HtmlPage addCustomerPage = (HtmlPage) webClient.getPage("http://localhost:8080/VVS_webappdemo/addCustomer.html");
	    HtmlForm addForm = addCustomerPage.getForms().get(0);
	    addForm.getInputByName("vat").setValueAttribute(mixedVAT);
	    addForm.getInputByName("designation").setValueAttribute("Test User");
	    addForm.getInputByName("phone").setValueAttribute("123456789");
	    HtmlPage responsePage = (HtmlPage) addForm.getInputByValue("Get Customer").click();

	    // Use HtmlAnchor to navigate to the error handling page if not redirected automatically
	    if (!(responsePage.asText().contains(expectedErrorMessage))) {
	        HtmlAnchor errorPageLink = responsePage.getAnchorByHref("AddCustomerPageController");
	        responsePage = (HtmlPage) errorPageLink.click();
	    }

	    // Check for the expected error message in the response page
	    boolean errorDisplayed = responsePage.asText().contains(expectedErrorMessage);
	    assertTrue(errorDisplayed, "Error message for VAT with mixed characters should be displayed.");
	}
    /* ---------------------------------------------------- DESIGNATION TESTS -----------------------------------------------------------------*/
    @Test
    public void testEmptyDesignationShouldNotAddCustomer() throws Exception {
        String vat = "506774287";  // Ensure this VAT is unique for the test
        customerFunctions.deleteCustomer(webClient,vat);  // Clean up before test

        HtmlPage addCustomerPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/addCustomer.html");
        HtmlForm form = addCustomerPage.getForms().get(0);
        form.getInputByName("vat").setValueAttribute(vat);
        form.getInputByName("designation").setValueAttribute("");  // Empty designation
        form.getInputByName("phone").setValueAttribute("911223344");
        HtmlPage responsePage = (HtmlPage) form.getInputByValue("Get Customer").click();

        // Assuming the page will show some error or remain the same after trying to add an invalid customer
        String expectedErrorMessage = "It was not possible to fulfill the request:";  // The exact message should match your application's output
        assertTrue(responsePage.asText().contains(expectedErrorMessage), "Expected error message should be displayed.");

        // Check if the customer was not added
        HtmlPage allCustomersPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/GetAllCustomersPageController");
        assertFalse(allCustomersPage.asText().contains(vat), "Customer with empty designation should not be listed.");
        //both assertions failed as expected because the app creates the customer plus shows no error messages
    }

	@Test
	public void testDesignationWithSpecialCharacters() throws Exception {
	    String vat = "197672337";
	    customerFunctions.deleteCustomer(webClient,vat);
	    String expectedErrorMessage = "Can't add customer with vat number " + vat + ".";
	    String invalidDesignation = "Valid@Name#";
	    
	    HtmlPage addCustomerPage = (HtmlPage) webClient.getPage("http://localhost:8080/VVS_webappdemo/addCustomer.html");
	    HtmlForm form = addCustomerPage.getForms().get(0);
	    form.getInputByName("vat").setValueAttribute(vat);
	    form.getInputByName("designation").setValueAttribute(invalidDesignation);
	    form.getInputByName("phone").setValueAttribute("962256789");
	    HtmlPage responsePage = (HtmlPage) form.getInputByValue("Get Customer").click();
	    
        assertTrue(responsePage.asText().contains(expectedErrorMessage), "Expected error message should be displayed.");

        // Check if the customer was not added
        HtmlPage allCustomersPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/GetAllCustomersPageController");
        assertFalse(allCustomersPage.asText().contains(vat), "Customer with Designation with special characters should not be listed.");
        //both assertions failed as expected because the app creates the customer plus shows no error messages
	
	}

	@Test
	public void testExcessivelyLongDesignation() throws Exception {
	    String vat = "509257810";
	    customerFunctions.deleteCustomer(webClient,vat);
	    String expectedErrorMessage = "Can't add customer with vat number " + vat + ".";
	    String longDesignation = "This is a very long designation that should exceed the typical limits set by the database or validation logic and so on bla bla bla kjbjdhgfcg usgdueygegy egdeufguf eiufhieuhf jehfgfieg ehfgefgfif iehiegifgiufiu iehfifuheifh jehiehfi jhhhhhhh xxxxx jwhjwhwjhwjwhjw    aknfjfherjfnfj        sfrfev vvvvvvvvvvvvvvvvvvvvv ttttttttttttt qqqqqqqqqqqqqqqqqqq"; // Excessively long designation
	    
	    HtmlPage addCustomerPage = (HtmlPage) webClient.getPage("http://localhost:8080/VVS_webappdemo/addCustomer.html");
	    HtmlForm form = addCustomerPage.getForms().get(0);
	    form.getInputByName("vat").setValueAttribute(vat);
	    form.getInputByName("designation").setValueAttribute(longDesignation);
	    form.getInputByName("phone").setValueAttribute("963356789");
	    HtmlPage responsePage = (HtmlPage) form.getInputByValue("Get Customer").click();

	    // Navigate using HtmlAnchor if error message is not displayed
	    if (!responsePage.asText().contains(expectedErrorMessage)) {
	        HtmlAnchor errorPageLink = responsePage.getAnchorByHref("AddCustomerPageController");
	        responsePage = (HtmlPage) errorPageLink.click();
	    }
	    
	    assertTrue(responsePage.asText().contains(vat), "Excessively long designation should not create a customer.");
	}

	/* ---------------------------------------------------- PHONE NUM TESTS -----------------------------------------------------------------*/
    /**
     * Test to verify that an empty phone number is not accepted.
     * @throws Exception on page loading or form interaction errors.
     */
    @Test
    public void testEmptyPhoneNumber() throws Exception {	
        String vat = "501965843";
        customerFunctions.deleteCustomer(webClient,vat);

        String emptyPhone = "";

        customerFunctions.addCustomer(webClient,vat,"Valid Name",emptyPhone);

	    // Navigate to the list all customers page to verify if the customer was not added
	    HtmlPage allCustomersPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/GetAllCustomersPageController");
        
	    assertFalse(allCustomersPage.asText().contains(vat), "vaT  should not appear for customer with empty PHONE NUM");
	    assertFalse(allCustomersPage.asText().contains("Valid Name"), "Customer with empty VAT should not be listed");
        
    	//although theres no response from server or error messages given by the app, the customer is indeed not created with 
    	//EmptyPhoneNumber
    
    }

    /**
     * Test to verify that phone numbers containing alphabetic characters are not accepted.
     * @throws Exception on page loading or form interaction errors.
     */
    @Test
    public void testPhoneNumberWithLetters() throws Exception {
        String vat = "502965843";
        customerFunctions.deleteCustomer(webClient,vat);

        String invalidPhone = "12345ABCD";
        customerFunctions.addCustomer(webClient,vat,"Valid Name",invalidPhone);

	    // Navigate to the list all customers page to verify if the customer was not added
	    HtmlPage allCustomersPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/GetAllCustomersPageController");
        
	    assertFalse(allCustomersPage.asText().contains(vat), "vaT  should not appear for customer with empty PHONE NUM");
	    assertFalse(allCustomersPage.asText().contains("Valid Name"), "Customer name should not be listed");
    	//although theres no response from server or error messages given by the app, the customer is indeed not created with 
    	//PhoneNumberWithLetters
    }

    /**
     * Test to verify that excessively long phone numbers are not accepted.
     * @throws Exception on page loading or form interaction errors.
     */
    @Test
    public void testExcessivelyLongPhoneNumber() throws Exception {
        String vat = "503965843";
        customerFunctions.deleteCustomer(webClient,vat);

        String longPhone = "12345678901234567890";
        customerFunctions.addCustomer(webClient,vat,"Valid Name",longPhone);

	    // Navigate to the list all customers page to verify if the customer was not added
	    HtmlPage allCustomersPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/GetAllCustomersPageController");
        
	    assertFalse(allCustomersPage.asText().contains(vat), "vaT  should not appear for customer with empty PHONE NUM");
	    assertFalse(allCustomersPage.asText().contains("Valid Name"), "Customer name should not be listed");
    	//although theres no response from server or error messages given by the app, the customer is indeed not created with 
    	//Phone Number Excessively Long
        
    }
}



