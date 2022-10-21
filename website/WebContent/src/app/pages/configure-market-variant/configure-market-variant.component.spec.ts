import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigureMarketVariantComponent } from './configure-market-variant.component';

describe('ConfigureMarketVariantComponent', () => {
  let component: ConfigureMarketVariantComponent;
  let fixture: ComponentFixture<ConfigureMarketVariantComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigureMarketVariantComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigureMarketVariantComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
