package web_scraping;

import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

public class CountriesWikipedia {
	
	private static final String url = "https://en.wikipedia.org/wiki/List_of_countries_and_dependencies_by_population";
	
	public static void main(String[] args) throws Exception {
		
		HtmlPage page;
		
		try (final WebClient webClient = new WebClient(BrowserVersion.getDefault())) {
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);

			page = webClient.getPage(url);
		}
		
		// get table using a unique feature on the webpage via XPath
		final HtmlTable countriesTable = (HtmlTable) page.getByXPath("//table[@class='wikitable sortable']").toArray()[0];

		for (final HtmlTableRow row : countriesTable.getRows()) {
			// this table has six columns, we need the 2nd and 3rd
		    List<HtmlTableCell> infoCountry = row.getCells();
			if (!infoCountry.get(0).asText().matches("[0-9]+"))  // skip header
				continue; 
  	        System.out.println(infoCountry.get(1).asText() + "  " + infoCountry.get(2).asText());
		    
		}
	}
}
