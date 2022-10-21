package de.upb.crc901.testbed.otfproviderregistry.common;

import de.upb.crc901.testbed.otfproviderregistry.Subject;

/**
 * A configuration can be generated by the OTFProviderRegistry and sent to an OTFProvider. It modifies the overall graph topology of the
 * pub-/sub-system.
 * 
 * @author Michael
 *
 */
public class Configuration {
	public final Tuple<String, Subject> left, right;
	public final String label;

	public Configuration(Tuple<String, Subject> left, String label, Tuple<String, Subject> right) {
		this.left = left;
		this.right = right;
		this.label = label;
	}

	public String toString() {
		String l = left == null ? "null" : left.toString();
		String r = right == null ? "null" : right.toString();
		return "[" + l + ", " + label + ", " + r + "]";
	}
}
