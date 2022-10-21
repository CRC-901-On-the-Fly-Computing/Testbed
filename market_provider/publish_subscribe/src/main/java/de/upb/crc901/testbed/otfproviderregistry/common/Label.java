package de.upb.crc901.testbed.otfproviderregistry.common;

/**
 * Provides functions for the self-stabilizing protocol for the pub-/sub-system written by A1.
 * 
 * @author Michael
 *
 */
public class Label {

	/**
	 * Defines the function l that is used to generate labels for subscribers.
	 * 
	 * @param n
	 * @return
	 */
	public static String generateLabel(int n) {
		String bin_n = Integer.toBinaryString(n);
		// Move first char to end:
		bin_n = bin_n.substring(1) + bin_n.charAt(0);
		return bin_n;
	}

	/**
	 * Defines the function l that is used to generate labels for subscribers.
	 * 
	 * @param n
	 * @return
	 */
	public static String convertFloatLabel(float label) {
		if (label == 1f)
			return "0";
		float sub = 1f / 2f;
		String erg = "";
		while (label != 0f) {
			if (label - sub >= 0) {
				label = label - sub;
				erg = erg + "1";
			} else
				erg = erg + "0";
			sub = sub * (1f / 2f);
		}
		if (erg.equals(""))
			return "0";
		return erg;
	}

	/**
	 * Returns the float representation in [0,1) for the given label.
	 * 
	 * @param label
	 * @return
	 */
	public static float convertLabelToFloat(String label) {
		float erg = 0;
		for (int i = 1; i <= label.length(); i++) {
			String digit = String.valueOf(label.charAt(i - 1));
			float f = Float.valueOf(digit);
			erg = (float) (erg + f / Math.pow(2, i));
		}
		return erg;
	}

	/**
	 * Computes the inverse function l^-1.
	 * 
	 * @param label
	 * @return
	 */
	public static int reconvertLabelToNumber(String label) {
		if (label.length() > 1)
			label = label.charAt(label.length() - 1) + label.substring(0, label.length() - 1);
		return Integer.parseInt(label, 2);
	}
}
