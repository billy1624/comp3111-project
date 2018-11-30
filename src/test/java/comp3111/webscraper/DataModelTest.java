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
			data.setPostedd("01/01/2001");
			Field postedd_field = DataModel.class.getDeclaredField("postedd");
			postedd_field.setAccessible(true);
			SimpleStringProperty x = (SimpleStringProperty) postedd_field.get(data);
	        assertThat(x.get(),is("01/01/2001"));

		} catch (SecurityException e1) {
			e1.printStackTrace();
		}catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        
        
        /* price */
		try {
			data.setPrice("55.6");
			Field price_field = DataModel.class.getDeclaredField("price");
			price_field.setAccessible(true);
			SimpleStringProperty x = (SimpleStringProperty) price_field.get(data);
	        assertThat(x.get(),is("55.6"));

		} catch (SecurityException e1) {
			e1.printStackTrace();
		}catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
        /* title */
		try {
			data.setTitle("Hi");
			Field title_field = DataModel.class.getDeclaredField("title");
			title_field.setAccessible(true);
			SimpleStringProperty x = (SimpleStringProperty) title_field.get(data);
	        assertThat(x.get(),is("Hi"));

		} catch (SecurityException e1) {
			e1.printStackTrace();
		}catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        
        /* url */
		try {
			data.setUrl("www.google.com");
			Field url_field = DataModel.class.getDeclaredField("url");
			url_field.setAccessible(true);
			SimpleStringProperty x = (SimpleStringProperty) url_field.get(data);	      
	        assertThat(x.get(),is("www.google.com"));

		} catch (SecurityException e1) {
			e1.printStackTrace();
		}catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}


	}

}
