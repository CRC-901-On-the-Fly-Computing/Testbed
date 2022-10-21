import { Component, EventEmitter, Input, Output, OnInit, ViewChild, ElementRef } from '@angular/core';

@Component({
  selector: 'app-star',
  templateUrl: './star.component.html',
  styleUrls: ['./star.component.sass']
})

export class StarComponent implements OnInit {
  @ViewChild('clickableStars') clickableStarsEl: ElementRef;

  @Input() count = 0;
  @Input() size = '';
  @Input() change = false;
  @Input() style = {};
  @Output() countChange = new EventEmitter<number>();

  // maximum number of star that component will draw
  countMax = 5;

  ngOnInit() {
    // making sure count at least is always zero
    this.count = this.count >= 0 ? this.count : 0;
  }

  // making an empty array for drawing stars with ngFor in size of i
  counter(i: number) {
    i = Math.ceil(i);
    if (i !== undefined && i > 0) {
      return Array.from({ length: i }, j => j);
    }
    return [];
  }

  onStarClick(i: any) {
    if (this.change) {
      const _i = parseInt(i, 0) + 1;
      this.count = _i !== this.count ? _i : 0;
      this.countChange.emit(this.count);
    }
  }

  onMouseOver(i: any) {
    for (let j = 0; j <= i; j++) {
      const el = this.clickableStarsEl.nativeElement.querySelector(`.star.click:nth-child(${j + 1})`);
      if (el) {
        el.classList.toggle('hover');
      }
    }
  }

  // calculate width of the box
  countWidth = () => Math.round(this.count * 100) / 100 * 20 + '%';

  // calculate left position
  calculateLeft = (i: number) => Math.round((100 / this.countMax) * i) + '%';

  // helper function to get keys
  objectKeys = arr => Object.keys(arr);

  // make the array
  makeEmptyArray = () => this.objectKeys(this.counter(this.countMax));
}
