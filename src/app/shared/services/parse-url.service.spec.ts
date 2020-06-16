import { TestBed } from '@angular/core/testing';

import { ParseUrlService } from './parse-url.service';

describe('ParseUrlService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ParseUrlService = TestBed.get(ParseUrlService);
    expect(service).toBeTruthy();
  });
});
