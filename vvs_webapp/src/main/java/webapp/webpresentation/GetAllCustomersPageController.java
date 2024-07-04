package webapp.webpresentation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import webapp.services.ApplicationException;
import webapp.services.CustomerService;
import webapp.services.CustomersDTO;

@WebServlet("/GetAllCustomersPageController")
public class GetAllCustomersPageController extends PageController{
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CustomerService cs = CustomerService.INSTANCE;        

		CustomersHelper csh = new CustomersHelper();
		request.setAttribute("helper", csh);
		try {		
			CustomersDTO c = cs.getAllCustomers();
			csh.fillWithCustomers(c.customers);
			request.getRequestDispatcher("CustomersInfo.jsp").forward(request, response);

		} catch (ApplicationException e) {
			csh.addMessage("It was not possible to fulfill the request: " + e.getMessage());
			//request.getRequestDispatcher("CustomerError.jsp").forward(request, response); 
		}

	}
}
