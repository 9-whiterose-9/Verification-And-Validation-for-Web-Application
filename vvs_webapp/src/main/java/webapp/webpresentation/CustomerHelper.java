package webapp.webpresentation;

import webapp.services.CustomerDTO;

/**
 * Helper class to assist in the response of getCustomerByVAT.
 * This class is the response information expert.
 * 
 * @author fmartins
 *
 */
public class CustomerHelper extends Helper {

	private int id;
	private String designation;
	private int vat;
	private int phNumber;
	
	public void setId(int id) {
		this.id = id;	
	}

	public int getId() {
		return id;	
	}

	public void setDesignation(String desig) {
		this.designation = desig;	
	}
	
	public String getDesignation() {
		return designation;
	}
	
	public void setVat(int vat) {
		this.vat = vat;
	}

	public int getVat() {
		return vat;
	}
	
	public int getPhNumber() {
		return phNumber;
	}
		
	public void setPhNumber(int ph) {
		this.phNumber = ph;
	}

	public void fillWithCustomer(CustomerDTO customerDTO) {
		id = customerDTO.id;
		designation = customerDTO.designation;
		vat = customerDTO.vat;
		phNumber = customerDTO.phoneNumber;
	}
}
