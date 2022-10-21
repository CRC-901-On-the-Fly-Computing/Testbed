import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserApiService } from '../../../generated/api/service-requester/api/userApi.service';
import { Offer } from '../../../generated/api/service-requester/model/offer';
import { ServiceRatingsService, ServiceCompositionRatingsService } from '../../../generated/api/review-board';

import { MessageService } from '../../../services/messages/messages.service';
import { LoadingService } from '../../../services/loading/loading.service';
import { DialogService } from '../../../services/dialog/dialog.service';

@Component({
  selector: 'app-offer',
  templateUrl: './offer.component.html',
  styleUrls: ['./offer.component.sass']
})
export class OfferComponent implements OnInit {

  constructor(
    private loading: LoadingService,
    private message: MessageService,
    private dialog: DialogService,
    private route: ActivatedRoute,
    private userApiService: UserApiService,
    private serviceRatingsService: ServiceRatingsService,
    private serviceCompositionRatingsService: ServiceCompositionRatingsService,
  ) { }

  offer: Offer;
  offerUUID = '';
  requestUUID = '';
  compositionID = '';
  services = [];
  result = {};
  error: any;
  offerRating = {};
  serviceRatings = {};
  requestInProgress = false;
  checked = false;

  ngOnInit() {
    this.offerUUID = this.route.snapshot.paramMap.get('id');
    this.requestUUID = this.route.snapshot.paramMap.get('oid');
    if (this.requestUUID && this.offerUUID) {
      this.loadData();
    }
  }

  loadData = () => {
    this.loading.show();
    this.userApiService.getSpecificOffer(this.requestUUID, this.offerUUID).subscribe(response => {
      this.checked = true;
      this.offer = response || {};
      if (this.offer.offerUUID !== undefined) {
        this.offer.offerScore = Number.parseFloat(this.offer.offerScore.toFixed(2));
        this.offer.compositionID = this.offer.compositionID || this.offer.compositionAndOTFProvider.simpleComposition.serviceCompositionId;
        // tslint:disable-next-line:max-line-length
        this.serviceCompositionRatingsService.getAggregatedServiceCompositionReputationUsingGET(this.offer.compositionID).subscribe(_response => {
          this.offerRating = _response || {};
        }, error => {
          this.offerRating = { overall: 0 };
        });

        this.services = this.offer.compositionAndOTFProvider.simpleComposition.serviceId;
        this.services.map(x => {
          this.serviceRatingsService.getAggregatedServiceReputationUsingGET(x).subscribe(_response => {
            this.serviceRatings[x] = _response || { overall: 0 };
          }, error => {
            this.loading.hide();
            this.serviceRatings[x] = { overall: 0 };
          });
        });
      }
      this.loading.hide();
    }, err => {
      this.checked = true;
      this.loading.hide();
      this.error = err.error;
    });
  }

  buyTheOffer = () => {
    if (!this.requestInProgress) {
      this.requestInProgress = true;
      this.userApiService.buyOffer(this.requestUUID, this.offerUUID).subscribe(response => {
        this.result = response;
        this.requestInProgress = false;
        this.message.success(`
          You can check your requested service in <a href="/my-services">My Services Page</a>.
          `);
      }, error => {
        this.requestInProgress = false;
        this.message.error(error.message);
      });
    }
  }

  openDialog = (json) => {
    this.dialog.open(json);
  }
}
