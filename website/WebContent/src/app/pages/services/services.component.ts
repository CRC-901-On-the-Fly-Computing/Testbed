import { Component, OnInit } from '@angular/core';
import { LoadingService } from '../../services/loading/loading.service';
import { MessageService } from '../../services/messages/messages.service';
import { CodeProviderRegistryService } from '../../generated/api/code-provider/api/codeProviderRegistry.service';
import { CodeProviderRegistry } from '../../generated/api/code-provider/model/codeProviderRegistry';

@Component({
  selector: 'app-services',
  templateUrl: './services.component.html',
  styleUrls: ['./services.component.sass']
})
export class ServicesComponent implements OnInit {
  constructor(
    private loading: LoadingService,
    private message: MessageService,
    private codeProviderRegistryService: CodeProviderRegistryService,
  ) { }

  dataSource: Array<CodeProviderRegistry>;
  checked: boolean;
  displayedColumns = ['code_provider_id', 'repository_url', 'subfolder', 'tag', 'actions'];

  ngOnInit() {
    this.loadData();
  }

  loadData = () => {
    this.loading.show();
    this.dataSource = [];
    this.codeProviderRegistryService.codeProviderRegistryGet().subscribe(response => {
      this.dataSource = <Array<object>> response;
      this.checked = true;
      this.loading.hide();
    }, error => {
      this.loading.hide();
      this.message.error(error.message);
    });
  }

}
