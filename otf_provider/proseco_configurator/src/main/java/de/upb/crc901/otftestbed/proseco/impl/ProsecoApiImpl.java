package de.upb.crc901.otftestbed.proseco.impl;

import de.upb.crc901.otftestbed.commons.connect.PocConnector;
import de.upb.crc901.otftestbed.commons.general.InterviewData;
import de.upb.crc901.otftestbed.commons.general.JobState;
import de.upb.crc901.otftestbed.commons.general.ProsecoProcessConfiguration;
import de.upb.crc901.otftestbed.commons.general.StoredJobstate;
import de.upb.crc901.otftestbed.commons.model.OTFProvider;
import de.upb.crc901.otftestbed.commons.requester.ConfigurationMarketMonitorSources;
import de.upb.crc901.otftestbed.commons.requester.InterviewResponse;
import de.upb.crc901.otftestbed.commons.requester.InterviewResponse.InterviewState;
import de.upb.crc901.otftestbed.commons.requester.InterviewResponse.QuestionType;
import de.upb.crc901.otftestbed.commons.rest.SimpleJSONMessage;
import de.upb.crc901.otftestbed.proseco.generated.spring_server.api.ProsecoApiDelegate;
import de.upb.crc901.otftestbed.proseco.impl.components.DeploymentComponent;
import de.upb.crc901.otftestbed.proseco.impl.components.ServiceFilteringComponent;
import de.upb.crc901.otftestbed.proseco.impl.config.EnvironmentVariableConfiguration;
import de.upb.crc901.otftestbed.proseco.impl.repository.MarketMonitorSourcesRepository;
import de.upb.crc901.otftestbed.proseco.impl.repository.RequestDBConnector;
import de.upb.crc901.otftestbed.proseco.impl.repository.StoredMMSources;
import de.upb.crc901.proseco.commons.config.PROSECOConfig;
import de.upb.crc901.proseco.commons.interview.InterviewFillout;
import de.upb.crc901.proseco.commons.processstatus.InvalidStateTransitionException;
import de.upb.crc901.proseco.commons.util.PROSECOProcessEnvironment;
import de.upb.crc901.proseco.commons.util.Parser;
import de.upb.crc901.proseco.core.composition.FileBasedConfigurationProcess;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@ComponentScan(basePackages = { "de.upb.crc901.otftestbed.proseco.utils",
		"de.upb.crc901.otftestbed.proseco.newapi.utils" })
public class ProsecoApiImpl implements ProsecoApiDelegate {

	private static final Logger log = LoggerFactory.getLogger(ProsecoApiImpl.class);

	@Autowired
	private PocConnector connect;

	@Autowired
	private RestTemplate restTemplate;

	/** All configurations set by helm */
	@Autowired
	private EnvironmentVariableConfiguration config;

	/** Can spawn proseco containers */
	@Autowired
	private DeploymentComponent kubernetesDeployment;

	/** The key in the answer-map that specifies the domain. */
	public static final String DOMAIN_KEY = "domain";

	/** The key in the answer-map that specifies the timeout. */
	public static final String TIMEOUT_KEY = "timeout";

	/**
	 * PROSECO configuration file (i.e. the names of the folders can be changed
	 * here)
	 */
	private File prosecoConfigFile = new File("conf/proseco.conf");

	/** Maps a request to the process environment that contains all paths etc. */
	private Map<UUID, PROSECOProcessEnvironment> requestToProcessEnv = new HashMap<>();

	/** Maps a request to the configuration process that controls proseco. */
	private Map<UUID, FileBasedConfigurationProcess> requestToProcess = new HashMap<>();

	@Autowired
	MarketMonitorSourcesRepository monitorSourcesRepository;

	@Autowired
	ServiceFilteringComponent serviceFilterer;

	// re-register every 10 seconds
	@Scheduled(fixedDelay =  10 * 1000)
	public void registerAtRegistry() {
		log.debug("Registering at the otfp registry.");
		String otfNamespace = config.getOtfpNamespace();
		String configuratorHost = "http://proseco-configurator." + otfNamespace + ":8080/api/";
		OTFProvider otfProvider = new OTFProvider(config.getOtfpName(), config.getOtfpIcon(), config.getOtfpUUID(),
				configuratorHost);

		// get all domains
		PROSECOConfig prosecoConfig = PROSECOConfig.get(prosecoConfigFile);
		File[] allFiles = prosecoConfig.getDirectoryForDomains().listFiles();
		for (File possibleDomain : allFiles) {
			if (possibleDomain.isDirectory()) {
				String domainName = possibleDomain.getName();
				log.debug("Found possible domain {}", domainName);
				// register at the registry under this domain
				// FIXME: Fix url paths
				connect.toMarketProvider().setOtfpRegistryBasePath("http://otfp-registry-new."
						+ config.getMarketProviderNamespace() + ":8080/api/");
				log.debug("Registering for domain {} at {}", possibleDomain, connect.toMarketProvider()
						.getOtfpRegistryBasePath());
				connect.toMarketProvider().callOtfpRegistry().insertOtfProviderForDomain(otfProvider, domainName);
			}
		}
		log.debug("Finished registering.");
	}

	@Override
	public ResponseEntity<InterviewResponse> postInterview(Map<String, String> interviewGenericMap, UUID uuid) {

		Map<String, String> interviewMap = interviewGenericMap;
		
		log.debug("Got request with map {}", interviewMap);
		/*
		 * If the request is new, we first have to create a proseco process.
		 */
		if (isNewRequest(uuid)) {
			if (interviewMap.containsKey(DOMAIN_KEY)) {
				String domain = interviewMap.get(DOMAIN_KEY);
				FileBasedConfigurationProcess configurationProcess = new FileBasedConfigurationProcess(
						prosecoConfigFile);
				try {
					configurationProcess.createNew();
					configurationProcess.fixDomain(domain);
					configurationProcess.setProcessId(uuid.toString());
					PROSECOProcessEnvironment ppEnv = configurationProcess.getProcessEnvironment();
					requestToProcess.put(uuid, configurationProcess);
					requestToProcessEnv.put(uuid, ppEnv);
				} catch (InvalidStateTransitionException e) {
					log.error("Failed to create proseco process", e);
				}
			} else {
				// we cannot create a new proseco process without at least knowing the domain.
				log.warn("No domain given in the interview.");
				return ResponseEntity.status(500).build();

			}
		}

		/** First step: update the interview in proseco. */
		FileBasedConfigurationProcess configurationProcess = requestToProcess.get(uuid);
		try {
			configurationProcess.updateInterview(interviewMap);
		} catch (InvalidStateTransitionException e) {
			log.error("Failed to update interview ", e);
		}

		InterviewResponse nextQuestion = getNextOpenQuestion(uuid, interviewMap);

		/* Update the request in the database */
		Optional<StoredJobstate> optionalJobState = RequestDBConnector.getInstance().findById(uuid);
		if (optionalJobState.isPresent()) {
			RequestDBConnector.getInstance().deleteById(uuid);
		}
		InterviewData interviewData = new InterviewData(interviewMap.get(DOMAIN_KEY),
				// the timeout may have not been set yet
				Integer.parseInt(interviewMap.getOrDefault(TIMEOUT_KEY, Integer.toString(0))), interviewMap,
				// the services will be set when the interview is done.
				new ArrayList<String>(),
				// resolve configuration via environment variables.
				ProsecoProcessConfiguration.getKubernetesConfiguration(config.getOtfpNamespace(),
						config.getMarketProviderNamespace(), config.getElasticNamespace(),
						config.getExecutionNamespace()));

		if (nextQuestion.getQuestionType() == QuestionType.NO_QUESTION) {
//			 No more questions
			RequestDBConnector.getInstance()
					.insert(new StoredJobstate(uuid, JobState.READY_TO_CONFIGURE, interviewData));
		} else {
			RequestDBConnector.getInstance()
					.insert(new StoredJobstate(uuid, JobState.PROSECO_NEEDS_MORE_INFOS, interviewData));
		}

		return ResponseEntity.status(200).body(nextQuestion);
	}

	private boolean isNewRequest(UUID uuid) {
		return !requestToProcess.containsKey(uuid) && !requestToProcessEnv.containsKey(uuid);
	}

	@Override
	public ResponseEntity<Void> runConfiguration(UUID uuid) {
		log.debug("** runConfiguration: started");
		Optional<StoredJobstate> optionalJob = RequestDBConnector.getInstance().findById(uuid);
		if (!optionalJob.isPresent()) {
			log.warn("Couldn't find job state for uuid {}. Maybe the interview isn't done yet?", uuid);
			return ResponseEntity.status(400).build();
		}

		// interview is done -> update the jobstate repo as a preparation for the
		// proseco_container
		StoredJobstate job = optionalJob.get();
		log.debug("** job: {}", job);

		Map<String, String> interviewMap = job.getInterviewData().getAnswers();
		log.debug("** interview_map: {}", interviewMap);
		List<String> availableServices = serviceFilterer.getAllAvailableServices();

		log.debug("Filtered services are: {}", availableServices);

		RequestDBConnector.getInstance().deleteById(uuid);
		log.debug("** RequestDBConnector: done");
		InterviewData interviewData = new InterviewData(interviewMap.get(DOMAIN_KEY),
				Integer.parseInt(interviewMap.get(TIMEOUT_KEY)), interviewMap, availableServices,
				ProsecoProcessConfiguration.getKubernetesConfiguration(config.getOtfpNamespace(),
						config.getMarketProviderNamespace(), config.getElasticNamespace(),
						config.getExecutionNamespace()));
		log.debug("** interview_data: {}", interviewData);
		RequestDBConnector.getInstance().insert(new StoredJobstate(uuid, JobState.CONFIGURING, interviewData));
		log.debug("** RequestDBConnector: stored");
		if (monitorSourcesRepository.existsById(uuid)) {
			monitorSourcesRepository.deleteById(uuid);
			log.debug("** monitorSourcesRepository: deleted");
		}

		String prosecoAppendix = uuid.toString();
		log.debug("** prosecoAppendix: {}", prosecoAppendix);
		// FIXME: I have no idea how to make this in a clea manner. The problem is that
		// this request has to be reachable from outside the cluster, since the website
		// makes the queries.
		monitorSourcesRepository.insert(new StoredMMSources(uuid, new ConfigurationMarketMonitorSources(
				"http://sfb-k8master-1.cs.uni-paderborn.de:30080/elastic/search/configuration_nodes/_search?size=10000&q=elasticID.keyword:"
						+ prosecoAppendix,
				"http://sfb-k8master-1.cs.uni-paderborn.de:30080/elastic/search"
						+ "/configuration_edges/_search?size=10000&q=elasticID.keyword:" + prosecoAppendix)));

		log.debug("** monitorSourcesRepository: inserted");
		// spawn proseco_container!
		kubernetesDeployment.spawnProsecoPod(uuid);
		log.debug("** kubernetesDeployment: done");
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Retrieves the logs of a proseco container via the api.
	 */
	@Override
	public ResponseEntity<SimpleJSONMessage> getLogs(UUID uuid) {
		try (KubernetesClient client = new DefaultKubernetesClient()) {
			return ResponseEntity.ok(new SimpleJSONMessage(
					client.pods().inNamespace(config.getPodNamespace()).withName(uuid.toString()).getLog(true)));
		}
	}

	@Override
	public ResponseEntity<Void> restartContainer(UUID uuid) {
		// delete pod
		try (KubernetesClient client = new DefaultKubernetesClient()) {
			client.pods().inNamespace(config.getPodNamespace()).withName(uuid.toString()).delete();
		}
		// delete the graph in elastic s.t. the website works
		// FIXME: fix path
		String prosecoAppendix = uuid.toString();
		String deleteNodesURI = "http://sfb-k8master-1.cs.uni-paderborn.de:30080/elastic/search/configuration_nodes/_delete_by_query?q=elasticID.keyword:"
				+ prosecoAppendix;
		String deleteEdgesURI = "http://sfb-k8master-1.cs.uni-paderborn.de:30080/elastic/search"
				+ "/configuration_edges/_delete_by_query?q=elasticID.keyword:" + prosecoAppendix;
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.postForEntity(deleteEdgesURI, null, Object.class);
		restTemplate.postForEntity(deleteNodesURI, null, Object.class);

		// update database jobstate
		Optional<StoredJobstate> data = RequestDBConnector.getInstance().findById(uuid);
		if (data.isPresent()) {
			RequestDBConnector.getInstance().deleteById(uuid);
			StoredJobstate interviewData = data.get();
			RequestDBConnector.getInstance()
					.insert(new StoredJobstate(uuid, JobState.CONFIGURING, interviewData.getInterviewData()));
		}

		// spawn new
		kubernetesDeployment.spawnProsecoPod(uuid);
		return ResponseEntity.ok().build();

	}

	/**
	 * Lists all configuration processes.
	 */
	@Override
	public ResponseEntity<List<UUID>> listContainers() {
		try (KubernetesClient client = new DefaultKubernetesClient()) {
			return ResponseEntity.ok(client.pods().inNamespace(config.getPodNamespace()).list().getItems().stream()
					.map(pod -> pod.getMetadata().getName()).map(UUID::fromString).collect(Collectors.toList()));
		}
	}

	/**
	 * Parses the {@link InterviewFillout} for this configuration process and
	 * returns the next open question.
	 * 
	 * @return the new question in our format
	 */
	@SuppressWarnings("unchecked")
	private InterviewResponse getNextOpenQuestion(UUID requestUUID, Map<String, String> answers) {
		PROSECOProcessEnvironment ppEnv = requestToProcessEnv.get(requestUUID);

		final File interviewFile = new File(
				ppEnv.getInterviewDirectory().getAbsolutePath() + File.separator + "interview.yaml");
		final Parser parser = new Parser();
		InterviewFillout fillout = null;
		try {
			fillout = new InterviewFillout(parser.initializeInterviewFromConfig(interviewFile));
		} catch (IOException e) {
			log.error("Failed to read fillout!");
			throw new IllegalStateException(e);
		}
		Yaml yaml = new Yaml();
		FileInputStream fis;
		try {
			fis = new FileInputStream(ppEnv.getInterviewDirectory().getAbsolutePath() + File.separator
					+ fillout.getInterview().getQuestionRepo());

			Object questionRepo = yaml.load(fis);
			Map<String, Object> parsed = (Map<String, Object>) questionRepo;
			List<Map<String, Object>> questions = (List<Map<String, Object>>) parsed.get("questions");
			for (Map<String, Object> question : questions) {
				String questionID = (String) question.get("id");
				if (!answers.containsKey(questionID)) {
					return parseNewQuestionFromYaml(question, questionID);
				}
			}
		} catch (FileNotFoundException e) {
			log.error("Failed to read questions.yaml");
			throw new IllegalStateException(e);
		}
		throw new IllegalStateException("Interview file contains no questions...");
	}

	/**
	 * Reads the html fields to parse a question.
	 * 
	 * @param question   yaml map of key values
	 * @param questionID the id of this question
	 * @return the question in our format
	 */
	@SuppressWarnings("unchecked")
	private InterviewResponse parseNewQuestionFromYaml(Map<String, Object> question, String questionID) {
		// new question:
		String questionContent = (String) question.get("content");
		Map<String, Object> possibleAnswers = (Map<String, Object>) question.get("uiElement");
		String answerType = (String) possibleAnswers.get("@type");
		if ("Input".equals(answerType)) {
			Map<String, Object> answerAttributes = (Map<String, Object>) possibleAnswers.get("attributes");
			String answerFieldType = (String) answerAttributes.get("type");
			if (TIMEOUT_KEY.equals(questionID)) {
				return new InterviewResponse(InterviewState.MORE_INFORMATION_NEEDED, questionContent,
						QuestionType.NUMBER, questionID);
			}
			if ("file".equals(answerFieldType)) {
				return new InterviewResponse(InterviewState.MORE_INFORMATION_NEEDED, questionContent, QuestionType.FILE,
						questionID);
			}
			if ("hidden".equals(answerFieldType)) {
				return new InterviewResponse(InterviewState.INTERVIEW_DONE, questionContent, QuestionType.NO_QUESTION,
						questionID);
			}
			return new InterviewResponse(InterviewState.MORE_INFORMATION_NEEDED, questionContent, QuestionType.TEXT,
					questionID);

		}
		if ("Select".equals(answerType)) {
			List<Map<String, Object>> options = (List<Map<String, Object>>) possibleAnswers.get("options");
			List<String> optionValues = new ArrayList<>();
			for (Map<String, Object> option : options) {
				optionValues.add((String) option.get("content"));
			}
			return new InterviewResponse(InterviewState.MORE_INFORMATION_NEEDED, questionContent, QuestionType.DROPDOWN,
					optionValues, questionID);
		} else {
			throw new IllegalStateException("Failed to parse question " + question.toString());
		}
	}

	@Override
	public ResponseEntity<ConfigurationMarketMonitorSources> getSources(UUID request) {
		Optional<StoredMMSources> optionalSources = monitorSourcesRepository.findById(request);
		if (optionalSources.isPresent()) {
			return ResponseEntity.ok(optionalSources.get().getSources());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	public ResponseEntity<JobState> getJobstate(UUID request) {
		Optional<StoredJobstate> optionalJobState = RequestDBConnector.getInstance().findById(request);
		if (optionalJobState.isPresent()) {
			return ResponseEntity.ok(optionalJobState.get().getJobstate());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
