package Items;


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
		return "idUser1: "+this.idUser1 +"idUser2: "+this.idUser2 +"   idMessage: "+this.idMessage +"   Text: "+this.text +"   TimeSent: "+ this.timeSent;
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
	public String getTimesent() {
		return this.timeSent;
	}
	
}
