package Items;

/**
 * Message Class that stores information about the messages that are sent
 * @param idUser1   id of the sender of the message
 * @param idUser2   id of the receiver of the message
 * @param idMessage    id of the message sent
 * @param text   text of the message sent
 * @param timeSent time value of the message
 */

public class Message {
	
	private int idUser1,idUser2, idMessage;  
	private String text,timeSent; 
	
	public Message(int idUser1,int idUser2, int idMessage, String text, String timeSent) {
		this.idUser1 = idUser1;
		this.idUser2 = idUser2;
		this.idMessage = idMessage;
		this.text = text;
		this.timeSent = timeSent;	
	}
	public String toString() { 
		return "idUser1: "+this.idUser1 +" idUser2: "+this.idUser2 +" idMessage: "+this.idMessage +" Text: "+this.text +" TimeSent: "+ this.timeSent;
	}	
	public void setIdUser1(int idUser1) { 
		this.idUser1 = idUser1;
	}
	public void setIdUser2(int idUser2) { 
		this.idUser2 = idUser2;
	}
	public void setIdMessage(int idMessage) { 
		this.idMessage = idMessage;
	}
	public void setText(String text) { 
		this.text = text ;
	}
	public void setTimeSent(String timeSent) { 
		this.timeSent = timeSent ;
	}	
	public int getIdUser1() { 
		return this.idUser1;
	}
	public int getIdUser2() { 
		return this.idUser2;
	}
	public int getIdMessage() {
		return this.idMessage;
	}
	public String getText() { 
		return this.text;
	}
	public String getTimeSent() { 
		return this.timeSent;
	}
	
	/**
	 * equal method
	 * @return true if the message is the same as the one taken in parametre
	 */
	
	public boolean equals(Object o) {
		if (this == o) return true;
		
		if (o == null || getClass()!=o.getClass()) {
			return false;
		}
		
		Message m = (Message) o;
		
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(idUser1, m.idUser1)
				.append(idUser2, m.idUser2)
				.append(idMessage, m.idMessage)
				.append(text, m.text)
				.append(timeSent, m.timeSent)
				.isEquals();
		
	}	
	
}
