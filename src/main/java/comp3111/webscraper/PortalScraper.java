package comp3111.webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.scene.control.TextArea;

import java.util.Date;
import java.util.List;

public abstract class PortalScraper {
	public abstract List<Item> scrape(WebClient client, String keyword, TextArea textAreaConsole);
}
