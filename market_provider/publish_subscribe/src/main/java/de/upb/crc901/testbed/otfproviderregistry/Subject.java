package de.upb.crc901.testbed.otfproviderregistry;

/**
 * Classes implementing this interface periodically execute timeout() and are able to send messages to other Subjects via receiveMessage.
 * 
 * @author Michael
 *
 */
public interface Subject {

	/**
	 * A method that has to be executed in each iteration of the Threads run method.
	 */
	public void timeout();

	/**
	 * Returns the (unique) id of this subject.
	 * 
	 * @return
	 */
	public String getIdentifier();

	/**
	 * The subject receives the given message. It is guaranteed that the message will eventually (but not necessary immediately) be processed by the
	 * subject.
	 * 
	 * @param m
	 */
	public void receiveMessage(Object m);
}
