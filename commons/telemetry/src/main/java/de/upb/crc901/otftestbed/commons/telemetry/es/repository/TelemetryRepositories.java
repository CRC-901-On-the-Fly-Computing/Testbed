package de.upb.crc901.otftestbed.commons.telemetry.es.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelemetryRepositories {

	@Autowired
	public AmpehreRepository ampehreRepository;

	@Autowired
	public AnalysisRepository analysisRepository;

	@Autowired
	public AuthenticationsRepository authenticationsRepository;

	@Autowired
	public CodeProvidersRepository codeProvidersRepository;

	@Autowired
	public ComputeCentersRepository computeCentersRepository;

	@Autowired
	public CpaChecksRepository cpaChecksRepository;

	@Autowired
	public NlpRequestsRepository nlpRequestsRepository;

	@Autowired
	public OtfProvidersRepository otfProvidersRepository;

	@Autowired
	public ReputationsCompositionRepository reputationsCompositionRepository;

	@Autowired
	public ReputationsServiceRepository reputationsServiceRepository;

	@Autowired
	public RequestsRepository requestsRepository;

	@Autowired
	public RequestersRepository requestersRepository;

	@Autowired
	public ServiceProvidersRepository serviceProvidersRepository;

	@Autowired
	public TransactionsCompositionRepository transactionsCompositionRepository;

	@Autowired
	public TransactionsServiceRepository transactionsServiceRepository;
	
	@Autowired
	public OtfProviderNetworkRepository otfProviderNetworkRepository;
}
