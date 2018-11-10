package comp3111.webscraper;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ControllerTest {
	
    private static Controller controller;
    private static FXMLLoader  loader ;
    private static Parent root;
    private static Scene scene;
 
	
	 public static class MyApp extends Application {
	    @Override
	    public void start(Stage primaryStage) throws Exception {
	    	loader = new FXMLLoader();
	    	//loader.setLocation(WebScraperApplication.class.getResource("/ui.fxml"));
	        root = loader.load();
	   		scene = new Scene(root);
	   		controller = loader.getController();
	   		controller.setHostServices(getHostServices());
	   		assertNotNull(controller);
	    	primaryStage.getIcons().add(new Image( WebScraperApplication.class.getResource("/java-icon.png").toString()));    	
	    	primaryStage.setScene(scene);
	    	primaryStage.setTitle("WebScrapper");
	    	primaryStage.show();   		
	    }
	 }
    
	@Test
	public void test_closeAndResetAll() {
		Thread t = new Thread("JavaFX Init Thread") {
	        public void run() {
	          Application.launch(MyApp.class);
	        }
	      };
	      t.setDaemon(true);
	      t.start();
	}

}