package vvs_webapp;

import static org.junit.Assert.*;
import org.junit.*;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import java.net.MalformedURLException;

import java.io.*;
import java.util.*;

public class TestIndex {
	
	private static final String APPLICATION_URL = "http://localhost:8080/VVS_webappdemo/";
	private static final int APPLICATION_NUMBER_USE_CASES = 11;

	private static WebClient webClient;
	private static HtmlPage page;
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		webClient = new WebClient(BrowserVersion.getDefault());
		
		// possible configurations needed to prevent JUnit tests to fail for complex HTML pages
        webClient.setJavaScriptTimeout(15000);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        
		page = webClient.getPage(APPLICATION_URL);
		assertEquals(200, page.getWebResponse().getStatusCode()); // OK status
	}
	
	@AfterClass
	public static void takeDownClass() {
		webClient.close();
	}
	
	@Test
	public void indexTest() throws Exception {
        assertEquals("WebAppDemo Menu", page.getTitleText());

        final String pageAsXml = page.asXml();
        assertTrue(pageAsXml.contains("<div class=\"w3-container w3-blue-grey w3-center w3-allerta\" id=\"body\">"));

        final String pageAsText = page.asText();
        assertTrue(pageAsText.contains("WebAppDemo Menu"));
	}
	
	@Test
	public void numberOfOptionsTest() throws Exception { 
         List<DomElement> inputs = page.getElementsById("botao2");  // get list of case uses
         assertTrue(inputs.size()==APPLICATION_NUMBER_USE_CASES);
	}
	
	@Test
	public void addCustomerClick() throws Exception {
		
		// get the button for inserting a new customer
		DomElement addCustomerButton = page.getElementsById("botao2").get(0);
		assertEquals("Insert new Customer", addCustomerButton.asText());
		// click on it
		HtmlPage addCustomerPage = addCustomerButton.click();
		
		// check if title is the one expected
		assertEquals("Enter Name", addCustomerPage.getTitleText());
		// original page is unchanged
        assertEquals("WebAppDemo Menu", page.getTitleText());
		
	}
	
	// alternatively, using Href
	@Test
	public void addCustomerHref() throws Exception {
		
		// get the link for inserting a new customer
		HtmlAnchor addCustomerLink = page.getAnchorByHref("addCustomer.html");
		// click on it
		HtmlPage nextPage = (HtmlPage) addCustomerLink.openLinkInNewWindow();
//		HtmlPage nextPage = addCustomerLink.click();
		
		// check if title is the one expected
		assertEquals("Enter Name", nextPage.getTitleText());
		// original page is unchanged
        assertEquals("WebAppDemo Menu", page.getTitleText());
		
	}

	/**
	 * Here we test two operations (insert & remove) in order to leave the database
	 * in the original state
	 * 
	 * @throws IOException
	 */
	@Test
	public void insertAndRemoveClientTest() throws IOException {
        final String VAT = "503183504";
        final String DESIGNATION = "FCUL";
        final String PHONE = "217500000";
		
		// get a specific link
		HtmlAnchor addCustomerLink = page.getAnchorByHref("addCustomer.html");
		// click on it
		HtmlPage nextPage = (HtmlPage) addCustomerLink.openLinkInNewWindow();
		// check if title is the one expected
		assertEquals("Enter Name", nextPage.getTitleText());
		
		// get the page first form:
		HtmlForm addCustomerForm = nextPage.getForms().get(0);
		
		// place data at form
		HtmlInput vatInput = addCustomerForm.getInputByName("vat");
		vatInput.setValueAttribute(VAT);
		HtmlInput designationInput = addCustomerForm.getInputByName("designation");
		designationInput.setValueAttribute(DESIGNATION);
		HtmlInput phoneInput = addCustomerForm.getInputByName("phone");
		phoneInput.setValueAttribute(PHONE);
		// submit form
		HtmlInput submit = addCustomerForm.getInputByName("submit");
		
		// check if report page includes the proper values
		HtmlPage reportPage = submit.click();
		
		// ----- alternative, use a POST request:
		// (ref: https://stackoverflow.com/questions/21628614/)
//		java.net.URL requestURL = new java.net.URL(APPLICATION_URL+"AddCustomerPageController");
//		WebRequest request = new WebRequest(requestURL, HttpMethod.POST);
		
		// Option 1: Set request body
//		String formData = String.format("vat=%s&designation=%s&phone=%s", VAT, DESIGNATION, PHONE);
//		request.setRequestBody(formData);
		
		// Option 2: Set request parameters
//		List<NameValuePair> requestParameters = new ArrayList<NameValuePair>();
//		requestParameters.add(new NameValuePair("vat", VAT));
//		requestParameters.add(new NameValuePair("designation", DESIGNATION));
//		requestParameters.add(new NameValuePair("phone", PHONE));
//		request.setRequestParameters(requestParameters);
		
//		HtmlPage reportPage = webClient.getPage(request);
		
		// ----- end alternative

		String textReportPage = reportPage.asText();
//		System.out.println(textReportPage);  // check if it is the report page
		assertEquals("Customer Info", reportPage.getTitleText());
		assertTrue(textReportPage.contains(DESIGNATION));
		assertTrue(textReportPage.contains(PHONE));
		
		// at index, goto Remove case use and remove the previous client
		HtmlAnchor removeCustomerLink = page.getAnchorByHref("RemoveCustomerPageController");
		nextPage = (HtmlPage) removeCustomerLink.openLinkInNewWindow();
		assertTrue(nextPage.asText().contains(VAT));
		
		HtmlForm removeCustomerForm = nextPage.getForms().get(0);
		vatInput = removeCustomerForm.getInputByName("vat");
		vatInput.setValueAttribute(VAT);
		submit = removeCustomerForm.getInputByName("submit");
		reportPage = submit.click();
		assertFalse(reportPage.asText().contains(VAT));
		
		// now check that the new client was erased
		HtmlAnchor getCustomersLink = page.getAnchorByHref("GetAllCustomersPageController");
		nextPage = (HtmlPage) getCustomersLink.openLinkInNewWindow();
		assertFalse(nextPage.asText().contains(VAT));
	}

	// not testing, just to show how to access tables inside the HTML
	@Test
	public void tablesTest() throws MalformedURLException {
		HtmlAnchor getCustomersLink = page.getAnchorByHref("GetAllCustomersPageController");
		HtmlPage nextPage = (HtmlPage) getCustomersLink.openLinkInNewWindow();
		
		final HtmlTable table = nextPage.getHtmlElementById("clients");
		System.out.println("---------------------------------");
		for (final HtmlTableRow row : table.getRows()) {
		    System.out.println("Found row");
		    for (final HtmlTableCell cell : row.getCells()) {
		       System.out.println("   Found cell: " + cell.asText());
		    }
		}
		System.out.println("---------------------------------");
	}
	
	// Eg of testing a GET request.
	// For a POST request cf. stackoverflow.com/questions/30687614
	@Test
	public void parametersGetTest() throws IOException {
		
		// Build a GET request
		java.net.URL url = new java.net.URL(APPLICATION_URL+"GetCustomerPageController");
		WebRequest request = new WebRequest(url, HttpMethod.GET);

		// Set the request parameters
		List<NameValuePair> requestParameters = new ArrayList<NameValuePair>();
		requestParameters.add(new NameValuePair("vat", "197672337"));
		request.setRequestParameters(requestParameters);
		
		HtmlPage reportPage = webClient.getPage(request);
		assertEquals(HttpMethod.GET, reportPage.getWebResponse().getWebRequest().getHttpMethod());		
		
//		System.out.println(reportPage.asText());
		assertTrue(reportPage.asXml().contains("JOSE FARIA"));
		assertFalse(reportPage.asXml().contains("LUIS SANTOS"));
		
		// to check GET parameter's
//		List<NameValuePair> parameters = reportPage.getWebResponse().getWebRequest().getRequestParameters();
//		for (NameValuePair parameter : parameters) {
//			System.out.println(parameter.getName() + " = " + parameter.getValue());
//		}
	}

}