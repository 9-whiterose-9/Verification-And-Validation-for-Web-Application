package webapp.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddressRowDataGateway{

	private String address;
	private int id;
	private int customerVat;
	
	// 1. constructor 

			/**
			 * Creates a new address given its address and customer vat.
			 * 
			 * @param address The customer's address
			 * @param customerVat The customer's vat
			 */
	
	public AddressRowDataGateway() {
		
	}
		
	public AddressRowDataGateway (String address, int customerVat) {
		this.address = address;
		this.customerVat = customerVat;
		
	}
	
	public AddressRowDataGateway(ResultSet rs) throws RecordNotFoundException {
		try {
			fillAttributes(rs.getString("address"), 
					rs.getInt("customerVat"));
			this.id = rs.getInt("id");
		} catch (SQLException e) {
			throw new RecordNotFoundException ("Customer does not exist", e);
		}
	}
	
	private void fillAttributes(String address, int customerVat) {
		this.address = address;
		this.customerVat = customerVat;
	}
		
	// 2. getters and setters
	public int getId() {
		return id;
	}

	public int getCustVat() {
		return customerVat;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}	
	
	
	/**
	 * The insert adress SQL statement
	 */
	
	private static final String INSERT_ADDRESS_SQL = 
			"insert into address (id, address, customer_Vat) " +
			"values (DEFAULT, ?, ?)";
	
	
	public void insert() throws PersistenceException {
		try (PreparedStatement statement = DataSource.INSTANCE.prepare(INSERT_ADDRESS_SQL)){
			// set statement arguments
			statement.setString(1, address);
			statement.setInt(2, customerVat);
			// executes SQL
			statement.executeUpdate();
		}  catch (SQLException e) {
			throw new PersistenceException("Internal error!", e);
		}
	}
	
	/**
	 * The select address by customer id SQL statement
	 */
	
	private static final String	GET_ADDRESS_BY_CUSTOMER_VAT_SQL =
			"select * " +
				"from address " +
				"where customer_Vat = ?";
	
	/**
	 * Gets the products of a sale by its sale id 
	 * 
	 * @param saleId The sale id to get the products of
	 * @return The set of products that compose the sale
	 * @throws PersistenceException When there is an error obtaining the
	 *         information from the database.CustomerAddress
	 *         
	 *         
	 */
	public List<AddressRowDataGateway> getCustomerAddresses (int customerVat) throws PersistenceException {
		List<AddressRowDataGateway> addrs = new ArrayList<>();
		try (PreparedStatement statement = DataSource.INSTANCE.prepare(GET_ADDRESS_BY_CUSTOMER_VAT_SQL)){
			statement.setInt(1, customerVat);
			try (ResultSet rs = statement.executeQuery()) {
				while (rs.next()) {
					addrs.add(load(rs));
				}
				rs.next();
				return addrs;
			}
		} catch (SQLException e) {
			throw new PersistenceException("Internal error getting a customer by its VAT number", e);
		}
	}
	private static AddressRowDataGateway load(ResultSet rs) throws RecordNotFoundException{
		try {
			AddressRowDataGateway newCustomerAddress = new AddressRowDataGateway(rs.getString("address"), rs.getInt("customer_vat"));
			newCustomerAddress.id = rs.getInt("id");
			return newCustomerAddress;
		} catch (SQLException e) {
			throw new RecordNotFoundException ("Address does not exist", e);
		}
	}
	
}
