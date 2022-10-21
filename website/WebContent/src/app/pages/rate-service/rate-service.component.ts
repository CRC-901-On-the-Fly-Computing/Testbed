import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { ServiceReputation } from '../../generated/api/service-requester/model/serviceReputation';
import { UserApiService } from '../../generated/api/service-requester/api/userApi.service';
import { ItemAndRequest } from '../../generated/api/service-requester/model/itemAndRequest';

import { MessageService } from '../../services/messages/messages.service';
import { LoadingService } from '../../services/loading/loading.service';

@Component({
  selector: 'app-rate-service',
  templateUrl: './rate-service.component.html',
  styleUrls: ['./rate-service.component.sass']
})

export class RateServiceComponent implements OnInit {

  constructor(
    private loading: LoadingService,
    private message: MessageService,
    private router: Router,
    private route: ActivatedRoute,
    private userApiService: UserApiService,
  ) { }

  requestUUID: string;
  rates: ServiceReputation;
  requests: Array<ItemAndRequest>;
  selectedRequest: ItemAndRequest;
  checked: boolean;
  requestInProgress: boolean;
  result = {};
  error = {};

  ngOnInit() {
    this.loadData();
    this.route.params.subscribe(params => {
      this.requestUUID = params['id'] || '';
    });

    this.rates = {
      date: 0,
      other: 0,
      overall: 0,
      performance: 0,
      reputationMessage: '',
      security: 0,
      usability: 0
    };
  }


  loadData = () => {
    this.loading.show();
    this.requests = [];
    this.userApiService.getAllItems().subscribe(response => {
      this.requests = response;
      this.checked = true;
      this.loading.hide();
      this.selectedRequest = response.filter(x => x.requestUUID === this.requestUUID)[0] || response[response.length - 1];
    }, error => {
      this.loading.hide();
      this.message.error(error.message);
    });
  }

  onSubmit = () => {
    this.requestInProgress = true;
    this.userApiService.putRating(this.rates, this.selectedRequest.requestUUID).subscribe(response => {
      this.message.success(response.message);
      this.result = response;
      this.requestInProgress = false;
    }, error => {
      this.requestInProgress = false;
      this.message.error(error.message);
    });
  }

  onChange = () => {
    this.message.clear();
    const path = this.route.snapshot.routeConfig.path.split('/')[0];
    if (path) {
      this.router.navigate([`/${path}/${this.selectedRequest.requestUUID}`]);
    }
  }

}
