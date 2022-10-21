import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserApiService } from '../../../generated/api/service-requester/api/userApi.service';
import { ServiceRatingsService, ServiceCompositionRatingsService } from '../../../generated/api/review-board';
import { LoadingService } from '../../../services/loading/loading.service';
import { MessageService } from '../../../services/messages/messages.service';
import { ServiceRegistryService } from '../../../generated/api/code-provider/api/serviceRegistry.service';
import { ServiceRegistry } from '../../../generated/api/code-provider/model/serviceRegistry';


@Component({
  selector: 'app-services-list',
  templateUrl: './services-list.component.html',
  styleUrls: ['./services-list.component.sass']
})

export class ServicesListComponent implements OnInit {
  constructor(
    private loading: LoadingService,
    private message: MessageService,
    private route: ActivatedRoute,
    private serviceRegistryService: ServiceRegistryService,
  ) { }

  service_id: any;
  dataSource: Array<ServiceRegistry>;
  checked = false;
  displayedColumns = ['service_id', 'hardware', 'actions'];
  code_provider_id = '';

  ngOnInit() {
    this.service_id = this.route.snapshot.paramMap.get('sid');
    this.code_provider_id = this.route.snapshot.paramMap.get('id');
    if (this.code_provider_id) {
      this.loadData();
    }
  }

  loadData = () => {
    this.loading.show();

    if (this.service_id) {

      this.loading.hide();
    } else {
      this.serviceRegistryService.serviceRegistryGet(`eq.${this.code_provider_id}`).subscribe(response => {
        this.dataSource = <Array<object>>response;
        this.checked = true;
        this.loading.hide();
      }, error => {
        this.loading.hide();
        this.message.error(error.message);
      });
    }
  }

}
