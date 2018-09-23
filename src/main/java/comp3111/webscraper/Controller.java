/**
 * 
 */
package comp3111.webscraper;


import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.control.TextArea;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuBar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import java.util.List;
import javafx.scene.layout.*;
import com.gargoylesoftware.htmlunit.javascript.host.ApplicationCache;
import javafx.geometry.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;

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
    
    private WebScraper scraper;
    
    private HostServices hservices;
    
    
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
    	
    }
    
    /**
     * Called when the search button is pressed.
     */
    @FXML
    private void actionSearch() {
    	System.out.println("actionSearch: " + textFieldKeyword.getText());
    	List<Item> result = scraper.scrape(textFieldKeyword.getText());
    	String output = "";
    	for (Item item : result) {
    		output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\n";
    	}
    	textAreaConsole.setText(output);
    	
    	
    }
    
    /**
     * Called when the new button is pressed. Very dummy action - print something in the command prompt.
     */
    @FXML
    private void actionNew() {
    	System.out.println("actionNew");
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
        final HBox nbox1 = new HBox();
        final Text title_label = new Text("404_Girl_Not_Found");
        title_label.setFont(Font.font(java.awt.Font.SERIF, 25));
        title_label.setFill(Color.CORNFLOWERBLUE);
        nbox1.getChildren().add(title_label);
        nbox1.setAlignment(Pos.CENTER);
        
        final Text teamName_label = new Text("Group 77");
        teamName_label.setFont(Font.font(java.awt.Font.SERIF, 16));
        teamName_label.setFill(Color.DARKORCHID);
        
        
        String [] name = { "Chan Chi Wa", "Ngan Cheuk Hei","Yeung Chak Ho" };
        String [] itsc = { "cwchanbf", "chnganaa", "chyeungam" };
        String [] link = { "billy1624", "chnganaa", "sawaYch" };
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
        grid.add(teamName_label, 1, 1);
        
   
        
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
            grid.add(member_name_label[i], 0, 2+i);
            grid.add(member_itsc_label[i], 1, 2+i);
            grid.add(pictureRegion[i], 2, 2+i);
            grid.add(member_github_link[i], 3, 2+i);
        	
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
     * Handle action related to leave the program and disconnect all connection
     */
    @FXML
    public void quit() {
    	Platform.exit();
        System.exit(0);
    }
    
}

