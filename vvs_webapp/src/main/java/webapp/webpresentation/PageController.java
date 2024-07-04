package webapp.webpresentation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An abstract Page Controller. 
 *   
 * A Page Controller is an object that handles a request 
 * for a specific page or action on a web site.
 * There is one input controller for each logical page (or action) of the web site.
 *    
 * @author fmartins
 *
 */
public abstract class PageController extends HttpServlet {

	private static final long serialVersionUID = -7066373204929867189L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request,response);
	}

	/**
	 * Strategy method for processing each request
	 * @throws ServletException
	 * @throws IOException
	 */
	protected abstract void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
		
	protected boolean isInt(Helper help, String num, String mensagem) {
		try {
			Integer.parseInt(num);
			return true;
		} catch (NumberFormatException e) {
			help.addMessage(mensagem);
			return false;
		}
	}

	protected int intValue(String num) {
		try {
			return Integer.parseInt(num);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	protected boolean isFilled (Helper helper, String valor, String mensagem) {
		if (valor.equals("")) {
			helper.addMessage(mensagem);
			return false;
		}
		return true;
	}

}
