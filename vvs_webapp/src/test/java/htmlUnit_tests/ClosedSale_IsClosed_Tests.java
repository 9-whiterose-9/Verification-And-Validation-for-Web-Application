package htmlUnit_tests;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import functions_for_tests.Customer_Aux_Functions_For_Tests;
import functions_for_tests.Database_Aux_Functions_For_Tests;
import functions_for_tests.Sales_Aux_Functions_For_Tests;

/**
 *  2 (d) 
 *  This class contains tests related to the closure and verification of sales. 
 */
public class ClosedSale_IsClosed_Tests {
	
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
     * Tests that a sale is correctly closed.
     * @throws Exception If there is an error accessing the web pages or interacting with forms.
     */
    @Test
    public void testSaleClosure() throws Exception {
        String vatNumber = "123456789";
        String customerName = "Damon Salvatore";
        String phoneNumber = "919225454";

        customerFunctions.setupCustomer(webClient,vatNumber, customerName, phoneNumber);

        String saleId = salesFunctions.getLatestSaleId(webClient,vatNumber); // Get the ID of the latest sale to close it
        if (saleId != null) {
            HtmlPage closeSalePage = webClient.getPage("http://localhost:8080/VVS_webappdemo/UpdateSaleStatusPageControler");
            HtmlForm form = closeSalePage.getForms().get(0);
            form.getInputByName("id").setValueAttribute(saleId); // Correct field name based on form requirement
            HtmlInput closeButton = form.getInputByValue("Close Sale");
            closeButton.click();

            HtmlPage salesPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/getSales.html");
            form = salesPage.getForms().get(0);
            form.getInputByName("customerVat").setValueAttribute(vatNumber);
            HtmlPage resultPage = (HtmlPage) form.getInputByValue("Get Sales").click();

            HtmlTable salesTable = (HtmlTable) resultPage.getFirstByXPath("//table[@class='w3-table w3-bordered']");
            boolean saleClosed = false;
            for (HtmlTableRow row : salesTable.getRows()) {
                if (row.getCell(0).asText().equals(saleId) && row.getCell(3).asText().equals("C")) {
                    saleClosed = true;
                    break;
                }
            }
            assertTrue(saleClosed, "The sale should be listed as closed.");
        } else {
            assertTrue(false, "No sale found to close.");
        }
    }
    
    /**
     * Tests the application's behavior when attempting to close an already closed sale.
     * The test ensures that the application does not close a sale multiple times.
     * @throws Exception If there is an error accessing the web pages or interacting with forms.
     */
    @Test
    public void testClosingAlreadyClosedSale() throws Exception {
        String vatNumber = "123456789";
        String customerName = "Damon Salvatore";
        String phoneNumber = "919225454";

        customerFunctions.setupCustomer(webClient,vatNumber, customerName, phoneNumber);

        String saleId = salesFunctions.getLatestSaleId(webClient,vatNumber);
        if (saleId != null) {
            HtmlPage closeSalePage = webClient.getPage("http://localhost:8080/VVS_webappdemo/UpdateSaleStatusPageControler");
            HtmlForm form = closeSalePage.getForms().get(0);
            form.getInputByName("id").setValueAttribute(saleId);
            HtmlInput closeButton = form.getInputByValue("Close Sale");
            closeButton.click();  // First closure attempt

            // Second closure attempt on the same sale
            closeButton.click();
            HtmlPage salesPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/getSales.html");
            form = salesPage.getForms().get(0);
            form.getInputByName("customerVat").setValueAttribute(vatNumber);
            HtmlPage resultPage = (HtmlPage) form.getInputByValue("Get Sales").click();
            HtmlTable salesTable = (HtmlTable) resultPage.getFirstByXPath("//table[@class='w3-table w3-bordered']");
            int closedCount = 0;
            for (HtmlTableRow row : salesTable.getRows()) {
                if (row.getCell(0).asText().equals(saleId) && row.getCell(3).asText().equals("C")) {
                    closedCount++;
                }
            }
            assertTrue(closedCount == 1, "A sale should only be closed once, and not multiple times.");
        } else {
            assertTrue(false, "No sale found to close.");
        }
    }
    
    /**
     * Tests that a closed sale remains closed after subsequent operations.
     * @throws Exception If there is an error accessing the web pages or interacting with forms.
     */
    @Test
    public void testClosedSaleRemainsClosed() throws Exception {
        String vatNumber = "123456789";
        String customerName = "Damon Salvatore";
        String phoneNumber = "919225454";

        customerFunctions.setupCustomer(webClient, vatNumber, customerName, phoneNumber);
        salesFunctions.createSaleForCustomer(webClient, vatNumber);

        String saleId = salesFunctions.getLatestSaleId(webClient, vatNumber);
        if (saleId != null) {
            HtmlPage closeSalePage = webClient.getPage("http://localhost:8080/VVS_webappdemo/UpdateSaleStatusPageControler");
            HtmlForm form = closeSalePage.getForms().get(0);
            form.getInputByName("id").setValueAttribute(saleId);
            HtmlInput closeButton = form.getInputByValue("Close Sale");
            closeButton.click();

            // Recheck the sale status
            HtmlPage salesPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/getSales.html");
            form = salesPage.getForms().get(0);
            form.getInputByName("customerVat").setValueAttribute(vatNumber);
            HtmlPage resultPage = (HtmlPage) form.getInputByValue("Get Sales").click();

            HtmlTable salesTable = (HtmlTable) resultPage.getFirstByXPath("//table[@class='w3-table w3-bordered']");
            boolean saleClosed = false;
            for (HtmlTableRow row : salesTable.getRows()) {
                if (row.getCell(0).asText().equals(saleId) && row.getCell(3).asText().equals("C")) {
                    saleClosed = true;
                    break;
                }
            }
            assertTrue(saleClosed, "The sale should remain listed as closed.");
        } else {
            assertTrue(false, "No sale found to close.");
        }
    }

    /**
     * Tests the application's behavior when attempting to close a sale for a non-existent customer.
     * The test ensures that the application does not close a sale for a customer that is not in the system.
     * @throws Exception If there is an error accessing the web pages or interacting with forms.
     */
    @Test
    public void testClosingSaleForNonExistentCustomer() throws Exception {
        String vatNumber = "503183504"; // VAT number for a non-existent customer

        HtmlPage salesPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/getSales.html");
        HtmlForm form = salesPage.getForms().get(0);
        form.getInputByName("customerVat").setValueAttribute(vatNumber);
        HtmlPage resultPage = (HtmlPage) form.getInputByValue("Get Sales").click();

        HtmlTable salesTable = (HtmlTable) resultPage.getFirstByXPath("//table[@class='w3-table w3-bordered']");
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

