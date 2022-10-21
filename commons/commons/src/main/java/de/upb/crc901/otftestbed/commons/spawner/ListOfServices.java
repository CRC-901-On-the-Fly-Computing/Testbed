package de.upb.crc901.otftestbed.commons.spawner;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class ListOfServices {

	@ApiModelProperty(example="[\"Catalano.Imaging.sede.CropFrom0\"]")
	private ArrayList<String> services = new ArrayList<>();

	public List<String> getServices() {
		return services;
	}
}
