
public class Message {
	
	private int idUser, idMessage;
	private String text,timeSent;
	
	public Message(int idUser, int idMessage, String text, String timeSent) {
		this.idUser = idUser;
		this.idMessage = idMessage;
		this.text = text;
		this.timeSent = timeSent;	
	}
	public String toString() { // Override the toString method		
		System.out.println("idUser: "+this.idUser +"   idMessage: "+this.idMessage +"   Text: "+this.text +"   TimeSent: "+ this.timeSent);
		return null;
	}	
	public void setIdUser(int idUser) {
		this.idUser = idUser;
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
	public int getIdUser() {
		return this.idUser;
	}
	public int getIdMessage() {
		return this.idMessage;
	}
	public String getText() {
		return this.text;
	}
	public String getTimesent() {
		return this.timeSent;
	}
	
}
