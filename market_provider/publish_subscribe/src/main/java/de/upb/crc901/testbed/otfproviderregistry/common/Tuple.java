package de.upb.crc901.testbed.otfproviderregistry.common;

/**
 * Just a container for a pair of Objects that may differ in their types.
 * 
 * @author Michael
 *
 * @param <X>
 * @param <Y>
 */
public class Tuple<X, Y> {
	public X x;
	public Y y;

	public Tuple(X x, Y y) {
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return "(" + (x != null ? x.toString() : "null") + ", " + (y != null ? y.toString() : "null") + ")";
	}
}