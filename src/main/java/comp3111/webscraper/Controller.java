package comp3111.webscraper;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Queue;

import com.gargoylesoftware.css.parser.javacc.ParseException;

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
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
 *         Controller class that manage GUI interaction. Please see document
 *         about JavaFX for details.
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

    @FXML
    private MenuBar menuBar;

    final ObservableList<DataModel> data = FXCollections.observableArrayList();
    

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
     * Comparing the data
     * @author Ngan Cheuk Hei - chnganaa
     */
    public int DateCompare(String a, String b) throws ParseException, java.text.ParseException {
    	Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(a);
    	Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(b);    
		return date1.compareTo(date2);
    }

    /**
     * Default initializer.
     * Init javaFx view components if needed
     * @author Yeung Chak Ho - chyeungam
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

        //task6_iii_testCase();
        title_col.setCellValueFactory(new PropertyValueFactory<DataModel,String>("title"));
        title_col.setCellFactory(TextFieldTableCell.forTableColumn());
        price_col.setCellValueFactory(new PropertyValueFactory<DataModel,String>("price"));
        price_col.setCellFactory(TextFieldTableCell.forTableColumn());
        url_col.setCellValueFactory(new PropertyValueFactory<DataModel,String>("url"));
        url_col.setCellFactory(TextFieldTableCell.forTableColumn());
        posted_date_col.setCellValueFactory(new PropertyValueFactory<DataModel,String>("postedd"));
        posted_date_col.setCellFactory(TextFieldTableCell.forTableColumn());
        tableView.setItems(data);
        tableView.setEditable(false);
        Comparator<String> PriceCompare = (r1, r2) -> Double.parseDouble(r1) >  Double.parseDouble(r2)? 1: -1;
                
        Comparator<String> PostDateCompare = (r1, r2) -> {
        	int result = 0;
			try {
				result = DateCompare(r1, r2);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return result;
		};
                
        price_col.setComparator(PriceCompare);	
        posted_date_col.setComparator(PostDateCompare);
    }

    /**
     * Update your last search keyword to queue
     * @author Yeung Chak Ho - chyeungam
     */
    private void updateLastSearch_Keyword() {
        lastSearchQueue.offer(textFieldKeyword.getText());
        if (lastSearchQueue.size() == 3)
            lastSearchQueue.poll();
        return;
    }
    
    /**	
     * Append item to last search queue
     * @param what_to_add - the seach record will add to the queue
     * @author Yeung Chak Ho - chyeungam
     * 
     */
    private void updateLastSearch_Item(List<Item> what_to_add) {
        lastSearchItemQueue.offer(what_to_add);
        if (lastSearchItemQueue.size() == 3)
            lastSearchItemQueue.poll();
    }
    
    /**
     * Scaping portal with async 
     * @author Yeung Chak Ho - chyeungam
     * Call the function to update summary and table
     * @author Ngan Cheuk Hei - chnganaa
     *
     */
    public class SearchAsyncTask extends Task<List<Item>> {
        @Override
        protected List<Item> call() throws Exception {

            List<Item> result = scraper.scrape(textFieldKeyword.getText(), textAreaConsole);
            System.out.println("actionSearch: " + textFieldKeyword.getText());
            String output = "";
            
            for (Item item : result) {
                output += item.toString() + "\n";
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                DataModel tmp = new DataModel(item.getTitle(), Double.toString(item.getPrice()), item.getUrl(),df.format(item.getPostedOn()));
        	   	data.add(tmp);                    
            }
            textAreaConsole.textProperty().unbind();
            textAreaConsole.appendText(output);

            // copy for further use
            recordItem = new ArrayList<Item>(result);
            
            // update distribution bar chart
            UpdateDistributionChart_Later(recordItem);
            // update other page here
            
            // summary update
            UpdateSummary(recordItem);
            // table update
            UpdateTable(recordItem);
            return null;
        }

    };

    /**
     * Called when the search(i.e. Go) button is pressed.
     * @author Yeung Chak Ho - chyeungam
     * @author Billy
     * @author Ngan Cheuk Hei - chnganaa 
     */
    String lowset_item_link = "";
    String Latest_item_link = "";

    @FXML
    private void actionSearch() {
        busy_idtr.setVisible(true);

        // use Async
        SearchAsyncTask task = new SearchAsyncTask();

        task.setOnSucceeded(eventHandler -> {
            // enable last search function
            lastSearchBt.setDisable(false);
            refineBt.setDisable(false);
            busy_idtr.setVisible(false);
            updateLastSearch_Item(recordItem);
            updateLastSearch_Keyword();
            refine_lastSearch = false;
        });
        
        scraperTh = new Thread(task);
        scraperTh.start();

    }

    /**
     * Helper function
     * Check any bar is double clicked
     * @return Boolean - is any bar being selected
     * @author Yeung Chak Ho - chyeungam
     */
    private Boolean has_bar_selected() {
        for (Boolean iter : bar_smVector)
            if (iter == true)
                return iter;
        return false;
    }

    /**
     * Helper function
     * If a bar is selected, reset others
     * @param var - reserved for implement multiple bar selcetion
     * @param _dist_data - distribution data, list of item 
     * @param ps - price start of bin 
     * @param pe - price end of bin
     * @author Yeung Chak Ho - chyeungam
     */
    private void update_bar_only_one(Integer var, List<Item> _dist_data, Double ps, Double pe) {
        // reset the one which is selected first
        int myCounter = 0;
        int targetIndex = 0;
        for (Boolean itr : bar_smVector) {
            if (itr == true) {
                bar_smVector.set(myCounter, false);
                Series<String, Integer> serie = ((Series<String, Integer>) barChartHistogram.getData().get(0));
                Data<String, Integer> item = serie.getData().get(myCounter);
                targetIndex = myCounter;
                item.getNode().setStyle("-fx-bar-fill: #f3622d;");
            }
            myCounter++;
        }

        String output = "";
        for (Item itr : _dist_data) {
            if (itr.getPrice() >= ps && itr.getPrice() < pe) {
                output += itr.getTitle() + "\t" + itr.getPrice() + "\t" + itr.getUrl() + "\n";
            }
        }
        textAreaConsole.textProperty().unbind();
        textAreaConsole.setText(output);

    }


    /**
     * hyperlink onclick handler
     * link to member's github page
     * @author Yeung Chak Ho - chyeungam
     *
     */
    public class github_EventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String text = ((Hyperlink) event.getSource()).getText();
            hservices.showDocument("https://github.com/" + text);
        }
    }

    /**
     * Called when the new button is pressed. Very dummy action - print something in
     * the command prompt. actionNew() actually is LastSearchAction The result will
     * revert to the last Search
     * @author Yeung Chak Ho - chyeungam
     */
    @FXML
    private void actionLastSearch() {
        lastSearchBt.setDisable(true);
        refineBt.setDisable(false);

        String lastKeyword = lastSearchQueue.peek();
        System.out.println("last Search : " + lastKeyword);
        textFieldKeyword.setText(lastKeyword); // set back to last search keyword

        if (lastSearchItemQueue.peek() == null) {
            System.out.println("last search item queue EMPTY!");
            	closeAndResetAll(); // buggy here
            return;
        }
        refine_lastSearch = true;
        String output = "";
        for (Item item : lastSearchItemQueue.peek()) {
            output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\n";
        }		
        
        /*
         * trigger: update of other page
         */
        // Console page update	
        textAreaConsole.setText(output);

        // Distribution chart update
        UpdateDistributionChart_Later(lastSearchItemQueue.peek());

        // summary page update
        UpdateSummary(lastSearchItemQueue.peek());
        // table update
        UpdateTable(lastSearchItemQueue.peek());
    }

    /**
     * Task 5 Refine Search function Only extract the the data with certain keyword,
     * title field(handle locally), Refined result will NOT save as a search record
     * LastSearch/Current can refine one time only
     * @author Yeung Chak Ho - chyeungam
     */
    @FXML
    private void actionRefine() {
        // after click once, disable
        refineBt.setDisable(true);

        String output = "";
        // if previous action is lastSearch, then use lastSearch Item List,
        // otherwise previous action can only be actionSearch, then use current Item
        // List;
        Iterator<Item> iter = null;
        List<Item> targetItem_list;
        if (refine_lastSearch == true) {
            iter = lastSearchItemQueue.peek().listIterator();
            targetItem_list = lastSearchItemQueue.peek();
        } else {
            iter = recordItem.listIterator();
            targetItem_list = recordItem;
        }

        for (; iter.hasNext();) {
            Item item = iter.next();
            if (item.getTitle().matches("(.*)" + textFieldKeyword.getText() + "(.*)")) {
                output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\n";
            } else
                iter.remove();
        }
        
        /*
         * trigger: update of other page
         */
        // Console update
        textAreaConsole.setText(output);
        
        // Distribution chart update
        UpdateDistributionChart_Later(targetItem_list);

        // summary page update
       
				// TODO Auto-generated method stub
		        UpdateSummary(targetItem_list);

		
      
        
        // table update
        UpdateTable(targetItem_list);
    }

    /**
     * About Us Dialog that show information: 
     * - member's itsc 
     * - member's github page link
     * 
     * @author Yeung Chak Ho - chyeungam
     */
    public void createAboutUsDialog() {
        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.DECORATED);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.getIcons().add(new Image(getClass().getResource("/java-icon.png").toString()));
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
        String[] name = { "Chan Chi Wa", "Ngan Cheuk Hei", "Yeung Chak Ho" };
        String[] itsc = { "cwchanbf", "chnganaa", "chyeungam" };
        String[] link = { "billy1624", "nganhei", "sawaYch" };
        Text[] member_name_label = new Text[3];
        Text[] member_itsc_label = new Text[3];
        Hyperlink[] member_github_link = new Hyperlink[3];

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

        ImageView[] imvCollection = new ImageView[3];
        Image image = new Image(getClass().getResource("/github-icon.png").toString());
        final HBox[] pictureRegion = new HBox[3];

        // config components and added to Grid
        for (int i = 0; i < 3; ++i) {
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
            grid.add(member_name_label[i], 0, 3 + i);
            grid.add(member_itsc_label[i], 1, 3 + i);
            grid.add(pictureRegion[i], 2, 3 + i);
            grid.add(member_github_link[i], 3, 3 + i);

        }

        // add close event handler
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

        // packup all the stuff
        sceneGroup.getChildren().add(grid);

        // show it
        dialog.setScene(scene);
        dialog.setResizable(false);
        dialog.show();
    }

    /**
     * Handle action related to "About your Team" menu item.
     * 
     * @param event Event on "About your Team" menu item
     * @author Yeung Chak Ho - chyeungam
     */
    @FXML
    public void handleAboutYourTeamAction(final ActionEvent event) {
        createAboutUsDialog();
        // debug message
    }

    /**
     * Pass hostService from MainApplication, for hyperlink function
     * 
     * @param obj HostServices of MainApplication
     * @author Yeung Chak Ho - chyeungam
     */
    public void setHostServices(HostServices obj) {
        hservices = obj;
    }

    /**
     * Handle action related to exit the program; and disconnect all connection
     * 
     * @author Yeung Chak Ho - chyeungam
     */
    @FXML
    public void quit() {
        scraper.getWebClient().close();
        Platform.exit();
    }

    /**
     * Handle close button (Task 6.iii) 
     * TODO: 
     * 0. close webClient connection 
     * 1. clear console 
     * 2. clear search bar keyword 
     * 3. clear summary 
     * 4. clear tableView items
     * 5. clear distribution 
     * 6. clear trend 
     * 7. refine button 
     * 8. clear all search record
     * 
     * @author Yeung Chak Ho - chyeungam
     * 
     */
    @FXML
    public void closeAndResetAll() {   
        // 0
        scraper.getWebClient().close();
        // be careful, handle null thread
        if(scraperTh != null)
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
        for (int i = 0; i < tableView.getItems().size(); i++) {
            tableView.getItems().clear();
        }
        // 5
        barChartHistogram.getData().clear();
        NumberAxis axis = (NumberAxis) barChartHistogram.getYAxis();
        axis.setUpperBound(125.0);
        axis.setLowerBound(0.0);
        // 6
        areaChart.getData().clear();
        NumberAxis axis2 = (NumberAxis) areaChart.getYAxis();
        axis2.setUpperBound(110.0);
        axis2.setLowerBound(0.0);
        areaChartCb.getItems().clear();
        // 7
        refineBt.setDisable(true);

        // 8
        if (lastSearchItemQueue.size() > 0 && lastSearchQueue.size() > 0 && lastSearchQueue.peek()!="NULL" ) {        	
        	clearHistory_Alert();
        }else {
        	lastSearchBt.setDisable(true);
        }
   
    }
    
    public void clearHistory_Alert() {    	    	  
    	Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				final Alert alert = new Alert(AlertType.INFORMATION, "You are going to close current search record now.\nDo you also want to clear latest search history?", ButtonType.YES, ButtonType.NO);
		        alert.setTitle("Close current search");
		        alert.setHeaderText("");
		        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		        stage.getIcons().add(new Image( getClass().getResource("/java-icon.png").toString()));
		        Optional<ButtonType> opt = alert.showAndWait();
		        ButtonType rtn = ButtonType.YES;
		        if (opt.isPresent())
		        	rtn = opt.get();
		        if (rtn == ButtonType.YES) {
		        	erase_search_history();
		        } else if (rtn == ButtonType.NO) {
		        	lastSearchBt.setDisable(false);
		        }
			}
    		
    	});    	    	
    }
    
    public void erase_search_history() {
      	// clear search history	        	
        for (int i = 0; i < lastSearchQueue.size(); ++i)
            lastSearchQueue.poll();

        for (int i = 0; i < lastSearchItemQueue.size(); ++i)
            lastSearchItemQueue.poll();
        // push null indicate last record is null
        lastSearchQueue.offer("NULL");
        lastSearchItemQueue.offer(null);
    }

    /**
     * DataModel for Barchart histogram
     * 
     * @author Yeung Chak Ho - chyeungam
     *
     */
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

    /**
     * Render and update distibution barchart
     * 
     * @param dist_data - List of search record
     * @author Yeung Chak Ho - chyeungam
     *
     */
    private void UpdateDistributionChart_Later(List<Item> dist_data) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // reset it first every time
                bar_smVector.clear();
                barChartHistogram.getData().clear();
                NumberAxis axis = (NumberAxis) barChartHistogram.getYAxis();
                axis.setUpperBound(125.0);
                axis.setLowerBound(0.0);

                // if no search record, then just leave it
                if (dist_data.isEmpty())
                    return;

                /*
                 * do the calculation 1. find min and max in the list 2. then diff between max
                 * and min divide by 10 is range of bin
                 */
                Item minp = null;
                Item maxp = null;
                for (Item x : dist_data) {
                    minp = (minp == null || x.getPrice() < minp.getPrice()) ? x : minp;
                }
                for (Item x : dist_data) {
                    maxp = (maxp == null || x.getPrice() > maxp.getPrice()) ? x : maxp;
                }
                Double minPrice = minp.getPrice();
                Double maxPrice = maxp.getPrice();
                Double rng = (minPrice + maxPrice) / 10;
                DecimalFormat formatter = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

                /*
                 * handle if all items have same price Then just show a single bar
                 */
                if (minPrice.compareTo(maxPrice) == 0) {
                    Series<String, Integer> dist_series = new Series<String, Integer>();
                    dist_series.setName("The selling prize of '" + textFieldKeyword.getText() + "'");
                    int iter = 0;
                    dist_series.getData()
                            .add(new XYChart.Data<String, Integer>(Double.toString(maxPrice), dist_data.size()));
                    barChartHistogram.setCategoryGap(200);
                    barChartHistogram.getData().addAll(dist_series);
                    bar_smVector.add(false); // state 0: not selected
                    return;
                }

                // handle if items have different price
                ArrayList<Double> v = new ArrayList<Double>();
                int[] vv = new int[10];
                double tmp = minPrice;
                for (int i = 0; i < 10; ++i) {
                    if (i == 0)
                        v.add(minPrice);
                    else if (i == 9)
                        v.add(maxPrice);
                    else
                        v.add(tmp += rng);
                }

                // sort the price first
                dist_data.sort(new Comparator<Item>() {
                    public int compare(Item it, Item it1) {
                        Double p = it.getPrice();
                        return p.compareTo(it1.getPrice());
                    }
                });

                // count: how many items in each bin
                for (Item tmp1 : dist_data) {
                    for (int i = 0; i < 9; ++i) {
                        if (tmp1.getPrice() == maxPrice) {
                            vv[9] = vv[9] + 1;
                            break;
                        } else if (tmp1.getPrice() >= v.get(i) && tmp1.getPrice() < v.get(i + 1)) {
                            vv[i] = vv[i] + 1;
                            break;
                        }
                    }
                }

                Series<String, Integer> dist_series = new Series<String, Integer>();
                dist_series.setName("The selling prize of '" + textFieldKeyword.getText() + "'");
                int iter = 0;
                for (Double _price : v) {
                    dist_series.getData()
                            .add(new XYChart.Data<String, Integer>(formatter.format(Math.round(_price)), vv[iter++]));
                    bar_smVector.add(false); // state false/0: not selected
                }

                // update the chart with new data
                barChartHistogram.setCategoryGap(0);
                barChartHistogram.getData().addAll(dist_series);

                // add IDs
                for (int itr = 0; itr < 10; ++itr)
                    dist_series.getData().get(itr).getNode().setId(Integer.toString(itr));

                // add mouse event handler
                for (Object serie : barChartHistogram.getData()) {
                    for (Data<String, Integer> item : ((Series<String, Integer>) serie).getData()) {
                        BarChart_BarEntered_Handler beh_instance = new BarChart_BarEntered_Handler();
                        beh_instance.setData(item);
                        item.getNode().setOnMouseEntered(beh_instance);

                        BarChart_BarExited_Handler bExith_instance = new BarChart_BarExited_Handler();
                        bExith_instance.setData(item);
                        item.getNode().setOnMouseExited(bExith_instance);

                        BarChart_BarDBClick_Handler bDBch_instance = new BarChart_BarDBClick_Handler();
                        bDBch_instance.setData(item);
                        bDBch_instance.setDistributionData(dist_data);
                        bDBch_instance.setRange(rng);
                        item.getNode().setOnMouseClicked(bDBch_instance);
                    }
                }
            }
        });
    }

    /**
     * BarChart onEntered Event Handler Inner Class When mouse is entered to a bar -
     * the color will be changed
     * 
     * @author Yeung Chak Ho - chyeungam
     *
     */
    public class BarChart_BarEntered_Handler implements EventHandler<MouseEvent> {
        private Data<String, Integer> item;

        @Override
        public void handle(MouseEvent event) {
            System.out.println("Cursor Enter Bar");
            for (Boolean see : bar_smVector)
                if (!has_bar_selected())
                    item.getNode().setStyle("-fx-bar-fill: #a9e200;");
        }

        public void setData(Data<String, Integer> input) {
            item = input;
        }

    }

    /**
     * BarChart onExited Event Handler Inner Class When mouse is exited from a bar -
     * reset color to original one
     * 
     * @author Yeung Chak Ho - chyeungam
     *
     */
    public class BarChart_BarExited_Handler implements EventHandler<MouseEvent> {
        private Data<String, Integer> item;

        @Override
        public void handle(MouseEvent event) {
            if (!has_bar_selected())
                item.getNode().setStyle("-fx-bar-fill: #f3622d;");
        }

        public void setData(Data<String, Integer> input) {
            item = input;
        }

    }

    /**
     * BarChart Double Click Event Handler Inner Class When bar is double clicked -
     * Its color will be changed - bar_smVector will update to indicate it has been
     * clicked - update textAreaConsole
     * 
     * @author Yeung Chak Ho - chyeungam
     *
     */
    public class BarChart_BarDBClick_Handler implements EventHandler<MouseEvent> {
        private Data<String, Integer> item;
        private List<Item> dist_data;
        private Double rng;
        
        /**
         * The mouse event for barchart
         * @param mouseEvent - event type
         * @author Yeung Chak Ho - chyeungam
         */
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    Boolean current_state = bar_smVector.get(Integer.parseInt(item.getNode().getId()));
                    if (!current_state) {
                        item.getNode().setStyle("-fx-bar-fill: #9a42c8;");
                        update_bar_only_one(Integer.parseInt(item.getNode().getId()), dist_data,
                                Double.parseDouble(item.getXValue()), Double.parseDouble(item.getXValue()) + rng);
                    } else {
                        item.getNode().setStyle("-fx-bar-fill: #f3622d;");
                        String output = "";
                        for (Item item : dist_data) {
                            output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\n";
                        }
                        textAreaConsole.textProperty().unbind();
                        textAreaConsole.setText(output);
                    }
                    bar_smVector.set(Integer.parseInt(item.getNode().getId()), !current_state);
                }
            }
        }

        /**
         * Update the X-axis data
         * 
         * @param input - the data of X axis that used by XYChart (Barchart)
         * @author Yeung Chak Ho - chyeungam
         */
        public void setData(Data<String, Integer> input) {
            item = input;
        }

        /**
         * Set the Item List to the bar chart for displaying
         * 
         * @param input - The distribution data. Should be List of Item.
         * @author Yeung Chak Ho - chyeungam
         */
        public void setDistributionData(List<Item> input) {
            dist_data = input;
        }

        /**
         * Set the range between the each bin of barchart
         * 
         * @param range - range between the each bin of barchart
         * @author Yeung Chak Ho - chyeungam
         */
        public void setRange(Double range) {
            rng = range;
        }
    }

    /**
     * Task6 test Case This funtion will init all page with garbage data, including
     * barchart, areachart, etc (some of them are in Advance Task)
     * 
     * @author Yeung Chak Ho - chyeungam
     */
    void task6_iii_testCase() {
        /* Data that used in the test case */
        final ObservableList<DataModel> data = FXCollections.observableArrayList(
                new DataModel("Title1", "0.0", "item1@example.com", "01/02/2018"),
                new DataModel("Title2", "10.0", "item2@example.com", "03/04/2018"),
                new DataModel("Title3", "20.0", "item3@example.com", "05/06/2018"),
                new DataModel("Title4", "100.0", "item4@example.com", "07/08/2018"),
                new DataModel("Title5", "500.0", "item5@example.com", "09/10/2018"));
        title_col.setCellValueFactory(new PropertyValueFactory<DataModel, String>("title"));
        title_col.setCellFactory(TextFieldTableCell.forTableColumn());
        price_col.setCellValueFactory(new PropertyValueFactory<DataModel, String>("price"));
        price_col.setCellFactory(TextFieldTableCell.forTableColumn());
        url_col.setCellValueFactory(new PropertyValueFactory<DataModel, String>("url"));
        url_col.setCellFactory(TextFieldTableCell.forTableColumn());
        posted_date_col.setCellValueFactory(new PropertyValueFactory<DataModel, String>("postedd"));
        posted_date_col.setCellFactory(TextFieldTableCell.forTableColumn());
        tableView.setItems(data);

        // barchart data set
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
        areaChartCb.getItems().addAll("Option 1", "Option 2", "Option 3");
    }


    /**
     * Update summary information
     * 
     * @param result - List of item record
     * @author Ngan Cheuk Hei - chnganaa
     *
     */
private void UpdateSummary(List<Item> result){	
	Platform.runLater(new Runnable() {
        @Override
        public void run() {
	    int num = 0 ;
	    int numNo0 = 0;
	    double TotalPrice = 0;
	    double LPrice = 0;

	    for (Item item : result) 
	    {
	    	 
	    	 if (item.getPrice()!=0 && LPrice == 0)
	    	 {
		        LPrice = item.getPrice();		        
		        lowset_item_link = item.getUrl();
		        
	    	 } 
		    else
		        if (LPrice != 0 && Double.compare(LPrice, item.getPrice())>0 && item.getPrice() != 0)
		        {
		            LPrice = item.getPrice();
		            lowset_item_link = item.getUrl();
		         
		           
		        }	    			    	
	    	
		     // Total price of the items
	    	 if (item.getPrice()!=0)
	    	 {
		        TotalPrice += item.getPrice();
		        numNo0 += 1;
	    	 }
		     num += 1; 
		  }
		            
	
	
	
	
	//number of items
	labelCount.setText(Integer.toString(num));
	
	if (num == 0 || data.isEmpty()){
	    labelPrice.setText("-");
	    labelMin.setText("-");
	    labelLatest.setText("-");
	}
	else
	{
		
	    // find latest link
	    // init first		
	    Latest_item_link =  data.get(0).getUrl();	//indexOutOfBoundsException
	    int latest_index = 0;
	    for(int i = 0; i < result.size()-1 ;++i){
	        if( result.get(i).getPostedOn().compareTo(result.get(latest_index).getPostedOn()) > 0){
	        	latest_index = i;
	        	Latest_item_link = result.get(latest_index).getUrl();
	        }
	    }
	    
	    labelPrice.setText(Double.toString(Math.round(TotalPrice/numNo0)));
	    labelMin.setText(lowset_item_link);
	   
	    labelMin.setOnAction( new EventHandler<ActionEvent>(){

	        @Override
	        public void handle(ActionEvent event) {
	           
	            hservices.showDocument(lowset_item_link);
	        }
	        
	    }
	    );
	    
	    labelLatest.setText(Latest_item_link);
	    System.out.println("Latestil:"+Latest_item_link);

	
	    labelLatest.setOnAction( new EventHandler<ActionEvent>(){
	
	        @Override
	        public void handle(ActionEvent event) {
	            hservices.showDocument(Latest_item_link);
	        }
	        
	    }
	    );
	}
        };
	        
 });
}

/**
 * Update table information
 * 
 * @param result - List of item record
 * @author Ngan Cheuk Hei - chnganaa
 *
 */
public void UpdateTable(List<Item> result){
	Platform.runLater(new Runnable() {
     @Override
	        public void run() {
    	 		if (data.isEmpty()) return;
	     	   for ( int i = 0; i< tableView.getItems().size(); i++) {
	          		tableView.getItems().clear();
	          	}
     	   
			 for (Item item : result) {
			     DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			     DataModel tmp = new DataModel(item.getTitle(), Double.toString(item.getPrice()), item.getUrl(),df.format(item.getPostedOn()));
			   	 data.add(tmp); //NullPointerException 1168       	
			 }
	
		}
	});
}
}