import { Component, OnInit, ViewChild, AfterViewChecked, ChangeDetectorRef } from '@angular/core';
import { IntroService } from '../../services/intro/intro.service';

@Component({
  selector: 'app-intro',
  templateUrl: './intro.component.html',
  styleUrls: ['./intro.component.sass'],
  // tslint:disable-next-line:use-host-property-decorator
  host: { '(window:resize)': 'onResize()' }
})
export class IntroComponent implements OnInit, AfterViewChecked {

  constructor(private intro: IntroService, private cdr: ChangeDetectorRef) { }
  @ViewChild('intro') introEl;

  show = false;
  introStyle = { top: '0px', left: '0px' };
  data: any;

  ngOnInit() {
    this.intro.getShow.subscribe(data => this.show = data);
    this.intro.getData.subscribe(data => this.data = data);
  }

  ngAfterViewChecked() {
    if (this.data && this.introEl) {
      const el = this.introEl.nativeElement;
      this.introStyle.top = parseInt(this.data.style.top, 0) - el.getBoundingClientRect().height - 10 + 'px';
      this.introStyle.left = this.data.style.left;
      this.cdr.detectChanges();
    }
  }

  goNext() {
    this.intro.goNext();
  }

  goPrev() {
    this.intro.goPrev();
  }

  close() {
    this.intro.close();
  }

  onResize() {
    this.intro.refresh();
  }

}
