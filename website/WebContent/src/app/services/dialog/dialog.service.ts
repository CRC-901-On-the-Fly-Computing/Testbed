import { Injectable } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import hljs from 'highlight.js/lib/highlight';
import javascript from 'highlight.js/lib/languages/javascript';
import { DialogComponent } from '../../components/dialog/dialog.component';

@Injectable()
export class DialogService {

  constructor(public dialog: MatDialog) { }

  dialogRef: any;

  open(data: any, format = 'json'): void {
    let content = data;
    if (format === 'json') {
      hljs.registerLanguage('javascript', javascript);
      const strData = JSON.stringify(data, null, 5);
      content = hljs.highlight('javascript', strData);
      content = content.value;
    }

    this.dialogRef = this.dialog.open(DialogComponent, {
      width: '80%',
      data: { content, format },
    });

    this.dialogRef.afterClosed().subscribe(result => {
    });
  }

  close(): void {
    this.dialogRef.close();
  }
}
