import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-configure-market-variant',
  templateUrl: './configure-market-variant.component.html',
  styleUrls: ['./configure-market-variant.component.sass']
})

export class ConfigureMarketVariantComponent implements OnInit {
  public theBoundCallback: Function;

  requirementSpecifications = [{ name: 'simple' }, { name: 'extended' }];
  matcher = [{ name: 'completeFuzzy' }, { name: 'notSoFuzzy' }];
  serviceComposition = [{ name: 'SpecialConfigurationStrategy' }, { name: 'NotSoSpecialConfigurationStrategy' }];
  authenticationFile: any;
  constructor() { }

  ngOnInit() {
    this.theBoundCallback = this.sampleCallBack.bind(this);
  }

  sampleCallBack() {
    // here you can access the file with this.authenticationFile
  }

}
