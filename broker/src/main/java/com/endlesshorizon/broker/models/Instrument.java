//package com.endlesshorizon.broker.models;

//import lombok.Getter;
//import lombok.Setter;

//public class Instrument implements Instruments {
//	private float price;
//	private int quantity;

//	@Override
//	public void addStock(int stock) {
//		quantity += stock;
//		// TODO Auto-generated method stub
		
//	}
	
//	@Override
//	public void subStock(int stock) {
//		quantity -= stock;
//		// TODO Auto-generated method stub
		
//	}
	
//	public void setPrice(float price) {
//		this.price = price;
//	}

//	public float getPrice() {
//		return price;
//	}

//	public void setQuantity(int quantity) {
//		this.quantity = quantity;
//	}

//	public int getQuantity() {
//		return quantity;
//	}
	
//    @Override
//    public String toString() { 
//        return String.format("Price: " + price + "\nQuantity: " + quantity); 
//    } 

//	public static void main(String[] args) {
//		Instrument hat = new Instrument();
//		hat.setPrice(Float.parseFloat("12.8"));
//		hat.setQuantity(20);
//		hat.addStock(8);
//		hat.subStock(10);
//		System.out.println(hat.toString());
//	}

//}