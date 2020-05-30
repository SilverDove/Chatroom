package ChatroomTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Items.Message;

public class MessageTest {

	@Test
	public void testToString() {
		Message msg = new Message(1,2,10,"Test msg","Date test");
		
		String expected = "idUser1: 1 idUser2: 2 idMessage: 10 Text: Test msg TimeSent: Date test";
		String actual = msg.toString();
		
		assertEquals(expected,actual);
	}

	@Test
	public void testSetIdUser1() {
		
		Message msgExpected = new Message(1,2,10,"Test msg","Date test");
		
		Message msgActual = new Message(87,2,10,"Test msg","Date test");
		msgActual.setIdUser1(1);
		
		assertEquals(msgExpected,msgActual);
	}

	@Test
	public void testSetIdUser2() {
		Message msgExpected = new Message(1,2,10,"Test msg","Date test");
		
		Message msgActual = new Message(1,89,10,"Test msg","Date test");
		msgActual.setIdUser2(2);
		
		assertEquals(msgExpected,msgActual);
	}

	@Test
	public void testSetIdMessage() {
		Message msgExpected = new Message(1,2,10,"Test msg","Date test");
		
		Message msgActual = new Message(1,2,99,"Test msg","Date test");
		msgActual.setIdMessage(10);
		
		assertEquals(msgExpected,msgActual);
	}

	@Test
	public void testSetText() {
		Message msgExpected = new Message(1,2,10,"Test msg","Date test");
		
		Message msgActual = new Message(1,2,10,"HELLO WORLD","Date test");
		msgActual.setText("Test msg");
		assertEquals(msgExpected,msgActual);
	}

	@Test
	public void testSetTimeSent() {
		Message msgExpected = new Message(1,2,10,"Test msg","Date test");
		
		Message msgActual = new Message(1,2,10,"Test msg","RANDOM");
		msgActual.setTimeSent("Date test");
		assertEquals(msgExpected,msgActual);
	}

	@Test
	public void testGetIdUser1() {
		Message msg = new Message(1,2,10,"Test msg","Date test");
		
		int expected = 1;
		int actual = msg.getIdUser1();
		assertEquals(expected,actual);
	}

	@Test
	public void testGetIdUser2() {
		Message msg = new Message(1,2,10,"Test msg","Date test");
		
		int expected = 2;
		int actual = msg.getIdUser2();
		assertEquals(expected,actual);
	}

	@Test
	public void testGetIdMessage() {
		Message msg = new Message(1,2,10,"Test msg","Date test");
		
		int expected = 10;
		int actual = msg.getIdMessage();
		assertEquals(expected,actual);
	}

	@Test
	public void testGetText() {
		Message msg = new Message(1,2,10,"Test msg","Date test");
		
		String expected = "Test msg";
		String actual = msg.getText();
		assertEquals(expected,actual);
	}

	@Test
	public void testGetTimeSent() {
		Message msg = new Message(1,2,10,"Test msg","Date test");
		
		String expected = "Date test";
		String actual = msg.getTimeSent();
		assertEquals(expected,actual);
	}

}
