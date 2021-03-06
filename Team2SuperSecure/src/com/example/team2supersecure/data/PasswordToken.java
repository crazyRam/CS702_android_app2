
/**
 * This whole PasswordToken class retrieved from 
 * stackoverflow.com/questions/11158568/creating-password-verification-for-my-android-application
 * on April 21, 2014
 */


package com.example.team2supersecure.data;

import java.security.MessageDigest;


public class PasswordToken {
	
	static public String makeDigest(String pwd){
		String hexStr = "";
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.reset();
			byte[] buffer = pwd.getBytes();
			md.update(buffer);
			byte[] digest = md.digest();
			
			for (int i = 0; i < digest.length; i++) {
				hexStr += Integer.toString( (digest[i] & 0xff) + 0x100, 16 ).substring(1);
			}
			
			/**
			 *  If the algorithm is not working for some reason on this device
			 *  we have to use the strings hash code, which
			 *  could allow duplicates but at least allows tokens
			 *   
			 */
		} catch(Exception e) {
			hexStr = Integer.toHexString(pwd.hashCode());
		}
		
		return hexStr;
	}
	
	static public boolean validate(String pwd, String token){
		String digestToken = "";
		String simpleToken = "";
		
		digestToken = makeDigest(pwd); 
		
		if (0 == digestToken.compareTo(token)) {
			return true;
		}
		
//		if (0 == simpleToken.compareTo(token)) {
//			return true;
//		}
		
		return false;
		
	}
	

}
