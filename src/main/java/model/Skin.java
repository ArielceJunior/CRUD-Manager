package model;

public class Skin {

	private int id;
	private String name;
	private String weapon;
	private Float waste;
	private User user;
	private Double price;

	public Skin() {}
	
	public Skin(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWeapon() {
		return weapon;
	}

	public void setWeapon(String weapon) {
		this.weapon = weapon;
	}

	public Float getWaste() {
		return waste;
	}

	public void setWaste(Float waste) {
		this.waste = waste;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
