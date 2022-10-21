package de.upb.crc901.otftestbed.commons.telemetry.es.property;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class Transaction {

	@Field(type = FieldType.Double)
	private Double buyPrice;

	@Field(type = FieldType.Double)
	private Double salePrice;


	public Double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(Double buyPrice) {
		this.buyPrice = buyPrice;
	}

	public Transaction buyPrice(Double buyPrice) {
		setBuyPrice(buyPrice);
		return this;
	}


	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public Transaction salePrice(Double salePrice) {
		setSalePrice(salePrice);
		return this;
	}
}
