package htmlUnit_tests;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import functions_for_tests.Customer_Aux_Functions_For_Tests;
import functions_for_tests.Database_Aux_Functions_For_Tests;
import functions_for_tests.Sales_Aux_Functions_For_Tests;

/**
 * 2(c)
 * This class contains tests related to the creation and verification of sales of question 2 (c).
 */
public class NewSale_IsOpen_Tests {

	private static WebClient webClient;
	private static Sales_Aux_Functions_For_Tests salesFunctions;
	private static Customer_Aux_Functions_For_Tests customerFunctions;
	private static Database_Aux_Functions_For_Tests dbFunctions;
	
	/**
	 * Sets up the WebClient and auxiliary functions before all tests are run.
	 * Disables CSS and JavaScript for the WebClient to focus on HTML content.
	 * Initializes the database with necessary data.
	 * @throws Exception if there is an error initializing the WebClient or the database.
	 */
	@BeforeAll
	public static void setupClass() throws Exception {
		webClient = new WebClient();
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		salesFunctions = new Sales_Aux_Functions_For_Tests();
		customerFunctions = new Customer_Aux_Functions_For_Tests();
		dbFunctions = new Database_Aux_Functions_For_Tests();
		dbFunctions.initDatabase(webClient,customerFunctions);
	}
	/* ------------------------------------------------ TESTS -----------------------------------------------------------*/

	/**
	 * Tests that a newly created sale is listed as open.
	 * This method ensures that after creating a new sale for a customer,
	 * the sale is displayed as 'Open' in the list of open sales.
	 * @throws Exception If there is an error accessing the web pages or interacting with forms.
	 */
	@Test
	public void testNewSaleIsOpen() throws Exception {
		String vatNumber = "123456789";
		String customerName = "Roger Waters";
		customerFunctions.addCustomer(webClient,vatNumber, customerName, "123123123");
		HtmlPage newSalePage = salesFunctions.createSale(webClient,vatNumber);

		// Check the sale information on the sale detail page
		HtmlTable salesTable = (HtmlTable) newSalePage.getFirstByXPath("//table[@class='w3-table w3-bordered']");
		boolean saleOpen = false;
		for (HtmlTableRow row : salesTable.getRows()) {
			if (row.asText().contains(vatNumber) && row.asText().contains("O")) {
				saleOpen = true;
				break;
			}
		}
		assertTrue(saleOpen, "The new sale should be listed as open");
	}

	/**
	 * Tests the application's behavior when creating a sale for a customer with an empty designation.
	 * The test expects the application to reject such a sale, but the current implementation does not.
	 * @throws Exception If there is an error accessing the web pages or interacting with forms.
	 */
	@Test
	public void testSaleOnCustomerWithEmptyDesignation() throws Exception {
		String vat = "501964843";
		customerFunctions.addCustomer(webClient,vat, "", "964512342"); // Add customer with empty designation
		salesFunctions.assertSaleIsOpen(webClient,vat); 
		//this test should have been false, not true. indicates the app does not check if the customer is valid or not 
		// before adding the sale
	}

	/**
	 * Tests the application's ability to handle sales creation for a customer whose designation includes special characters.
	 * The application should ideally reject such a sale but currently does not handle it appropriately.
	 * @throws Exception If there is an error accessing the web pages or interacting with forms.
	 */
	@Test
	public void testSaleOnCustomerWithSpecialCharactersInDesignation() throws Exception {
		String vat = "509257810";
		customerFunctions.addCustomer(webClient,vat, "Special@Name#", "917654321"); // Add customer with special characters in designation
		salesFunctions.assertSaleIsOpen(webClient,vat);
	}

	/**
	 * Tests the application's behavior when attempting to create a sale without providing a VAT number.
	 * The test checks whether the application handles this case gracefully by showing an error message.
	 * Currently, the application returns a blank page instead.
	 * @throws Exception If there is an error accessing the web pages or interacting with forms.
	 */
	@Test
	public void testSaleCreationWithoutCustomer() throws Exception { // Error: program not prepared to handle this, returns a blank page
		// Create a sale without associating it with any customer
		HtmlPage newSalePage = webClient.getPage("http://localhost:8080/VVS_webappdemo/addSale.html");
		HtmlForm saleForm = newSalePage.getForms().get(0);
		// Do not set customer VAT number
		HtmlPage resultPage = saleForm.getInputByValue("Add Sale").click();

		// Ensure the sale creation fails gracefully
		assertNotNull(resultPage.querySelector(".error"), "Error message should be displayed for sale creation without customer.");
	}


	/**
	 * Tests the application's behavior when attempting to create a sale for a non-existent customer with a valid VAT number.
	 * The test ensures that the application does not allow a sale to be created for a customer that is not in the system,
	 * and verifies that the customer has no associated sales.
	 * @throws Exception If there is an error accessing the web pages or interacting with forms.
	 */
	@Test
	public void testSaleCreationForNonExistentCustomer() throws Exception {
		String vatNumber = "503183504"; // Valid VAT number for non-existent customer

		// Attempt to create a sale for a non-existent customer
		HtmlPage resultPage = salesFunctions.createSaleForCustomer(webClient, vatNumber);

		// Verify that an error message is displayed
		assertNotNull(resultPage.querySelector(".error"), "Error message should be displayed for sale creation for a non-existent customer.");

		// Verify that the non-existent customer has no sales
		HtmlPage salesPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/getSales.html");
		HtmlForm form = salesPage.getForms().get(0);
		form.getInputByName("customerVat").setValueAttribute(vatNumber);
		HtmlPage salesResultPage = (HtmlPage) form.getInputByValue("Get Sales").click();

		HtmlTable salesTable = (HtmlTable) salesResultPage.getFirstByXPath("//table[@class='w3-table w3-bordered']");
		boolean saleFound = false;
		if (salesTable != null) {
			for (HtmlTableRow row : salesTable.getRows()) {
				if (row.asText().contains(vatNumber)) {
					saleFound = true;
					break;
				}
			}
		}
		assertFalse(saleFound, "No sales should be listed for a non-existent customer");
	}

}
