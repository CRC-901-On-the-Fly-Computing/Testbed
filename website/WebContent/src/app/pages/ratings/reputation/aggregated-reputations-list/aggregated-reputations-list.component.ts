import { Component, OnInit } from '@angular/core';
import { ServiceCompositionRatingsService } from '../../../../generated/api/review-board';
import { LoadingService } from '../../../../services/loading/loading.service';
import { MessageService } from '../../../../services/messages/messages.service';

@Component({
  selector: 'app-aggregated-reputations-list',
  templateUrl: './aggregated-reputations-list.component.html',
  styleUrls: ['./aggregated-reputations-list.component.sass']
})

export class AggregatedReputationsListComponent implements OnInit {
  constructor(
    private loading: LoadingService,
    private message: MessageService,
    private serviceCompositionRatingsService: ServiceCompositionRatingsService
  ) { }

  dataSource = [];
  checked = false;
  counts = {};

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.loading.show();
    this.serviceCompositionRatingsService.getAggregatedServiceCompositionReviewListUsingGET().subscribe(
      response => {
        this.checked = true;
        this.dataSource = response.map(x => {
          this.serviceCompositionRatingsService.countServiceCompositionReviewsUsingGET(x.serviceCompositionId).subscribe(
            _response => {
              this.counts[x.serviceCompositionId] = _response.message;
            }
          );
          return x;
        });
        this.loading.hide();
      }, error => {
        this.loading.hide();
        this.message.error(error.message);
      });
  }
}
