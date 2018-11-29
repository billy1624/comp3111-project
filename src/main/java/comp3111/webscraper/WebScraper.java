package comp3111.webscraper;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Entry point of scrapping from two different portals
 * @author Chan Chi Wa - cwchanbf
 *
 *
 * WebScraper provide a sample code that scrape web content. After it is constructed, you can call the method scrape with a keyword,
 * the client will go to the default url and parse the page by looking at the HTML DOM.  
 * <br>
 * In this particular sample code, it access to craigslist.org. You can directly search on an entry by typing the URL
 * <br>
 * https://newyork.craigslist.org/search/sss?sort=rel&amp;query=KEYWORD
 *  <br>
 * where KEYWORD is the keyword you want to search.
 * <br>
 * Assume you are working on Chrome, paste the url into your browser and press F12 to load the source code of the HTML. You might be freak
 * out if you have never seen a HTML source code before. Keep calm and move on. Press Ctrl-Shift-C (or CMD-Shift-C if you got a mac) and move your
 * mouse cursor around, different part of the HTML code and the corresponding the HTML objects will be highlighted. Explore your HTML page from
 * body &rarr; section class="page-container" &rarr; form id="searchform" &rarr; div class="content" &rarr; ul class="rows" &rarr; any one of the multiple 
 * li class="result-row" &rarr; p class="result-info". You might see something like this:
 * <br>
 * <pre>
 * {@code
 *    <p class="result-info">
 *        <span class="icon icon-star" role="button" title="save this post in your favorites list">
 *           <span class="screen-reader-text">favorite this post</span>
 *       </span>
 *       <time class="result-date" datetime="2018-06-21 01:58" title="Thu 21 Jun 01:58:44 AM">Jun 21</time>
 *       <a href="https://newyork.craigslist.org/que/clt/d/green-star-polyp-gsp-on-rock/6596253604.html" data-id="6596253604" class="result-title hdrlnk">Green Star Polyp GSP on a rock frag</a>
 *       <span class="result-meta">
 *               <span class="result-price">$15</span>
 *               <span class="result-tags">
 *                   pic
 *                   <span class="maptag" data-pid="6596253604">map</span>
 *               </span>
 *               <span class="banish icon icon-trash" role="button">
 *                   <span class="screen-reader-text">hide this posting</span>
 *               </span>
 *           <span class="unbanish icon icon-trash red" role="button" aria-hidden="true"></span>
 *           <a href="#" class="restore-link">
 *               <span class="restore-narrow-text">restore</span>
 *               <span class="restore-wide-text">restore this posting</span>
 *           </a>
 *       </span>
 *   </p>
 *}
 *</pre>
 * <br>
 * The code 
 * <pre>
 * {@code
 * List<?> items = (List<?>) page.getByXPath("//li[@class='result-row']");
 * }
 * </pre>
 * extracts all result-row and stores the corresponding HTML elements to a list called items. Later in the loop it extracts the anchor tag 
 * &lsaquo; a &rsaquo; to retrieve the display text (by .asText()) and the link (by .getHrefAttribute()). It also extracts  
 *
 */
public class WebScraper {

	/**
	 * WebClient {@link WebScraper} for all web scraper
	 * @author Chan Chi Wa - cwchanbf
	 */
	private WebClient client;

	/**
	 * The combined item list {@link Vector} of all item scraped from two different portals
	 * @author Chan Chi Wa - cwchanbf
	 */
	private Vector<Item> combinedList;


	/**
	 * Default Constructor
	 * @author Chan Chi Wa - cwchanbf
	 */
	public WebScraper() {
		client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
	}


	/**
	 * The only method implemented in this class, to scrape web content from the craigslist
	 * @param keyword - the keyword you want to search
	 * @param textAreaConsole - the TextArea instance for outputting related hint
	 * @return A list of Item that has found. A zero size list is return if nothing is found. Null if any exception (e.g. no connectivity)
	 * @author Chan Chi Wa - cwchanbf
	 */
	public List<Item> scrape(String keyword, TextArea textAreaConsole) {
		combinedList = new Vector<Item>();

		List<Item> craigslistList = new CraigslistScraper().scrape(client, keyword, textAreaConsole);
		List<Item> prelovedList = new PrelovedScraper().scrape(client, keyword, textAreaConsole);
		combinedList.addAll(craigslistList);
		combinedList.addAll(prelovedList);
		Collections.sort(combinedList, new ItemComparator());

		return combinedList;
	}

	
	/**
	 * Method for return web client instance, used for controller, e.g. for shutdown connection
	 * @return WebClient in WebScraper class
	 * @author Chan Chi Wa - cwchanbf
	 */
	WebClient getWebClient() {
		return client;
	}


}


/**
 * A item comparator used to sort a list of items by the ascending order of price
 * @author Chan Chi Wa - cwchanbf
 */
class ItemComparator implements Comparator<Item> {
	/**
	 * Compare two item based on price and portal, sort two item by the ascending order of price
	 * @return a negative integer, zero, or a positive integer as this item's price
	 * 	       is less than, equal to, or greater than the specified item's price.
	 * @author Chan Chi Wa - cwchanbf
	 */
	@Override
	public int compare(Item a, Item b) {
		if ( a.compareTo(b) != 0 ) {
			return a.compareTo(b);
		} else {
			if ( a.getPortal() == b.getPortal() ) {
				return 0;
			} else if ( a.getPortal() == Portal.Craigslist ) {
				return -1;
			} else if ( a.getPortal() == Portal.Preloved ){
				return 1;
			} else {
				return 0;
			}
		}
	}
}

