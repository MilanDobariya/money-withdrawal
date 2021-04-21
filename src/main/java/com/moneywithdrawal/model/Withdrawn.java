package com.moneywithdrawal.model;

public class Withdrawn {

	private double amount;
	private int noteType;
	private int quantity;
	private int charge;
	private String currencyType;
	
	
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public int getCharge() {
		return charge;
	}
	public void setCharge(int charge) {
		this.charge = charge;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public void setNoteType(int noteType) {
		this.noteType = noteType;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getNoteType() {
		return noteType;
	}
}
