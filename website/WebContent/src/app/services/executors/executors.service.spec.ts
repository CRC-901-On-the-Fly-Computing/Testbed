import { TestBed, inject } from '@angular/core/testing';

import { ExecutorsService } from './executors.service';

describe('ExecutorsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ExecutorsService]
    });
  });

  it('should be created', inject([ExecutorsService], (service: ExecutorsService) => {
    expect(service).toBeTruthy();
  }));
});
