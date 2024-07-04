package webapp.webpresentation;

import webapp.services.SaleDeliveryDTO;

public class SaleDeliveryHelper extends Helper{
	
	private int id;
	
	private int sale_id;

	private int customer_vat;
	
	private int addr_id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSale_id() {
		return sale_id;
	}

	public void setSale_id(int sale_id) {
		this.sale_id = sale_id;
	}

	public int getCustomer_vat() {
		return customer_vat;
	}

	public void setCustomer_vat(int customer_vat) {
		this.customer_vat = customer_vat;
	}

	public int getAddr_id() {
		return addr_id;
	}

	public void setAddr_id(int addr_id) {
		this.addr_id = addr_id;
	}
	
	public void fillWithSaleDelivery(SaleDeliveryDTO saledelDto) {
		id = saledelDto.id;
		addr_id = saledelDto.addr_id;
		customer_vat = saledelDto.customer_vat;
		sale_id = saledelDto.sale_id;
	}
}
