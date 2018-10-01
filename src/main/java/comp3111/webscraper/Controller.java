/**
 * 
 */
package comp3111.webscraper;


import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.control.TextArea;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;






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
    
    
    
    /* task 6 related data member */
    
    @FXML
    private MenuBar menuBar; 
    
    /**
     * Default controller
     */
    public Controller() {
    	scraper = new WebScraper();
    }

    /**
     * Default initializer. It is empty.
     */
    @FXML
    private void initialize() {
    	// load test case, for test
    	task6_iii_testCase();
    	
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
    
    /**
     * Called when the search button is pressed.
     */
    @FXML
    private void actionSearch() {    	
    	busy_idtr.setVisible(true);

        // use Async
    	Task<List<Item>> task = new Task<List<Item>>() {
            @Override protected List<Item> call() throws Exception {
            	
            	List<Item> result = scraper.scrape(textFieldKeyword.getText());
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
            	UpdateDistributionChart_Later(recordItem);
            	
                return null;
            }
        };
        
        task.setOnSucceeded(eventHandler->{
        	// enable last search function
        	lastSearchBt.setDisable(false);
        	refineBt.setDisable(false);   
        	busy_idtr.setVisible(false);
        }
        );
        
        new Thread(task).start();
    	
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
     * If one bar selected, reset other
     * 
     */
    private void update_bar_only_one(Integer var) {
    	// reset the one which is selected first
    	int myCounter = 0;
    	for(Boolean itr: bar_smVector) {
    		if(itr == true) {
    			bar_smVector.set(myCounter, false);
        		Series<String,Integer> serie =  ((Series<String,Integer>) barChartHistogram.getData().get(0));
        		Data<String, Integer> item = serie.getData().get(myCounter);
    			item.getNode().setStyle("-fx-bar-fill: #f3622d;");
    		}
    		myCounter ++;
    	}
    			
    }
    
    
    /*
     * Advance Task 1 ; just for fun
     * update distribution in Async way, non UI thread
     * 
     */
	private void UpdateDistributionChart_Later(List<Item> dist_data) {
	    Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	// reset it first
	        	bar_smVector.clear();
	          	barChartHistogram.getData().clear();
            	NumberAxis axis = (NumberAxis)barChartHistogram.getYAxis();
            	axis.setUpperBound(125.0);
            	axis.setLowerBound(0.0);
            	
            	if(dist_data.isEmpty()) return;
            	
            	// do the calculation
            	Item minp= null;
            	Item maxp= null;
            	for(Item x:dist_data){
            		minp=(minp==null||x.getPrice()<minp.getPrice())?x:minp;
            	}
            	for(Item x:dist_data){
            		maxp=(maxp==null||x.getPrice()>maxp.getPrice())?x:maxp;
            	}
            	Double minPrice=minp.getPrice();
            	Double maxPrice=maxp.getPrice();
            	Double rng= (minPrice + maxPrice)/10;
            	System.out.println("min" + minPrice);
            	System.out.println("max" + maxPrice);
            	DecimalFormat formatter = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance( Locale.ENGLISH ));
            	
            	// only single price
            	if (minPrice.compareTo(maxPrice)==0) {
            		Series<String, Integer> dist_series = new Series<String, Integer>();
                	dist_series.setName("The selling prize of '" + textFieldKeyword.getText() + "'");
                	int iter = 0;
                    dist_series.getData().add(new XYChart.Data<String, Integer>(Double.toString(maxPrice), dist_data.size()));            		
                	barChartHistogram.setCategoryGap(200);
                    barChartHistogram.getData().addAll(dist_series);    
        			bar_smVector.add(false); // state 0: not selected
            		return;
            	}
            	
            	// multiple price
                ArrayList<Double> v = new ArrayList<Double>();
                int[] vv = new int[10];
                double tmp = minPrice;
                for(int i=0;i<10;++i) {
                	if (i==0)
                		v.add(minPrice);
                	else
                	if (i==9)
                		v.add(maxPrice);
                	else
                		v.add(tmp+=rng);
                }
                //sort and count
                dist_data.sort( new Comparator<Item>() {
                	public int compare(Item it, Item it1) {
						Double p = it.getPrice();
						return p.compareTo(it1.getPrice());
                	}                	                	
                });

                
                // count
                for(Item tmp1: dist_data) {
                	for(int i=0; i<9; ++i) {
                		if(tmp1.getPrice() == maxPrice) {
                			vv[9] = vv[9] + 1;
                		}else
                		if(tmp1.getPrice()>=v.get(i) && tmp1.getPrice()<v.get(i+1)) {                			
                			vv[i] = vv[i] + 1;
                			break;
                		}
                	}
                }                

            	Series<String, Integer> dist_series = new Series<String, Integer>();
            	dist_series.setName("The selling prize of '" + textFieldKeyword.getText() + "'");
            	int iter = 0;
        		for (Double _price : v) {
        			dist_series.getData().add(new XYChart.Data<String, Integer>(formatter.format(Math.round(_price)), vv[iter++]));
        			bar_smVector.add(false); // state false/0: not selected
        		}

            	barChartHistogram.setCategoryGap(0);
            	barChartHistogram.getData().addAll(dist_series);
            	
            	// add IDs
            	for(int itr = 0; itr < 10; ++itr)
            		dist_series.getData().get(itr).getNode().setId(Integer.toString(itr));

            	
            	 for (Object serie: barChartHistogram.getData()){
            		 for (Data<String, Integer> item: ((Series<String,Integer>) serie).getData()){
            			 item.getNode().setOnMouseEntered(e -> {
            				 
            				 for(Boolean see: bar_smVector)
            			    		if (see == true) System.out.print("1");
            			    		else System.out.print("0");
            				 System.out.print("\n");
            				 if(!has_bar_selected())
            				 item.getNode().setStyle("-fx-bar-fill: #a9e200;");
            			 });
            			 item.getNode().setOnMouseExited(e -> {
            				 if(!has_bar_selected())
            				 item.getNode().setStyle("-fx-bar-fill: #f3622d;");
            			 });
            			 item.getNode().setOnMouseClicked(new EventHandler<MouseEvent>() {
            					    @Override
            					    public void handle(MouseEvent mouseEvent) {
            					        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            					            if(mouseEvent.getClickCount() == 2){
            					                System.out.println("Double clicked");
            					                Boolean current_state = bar_smVector.get(Integer.parseInt(item.getNode().getId()));
            					                if(!current_state) {
                		            				item.getNode().setStyle("-fx-bar-fill: #9a42c8;");	
            					                }else {
            			            				 item.getNode().setStyle("-fx-bar-fill: #f3622d;");
            					                }
            					                
            					                update_bar_only_one(Integer.parseInt(item.getNode().getId()));
            					                
            		            				bar_smVector.set(Integer.parseInt(item.getNode().getId()),
            		            						!current_state);
            					            }
            					        }
            					    }
            				 }
            			 );
                     }
                 }
        	
	        }
	    });
	}
    
    /**
     * Called when the new button is pressed. Very dummy action - print something in the command prompt.
     */
    @FXML
    private void actionNew() {
    	System.out.println("actionNew");
    }
    
    
    /**
     * Task 5 Refine Search function
     * 
     */   
    @FXML
    private void actionRefine() {
    	// after click once, disable
    	refineBt.setDisable(true);
    	System.out.println("Refine:" + textFieldKeyword.getText());
    	String output = "";
    	for (Item item : recordItem) {
    		if (item.getTitle().matches("(.*)"+ textFieldKeyword.getText() + "(.*)"))
    			output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\n";
    	}
    	textAreaConsole.setText(output);
    	// trigger: update of other page
    }
    
    
    /**
     * Handle action related to "About your Team" menu item.
     * 
     * @param event Event on "About your Team" menu item.
     */
    @FXML
    private void handleAboutYourTeamAction(final ActionEvent event)
    {
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
            member_github_link[i].setOnAction(new EventHandler<ActionEvent>() {             
                @Override
                public void handle(ActionEvent event) {
                	String text = ((Hyperlink)event.getSource()).getText();
                	hservices.showDocument("https://github.com/" + text);
                }
            });
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
            	  System.out.println("About Your Team Dialog Closed");
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
    	// debug message
        System.out.println("About Your Team Dialog Opened");      
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
        System.exit(0);
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
    	System.out.println("closeAndResetAll()");
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
	
    @SuppressWarnings("unchecked")
	void task6_iii_testCase() {
    	title_col.setCellValueFactory(new PropertyValueFactory<DataModel,String>("title"));
    	title_col.setCellFactory(TextFieldTableCell.forTableColumn());
    	price_col.setCellValueFactory(new PropertyValueFactory<DataModel,String>("price"));
    	price_col.setCellFactory(TextFieldTableCell.forTableColumn());
    	url_col.setCellValueFactory(new PropertyValueFactory<DataModel,String>("url"));
    	url_col.setCellFactory(TextFieldTableCell.forTableColumn());
    	posted_date_col.setCellValueFactory(new PropertyValueFactory<DataModel,String>("postedd"));
    	posted_date_col.setCellFactory(TextFieldTableCell.forTableColumn());
    	tableView.setItems(data);
    	    	 
    	// just a test for bar chart
		final String it1 = "Item1";
    	final String it2 = "Item2";
    	final String it3 = "Item3";
    	final String it4 = "Item4";
    	final String it5 = "Item5";
    	Series<String, Double> series1 = new Series<String, Double>();
        series1.setName("Test Data Set");       
        series1.getData().add(new XYChart.Data<String, Double>(it1, 25601.34));
        series1.getData().add(new XYChart.Data<String, Double>(it2, 20148.82));
        series1.getData().add(new XYChart.Data<String, Double>(it3, (double) 10000));
        series1.getData().add(new XYChart.Data<String, Double>(it4, 35407.15));
        series1.getData().add(new XYChart.Data<String, Double>(it5, (double) 12000));   
    	barChartHistogram.getData().addAll(series1);
    	barChartHistogram.getXAxis().setLabel("price");
    	barChartHistogram.getYAxis().setLabel("number of items");
    	

    	
    	// trend test data set
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
    	areaChart.getData().addAll(series2);    	
    	areaChartCb.getItems().addAll(
    		    "Option 1",
    		    "Option 2",
    		    "Option 3"
    		);
    }
    
    // my data model: for testing only
    public static class DataModel {
        private final SimpleStringProperty title;
        private final SimpleStringProperty price;
        private final SimpleStringProperty url;
        private final SimpleStringProperty postedd;
     
        private DataModel(String _title, String _price, String _url, String _posted_date) {
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



