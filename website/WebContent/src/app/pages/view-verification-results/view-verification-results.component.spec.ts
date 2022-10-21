import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewVerificationResultsComponent } from './view-verification-results.component';

describe('ViewVerificationResultsComponent', () => {
  let component: ViewVerificationResultsComponent;
  let fixture: ComponentFixture<ViewVerificationResultsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ViewVerificationResultsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewVerificationResultsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
