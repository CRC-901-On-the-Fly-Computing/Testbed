package de.upb.crc901.testbed.otfproviderregistry.gui;

import java.awt.Color;

import de.upb.crc901.testbed.otfproviderregistry.Domain;
import de.upb.crc901.testbed.otfproviderregistry.Parameters;

/**
 * Maps domainnames to colors.
 * 
 * @author Michael
 *
 */
public class DomainColorMapper {

	public static Color mapDomain(Domain d) {
		switch (d.getName()) {
		case "0":
			return Parameters.DOMAIN_COLORS[0];
		case "1":
			return Parameters.DOMAIN_COLORS[1];
		case "2":
			return Parameters.DOMAIN_COLORS[2];
		case "3":
			return Parameters.DOMAIN_COLORS[3];
		case "4":
			return Parameters.DOMAIN_COLORS[4];
		case "5":
			return Parameters.DOMAIN_COLORS[5];
		case "6":
			return Parameters.DOMAIN_COLORS[6];
		case "7":
			return Parameters.DOMAIN_COLORS[7];
		default:
			return Color.BLACK;
		}
	}
}
