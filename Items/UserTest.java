package Items;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UserTest {

	@Test
	public void testToString() {
		User user = new User("JS","John","Smith","1234");
		
		String expected = "Username: JS Firstname: John Lastname: Smith Password: 1234";
		String actual = user.toString();

		assertEquals(expected,actual);
	}

	@Test
	public void testSetUsername() {
		User userExpected = new User("JS","John","Smith","1234");
		User userActual = new User("INCORRECT","John","Smith","1234");
		userActual.setUsername("JS");

		assertEquals(userExpected,userActual);
	}

	@Test
	public void testSetFirstname() {
		User userExpected = new User("JS","John","Smith","1234");
		User userActual = new User("JS","INCORRECT","Smith","1234");
		userActual.setFirstname("John");

		assertEquals(userExpected,userActual);
	}

	@Test
	public void testSetLastname() {
		User userExpected = new User("JS","John","Smith","1234");
		User userActual = new User("JS","John","INCORRECT","1234");
		userActual.setLastname("Smith");

		assertEquals(userExpected,userActual);
	}

	@Test
	public void testSetPassword() {
		User userExpected = new User("JS","John","Smith","1234");
		User userActual = new User("JS","John","Smith","INCORRECT");
		userActual.setPassword("1234");

		assertEquals(userExpected,userActual);
	}

	@Test
	public void testGetUsername() {
		User user = new User("JS","John","Smith","1234");
		
		String expected = "JS";
		String actual = user.getUsername();
		
		assertEquals(expected,actual);
	}

	@Test
	public void testGetLastname() {
		User user = new User("JS","John","Smith","1234");
		
		String expected = "Smith";
		String actual = user.getLastname();
		
		assertEquals(expected,actual);
	}

	@Test
	public void testGetFirstname() {
		User user = new User("JS","John","Smith","1234");
		
		String expected = "John";
		String actual = user.getFirstname();
		
		assertEquals(expected,actual);
	}

	@Test
	public void testGetPassword() {
		User user = new User("JS","John","Smith","1234");
		
		String expected = "1234";
		String actual = user.getPassword();
		
		assertEquals(expected,actual);
	}

}
