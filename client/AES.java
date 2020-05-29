package client;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
 
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SecretKeySpec secretKey;
	private byte[] key;
	
	public AES() {
		setKey();
	}
	
	private static String randomString(int n) {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYYZ" + "0123456789" + "abcdefghijklmnopqrstuvwxyz";
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			int random = (int)(AlphaNumericString.length()*Math.random());
			sb.append(AlphaNumericString.charAt(random));
		}
		return sb.toString();
	}
	
	private void setKey() {
		String myKey = randomString(256);
		MessageDigest sha = null;
		try {
			this.key = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			this.key = sha.digest(this.key);
			this.key = Arrays.copyOf(this.key, 16);
			this.secretKey = new SecretKeySpec(this.key, "AES");
		}catch(NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}catch(UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public String encrypt(String clearText) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(clearText.getBytes("UTF-8")));
		}catch(Exception ex) {
			System.out.println("Error while encrypting: " + ex.toString());
		}
		return null;
	}
	
	public String decrypt(String encodedText) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(encodedText.getBytes("UTF-8"))));
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("Error while decrypting: " + ex.toString());
		}
		return null;
	}
}
	
     

        
