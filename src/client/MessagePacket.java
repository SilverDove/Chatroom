package client;

import java.io.Serializable;

/**
 * Date: 30-05-2020
 * This class is based on the actual principle of packet exchange in networking
 * We use this class to pack all the information about the sender and receiver so the packet properly reaches the destination
 * It contains a field AES for encryption and deciphering if necessary 
 * It also contains the message itself to be transmitted
 * 
 * With the contribution of TABOU Metagang & KASPAR Karen
 * @author JATOB Nicolas
 */
public class MessagePacket implements Serializable{
	
	/**
	 * long object holding the serialVersionUID
	 */
	private static final long serialVersionUID = 01;
	
	/**
	 * String holding the user's name who is the recipient
	 */
	private String receiver;
	
	/**
	 * String holding the user's name who is the sender
	 */
	private String sender;
	
	/**
    * String holding the message to be transmitted	
    */
	private String message;
	
	/**
	 * AES object for the encryption and decryption of the message
	 */
	private AES encryptor;
	
	/**
	 * Boolean indicating the status of the packet, private or not
	 */
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
	
	/**
	 * This method returns the name of the recipient
     * @return string name of the message's recipient
	 */
	public String getReceiver()
	{
		return receiver;
	}
	
	/**
	 * This method returns the name of the sender
     * @return string name of the message's sender
	 */
	public String getSender()
	{
		return sender;
	}
	
	/**
	* This method returns the message to be communicated
     * @return string value of the message
	 */
	public String getMessage()
	{
		return message;
	}
	
	/**
	 * This method indicates whether or not the packet is private
     * @return boolean value depending on the packet's state
	 */
	public boolean getPacketState()
	{
		return isPrivate;
	}
	
	/**
	 * This method encrypts the message
	 */
	public void encryptMessage()
	{
		message = encryptor.encrypt(message);
	}
	
	/**
	 * This method decipher the message
	 */
	public void decryptMessage()
	{
		message = encryptor.decrypt(message);
	}
}
