import { TestBed } from '@angular/core/testing';

import { User } from './user';

describe('InsciptionService', () => {
  let service: User;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(User);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
