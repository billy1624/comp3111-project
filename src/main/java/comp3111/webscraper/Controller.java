/**
 * 
 */
package comp3111.webscraper;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;





/**
 * 
 * @author kevinw
 *
 *
 * Controller class that manage GUI interaction. Please see document about JavaFX for details.
 * 
 */
public class Controller {

    @FXML 
    private Label labelCount; 

    @FXML 
    private Label labelPrice; 

    @FXML 
    private Hyperlink labelMin; 

    @FXML 
    private Hyperlink labelLatest; 

    @FXML
    private TextField textFieldKeyword;
    
    @FXML
    private TextArea textAreaConsole;
    
    @FXML
    private TableView<DataModel> tableView;
    
    @FXML
    private BarChart barChartHistogram;
    
    @FXML
    private AreaChart areaChart; 
    
    @FXML
    private ComboBox areaChartCb;
    
    @FXML
    private MenuItem lastSearchBt;
    
    @FXML
    private TableColumn<DataModel, String> title_col;
    
    @FXML
    private TableColumn<DataModel, String> price_col;
    
    @FXML
    private TableColumn<DataModel, String> url_col;
    
    @FXML
    private TableColumn<DataModel, String> posted_date_col;

    @FXML
    private Button refineBt;
    
    @FXML
    private ProgressIndicator busy_idtr;
    
    private WebScraper scraper;
    
    private HostServices hservices;
    
    private List<Item> recordItem;
        
    private List<Boolean> bar_smVector;
    
    private Queue<String> lastSearchQueue;
    
    private Queue<List<Item>> lastSearchItemQueue;
    
    private Thread scraperTh;
    
    private boolean refine_lastSearch;
            
    
    /* task 6 related data member */
    
    @FXML
    private MenuBar menuBar; 
    
    /**
     * Default controller
     */
    public Controller() {
    	scraper = new WebScraper();
    	
    	// init last search keyword, record item
    	lastSearchQueue = new LinkedList<String>();
    	lastSearchQueue.offer("NULL");
    	lastSearchItemQueue = new LinkedList<List<Item>>();
    	lastSearchItemQueue.offer(null);
    }

    /**
     * Default initializer. It is empty.
     */
    @FXML
    private void initialize() {
    	// load test case, for test
    	//task6_iii_testCase();
    	
    	// disable at the beginning
    	lastSearchBt.setDisable(true);
    	refineBt.setDisable(true);
    	busy_idtr.setScaleX(0.3);
    	busy_idtr.setScaleY(0.3);
    	busy_idtr.setVisible(false);
		barChartHistogram.getXAxis().setAnimated(false);
		barChartHistogram.getYAxis().setAnimated(true);
		barChartHistogram.setAnimated(true);
		bar_smVector = new ArrayList<Boolean>();
    }
    
    /*
     * Update your last search keyword, item to queue 
     * 
     */
    private void updateLastSearch_Keyword() {
    	lastSearchQueue.offer(textFieldKeyword.getText());
    	if (lastSearchQueue.size() == 3) lastSearchQueue.poll();  
    	return;
    }
    

	private void updateLastSearch_Item(List<Item> what_to_add) {
    	lastSearchItemQueue.offer(what_to_add);
    	if (lastSearchItemQueue.size() == 3) lastSearchItemQueue.poll();  
    }
    
    /**
     * Called when the search(i.e. Go) button is pressed.
     */
    @FXML
    private void actionSearch() {    	
    	busy_idtr.setVisible(true);

        // use Async
    	Task<List<Item>> task = new Task<List<Item>>() {
            @Override 
            protected List<Item> call() throws Exception {
            	
            	List<Item> result = scraper.scrape(textFieldKeyword.getText(), textAreaConsole);
        		System.out.println("actionSearch: " + textFieldKeyword.getText());            	
        		String output = "";
            	for (Item item : result) {
            		output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\n";
            	}
            	textAreaConsole.textProperty().unbind();
            	textAreaConsole.setText(output);
            	
            	// copy for further use
            	recordItem = new ArrayList<Item>(result);    
            	
            	// update distribution bar chart      
            	//UpdateDistributionChart_Later(recordItem);
            	
            	// update other page here
            	
                return null;
            }
        };
        
        task.setOnSucceeded(eventHandler->{
        	// enable last search function
        	lastSearchBt.setDisable(false);
        	refineBt.setDisable(false);   
        	busy_idtr.setVisible(false);
        	updateLastSearch_Item(recordItem);
        	updateLastSearch_Keyword();
        	refine_lastSearch = false;
        }
        );       
        
        scraperTh = new Thread(task);
        scraperTh.start();
        
    	
    }      
    
    
    /*
     * Check any bar is double clicked
     * 
     */
    private Boolean has_bar_selected() {
    	for(Boolean iter: bar_smVector)
    		if (iter == true) return iter;
		return false;
    }
    
    /*
     * Advance Task 1 helper function
     * If one bar selected, reset other
     * 
     */
    private void update_bar_only_one(Integer var, List<Item> _dist_data, Double ps, Double pe) {
    	// reset the one which is selected first
    	int myCounter = 0;
    	int targetIndex = 0;
    	for(Boolean itr: bar_smVector) {
    		if(itr == true) {
    			bar_smVector.set(myCounter, false);
        		Series<String,Integer> serie =  ((Series<String,Integer>) barChartHistogram.getData().get(0));
        		Data<String, Integer> item = serie.getData().get(myCounter);
        		targetIndex = myCounter;
    			item.getNode().setStyle("-fx-bar-fill: #f3622d;");
    		}
    		myCounter ++;
    	}
    	
    	String output = "";
    	for(Item itr:  _dist_data){
    		if (itr.getPrice() >= ps && itr.getPrice()< pe) {
        		output += itr.getTitle() + "\t" + itr.getPrice() + "\t" + itr.getUrl() + "\n";
    		}
    	}
    	textAreaConsole.textProperty().unbind();
    	textAreaConsole.setText(output);

    }
    
    public class github_EventHandler implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event) {
        	String text = ((Hyperlink)event.getSource()).getText();
        	hservices.showDocument("https://github.com/" + text);        	
        }
    }    
    
    /**
     * Called when the new button is pressed. Very dummy action - print something in the command prompt.
     * actionNew() actually is LastSearchAction
     * The result will revert to the last Search
     */
    @FXML
    private void actionLastSearch() {
    	lastSearchBt.setDisable(true);
    	refineBt.setDisable(false);

    	
    	String lastKeyword = lastSearchQueue.peek();
    	System.out.println("last Search : "+ lastKeyword);
    	textFieldKeyword.setText(lastKeyword); // set back to last search keyword
    	
    	if (lastSearchItemQueue.peek() == null) {
    		System.out.println("last search item queue EMPTY!");
    		closeAndResetAll();
    		return;
    	}
    	// Console page update
    	String output = "";
    	for (Item item : lastSearchItemQueue.peek()) {
    		output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\n";
    	}
    	textAreaConsole.setText(output);
    	refine_lastSearch = true;
    	/*
    	 * trigger: update of other page 
    	 */
    	// advance 1 distribution chart update
    	//UpdateDistributionChart_Later(lastSearchItemQueue.peek());
    	
    	// summary page update
    	
    	// table update

    	
    	
    }

    /**
     * Task 5 Refine Search function
     * Only extract the the data with certain keyword, title field(handle locally),
     * Refined result will NOT save as a search record
     * LastSearch/Current can refine one time only
     */   
    @FXML
    private void actionRefine() {
    	// after click once, disable
    	refineBt.setDisable(true);
    	
    	String output = "";
    	// if previous action is lastSearch, then use lastSearch Item List,
    	// otherwise previous action can only be actionSearch, then use current Item List;
    	Iterator<Item> iter = null;
    	if (refine_lastSearch == true) {
    		iter = lastSearchItemQueue.peek().listIterator();    	
    	}
    		else {
    			iter = recordItem.listIterator();
    		}
    	
    	for ( ; iter.hasNext(); ) {
    		Item item = iter.next();
    		if (item.getTitle().matches("(.*)"+ textFieldKeyword.getText() + "(.*)")) {
    			output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\n";
    	    }else
    	    	iter.remove();
    	}
    	textAreaConsole.setText(output);    	
    	/*
    	 * trigger: update of other page 
    	 */
    	// advance 1 distribution chart update
    	//UpdateDistributionChart_Later(recordItem);
    	
    	// summary page update
    	
    	// table update    	
    }
    
    public void createAboutUsDialog() {
    	Stage dialog = new Stage();
    	dialog.initStyle(StageStyle.DECORATED);
    	dialog.initModality(Modality.APPLICATION_MODAL);
    	dialog.getIcons().add(new Image( getClass().getResource("/java-icon.png").toString()));    	
    	dialog.setTitle("About Our Team");
    	
    	// add all information
        final Group sceneGroup = new Group();
        final Scene scene = new Scene(sceneGroup, Color.WHITESMOKE);
        
        // center title 
        Image deca = new Image(getClass().getResource("/java-icon.png").toString());
        ImageView deca_imvCollection = new ImageView();
        deca_imvCollection.setImage(deca);
        deca_imvCollection.setFitHeight(128);
        deca_imvCollection.setFitWidth(128);
        deca_imvCollection.setSmooth(true);
        final HBox nbox1 = new HBox();
        final HBox nbox3 = new HBox();
        final HBox nbox4 = new HBox();
        final Text title_label = new Text("404_Girl_Not_Found");
        title_label.setFont(Font.font(java.awt.Font.SERIF, 25));
        title_label.setFill(Color.CORNFLOWERBLUE);
        nbox1.getChildren().add(deca_imvCollection);
        nbox3.getChildren().add(title_label);
        nbox1.setAlignment(Pos.CENTER);
        nbox3.setAlignment(Pos.CENTER);
        
        final Text teamName_label = new Text("Group 77");
        teamName_label.setFont(Font.font(java.awt.Font.SERIF, 16));
        teamName_label.setFill(Color.DARKORCHID);
        nbox4.getChildren().add(teamName_label);
        nbox4.setAlignment(Pos.CENTER);
        
        // hard code :(
        String [] name = { "Chan Chi Wa", "Ngan Cheuk Hei","Yeung Chak Ho" };
        String [] itsc = { "cwchanbf", "chnganaa", "chyeungam" };
        String [] link = { "billy1624", "nganhei", "sawaYch" };
        Text [] member_name_label = new Text[3];
        Text [] member_itsc_label = new Text[3];
        Hyperlink [] member_github_link = new Hyperlink[3];
                
        
        GridPane grid = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setFillWidth(true);
        grid.getColumnConstraints().add(column1);
        
        grid.setGridLinesVisible(false);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(50, 50, 50, 50));
         
        grid.add(nbox1, 0, 0, 4, 1);
        grid.add(nbox3, 1, 1);
        grid.add(nbox4, 1, 2);
        
        
        ImageView []imvCollection = new ImageView[3];
        Image image = new Image( getClass().getResource("/github-icon.png").toString());   
        final HBox [] pictureRegion= new HBox[3];

        for(int i = 0; i < 3; ++i) {
        	imvCollection[i] = new ImageView();
        	imvCollection[i].setImage(image);
        	imvCollection[i].setFitHeight(16);
        	imvCollection[i].setFitWidth(16);
        	imvCollection[i].setSmooth(true);
        	pictureRegion[i] = new HBox();
        	pictureRegion[i].getChildren().add(imvCollection[i]);
        	pictureRegion[i].setAlignment(Pos.CENTER);
            member_name_label[i] = new Text(name[i]);
            member_itsc_label[i] = new Text(itsc[i]);
            member_github_link[i] = new Hyperlink(link[i]);
            member_github_link[i].setVisited(false);
            member_github_link[i].setBorder(Border.EMPTY);
            member_github_link[i].setOnAction(new github_EventHandler());
            grid.add(member_name_label[i], 0, 3+i);
            grid.add(member_itsc_label[i], 1, 3+i);
            grid.add(pictureRegion[i], 2, 3+i);
            grid.add(member_github_link[i], 3, 3+i);
        	
        }
            
    	Button button = new Button("Close");
    	button.setOnAction(new EventHandler<ActionEvent>() {
              @Override
              public void handle(ActionEvent event) {
            	  dialog.close();
              }
          });        
        
        final HBox nbox2 = new HBox();
        nbox2.getChildren().add(button);
        nbox2.setAlignment(Pos.CENTER);
        grid.add(nbox2, 1, 9);
        
        sceneGroup.getChildren().add(grid);
        
    	// show it
    	dialog.setScene(scene);
    	dialog.setResizable(false);
    	dialog.show();
    }
    
    /**
     * Handle action related to "About your Team" menu item.
     * 
     * @param event Event on "About your Team" menu item.
     */
    @FXML
    public void handleAboutYourTeamAction(final ActionEvent event)
    {
    	createAboutUsDialog();
    	// debug message
    }

    /**
     * Pass hostService from MainApplication, for hyperlink function
     * 
     * @param obj HostServices of MainApplication
     */  
    public void setHostServices(HostServices obj) {
    	hservices = obj;
    }

    
    /**
     * Handle action related to leave the program and disconnect all connection     * 
     */
    @FXML
    public void quit() {
    	scraper.getWebClient().close();
    	Platform.exit();
        //System.exit(0);
    }
    
    /**
     *  Handle close button (task 6.iii)
     *	TODO:
     *		1. clear console
     *		2. clear search bar keyword
     *		3. clear summary
     *		4. clear tableView items
     *		5. clear distribution
     *		6. clear trend
     *		7. refine button
     */
    @FXML
    public void closeAndResetAll() {
    	
    	for(int i = 0; i < lastSearchQueue.size(); ++i ) {
    		lastSearchQueue.poll();  
    	}
    	
    	for(int i = 0; i < lastSearchItemQueue.size(); ++i ) {
    		lastSearchItemQueue.poll();  
    	}

    	
    	// 0
        scraper.getWebClient().close();
    	scraperTh.stop();
    	busy_idtr.setVisible(false);

    	// 1
    	textAreaConsole.setText("");
    	// 2
    	textFieldKeyword.setText("");
    	// 3
    	labelCount.setText("<total>");
    	labelPrice.setText("<AvgPrice>");
    	labelMin.setText("<Lowest>");
    	labelLatest.setText("<Latest>");
    	// 4
    	for ( int i = 0; i<tableView.getItems().size(); i++) {
    	    tableView.getItems().clear();
    	}
    	// 5
    	barChartHistogram.getData().clear();
    	NumberAxis axis = (NumberAxis)barChartHistogram.getYAxis();
    	axis.setUpperBound(125.0);
    	axis.setLowerBound(0.0);
    	//6 
    	areaChart.getData().clear();
    	NumberAxis axis2 = (NumberAxis)areaChart.getYAxis();
    	axis2.setUpperBound(110.0);
    	axis2.setLowerBound(0.0);
    	areaChartCb.getItems().clear();
    	//7
    	refineBt.setDisable(true);
    }
    
    
    /**
     *	just a test case      
     */
	final ObservableList<DataModel> data = FXCollections.observableArrayList(
		    new DataModel("Title1", "0.0", "item1@example.com","01/02/2018"),
		    new DataModel("Title2", "10.0", "item2@example.com","03/04/2018"),
		    new DataModel("Title3", "20.0", "item3@example.com","05/06/2018"),
		    new DataModel("Title4", "100.0", "item4@example.com","07/08/2018"),
		    new DataModel("Title5", "500.0", "item5@example.com","09/10/2018")
		);
	
    // my data model: for testing only
    public static class DataModel {
        private SimpleStringProperty title;
        private SimpleStringProperty price;
        private SimpleStringProperty url;
        private SimpleStringProperty postedd;
     
        public DataModel(String _title, String _price, String _url, String _posted_date) {
            this.title = new SimpleStringProperty(_title);
            this.price = new SimpleStringProperty(_price);
            this.url = new SimpleStringProperty(_url);
            this.postedd = new SimpleStringProperty(_posted_date);

        }
        
        public String getTitle() {
            return title.get();
        }
        
        public void setTitle(String _title) {
            title.set(_title);
        }
            
        public String getPrice() {
            return price.get();
        }
        public void setPrice(String _price) {
            price.set(_price);
        }
        
        public String getUrl() {
            return url.get();
        }
        public void setUrl(String _url) {
            url.set(_url);
        }

        public String getPostedd() {
            return postedd.get();
        }
        public void setPostedd(String _posted_date) {
            this.postedd.set(_posted_date);
        }
            
    }

}



