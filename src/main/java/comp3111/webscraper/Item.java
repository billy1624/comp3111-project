package comp3111.webscraper;


import javafx.beans.property.SimpleStringProperty;
import java.util.Date;

/**
 * Item class represent a item posted on a portal, which implements Comparable interface
 * @author Chan Chi Wa - cwchanbf
 * @author Ngan Cheuk Hei - chnganaa
 */
public class Item implements Comparable<Item> {

	/**
	 * The title of the item posted on a portal
	 * @author Chan Chi Wa - cwchanbf
	 */
	private String title ;

	/**
	 * The price of the item posted on a portal
	 * @author Chan Chi Wa - cwchanbf
	 */
	private double price ;

	/**
	 * The portal {@link Portal} of the item posted on
	 * @author Chan Chi Wa - cwchanbf
	 */
	private Portal portal;

	/**
	 * The url of the posted item
	 * @author Chan Chi Wa - cwchanbf
	 */
	private String url ;

	/**
	 * The posted date {@link Date} of the item
	 * @author Chan Chi Wa - cwchanbf
	 */
	private Date postedOn;


	/**
	 * A default constructor
	 * @author Chan Chi Wa - cwchanbf
	 * @author Ngan Cheuk Hei - chnganaa
	 */
	public Item() {

	}


	/**
	 * A constructor initializing all instance variables
	 * @param title Title of the item
	 * @param price Price of the item
	 * @param portal Portal of the item
	 * @param url Url of the item
	 * @param postedOn Posted date of the item
	 * @author Chan Chi Wa - cwchanbf
	 * @author Ngan Cheuk Hei - chnganaa
	 */
	public Item(String title, double price, Portal portal, String url, Date postedOn) {
		this.title = title;
		this.price = price;
		this.portal = portal;
		this.url = url;
		this.postedOn = postedOn;
	}


	/**
	 * Get the title of the item
	 * @return Title of the item
	 * @author Chan Chi Wa - cwchanbf
	 * @author Ngan Cheuk Hei - chnganaa
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * Set the title of the item
	 * @param title Title of the item
	 * @author Chan Chi Wa - cwchanbf
	 */
	public void setTitle(String title) {
		this.title = title;
	}


	/**
	 * Get the price of the item
	 * @return Price of the item
	 * @author Chan Chi Wa - cwchanbf
	 * @author Ngan Cheuk Hei - chnganaa
	 */
	public double getPrice() {
		return price;
	}


	/**
	 * Set the price of the item
	 * @param price Price of the item
	 * @author Chan Chi Wa - cwchanbf
	 */
	public void setPrice(double price) {
		this.price = price;
	}


	/**
	 * Get the portal {@link Portal} of the item
	 * @return Portal of the item
	 * @author Chan Chi Wa - cwchanbf
	 * @author Ngan Cheuk Hei - chnganaa
	 */
	public Portal getPortal() {
		return portal;
	}


	/**
	 * Set the portal {@link Portal} of the item
	 * @param portal Portal of the item
	 * @author Chan Chi Wa - cwchanbf
	 */
	public void setPortal(Portal portal) {
		this.portal = portal;
	}


	/**
	 * Get the url of the item
	 * @return Url of the item
	 * @author Chan Chi Wa - cwchanbf
	 * @author Ngan Cheuk Hei - chnganaa
	 */
	public String getUrl() {
		return url;
	}


	/**
	 * Set the url of the item
	 * @param url Url of the item
	 * @author Chan Chi Wa - cwchanbf
	 */
	public void setUrl(String url) {
		this.url = url;
	}


	/**
	 * Get the posted date {@link Date} of the item
	 * @return Posted date of the item
	 * @author Chan Chi Wa - cwchanbf
	 * @author Ngan Cheuk Hei - chnganaa
	 */
	public Date getPostedOn() {
		return postedOn;
	}


	/**
	 * Set the posted date {@link Date} of the item
	 * @param postedOn Posted date of the item
	 * @author Chan Chi Wa - cwchanbf
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}


	/**
	 * Override the toString method for item class
	 * @return String representation of the item
	 * @author Chan Chi Wa - cwchanbf
	 * @author Ngan Cheuk Hei - chnganaa
	 */
	@Override
	public String toString() {
		return "Item{" +
				"title='" + title + '\'' +
				", price=" + price +
				", portal=" + portal +
				", url='" + url + '\'' +
				", postedOn=" + postedOn +
				'}';
	}

	/**
	 * Implement compareTo method {@link Comparable}
	 * @return A negative integer, zero, or a positive integer as this item's price
	 *         is less than, equal to, or greater than the specified item's price.
	 * @author Chan Chi Wa - cwchanbf
	 * @author Ngan Cheuk Hei - chnganaa
	 */ 
	@Override
	public int compareTo(Item o) {
		return Double.compare(price, o.price);
	}


}
