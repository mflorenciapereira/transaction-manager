import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TransactionListComponent } from './transaction-list.component';
import { TransactionService } from '../service/transaction.service';
import { HttpClientModule } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transaction } from '../model/transaction';


describe('TransactionListComponent', () => {
  let component: TransactionListComponent;
  let fixture: ComponentFixture<TransactionListComponent>;
  const mockTransactionService = jasmine.createSpyObj('transactionService', ['findAll']);
  let now = new Date();
  mockTransactionService.findAll.and.returnValue(Observable.of([
    { id: 'ID1', type: 'credit', amount:1000.0, effectiveDate: now, showing: false }      
  ]));


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TransactionListComponent ],
      providers: [{provide: TransactionService, useValue: mockTransactionService}],
      imports: [HttpClientModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TransactionListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create and fetch transactions', () => {
    expect(component).toBeTruthy();
    expect(mockTransactionService.findAll).toHaveBeenCalled(); 
    let transaction: Transaction;
    transaction = component.transactions[0];
    expect(transaction.id).toEqual('ID1');
    expect(transaction.type).toEqual('credit');
    expect(transaction.amount).toEqual(1000.0);
    expect(transaction.effectiveDate).toEqual(now);
    expect(transaction.showing).toEqual(false);  
  });

  it('should toggle transaction 0 showing flag', () => {
    expect(component).toBeTruthy();
    expect(mockTransactionService.findAll).toHaveBeenCalled(); 
    component.toggleShowDetails(0);
    expect(component.transactions[0].showing).toEqual(true);
  });

  it('should render a table', async(() => {
    expect(component).toBeTruthy();    
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('table').textContent).toContain('ID1');
  }));


});
