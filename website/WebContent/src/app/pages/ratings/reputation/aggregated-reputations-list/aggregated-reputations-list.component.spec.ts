import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AggregatedReputationsListComponent } from './aggregated-reputations-list.component';

describe('AggregatedReputationsListComponent', () => {
  let component: AggregatedReputationsListComponent;
  let fixture: ComponentFixture<AggregatedReputationsListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AggregatedReputationsListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AggregatedReputationsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
