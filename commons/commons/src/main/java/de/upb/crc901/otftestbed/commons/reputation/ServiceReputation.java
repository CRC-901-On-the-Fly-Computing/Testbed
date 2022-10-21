package de.upb.crc901.otftestbed.commons.reputation;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModelProperty;

/**
 * A simple service reputation for a service composition.
 */
@JsonIgnoreProperties({ "dateAsString" })
public class ServiceReputation {

	@ApiModelProperty(example = "1")
	private double overall = 0;
	@ApiModelProperty(example = "2")
	private double usability = 0;
	@ApiModelProperty(example = "3")
	private double performance = 0;
	@ApiModelProperty(example = "4")
	private double security = 0;
	@ApiModelProperty(example = "5")
	private double other = 0;
	@ApiModelProperty(example = "Simply Amazing ;) ")
	private String reputationMessage = "";

	private Date date;
	@ApiModelProperty(hidden = true)
	private static DateFormat df = DateFormat.getDateInstance();

	public ServiceReputation(double overall, double usability, double performance, double security, double other,
			Date date, String reputationMessage) {
		this.overall = overall;
		this.usability = usability;
		this.performance = performance;
		this.security = security;
		this.other = other;
		this.date = date;
		this.reputationMessage = reputationMessage;
	}

	public ServiceReputation(double overall, double usability, double performance, double security, double other,
			Date date) {
		this(overall, usability, performance, security, other, date, "");
	}

	public ServiceReputation(double overall, double usability, double performance, double security, double other) {
		this(overall, usability, performance, security, other, new Date(), "");
	}

	public ServiceReputation() {
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setDate(long date) {
		this.date = new Date(date);
	}

	public double getOverall() {
		return overall;
	}

	public void setOverall(double overall) {
		this.overall = overall;
	}

	public double getUsability() {
		return usability;
	}

	public void setUsability(double usability) {
		this.usability = usability;
	}

	public double getPerformance() {
		return performance;
	}

	public void setPerformance(double performance) {
		this.performance = performance;
	}

	public double getSecurity() {
		return security;
	}

	public void setSecurity(double security) {
		this.security = security;
	}

	public double getOther() {
		return other;
	}

	public void setOther(double other) {
		this.other = other;
	}

	public Date getDate() {
		return date;
	}

	public void setReputationMessage(String reputationMessage) {
		this.reputationMessage = reputationMessage;
	}

	public String getReputationMessage() {
		return reputationMessage;
	}

	public void parseDateFromString(String serializedDate) {
		try {
			this.date = df.parse(serializedDate);
		} catch (ParseException e) {
		}
	}

	@ApiModelProperty(hidden = true)
	public String getDateAsString() {
		return df.format(date);
	}
}
