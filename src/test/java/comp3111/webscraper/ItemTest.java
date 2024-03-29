package comp3111.webscraper;


import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;


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
	
	@Test
	public void testSetPortal() {
		Item i = new Item();
		i.setPortal(Portal.Preloved);
		assertEquals(i.getPortal(),Portal.Preloved);
	}
	
	@Test
	public void testSetPostedOn() {
		Item i = new Item();
		Date expected = new Date(20181111);
		i.setPostedOn(expected);
		assertEquals(i.getPostedOn(),new Date(20181111));
	}
	
	@Test
	public void test_toString() throws ParseException {
		String date = "1970-01-01 13:36";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		sdf.setTimeZone(TimeZone.getTimeZone("HKT"));
		Date d = sdf.parse(date);
		Item i = new Item("01", 0.0, Portal.Craigslist, "url", d);
		assertEquals(i.getPostedOn(),d);
		assertEquals(i.getPortal(),Portal.Craigslist);
		assertEquals(i.getUrl(), "url");
		assertEquals(i.getPrice(), 0.0, 0.001);
		assertEquals(i.getTitle(), "01");
		String result = i.toString();
		assertEquals(result, "Item{title='01', price=0.0, portal=Craigslist, url='url', postedOn="+ d +"}");
		
		Item b = new Item("02", 5.0, Portal.Craigslist, "url", new Date(20181111));
				
		int larger = i.compareTo(b);
		assertEquals(larger, -1);


	}
}
