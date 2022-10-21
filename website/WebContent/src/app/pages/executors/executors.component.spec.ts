import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExecutorsComponent } from './executors.component';

describe('ExecutorsComponent', () => {
  let component: ExecutorsComponent;
  let fixture: ComponentFixture<ExecutorsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExecutorsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExecutorsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
