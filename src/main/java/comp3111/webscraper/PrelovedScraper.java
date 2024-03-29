package comp3111.webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Web Scraper for scrapping item from Preloved, which extends PortalScraper
 * @author Chan Chi Wa - cwchanbf
 */
public class PrelovedScraper extends PortalScraper {

	/**
	 * Implementing scrape method for Preloved, scrapping search result from it
	 * @param client WebClient for getting web page from web
	 * @param keyword Keyword for search on portal
	 * @param textAreaConsole TextArea on GUI interface, showing program activity
	 * @return List of search result from the portal
	 * @author Chan Chi Wa - cwchanbf
	 */
	@Override
	public List<Item> scrape(WebClient client, String keyword, TextArea textAreaConsole) {
		final String PRELOVED_URL = "https://www.preloved.co.uk/";
		final boolean FETCH_ONE_PAGE_ONLY = true;
		Vector<Item> result = new Vector<Item>();

		try {
			int pageNum = 0;
			int totalPage = 1;

			do {
				pageNum++;

				String searchUrl = PRELOVED_URL + "search?";
				searchUrl += "keyword=" + URLEncoder.encode(keyword, "UTF-8") + "&";
				searchUrl += "page=" + pageNum;

				HtmlPage page = client.getPage(searchUrl);

				List<?> items = this.getResultRows(page);

				if ( items.size() == 0 ) {
					String msg = "No matched items found on " + Portal.Preloved + ".";
					System.out.println(msg);
					this.appendTextToUserConsole(msg, textAreaConsole);
					break;
				}

				HtmlElement currPageNum = this.getCurrPageNum(page);

				if ( currPageNum != null ) {
					String[] pageNumSplit = currPageNum.asText().replace("Go to Search Results Page ", "").replace("of ", "").split(" ");
					pageNum = Integer.valueOf(pageNumSplit[0]);
					totalPage = Integer.valueOf(pageNumSplit[1]);
				}

				String status = "Fetching form " + Portal.Preloved + ": Page " + pageNum + " of " + totalPage + " was processed.";
				System.out.println(status);
				System.out.println(searchUrl);
				this.appendTextToUserConsole(status, textAreaConsole);

				if ( FETCH_ONE_PAGE_ONLY ) {
					final String ONE_PAGE_ONLY = "Fetching form " + Portal.Preloved + ": FETCH_ONE_PAGE_ONLY = true, only fetch the first page of result, the following page was skipped for faster loading speed.\n";
					System.out.print(ONE_PAGE_ONLY);
					this.appendTextToUserConsole(ONE_PAGE_ONLY, textAreaConsole);
				}

				for (int i = 0; i < items.size(); i++) {
					HtmlElement htmlItem = (HtmlElement) items.get(i);

					Item item = new Item();
					item.setTitle(this.getTitle(htmlItem));
					item.setPortal(Portal.Preloved);
					item.setUrl(this.getUrl(htmlItem));
					item.setPrice(this.getPrice(htmlItem));
					item.setPostedOn(this.getPostedDate(client, htmlItem));

					String fetchingPostedDate = "Getting " + Portal.Preloved + " item's posted date... (" + (i+1) + " of " + items.size() + " processed)";
					System.out.println(fetchingPostedDate);
					this.appendTextToUserConsole(fetchingPostedDate, textAreaConsole);

					result.add(item);
				}
				System.out.println("item count: " + items.size());
			} while (!FETCH_ONE_PAGE_ONLY && pageNum < totalPage);

			System.out.println("result count: " + result.size());
			this.appendTextToUserConsole("\n", textAreaConsole);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return result;
	}


	/**
	 * Get the current page number
	 * @param page The current HtmlPage obj
	 * @return A list of result rows
	 * @author Chan Chi Wa - cwchanbf
	 */
	public HtmlElement getCurrPageNum(HtmlPage page) {
		return page.getFirstByXPath("//li[@class='pagination__nav__item pagination__nav__item--page pagination__nav__item--current-page']/a");
	}


	/**
	 * Get the result rows from the current page
	 * @param page The current HtmlPage obj
	 * @return A list of result rows
	 * @author Chan Chi Wa - cwchanbf
	 */
	public List<?> getResultRows(HtmlPage page) {
		return (List<?>) page.getByXPath("//ul[@id='search-results-list']//li[@class='search-result']");
	}


	/**
	 * Get the title of the current result row
	 * @param htmlElement The result row HtmlElement
	 * @return Title of the item
	 * @author Chan Chi Wa - cwchanbf
	 */
	public String getTitle(HtmlElement htmlElement) {
		HtmlElement itemName = ((HtmlElement) htmlElement.getFirstByXPath(".//span[@itemprop='name']"));
		return itemName.asText();
	}


	/**
	 * Get the url of the current result row
	 * @param htmlElement The result row HtmlElement
	 * @return Url of the item
	 * @author Chan Chi Wa - cwchanbf
	 */
	public String getUrl(HtmlElement htmlElement) {
		return htmlElement.getAttribute("data-href");
	}


	/**
	 * Get the price of the current result row
	 * @param htmlElement The result row HtmlElement
	 * @return Price of the item
	 * @author Chan Chi Wa - cwchanbf
	 */
	public double getPrice(HtmlElement htmlElement) {
		HtmlElement spanPrice = ((HtmlElement) htmlElement.getFirstByXPath(".//span[@itemprop='price']"));
		// It is possible that an item doesn't have any price, we set the price to 0.0
		// in this case
		String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText();
		double price = Double.valueOf(itemPrice.replaceAll("[^0-9.]", ""));
		price *= 10.0;
		return price;
	}


	/**
	 * Get the posted date of the current result row
	 * @param client WebClient for getting web page from web
	 * @param htmlElement The result row HtmlElement
	 * @return Posted date of the item
	 * @author Chan Chi Wa - cwchanbf
	 */
	public Date getPostedDate(WebClient client, HtmlElement htmlElement) {
		HtmlPage itemPage = null;
		try {
			itemPage = client.getPage(this.getUrl(htmlElement));
			HtmlElement lastUpdateHtml = itemPage.getFirstByXPath("//li[@class='classified__additional__meta__item classified__timeago']");
			String lastUpdate = lastUpdateHtml.asText().replace("This advert was updated ", "");
			String[] lastUpdateSplit = lastUpdate.split(" ");
			long secondOffset = 0;
			final int SECOND = 60*1000;
			final int HOUR = 60*SECOND;
			Date postedOn = new Date();
			if ( lastUpdate.contains("hour ago") || lastUpdate.contains("hours ago") ) {
				secondOffset += Integer.valueOf(lastUpdateSplit[0]) * HOUR;
				postedOn = new Date(new Date().getTime() - secondOffset);
			} else if ( lastUpdate.contains("day ago") || lastUpdate.contains("days ago") ) {
				Calendar c = Calendar.getInstance();
				c.setTime(postedOn);
				c.add(Calendar.DATE, -Integer.valueOf(lastUpdateSplit[0]));
				postedOn = c.getTime();
			}
			return postedOn;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return null;
	}


	/**
	 * Append the message to GUI console
	 * @param s The string message append to GUI console
	 * @param textAreaConsole The text console on GUI
	 * @author Chan Chi Wa - cwchanbf
	 */
	public void appendTextToUserConsole(String s, TextArea textAreaConsole) {
		Platform.runLater(() -> textAreaConsole.appendText(s + "\n"));
	}

}
