import { Component, OnInit } from '@angular/core';
import { UserApiService } from '../../generated/api/service-requester/api/userApi.service';
import { LoadingService } from '../../services/loading/loading.service';
import { MessageService } from '../../services/messages/messages.service';
import { DialogService } from '../../services/dialog/dialog.service';

@Component({
  selector: 'app-my-services',
  templateUrl: './my-services.component.html',
  styleUrls: ['./my-services.component.sass']
})

export class MyServicesComponent implements OnInit {
  constructor(
    private loading: LoadingService,
    private message: MessageService,
    private dialog: DialogService,
    private userApiService: UserApiService,
  ) { }

  requests: Array<object>;
  checked: boolean;
  displayedColumns = ['name', 'composition', 'price', 'status', 'actions'];

  ngOnInit() {
    this.loadData();
  }

  loadData = () => {
    this.loading.show();
    this.requests = [];
    this.userApiService.getAllItems().subscribe(response => {
      this.requests = response;
      this.checked = true;
      this.loading.hide();
    }, error => {
      this.loading.hide();
      this.message.error(error.message);
    });
  }

  openDialog = (json) => {
    this.dialog.open(json);
  }
}
