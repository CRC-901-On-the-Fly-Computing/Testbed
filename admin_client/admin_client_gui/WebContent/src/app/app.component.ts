import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent {
  title = 'Admin Client';

  notificationsOptions = {
    timeOut: 10000,
    showProgressBar: true,
    pauseOnHover: true,
    animate: 'fromBottom'
  };

}
