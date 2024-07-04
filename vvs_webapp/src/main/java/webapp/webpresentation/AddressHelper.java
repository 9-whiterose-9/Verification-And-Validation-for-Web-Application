package webapp.webpresentation;

import webapp.services.AddressDTO;

public class AddressHelper extends Helper{
	private int id;
	private int customerVat;
	private String address;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCustomerVat() {
		return customerVat;
	}

	public void setCustomerVat(int customerVat) {
		this.customerVat = customerVat;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void fillWithAddress(AddressDTO addressDTO) {
		id = addressDTO.id;
		customerVat = addressDTO.customerVat;
		address = addressDTO.address;
	}
}
