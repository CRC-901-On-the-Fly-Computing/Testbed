import { Component, OnInit } from '@angular/core';
import { ExecutorSpawnerService } from '../../generated';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-executors',
  templateUrl: './executors.component.html',
  styleUrls: ['./executors.component.sass']
})
export class ExecutorsComponent implements OnInit {

  constructor(private executorSpawner: ExecutorSpawnerService) { }

  displayedColumns = ['executorName', 'listOfServices'];
  dataSource: any;

  ngOnInit() {
    this.executorSpawner.getExecutors().subscribe(res => {
      this.dataSource = new MatTableDataSource(res);
    });
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

}
