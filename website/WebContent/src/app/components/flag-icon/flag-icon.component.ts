import { Component, OnInit, Input } from '@angular/core';
import * as data from './data.json';

@Component({
  selector: 'app-flag-icon',
  templateUrl: './flag-icon.component.html',
  styleUrls: ['./flag-icon.component.sass']
})
export class FlagIconComponent implements OnInit {

  constructor() { }

  @Input() country = '';
  countryClass = '';

  ngOnInit() {
    if (this.country.length) {
      this.countryClass = data[this.country.toLowerCase()];
    }
  }
}
