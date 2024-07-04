package functions_for_tests;

import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


/**
 * Utility class for auxiliary database functions used in tests.
 * This class provides methods to initialize the database for testing purposes.
 */
public class Database_Aux_Functions_For_Tests {
	/**
	 * Initializes the database by deleting all existing customers.
	 * This method navigates to the page listing all customers, extracts their VAT numbers,
	 * and deletes each customer using the provided customer functions.
	 * 
	 * @param webClient The web client used to interact with the web application.
	 * @param customerFunctions The customer functions used to extract VAT numbers and delete customers.
	 * @throws Exception if there is an error during page navigation or customer deletion.
	 */
    public void initDatabase(WebClient webClient,Customer_Aux_Functions_For_Tests customerFunctions) throws Exception {
        HtmlPage listAllCustomersPage = webClient.getPage("http://localhost:8080/VVS_webappdemo/GetAllCustomersPageController");
        List<String> allVatNumbers = customerFunctions.extractAllVatNumbers(listAllCustomersPage);
        for (String vat : allVatNumbers) {
        	customerFunctions.deleteCustomer(webClient, vat);
        }
    }
}
