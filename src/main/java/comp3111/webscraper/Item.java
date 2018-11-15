package comp3111.webscraper;

import java.util.Date;

public class Item implements Comparable<Item> {

	/**
	 * 
	 * @author Chan Chi Wa - cwchanbf
	 */
	private String title ; 
	private double price ;
	private Portal portal;
	private String url ;
	private Date postedOn;
	private String posted_date; // TODO: Need to remove

	public Item() {

	}

	public Item(String title, double price, Portal portal, String url, Date postedOn) {
		this.title = title;
		this.price = price;
		this.portal = portal;
		this.url = url;
		this.postedOn = postedOn;
	}
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Portal getPortal() {
		return portal;
	}

	public void setPortal(Portal portal) {
		this.portal = portal;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getPosted_date() {
		return posted_date;
	}
	public void setPosted_date(String posted_date) {
		this.posted_date = posted_date;
	}
	

	public Date getPostedOn() {
		return postedOn;
	}

	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

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

	@Override
	public int compareTo(Item o) {
		return Double.compare(price, o.price);
	}
}
