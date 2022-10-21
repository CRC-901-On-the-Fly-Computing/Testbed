package de.upb.crc901.otftestbed.service_requester.impl.test;

import static de.upb.crypto.react.acs.policy.PolicyBuilder.policy;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.upb.crc901.otftestbed.buy_processor.generated.java_client.api.BuyProcessorApi;
import de.upb.crc901.otftestbed.commons.connect.MarketProviderConnector;
import de.upb.crc901.otftestbed.commons.connect.OtfProviderConnector;
import de.upb.crc901.otftestbed.commons.connect.PocConnector;
import de.upb.crc901.otftestbed.commons.environment.Profiles;
import de.upb.crc901.otftestbed.commons.general.CompositionAndOTFProvider;
import de.upb.crc901.otftestbed.commons.general.JobState;
import de.upb.crc901.otftestbed.commons.model.OTFProvider;
import de.upb.crc901.otftestbed.commons.reputation.BuyResponse;
import de.upb.crc901.otftestbed.commons.reputation.BuyTokens;
import de.upb.crc901.otftestbed.commons.reputation.SimpleAttributes;
import de.upb.crc901.otftestbed.commons.requester.InterviewResponse;
import de.upb.crc901.otftestbed.commons.requester.InterviewResponse.InterviewState;
import de.upb.crc901.otftestbed.commons.requester.InterviewResponse.QuestionType;
import de.upb.crc901.otftestbed.commons.requester.OTFProviderConfidence;
import de.upb.crc901.otftestbed.commons.requester.Offer;
import de.upb.crc901.otftestbed.commons.requester.SimpleComposition;
import de.upb.crc901.otftestbed.commons.requester.SimplePolicy;
import de.upb.crc901.otftestbed.commons.rest.SimpleJSONUuid;
import de.upb.crc901.otftestbed.credential_issuer.generated.java_client.api.CredentialIssuerApi;
import de.upb.crc901.otftestbed.proseco.generated.java_client.api.ProsecoApi;
import de.upb.crc901.otftestbed.registry.generated.java_client.api.ProviderRegistryApi;
import de.upb.crc901.otftestbed.service_requester.impl.components.DataHolderComponent;
import de.upb.crc901.otftestbed.service_requester.impl.controllers.DataManagementController;
import de.upb.crc901.otftestbed.service_requester.impl.controllers.InitialInterviewController;
import de.upb.crc901.otftestbed.service_requester.impl.controllers.MarketController;
import de.upb.crc901.otftestbed.service_requester.impl.controllers.OtfProviderController;
import de.upb.crc901.otftestbed.service_requester.impl.exceptions.InvalidAnswerException;
import de.upb.crc901.otftestbed.service_requester.impl.exceptions.InvalidAttributeException;
import de.upb.crc901.otftestbed.service_requester.impl.exceptions.NoSuchOtfProviderException;
import de.upb.crc901.otftestbed.service_requester.impl.exceptions.NoSuchRequestException;
import de.upb.crc901.otftestbed.service_requester.impl.exceptions.NoUserRegisteredException;
import de.upb.crc901.otftestbed.system_manager.generated.java_client.api.SystemManagerApi;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactCredentialIssueResponse;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactCredentialIssuer;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactIssuerKeyPair;
import de.upb.crypto.react.acs.issuer.impl.react.credentials.ReactIssuerKeyPairFactory;
import de.upb.crypto.react.acs.issuer.impl.react.reviewtokens.ReactReviewTokenIssueResponse;
import de.upb.crypto.react.acs.issuer.impl.react.reviewtokens.ReactReviewTokenIssuer;
import de.upb.crypto.react.acs.policy.PolicyInformation;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParameters;
import de.upb.crypto.react.acs.setup.impl.react.ReactPublicParametersFactory;
import de.upb.crypto.react.acs.systemmanager.impl.react.ReactSystemManager;
import de.upb.crypto.react.acs.systemmanager.impl.react.ReactSystemManagerPublicIdentity;
import de.upb.crypto.react.acs.verifier.impl.react.credentials.ReactCredentialVerifier;
import de.upb.crypto.react.buildingblocks.attributes.AttributeDefinition;
import de.upb.crypto.react.buildingblocks.attributes.BigIntegerAttributeDefinition;
import de.upb.crypto.react.buildingblocks.attributes.StringAttributeDefinition;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class InitialInterviewControllerTest {

	@BeforeClass
	public static void setProfile() {
		System.setProperty("spring.profiles.active", Profiles.OFFLINE.toString());
		System.setProperty("spring.rabbitmq.host", "localhost");
		System.setProperty("spring.rabbitmq.port", "9000");
		System.setProperty("spring.rabbitmq.username", "admin");
		System.setProperty("spring.rabbitmq.password", "admin");

	}

	/* Used to mock API calls */
	@MockBean
	PocConnector connect;

	@MockBean
	OtfProviderConnector otfProviderConnectorMock;

	@MockBean
	ProsecoApi prosecoApiMock;

	@MockBean
	MarketProviderConnector marketProviderMock;

	@MockBean
	SystemManagerApi systemManagerApiMock;

	@MockBean
	CredentialIssuerApi credentialIssuerApiMock;

	@MockBean
	ProviderRegistryApi providerRegistryApiMock;

	@MockBean
	BuyProcessorApi buyProcessorApiMock;

	/* The controllers that shall be tested */
	@Autowired
	InitialInterviewController initialInterviewController;

	@Autowired
	@InjectMocks
	MarketController marketController;

	@Autowired
	OtfProviderController otfpController;

	@Autowired
	DataManagementController dataManagementController;

	@SpyBean
	DataHolderComponent dataHolder;

	/* Test internal variables */
	private ReactPublicParameters pp;

	private ReactSystemManagerPublicIdentity smPI;

	private OTFProvider mockedProvider;

	private InterviewResponse mockedQuestionList;

	private InterviewResponse mockedQuestionNumber;

	private InterviewResponse mockedFinalQuestion;

	@PostConstruct
	public void setupTest() {
		/* Wire the poc connector... */
		doNothing().when(marketProviderMock).setBasePathResolver(any());
		doNothing().when(otfProviderConnectorMock).setBasePathResolver(any());
		when(connect.toMarketProvider()).thenReturn(marketProviderMock);
		when(connect.toOtfProvider()).thenReturn(otfProviderConnectorMock);
		when(marketProviderMock.callSystemManager()).thenReturn(systemManagerApiMock);
		when(marketProviderMock.callCredentialIssuer()).thenReturn(credentialIssuerApiMock);
		when(marketProviderMock.callOtfpRegistry()).thenReturn(providerRegistryApiMock);
		when(otfProviderConnectorMock.callProsecoConfigurator(anyString())).thenReturn(prosecoApiMock);
		when(marketProviderMock.callBuyProcessor()).thenReturn(buyProcessorApiMock);

		/* Setting up the system manager */
		ReactPublicParametersFactory setup = new ReactPublicParametersFactory();
		setup.setDebugMode(true);
		pp = setup.create();
		ReactSystemManager systemManager = new ReactSystemManager(pp);
		smPI = systemManager.getPublicIdentity();

		/* Mock the system manager API calls */
		when(systemManagerApiMock.getPublicParameter()).thenReturn(pp);
		when(systemManagerApiMock.getPublicIdentity()).thenReturn(smPI);
		when(systemManagerApiMock.joinUser(any()))
				.thenAnswer(i -> systemManager.nonInteractiveJoinVerification(i.getArgument(0)));

		/* Setting up the credential issuer */
		/* Defining attributes... */
		BigIntegerAttributeDefinition ageAttribute = new BigIntegerAttributeDefinition("age", BigInteger.valueOf(12),
				BigInteger.valueOf(63));
		StringAttributeDefinition countryAttribute = new StringAttributeDefinition("country",
				"(Germany|France|USA|Canada)");
		StringAttributeDefinition subscriptionLevelAttribute = new StringAttributeDefinition("subscriptionLevel",
				"(Free|Basic|Prime|Premium)");
		StringAttributeDefinition userLicenseAttribute = new StringAttributeDefinition("userLicense",
				"(Private|Business)");
		List<AttributeDefinition> attributes = Arrays.asList(ageAttribute, countryAttribute, subscriptionLevelAttribute,
				userLicenseAttribute);
		ReactIssuerKeyPairFactory factory = new ReactIssuerKeyPairFactory();
		ReactIssuerKeyPair keyPair = factory.create(pp, attributes.size());
		ReactCredentialIssuer credentialIssuer = new ReactCredentialIssuer(pp, keyPair, attributes);

		/* Mock the credential issuer API calls */
		when(credentialIssuerApiMock.getPublicIdentity()).thenReturn(credentialIssuer.getPublicIdentity());
		when(credentialIssuerApiMock.requestCredential(any()))
				.thenAnswer(i -> credentialIssuer.issueNonInteractively(i.getArgument(0)));

		/* Setting up the review token issuer */
		ReactReviewTokenIssuer reviewTokenIssuer = new ReactReviewTokenIssuer(pp);

		/* Setting up the access-token issuer */
		StringAttributeDefinition accessCredentialDefinition = new StringAttributeDefinition("accessCredential", ".*");
		ReactCredentialIssuer accessTokenIssuer = new ReactCredentialIssuer(pp,
				Arrays.asList(accessCredentialDefinition));

		/* Setting up the credential verifier */
		ReactCredentialVerifier credentialVerifier = new ReactCredentialVerifier(pp, smPI);

		/* Mock the buy-processor API calls */
		when(buyProcessorApiMock.getReviewTokenIssuerPublicIdentity())
				.thenReturn(reviewTokenIssuer.getPublicIdentity());
		when(buyProcessorApiMock.getAccessTokenIssuerPublicIdentity())
				.thenReturn(accessTokenIssuer.getPublicIdentity());
		when(buyProcessorApiMock.getPublicIdentity()).thenReturn(credentialVerifier.getIdentity());

		/* Mock otf-provider registry */
		mockedProvider = new OTFProvider("mockedProvider", "http://some.url", UUID.randomUUID(),
				"http://someGatekeeper.url");
		
		/* Mock simple registry response */
		OTFProviderConfidence singleConfidence = new OTFProviderConfidence(mockedProvider, 1.0);
		when(providerRegistryApiMock.getConfidencesForProviders(anyMap(), anyString()))
				.thenReturn(Arrays.asList(singleConfidence));

		mockedQuestionList = new InterviewResponse(InterviewState.MORE_INFORMATION_NEEDED,
				"Some question that wants a list", QuestionType.DROPDOWN, Arrays.asList("A", "B", "C"),
				"someListQuestionID");

		mockedQuestionNumber = new InterviewResponse(InterviewState.MORE_INFORMATION_NEEDED,
				"Some question that wants a number", QuestionType.NUMBER, "someNumberQuestionID");
		mockedFinalQuestion = new InterviewResponse(InterviewState.INTERVIEW_DONE, "We are done",
				QuestionType.NO_QUESTION, "someFinalQuestionID");
		
		/* Mock an offer... */
		PolicyInformation pi = policy(pp).forIssuer(credentialIssuer.getPublicIdentity()).attribute("age")
				.isInRange(8, 35).attribute("country").isInSet("Germany", "USA", "Canada")
				.attribute("subscriptionLevel").isInSet("Free", "Premium").attribute("userLicense")
				.isInSet("Private", "Business").build();

		SimpleComposition simpleComposition = new SimpleComposition();
		simpleComposition.setServiceId(Arrays.asList("A", "B", "C"));
		simpleComposition.setServiceCompositionId("A-B-C");

		Map<String, String> nonFunctionalProperties = new HashMap<String, String>();
		nonFunctionalProperties.put("price", "30€");

		Offer offer = new Offer();
		offer.setPolicyInformation(pi);
		offer.setSimplePolicy(SimplePolicy.fromPolicyInformation(pi, pp));
		offer.setCompositionID("A-B-C");
		offer.setCompositionAndOTFProvider(
				new CompositionAndOTFProvider(simpleComposition, mockedProvider.getOtfpUUID()));
		offer.setOfferUUID(UUID.randomUUID());
		offer.setNonFunctionalProperties(nonFunctionalProperties);

		when(buyProcessorApiMock.getOffersForRequest(eq(mockedProvider.getOtfpUUID()), any()))
				.thenReturn(Arrays.asList(offer));
		when(buyProcessorApiMock.getOffer(eq(mockedProvider.getOtfpUUID()), any(), eq(offer.getOfferUUID())))
				.thenReturn(offer);

		/* Mock buying */
		when(buyProcessorApiMock.buyItem(any(), eq(mockedProvider.getOtfpUUID()), any(), eq(offer.getOfferUUID())))
				.thenAnswer(new Answer<BuyResponse>() {
					@Override
					public BuyResponse answer(InvocationOnMock invocation) throws Throwable {
						BuyTokens tokens = (BuyTokens) invocation.getArgument(0);
						boolean policySatisfied = credentialVerifier
								.verifyNonInteractiveProof(tokens.getAuthenticationToken(), pi).isVerify();
						if (!policySatisfied)
							throw new IllegalStateException("Authentication Failed");
						ReactReviewTokenIssueResponse reviewToken = reviewTokenIssuer
								.issueNonInteractively(tokens.getReviewTokenRequest());
						ReactCredentialIssueResponse accessToken = accessTokenIssuer
								.issueNonInteractively(tokens.getAccessTokenRequest());
						return new BuyResponse(reviewToken, accessToken, "http://some-service.url");

					}
				});
	}

	@Before
	public void resetUserRegistrationAndProsecoProgress() {
		dataHolder.setUser(null);
		dataHolder.setRegistered(false);
		/* Mock a proseco */
		when(prosecoApiMock.postInterview(anyMap(), any())).thenAnswer(new Answer<InterviewResponse>() {
			int counter = 0;

			@Override
			public InterviewResponse answer(InvocationOnMock invocation) throws Throwable {
				if (counter == 0) {
					counter++;
					return mockedQuestionList;
				}
				if (counter == 1) {
					counter++;
					return mockedQuestionNumber;
				}
				return mockedFinalQuestion;
			}
		});

		when(prosecoApiMock.getJobstate(any())).thenAnswer(new Answer<JobState>() {
			int calledCounter = 0;

			@Override
			public JobState answer(InvocationOnMock invocation) throws Throwable {
				if (calledCounter++ == 0) {
					return JobState.BUILDING;
				}
				return JobState.DONE;
			}
		});
	}

	@Test
	public void testValidUserRegistration() {
		System.out.println("Testing user registration.");
		SimpleAttributes validUserAttributes = new SimpleAttributes();
		Map<String, Object> validAttributeMap = new HashMap<>();
		validAttributeMap.put("age", 12);
		validAttributeMap.put("country", "USA");
		validAttributeMap.put("subscriptionLevel", "Free");
		validAttributeMap.put("userLicense", "Business");
		validUserAttributes.setAttributePairs(validAttributeMap);
		marketController.registerUser(validUserAttributes);
	}

	@Test
	public void testInvalidUserRegistrationAge() {
		System.out.println("Testing user registration with invalid attributes for 'age'...");
		SimpleAttributes validUserAttributes = new SimpleAttributes();
		Map<String, Object> validAttributeMap = new HashMap<>();
		validAttributeMap.put("age", 11);
		validAttributeMap.put("country", "USA");
		validAttributeMap.put("subscriptionLevel", "Free");
		validAttributeMap.put("userLicense", "Business");
		validUserAttributes.setAttributePairs(validAttributeMap);
		assertThatThrownBy(() -> marketController.registerUser(validUserAttributes))
				.isExactlyInstanceOf(InvalidAttributeException.class);
	}

	@Test
	public void testInvalidUserRegistrationCountry() {
		System.out.println("Testing user registration with invalid attributes for 'country'...");
		SimpleAttributes validUserAttributes = new SimpleAttributes();
		Map<String, Object> validAttributeMap = new HashMap<>();
		validAttributeMap.put("age", 12);
		validAttributeMap.put("country", "Russia");
		validAttributeMap.put("subscriptionLevel", "Free");
		validAttributeMap.put("userLicense", "Business");
		validUserAttributes.setAttributePairs(validAttributeMap);
		assertThatThrownBy(() -> marketController.registerUser(validUserAttributes))
				.isExactlyInstanceOf(InvalidAttributeException.class);
	}

	@Test
	public void testInvalidUserRegistrationSubscriptionLevel() {
		System.out.println("Testing user registration with invalid attributes for 'subscriptionLevel'...");
		SimpleAttributes validUserAttributes = new SimpleAttributes();
		Map<String, Object> validAttributeMap = new HashMap<>();
		validAttributeMap.put("age", 12);
		validAttributeMap.put("country", "USA");
		validAttributeMap.put("subscriptionLevel", "None");
		validAttributeMap.put("userLicense", "Business");
		validUserAttributes.setAttributePairs(validAttributeMap);
		assertThatThrownBy(() -> marketController.registerUser(validUserAttributes))
				.isExactlyInstanceOf(InvalidAttributeException.class);
	}

	@Test
	public void testInvalidUserRegistrationUserLicense() {
		System.out.println("Testing user registration with invalid attributes for 'userLicense'...");
		SimpleAttributes validUserAttributes = new SimpleAttributes();
		Map<String, Object> validAttributeMap = new HashMap<>();
		validAttributeMap.put("age", 12);
		validAttributeMap.put("country", "USA");
		validAttributeMap.put("subscriptionLevel", "Free");
		validAttributeMap.put("userLicense", "Pirated Version");
		validUserAttributes.setAttributePairs(validAttributeMap);
		assertThatThrownBy(() -> marketController.registerUser(validUserAttributes))
				.isExactlyInstanceOf(InvalidAttributeException.class);
	}

	@Test
	public void testStartInterviewWithoutRegistration() {
		String requestName = "testStartInterviewWithoutRegistration";
		/* Should break here! */
		assertThatThrownBy(() -> initialInterviewController.initializeServiceRequest(requestName))
				.isExactlyInstanceOf(NoUserRegisteredException.class);
	}

	@Test
	public void selectNonExistantOtfProvider() {
		testValidUserRegistration();
		String requestName = "testSelectNonExistantOTFProvider";
		String userInput = "I want to do machine learning.";
		ResponseEntity<SimpleJSONUuid> uuid = initialInterviewController.initializeServiceRequest(requestName);
		UUID requestUUID = uuid.getBody().getUuid();
		initialInterviewController.initialInterview(requestUUID, userInput);
		/* Negligible chance of a duplicate uuid here. */
		assertThatThrownBy(() -> otfpController.acceptOtfProvider(requestUUID, UUID.randomUUID()))
				.isExactlyInstanceOf(NoSuchOtfProviderException.class);
	}

	@Test
	public void testInterviewForNonExistantRequest() {
		testValidUserRegistration();
		String userInput = "I want to do machine learning.";
		/* Proceed with any uuid */
		assertThatThrownBy(() -> initialInterviewController.initialInterview(UUID.randomUUID(), userInput))
				.isExactlyInstanceOf(NoSuchRequestException.class);
	}

	@Test
	public void testInvalidInterviewAnswerForLists() {
		testValidUserRegistration();
		String requestName = "testInvalidInterviewAnswer";
		String userInput = "I want to do machine learning.";
		ResponseEntity<SimpleJSONUuid> uuid = initialInterviewController.initializeServiceRequest(requestName);
		UUID requestUUID = uuid.getBody().getUuid();
		initialInterviewController.initialInterview(requestUUID, userInput);
		otfpController.acceptOtfProvider(requestUUID, mockedProvider.getOtfpUUID());

		/* Now answer */
		List<List<String>> interviewData = new ArrayList<>();
		interviewData.add(Arrays.asList(mockedQuestionList.getQuestionID(), "D"));
		assertThatThrownBy(() -> otfpController.answerProsecoInterview(requestUUID, interviewData))
				.isExactlyInstanceOf(InvalidAnswerException.class);
	}

	@Test
	public void testInvalidInterviewAnswerForNumbers() {
		testValidUserRegistration();
		String requestName = "testInvalidInterviewAnswer";
		String userInput = "I want to do machine learning.";
		ResponseEntity<SimpleJSONUuid> uuid = initialInterviewController.initializeServiceRequest(requestName);
		UUID requestUUID = uuid.getBody().getUuid();
		initialInterviewController.initialInterview(requestUUID, userInput);
		otfpController.acceptOtfProvider(requestUUID, mockedProvider.getOtfpUUID());

		/* Now answer the list question */
		List<List<String>> interviewData = new ArrayList<>();
		interviewData.add(Arrays.asList(mockedQuestionList.getQuestionID(), "A"));
		otfpController.answerProsecoInterview(requestUUID, interviewData);
		interviewData.add(Arrays.asList(mockedQuestionNumber.getQuestionID(), "Fourthy-Two"));
		assertThatThrownBy(() -> otfpController.answerProsecoInterview(requestUUID, interviewData))
				.isExactlyInstanceOf(InvalidAnswerException.class);
	}

	// TODO add checks that the request is in the correct state after each phase...
	@Test
	public void testServiceRequesterWorkflow() {
		System.out.println("Starting to test service requester workflow.");
		/* Setup a valid user registration */
		testValidUserRegistration();

		String requestName = "testChatbotAndRegistryValidEntry";
		String userInput = "I want to do machine learning.";

		ResponseEntity<SimpleJSONUuid> uuid = initialInterviewController.initializeServiceRequest(requestName);
		UUID requestUUID = uuid.getBody().getUuid();
		initialInterviewController.initialInterview(requestUUID, userInput);

		/* Answer a valid provider... */
		otfpController.acceptOtfProvider(requestUUID, mockedProvider.getOtfpUUID());
		/* Answer a valid question... */
		List<List<String>> interviewData = new ArrayList<>();
		interviewData.add(Arrays.asList(mockedQuestionList.getQuestionID(), "A"));
		otfpController.answerProsecoInterview(requestUUID, interviewData);
		interviewData.add(Arrays.asList(mockedQuestionNumber.getQuestionID(), "42"));
		otfpController.answerProsecoInterview(requestUUID, interviewData);
		/* Some processing ... */
		assertEquals(JobState.BUILDING, dataManagementController.getJobstateForRequest(requestUUID).getBody());
		assertEquals(JobState.DONE, dataManagementController.getJobstateForRequest(requestUUID).getBody());
		/* Get the offers & accept one*/
		List<Offer> offers = dataManagementController.getAllOffersForRequest(requestUUID).getBody();
		assertFalse(offers.isEmpty());
		//TODO mock rabbit
		
	//	marketController.buyOffer(requestUUID, offers.get(0).getOfferUUID());
	}

	private String getHashOfOffer() {
		return null;
	}
}
