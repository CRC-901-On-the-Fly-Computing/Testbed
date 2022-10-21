import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VerificationInfoComponent } from './verification-info.component';

describe('VerificationInfoComponent', () => {
  let component: VerificationInfoComponent;
  let fixture: ComponentFixture<VerificationInfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VerificationInfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VerificationInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
