package com.example.thevoiceofpeople;

public class SignupAccount {
	
	private String userName;
	private String email;
	private String password;
	
	SignupAccount(){
		userName=null;
		email=null;
		password=null;
	}
	void setUserName(String userName){
		this.userName=userName;
	}
	void setEmail(String email){
		this.email=email;
	}
	void setPassword(String password){
		this.password=password;
	}
	String getUserName(){
		return userName;
	}
	String getEmail(){
		return email;
	}
	String getPassword(){
		return password;
	}

}