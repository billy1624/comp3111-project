package comp3111.webscraper;

import static org.junit.Assert.assertEquals;


import com.gargoylesoftware.css.parser.javacc.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class nganhei_test {
	
	@Test
	 /**
     * Test the comparison of the date
     * @author Ngan Cheuk Hei - chnganaa
     */ 
    public void DateTest() throws ParseException, java.text.ParseException {
    	Controller test = new Controller();

    	String date1 = "2018-09-09 23:55";
    	String date2 = "2018-09-09 23:55";
    	String date3 = "2018-09-10 00:00";
    	String date4 = "2018-09-08 00:00";
    	assertEquals(test.DateCompare(date1, date2), 0);
    	assertEquals(test.DateCompare(date1, date3), -1);
    	assertEquals(test.DateCompare(date1, date4), 1);

    }
	
	
	
}
