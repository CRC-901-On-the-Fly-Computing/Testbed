import { Component, OnInit } from '@angular/core';
import { LoadingService } from '../../services/loading/loading.service';
import { MessageService } from '../../services/messages/messages.service';
import { HttpClient } from '@angular/common/http';
import APP_URL from '../../app.url.json';

interface ElasticSearchResult {
  hits: Hits;
}

interface Hits {
  hits: Array<Hit>;
}

interface Hit {
  _source: CPACheck;
}

interface CPACheck {
  'cpaCheck.isCertified': boolean;
  'cpaCheck.isVerified': string;
  'cpaCheck.size': string;
  'duration': number;
  'service.name': string;
  'time': number;
}

@Component({
  selector: 'app-view-verification-results',
  templateUrl: './view-verification-results.component.html',
  styleUrls: ['./view-verification-results.component.sass']
})

export class ViewVerificationResultsComponent implements OnInit {

  constructor(
    private loading: LoadingService,
    private message: MessageService,
    private http: HttpClient
  ) { }

  cpa_checks: Array<Hit>;
  checked = false;
  displayedColumns = ['name', 'duration', 'size', 'result'];

  ngOnInit() {
    this.loadData();
  }

  getConfig() {
    // now returns an Observable of Config
    return this.http.get<ElasticSearchResult>(APP_URL.MONITOR_CPA);
  }

  loadData = () => {
    this.loading.show();

    this.getConfig()
      .subscribe((data: ElasticSearchResult) => {
        this.checked = true;
        this.cpa_checks = data.hits.hits;
        this.loading.hide();
      },
        error => {
          this.loading.hide();
          if (error.message !== undefined) {
            this.message.error(error.message);
          } else {
            this.message.error(error);
          }
        }
      );
  }

}
