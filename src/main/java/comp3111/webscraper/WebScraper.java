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
 *
 */
public class WebScraper {

	private WebClient client;
	private Vector<Item> combinedList;

	/**
	 * Default Constructor 
	 */
	public WebScraper() {
		client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
	}

	/**
	 * The only method implemented in this class, to scrape web content from the craigslist
	 * 
	 * @param keyword - the keyword you want to search
	 * @param textAreaConsole - the TextArea instance for outputting related hint
	 * @return A list of Item that has found. A zero size list is return if nothing is found. Null if any exception (e.g. no connectivity)
	 */
	public List<Item> scrape(String keyword, TextArea textAreaConsole) {
		combinedList = new Vector<Item>();

		// Thread thread = new Thread(() -> {
			List<Item> craigslistList = scrapeFromCraigslist(keyword, textAreaConsole);
			List<Item> prelovedList = scrapeFromPreloved(keyword, textAreaConsole);
			combinedList.addAll(craigslistList);
			combinedList.addAll(prelovedList);
			Collections.sort(combinedList, new ItemComparator());
		// });
		// thread.start();

		return combinedList;
	}


	public List<Item> scrapeFromCraigslist(String keyword, TextArea textAreaConsole) {
		final String CRAIGSLIST_URL = "https://newyork.craigslist.org/";
		Vector<Item> result = new Vector<Item>();

		try {
			int nextItemNum = 0;

			do {
				String searchUrl = CRAIGSLIST_URL + "search/sss?";
				searchUrl += "s=" + nextItemNum + "&";
				searchUrl += "sort=rel&query=" + URLEncoder.encode(keyword, "UTF-8");

				HtmlPage page = client.getPage(searchUrl);

				List<?> items = (List<?>) page.getByXPath("//li[@class='result-row']");

				if ( items.size() == 0 ) {
					String msg = "No matched items found on " + Portal.Craigslist + ".\n";
					System.out.println(msg);
					Platform.runLater(() -> textAreaConsole.appendText(msg));
					break;
				}

				HtmlElement itemRangeFrom = page.getFirstByXPath("//form[@id='searchform']//span[@class='rangeFrom']");
				HtmlElement itemRangeTo = page.getFirstByXPath("//form[@id='searchform']//span[@class='rangeTo']");
				HtmlElement itemTotalCount = page.getFirstByXPath("//form[@id='searchform']//span[@class='totalcount']");
				int rangefrom = Integer.valueOf(itemRangeFrom.asText());
				int rangeTo = Integer.valueOf(itemRangeTo.asText());
				int totalCount = Integer.valueOf(itemTotalCount.asText());
				if ( rangeTo < totalCount ) {
					nextItemNum = rangeTo;
				} else {
					nextItemNum = 0;
				}

				String status = "Fetching form " + Portal.Craigslist + ": item " + rangefrom + " - " + rangeTo + ", " + totalCount + " in total.";
				System.out.println(status);
				textAreaConsole.appendText(status + "\n");

				for (int i = 0; i < items.size(); i++) {
					HtmlElement htmlItem = (HtmlElement) items.get(i);
					HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//p[@class='result-info']/a"));
					HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//a/span[@class='result-price']"));
					HtmlElement postedDate = ((HtmlElement) htmlItem.getFirstByXPath(".//time[@class='result-date']"));

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					Date postedOn= sdf.parse(postedDate.getAttribute("datetime"));

					// It is possible that an item doesn't have any price, we set the price to 0.0
					// in this case
					String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText();

					Item item = new Item();
					item.setTitle(itemAnchor.asText());
					item.setPortal(Portal.Craigslist);
					item.setUrl(itemAnchor.getHrefAttribute());
					double price = Double.valueOf(itemPrice.replace("$", "").replace(",", "").replace(" ", ""));
					price *= 7.8;
					item.setPrice(price);
					item.setPostedOn(postedOn);

					// System.out.println(item);
					// System.out.println(spanPrice);
					// System.out.println(itemPrice);
					// System.out.println(item.getPrice());
					// System.out.println();

					result.add(item);
				}
				System.out.println("item count: " + items.size());
				// Platform.runLater(() -> textAreaConsole.appendText("item count: " + items.size() + "\n"));
			} while (nextItemNum > 0);

			System.out.println("result count: " + result.size());
			// Platform.runLater(() -> textAreaConsole.appendText("result count: " + result.size() + "\n"));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return result;
	}


	public List<Item> scrapeFromPreloved(String keyword, TextArea textAreaConsole) {
		final String PRELOVED_URL = "https://www.preloved.co.uk/";
		final boolean FETCH_ONE_PAGE_ONLY = true;
		Vector<Item> result = new Vector<Item>();

		try {
			int pageNum = 0;
			int totalPage = 0;

			do {
				pageNum++;

				String searchUrl = PRELOVED_URL + "search?";
				searchUrl += "keyword=" + URLEncoder.encode(keyword, "UTF-8") + "&";
				searchUrl += "page=" + pageNum;

				HtmlPage page = client.getPage(searchUrl);

				List<?> items = (List<?>) page.getByXPath("//ul[@id='search-results-list']//li[@class='search-result']");

				if ( items.size() == 0 ) {
					String msg = "No matched items found on " + Portal.Preloved + ".\n";
					System.out.println(msg);
					Platform.runLater(() -> textAreaConsole.appendText(msg));
					break;
				}

				HtmlElement currPageNum = page.getFirstByXPath("//li[@class='pagination__nav__item pagination__nav__item--page pagination__nav__item--current-page']/a");
				String[] pageNumSplit = currPageNum.asText().replace("Go to Search Results Page ", "").replace("of ", "").split(" ");

				pageNum = Integer.valueOf(pageNumSplit[0]);
				totalPage = Integer.valueOf(pageNumSplit[1]);

				String status = "Fetching form " + Portal.Preloved + ": Page " + pageNum + " of " + totalPage + " was processed.";
				System.out.println(status);
				Platform.runLater(() -> textAreaConsole.appendText(status + "\n"));

				if ( FETCH_ONE_PAGE_ONLY ) {
					final String ONE_PAGE_ONLY = "Fetching form " + Portal.Preloved + ": FETCH_ONE_PAGE_ONLY = true, only fetch the first page of result, the following page was skipped for faster loading speed.\n";
					System.out.print(ONE_PAGE_ONLY);
					Platform.runLater(() -> textAreaConsole.appendText(ONE_PAGE_ONLY));
				}

				for (int i = 0; i < items.size(); i++) {
					HtmlElement htmlItem = (HtmlElement) items.get(i);
					HtmlElement itemName = ((HtmlElement) htmlItem.getFirstByXPath(".//span[@itemprop='name']"));
					HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//span[@itemprop='price']"));
					String itemUrl = htmlItem.getAttribute("data-href");
					Date postedOn = new Date();

					HtmlPage itemPage = client.getPage(itemUrl);
					HtmlElement lastUpdateHtml = itemPage.getFirstByXPath("//li[@class='classified__additional__meta__item classified__timeago']");
					String lastUpdate = lastUpdateHtml.asText().replace("This advert was updated ", "");
					String[] lastUpdateSplit = lastUpdate.split(" ");
					long secondOffset = 0;
					final int SECOND = 60*1000;
					final int HOUR = 60*SECOND;
					if ( lastUpdate.contains("hour ago") || lastUpdate.contains("hours ago") ) {
						secondOffset += Integer.valueOf(lastUpdateSplit[0]) * HOUR;
						postedOn = new Date(new Date().getTime() - secondOffset);
					} else if ( lastUpdate.contains("day ago") || lastUpdate.contains("days ago") ) {
						Calendar c = Calendar.getInstance();
						c.setTime(postedOn);
						c.add(Calendar.DATE, -Integer.valueOf(lastUpdateSplit[0]));
						postedOn = c.getTime();
					}


					// It is possible that an item doesn't have any price, we set the price to 0.0
					// in this case
					String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText();

					Item item = new Item();
					item.setTitle(itemName.asText());
					item.setPortal(Portal.Preloved);

					item.setUrl(itemUrl);
					double price = Double.valueOf(itemPrice.replace("£", "").replace(",", "").replace(" ", ""));
					price *= 10.0;
					item.setPrice(price);
					item.setPostedOn(postedOn);

					// System.out.println(item);
					// System.out.println(spanPrice);
					// System.out.println(itemPrice);
					// System.out.println(item.getPrice());
					// System.out.println();

					result.add(item);
				}
				System.out.println("item count: " + items.size());
				// Platform.runLater(() -> textAreaConsole.appendText("item count: " + items.size() + "\n"));
			} while (!FETCH_ONE_PAGE_ONLY && pageNum < totalPage);

		System.out.println("result count: " + result.size());
		// Platform.runLater(() -> textAreaConsole.appendText("result count: " + result.size() + "\n"));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return result;
	}
	
	/**
	 * Method for return web client instance, used for controller, e.g. for shutdown connection
	 * @return Web
	 * 
	 */
	WebClient getWebClient() {
		return client;
	}


}


class ItemComparator implements Comparator<Item> {
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
