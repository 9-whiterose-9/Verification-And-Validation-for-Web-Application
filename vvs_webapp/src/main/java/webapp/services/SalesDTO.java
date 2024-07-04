package webapp.services;

import java.util.List;

public class SalesDTO {
	public final List<SaleDTO> sales;

	public SalesDTO(List<SaleDTO> sales) {
		this.sales = sales;
	}

	public List<SaleDTO> getSales() {
		return this.sales;
	}
	
}
