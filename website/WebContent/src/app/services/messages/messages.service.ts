import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material';
import { MessagesComponent } from '../../components/messages/messages.component';

@Injectable()
export class MessageService {
  constructor(public snackBar: MatSnackBar) { }

  clear() {
    this.snackBar.dismiss();
  }

  open(message: string, duration, type = 'error') {
    const data = {
      message,
      closeCallBack: () => { this.snackBar.dismiss(); },
      type,
    };
    this.snackBar.openFromComponent(MessagesComponent, { data, duration });
  }

  error(message: string) {
    this.open(message, 12000, 'error');
  }

  success(message: string) {
    this.open(message, 7000, 'success');
  }

}
