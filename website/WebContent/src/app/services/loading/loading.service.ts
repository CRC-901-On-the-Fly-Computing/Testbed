import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Injectable()
export class LoadingService {
  constructor() { }

  private showing = new BehaviorSubject<boolean>(false);
  getShowing = this.showing.asObservable();

  public show() {
    this.showing.next(true);
  }

  public hide() {
    this.showing.next(false);
  }

}
