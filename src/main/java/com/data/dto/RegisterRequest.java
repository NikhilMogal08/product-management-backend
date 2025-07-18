package com.data.dto;


public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String role;
    private String mobileNumber;
    private String address;
    
	public RegisterRequest() {
		super();
	}
	
	
	public RegisterRequest(String name, String email, String password, String role,String mobileNumber,String address) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.mobileNumber=mobileNumber;
		this.address=address;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}


	public String getMobileNumber() {
		return mobileNumber;
	}


	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}
    
    
}
