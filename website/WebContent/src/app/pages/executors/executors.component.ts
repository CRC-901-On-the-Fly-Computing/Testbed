import { Component, OnInit } from '@angular/core';
import { ExecutorsService } from '../../services/executors/executors.service';
import { LoadingService } from '../../services/loading/loading.service';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import APP_URL from '../../app.url.json';

@Component({
  selector: 'app-executors',
  templateUrl: './executors.component.html',
  styleUrls: ['./executors.component.sass']
})

export class ExecutorsComponent implements OnInit {

  constructor(
    private executorsService: ExecutorsService,
    private loading: LoadingService,
    private route: ActivatedRoute,
    private location: Location,
  ) { }

  uuid = '';
  log = '';
  dataSource = [];
  compositions = {};
  displayedColumns = ['uuid', 'composition', 'actions'];
  checked = false;
  baseURLs = [
    APP_URL.EXEC_GATEWAY_1,
    APP_URL.EXEC_GATEWAY_2
  ];

  ngOnInit() {
    this.uuid = this.route.snapshot.paramMap.get('id') || '';
    this.loadData();
  }

  loadData() {
    this.loading.show();

    this.baseURLs.forEach((bUrl, i) => {
      this.executorsService.getAddresses(bUrl).subscribe(response => {

        // parse response and remove duplicates
        const ds = this.executorsService.parseUrls(response).filter(x => this.dataSource.findIndex(y => x.uuid === y.uuid) === -1);

        // merge lists
        this.dataSource = [...this.dataSource, ...ds];
        this.executorsService.getServices(bUrl).subscribe(_response => {
          this.compositions = { ...this.compositions, ...this.executorsService.parseCompositions(_response) };
          if (!this.uuid) {
            this.checked = true;
            this.loading.hide();
          }
        }, err => {
          this.checked = true;
          this.loading.hide();
        });

        // when all all data is fetched now it loads logs
        if (i === this.baseURLs.length - 1) {
          if (this.uuid) {
            this.loadLog();
          }
        }

      }, err => {
        this.checked = true;
        this.loading.hide();
      });
    });
  }

  // loading logs based on this.uuid
  loadLog() {
    this.checked = false;
    const url = this.dataSource.filter(x => x.uuid === this.uuid)[0]['url'];
    if (url) {
      this.loading.show();
      this.executorsService.getLog(url, this.uuid).subscribe(response => {
        this.log = response;
        this.loading.hide();
        this.checked = true;
      }, error => {
        this.log = 'There is no log';
        this.loading.hide();
        this.checked = true;
      });
    }
  }

  // caculating object length
  objectLength = (obj) => Object.keys(obj).length;

  onClick = (uuid) => {
    this.uuid = uuid;
    this.log = '';
    const path = this.route.snapshot.routeConfig.path.split('/')[0];
    if (path) {
      this.location.go(`/${path}/${uuid}`);
      this.loadLog();
    }
    return false;
  }

  goBack = () => {
    const path = this.route.snapshot.routeConfig.path.split('/')[0];
    if (path) {
      this.location.go(`/${path}`);
      this.uuid = '';
      this.log = '';
    }
    return false;
  }
}
