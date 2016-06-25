package com.example.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/*	
 * The Utils of encryption
 * encrypt the password use the type MD5
 */
public class MD5Util {
	public static String passwordMD5(String password) {
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] digest = messageDigest.digest(password.getBytes());
			for (int j = 0; j < digest.length; j++) {
				int result = digest[j] & 0xff;
				String hexString = Integer.toHexString(result);
				if (hexString.length() < 2){
					sb.append("0");
				}
				sb.append(hexString);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
}
