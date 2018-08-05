import { TestBed, inject } from '@angular/core/testing';

import { AddLockService } from './add-lock.service';

describe('AddLockService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AddLockService]
    });
  });

  it('should be created', inject([AddLockService], (service: AddLockService) => {
    expect(service).toBeTruthy();
  }));
});
