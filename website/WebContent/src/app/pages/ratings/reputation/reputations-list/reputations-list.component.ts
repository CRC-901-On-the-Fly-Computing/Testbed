import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ServiceCompositionRatingsService } from '../../../../generated/api/review-board';
import { ServiceReputationAndSignature } from '../../../../generated/api/review-board/model/serviceReputationAndSignature';
import { LoadingService } from '../../../../services/loading/loading.service';

@Component({
  selector: 'app-reputations-list',
  templateUrl: './reputations-list.component.html',
  styleUrls: ['./reputations-list.component.sass']
})

export class ReputationsListComponent implements OnInit {

  private compositionID = '';

  constructor(
    private loading: LoadingService,
    private route: ActivatedRoute,
    private serviceCompositionRatingsService: ServiceCompositionRatingsService
  ) { }

  dataSource: Array<ServiceReputationAndSignature>;
  checked = false;

  ngOnInit() {
    this.compositionID = this.route.snapshot.paramMap.get('id');
    if (this.compositionID) {
      this.loadData();
    }
  }

  loadData() {
    this.loading.show();
    this.serviceCompositionRatingsService.getServiceCompositionReviewListUsingGET(this.compositionID).subscribe(
      response => {
        this.dataSource = response;
        this.loading.hide();
        this.checked = true;
      }, error => {
        this.loading.show();
        this.checked = true;
      });

  }
}
