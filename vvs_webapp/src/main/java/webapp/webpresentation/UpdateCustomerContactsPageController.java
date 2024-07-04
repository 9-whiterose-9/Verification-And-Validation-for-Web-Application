package webapp.webpresentation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
@WebServlet("/UpdateCustomerContactsPageController")
public class UpdateCustomerContactsPageController extends PageController {

	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CustomerService cs = CustomerService.INSTANCE;        

		CustomerHelper ch = new CustomerHelper();
		request.setAttribute("helper", ch);
		try{
			String vat = request.getParameter("vat");
			String phone = request.getParameter("phone");
			if (isInt(ch, vat, "Invalid VAT number") && isInt(ch, phone, "Invalid phone number")) {
				int vatNumber = intValue(vat);
				int phoneNumber = intValue(phone);
				cs.updateCustomerPhone(vatNumber,phoneNumber);
				ch.fillWithCustomer(cs.getCustomerByVat(vatNumber));
				request.getRequestDispatcher("CustomerInfo.jsp").forward(request, response);
			}
		} catch (ApplicationException e) {
			ch.addMessage("It was not possible to fulfill the request: " + e.getMessage());
			request.getRequestDispatcher("CustomerError.jsp").forward(request, response); 
		}
	}

}
