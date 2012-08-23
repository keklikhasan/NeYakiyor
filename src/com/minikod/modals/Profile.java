package com.minikod.modals;

public class Profile {
	public int id;
	public String code;
	public boolean active;
	public int defType;
	
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
	public int getDefType() {
		return defType;
	}
	public void setDefType(int defType) {
		this.defType = defType;
	}
	
	
}
