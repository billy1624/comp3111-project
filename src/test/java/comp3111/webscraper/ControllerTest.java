package comp3111.webscraper;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


import org.junit.Test;

import comp3111.webscraper.Controller.DataModel;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    	method2.setAccessible(true);    	
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
    
    @Test // fuck my life
    public void test_closeAndResetAll() throws Exception {
    	Controller test = new Controller();
    	// ready
        final Field field = test.getClass().getDeclaredField("textAreaConsole");
        field.setAccessible(true);
        TextArea result_ta = new TextArea("123");       
        field.set(test, result_ta);        

        final Field field1 = test.getClass().getDeclaredField("textFieldKeyword");
        field1.setAccessible(true);
        TextField result_tf = new TextField("123");  
        field1.set(test, result_tf);   
        
        final Field field2 = test.getClass().getDeclaredField("labelCount");
        field2.setAccessible(true);
        Label result_lbc = new Label("123");  
        field2.set(test, result_lbc);   
        
        final Field field3 = test.getClass().getDeclaredField("labelPrice");
        field3.setAccessible(true);
        Label result_lbp = new Label("123");  
        field3.set(test, result_lbp);   
        
        
        final Field field4 = test.getClass().getDeclaredField("labelMin");
        field4.setAccessible(true);
        Hyperlink result_lbm = new Hyperlink();
        result_lbm.setText("123");        
        field4.set(test, result_lbm);   
        
        final Field field5 = test.getClass().getDeclaredField("labelLatest");
        field5.setAccessible(true);
        Hyperlink result_lbl = new Hyperlink();  
        result_lbl.setText("123");        
        field5.set(test, result_lbl);
        
        
        final ObservableList<DataModel> data = FXCollections.observableArrayList(
    		    new DataModel("Title1", "0.0", "item1@example.com","01/02/2018"),
    		    new DataModel("Title2", "10.0", "item2@example.com","03/04/2018"),
    		    new DataModel("Title3", "20.0", "item3@example.com","05/06/2018"),
    		    new DataModel("Title4", "100.0", "item4@example.com","07/08/2018"),
    		    new DataModel("Title5", "500.0", "item5@example.com","09/10/2018")
    		);
        final Field field6 = test.getClass().getDeclaredField("tableView");
        field6.setAccessible(true);
        TableView result_tbw = new TableView();  
        result_tbw.setItems(data);
        field6.set(test, result_tbw);
        
        final String it1 = "Item1";
    	final String it2 = "Item2";
    	final String it3 = "Item3";
    	final String it4 = "Item4";
    	final String it5 = "Item5";
    	Series<String, Double> series1 = new Series<String, Double>();
        final Field field7 = test.getClass().getDeclaredField("barChartHistogram");
        field7.setAccessible(true);
    	NumberAxis yaxis = new NumberAxis();
    	CategoryAxis xaxis = new CategoryAxis();
        BarChart result_bc = new BarChart(xaxis, yaxis);     
        result_bc.getData().addAll(series1);
        field7.set(test, result_bc);
        
        Series<String, Number> series2 = new XYChart.Series<String, Number>();
    	series2.setName("Test Data Set");
    	series2.getData().add(new Data<String, Number>("a", 4));
    	series2.getData().add(new Data<String, Number>("b", 10));
    	series2.getData().add(new Data<String, Number>("c", 15));
    	series2.getData().add(new Data<String, Number>("d", 8));
    	series2.getData().add(new Data<String, Number>("e", 5));
    	series2.getData().add(new Data<String, Number>("f", 18));
    	series2.getData().add(new Data<String, Number>("g", 15));
    	series2.getData().add(new Data<String, Number>("h", 13));
    	series2.getData().add(new Data<String, Number>("i", 19));
    	series2.getData().add(new Data<String, Number>("j", 21));
    	series2.getData().add(new Data<String, Number>("k", 21));
        final Field field8 = test.getClass().getDeclaredField("areaChart");
        field8.setAccessible(true);
        NumberAxis yaxis1 = new NumberAxis();
    	CategoryAxis xaxis1 = new CategoryAxis();
        AreaChart result_ac = new AreaChart(xaxis1, yaxis1);     
        result_ac.getData().addAll(series2);
        field8.set(test, result_ac);
        
        
        final Field field9 = test.getClass().getDeclaredField("areaChartCb");
        field9.setAccessible(true);
        ComboBox result_cbx = new ComboBox();     
        result_cbx.getItems().addAll(
    		    "Option 1",
    		    "Option 2",
    		    "Option 3"
    		);
        field9.set(test, result_cbx);
        
        final Field field10 = test.getClass().getDeclaredField("refineBt");
        field10.setAccessible(true);
        Button result_rbt = new Button();     
        result_rbt.setDisable(false);
        field10.set(test, result_rbt);       
        
        final Field field11 = test.getClass().getDeclaredField("lastSearchBt");
        field11.setAccessible(true);
        MenuItem result_lsbt = new MenuItem();     
        result_lsbt.setDisable(false);
        field11.set(test, result_lsbt);      
        
        final Field field12 = test.getClass().getDeclaredField("busy_idtr");
        field12.setAccessible(true);
        ProgressIndicator result_bsidtr = new ProgressIndicator();     
        result_bsidtr.setVisible(true);
        field12.set(test, result_bsidtr); 
        
        
        
        // test initialize
        Method method = null;
		method = test.getClass().getDeclaredMethod("initialize", null);		
    	method.setAccessible(true);    	   
		method.invoke(test, null);        
        
        Button current_rbt = (Button)field10.get(test);
        MenuItem current_lsbt = (MenuItem)field11.get(test);
        ProgressIndicator current_bsidtr = (ProgressIndicator)field12.get(test);    

        
        assertEquals(current_rbt.isDisable(), true);
        assertEquals(current_lsbt.isDisable(), true);
        assertEquals(current_bsidtr.isVisible(), false);



        
        
        // execute target test function
        test.closeAndResetAll();
        
        // assert result
        TextArea current_ta = (TextArea)field.get(test);     
        TextField current_tf = (TextField)field1.get(test);   
        Label current_lbc = (Label)field2.get(test);
        Label current_lbp = (Label)field3.get(test);   
        Hyperlink current_lbm = (Hyperlink)field4.get(test);   
        Hyperlink current_lbl = (Hyperlink)field5.get(test);   
        TableView current_tbw = (TableView)field6.get(test);   
        BarChart current_bc = (BarChart)field7.get(test);
        AreaChart current_ac = (AreaChart)field8.get(test);
        ComboBox current_cbx = (ComboBox)field9.get(test);


        assertEquals(current_ta.getText(), "");
        assertEquals(current_tf.getText(), "");
        assertEquals(current_lbc.getText(), "<total>");
        assertEquals(current_lbp.getText(), "<AvgPrice>");
        assertEquals(current_lbm.getText(), "<Lowest>");
        assertEquals(current_lbl.getText(), "<Latest>");
        assertEquals(current_tbw.getItems().size(), 0);
        assertEquals(current_bc.getData().size(), 0);
        assertEquals(((ValueAxis<Number>) current_bc.getYAxis()).getUpperBound(), 125.0, 0.001);
        assertEquals(((ValueAxis<Number>) current_bc.getYAxis()).getLowerBound(), 0.0, 0.001);
        assertEquals(current_ac.getData().size(), 0);
        assertEquals(((ValueAxis<Number>) current_ac.getYAxis()).getUpperBound(), 110.0, 0.001);
        assertEquals(((ValueAxis<Number>) current_ac.getYAxis()).getLowerBound(), 0.0, 0.001);
        assertEquals(current_cbx.getItems().size(), 0);
        assertEquals(current_rbt.isDisable(), true);



    	
    }
    
}