package comp3111.webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextArea;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.assertEquals;

//@Ignore
public class PrelovedScraperTest {

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
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		PrelovedScraper prelovedScraper = new PrelovedScraper();
		List<Item> itemList;
		TextArea textAreaConsole = new TextArea("");

		itemList = prelovedScraper.scrape(client, "abc", textAreaConsole);
		assertEquals(Vector.class, itemList.getClass());

		itemList = prelovedScraper.scrape(client, "google pixel 5", textAreaConsole);
		assertEquals(Vector.class, itemList.getClass());

		itemList = prelovedScraper.scrape(client, "car", textAreaConsole);
		assertEquals(Vector.class, itemList.getClass());

		itemList = prelovedScraper.scrape(client, "NoResultFound...", textAreaConsole);
		assertEquals(Vector.class, itemList.getClass());
	}


	@Test
	public void getCurrPageNumTest() {
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		PrelovedScraper prelovedScraper = new PrelovedScraper();

		try {
			HtmlPage page;
			HtmlElement currPageNum;

			page = client.getPage(new File("test-web-cache/preloved_search_abc_page1.html").toURI().toURL());
			currPageNum = prelovedScraper.getCurrPageNum(page);
			assertEquals("Go to Search Results Page 1 of 9", currPageNum.asText());

			page = client.getPage(new File("test-web-cache/preloved_search_abc_page2.html").toURI().toURL());
			currPageNum = prelovedScraper.getCurrPageNum(page);
			assertEquals("Go to Search Results Page 2 of 9", currPageNum.asText());

			page = client.getPage(new File("test-web-cache/preloved_search_abc_page3.html").toURI().toURL());
			currPageNum = prelovedScraper.getCurrPageNum(page);
			assertEquals("Go to Search Results Page 3 of 9", currPageNum.asText());

			page = client.getPage(new File("test-web-cache/preloved_search_abc_page4.html").toURI().toURL());
			currPageNum = prelovedScraper.getCurrPageNum(page);
			assertEquals("Go to Search Results Page 4 of 9", currPageNum.asText());

			page = client.getPage(new File("test-web-cache/preloved_search_abc_page5.html").toURI().toURL());
			currPageNum = prelovedScraper.getCurrPageNum(page);
			assertEquals("Go to Search Results Page 5 of 9", currPageNum.asText());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void getResultRowsTest() {
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		PrelovedScraper prelovedScraper = new PrelovedScraper();

		try {
			HtmlPage page;
			List<?> resultRows;

			page = client.getPage(new File("test-web-cache/preloved_search_abc_page1.html").toURI().toURL());
			resultRows = prelovedScraper.getResultRows(page);
			assertEquals(20, resultRows.size());

			page = client.getPage(new File("test-web-cache/preloved_search_abc_page2.html").toURI().toURL());
			resultRows = prelovedScraper.getResultRows(page);
			assertEquals(20, resultRows.size());

			page = client.getPage(new File("test-web-cache/preloved_search_abc_page3.html").toURI().toURL());
			resultRows = prelovedScraper.getResultRows(page);
			assertEquals(20, resultRows.size());

			page = client.getPage(new File("test-web-cache/preloved_search_abc_page4.html").toURI().toURL());
			resultRows = prelovedScraper.getResultRows(page);
			assertEquals(20, resultRows.size());

			page = client.getPage(new File("test-web-cache/preloved_search_abc_page5.html").toURI().toURL());
			resultRows = prelovedScraper.getResultRows(page);
			assertEquals(20, resultRows.size());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void getTitleTest() {
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		PrelovedScraper prelovedScraper = new PrelovedScraper();

		try {
			HtmlPage page;
			HtmlElement resultRows;
			String title;

			page = client.getPage(new File("test-web-cache/preloved_search_abc_page1.html").toURI().toURL());
			resultRows = (HtmlElement) prelovedScraper.getResultRows(page).get(0);
			title = prelovedScraper.getTitle(resultRows);
			assertEquals("Dog crate", title);

			resultRows = (HtmlElement) prelovedScraper.getResultRows(page).get(1);
			title = prelovedScraper.getTitle(resultRows);
			assertEquals("generator", title);

			resultRows = (HtmlElement) prelovedScraper.getResultRows(page).get(2);
			title = prelovedScraper.getTitle(resultRows);
			assertEquals("The ABC Murders", title);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void getUrlTest() {
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		PrelovedScraper prelovedScraper = new PrelovedScraper();

		try {
			HtmlPage page;
			HtmlElement resultRows;
			String url;

			page = client.getPage(new File("test-web-cache/preloved_search_abc_page1.html").toURI().toURL());
			resultRows = (HtmlElement) prelovedScraper.getResultRows(page).get(0);
			url = prelovedScraper.getUrl(resultRows);
			assertEquals("https://www.preloved.co.uk/adverts/show/118472877/dog-crate.html?link=%2Fsearch%3Fkeyword%3Dabc", url);

			resultRows = (HtmlElement) prelovedScraper.getResultRows(page).get(1);
			url = prelovedScraper.getUrl(resultRows);
			assertEquals("https://www.preloved.co.uk/adverts/show/118308268/generator.html?link=%2Fsearch%3Fkeyword%3Dabc", url);

			resultRows = (HtmlElement) prelovedScraper.getResultRows(page).get(2);
			url = prelovedScraper.getUrl(resultRows);
			assertEquals("https://www.preloved.co.uk/adverts/show/621695467/the-abc-murders.html?link=%2Fsearch%3Fkeyword%3Dabc", url);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void getPriceTest() {
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		PrelovedScraper prelovedScraper = new PrelovedScraper();

		try {
			HtmlPage page;
			HtmlElement resultRows;
			double price;

			page = client.getPage(new File("test-web-cache/preloved_search_abc_page1.html").toURI().toURL());
			resultRows = (HtmlElement) prelovedScraper.getResultRows(page).get(0);
			price = prelovedScraper.getPrice(resultRows);
			assertEquals(150.0, price, 0.0000000001);

			resultRows = (HtmlElement) prelovedScraper.getResultRows(page).get(1);
			price = prelovedScraper.getPrice(resultRows);
			assertEquals(1500.0, price, 0.0000000001);

			resultRows = (HtmlElement) prelovedScraper.getResultRows(page).get(2);
			price = prelovedScraper.getPrice(resultRows);
			assertEquals(79.9, price, 0.0000000001);

			page = client.getPage(new File("test-web-cache/preloved_search_abc_page2.html").toURI().toURL());
			resultRows = (HtmlElement) prelovedScraper.getResultRows(page).get(11);
			price = prelovedScraper.getPrice(resultRows);
			assertEquals(0.0, price, 0.0000000001);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
