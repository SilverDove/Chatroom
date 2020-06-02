package test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.AES;

/**
 * @author Karen_DELL_13
 *
 */
class AEStest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz"; 
		
		Random rand = new Random();
		int upperbound = 1000;
		int  n = rand.nextInt(upperbound); 
		
		StringBuilder sb = new StringBuilder(n); 
		
		for (int i = 0; i < n; i++) { 
			int index = (int)(AlphaNumericString.length() * Math.random()); 
			sb.append(AlphaNumericString.charAt(index)); 
		}
		
		String text = sb.toString(); 
		
		AES aes = new AES();
		
		String coded = aes.encrypt(text);
		String decrypted = aes.decrypt(coded);
		
		assertTrue(text.equals(decrypted));
		
		
		
		
	}

}
