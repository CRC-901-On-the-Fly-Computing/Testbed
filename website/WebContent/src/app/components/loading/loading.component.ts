import { Component, OnInit } from '@angular/core';
import { LoadingService } from '../../services/loading/loading.service';

@Component({
  selector: 'app-loading',
  templateUrl: './loading.component.html',
  styleUrls: ['./loading.component.sass']
})
export class LoadingComponent implements OnInit {

  constructor(private loading: LoadingService) { }

  showing = false;

  ngOnInit() {
    this.loading.getShowing.subscribe(showing => this.showing = showing);
  }

}
