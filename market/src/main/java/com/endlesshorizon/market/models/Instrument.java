package com.endlesshorizon.market.models;

//import lombok.Getter;
//import lombok.Setter;

public class Instrument implements Instruments {
	private String name;
	private float price;
	private int quantity;

	public Instrument(String instrument, float price, int quantity) {
		setName(instrument);
		setPrice(price);
		setQuantity(quantity);
	}

	@Override
	public void addStock(int stock) {
		quantity += stock;
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void subStock(int stock) {
		quantity -= stock;
		// TODO Auto-generated method stub
		
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getPrice() {
		return price;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}
	
    @Override
    public String toString() { 
        return String.format("Instrument Name: " + name + "\nPrice: " + price + "\nQuantity: " + quantity); 
    } 

	// test purposes.
	//public static void main(String[] args) {
	//	float price = Float.parseFloat("12.8");
	//	int quantity = 20;
	//	Instrument item = new Instrument("eggs", price, quantity);
	//	System.out.println(item.toString());
	//}

}
