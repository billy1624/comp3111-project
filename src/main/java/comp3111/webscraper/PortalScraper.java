package comp3111.webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.scene.control.TextArea;

import java.util.Date;
import java.util.List;

/**
 * The abstract class of all portal scraper
 * @author Chan Chi Wa - cwchanbf
 */
public abstract class PortalScraper {
	/**
	 * Each portal scraper should implement the scrape function to scrape items from the portal by keyword
	 * @param client WebClient for getting web page from web
	 * @param keyword Keyword for search on portal
	 * @param textAreaConsole TextArea on GUI interface, showing program activity
	 * @return List of search result from the portal
	 * @author Chan Chi Wa - cwchanbf
	 */
	public abstract List<Item> scrape(WebClient client, String keyword, TextArea textAreaConsole);
}
