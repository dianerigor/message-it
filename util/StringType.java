/**
 * 
 */
package org.aprs.messageit.util;

/**
 * @author toshiba
 *
 */
public enum StringType {
	EMAILADDRESS, NUMBER, COMMAND, ABBR;
	
	public static StringType getStringType(String s){
		final int comLength = 1;
		final int abbrLength = 3;
		
		boolean comTest = false;
		boolean abbrTest = false;
		boolean addressTest = false;
		boolean numberTest = false;
		
		//Command
		if(s.length() == comLength){
			comTest = true;
		}
		
		//Abbr.
		if (s.length() == abbrLength){
			abbrTest = true;
		}
		
		//Email Address
		if (s.indexOf("@") != -1){
			addressTest = true;
		}
		
		//Number
		numberTest = true;
		for (char c : s.toCharArray()){
			if (c < '0' || c > '9')
				numberTest = false;
		}
		
		//Final
		if(comTest)
			return StringType.COMMAND;
		else if(abbrTest)
			return StringType.ABBR;
		else if(addressTest)
			return StringType.EMAILADDRESS;
		else if(numberTest)
			return StringType.NUMBER;
		
		return null;
	}
}
