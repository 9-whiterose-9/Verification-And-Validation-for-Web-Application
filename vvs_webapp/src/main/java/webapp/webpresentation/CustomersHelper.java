package webapp.webpresentation;

import java.util.ArrayList;
import java.util.List;

import webapp.services.CustomerDTO;


/**
 * Helper class to assist in the response of getCustomerByVAT.
 * This class is the response information expert.
 * 
 * @author fmartins
 *
 */
public class CustomersHelper extends Helper{

	private List<CustomerHelper> customers;
	
	public CustomersHelper() {
		customers = new ArrayList<>();
	}
	
	public List<CustomerHelper> getCustomers() {
		return customers;
	}

	public void fillWithCustomers(List<CustomerDTO> cust) {
		for(CustomerDTO c : cust) {
			CustomerHelper ch = new CustomerHelper();
			ch.fillWithCustomer(c);
			customers.add(ch);
		}
	}
}


