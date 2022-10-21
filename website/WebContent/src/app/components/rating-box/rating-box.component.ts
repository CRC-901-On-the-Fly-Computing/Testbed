import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-rating-box',
  templateUrl: './rating-box.component.html',
  styleUrls: ['./rating-box.component.sass']
})

export class RatingBoxComponent {

  @Input() rating = {};
  @Input() page = '';
  @Input() size = 'small';

}
