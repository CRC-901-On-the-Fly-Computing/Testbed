import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RolesDropdownComponent } from './roles-dropdown.component';

describe('RolesDropdownComponent', () => {
  let component: RolesDropdownComponent;
  let fixture: ComponentFixture<RolesDropdownComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RolesDropdownComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RolesDropdownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
