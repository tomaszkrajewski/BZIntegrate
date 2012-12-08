package com.tk.bzintegration;

public interface Authenticator {

	public static final String SUCCESS = "SUCCESS";
	
	/**
	 * Must return Authenticator.SUCCESS when credientals are 
	 * correct or any other string otherwise.
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public String authenticateUser(String username, char[] password) throws Exception;
}
