package de.upb.crc901.testbed.otfproviderregistry.common;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A class that is able to pick distinct elements from a given set. The given set is read-only and thus will NOT be modified by this class!
 * 
 * @author Michael
 *
 */
public class RandomPicker<T> {
	private List<T> objects;
	private Random rng;

	public RandomPicker() {
		this.objects = new ArrayList<>();
		this.rng = new SecureRandom();
	}

	public RandomPicker(List<T> objects) {
		this.objects = objects;
		this.rng = new SecureRandom();
	}

	/**
	 * Picks an object out of the set and returns it.
	 * 
	 * @return the picked object
	 * @throws IllegalArgumentException
	 *             if the current set is empty
	 */
	public T pickObject() {
		if (objects.isEmpty())
			throw new IllegalArgumentException("Pool is empty - cannot pick!");
		return objects.get(rng.nextInt(objects.size()));
	}

	/**
	 * Pick the given amount of objects from the set of objects and returns them in a list.
	 * 
	 * @param number
	 *            cardinality has to be smaller than the given object set when distinct is enabled
	 * @param distinct
	 * @return
	 */
	public List<T> pickObjects(int number, boolean distinct) {
		if (number > objects.size() && distinct)
			throw new IllegalArgumentException("Pick value is higher than the set of objects with distinct enabled!");
		List<T> erg = new ArrayList<>();
		while (erg.size() < number) {
			T o = objects.get(rng.nextInt(objects.size()));
			if (!erg.contains(o) || (erg.contains(o) && !distinct))
				erg.add(o);
		}
		return erg;
	}

	/**
	 * Pick the given amount of objects from the set of objects and returns their indices in a list.
	 * 
	 * @param number
	 * @param distinct
	 * @return
	 */
	public List<Integer> pickObjectIndices(int number, boolean distinct) {
		if (number > objects.size())
			throw new IllegalArgumentException("Pick value is higher than the set of objects!");
		List<Integer> erg = new ArrayList<>();
		while (erg.size() < number) {
			Integer index = rng.nextInt(objects.size());
			if (!erg.contains(index) || (erg.contains(index) && !distinct))
				erg.add(index);
		}
		return erg;
	}

	public void addObject(T object) {
		this.objects.add(object);
	}

	public void setObjects(List<T> objects) {
		this.objects = objects;
	}

	public int size() {
		return this.objects.size();
	}
}
