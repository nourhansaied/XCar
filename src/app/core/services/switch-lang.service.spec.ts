import { TestBed } from '@angular/core/testing';

import { SwitchLangService } from './switch-lang.service';

describe('SwitchLangService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SwitchLangService = TestBed.get(SwitchLangService);
    expect(service).toBeTruthy();
  });
});
