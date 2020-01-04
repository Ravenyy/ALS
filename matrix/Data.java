package matrix;

public class Data {
	
	private int itemId;
	private String username;
	private int rating;
	private String category;
	
	public Data(int itemId, String username, int rating, String category) {
		this.itemId = itemId;
		this.username = username;
		this.rating = rating;
		this.category = category;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	
}
