import { Component, OnInit } from '@angular/core';
import { IntroService } from './services/intro/intro.service';
import APP_INTRO from './app.intro.json';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  constructor(private intro: IntroService) { }

  ngOnInit() {
    this.intro.init(APP_INTRO);
  }

}
