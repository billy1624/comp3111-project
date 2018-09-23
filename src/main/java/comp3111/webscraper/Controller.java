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
        
        final Text member1_name_label = new Text("Chan Chi Wa");
        final Text member1_itsc_label = new Text("cwchanbf");
        
        final Hyperlink member1_github_label = new Hyperlink("billy1624");
        member1_github_label.setVisited(false);
        member1_github_label.setBorder(Border.EMPTY);
        member1_github_label.setOnAction(new EventHandler<ActionEvent>() {
         
            @Override
            public void handle(ActionEvent event) {
            	String text = ((Hyperlink)event.getSource()).getText();
            	hservices.showDocument("https://github.com/" + text);
            }
        });
        
        final Text member2_name_label = new Text("Ngan Cheuk Hei");
        final Text member2_itsc_label = new Text("chnganaa");
        
        
        final Hyperlink member2_github_label = new Hyperlink("nganhei");
        member2_github_label.setVisited(false);
        member2_github_label.setBorder(Border.EMPTY);
        member2_github_label.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
            	String text = ((Hyperlink)event.getSource()).getText();
            	hservices.showDocument("https://github.com/" + text);
            }
        });
        
        final Text member3_name_label = new Text("Yeung Chak Ho");
        final Text member3_itsc_label = new Text("chyeungam");
        final Hyperlink member3_github_label = new Hyperlink("sawaYch");
        member3_github_label.setVisited(false);
        member3_github_label.setBorder(Border.EMPTY);
        member3_github_label.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
            	String text = ((Hyperlink)event.getSource()).getText();
            	hservices.showDocument("https://github.com/" + text);
            }
        });

        
        
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
        
        grid.add(member1_name_label, 0, 2);
        grid.add(member1_itsc_label, 1, 2);
        final ImageView imv = new ImageView();
        final ImageView imv2 = new ImageView();
        final ImageView imv3 = new ImageView();

        Image image = new Image( getClass().getResource("/github-icon.png").toString());   
        imv.setImage(image);
        imv.setFitHeight(16);
        imv.setFitWidth(16);
        imv.setSmooth(true);
        
        imv2.setImage(image);
        imv2.setFitHeight(16);
        imv2.setFitWidth(16);
        imv2.setSmooth(true);
        
        imv3.setImage(image);
        imv3.setFitHeight(16);
        imv3.setFitWidth(16);
        imv3.setSmooth(true);
        final HBox [] pictureRegion= new HBox[3];
        
    	pictureRegion[0] = new HBox();
    	pictureRegion[0].getChildren().add(imv);
    	pictureRegion[0].setAlignment(Pos.CENTER);
    
    	pictureRegion[1] = new HBox();
    	pictureRegion[1].getChildren().add(imv2);
    	pictureRegion[1].setAlignment(Pos.CENTER);
    
    	pictureRegion[2] = new HBox();
    	pictureRegion[2].getChildren().add(imv3);
    	pictureRegion[2].setAlignment(Pos.CENTER);
    
    	Button button = new Button("Close");
    	button.setOnAction(new EventHandler<ActionEvent>() {
              @Override
              public void handle(ActionEvent event) {
            	  dialog.close();
            	  System.out.println("About Your Team Dialog Closed");
              }
          });

        
        grid.add(pictureRegion[0], 2,2);
        grid.add(member1_github_label, 3, 2);
        
        grid.add(member2_name_label, 0, 3);
        grid.add(member2_itsc_label, 1, 3);
        grid.add(pictureRegion[1], 2, 3);
        grid.add(member2_github_label, 3, 3);

        grid.add(member3_name_label, 0, 4);
        grid.add(member3_itsc_label, 1, 4);
        grid.add(pictureRegion[2], 2, 4);
        grid.add(member3_github_label, 3, 4);
        
        
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

    public void setHostServices(HostServices obj) {
    	hservices = obj;
    }
    
    
}

