package webapp.webpresentation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import webapp.services.ApplicationException;
import webapp.services.SaleService;
import webapp.services.SalesDTO;

@WebServlet("/GetSalePageController")
public class GetSalePageController extends PageController{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SaleService ss = SaleService.INSTANCE;
		
		SalesHelper sh = new SalesHelper();
		request.setAttribute("salesHelper", sh);
		
		try{
			String vat = request.getParameter("customerVat");
			if (isInt(sh, vat, "Invalid VAT number")) {
				int vatNumber = intValue(vat);
				SalesDTO sdto = ss.getSaleByCustomerVat(vatNumber);
				sh.fillWithSales(sdto.sales);
				request.getRequestDispatcher("SalesInfo.jsp").forward(request, response);
			}
		} catch (ApplicationException e) {
			sh.addMessage("It was not possible to fulfill the request: " + e.getMessage());
			request.getRequestDispatcher("CustomerError.jsp").forward(request, response); 
		}
	}

}
