package de.upb.crc901.testbed.otfproviderregistry.common;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Will be used to simulate a coin with dynamic probabilities.
 * 
 * @author Michael
 *
 */
public class Coin {
	private Random random;

	public Coin() {
		random = new SecureRandom();
	}

	public Coin(long seed) {
		random = new SecureRandom();
		random.setSeed(seed);
	}

	/**
	 * Returns true with the given probability and false otherwise. Throws an error if the given probability is not in the range between 0 and 100.
	 * 
	 * @param successProbability
	 * @return
	 */
	public boolean tossCoin(int successProbability) {
		if (successProbability < 0 || successProbability > 100)
			throw new IllegalArgumentException("Error: Successprobability is not in the correct range: " + successProbability);
		if (successProbability == 100)
			return true;
		if (successProbability == 0)
			return false;

		int outcome = random.nextInt(100);

		if (outcome > successProbability)
			return false;
		return true;
	}

	/**
	 * Returns true with the given probability and false otherwise. Throws an error if the given probability is not in the range between 0 and 1.
	 * 
	 * @param successProbability
	 * @return
	 */
	public boolean tossCoin(float successProbability) {
		if (successProbability < 0 || successProbability > 1)
			throw new IllegalArgumentException("Error: Successprobability is not in the correct range: " + successProbability);
		if (successProbability == 1)
			return true;
		if (successProbability == 0)
			return false;
		float outcome = random.nextFloat();

		if (outcome >= successProbability)
			return false;
		return true;
	}

	public void setSeed(long seed) {
		this.random.setSeed(seed);
	}
}
