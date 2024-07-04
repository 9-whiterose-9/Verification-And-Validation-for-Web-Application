package functions_for_tests;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

public class Addresses_Aux_Functions_For_Tests {
    /**
     * Extracts the address ID for a given customer by VAT number.
     * This method navigates to the page listing address information and extracts the first available address ID.
     * @param webClient The web client used to interact with the web application.
     * @param vatNumber VAT number of the customer whose address ID is to be retrieved.
     * @return The ID of the address as a string.
     * @throws Exception on page loading or form interaction errors.
     */
	public String extractAddressId(WebClient webClient,String vatNumber) throws Exception {
        // Navigate to the sale delivery page which shows addresses
        HtmlPage deliveryPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/saleDeliveryVat.html");
        HtmlForm form = deliveryPage.getForms().get(0);
        form.getInputByName("vat").setValueAttribute(vatNumber);
        HtmlPage addressListPage = form.getInputByValue("Get Customer").click();

        // Extract the address ID from the table
        HtmlTable table = (HtmlTable) addressListPage.getFirstByXPath("//table[@class='w3-table w3-bordered']");
        if (table != null && table.getRowCount() > 1) { // Ensure there is at least one address
            HtmlTableRow row = table.getRow(1); // Get the first address row (excluding header)
            return row.getCell(0).asText(); // Assuming the first column is the address ID
        }
        throw new Exception("No address found for the customer with VAT: " + vatNumber);
    }
    /**
     * Adds an address to the existing customer.
     * @param webClient The web client used to interact with the web application.
     * @param address The street address.
     * @param door Door number.
     * @param postalCode Postal code.
     * @param locality City or locality.
     * @throws Exception on page loading or form interaction errors.
     */
	public void addAddress(WebClient webClient,String vatNumber,String address, String door, String postalCode, String locality) throws Exception {
        HtmlPage addAddressPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/addAddressToCustomer.html");
        HtmlForm addressForm = addAddressPage.getForms().get(0);
        addressForm.getInputByName("vat").setValueAttribute(vatNumber);
        addressForm.getInputByName("address").setValueAttribute(address);
        addressForm.getInputByName("door").setValueAttribute(door);
        addressForm.getInputByName("postalCode").setValueAttribute(postalCode);
        addressForm.getInputByName("locality").setValueAttribute(locality);
        addressForm.getInputByValue("Insert").click();
    }
    
    /**
     * Counts the number of address entries on a customer's detail page.
     * @param customerPage The page containing the customer's details.
     * @return The number of addresses listed on the page.
     */
	public int countAddresses(HtmlPage customerPage) {
        HtmlTable addressTable = (HtmlTable) customerPage.getFirstByXPath("//table[@class='w3-table w3-bordered']");
        return addressTable != null ? addressTable.getRowCount() - 1 : 0; // Subtract header row if present
    }
    
}
