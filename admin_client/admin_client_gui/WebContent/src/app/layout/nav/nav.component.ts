import { Component } from '@angular/core';
import * as nav from './nav.items';


@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.sass']
})
export class NavComponent {
  items = nav.items;
}
