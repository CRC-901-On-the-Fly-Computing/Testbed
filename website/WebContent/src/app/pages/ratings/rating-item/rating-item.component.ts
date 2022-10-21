import { Component, Input } from '@angular/core';
import { DialogService } from '../../../services/dialog/dialog.service';

@Component({
  selector: 'app-rating-item',
  templateUrl: './rating-item.component.html',
  styleUrls: ['./rating-item.component.sass']
})

export class RatingItemComponent {
  constructor(private dialog: DialogService) { }

  @Input() signature: any;
  @Input() data: any;
  @Input() count: any;
  @Input() title: any;
  @Input() content = '';
  @Input() single = false;

  openDialog = () => {
    this.dialog.open(this.signature);
  }

}
