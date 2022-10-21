package de.upb.crc901.testbed.otfproviderregistry.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Provides various hash functions.
 * 
 * @author Michael
 *
 */
public class Hashing {

	/**
	 * Hashes the given id of a node to the interval [0,1) and returns the hash value as a float.
	 * 
	 * @param id
	 * @return
	 */
	public static float hashIntegerToFloat(int id) {
		String stringToEncrypt = String.valueOf(id) + String.valueOf(Integer.hashCode(id));
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		messageDigest.update(stringToEncrypt.getBytes());
		String encryptedString = new String(messageDigest.digest());
		int hash = encryptedString.hashCode() < 0 ? encryptedString.hashCode() * -1 : encryptedString.hashCode();

		// Convert to float:
		String s = String.valueOf(((float) hash) / Integer.MAX_VALUE);
		s = s + hash;
		float finalHash = Float.valueOf(s);
		if (finalHash == 0) {
			try {
				messageDigest = MessageDigest.getInstance("SHA-512");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			messageDigest.update(stringToEncrypt.getBytes());
			encryptedString = new String(messageDigest.digest());
			hash = encryptedString.hashCode() < 0 ? encryptedString.hashCode() * -1 : encryptedString.hashCode();

			// Convert to float:
			s = String.valueOf(((float) hash) / Integer.MAX_VALUE);
			s = s + hash;
			finalHash = Float.valueOf(s);
		}
		return finalHash;
	}

	/**
	 * Hashes the given id of a node to the interval [0,1) and returns the hash value as a float.
	 * 
	 * @param id
	 * @return
	 */
	public static float hashStringToFloat(String id) {
		String stringToEncrypt = id + id.hashCode();
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		messageDigest.update(stringToEncrypt.getBytes());
		String encryptedString = new String(messageDigest.digest());
		int hash = encryptedString.hashCode() < 0 ? encryptedString.hashCode() * -1 : encryptedString.hashCode();

		// Convert to float:
		String s = String.valueOf(((float) hash) / Integer.MAX_VALUE);
		s = s + hash;
		float finalHash = Float.valueOf(s);
		if (finalHash == 0) {
			try {
				messageDigest = MessageDigest.getInstance("SHA-512");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			messageDigest.update(stringToEncrypt.getBytes());
			encryptedString = new String(messageDigest.digest());
			hash = encryptedString.hashCode() < 0 ? encryptedString.hashCode() * -1 : encryptedString.hashCode();

			// Convert to float:
			s = String.valueOf(((float) hash) / Integer.MAX_VALUE);
			s = s + hash;
			finalHash = Float.valueOf(s);
		}
		return finalHash;
	}

	/**
	 * Probability for a collision depend on the bitstring_size!
	 * 
	 * @param id
	 * @return
	 */
	public static String hashStringtoBitstring(String input, int bitstring_size) {
		String stringToEncrypt = String.valueOf(input.hashCode());
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		messageDigest.update(stringToEncrypt.getBytes());

		byte[] bArray = messageDigest.digest();
		String bitstring = "";
		for (int i = 0; i < bArray.length; i++) {
			Byte b = bArray[i];
			bitstring = bitstring + Integer.toBinaryString(Byte.toUnsignedInt(b));
		}

		return bitstring.substring(0, bitstring_size);
	}
}
