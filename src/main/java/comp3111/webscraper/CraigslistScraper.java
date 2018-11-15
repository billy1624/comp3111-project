package comp3111.webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class CraigslistScraper extends PortalScraper {

	@Override
	public List<Item> scrape(WebClient client, String keyword, TextArea textAreaConsole) {
		final String CRAIGSLIST_URL = "https://newyork.craigslist.org/";
		Vector<Item> result = new Vector<Item>();

		try {
			int nextItemNum = 0;
			int pageNum = 0;
			int totalPage = 1;

			do {
				pageNum++;

				String searchUrl = CRAIGSLIST_URL + "search/sss?";
				if ( nextItemNum > 0 ) {
					searchUrl += "s=" + nextItemNum + "&";
				}
				searchUrl += "query=" + URLEncoder.encode(keyword, "UTF-8");
				searchUrl += "&sort=rel";

				HtmlPage page = client.getPage(searchUrl);

				List<?> items = this.getResultRows(page);

				if ( items.size() == 0 ) {
					String msg = "No matched items found on " + Portal.Craigslist + ".";
					System.out.println(msg);
					this.appendTextToUserConsole(msg, textAreaConsole);
					break;
				}

				int rangefrom = this.getPageItemRangeFrom(page);
				int rangeTo = this.getPageItemRangeTo(page);
				int totalCount = this.getPageTotalItemCount(page);
				if ( rangeTo < totalCount ) {
					nextItemNum = rangeTo;
				} else {
					nextItemNum = 0;
				}

				final int PAGE_ITEM_NUM = 120;
				if ( totalCount > PAGE_ITEM_NUM ) {
					totalPage = new Double(Math.ceil(1.0*totalCount/PAGE_ITEM_NUM)).intValue();
				}

				String status = "Fetching form " + Portal.Craigslist + ": Page " + pageNum + " of " + totalPage + " was processed.";
				System.out.println(status);
				System.out.println(searchUrl);
				this.appendTextToUserConsole(status, textAreaConsole);

				for (int i = 0; i < items.size(); i++) {
					HtmlElement htmlItem = (HtmlElement) items.get(i);

					Item item = new Item();
					item.setTitle(this.getTitle(htmlItem));
					item.setPortal(Portal.Craigslist);
					item.setUrl(this.getUrl(htmlItem));
					item.setPrice(this.getPrice(htmlItem));
					item.setPostedOn(this.getPostedDate(htmlItem));

					result.add(item);
				}
				System.out.println("item count: " + items.size());
			} while (pageNum < totalPage);

			System.out.println("result count: " + result.size());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return result;
	}


	public int getPageItemRangeFrom(HtmlPage page) {
		HtmlElement itemRangeFrom = page.getFirstByXPath("//form[@id='searchform']//span[@class='rangeFrom']");
		return Integer.valueOf(itemRangeFrom.asText());
	}


	public int getPageItemRangeTo(HtmlPage page) {
		HtmlElement itemRangeTo = page.getFirstByXPath("//form[@id='searchform']//span[@class='rangeTo']");
		return Integer.valueOf(itemRangeTo.asText());
	}


	public int getPageTotalItemCount(HtmlPage page) {
		HtmlElement itemTotalCount = page.getFirstByXPath("//form[@id='searchform']//span[@class='totalcount']");
		return Integer.valueOf(itemTotalCount.asText());
	}


	public List<?> getResultRows(HtmlPage page) {
		return (List<?>) page.getByXPath("//li[@class='result-row']");
	}


	public String getTitle(HtmlElement htmlElement) {
		HtmlAnchor itemAnchor = ((HtmlAnchor) htmlElement.getFirstByXPath(".//p[@class='result-info']/a"));
		return itemAnchor.asText();
	}


	public String getUrl(HtmlElement htmlElement) {
		HtmlAnchor itemAnchor = ((HtmlAnchor) htmlElement.getFirstByXPath(".//p[@class='result-info']/a"));
		return itemAnchor.getHrefAttribute();
	}


	public double getPrice(HtmlElement htmlElement) {
		HtmlElement spanPrice = ((HtmlElement) htmlElement.getFirstByXPath(".//p[@class='result-info']//span[@class='result-price']"));
		// It is possible that an item doesn't have any price, we set the price to 0.0
		// in this case
		String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText();
		double price = Double.valueOf(itemPrice.replaceAll("[^0-9.]", ""));
		price *= 7.8;
		return price;
	}


	public Date getPostedDate(HtmlElement htmlElement) {
		HtmlElement postedDate = ((HtmlElement) htmlElement.getFirstByXPath(".//time[@class='result-date']"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			return sdf.parse(postedDate.getAttribute("datetime"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}


	public void appendTextToUserConsole(String s, TextArea textAreaConsole) {
		Platform.runLater(() -> textAreaConsole.appendText(s + "\n"));
	}

}
