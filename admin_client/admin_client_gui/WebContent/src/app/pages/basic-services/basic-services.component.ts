import { Component, OnInit } from '@angular/core';
import { NotificationsService } from 'angular2-notifications';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-basic-services',
  templateUrl: './basic-services.component.html',
  styleUrls: ['./basic-services.component.sass']
})
export class BasicServicesComponent implements OnInit {

  constructor(private notifications: NotificationsService) { }

  services = [
    {
      code_provider_id: 'catalano',
      repository_url: 'https://github.com/CRC-901-On-the-Fly-Computing/ServiceCodeProvider.git',
      subfolder: 'catalano',
      tag: 'develop'
    },
    {
      code_provider_id: 'weka',
      repository_url: 'https://github.com/CRC-901-On-the-Fly-Computing/ServiceCodeProvider.git',
      subfolder: 'weka.ml',
      tag: 'develop'
    },
    {
      code_provider_id: 'c2imaging',
      repository_url: 'https://github.com/CRC-901-On-the-Fly-Computing/ServiceCodeProvider.git',
      subfolder: 'CServices',
      tag: 'develop'
    }
  ];

  displayedColumns = ['code_provider_id', 'repository_url', 'subfolder', 'tag', 'actions'];
  dataSource = new MatTableDataSource(this.services);

  ngOnInit() {
    this.notifications.success('hello world!', 'this is a test');
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  basename(path: string) {
    return path.split(/[\\/]/).pop();
  }

}
