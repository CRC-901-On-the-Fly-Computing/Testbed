package de.upb.crc901.otftestbed.commons.requester;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InterviewResponse {

  public enum InterviewState {
    MORE_INFORMATION_NEEDED, INTERVIEW_DONE
  }

  public enum QuestionType {
    FILE, NO_QUESTION, DROPDOWN, NUMBER, TEXT
  }

  @JsonProperty
  private InterviewState interviewState;

  @JsonProperty
  private String question;
  
  @JsonProperty
  private String questionID;

  @JsonProperty
  private QuestionType questionType;

  @JsonProperty
  private List<String> dropdownList;

  public InterviewResponse() {
    super();
  }

  public InterviewResponse(InterviewState interviewState, String nextQuestion,
      QuestionType responseType, List<String> dropdownList, String questionID) {
    super();
    this.interviewState = interviewState;
    this.question = nextQuestion;
    this.dropdownList = dropdownList;
    this.questionType = responseType;
    this.questionID = questionID;
  }


  public InterviewResponse(InterviewState interviewState, String nextQuestion,
      QuestionType responseType, String questionID) {
    this(interviewState, nextQuestion, responseType, new ArrayList<>(), questionID);
  }

  @JsonIgnore
  public InterviewState getInterviewState() {
    return interviewState;
  }

  @JsonIgnore
  public String getQuestion() {
    return question;
  }

  @JsonIgnore
  public QuestionType getQuestionType() {
    return questionType;
  }

  @JsonIgnore
  public List<String> getDropdownList() {
    return dropdownList;
  }

  /**
 * @return the questionID
 */
public String getQuestionID() {
	return questionID;
}

/**
 * @param questionID the questionID to set
 */
public void setQuestionID(String questionID) {
	this.questionID = questionID;
}

public void setInterviewState(InterviewState interviewState) {
    this.interviewState = interviewState;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public void setQuestionType(QuestionType questionType) {
    this.questionType = questionType;
  }

  public void setDropdownList(List<String> dropdownList) {
    this.dropdownList = dropdownList;
  }
  

}
