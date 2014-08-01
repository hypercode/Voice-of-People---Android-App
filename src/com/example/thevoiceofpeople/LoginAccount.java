package com.example.thevoiceofpeople;

public class LoginAccount {
	
	private String email;
	private String password;
	
	LoginAccount(){
		email=null;
		password=null;
	}
	void setEmail(String userName_email){
		this.email=userName_email;
	}
	void setPassword(String password){
		this.password=password;
	}
	String getEmail(){
		return email;
	}
	String getPassword(){
		return password;
	}

}
