package comp3111.webscraper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class nganhei_test {

	
	@Test
	public void testSetTitle() {
		Item i = new Item();
		i.setTitle("ABCDE");
		assertEquals(i.getTitle(), "ABCDE");
	}
}