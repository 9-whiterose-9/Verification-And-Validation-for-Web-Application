package webapp.webpresentation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import webapp.services.AddressesDTO;
import webapp.services.ApplicationException;
import webapp.services.CustomerService;


/**
 * Servlet implementation class GetCustomerPageController
 * 
 * Handles the http get and post. Decides which model and view to use.
 * 
 * Decode the URL, extract any form data, decide action 
 * Create and invoke any model objects
 * Determine which view should display the result page 
 * (Forward information to it)
 * 
 */
@WebServlet("/GetCustomerPageController")
public class GetCustomerPageController extends PageController {

	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CustomerService cs = CustomerService.INSTANCE;        

		CustomerHelper ch = new CustomerHelper();
		request.setAttribute("helper", ch);
		AddressesHelper ash = new AddressesHelper();
		request.setAttribute("addressesHelper", ash);
		try {
			String vat = request.getParameter("vat");
			String address = request.getParameter("address");
			String door = request.getParameter("door");
			String postalCode = request.getParameter("postalCode");
			String locality = request.getParameter("locality");
			if (isInt(ch, vat, "Invalid VAT number") || isInt(ash, vat, "Invalid VAT number")) {
				int vatNumber = intValue(vat);
				ch.fillWithCustomer(cs.getCustomerByVat(vatNumber));
				if(address != null) {
					cs.addAddressToCustomer(vatNumber, (address + ";" + door + ";" + postalCode + ";" + locality));
				}
				AddressesDTO a = cs.getAllAddresses(vatNumber);
				ash.fillWithAddresses(a.addrs);
				request.getRequestDispatcher("CustomerInfo.jsp").forward(request, response);
			}
		} catch (ApplicationException e) {
			ch.addMessage("It was not possible to fulfill the request: " + e.getMessage());
			request.getRequestDispatcher("CustomerError.jsp").forward(request, response); 
		}

	}

}
