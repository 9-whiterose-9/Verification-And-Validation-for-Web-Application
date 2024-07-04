package webapp.webpresentation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import webapp.services.ApplicationException;
import webapp.services.SaleService;
import webapp.services.SalesDTO;

@WebServlet("/UpdateSaleStatusPageControler")
public class UpdateSaleStatusPageControler extends PageController{
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SaleService ss = SaleService.INSTANCE;        

		SalesHelper ssh = new SalesHelper();
		request.setAttribute("salesHelper", ssh);
		try{
			String id = request.getParameter("id");
			if(id != null) {
				if (isInt(ssh, id, "Invalid VAT number")) {
				
					int idNumber = intValue(id);
					ss.updateSale(idNumber);
				}
			}	
			SalesDTO s = ss.getAllSales();
			ssh.fillWithSales(s.sales);
			request.getRequestDispatcher("CloseSale.jsp").forward(request, response);	
		} catch (ApplicationException e) {
			//ch.addMessage("It was not possible to fulfill the request: " + e.getMessage());
			request.getRequestDispatcher("CustomerError.jsp").forward(request, response); 
		}
	}
}
