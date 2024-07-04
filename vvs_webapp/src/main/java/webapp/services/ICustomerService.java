package webapp.services;
/**
 * Question 4
 * Interface representing the customer service in the application.
 * This interface is part of the refactoring to facilitate mocking and 
 * unit testing of business layer modules, as required in Question 4.
 * It defines the essential operations related to customer management 
 * that need to be implemented by any customer service class.
 */
public interface ICustomerService {
    /**
     * Adds a new customer with the specified VAT number, designation, and phone number.
     *
     * @param vat The VAT number of the customer to be added.
     * @param designation The designation or name of the customer.
     * @param phoneNumber The phone number of the customer.
     * @throws ApplicationException if there is an error during the operation, 
     * such as an invalid VAT number or database access issue.
     */
    void addCustomer(int vat, String designation, int phoneNumber) throws ApplicationException;
    
    /**
     * Retrieves the customer details based on the specified VAT number.
     *
     * @param vat The VAT number of the customer to be retrieved.
     * @return A CustomerDTO object containing the customer's details.
     * @throws ApplicationException if there is an error during the operation, 
     * such as an invalid VAT number or if the customer is not found.
     */
    CustomerDTO getCustomerByVat(int vat) throws ApplicationException;
}
