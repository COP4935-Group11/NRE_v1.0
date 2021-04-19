package com.driver;

import java.util.LinkedList;

public class DriverStack{
	
	protected LinkedList<Driver> DriverStoragePool;
	
	public LinkedList<Driver> getStack(){return this.DriverStoragePool;}
	
	public DriverStack() {
		
		this.DriverStoragePool = new LinkedList<>();
		
	}
		
	
	public void push(Driver driver) {
		
		this.DriverStoragePool.push(driver);		
		
	}
	
	public void quit() {
		
		this.DriverStoragePool.pop().driver.quit();		
		
	}
	
	public void reset() {
		
		this.DriverStoragePool = new LinkedList<>();		
		
	}
		
}