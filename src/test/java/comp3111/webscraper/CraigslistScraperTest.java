package comp3111.webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextArea;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.assertEquals;

public class CraigslistScraperTest {

	/**
	 * Jobs that calls before every test case
	 * Purpose: create a new JavaFx UI Thread for GUI related testing
	 * @throws InterruptedException
	 * @author Sawa
	 */
	@BeforeClass
	public static void javaFxThread() throws InterruptedException {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				new JFXPanel(); // Initializes the JavaFx Platform
			}
		});
		// start the thread
		thread.start();
	}


	@Test
	public void scrapeTest() {
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		CraigslistScraper craigslistScraper = new CraigslistScraper();
		List<Item> itemList;
		TextArea textAreaConsole = new TextArea("");

		itemList = craigslistScraper.scrape(client, "abc", textAreaConsole);
		assertEquals(Vector.class, itemList.getClass());

		itemList = craigslistScraper.scrape(client, "google pixel 5", textAreaConsole);
		assertEquals(Vector.class, itemList.getClass());

		itemList = craigslistScraper.scrape(client, "NoResultFound...", textAreaConsole);
		assertEquals(Vector.class, itemList.getClass());
	}


	@Test
	public void getPageItemRangeFromTest() {
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		CraigslistScraper craigslistScraper = new CraigslistScraper();

		try {
			HtmlPage page;
			int pageItemRangeFrom;

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page1.html").toURI().toURL());
			pageItemRangeFrom = craigslistScraper.getPageItemRangeFrom(page);
			assertEquals(1, pageItemRangeFrom);

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page2.html").toURI().toURL());
			pageItemRangeFrom = craigslistScraper.getPageItemRangeFrom(page);
			assertEquals(121, pageItemRangeFrom);

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page3.html").toURI().toURL());
			pageItemRangeFrom = craigslistScraper.getPageItemRangeFrom(page);
			assertEquals(241, pageItemRangeFrom);

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page4.html").toURI().toURL());
			pageItemRangeFrom = craigslistScraper.getPageItemRangeFrom(page);
			assertEquals(361, pageItemRangeFrom);

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page5.html").toURI().toURL());
			pageItemRangeFrom = craigslistScraper.getPageItemRangeFrom(page);
			assertEquals(481, pageItemRangeFrom);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void getPageItemRangeToTest() {
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		CraigslistScraper craigslistScraper = new CraigslistScraper();

		try {
			HtmlPage page;
			int pageItemRangeTo;

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page1.html").toURI().toURL());
			pageItemRangeTo = craigslistScraper.getPageItemRangeTo(page);
			assertEquals(120, pageItemRangeTo);

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page2.html").toURI().toURL());
			pageItemRangeTo = craigslistScraper.getPageItemRangeTo(page);
			assertEquals(240, pageItemRangeTo);

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page3.html").toURI().toURL());
			pageItemRangeTo = craigslistScraper.getPageItemRangeTo(page);
			assertEquals(360, pageItemRangeTo);

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page4.html").toURI().toURL());
			pageItemRangeTo = craigslistScraper.getPageItemRangeTo(page);
			assertEquals(480, pageItemRangeTo);

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page5.html").toURI().toURL());
			pageItemRangeTo = craigslistScraper.getPageItemRangeTo(page);
			assertEquals(490, pageItemRangeTo);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void getPageTotalItemCountTest() {
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		CraigslistScraper craigslistScraper = new CraigslistScraper();

		try {
			HtmlPage page;
			int pageTotalItemCount;

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page1.html").toURI().toURL());
			pageTotalItemCount = craigslistScraper.getPageTotalItemCount(page);
			assertEquals(488, pageTotalItemCount);

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page2.html").toURI().toURL());
			pageTotalItemCount = craigslistScraper.getPageTotalItemCount(page);
			assertEquals(485, pageTotalItemCount);

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page3.html").toURI().toURL());
			pageTotalItemCount = craigslistScraper.getPageTotalItemCount(page);
			assertEquals(488, pageTotalItemCount);

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page4.html").toURI().toURL());
			pageTotalItemCount = craigslistScraper.getPageTotalItemCount(page);
			assertEquals(489, pageTotalItemCount);

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page5.html").toURI().toURL());
			pageTotalItemCount = craigslistScraper.getPageTotalItemCount(page);
			assertEquals(490, pageTotalItemCount);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void getResultRowsTest() {
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		CraigslistScraper craigslistScraper = new CraigslistScraper();

		try {
			HtmlPage page;
			List<?> resultRows;

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page1.html").toURI().toURL());
			resultRows = craigslistScraper.getResultRows(page);
			assertEquals(120, resultRows.size());

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page2.html").toURI().toURL());
			resultRows = craigslistScraper.getResultRows(page);
			assertEquals(120, resultRows.size());

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page3.html").toURI().toURL());
			resultRows = craigslistScraper.getResultRows(page);
			assertEquals(120, resultRows.size());

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page4.html").toURI().toURL());
			resultRows = craigslistScraper.getResultRows(page);
			assertEquals(120, resultRows.size());

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page5.html").toURI().toURL());
			resultRows = craigslistScraper.getResultRows(page);
			assertEquals(10, resultRows.size());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void getTitleTest() {
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		CraigslistScraper craigslistScraper = new CraigslistScraper();

		try {
			HtmlPage page;
			HtmlElement resultRows;
			String title;

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page1.html").toURI().toURL());
			resultRows = (HtmlElement) craigslistScraper.getResultRows(page).get(0);
			title = craigslistScraper.getTitle(resultRows);
			assertEquals("ABC Carpet Persian Rug Design Bokhara hand made in Pakistan,", title);

			resultRows = (HtmlElement) craigslistScraper.getResultRows(page).get(1);
			title = craigslistScraper.getTitle(resultRows);
			assertEquals("METAL SILVER FOILED MIRROR FROM ABC HOME", title);

			resultRows = (HtmlElement) craigslistScraper.getResultRows(page).get(2);
			title = craigslistScraper.getTitle(resultRows);
			assertEquals("ABC Home Mirrored Console Table - Iron Base with Mirror Top", title);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void getUrlTest() {
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		CraigslistScraper craigslistScraper = new CraigslistScraper();

		try {
			HtmlPage page;
			HtmlElement resultRows;
			String url;

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page1.html").toURI().toURL());
			resultRows = (HtmlElement) craigslistScraper.getResultRows(page).get(0);
			url = craigslistScraper.getUrl(resultRows);
			assertEquals("https://newyork.craigslist.org/brk/atq/d/abc-carpet-persian-rug-design/6740270622.html", url);

			resultRows = (HtmlElement) craigslistScraper.getResultRows(page).get(1);
			url = craigslistScraper.getUrl(resultRows);
			assertEquals("https://newyork.craigslist.org/wch/fuo/d/metal-silver-foiled-mirror/6726411631.html", url);

			resultRows = (HtmlElement) craigslistScraper.getResultRows(page).get(2);
			url = craigslistScraper.getUrl(resultRows);
			assertEquals("https://newyork.craigslist.org/wch/fuo/d/abc-home-mirrored-console/6732746593.html", url);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void getPriceTest() {
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		CraigslistScraper craigslistScraper = new CraigslistScraper();

		try {
			HtmlPage page;
			HtmlElement resultRows;
			double price;

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page1.html").toURI().toURL());
			resultRows = (HtmlElement) craigslistScraper.getResultRows(page).get(0);
			price = craigslistScraper.getPrice(resultRows);
			assertEquals(5070.0, price, 0.0000000001);

			resultRows = (HtmlElement) craigslistScraper.getResultRows(page).get(1);
			price = craigslistScraper.getPrice(resultRows);
			assertEquals(2535.0, price, 0.0000000001);

			resultRows = (HtmlElement) craigslistScraper.getResultRows(page).get(2);
			price = craigslistScraper.getPrice(resultRows);
			assertEquals(2730.0, price, 0.0000000001);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void getPostedDateTest() {
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		CraigslistScraper craigslistScraper = new CraigslistScraper();

		try {
			HtmlPage page;
			HtmlElement resultRows;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date postedDate;

			page = client.getPage(new File("test-web-cache/craigslist_search_abc_page1.html").toURI().toURL());
			resultRows = (HtmlElement) craigslistScraper.getResultRows(page).get(0);
			postedDate = craigslistScraper.getPostedDate(resultRows);
			assertEquals(sdf.parse("2018-11-14 20:09"), postedDate);

			resultRows = (HtmlElement) craigslistScraper.getResultRows(page).get(1);
			postedDate = craigslistScraper.getPostedDate(resultRows);
			assertEquals(sdf.parse("2018-11-14 18:35"), postedDate);

			resultRows = (HtmlElement) craigslistScraper.getResultRows(page).get(2);
			postedDate = craigslistScraper.getPostedDate(resultRows);
			assertEquals(sdf.parse("2018-11-14 18:30"), postedDate);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
