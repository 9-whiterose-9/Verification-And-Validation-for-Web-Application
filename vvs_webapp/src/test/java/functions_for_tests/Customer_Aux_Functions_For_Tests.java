package functions_for_tests;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

public class Customer_Aux_Functions_For_Tests {
	
    /**
     * Ensures that the customer does not exist before the test, then creates the customer.
     * @param webClient The web client used to interact with the web application.
     * @param vatNumber VAT number of the customer.
     * @param designation Name or designation of the customer.
     * @param phoneNumber Phone number of the customer.
     * @throws Exception on page loading or form interaction errors.
     */
	public void setupCustomer(WebClient webClient, String vatNumber, String designation, String phoneNumber) throws Exception {
        deleteCustomer(webClient, vatNumber);  // Ensure customer does not exist
        addCustomer(webClient, vatNumber, designation, phoneNumber);  // Then add the customer
    }
	
	public List<String> extractAllVatNumbers(HtmlPage page) throws Exception {
        List<String> vatNumbers = new ArrayList<>();
        HtmlTable vatTable = (HtmlTable) page.getFirstByXPath("//table[@id='clients']");
        if (vatTable != null) {
            for (int i = 1; i < vatTable.getRowCount(); i++) { // Skip the first row assuming it's the header
                HtmlTableRow row = vatTable.getRow(i);
                String vatNumber = row.getCell(2).asText(); // VAT is in the third column (index 2)
                if (vatNumber.matches("\\d+")) { // Ensure the VAT number consists only of digits
                    vatNumbers.add(vatNumber);
                }
            }
        }
        return vatNumbers;
    }
    
    /**
     * Deletes a customer by VAT number if they exist.
     * @param webClient The web client used to interact with the web application.
     * @param vatNumber VAT number of the customer to delete.
     * @throws Exception on page loading or form interaction errors.
     */
    public void deleteCustomer(WebClient webClient, String vatNumber) throws Exception {
        if (customerExists(webClient,vatNumber)) {
            HtmlPage removePage = webClient.getPage("http://localhost:8080/VVS_webappdemo/RemoveCustomerPageController");
            HtmlForm removeForm = removePage.getFirstByXPath("//form[@action='RemoveCustomerPageController']");
            HtmlInput vatInput = removeForm.getInputByName("vat");
            vatInput.setValueAttribute(vatNumber);
            HtmlInput submitButton = removeForm.getInputByValue("Remove");
            submitButton.click();
        }
    }

    /**
     * Checks if a customer exists by VAT number.
     * @param webClient The web client used to interact with the web application.
     * @param vatNumber VAT number of the customer to check.
     * @return true if the customer exists, false otherwise.
     * @throws Exception on page loading errors.
     */
    public boolean customerExists(WebClient webClient,String vatNumber) throws Exception {
        HtmlPage page = webClient.getPage("http://localhost:8080/VVS_webappdemo/GetAllCustomersPageController");
        return page.asText().contains(vatNumber);
    }

    /**
     * Adds a new customer with the provided details.
     * @param webClient The web client used to interact with the web application.
     * @param vatNumber VAT number of the customer.
     * @param designation Name or designation of the customer.
     * @param phoneNumber Phone number of the customer.
     * @throws Exception on page loading or form interaction errors.
     */
    public void addCustomer(WebClient webClient, String vatNumber, String designation, String phoneNumber) throws Exception {
        HtmlPage addCustomerPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/addCustomer.html");
        HtmlForm addForm = addCustomerPage.getForms().get(0);
        addForm.getInputByName("vat").setValueAttribute(vatNumber);
        addForm.getInputByName("designation").setValueAttribute(designation);
        addForm.getInputByName("phone").setValueAttribute(phoneNumber);
        addForm.getInputByValue("Get Customer").click();
    }
    
    /**
     * Refreshes and returns the details page of a customer by their VAT number.
     * @param webClient The web client used to interact with the web application.
     * @param vatNumber VAT number of the customer.
     * @return HtmlPage containing the customer's details.
     * @throws Exception on page loading errors.
     */
    public HtmlPage refreshCustomerDetails(WebClient webClient, String vatNumber) throws Exception {
        URL url = new URL("http://localhost:8080/VVS_webappdemo/GetCustomerPageController?vat=" + vatNumber + "&submit=Get+Customer");
        WebRequest request = new WebRequest(url, com.gargoylesoftware.htmlunit.HttpMethod.GET);
        return webClient.getPage(request);
    }
    
}
