package webapp.webpresentation;

import java.util.ArrayList;
import java.util.List;

import webapp.services.SaleDTO;

public class SalesHelper extends Helper{

	private List<SaleHelper> sales;

	public SalesHelper() {
		sales = new ArrayList<>();
	}
	
	public List<SaleHelper> getSales() {
		return sales;
	}
	
	public void fillWithSales(List<SaleDTO> sl) {
		for(SaleDTO s : sl) {
			SaleHelper sh = new SaleHelper();
			sh.fillWithSale(s);
			sales.add(sh);
		}
	}
}
