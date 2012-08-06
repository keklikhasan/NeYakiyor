package com.minikod.modals;

public class Profile {
	public int id;
	public String code;
	public boolean active;
	public int fuel_type;
	public Double unit_price;
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getFuel_type() {
		return fuel_type;
	}

	public void setFuel_type(int fuel_type) {
		this.fuel_type = fuel_type;
	}

	public Double getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(Double unit_price) {
		this.unit_price = unit_price;
	}

}
