package comp3111.webscraper;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Vector;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextArea;

@Ignore
public class WebScraperTest {

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
		WebScraper webScraper = new WebScraper();
		List<Item> itemList;
		TextArea textAreaConsole = new TextArea("");

		itemList = webScraper.scrape("abc", textAreaConsole);
		assertEquals(Vector.class, itemList.getClass());

		itemList = webScraper.scrape("google pixel 5", textAreaConsole);
		assertEquals(Vector.class, itemList.getClass());

		itemList = webScraper.scrape("NoResultFound...", textAreaConsole);
		assertEquals(Vector.class, itemList.getClass());
	}


	@Test
	public void itemComparatorTest() {
		// List<Item> sortedItemList = new Vector<>();
		// sortedItemList.add(new Item("Title", 0.0, Portal.Preloved, "url", new Date(0)));
		// sortedItemList.add(new Item("Title", 1.0, Portal.Preloved, "url", new Date(0)));
		// sortedItemList.add(new Item("Title", 2.0, Portal.Craigslist, "url", new Date(0)));
		// sortedItemList.add(new Item("Title", 2.0, Portal.Preloved, "url", new Date(0)));
		//
		// List<Item> unsortedItemList1= new Vector<>();
		// unsortedItemList1.add(new Item("Title", 0.0, Portal.Preloved, "url", new Date(0)));
		// unsortedItemList1.add(new Item("Title", 2.0, Portal.Preloved, "url", new Date(0)));
		// unsortedItemList1.add(new Item("Title", 1.0, Portal.Preloved, "url", new Date(0)));
		// unsortedItemList1.add(new Item("Title", 2.0, Portal.Craigslist, "url", new Date(0)));
		// unsortedItemList1.sort(new ItemComparator());
		// for (int i=0; i<sortedItemList.size(); i++) {
		// 	assert(sortedItemList.get(i), unsortedItemList1.get(i));
		// }
		//
		// List<Item> unsortedItemList2= new Vector<>();
		// unsortedItemList2.add(new Item("Title", 2.0, Portal.Preloved, "url", new Date(0)));
		// unsortedItemList2.add(new Item("Title", 2.0, Portal.Craigslist, "url", new Date(0)));
		// unsortedItemList2.add(new Item("Title", 1.0, Portal.Preloved, "url", new Date(0)));
		// unsortedItemList2.add(new Item("Title", 0.0, Portal.Preloved, "url", new Date(0)));
		// unsortedItemList2.sort(new ItemComparator());
		// for (int i=0; i<sortedItemList.size(); i++) {
		// 	assertEquals(sortedItemList.get(i), unsortedItemList1.get(i));
		// }

	}

}
