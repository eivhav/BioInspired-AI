package flatlandv1;

import java.util.ArrayList;

public class ToolBox {
	
	
	public ArrayList<Integer> getDigitNumbers(String s){
		ArrayList<Integer> digits = new ArrayList<Integer>();   
		ArrayList<Integer> results = new ArrayList<Integer>(); 
		int number = 0;   
		boolean numFound = false; 
		
		for(int i = 0;  i < s.length(); i++){
			char c = s.charAt(i); 
			
			if(!Character.isDigit(c) && !numFound){ 
			}
			else if(Character.isDigit(c)){
				digits.add(Character.getNumericValue(c)); 
				numFound = true; 
			}
			if((!Character.isDigit(c) || i == (s.length()-1)) && numFound){  
				int k = digits.size()-1; 
				for(int j = 0; j < digits.size(); j++){
					number = (int) (number + (digits.get(j)*(Math.pow(10,k))));  
					k--; 
				}
				results.add(number); 
				numFound = false; 
				digits = new ArrayList<Integer>(); 	
				number = 0; 
			}
		}	
		return results; 	
				
	}
	
	
	public ArrayList<Double> getDoubleDigitNumbers(String s){
		ArrayList<Integer> digits = new ArrayList<Integer>();   
		ArrayList<Integer> decdigits = new ArrayList<Integer>();  
		ArrayList<Double> results = new ArrayList<Double>(); 
		double number = 0;    
		boolean numFound = false; 
		boolean decFound = false; 
		boolean negative = false; 
		s = s+" "; 
		
		for(int i = 0;  i < s.length(); i++){
			char c = s.charAt(i); 
			
			if(!Character.isDigit(c) && !numFound && c != '.' && c!= '-'){ 
			}
			else if(c == '-'){
				negative = true; 	
			}
			else if(c == '.' && numFound){
				decFound = true; 
			}
			else if(Character.isDigit(c) && !decFound){
				digits.add(Character.getNumericValue(c)); 
				numFound = true; 
			}
			else if(Character.isDigit(c) && decFound){
				decdigits.add(Character.getNumericValue(c));  
			}
			
			
			else if(!Character.isDigit(c) && numFound){  
				int k = digits.size()-1; 
				for(int j = 0; j < digits.size(); j++){
					number = (int) (number + (digits.get(j)*(Math.pow(10,k))));  
					k--; 
				}
				if(decFound){ 
					for(int j= 0; j < decdigits.size(); j++){
						number = (number + (decdigits.get(j)*(Math.pow(10,-j-1))));  
					}
					
				}
				
				if(negative){
					number = number*(-1);  
				}
				results.add(number); 
				numFound = false; 
				decFound = false; 
				negative = false; 
				digits = new ArrayList<Integer>();
				decdigits = new ArrayList<Integer>();
				number = 0; 
			}
		}	
		
		return results; 
	}

}
