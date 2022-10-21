import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Injectable()
export class IntroService {

  constructor() { }

  private items: any;
  private show = new BehaviorSubject<boolean>(false);
  private data = new BehaviorSubject<Object>('');
  private index = 0;

  getShow = this.show.asObservable();
  getData = this.data.asObservable();

  public init(items: any) {
    this.items = items;
  }

  public start() {
    if (localStorage.getItem('intro') === null) {
      this.show.next(true);
      this.index = 0;

      this.data.next(this.calData());
      return 'Intro started!';
    }
  }

  public goNext() {
    this.index++;
    this.data.next(this.calData());
  }

  public goPrev() {
    this.index--;
    this.data.next(this.calData());
  }

  public getItems() {
    return this.items;
  }

  public close() {
    this.hideStatus();
    this.show.next(false);
  }

  public hideStatus() {
    localStorage.setItem('intro', '0');
  }

  public showStatus() {
    localStorage.removeItem('intro');
  }

  calData() {
    let el = null;
    for (let i = 0; i < this.items.length; i++) {
      el = this.getEl(i);
      if (el !== null && el !== undefined && el.style !== undefined) {
        el.style.removeProperty('z-index');
        el.style.removeProperty('position');
      }
    }

    el = this.getEl(this.index);
    if (el === null) {
      return false;
    }

    el.style.setProperty('z-index', '9999999', 'important');
    el.style.setProperty('position', 'relative', 'important');

    const data = {
      hasNext: true,
      hasPrev: false,
      text: this.items[this.index].text,
      style: {
        left: window.scrollX + el.getBoundingClientRect().left + 'px',
        top: window.scrollY + el.getBoundingClientRect().top + 'px',
        width: el.getBoundingClientRect().width + 'px',
        height: el.getBoundingClientRect().height + 'px',
      }
    };

    if (this.items[this.index + 1] === undefined) {
      data.hasNext = false;
    }

    if (this.index > 0 && this.items[this.index - 1] !== undefined) {
      data.hasPrev = true;
    }

    return data;
  }

  refresh() {
    this.data.next(this.calData());
  }

  getEl(index: number) {
    const nodes = document.querySelectorAll(this.items[index].element);
    return nodes[nodes.length - 1];
  }
}
