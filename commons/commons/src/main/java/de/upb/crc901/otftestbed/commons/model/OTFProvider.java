package de.upb.crc901.otftestbed.commons.model;

import java.util.UUID;

public class OTFProvider {

	private String otfProviderName;

	private String iconURL;

	private UUID otfpUUID;

	private String gatekeeperURL;

	public OTFProvider() {}
	
	public OTFProvider(String otfProviderName, String iconURL, UUID otfpUUID, String gatekeeperURL) {
		super();
		this.otfProviderName = otfProviderName;
		this.iconURL = iconURL;
		this.otfpUUID = otfpUUID;
		this.gatekeeperURL = gatekeeperURL;
	}

	public String getOtfProviderName() {
		return otfProviderName;
	}

	public String getIconURL() {
		return iconURL;
	}

	public UUID getOtfpUUID() {
		return otfpUUID;
	}

	public String getGatekeeperURL() {
		return gatekeeperURL;
	}

	@Override
	public String toString() {
		return "OTFProvider [otfProviderName=" + otfProviderName + ", iconURL=" + iconURL + ", otfpUUID=" + otfpUUID
				+ ", gatekeeperURL=" + gatekeeperURL + "]";
	}

}
