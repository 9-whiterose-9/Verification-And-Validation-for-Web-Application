package webapp.webpresentation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import webapp.services.ApplicationException;
import webapp.services.CustomerServiceModified;
import webapp.services.ICustomerService;


public class AddCustomerPageControllerModified extends PageController{

	private static final long serialVersionUID = 1L;
	private ICustomerService customerService;

    public AddCustomerPageControllerModified(ICustomerService service) {
        this.customerService = service;
    }

    @Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Use `customerService` instead of `CustomerService.INSTANCE`
		CustomerServiceModified cs = (CustomerServiceModified) this.customerService;
		
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
    
    // Public method for testing
    public void testProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.process(request, response);
    }
    

    
}
