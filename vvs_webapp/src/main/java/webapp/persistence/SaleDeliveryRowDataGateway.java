package webapp.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SaleDeliveryRowDataGateway {
	
	private int id;
	
	private int sale_id;

	private int customer_vat;
	
	private int addr_id;
	
	/**
	 * The insert delivery SQL statement
	 */
	
	
	private static final String INSERT_SALEDELIVERY_SQL = 
			"insert into saledelivery (id, sale_id, customer_vat, address_id) " +
			"values (DEFAULT, ?, ?, ?)";
	
	/**
	 * The select delivery by saledelivery id SQL statement
	 */
	
	private static final String	GET_SALEDELIVERY_BY_ID_SQL =
			"select id, sale_id, customer_vat, address_id " +
				"from saledelivery " +
				"where id = ?";
	
	
	
	
	// 1. constructor 

	/**
	 * Creates a new delivery given it's sale id and customer id.
	 * 
	 * @param sale_id The sale's id
	 * @param customerDelivery_id The customer's id
	 */
	
	public SaleDeliveryRowDataGateway (int sale_id, int customer_vat, int addr_id) {
		this.addr_id = addr_id;
		this.sale_id = sale_id;
		this.customer_vat = customer_vat;
		
	}
	public SaleDeliveryRowDataGateway () {
	}
	
	public SaleDeliveryRowDataGateway(ResultSet rs) throws RecordNotFoundException {
		try {
			fillAttributes(rs.getInt("sale_id"), 
					rs.getInt("customer_vat"));
			this.id = rs.getInt("id");
		} catch (SQLException e) {
			throw new RecordNotFoundException ("Customer does not exist", e);
		}
	}
	
	private void fillAttributes(int sale_id, int customerVat) {
		this.sale_id = sale_id;
		this.customer_vat = customerVat;
	}
		

	// 2. getters and setters

	public int getId() {
		return id;
	}
	
	public int getSale_id() {
		return sale_id;
	}

	public int getCustomerVat() {
		return customer_vat;
	}
	
	public int getAddr_id() {
		return addr_id;
	}
	
	public void setAddr_id(int addr_id) {
		this.addr_id= addr_id;
	}
	
	// 3. interaction with the repository (a memory map in this simple example)

	public void insert() throws PersistenceException {
		try (PreparedStatement statement = DataSource.INSTANCE.prepare(INSERT_SALEDELIVERY_SQL)){
			// set statement arguments
			statement.setInt(1, sale_id);
			statement.setInt(2, customer_vat);
			statement.setInt(3, addr_id);
			// executes SQL
			statement.executeUpdate();
			
			
		}  catch (SQLException e){
			throw new PersistenceException("Internal error!", e);
		}
	}	
	
	/**
	 * The select delivery by customer id SQL statement
	 */
	
	private static final String	GET_SALEDELIVERY_BY_CUSTOMER_VAT_SQL =
			"select * " +
				"from saledelivery " +
				"where customer_vat = ?";
	
	public List<SaleDeliveryRowDataGateway> getAllSaleDelivery(int vat) throws PersistenceException {
		List<SaleDeliveryRowDataGateway> list = new ArrayList<>();
		try (PreparedStatement statement = DataSource.INSTANCE.prepare(GET_SALEDELIVERY_BY_CUSTOMER_VAT_SQL)) {
			//set statement arguments
			statement.setInt(1, vat);
			//executes SQL
			try (ResultSet rs = statement.executeQuery()) {
				while(rs.next()) {
					list.add(loadSaleDelivery(rs));
				}
				
				return list;
			}
		} catch (SQLException e) {
			throw new PersistenceException("Internal error getting a employee by id", e);
		}
	}

	private static SaleDeliveryRowDataGateway loadSaleDelivery(ResultSet rs) throws RecordNotFoundException{
		try {
			SaleDeliveryRowDataGateway newSaleDelivery = new SaleDeliveryRowDataGateway(rs.getInt("sale_id"), rs.getInt("customer_vat"), rs.getInt("address_id"));
			newSaleDelivery.id = rs.getInt("id");
			return newSaleDelivery;
		} catch (SQLException e) {
			throw new RecordNotFoundException ("Employee does not exist", e);
		}
	}
	
}
