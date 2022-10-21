import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewCodeProviderComponent } from './new-code-provider.component';

describe('NewCodeProviderComponent', () => {
  let component: NewCodeProviderComponent;
  let fixture: ComponentFixture<NewCodeProviderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewCodeProviderComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewCodeProviderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
