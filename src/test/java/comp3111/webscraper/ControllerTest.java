package comp3111.webscraper;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;


import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ControllerTest {
	
	Task task;
	Thread myTaskThread;
    
    public class Task extends Application{
    	
    	public Controller tmp;
		@Override
		public void start(Stage primaryStage) throws Exception {
			FXMLLoader loader = new FXMLLoader();
        	loader.setLocation(WebScraperApplication.class.getResource("/ui.fxml"));
       		VBox root = (VBox) loader.load();
       		Scene scene =  new Scene(root);
       		tmp = (Controller)loader.getController();       		
		}
		
		public Controller getController() {
			return tmp;
		}
    	
    }
    
    
    @Test
    public void test_controller() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel(); // Initializes the JavaFx Platform
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
							try {
								new Task().start(new Stage());
							} catch (Exception e) {
								e.printStackTrace();
							}					
						                                  
                    }
                });
            }
        });
        thread.start();// Initialize the thread    
    }
    
    @Test
    public void test_has_bar_selected() throws Exception {
    	Controller test = new Controller();
    	Controller test2 = new Controller();
    	List<Boolean> list = new ArrayList<>();
    	List<Boolean> list2 = new ArrayList<>();
    	list.add(true);
    	list2.add(false);
    	
    	Field field = test.getClass().getDeclaredField("bar_smVector");
    	field.setAccessible(true);
    	field.set(test, list);
    	
    	Field field2 = test2.getClass().getDeclaredField("bar_smVector");
    	field2.setAccessible(true);
    	field2.set(test2, list2);
    	
    	Method method = null;
    	Method method2 = null;
		method = test.getClass().getDeclaredMethod("has_bar_selected", null);
		method2 = test2.getClass().getDeclaredMethod("has_bar_selected", null);		
    	method.setAccessible(true);    	
    	Boolean result1;
    	Boolean result2;
		result1 = (Boolean) method.invoke(test, null);
		result2 = (Boolean) method.invoke(test2, null);
    	assertEquals(result1, true);
    	assertEquals(result2, false);
		
    }
    
    @Test
    public void test_setHostServices() throws Exception {
    	Controller test = new Controller();
    	Task a = new Task();
    	HostServices target = a.getHostServices();

        //when
        test.setHostServices(target);

        //then
        final Field field = test.getClass().getDeclaredField("hservices");
        field.setAccessible(true);
        assertEquals(field.get(test), target);
    }
    
}