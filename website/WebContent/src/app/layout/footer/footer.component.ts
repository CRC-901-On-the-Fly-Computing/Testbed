import { Component } from '@angular/core';
import APP_URL from '../../app.url.json';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.sass']
})

export class FooterComponent {
  poc_url = APP_URL.POC_URL;
}
