package webapp.services;

import java.util.List;

public class CustomersDTO {

	public final List<CustomerDTO> customers;
	
	public CustomersDTO(List<CustomerDTO> customers) {
		this.customers = customers;
	}

	public List<CustomerDTO> getCustomers() {
		return this.customers;
	}
	
}
