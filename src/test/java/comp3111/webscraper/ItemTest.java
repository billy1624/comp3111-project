package comp3111.webscraper;


import org.junit.Test;
import static org.junit.Assert.*;


public class ItemTest {

	@Test
	public void testSetTitle() {
		Item i = new Item();
		i.setTitle("ABCDE");
		assertEquals(i.getTitle(), "ABCDE");
	}
	@Test
	public void testSetPrice() {
		Item i = new Item();
		i.setPrice(12345.6);
		assertEquals(i.getPrice(), 12345.6, 0.001);
	}
	@Test
	public void testSetUrl() {
		Item i = new Item();
		i.setUrl("www.google.com");
		assertEquals(i.getUrl(), "www.google.com");
	}
}
