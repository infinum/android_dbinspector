import { TestBed } from '@angular/core/testing';

import { PragmaService } from './pragma.service';

describe('PragmaService', () => {
  let service: PragmaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PragmaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
