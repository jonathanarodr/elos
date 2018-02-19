package br.com.elos.authentication;

import javax.servlet.http.HttpServletRequest;

public class Auth {
	
	private HttpServletRequest request;
	private String[] credentials;
	
	public Auth(HttpServletRequest request) {
		this.request = request;
	}
	
	public void access(String[] credentials) {
		this.credentials = credentials;
	}

}
