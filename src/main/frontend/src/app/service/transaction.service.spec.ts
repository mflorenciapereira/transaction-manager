import { TestBed, inject } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';


import { TransactionService } from './transaction.service';

describe('TransactionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TransactionService],
      imports: [HttpClientModule]
    });
  });

  it('should be created', inject([TransactionService], (service: TransactionService) => {
    expect(service).toBeTruthy();
  }));
});
