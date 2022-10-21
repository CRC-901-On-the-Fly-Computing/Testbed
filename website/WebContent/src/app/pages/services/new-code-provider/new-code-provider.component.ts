import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CodeProviderRegistryService } from '../../../generated/api/code-provider/api/codeProviderRegistry.service';
import { CodeProviderRegistry } from '../../../generated/api/code-provider/model/codeProviderRegistry';
import { LoadingService } from '../../../services/loading/loading.service';

@Component({
  selector: 'app-new-code-provider',
  templateUrl: './new-code-provider.component.html',
  styleUrls: ['./new-code-provider.component.sass']
})

export class NewCodeProviderComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private loading: LoadingService,
    private codeProviderRegistryService: CodeProviderRegistryService,
  ) { }

  dataSource = <CodeProviderRegistry>{
    codeProvider_id: '',
    repository_url: '',
    subfolder: '',
    tag: '',
  };

  checked = false;
  editMode = false;
  code_provider_id = '';

  ngOnInit() {
    this.code_provider_id = this.route.snapshot.paramMap.get('id');
    if (this.code_provider_id) {
      this.editMode = true;
      this.loadData();
    }
  }

  loadData = () => {
    this.loading.show();
    this.codeProviderRegistryService.codeProviderRegistryGet(`eq.${this.code_provider_id}`).subscribe(response => {
      this.dataSource = response[0];
      this.checked = true;
      this.loading.hide();
    }, error => {
      this.loading.hide();
    });
  }
}
