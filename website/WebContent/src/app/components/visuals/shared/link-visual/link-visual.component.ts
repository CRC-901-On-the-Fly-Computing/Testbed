import { Component, Input } from '@angular/core';
import { Link } from '../../../../services/d3';

@Component({
  // tslint:disable-next-line:component-selector
  selector: '[linkVisual]',
  templateUrl: './link-visual.component.html',
  styleUrls: ['./link-visual.component.sass']
})

export class LinkVisualComponent  {
  // tslint:disable-next-line:no-input-rename
  @Input('linkVisual') link: Link;
}
