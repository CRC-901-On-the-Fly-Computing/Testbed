import { Component, OnInit } from '@angular/core';
// used in HTML as globals
import { Globals } from '../../globals';
import * as sidebar from './sidebar-items';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.sass']
})

export class SidebarComponent {
  constructor(
    private globals: Globals,
  ) { }

  items = sidebar.items;

}
