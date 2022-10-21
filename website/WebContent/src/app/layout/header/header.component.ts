import { Component } from '@angular/core';
import { IntroService } from '../../services/intro/intro.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.sass']
})
export class HeaderComponent {
  constructor(private intro: IntroService, private router: Router) { }

  showIntro() {
    this.intro.showStatus();
    window.location.href = '/';
  }

}
