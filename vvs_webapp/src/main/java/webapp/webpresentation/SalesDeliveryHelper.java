package webapp.webpresentation;

import java.util.ArrayList;
import java.util.List;

import webapp.services.SaleDeliveryDTO;

public class SalesDeliveryHelper extends Helper{
	private List<SaleDeliveryHelper> sales;

	public SalesDeliveryHelper() {
		sales = new ArrayList<>();
	}
	
	public List<SaleDeliveryHelper> getSalesDelivery() {
		return sales;
	}
	
	public void fillWithSalesDelivery(List<SaleDeliveryDTO> sl) {
		for(SaleDeliveryDTO sd : sl) {
			SaleDeliveryHelper sh = new SaleDeliveryHelper();
			sh.fillWithSaleDelivery(sd);
			sales.add(sh);
		}
	}
}
