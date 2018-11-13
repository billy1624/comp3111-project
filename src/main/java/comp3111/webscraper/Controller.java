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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;




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
    
    private WebScraper scraper;
    
    private HostServices hservices;
    
    private List<Item> recordItem;
    
    
    /* task 6 related data member */
    
    @FXML
    private MenuBar menuBar; 
  
    
    /**
     * Default controller
     */
    public Controller() {
        scraper = new WebScraper();
    }
    
    
    public int DateCompare(String a, String b){
        String a_mon;
        String b_mon;
        int a_day;
        int b_day;
        
        a_mon = a.substring(0, 3);
        b_mon = b.substring(0, 3);
        
        a_day = Integer.parseInt(a.substring(4, a.length()));
        b_day = Integer.parseInt(b.substring(4, b.length()));
                
        if( MonCompare(a_mon, b_mon)<0 ) return -1;
        if( MonCompare(a_mon, b_mon)>0 ) return 1;
        return a_day > b_day? 1 : a_day == b_day ? 0 : -1;
    }

    public int MonCompare(String a, String b){
        final List<String> monList = new ArrayList<String>(Arrays.asList(
                "Jan",
                "Feb",
                "Mar", 
                "Apr",
                "May",
                "Jun",
                "Jul",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec"));
        int a_index = 0;
        int b_index = 0;
        for (int i = 0; i < monList.size(); i++) {
            if (a.equals(monList.get(i))){
                a_index = i;
                break;
            }		
        }    	
        for (int i = 0; i < monList.size(); i++) {
            if (b.equals(monList.get(i))){
                b_index = i;
                break;
            }
        }
        return a_index < b_index ? -1 : a_index == b_index ? 0 : 1;
        
    }

    /**
     * Default initializer. It is empty.
     */
    @FXML
    private void initialize() {
        // load test case, for test
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
                
        Comparator<String> PostDateCompare = (r1, r2) -> DateCompare(r1, r2);
                
        price_col.setComparator(PriceCompare);	
        posted_date_col.setComparator(PostDateCompare);

        
        // disable at the beginning
        lastSearchBt.setDisable(true);
        refineBt.setDisable(true);
    }
    
    /**
     * Called when the search button is pressed.
     */
    String lowset_item_link = "";
    String Latest_item_link = "";

    @FXML
    private void actionSearch() {
        int num = 0 ;
        double TotalPrice = 0;
        double LPrice = 0;
        
        for ( int i = 0; i<tableView.getItems().size(); i++) {
            tableView.getItems().clear();
        }
        
        // enable last search function
        lastSearchBt.setDisable(false);
        refineBt.setDisable(false);

        System.out.println("actionSearch: " + textFieldKeyword.getText());
        List<Item> result = scraper.scrape(textFieldKeyword.getText());
        String output = "";
        for (Item item : result) {
            output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getPosted_date()+ '\t'+ item.getUrl() + "\n";
            DataModel tmp = new DataModel(item.getTitle(), Double.toString(item.getPrice()), item.getUrl(),item.getPosted_date());
            data.add(tmp);			    	
            if (num == 0)
            {
                LPrice = item.getPrice();
            } 
            else
                if (LPrice >  item.getPrice() && item.getPrice()!=0)
                {
                    LPrice = item.getPrice();
                    lowset_item_link = item.getUrl();
                    lowset_item_link = lowset_item_link.substring(31);
                }
                // Total price of the items
                TotalPrice += item.getPrice(); 
                 num += 1; 
            }
                    
        
    
        textAreaConsole.setText(output);
    
        //number of items
        labelCount.setText(Integer.toString(num));
    
        if (num == 0){
            labelPrice.setText("-");
            labelMin.setText("-");
            labelLatest.setText("-");
        }
        else
        {
            // find latest link
            // init first
            Latest_item_link =  data.get(0).getUrl().substring(31);	
            for(int i = 0; i < result.size()-1 ;++i){
                if( DateCompare(data.get(i).getPostedd(),data.get(i+1).getPostedd()) > 0){
                    Latest_item_link = data.get(i).getUrl().substring(31);
                }
            }
            
            labelPrice.setText(Double.toString(TotalPrice/num));
            labelMin.setText(lowset_item_link);
            labelMin.setOnAction( new EventHandler<ActionEvent>(){
    
                @Override
                public void handle(ActionEvent event) {
                    System.out.println(lowset_item_link);
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
             
        
    
        // copy for further use
        recordItem = new ArrayList<Item>(result);
        
        labelCount.setText(Integer.toString(num));
        
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
    
    //final ObservableList<DataModel> data = FXCollections.observableArrayList();
    final ObservableList<DataModel> data = FXCollections.observableArrayList();

//	= FXCollections.observableArrayList(
//		    new DataModel("", "0.0", "item1@example.com","01/02/2018"),
//		    new DataModel("Title2", "10.0", "item2@example.com","03/04/2018"),
//		    new DataModel("Title3", "20.0", "item3@example.com","05/06/2018"),
//		    new DataModel("Title4", "100.0", "item4@example.com","07/08/2018"),
//		    new DataModel("Title5", "500.0", "item5@example.com","09/10/2018")
//		);
    
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
                 
        // just a test for table, bar chart
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


