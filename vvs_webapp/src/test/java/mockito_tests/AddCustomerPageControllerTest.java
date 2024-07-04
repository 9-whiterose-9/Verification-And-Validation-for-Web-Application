package mockito_tests;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import webapp.services.ApplicationException;
import webapp.services.CustomerDTO;
import webapp.services.CustomerServiceModified;
import webapp.webpresentation.AddCustomerPageControllerModified;
/**
 * Question 4
 * Unit tests for the AddCustomerPageControllerModified class.
 * These tests verify the behavior of the controller's process method
 * under different conditions using mocked dependencies.
 */
@RunWith(MockitoJUnitRunner.class)
public class AddCustomerPageControllerTest {

    @InjectMocks
    private AddCustomerPageControllerModified acpcMod;
    
    @Mock
    private CustomerServiceModified customerService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;
    
    @Mock
    private RequestDispatcher dispatcher;
    
    /**
     * Sets up the test environment by initializing mocks and setting up 
     * default behaviors for the mocked objects.
     * 
     * @throws ApplicationException if there is an issue with setting up 
     * the customer service mock.
     */
    @Before
    public void setup() throws ApplicationException {
    	when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        // Set up the mock to return a valid CustomerDTO
        when(customerService.getCustomerByVat(anyInt())).thenReturn(new CustomerDTO(1,123456789, "John Doe", 987654321));
    }
    
    /**
     * Tests the successful addition of a customer.
     * Verifies that the customer service's addCustomer method is called with the correct parameters,
     * and the correct JSP page is forwarded to.
     * 
     * @throws IOException if an input or output error is detected when the servlet handles the request
     * @throws ServletException if the request could not be handled
     * @throws ApplicationException if there is an issue with the application logic
     */
    @Test
    public void testAddCustomerSuccess() throws IOException, ServletException, ApplicationException {
        // Arrange
        when(request.getParameter("vat")).thenReturn("123456789");
        when(request.getParameter("phone")).thenReturn("987654321");
        when(request.getParameter("designation")).thenReturn("John Doe");

        // Act
        acpcMod.testProcess(request, response);

        // Assert
        verify(customerService).addCustomer(eq(123456789), eq("John Doe"), eq(987654321));
        verify(request).getRequestDispatcher("CustomerInfo.jsp");
        verify(dispatcher).forward(request, response);
    }

    /**
     * Tests the failure case of adding a customer with an invalid VAT number.
     * Verifies that the customer service's addCustomer method throws an ApplicationException,
     * and the error JSP page is forwarded to.
     * 
     * @throws IOException if an input or output error is detected when the servlet handles the request
     * @throws ServletException if the request could not be handled
     * @throws ApplicationException if there is an issue with the application logic
     */
    @Test
    public void testAddCustomerFailure() throws IOException, ServletException, ApplicationException {
        // Arrange
        String invalidVAT = "333333333";
        when(request.getParameter("vat")).thenReturn(invalidVAT);
        when(request.getParameter("phone")).thenReturn("987654321");
        when(request.getParameter("designation")).thenReturn("John Doe");

        // Prepare the service to throw an exception when an invalid VAT is used
        doThrow(new ApplicationException("Invalid VAT number")).when(customerService).addCustomer(anyInt(), anyString(), anyInt());

        // Act
        acpcMod.testProcess(request, response);

        // Assert
        verify(customerService).addCustomer(anyInt(), eq("John Doe"), eq(987654321));
        verify(request).getRequestDispatcher("CustomerError.jsp");
        verify(dispatcher).forward(request, response);
    }

}
