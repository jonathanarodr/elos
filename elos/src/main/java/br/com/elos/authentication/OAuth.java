package br.com.elos.authentication;

public class OAuth {
 
	private String accessToken;
	private String[] credentials;
	
	public OAuth(String[] credentials) {
		this.credentials = credentials;
	}
	
}
