import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Globals } from '../../globals';
import * as sidebar from '../../layout/sidebar/sidebar-items';
import * as role from './role-items';

@Component({
  selector: 'app-roles-dropdown',
  templateUrl: './roles-dropdown.component.html',
  styleUrls: ['./roles-dropdown.component.sass']
})

export class RolesDropdownComponent {

  currentPage = '';
  items = role.items;
  selectedItem = this.globals.role || '';

  constructor(
    private globals: Globals,
    private router: Router
  ) {

    // finding the current page and choose it from the list
    this.currentPage = window.location.pathname.split('/')[1];
    if (this.currentPage) {
      const filteredItem = sidebar.items.find(x => x.link === this.currentPage);
      if (filteredItem && filteredItem.role) {
        this.selectedItem = filteredItem.role;
        this.globals.role = this.selectedItem;
      }
    }

  }

  onChange() {
    // set selected item as active role, so whole program can access it
    this.globals.role = this.selectedItem;
    const selectedRole = this.items.find(x => x.value === this.selectedItem);
    if (selectedRole && selectedRole.default) {
      this.router.navigate([`/${selectedRole.default}`]);
    }
  }
}
