import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
@Component({
  selector: 'app-verification-info',
  templateUrl: './verification-info.component.html',
  styleUrls: ['./verification-info.component.sass']
})
export class VerificationInfoComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
  ) { }

  id = '';

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
  }

}
