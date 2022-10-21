import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OtfProviderNetworkComponent } from './otf-provider-network.component';

describe('OtfProviderNetworkComponent', () => {
  let component: OtfProviderNetworkComponent;
  let fixture: ComponentFixture<OtfProviderNetworkComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OtfProviderNetworkComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OtfProviderNetworkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
