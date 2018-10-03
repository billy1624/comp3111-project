/**
 * 
 */
package comp3111.webscraper;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableView;
import java.util.List;
import java.net.URI;

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
    
    private TableView title; 
   
    private TableView price; 
    
    private TableView url; 
    
    private TableView posted; 
    
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
    	int num = 0 ;
    	double TotalPrice = 0;
    	double LPrice = 0;
    	String link = "";

    	System.out.println("actionSearch: " + textFieldKeyword.getText());
    	List<Item> result = scraper.scrape(textFieldKeyword.getText());
    	String output = "";
    	for (Item item : result) {
    			output += item.getTitle() + "\n" + item.getPrice() + "\n" + item.getUrl() + "\n";
   		
    		if (num == 0)
    		{
    			LPrice = item.getPrice();
    		} 
	   		else
	   			if (LPrice >  item.getPrice())
	   			{
	   				 LPrice = item.getPrice();
	   				 link = item.getUrl();
	   			}
    		
    	
    			TotalPrice += item.getPrice(); 
	   		 	num += 1; 
	   		 
    		
    	}
    	textAreaConsole.setText(output);
    	
    	labelCount.setText(Integer.toString(num));
    	
    	if (num == 0){
    		labelPrice.setText("-");
			labelMin.setText("-");
			labelLatest.setText("-");
		}
		else
		{
			labelPrice.setText(Double.toString(TotalPrice/num));
			labelMin.setText("<Lowest>");
			labelLatest.setText("<Latest>");
		}
    	
    	title.setItems(null);
    	//price.setValue();
    }
    
    /**
     * Called when the new button is pressed. Very dummy action - print something in the command prompt.
     */
    @FXML
    private void actionNew() {
    	System.out.println("actionNew");
    }
}

