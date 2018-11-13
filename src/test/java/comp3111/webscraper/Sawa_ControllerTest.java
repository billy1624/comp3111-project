package comp3111.webscraper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import comp3111.webscraper.Controller.BarChart_BarDBClick_Handler;
import comp3111.webscraper.Controller.BarChart_BarEntered_Handler;
import comp3111.webscraper.Controller.BarChart_BarExited_Handler;
import comp3111.webscraper.Controller.DataModel;
import comp3111.webscraper.Controller.SearchAsyncTask;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Sawa_ControllerTest {	   
	/**
	 * Dummy class for testing HostServices
	 * It must inherit Application class
	 * @author Sawa
	 *
	 */
	MyTask task;
	Thread myTaskThread; 
	Controller tmp;
    public class MyTask extends Application{    	
		@Override
		public void start(Stage primaryStage) throws Exception {
			// Dummy
			FXMLLoader loader = new FXMLLoader();
        	loader.setLocation(WebScraperApplication.class.getResource("/ui.fxml"));
       		VBox root = (VBox) loader.load();
       		Scene scene =  new Scene(root);
       		tmp = (Controller)loader.getController();        	
		}				
    	
    }
    
    /**
     * Jobs that calls before every test case
     * Purpose: create a new JavaFx UI Thread for GUI related testing   
     * @author Sawa
     */    
    @BeforeClass
    public static void javaFxThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel(); // Initializes the JavaFx Platform
            }
        });
        // start the thread
        thread.start(); 
    }
    	          
    @Test
    public void test_application() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel(); // Initializes the JavaFx Platform
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
							try {
								new WebScraperApplication().start(new Stage());				
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
    	List<Boolean> list = new ArrayList<>(); // set1 has bar has been selected
    	List<Boolean> list2 = new ArrayList<>(); // set2 no bar has been selected
    	list.add(true);
    	list2.add(false);
    	
    	// use reflection to get private data member
    	Field field = test.getClass().getDeclaredField("bar_smVector");
    	field.setAccessible(true);
    	field.set(test, list);
    	
    	Field field2 = test2.getClass().getDeclaredField("bar_smVector");
    	field2.setAccessible(true);
    	field2.set(test2, list2);
    	
    	// use reflection to get private member funtion
    	Method method = null;
    	Method method2 = null;
		method = test.getClass().getDeclaredMethod("has_bar_selected", (Class<?>[])null);
		method2 = test2.getClass().getDeclaredMethod("has_bar_selected", (Class<?>[])null);		
    	method.setAccessible(true);    	
    	method2.setAccessible(true);    	
    	Boolean result1;
    	Boolean result2;
		result1 = (Boolean) method.invoke(test, (Object[])null);
		result2 = (Boolean) method.invoke(test2,(Object[]) null);

		// validate result
		assertEquals(result1, true);
    	assertEquals(result2, false);		
    }
    
    @Test
    public void test_setHostServices() throws Exception {
    	Controller test = new Controller();
    	MyTask a = new MyTask();
    	HostServices target = a.getHostServices();

        // Get a host services first
        test.setHostServices(target);
         
        final Field field = test.getClass().getDeclaredField("hservices");
        field.setAccessible(true);
        
        // validate result
        assertEquals(field.get(test), target);
    }
    
    @Test
    public void test_updateLastSearchItem() throws Exception {
    	/*** Create all the things that required for the test ***/
    	
    	// controller init
    	Controller test = new Controller();
    	
    	// updateLastSearch_Item init
    	Method method = null;
  		method = test.getClass().getDeclaredMethod("updateLastSearch_Item", List.class);		
      	method.setAccessible(true);      	
      	
  		// recordItem instance init
		final Field recorditm = test.getClass().getDeclaredField("recordItem");
		recorditm.setAccessible(true);
		
		// create 2 item, add to List<Item>
		List<Item> testlist = new ArrayList<Item>();
		Item item0 = new Item();
		item0.setPrice(50);
		item0.setTitle("1");
		item0.setUrl("www.testing.com");
		
		Item item1 = new Item();
		item1.setPrice(33.3);
		item1.setTitle("2");
		item1.setUrl("www.testing.com");
		testlist.add(item0);
		testlist.add(item1);
		recorditm.set(test, testlist);
         
		final Field q1 = test.getClass().getDeclaredField("lastSearchItemQueue");
		q1.setAccessible(true);
		Queue<List<Item>> queue = (Queue<List<Item>>) q1.get(test);
				         
    	/** 
    	 *  Execute the test method
    	 *  When it it called, the queue "lastSearchItemQueue" 
    	 *  will append the recordItem into the queue.
    	 *  Assert it is not empty, size equal to 1    	
    	 **/
   		method.invoke(test, testlist);   
   		assertEquals(queue.size(), 2);
   		
   		// add one more items
		Item one_more = new Item();
		one_more.setPrice(99.9);
		one_more.setTitle("3");
		one_more.setUrl("www.testing.com");
		testlist.add(one_more);
		recorditm.set(test, testlist);
		
   		method.invoke(test, testlist);   	
   		assertEquals(queue.size(), 2);
    }
    
    @Test
    public void test_aboutUsDialog() {
    	Platform.runLater(new Runnable() {
    	    @Override
    	    public void run() {
    	        // Update UI here.
    	    	Exception e = null;
    	    	try {
			    	Controller test = new Controller();
					test.handleAboutYourTeamAction(new ActionEvent());
    	    	}catch(Exception ex) {
    	    		e = ex;
    	    	}
    	    	
    	    	// validate not exception throw
    	    	assertEquals(e, null);    	    	
    	    }
    	});    	
    }
    
    @Test
    public void test_actionSearch() throws Exception{
    	Controller test = new Controller();
    	Method method = null;
  		method = test.getClass().getDeclaredMethod("actionSearch",(Class<?>[]) null);		
      	method.setAccessible(true);
      	
      	final Field field12 = test.getClass().getDeclaredField("busy_idtr");
        field12.setAccessible(true);
        ProgressIndicator result_bsidtr = new ProgressIndicator();     
        result_bsidtr.setVisible(true);
        field12.set(test, result_bsidtr); 
      	
        final Field tfkeyword = test.getClass().getDeclaredField("textFieldKeyword");
 		tfkeyword.setAccessible(true);
 		TextField tf = new TextField();		
 		tfkeyword.set(test, tf);
      	      	
   		method.invoke(test, (Object[])null);
   		ProgressIndicator a = (ProgressIndicator) field12.get(test);
   		assertEquals(a.isVisible(), true);
    }
  
    @Test // fuck my life
    public void test_javafx_thread_releated() throws Exception {
    	Controller test = new Controller();
    	// ready, initialize all data member and using Java reflection to do assertion
        final Field field = test.getClass().getDeclaredField("textAreaConsole");
        field.setAccessible(true);
        TextArea result_ta = new TextArea("123");       
        field.set(test, result_ta);        

        final Field field1 = test.getClass().getDeclaredField("textFieldKeyword");
        field1.setAccessible(true);
        TextField result_tf = new TextField("google pixel");  
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
    	series1.getData().add(new Data("50.0",50.0));
        final Field field7 = test.getClass().getDeclaredField("barChartHistogram");
        field7.setAccessible(true);
    	NumberAxis yaxis = new NumberAxis();
    	CategoryAxis xaxis = new CategoryAxis();
        BarChart result_bc = new BarChart(xaxis, yaxis);     
        result_bc.getData().addAll(series1);
        field7.set(test, result_bc);
        
                
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
        
        final Field title_col_field = test.getClass().getDeclaredField("title_col");
        title_col_field.setAccessible(true);
        TableColumn<DataModel, String> title_col = new TableColumn<DataModel, String>();
        title_col_field.set(test, title_col);
        
        final Field price_col_field = test.getClass().getDeclaredField("price_col");
        price_col_field.setAccessible(true);
        TableColumn<DataModel, String> price_col = new TableColumn<DataModel, String>();
        price_col_field.set(test, price_col);
        
        final Field url_col_field = test.getClass().getDeclaredField("url_col");
        url_col_field.setAccessible(true);
        TableColumn<DataModel, String> url_col = new TableColumn<DataModel, String>();
        url_col_field.set(test, url_col);
        
        final Field posted_date_col_field = test.getClass().getDeclaredField("posted_date_col");
        posted_date_col_field.setAccessible(true);
        TableColumn<DataModel, String> posted_date_col = new TableColumn<DataModel, String>();
        posted_date_col_field.set(test, posted_date_col);
        
        final Field field8 = test.getClass().getDeclaredField("areaChart");
        field8.setAccessible(true);
        NumberAxis yaxis1 = new NumberAxis();
    	CategoryAxis xaxis1 = new CategoryAxis();
        AreaChart result_ac = new AreaChart(xaxis1, yaxis1);     
        field8.set(test, result_ac);
        
        /* test initialize
         * This funciton init some of javafx components's default state 
         */
        Method method = null;
		method = test.getClass().getDeclaredMethod("initialize",(Class<?>[]) null);		
    	method.setAccessible(true);    	   
		method.invoke(test, (Object[])null);        
        
        Button current_rbt = (Button)field10.get(test);
        MenuItem current_lsbt = (MenuItem)field11.get(test);
        ProgressIndicator current_bsidtr = (ProgressIndicator)field12.get(test);    
        
        assertEquals(current_rbt.isDisable(), true);
        assertEquals(current_lsbt.isDisable(), true);
        assertEquals(current_bsidtr.isVisible(), false);

        /* test update_bar_only_one() */
        Method method1 = null;
		method1 = test.getClass().getDeclaredMethod("update_bar_only_one", Integer.class, List.class, Double.class, Double.class);		
		method1.setAccessible(true);    
		
        
        final Field field21 = test.getClass().getDeclaredField("bar_smVector");
        field21.setAccessible(true);
        
    	List<Item> list = new ArrayList<>();
    	Item item = new Item();
    	item.setPrice(50);
    	item.setTitle("01");
    	item.setUrl("www.testing.com");
    	list.add(item); 
    	
    	List<Item> list2 = new ArrayList<>();
    	Item item2 = new Item();
    	item2.setPrice(500.0);
    	item2.setTitle("02");
    	item2.setUrl("www.testing.com");
    	list2.add(item2);
    	
    	List<Item> list3 = new ArrayList<>();
    	Item item3 = new Item();
    	item3.setPrice(-11.0);
    	item3.setTitle("03");
    	item3.setUrl("www.testing.com");
    	list3.add(item3);
    	
    	List<Boolean> boollist = new ArrayList<>();
    	boollist.add(true);    	
    	field21.set(test, boollist);
            	    	
		method1.invoke(test, 0, list, 0.0, 100.0);
		/* assertion explanation
		 * TextArea will not equal to empty, 
		 * since variable item's price is 50, and that set to the list of items, 
		 * where fall between 0.0 to 100.0,
		 * so textArea will show the information of 'item'
		 */
		assertNotEquals(result_ta.getText(), "");
		
		List<Boolean> boollist2 = new ArrayList<>();
    	boollist2.add(false);    	
    	field21.set(test, boollist);
    	method1.invoke(test, 0, list2, 50.0, 100.0);
		assertEquals(result_ta.getText(), "");
		
		method1.invoke(test, 0, list3, 50.0, 100.0);
		assertEquals(result_ta.getText(), "");
                     
		final Field th1 = test.getClass().getDeclaredField("scraperTh");
		th1.setAccessible(true);
		Thread thh = new Thread();
		th1.set(test, thh);
		
		/* execute the method that required to test
         * Reset all components 
         */
        test.closeAndResetAll();
        
        // assert result: check if all close and clear
        TextArea current_ta = (TextArea)field.get(test);     
        TextField current_tf = (TextField)field1.get(test);   
        Label current_lbc = (Label)field2.get(test);
        Label current_lbp = (Label)field3.get(test);   
        Hyperlink current_lbm = (Hyperlink)field4.get(test);   
        Hyperlink current_lbl = (Hyperlink)field5.get(test);   
        TableView current_tbw = (TableView)field6.get(test);   
        BarChart current_bc = (BarChart)field7.get(test);
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
        assertEquals(current_cbx.getItems().size(), 0);
        assertEquals(current_rbt.isDisable(), true);    
        
        /* test_updateLastSearchKeyword */
        Method method11 = null;
  		method11 = test.getClass().getDeclaredMethod("updateLastSearch_Keyword",(Class<?>[]) null);		
      	method11.setAccessible(true);
  		// get ready for instance
   		final Field q2 = test.getClass().getDeclaredField("lastSearchQueue");
		q2.setAccessible(true);
		Queue<List<Item>> ls_keyword_queue = (Queue<List<Item>>) q2.get(test);
				         
		final Field tfkeyword = test.getClass().getDeclaredField("textFieldKeyword");
		tfkeyword.setAccessible(true);
		TextField tf = new TextField("google pixel");		
		tfkeyword.set(test, tf);
		/*
    	 *  Execute the test method
    	 *  When it it called, the queue "lastSearchQueue" 
    	 *  will append the keyword into the queue.
    	 *  Assert it is not empty, size equal to 2 (when it reach 3, it poll the head)    	
    	 */
   		method11.invoke(test, (Object[])null);
   		method11.invoke(test, (Object[])null);
   		method11.invoke(test, (Object[])null);

   		// assertion
   		assertEquals(ls_keyword_queue.size(), 2);
   		
   		/* test actionLastSearch */
   		Method lastsearch_func = null;
   		lastsearch_func = test.getClass().getDeclaredMethod("actionLastSearch", (Class<?>[])null);		
   		lastsearch_func.setAccessible(true);   
   		lastsearch_func.invoke(test, (Object[])null);   		
         
		final Field q1 = test.getClass().getDeclaredField("lastSearchItemQueue");
		q1.setAccessible(true);
		Queue<List<Item>> lsIQ = new LinkedList<List<Item>>();
		List<Item> list4 = new ArrayList<>();
    	Item item4 = new Item();
    	item4.setPrice(50);
    	item4.setTitle("01");
    	item4.setUrl("www.testing.com");
    	list4.add(item4);
		lsIQ.offer(list4);
		q1.set(test,lsIQ);
         
         // execute test method
   		lastsearch_func.invoke(test, (Object[])null);
   		
   		
   		/* test actionRefine */
   		final Field recorditm = test.getClass().getDeclaredField("recordItem");
		recorditm.setAccessible(true);
		List<Item> testlist = new ArrayList<Item>();
		Item item5 = new Item();
		item5.setPrice(50);
		item5.setTitle("01");
		item5.setUrl("www.testing.com");
		Item item6 = new Item();
		item6.setPrice(50);
		item6.setTitle("123");
		item6.setUrl("www.testing.com");
		recorditm.set(test, testlist);
		
		
   		Method refine_func = null;
   		refine_func = test.getClass().getDeclaredMethod("actionRefine", (Class<?>[])null);		
   		refine_func.setAccessible(true);
   		// null
		final Field bool1 = test.getClass().getDeclaredField("refine_lastSearch");
		bool1.setAccessible(true);
		bool1.set(test, false);
   		refine_func.invoke(test, (Object[])null);
   		
   		// one, match
		bool1.set(test, true);
		testlist.add(item5);
   		refine_func.invoke(test,(Object[]) null);
   		
   		// one unmatch
   		TextField kwtf = (TextField) field1.get(test);
   		kwtf.setText("123");
		testlist.add(item6);
   		refine_func.invoke(test,(Object[]) null);
   		
   		/* test github event handler */
   		MyTask a = new MyTask();
    	HostServices target = a.getHostServices();

        //when
        test.setHostServices(target);

        //then
        final Field myfield = test.getClass().getDeclaredField("hservices");
        myfield.setAccessible(true);
        assertEquals(myfield.get(test), target);
        
        /* test GitHub EventHandler */
   		Hyperlink hl = new Hyperlink();
    	hl.setText("sawaYch");
    	hl.setOnAction(test.new github_EventHandler());
    	hl.fire();
    	
    	/* test barchart histogram */
    	Method barchart_update = test.getClass().getDeclaredMethod("UpdateDistributionChart_Later", List.class);		
    	barchart_update.setAccessible(true);
    	// prepare 3 set of list item
    	List<Item> barchart_testlist = new ArrayList<Item>();	
		for(int i = 0; i< 10; ++i) {
			Item m1 = new Item();
			m1.setPrice(50 + i);
			barchart_testlist.add(m1);
		}    	
    	barchart_update.invoke(test, barchart_testlist);
		
		List<Item> barchart_testlist2 = new ArrayList<Item>();	
		for(int i = 0; i< 10; ++i) {
			Item m1 = new Item();
			m1.setPrice(10-i);
			barchart_testlist2.add(m1);
		}    	
    	barchart_update.invoke(test, barchart_testlist2);    	

    	/* test barchart event handler */ 
    	for(int itr = 0; itr < series1.getData().size(); ++itr) {
    		series1.getData().get(itr).getNode().setId(Integer.toString(itr));
    		System.out.println(series1.getData().get(itr).getXValue());
    	}
    	
        result_bc.getData().addAll(series1);
		 for (Object serie: result_bc.getData()){
    		 for (Data<String, Integer> ittm: ((Series<String,Integer>) serie).getData()){
    			 
    			 BarChart_BarEntered_Handler beh_instance = test.new BarChart_BarEntered_Handler();
    			 beh_instance.setData(ittm);
    			 ittm.getNode().setOnMouseEntered(beh_instance);    			
    			 beh_instance.handle(null);
    			 
    			 BarChart_BarExited_Handler bExith_instance = test.new BarChart_BarExited_Handler();
    			 bExith_instance.setData(ittm);        			         			
    			 ittm.getNode().setOnMouseExited(bExith_instance);
    			 bExith_instance.handle(null);
    			 
    			 BarChart_BarDBClick_Handler bDBch_instance = test.new BarChart_BarDBClick_Handler();
    			 bDBch_instance.setData(ittm);
    			 bDBch_instance.setDistributionData(testlist);
    			 bDBch_instance.setRange(0.0);
    			 ittm.getNode().setOnMouseClicked(bDBch_instance);
    			 bDBch_instance.handle(new MouseEvent(MouseEvent.MOUSE_CLICKED,
    					   1024, 1024, 768, 768, MouseButton.PRIMARY, 2,
    					   true, true, true, true, true, true, true, true, true, true, null));
    			 
		    	List<Boolean> oneBarSelcetList = new ArrayList<>();
		    	List<Boolean> noBarSelectList = new ArrayList<>();
		    	oneBarSelcetList.add(true);
		    	noBarSelectList.add(false);
		    	
		    	Field smV1 = test.getClass().getDeclaredField("bar_smVector");
		    	smV1.setAccessible(true);
		    	smV1.set(test, oneBarSelcetList);
		    	beh_instance.handle(null);
		    	bExith_instance.handle(null);
		    	 bDBch_instance.handle(new MouseEvent(MouseEvent.MOUSE_CLICKED,
  					   0, 0, 0, 0, MouseButton.PRIMARY, 2,
  					   true, true, true, true, true, true, true, true, true, true, null));


		    	
		    	Field smV2 = test.getClass().getDeclaredField("bar_smVector");
		    	smV2.setAccessible(true);
		    	smV2.set(test, noBarSelectList);
		    	beh_instance.handle(null);
		    	bExith_instance.handle(null);
				bDBch_instance.handle(new MouseEvent(MouseEvent.MOUSE_CLICKED,
					   0, 0, 0, 0, MouseButton.PRIMARY, 2,
					   true, true, true, true, true, true, true, true, true, true, null));
				bDBch_instance.handle(new MouseEvent(MouseEvent.MOUSE_MOVED,
						   0, 0, 0, 0, MouseButton.MIDDLE, 2,
						   true, true, true, true, true, true, true, true, true, true, null));
				bDBch_instance.handle(new MouseEvent(MouseEvent.MOUSE_CLICKED,
						   0, 0, 0, 0, MouseButton.PRIMARY, 1,
						   true, true, true, true, true, true, true, true, true, true, null));
             }
         }
		 
    	
   		/* test quit */
   		Method method1111 = null;
		method1111 = test.getClass().getDeclaredMethod("quit", (Class<?>[])null);		
		method1111.setAccessible(true);    	     		
		// get ready for instance	
		method1111.invoke(test, (Object[])null);
    }
        
    @Test 
    public void test_javafx() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    	Controller test = new Controller();
    	// ready
        final Field field = test.getClass().getDeclaredField("textAreaConsole");
        field.setAccessible(true);
        TextArea result_ta = new TextArea("123");       
        field.set(test, result_ta);   
    }
    
    @Test
    public void test_AreaChart() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    	Controller test = new Controller();
    	
    	/* check if data can be added or not */
    	// create data set
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
        AreaChart original_ac = new AreaChart(xaxis1, yaxis1);     
        original_ac.getData().addAll(series2);
        
        // expected value
    	int expected_num = original_ac.getData().size();

    	// set it
        field8.set(test, original_ac);
        
        // Get it and assert
        AreaChart current_ac = (AreaChart)field8.get(test);        
        assertEquals(current_ac.getData().size(), expected_num);        
    }
        
    @Test
    public void test_Async() throws Exception{
    	/** async task test **/
    	Platform.runLater(new Runnable() {         
		    @Override
		    public void run() {
		    	Controller test = new Controller();
		    	SearchAsyncTask async_task = test.new SearchAsyncTask();
		    	try {
					async_task.call();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
    }
}	