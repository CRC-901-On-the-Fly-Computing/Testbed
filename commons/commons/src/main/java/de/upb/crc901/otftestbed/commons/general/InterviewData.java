package de.upb.crc901.otftestbed.commons.general;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Specifies all information that are needed to run PROSECO, i.e. all
 * information after the interview and the service matching is being done.
 * 
 * This data is being used to run a PROSECO configuration.
 * 
 * @author Mirko Jürgens
 *
 */
public class InterviewData {

	/**
	 * The domain of the request, at the moment only <code>automl</code> is
	 * supported.
	 */
	private String domain;

	/** The timeout of the request as specified in the interview. */
	private int timeout;

	/** The other answers of the interview. */
	private Map<String, String> answers;

	/**
	 * The list of services (specified by their unique id), that were matched using
	 * the FuzzyMatcher and the Execution-Gateway.
	 */
	private List<String> serviceIDs;

	private ProsecoProcessConfiguration ppConfig;
	
	public InterviewData() {
	}

	public InterviewData(String domain, int timeout, Map<String, String> answers, List<String> serviceIDs, ProsecoProcessConfiguration ppConfiguration) {
		super();
		this.domain = domain;
		this.timeout = timeout;
		this.answers = answers;
		this.serviceIDs = serviceIDs;
		this.ppConfig = ppConfiguration;
	}

	public String getDomain() {
		return domain;
	}

	public int getTimeout() {
		return timeout;
	}

	public Map<String, String> getAnswers() {
		return answers;
	}

	public List<String> getServiceIDs() {
		return serviceIDs;
	}

	public ProsecoProcessConfiguration getPpConfig() {
		return ppConfig;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setAnswers(Map<String, String> answers) {
		this.answers = answers;
	}

	public void setServiceIDs(List<String> serviceIDs) {
		this.serviceIDs = serviceIDs;
	}

	public void setPpConfig(ProsecoProcessConfiguration ppConfig) {
		this.ppConfig = ppConfig;
	}
	
	

}
