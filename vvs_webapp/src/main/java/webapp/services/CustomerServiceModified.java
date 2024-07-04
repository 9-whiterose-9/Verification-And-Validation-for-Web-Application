package webapp.services;

import webapp.persistence.CustomerFinder;
import webapp.persistence.CustomerRowDataGateway;
import webapp.persistence.PersistenceException;
/**
 * Question 4
 */
public class CustomerServiceModified implements ICustomerService{
    public static ICustomerService INSTANCE = new CustomerServiceModified();

    @Override
    public void addCustomer(int vat, String designation, int phoneNumber) throws ApplicationException {
	    if (!isValidVAT(vat)) {
	        throw new ApplicationException("Invalid VAT number: " + vat);
	    } else {
	        try {
	            System.out.println("Creating CustomerRowDataGateway...");
	            CustomerRowDataGateway customer = new CustomerRowDataGateway(vat, designation, phoneNumber);
	            System.out.println("Inserting customer...");
	            customer.insert();
	        } catch (PersistenceException e) {
	            System.out.println("PersistenceException: " + e.getMessage());
	            throw new ApplicationException("Can't add customer with vat number " + vat + ".", e);
	        }
	    }
    }

    @Override
    public CustomerDTO getCustomerByVat(int vat) throws ApplicationException {
		if (!isValidVAT (vat))
			throw new ApplicationException ("Invalid VAT number: " + vat);
		else try {
			CustomerRowDataGateway customer = new CustomerFinder().getCustomerByVATNumber(vat);
			return new CustomerDTO(customer.getCustomerId(), customer.getVAT(), 
					customer.getDesignation(), customer.getPhoneNumber());
		} catch (PersistenceException e) {
				throw new ApplicationException ("Customer with vat number " + vat + " not found.", e);
		}
    }
	//...
    
	/**
	 * Checks if a VAT number is valid.
	 * 
	 * @param vat The number to be checked.
	 * @return Whether the VAT number is valid. 
	 */
	private boolean isValidVAT(int vat) {
		// If the number of digits is not 9, error!
		if (vat < 100000000 || vat > 999999999)
			return false;
		
		// If the first number is not 1, 2, 5, 6, 8, 9, error!
		int firstDigit = vat / 100000000;
		if (firstDigit != 1 && firstDigit != 2 && 
			firstDigit != 5 && firstDigit != 6 &&
			firstDigit != 8 && firstDigit != 9)
			return false;
		
		// Checks the congruence modules 11.
		int sum = 0;
		int checkDigit = vat % 10;
		vat /= 10;
		
		for (int i = 2; i < 10 && vat != 0; i++) {
			sum += vat % 10 * i;
			vat /= 10;
		}
		
		int checkDigitCalc = 11 - sum % 11;
		if (checkDigitCalc == 10)
			checkDigitCalc = 0;
		return checkDigit == checkDigitCalc;
	}
}
