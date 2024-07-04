package webapp.webpresentation;

import java.util.ArrayList;
import java.util.List;

import webapp.services.AddressDTO;

public class AddressesHelper extends Helper{
private List<AddressHelper> addresses;
	
	public AddressesHelper() {
		addresses = new ArrayList<>();
	}
	
	public List<AddressHelper> getAddresses() {
		return addresses;
	}

	public void fillWithAddresses(List<AddressDTO> addr) {
		for(AddressDTO a : addr) {
			AddressHelper ah = new AddressHelper();
			ah.fillWithAddress(a);
			addresses.add(ah);
		}
	}
}
