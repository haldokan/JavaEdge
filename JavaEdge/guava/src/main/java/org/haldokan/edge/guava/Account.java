package org.haldokan.edge.guava;


public class Account {

	private String owner;
	private String id;
	private double balance;

	public Account(String owner, String id, double balance) {
		super();
		this.owner = owner;
		this.id = id;
		this.balance = balance;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

}
