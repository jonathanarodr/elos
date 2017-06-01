package br.com.elos.validation;

import java.util.Map;

public interface Validator {
	
	public Map<String,String> rules();
	public Map<String,String> messages();

}