package de.upb.crc901.otftestbed.service_requester.impl.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import de.upb.crc901.otftestbed.commons.connect.PocConnector;
import de.upb.crc901.otftestbed.commons.requester.InterviewResponse;
import de.upb.crc901.otftestbed.commons.requester.InterviewResponse.InterviewState;
import de.upb.crc901.otftestbed.commons.requester.InterviewResponse.QuestionType;
import de.upb.crc901.otftestbed.commons.telemetry.es.TelemetryService;
import de.upb.crc901.otftestbed.service_requester.impl.components.DataHolderComponent;
import de.upb.crc901.otftestbed.service_requester.impl.exceptions.InterviewFailedException;
import de.upb.crc901.otftestbed.service_requester.impl.exceptions.InvalidAnswerException;
import de.upb.crc901.otftestbed.service_requester.impl.models.ProsecoInterviewData;

@Component
public class OtfProviderController {

	private static final Logger logger = LoggerFactory.getLogger(OtfProviderController.class);

	@Autowired
	private PocConnector connect;

//	@Autowired
//	private TelemetryService telemetry;

	@Autowired
	private DataHolderComponent holder;

	/**
	 * Third phaser of the interview: the user starts with selecting an
	 * otf-provider.
	 * 
	 * @param requestUUID the unique identifier of the request
	 * @param otfpUUID    the uuid of the selected otf-provider
	 * @return the first question of the interview
	 */
	public ResponseEntity<InterviewResponse> acceptOtfProvider(UUID requestUUID, UUID otfpUUID) {
		holder.checkForExistentRequestAndRegisteredUser(requestUUID);

		logger.debug("Accepting otf provider...");
		holder.acceptOtfProvider(requestUUID, otfpUUID);

		ProsecoInterviewData prosecoInterviewData = holder.getProsecoInterviewDataForUUID(requestUUID);

		// TODO: remove when requests are send and logged by the OTFP-Registry
//		telemetry.logRequest(requestUUID.toString(),
//				prosecoInterviewData.getProsecoValues().get("domain"), otfpUUID.toString(), null);

		logger.debug("Getting first proseco question ...");
		return processProsecoInterviewResponse(requestUUID, prosecoInterviewData);
	}

	/**
	 * Answers a question from the interview. Then queries the OTF-Configurator for
	 * the next interview question, based on the currently known information. If
	 * there is no next question the configuration process will be started.
	 * 
	 * @param requestUUID the unique identifier of the request
	 * @param interview   all currently known interview information parsed in the
	 *                    format <code> [[question_key, question_answer]] </code>.
	 * 
	 * @return the interview response which is either a next question, or, the
	 *         notification that the configuration started,
	 */
	public ResponseEntity<InterviewResponse> answerProsecoInterview(UUID requestUUID, Object interview) {

		try {
			holder.checkForExistentRequestAndRegisteredUser(requestUUID);
			ProsecoInterviewData prosecoInterviewData = holder.getProsecoInterviewDataForUUID(requestUUID);
			logger.debug("User answered: {}, {}", interview, interview.getClass());
			Map<String, String> interviewAnswers = parseFromUserInput(interview);
			
			// check if the answer was valid
			checkAnswers(interviewAnswers, prosecoInterviewData);
			logger.debug("Current answers: {}", interviewAnswers);
			Map<String, String> extractedValues = interviewAnswers;

			prosecoInterviewData.updateProsecoValues(extractedValues);

			return processProsecoInterviewResponse(requestUUID, prosecoInterviewData);
		} catch (NoSuchElementException e) {
			logger.error("Failed to extract answers!", e);
			throw new InterviewFailedException();
		}
	}

	/**
	 * Helper method that parses the interview data from the website into a suitable
	 * format for the dataholder.
	 * 
	 * @param interview
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> parseFromUserInput(Object interview) {
		Map<String, String> toReturn = new HashMap<>();
		ArrayList<ArrayList<String>> input = (ArrayList<ArrayList<String>>) interview;
		Iterator<ArrayList<String>> i = input.iterator();
		while (i.hasNext()) {
			List<String> field = i.next();
			Iterator<String> fieldIterator = field.iterator();
			String question = fieldIterator.next();
			String answer = fieldIterator.next();
			toReturn.put(question, answer);
		}
		return toReturn;
	}

	private void checkAnswers(Map<String, String> userAnswers, ProsecoInterviewData prosecoInterviewData) {
		List<InterviewResponse> allQuestions = prosecoInterviewData.getAllQuestions();
		for (Map.Entry<String, String> userAnswer : userAnswers.entrySet()) {
			Optional<InterviewResponse> optionalMatchingQuestion = allQuestions.stream()
					.filter(q -> userAnswer.getKey().equals(q.getQuestionID())).findFirst();
			if (!optionalMatchingQuestion.isPresent()) {
				// if there was no such question, this could be just meta data
				continue;
			}
			InterviewResponse matchingQuestion = optionalMatchingQuestion.get();
			if (matchingQuestion.getQuestionType() == QuestionType.NUMBER) {
				// check if this is a number
				if (!userAnswer.getValue().matches("-?\\d+")) {
					throw new InvalidAnswerException();
				}
			} else if (matchingQuestion.getQuestionType() == QuestionType.DROPDOWN) {
				if (!matchingQuestion.getDropdownList().contains(userAnswer.getValue())) {
					throw new InvalidAnswerException();
				}
			}
		}
	}

	/**
	 * Internal helper method that queries the otf provider.
	 * 
	 * @param requestUUID          the unique identifier of the request
	 * @param prosecoInterviewData the interview data in a suitable format.
	 * @return
	 */
	private ResponseEntity<InterviewResponse> processProsecoInterviewResponse(UUID requestUUID,
			ProsecoInterviewData prosecoInterviewData) {
		String prosecoUrl = holder.getOtfProviderURLForRequest(requestUUID);
		logger.debug("Proseco url is {}", prosecoUrl);
		Map<String, String> prosecoStringMap = prosecoInterviewData.getProsecoValues();
		InterviewResponse prosecoResponse = connect.toOtfProvider().callProsecoConfigurator(prosecoUrl)
				.postInterview(prosecoStringMap, requestUUID);
		logger.debug(prosecoResponse+ "");
		prosecoInterviewData.addProsecoQuestion(prosecoResponse);
		logger.debug("Proseco answered: {} (Interview state is: {})", prosecoResponse.getQuestion(),
				prosecoResponse.getInterviewState());
		if (prosecoResponse.getInterviewState().equals(InterviewState.INTERVIEW_DONE)) {
			logger.debug("Interview done!");
			logger.debug("Starting configuration run!");
			connect.toOtfProvider().callProsecoConfigurator(prosecoUrl).runConfiguration(requestUUID);
			holder.finishThirdStepOfInterview(requestUUID);
		}
		return ResponseEntity.ok(prosecoResponse);
	}
}
