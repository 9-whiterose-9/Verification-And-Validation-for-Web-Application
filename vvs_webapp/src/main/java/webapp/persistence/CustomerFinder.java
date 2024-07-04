package webapp.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Creates a customer row gateway by finding it from the database.
 *	
 * @author fmartins
 * @Version 1.0 (03/02/2015)
 *
 */
public class CustomerFinder {
	
	/**
	 * The select customer by VAT SQL statement
	 */
	private static final String GET_CUSTOMER_BY_VAT_NUMBER_SQL = 
			   "select * from customer where vatnumber = ?";
	
	/**
	 * Gets a customer by its VAT number 
	 * 
	 * @param vat The VAT number of the customer to search for
	 * @return The result set of the query
	 * @throws PersistenceException When there is an error getting the customer
	 * from the database.
	 */
	public CustomerRowDataGateway getCustomerByVATNumber (int vat) throws PersistenceException {
		try (PreparedStatement statement = DataSource.INSTANCE.prepare(GET_CUSTOMER_BY_VAT_NUMBER_SQL)){
			statement.setInt(1, vat);
			try (ResultSet rs = statement.executeQuery()) {
				rs.next();
				return new CustomerRowDataGateway(rs);
			}
		} catch (SQLException e) {
			throw new PersistenceException("Internal error getting a customer by its VAT number", e);
		}
	}

}
