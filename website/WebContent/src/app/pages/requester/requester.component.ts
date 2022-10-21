import { Component, OnInit, Input, ElementRef, ViewChild } from '@angular/core';
import { OTFProviderConfidence } from '../../generated/api/service-requester/model/oTFProviderConfidence';
import { UserApiService } from '../../generated/api/service-requester/api/userApi.service';
import { InterviewResponse } from '../../generated/api/service-requester';
import { OtfProviderRatingsService } from '../../generated/api/review-board';
import { LoadingService } from '../../services/loading/loading.service';
import APP_URL from '../../app.url.json';
import { IntroService } from '../../services/intro/intro.service';
import {forEach} from "@angular/router/src/utils/collection";


class Message {
  public type: string;
  public extra: Array<any>;
  public body: string;
  public timestamp: Date;
  public from: string;
  public callback: Function;
  public extraCallback: Function;
  public questionID: string;

  constructor(
    body?: string,
    from = 'bot',
    timestamp = new Date(),
    callback?: Function,
    type = InterviewResponse.QuestionTypeEnum.TEXT,
    extra: Array<any> = [],
    extraCallback?: Function,
    questionID?: string
  ) {
    this.type = type;
    this.extra = extra;
    this.body = body;
    this.timestamp = timestamp;
    this.from = from;
    this.callback = callback;
    this.extraCallback = extraCallback;
    this.questionID = questionID;
  }
}

@Component({
  selector: 'app-requester',
  templateUrl: './requester.component.html',
  styleUrls: ['./requester.component.sass']
})

export class RequesterComponent implements OnInit {
  @ViewChild('messageElement') messageInputEl: ElementRef;
  @Input() message: Message = new Message('', 'user');

  constructor(
    private userApiService: UserApiService,
    private otfProviderRatingsService: OtfProviderRatingsService,
    private loading: LoadingService,
    private intro: IntroService,
  ) {
  }

  basePath = APP_URL.BASE_PATH_SR;
  fileUploadURL = `${this.basePath}/requests/{{uuid}}/upload`;
  projectName = '';
  requirements = '';
  requestUUID = '';
  prosecoInterviewQuestion = '';
  currentAnswer = '';
  requests = [];
  selectedProvider: OTFProviderConfidence;
  otfproviderList: OTFProviderConfidence[] = null;
  requestInProgress = false;
  questionIndex = 0;
  extractedInformation = {};
  currentlyAnsweredQuestion: Map<string, string> = new Map;
  messages = new Array<Message>();
  questions = new Array<Message>();

  otfLabels = {
    'otf-provider': 'Otf-Provider',
    'distr': 'Label for distro',
    'image': 'Label for image'
  }

  otfLabelsSwap = {
    'Otf-Provider':'otf-provider',
    'Label for distro': 'distr',
    'Label for image': 'image'
  }

  mlLabel = {
    'imagefiles-multiclass-classification' : 'Multi class classification image-ml1',
    'distributed-multiclass-classification' : 'Multi class classification distributed-ml2'
  }
  mlLabelSwap = {
    'Multi class classification image-ml1' : 'imagefiles-multiclass-classification',
    'Multi class classification distributed-ml2' : 'distributed-multiclass-classification'
  }

  scrollToBottom = () => {
    const lst = document.querySelectorAll('div.speech-bubble');
    if (lst.length) {
      lst[lst.length - 1].scrollIntoView();
    }
  }

  sendMessage = () => {
    if (!this.message.body || this.requestInProgress) {
      return false;
    }

    this.messages.push(this.message);

    // validate current message and last message from the bot
    if (this.inputValidator(this.message, this.messages.filter(m => m.from === 'bot').slice(-1)[0])) {
      if (this.questions[this.questionIndex] && this.questions[this.questionIndex].callback) {
        const callback = this.questions[this.questionIndex].callback;
        console.log('callback', this.message);
        callback.call(this);
      }
    }

    this.message = new Message('', 'user');
    this.focusOnMessageInput();
  }

  inputValidator(message: Message, question: Message) {
    let warning: Message;

    // number validation
    if (question.type === InterviewResponse.QuestionTypeEnum.NUMBER && !(parseInt(message.body, 0) > 0)) {
      warning = new Message('Please enter a number.', 'bot_warning');
    }

    // file upload valiation
    if (question.type === InterviewResponse.QuestionTypeEnum.FILE && message.body.length > 0) {
      warning = new Message('Please try to upload the file.', 'bot_warning');
    }

    // timeout validation
    if (question.questionID === 'timeout' && parseInt(message.body, 0) < 300) {
      this.message.body = '300';
    }

    // enum valition
    if (question.type === InterviewResponse.QuestionTypeEnum.DROPDOWN) {
      const qIndex = question.extra.findIndex(x => {
        const y = typeof (x) === 'object' ? x.label : x;
        return y.toLowerCase() === message.body.toLowerCase();
      });

      if (qIndex === -1) {
        warning = new Message('Please choose or type one of options.', 'bot_warning');
      }
    }

    // send a warning message
    if (warning !== undefined) {
      this.messages.push(warning);
      return false;
    }

    return true;
  }

  /**
   * STEP 1: Select a project name and store the returned uuid!
   */
  selectName = () => {
    this.projectName = this.message.body;

    this.requestInProgress = true;
    this.userApiService.initializeServiceRequest(this.projectName).subscribe(
      response => {
        this.requestUUID = response.uuid;
        this.requests.push(this.requestUUID);
        this.fileUploadURL = this.fileUploadURL.replace('{{uuid}}', this.requestUUID);
        this.requestInProgress = false;
        this.questionIndex++;
        if (this.questionIndex < this.questions.length) {
          this.messages.push(this.questions[this.questionIndex]);
        }

      },
      error => { console.error(error); }
    );
  }

  /**
   * STEP 2: User gives some sort of input. TODO: What if the user doesn'z give us enough?!
   */
  sendRequirements = () => {
    this.requirements = this.message.body;
    this.requestInProgress = true;
    console.log('POSTING ' + this.requirements);
    this.userApiService.initialInterview(this.requestUUID, this.requirements).subscribe(
      response => {
        this.otfproviderList = response;
        if (this.otfproviderList && this.otfproviderList.length) {
          this.questionIndex++;
          this.questions[this.questionIndex].extra = [];
          this.otfproviderList.map(x => this.otfProviderRatingsService.getOtfProviderRatingUsingGET(x.otfProvider.otfpUUID).subscribe(
            _response => {
              this.requestInProgress = false;
              this.questions[this.questionIndex].extra.push({
                label: this.otfLabels[x.otfProvider.otfProviderName] || 'Undefined',
                rate: _response.overall,
                name: x.otfProvider.otfProviderName,
              });
              // get rid of the callback trap, check if all otfproviders got the ratings
              if (this.questions[this.questionIndex].extra.length === this.otfproviderList.length) {
                this.messages.push(this.questions[this.questionIndex]);
              }
            }));
        } else {
          // tslint:disable-next-line:max-line-length
          this.messages.push(new Message(`Sorry, for “${this.requirements}” there is no OTF Provider that you can select. Please try again.`));
          this.requestInProgress = false;
        }
      },
      error => { console.error(error); }
    );
  }
  /**
   * STEP 3: The user selects an otf-provider.
   * @param selectedProvider
   */
  selectOtfProvider = () => {
    this.selectedProvider = this.otfproviderList.filter(
      x => x.otfProvider.otfProviderName.toLowerCase() === this.otfLabelsSwap[this.message.body]
    )[0] || null;

    if (!this.selectedProvider) {
      this.questions[this.questionIndex].extra = [];
      this.otfproviderList.map(x => this.questions[this.questionIndex].extra.push({ label: this.otfLabels[x.otfProvider.otfProviderName] }));
      this.messages.push(this.questions[this.questionIndex]);
      return null;
    }

    this.requestInProgress = true;
    console.log('POSTING ' + this.selectedProvider.otfProvider.otfpUUID);
    this.userApiService.acceptOTFProvider(this.selectedProvider.otfProvider.otfpUUID, this.requestUUID).subscribe(
      response => {
        this.prosecoInterviewQuestion = response.questionID;
        console.log(response);
        let questionText = response.question;
        if (response.questionID === 'timeout') {
          questionText = 'Creating your individual service may take some time.' +
            'The more time you give, the better service compositions can be found.' +
            'Please specify how many seconds you are willing to wait. The minimum value is 300.';
        }
        this.questionIndex++;

        let extras = []

        response.dropdownList.forEach(x => {
          extras.push({
            label : this.mlLabel[x],
            name : x
          });
        })

        const message = new Message(
          questionText,
          'bot',
          new Date(),
          this.sendProsecoAnswer,
          response.questionType,
          extras,
          this.dropDownClick,
          response.questionID
        );

        this.questions.push(message);
        this.messages.push(message);

        this.requestInProgress = false;
      },
      error => { console.error(error); }
    );
  }

  sendProsecoAnswer = (fileURL = '') => {
    if(!!this.mlLabelSwap[this.message.body]){
      this.currentAnswer = fileURL.length ? fileURL : this.mlLabelSwap[this.message.body];
    } else{
      this.currentAnswer = fileURL.length ? fileURL : this.message.body;
    }
    this.requestInProgress = true;
    this.currentlyAnsweredQuestion.set(this.prosecoInterviewQuestion, this.currentAnswer);
    console.log('currentlyAnsweredQuestion:', this.currentlyAnsweredQuestion);
    const interviewJson: string = JSON.stringify(Array.from(this.currentlyAnsweredQuestion.entries()));
    console.log('POSTING ' + interviewJson);
    this.userApiService.answerProsecoInterview(this.requestUUID, interviewJson).subscribe(
      response => {
        console.log('sendProsecoAnswer', response);
        if (response.interviewState === InterviewResponse.InterviewStateEnum.INTERVIEWDONE) {
          console.log('We are done with the proseco interview!');
          this.getExtractedInformation();
        } else {
          this.requestInProgress = false;
          this.prosecoInterviewQuestion = response.questionID;
          this.currentAnswer = '';

          const message = new Message(response.question, 'bot', new Date(), this.sendProsecoAnswer, response.questionType);
          switch (response.questionType) {
            case InterviewResponse.QuestionTypeEnum.DROPDOWN:
              message.extraCallback = this.dropDownClick;
              message.extra = response.dropdownList;
              break;
            case InterviewResponse.QuestionTypeEnum.FILE:
              // asking user upload the file
              this.messages.push(new Message(response.question, 'bot', new Date(), () => { }, response.questionType));

              // let user upload file from his/her side
              message.body = '';
              message.from = 'user';
              message.extraCallback = this.fileUploadCallback;
              break;
            case InterviewResponse.QuestionTypeEnum.NOQUESTION:
              break;
            case InterviewResponse.QuestionTypeEnum.NUMBER:
              break;

            default:
              break;
          }
          console.log('sendProsecoAnswer-message', message);
          this.messages.push(message);
        }
      },
      error => { console.error(error); }
    );
  }

  getExtractedInformation = () => {
    console.log('GETTING EXTRACTED INFO');

    this.userApiService.getExtractedInformation(this.requestUUID).subscribe(
      response => {
        this.extractedInformation = response;
        console.log(response);
        this.requestInProgress = false;
        let message = new Message('Done! I got your information. Here they are:');
        this.messages.push(message);

        const text = Object.entries(response).map(x => `${x[0]}: <strong>${x[1]}</strong>`).join('<br>');
        message = new Message(text);
        this.messages.push(message);

        message = new Message(
          'I\'ll carefully bring you some offers, ' +
          'please check them on ' +
          // tslint:disable-next-line:max-line-length
          '<a ng-reflect-router-link="/requests-in-process" routerlink="/requests-in-process" href="/requests-in-process/${this.requestUUID}">' +
          'Requests in Process</a> page.'
        );
        this.messages.push(message);

        // do not answer anymore
        this.questionIndex++;
      },
      error => { console.error(error); }
    );
  }

  ngOnInit() {
    this.loading.hide();
    this.initialMessages();
    this.focusOnMessageInput();

    console.log(this.intro.start());
  }

  initialMessages() {
    let message = new Message(`Hello. I am Cordula, your personal assistant that creates personal IT services for you on-the-fly.`);
    this.messages.push(message);

    message = new Message(`Please enter a name for your service.`, 'bot', new Date(), this.selectName);
    this.questions.push(message);
    this.messages.push(message);

    message = new Message(`What kind of problem should your personal IT service solve?`, 'bot', new Date(), this.sendRequirements);
    this.questions.push(message);

    // tslint:disable-next-line:max-line-length
    message = new Message(`I think these providers can create a personal service for you. From which provider would you like to obtain offers?`, 'bot', new Date(), this.selectOtfProvider, 'DROPDOWN', [], this.dropDownClick);
    this.questions.push(message);
  }

  dropDownClick = (selectedItem?: string) => {
    this.message.body = selectedItem;
    this.sendMessage();
  }

  fileUploadCallback = (item: any, response: any, status: any, headers: any) => {
    if (status === 200) {
      this.sendProsecoAnswer(response);
    }
  }

  focusOnMessageInput = () => {
    this.messageInputEl.nativeElement.focus();
  }
}
