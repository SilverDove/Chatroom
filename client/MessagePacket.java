package client;

import java.io.Serializable;

public class MessagePacket implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 01;
	
	private String receiver;
	private String sender;
	private String message;
	private AES encryptor;
	private boolean isPrivate = true;
	
	/**
	 * Constructor used when creating a packet containing a private message
	 * 
	 * @param _receiver
	 * @param _sender
	 * @param _message
	 * @param _encryptor
	 */
	public MessagePacket(String _receiver, String _sender, String _message, AES _encryptor)
	{
		receiver = _receiver;
		sender = _sender;
		message = _message;
		encryptor = _encryptor;
		isPrivate = true;
		
	}
	
	/**
	 * Constructor used when creating a packet containing a public message
	 * 
	 * @param _receiver
	 * @param _sender
	 * @param _message
	 */
	public MessagePacket(String _sender, String _message)
	{
		sender = _sender;
		message = _message;
		receiver = null;
		encryptor = null;
		isPrivate = false;
	}
	
	//throws error if message is public / isPrivate = false
	public String getReceiver()
	{
		return receiver;
	}
	
	public String getSender()
	{
		return sender;
	}
	
	
	public String getMessage()
	{
		return message;
	}
	
	public boolean getPacketState()
	{
		return isPrivate;
	}
	
	//throws error if message is public / isPrivate = false
	public void encryptMessage()
	{
		message = encryptor.encrypt(message);
	}
	
	//throws error if message is public / isPrivate = false
	public void decryptMessage()
	{
		message = encryptor.decrypt(message);
	}
}
