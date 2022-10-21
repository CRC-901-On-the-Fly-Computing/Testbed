import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RequesterApiService } from '../../generated/api/service-requester/api/requesterApi.service';
import { MessageService } from '../../services/messages/messages.service';

@Component({
  selector: 'app-user-creator',
  templateUrl: './user-creator.component.html',
  styleUrls: ['./user-creator.component.sass']
})

export class UserCreatorComponent implements OnInit {
  constructor(
    private message: MessageService,
    public formBuilder: FormBuilder,
    public requesterApiService: RequesterApiService
  ) {
    this.createForm();
  }

  readonly minAge = 0;
  readonly maxAge = 120;
  readonly licenses = [
    { name: 'Private', value: 'Private' },
    { name: 'Education', value: 'Education' },
    { name: 'Commercial', value: 'Commercial' }
  ];
  readonly subscriptionlevels = [
    { name: 'Prime', value: 'Prime' },
    { name: 'Basic', value: 'Basic' },
    { name: 'Free Trial', value: 'FreeTrial' }
  ];

  form: FormGroup;
  response = '';
  showSpinner = false;
  result: any;

  ngOnInit() {
    this.response = '';
  }

  createForm() {
    this.form = this.formBuilder.group({
      username: ['', Validators.required],
      attributes: this.formBuilder.group({
        attributePairs: this.formBuilder.group({
          country: 'USA',
          subscriptionLevel: 'Prime',
          userLicense: 'Private',
          age: [20, [Validators.min(this.minAge), Validators.max(this.maxAge)]]
        })
      })
    });
  }

  onFakeRegister = () => {
    const fakeData = {
      attributePairs: {
        country: 'USA',
        subscriptionLevel: 'Prime',
        userLicense: 'Private',
        age: 20
      }
    };

    this.requesterApiService.registerUser(fakeData).subscribe(
      response => this.message.success(response.message)
      , error => this.message.error(error.message)
    );
  }

  onSubmit() {
    if (this.form.invalid) {
      this.response = 'Form is invalid.';
      return;
    }

    this.response = '';
    this.showSpinner = true;

    const username = this.form.controls.username.value;
    const body = this.form.controls.attributes.value;

    // dummy - delete when no longer needed - vvvvv
    setTimeout(() => {
      console.log(body);
      this.showSpinner = false;
      this.response = 'This is a dummy. Nothing send to server.';
    }, 2000);
    // dummy - delete when no longer needed - ^^^^^

    // this is the real one - vvvvv
    //    this.userApiService.registerUserUsingPOST(username, body, 'response').subscribe(
    //      response => { this.showSpinner = false; this.response = response.body.message; console.log(response); },
    //      error => { this.showSpinner = false; this.response = String(error.error.message); console.error(error); }
    //    );
    // this is the real one - ^^^^^
  }

}
