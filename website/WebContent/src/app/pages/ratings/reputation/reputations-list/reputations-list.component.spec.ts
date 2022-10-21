import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReputationsListComponent } from './reputations-list.component';

describe('ReputationsListComponent', () => {
  let component: ReputationsListComponent;
  let fixture: ComponentFixture<ReputationsListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReputationsListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReputationsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
