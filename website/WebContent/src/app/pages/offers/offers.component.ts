import { Component, OnInit } from '@angular/core';
import { UserApiService } from '../../generated/api/service-requester/api/userApi.service';
import { ServiceCompositionRatingsService } from '../../generated/api/review-board';

import { LoadingService } from '../../services/loading/loading.service';
import { MessageService } from '../../services/messages/messages.service';

@Component({
  selector: 'app-offers',
  templateUrl: './offers.component.html',
  styleUrls: ['./offers.component.sass']
})

export class OffersComponent implements OnInit {

  constructor(
    private userApiService: UserApiService,
    private serviceCompositionRatingsService: ServiceCompositionRatingsService,
    private loading: LoadingService,
    private message: MessageService
  ) { }

  requests: Array<object>;
  checked = false;
  serviceRatings = {};
  displayedColumns = ['composition', 'price', 'score', 'reputation', 'actions'];

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.loading.show();
    this.requests = [];
    this.userApiService.getAllOffers().subscribe(response => {
      response.map(req => {
        req.offers.map(offer => {
          offer.offerScore = Number.parseFloat(offer.offerScore.toFixed(2));
          offer.compositionID = offer.compositionID || offer.compositionAndOTFProvider.simpleComposition.serviceCompositionId;
          // tslint:disable-next-line:max-line-length
          this.serviceCompositionRatingsService.getAggregatedServiceCompositionReputationUsingGET(offer.compositionID).subscribe(_response => {
            this.serviceRatings[offer.compositionID] = _response || { overall: 0 };
          }, error => {
            this.serviceRatings[offer.compositionID] = { overall: 0 };
          });
        });

        req.offers.sort((a, b) => b.offerScore - a.offerScore);

      });
      this.requests = response;
      this.loading.hide();
      this.checked = true;
    }, error => {
      this.loading.hide();
      this.message.error(error.message);
    });
  }
}
