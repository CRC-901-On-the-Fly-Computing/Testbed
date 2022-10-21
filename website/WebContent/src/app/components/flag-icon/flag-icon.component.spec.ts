import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FlagIconComponent } from './flag-icon.component';

describe('FlagIconComponent', () => {
  let component: FlagIconComponent;
  let fixture: ComponentFixture<FlagIconComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FlagIconComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FlagIconComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
