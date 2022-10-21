import { Component, Input } from '@angular/core';
import { Node } from '../../../../services/d3';
import { DialogService } from '../../../../services/dialog/dialog.service';

@Component({
  // tslint:disable-next-line:component-selector
  selector: '[nodeVisual]',
  templateUrl: './node-visual.component.html',
  styleUrls: ['./node-visual.component.sass']
})

export class NodeVisualComponent {
  // tslint:disable-next-line:no-input-rename
  @Input('nodeVisual') node: Node;
  constructor(private dialog: DialogService, ) { }

  onClick = () => {
    if (this.node.configurationLabel) {
      this.dialog.open(this.node.configurationLabel, 'html');
    }
  }
}
