package functions_for_tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
/**
 * Utility class providing functions for sales-related tests.
 * This class includes methods to create sales, check the status of sales, 
 * and extract information from sales-related web pages using HtmlUnit.
 */
public class Sales_Aux_Functions_For_Tests {
    /**
     * Extracts the ID of the latest sale for a given customer.
     * This method navigates to the sales page and submits the VAT to get all sales for the customer,
     * then retrieves the latest sale ID from the sales table.
     * 
     * @param webClient The web client used to interact with the web application.
     * @param customerVat The VAT number of the customer.
     * @return The ID of the latest sale.
     * @throws Exception if there is an error during page navigation or form submission.
     */
	public String extractLatestSaleId(WebClient webClient,String customerVat) throws Exception {
        // Navigate to the sales page and submit the VAT to get all sales for the customer
        HtmlPage salesPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/getSales.html");
        HtmlForm salesForm = salesPage.getForms().get(0);
        salesForm.getInputByName("customerVat").setValueAttribute(customerVat);
        HtmlPage resultPage = (HtmlPage) salesForm.getInputByValue("Get Sales").click();

        // Extract the latest sale ID from the sales table
        HtmlTable salesTable = (HtmlTable) resultPage.getFirstByXPath("//table[@class='w3-table w3-bordered']");
        int lastRowIndex = salesTable.getRowCount() - 1;  // Assuming the last row has the latest sale
        HtmlTableRow lastRow = salesTable.getRow(lastRowIndex);
        return lastRow.getCell(0).asText();  // Assuming the first column contains the sale ID
    }

    
    /**
     * Creates a sale for a given customer.
     * This method simulates the creation of a sale by interacting with the web form on the application.
     * @param webClient The web client used to interact with the web application.
     * @param vatNumber The VAT number of the customer for whom the sale is being created.
     * @return HtmlPage The page that confirms the sale has been created or shows the newly created sale.
     * @throws Exception on page loading or form interaction errors.
     */
	public HtmlPage createSale(WebClient webClient,String vatNumber) throws Exception {
        HtmlPage saleCreationPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/addSale.html");
        HtmlForm saleForm = saleCreationPage.getForms().get(0);
        HtmlInput vatInput = saleForm.getInputByName("customerVat");
        vatInput.setValueAttribute(vatNumber);
        HtmlInput submitButton = saleForm.getInputByValue("Add Sale");
        return submitButton.click();
    }
    /**
     * Creates a sale for a given customer and verifies that the sale is listed as open.
     * 
     * @param webClient The web client used to interact with the web application.
     * @param vatNumber The VAT number of the customer for whom the sale is being created.
     * @return HtmlPage The page showing the result of the sale creation.
     * @throws Exception if there is an error during page navigation or form submission.
     */
	public HtmlPage createSaleForCustomer(WebClient webClient,String vatNumber) throws Exception {
        HtmlPage newSalePage = webClient.getPage("http://localhost:8080/VVS_webappdemo/addSale.html");
        HtmlForm saleForm = newSalePage.getForms().get(0);
        saleForm.getInputByName("customerVat").setValueAttribute(vatNumber);
        HtmlPage resultPage = saleForm.getInputByValue("Add Sale").click();

        // Check if the new sale is listed as open
        HtmlTable salesTable = (HtmlTable) resultPage.getFirstByXPath("//table[@class='w3-table w3-bordered']");
        boolean saleOpen = false;
        for (HtmlTableRow row : salesTable.getRows()) {
            if (row.asText().contains(vatNumber) && row.asText().contains("O")) {
                saleOpen = true;
                break;
            }
        }
        assertTrue(saleOpen, "The new sale should be listed as open");
        return resultPage;
    }
	
    /**
     * Asserts that the latest sale for a given customer is open.
     * This method navigates to the sales retrieval page and verifies if the newest sale is open.
     * 
     * @param webClient The web client used to interact with the web application.
     * @param vat The VAT number of the customer.
     * @throws Exception if there is an error during page navigation or form submission.
     */
	public void assertSaleIsOpen(WebClient webClient,String vat) throws Exception {
        // Navigate to the sales retrieval page
        HtmlPage salesPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/getSales.html");
        HtmlForm form = salesPage.getForms().get(0);
        HtmlInput vatInput = form.getInputByName("customerVat");
        vatInput.setValueAttribute(vat);
        HtmlPage salesResultPage = (HtmlPage) form.getInputByValue("Get Sales").click();

        // Verify if the newest sale is open
        HtmlTable salesTable = (HtmlTable) salesResultPage.getFirstByXPath("//table[@class='w3-table w3-bordered']");
        if (salesTable != null && salesTable.getRowCount() >= 1) {
            int lastRowIndex = salesTable.getRowCount() - 1;
            String status = salesTable.getCellAt(lastRowIndex, 3).asText(); //  status is the fourth column
            String date = salesTable.getCellAt(lastRowIndex, 1).asText(); //  date is the second column

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sdf.format(new Date()); // Format today's date in "yyyy-MM-dd" format

            assertTrue(status.equals("O"),"Status should be 'O' for open." );
            assertTrue( date.equals(currentDate),"Date of the newest sale should be today's date.");
        } else {
            fail("No sales table found or no sales recorded.");
        }
    }
    /**
     * Retrieves the ID of the latest sale for a given customer.
     * 
     * @param webClient The web client used to interact with the web application.
     * @param customerVat The VAT number of the customer.
     * @return The ID of the latest sale or null if no sales found.
     * @throws Exception if there is an error during page navigation or form submission.
     */
	public String getLatestSaleId(WebClient webClient,String customerVat) throws Exception {
        HtmlPage salesPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/getSales.html");
        HtmlForm form = salesPage.getForms().get(0);
        form.getInputByName("customerVat").setValueAttribute(customerVat);
        HtmlPage resultPage = (HtmlPage) form.getInputByValue("Get Sales").click();

        HtmlTable salesTable = (HtmlTable) resultPage.getFirstByXPath("//table[@class='w3-table w3-bordered']");
        if (salesTable != null && salesTable.getRowCount() > 1) {  // Check if there's at least one sale
            HtmlTableRow lastRow = salesTable.getRow(salesTable.getRowCount() - 1);
            return lastRow.getCell(0).asText();  // Assuming the first column is the ID
        }
        return null;
    }
}
