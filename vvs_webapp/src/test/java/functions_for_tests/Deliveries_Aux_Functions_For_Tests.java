package functions_for_tests;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

public class Deliveries_Aux_Functions_For_Tests {
	
    /**
     * Inserts a new delivery for a given sale and address.
     * @param webClient The WebClient instance to use for the request.
     * @param vatNumber The VAT number of the customer.
     * @param saleId The ID of the sale.
     * @param addressId The ID of the address.
     * @throws Exception on page loading or form interaction errors.
     */
    public void insertDelivery(WebClient webClient, String vatNumber, String saleId, String addressId) throws Exception {
        // Insert a new delivery for the sale
        HtmlPage addDeliveryPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/AddSaleDeliveryPageController?vat=" + vatNumber);
        HtmlForm deliveryForm = addDeliveryPage.getForms().get(0);
        deliveryForm.getInputByName("addr_id").setValueAttribute(addressId);
        deliveryForm.getInputByName("sale_id").setValueAttribute(saleId);
        deliveryForm.getInputByValue("Insert").click();
    }

    /**
     * Checks if the delivery is correctly shown in the table and matches the expected details.
     * @param webClient The WebClient instance to use for the request.
     * @param vatNumber The VAT number of the customer.
     * @param expectedSaleId The expected sale ID.
     * @param expectedAddressId The expected address ID.
     * @return true if the delivery details match, false otherwise.
     * @throws Exception on page loading or form interaction errors.
     */
    public boolean checkDeliveryInTable(WebClient webClient, String vatNumber, String expectedSaleId, String expectedAddressId) throws Exception {
        // Navigate to the delivery check page and submit the form with the VAT number
        HtmlPage showDeliveryPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/showDelivery.html");
        HtmlForm showDeliveryForm = showDeliveryPage.getForms().get(0);
        showDeliveryForm.getInputByName("vat").setValueAttribute(vatNumber);
        HtmlPage deliveryResultPage = (HtmlPage) showDeliveryForm.getInputByValue("Get Customer").click();

        // Get the table on the resulting page and search for the correct sale and address IDs
        HtmlTable deliveryTable = (HtmlTable) deliveryResultPage.getFirstByXPath("//table[@class='w3-table w3-bordered']");
        if (deliveryTable != null) {
            for (final HtmlTableRow row : deliveryTable.getRows()) {
                if (row.getCell(1).asText().equals(expectedSaleId) && row.getCell(2).asText().equals(expectedAddressId)) {
                    return true; // The correct row is found
                }
            }
        }
        return false; // No matching row found
    }

}
