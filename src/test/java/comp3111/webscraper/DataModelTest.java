package comp3111.webscraper;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import javafx.beans.property.SimpleStringProperty;

import comp3111.webscraper.Controller.DataModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataModelTest {

	@Test
	public void dataModel_data_test() {
		DataModel data = new DataModel("Title1", "0.0", "item1@example.com","01/02/2018");
		assertEquals(data.getPostedd(), "01/02/2018");
		assertEquals(data.getTitle(), "Title1");
		assertEquals(data.getPrice(), "0.0");
		assertEquals(data.getUrl(), "item1@example.com");		
	}
	
	@Test
	public void dataBinding_test() {
		DataModel data = new DataModel("Title1", "0.0", "item1@example.com","01/02/2018");
		Class progClass = data.getClass();
		
		/* postedd */
		Field postedd_field = null;		
		SimpleStringProperty x;
        SimpleStringProperty src1 = new SimpleStringProperty();
            
		try {
			postedd_field = progClass.getDeclaredField("postedd");
			x =  (SimpleStringProperty) postedd_field.get(this);
	        x.bind(src1);

		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		postedd_field.setAccessible(true);
        String expected = "01/01/2001";
		data.setPostedd(expected);
        
		src1.set(expected);
        String actual = data.getPostedd();
        assertThat(actual,is(expected));
        
        /* price */
		Field price_field = null;		
        SimpleStringProperty y;
        SimpleStringProperty src2 = new SimpleStringProperty();

		try {
			price_field = progClass.getDeclaredField("price");
			y =  (SimpleStringProperty) price_field.get(this);
	        y.bind(src2);

		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		price_field.setAccessible(true);
        expected = "5.5";
		data.setPrice(expected);
        
		src2.set(expected);
        actual = data.getPrice();
        assertThat(actual,is(expected));
        
        /* title */
        Field title_field = null;		
        SimpleStringProperty z;
        SimpleStringProperty src3 = new SimpleStringProperty();

		try {
			title_field = progClass.getDeclaredField("title");
			z =  (SimpleStringProperty) price_field.get(this);
	        z.bind(src3);

		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		title_field.setAccessible(true);
        expected = "hi";
		data.setTitle(expected);
        
		src3.set(expected);
        actual = data.getTitle();
        assertThat(actual,is(expected));
        
        /* url */
        Field url_field = null;		
        SimpleStringProperty a;
        SimpleStringProperty src4 = new SimpleStringProperty();

		try {
			url_field = progClass.getDeclaredField("url");
			a =  (SimpleStringProperty) price_field.get(this);
	        a.bind(src4);

		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		url_field.setAccessible(true);
        expected = "www.google.com";
		data.setUrl(expected);
        
		src4.set(expected);
        actual = data.getUrl();
        assertThat(actual,is(expected));


	}

}
