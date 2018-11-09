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
		
		/* postedd */
		try {
			Class progClass = data.getClass();
			Field postedd_field = DataModel.class.getDeclaredField("postedd");
			postedd_field.setAccessible(true);
			SimpleStringProperty x = (SimpleStringProperty) postedd_field.get(data);
	        SimpleStringProperty src = new SimpleStringProperty();
	        x.bind(src);
	        String expected = "01/01/2001";
			src.set(expected);
	        String actual = data.getPostedd();
	        assertThat(actual,is(expected));

		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        /* price */
		try {
			Class progClass = data.getClass();
			Field price_field = DataModel.class.getDeclaredField("price");
			price_field.setAccessible(true);
			SimpleStringProperty x = (SimpleStringProperty) price_field.get(data);
	        SimpleStringProperty src = new SimpleStringProperty();
	        x.bind(src);
	        String expected = "55.6";
			src.set(expected);
	        String actual = data.getPrice();
	        assertThat(actual,is(expected));

		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        /* title */
		try {
			Class progClass = data.getClass();
			Field title_field = DataModel.class.getDeclaredField("title");
			title_field.setAccessible(true);
			SimpleStringProperty x = (SimpleStringProperty) title_field.get(data);
	        SimpleStringProperty src = new SimpleStringProperty();
	        x.bind(src);
	        String expected = "Hello World";
			src.set(expected);
	        String actual = data.getTitle();
	        assertThat(actual,is(expected));

		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        /* url */
		try {
			Class progClass = data.getClass();
			Field url_field = DataModel.class.getDeclaredField("url");
			url_field.setAccessible(true);
			SimpleStringProperty x = (SimpleStringProperty) url_field.get(data);
	        SimpleStringProperty src = new SimpleStringProperty();
	        x.bind(src);
	        String expected = "www.google.com";
			src.set(expected);
	        String actual = data.getUrl();
	        assertThat(actual,is(expected));

		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
