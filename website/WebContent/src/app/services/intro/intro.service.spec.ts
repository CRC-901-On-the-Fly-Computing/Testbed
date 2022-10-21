import { TestBed } from '@angular/core/testing';

import { IntroService } from './intro.service';

describe('IntroService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: IntroService = TestBed.get(IntroService);
    expect(service).toBeTruthy();
  });
});
