package com.minikod.modals;

import java.util.Date;

public class Record {

	public int id;
	public int type;
	public Double unitPrice;
	public Double fuelByPrice;
	public Double fuel;
	public Double km;
	public Date date;
	public int profile;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getFuelByPrice() {
		return fuelByPrice;
	}

	public void setFuelByPrice(Double fuelByPrice) {
		this.fuelByPrice = fuelByPrice;
	}

	public Double getFuel() {
		return fuel;
	}

	public void setFuel(Double fuel) {
		this.fuel = fuel;
	}

	public Double getKm() {
		return km;
	}

	public void setKm(Double km) {
		this.km = km;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProfile() {
		return profile;
	}

	public void setProfile(int profile) {
		this.profile = profile;
	}
	
	

}
