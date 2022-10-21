package de.upb.crc901.testbed.otfproviderregistry;

public class ServiceProvider extends Thread {

	private final String id; // unique
	private String address; // unique

	public ServiceProvider(String id) {
		this.id = id;
		this.address = id; // is not used by A1
	}

	public String getID() {
		return id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String toString() {
		return "ID: " + this.id;
	}
}
