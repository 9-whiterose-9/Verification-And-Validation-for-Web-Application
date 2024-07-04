package webapp.webpresentation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import webapp.services.AddressesDTO;
import webapp.services.ApplicationException;
import webapp.services.CustomerService;
import webapp.services.SaleService;
import webapp.services.SalesDTO;
import webapp.services.SalesDeliveryDTO;

@WebServlet("/AddSaleDeliveryPageController")
public class AddSaleDeliveryPageController extends PageController{
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CustomerService cs = CustomerService.INSTANCE;
		SaleService ss = SaleService.INSTANCE;
		
		AddressesHelper ash = new AddressesHelper();
		request.setAttribute("addressesHelper", ash);
		SalesHelper ssh = new SalesHelper();
		request.setAttribute("salesHelper", ssh);
		SalesDeliveryHelper sdh = new SalesDeliveryHelper();
		request.setAttribute("saleDeliveryHelper", sdh);
		
		try{
			String vat = request.getParameter("vat");
			String addr = request.getParameter("addr_id");
			String sale = request.getParameter("sale_id");
			
			if(addr != null && sale != null) {
				if (isInt(sdh, sale, "Invalid Sale Id") && isInt(sdh, addr, "Invalid Address Id")) {
					int addr_id = intValue(addr);
					int sale_id = intValue(sale);
					int customerVat = ss.addSaleDelivery(sale_id, addr_id);
					SalesDeliveryDTO sdd = ss.getSalesDeliveryByVat(customerVat);
					sdh.fillWithSalesDelivery(sdd.sales_delivery); 
					request.getRequestDispatcher("SalesDeliveryInfo.jsp").forward(request, response);
				}
			}
			if(isInt(ash, vat, "Invalid VAT number")) {
				int vatNumber = intValue(vat);
				AddressesDTO asd = cs.getAllAddresses(vatNumber);
				ash.fillWithAddresses(asd.addrs);
				SalesDTO ssd = ss.getSaleByCustomerVat(vatNumber);
				ssh.fillWithSales(ssd.sales);
				request.getRequestDispatcher("addSaleDelivery.jsp").forward(request, response);
			}
		} catch (ApplicationException e) {
			sdh.addMessage("It was not possible to fulfill the request: " + e.getMessage());
			request.getRequestDispatcher("CustomerError.jsp").forward(request, response); 
		}
	}
}
