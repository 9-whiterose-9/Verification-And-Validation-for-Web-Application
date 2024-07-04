package webapp.webpresentation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import webapp.services.ApplicationException;
import webapp.services.CustomerService;

@WebServlet("/AddCustomerPageController")
public class AddCustomerPageController extends PageController{
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CustomerService cs = CustomerService.INSTANCE;
		
		CustomerHelper ch = new CustomerHelper();
		request.setAttribute("helper", ch);
		
		try{
			String vat = request.getParameter("vat");
			String phone = request.getParameter("phone");
			String designation = request.getParameter("designation");
			if (isInt(ch, vat, "Invalid VAT number") && isInt(ch, phone, "Invalid phone number")) {
				int vatNumber = intValue(vat);
				int phoneNumber = intValue(phone);
				cs.addCustomer(vatNumber, designation, phoneNumber);
				ch.fillWithCustomer(cs.getCustomerByVat(vatNumber));
				request.getRequestDispatcher("CustomerInfo.jsp").forward(request, response);
			}
		} catch (ApplicationException e) {
			ch.addMessage("It was not possible to fulfill the request: " + e.getMessage());
			request.getRequestDispatcher("CustomerError.jsp").forward(request, response); 
		}
	}
}
