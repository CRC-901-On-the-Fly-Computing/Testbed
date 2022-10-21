import { Component, OnInit } from '@angular/core';
import { ActivatedRoute} from '@angular/router';
import { ServiceRegistryService } from '../../../generated/api/code-provider/api/serviceRegistry.service';
import { ServiceRegistry } from '../../../generated/api/code-provider/model/serviceRegistry';
import { LoadingService } from '../../../services/loading/loading.service';

@Component({
  selector: 'app-upload-service',
  templateUrl: './upload-service.component.html',
  styleUrls: ['./upload-service.component.sass']
})

export class UploadServiceComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private loading: LoadingService,
    private serviceRegistryService: ServiceRegistryService,
  ) { }

  dataSource = <ServiceRegistry>{
    codeProvider_id: '',
    repository_url: '',
    subfolder: '',
    tag: '',
  };
  code_provider_id = '';
  service_id = '';
  checked = false;
  editMode = false;

  ngOnInit() {
    this.code_provider_id = this.route.snapshot.paramMap.get('id');
    this.service_id = this.route.snapshot.paramMap.get('oid');

    if (this.code_provider_id && this.service_id) {
      this.loadData();
      this.editMode = true;
    }
  }

  loadData = () => {
    this.loading.show();
    this.serviceRegistryService.serviceRegistryGet(`eq.${this.code_provider_id}`, `eq.${this.service_id}`).subscribe(response => {
      this.dataSource = response[0];
      this.checked = true;
      this.loading.hide();
    }, error => {
      this.checked = true;
      this.loading.hide();
    });
  }
}
