package comp3111.webscraper;

import static org.junit.Assert.*;

import org.junit.Test;

public class PortalTest {

	@Test
	public void test() {
		assertEquals("Craigslist", Portal.Craigslist.name());
		assertEquals("Preloved", Portal.Preloved.name());
	}

}
