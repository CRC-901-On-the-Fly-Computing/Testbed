package de.upb.crc901.testbed.otfproviderregistry;

import java.util.ArrayList;
import java.util.List;

/**
 * This class models a single domain that can be identified via its (unique)
 * name.
 * 
 * @author Michael
 *
 */
public class Domain {
	static List<Domain> domains = new ArrayList<>();

	static Domain getDomain(String name) {
		for (Domain d : domains) {
			if (d.getName().equals(name))
				return d;
		}

		// If no domain with given name could be found: Generate new domain and add it
		// to list of domains.
		Domain d = new Domain();
		d.setName(name);
		domains.add(d);
		return d;
	}

	private String name;

	/**
	 * Returns the name of this domain.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name for this domain.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
}
