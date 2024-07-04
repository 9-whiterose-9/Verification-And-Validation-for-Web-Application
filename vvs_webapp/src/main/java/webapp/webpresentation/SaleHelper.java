package webapp.webpresentation;

import java.util.Date;

import webapp.services.SaleDTO;

public class SaleHelper extends Helper {

	private int id;
	private Date data;
	private Double total;
	private String statusId;
	private int customerVat;
	
	public void setId(int id) {
		this.id = id;	
	}

	public int getId() {
		return id;	
	}

	public void setDate(Date data) {
		this.data = data;	
	}
	
	public Date getDate() {
		return data;
	}
	
	public void setCustomerVat(int vat) {
		this.customerVat = vat;
	}

	public int getCustomerVat() {
		return customerVat;
	}
	
	public Double getTotal() {
		return total;
	}
		
	public void setTotal(Double total) {
		this.total = total;
	}
	
	public String getStatus() {
		return statusId;
	}
		
	public void setStatus(String status) {
		this.statusId = status;
	}
	

	public void fillWithSale(SaleDTO saleDto) {
		id = saleDto.id;
		data = saleDto.data;
		total = saleDto.total;
		statusId = saleDto.statusId;
		customerVat = saleDto.customerVat;
	}
}
