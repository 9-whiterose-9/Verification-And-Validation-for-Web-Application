package webapp.services;

import java.util.List;

public class AddressesDTO {
public final List<AddressDTO> addrs;
	
	public AddressesDTO(List<AddressDTO> addrs) {
		this.addrs = addrs;
	}
    // ADDED: Getter for the addresses list
    public List<AddressDTO> getAddresses() {
        return this.addrs;
    }
}
