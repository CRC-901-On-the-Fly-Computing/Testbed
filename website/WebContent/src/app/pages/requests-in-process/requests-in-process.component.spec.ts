import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestsInProcessComponent } from './requests-in-process.component';

describe('RequestsInProcessComponent', () => {
  let component: RequestsInProcessComponent;
  let fixture: ComponentFixture<RequestsInProcessComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RequestsInProcessComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RequestsInProcessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
