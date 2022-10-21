package de.upb.crc901.testbed.patriciatrie;

import de.upb.crc901.testbed.otfproviderregistry.common.Hashing;

/**
 * Models a publication. The id should be equal to either the id of the {@link ServiceProvider} that generated the publication, or to the requester
 * adress, in case the publication was initiated by a ServiceRequester. The content is either the {@link ServiceProvider} itself, or the request of
 * the ServiceRequester (as a String).
 * 
 * @author Michael
 *
 */
public class Publication {
	static final int BITSTRING_SIZE = 30;

	private final String id;
	private final Object content;
	private final long timeoutAfter;
	private final long timestamp;

	public Publication(String id, Object content) {
		this.id = id;
		this.content = content;
		this.timeoutAfter = Long.MAX_VALUE;
		this.timestamp = System.currentTimeMillis();
	}

	public Publication(String id, Object content, long timeoutAfter) {
		this.id = id;
		this.content = content;
		this.timeoutAfter = timeoutAfter;
		this.timestamp = System.currentTimeMillis();
	}

	/**
	 * Returns true, if the request stored in this publication has timed out.
	 * 
	 * @return
	 */
	public boolean requestTimedOut() {
		if (timeoutAfter == Long.MAX_VALUE)
			return false;
		return (System.currentTimeMillis() - timestamp) > timeoutAfter;
	}

	public String getPublicationLabel() {
		return Hashing.hashStringtoBitstring(id + content.toString(), BITSTRING_SIZE);
	}

	public Object getContent() {
		return content;
	}

	public String toString() {
		return "Publication: " + id + ", " + content.toString();
	}
}
