import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCreatorComponent } from './user-creator.component';

describe('UserCreatorComponent', () => {
  let component: UserCreatorComponent;
  let fixture: ComponentFixture<UserCreatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserCreatorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
