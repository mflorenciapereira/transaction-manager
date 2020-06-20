import { Component, OnInit } from '@angular/core';
import { TransactionService } from '../service/transaction.service';
import { Transaction } from '../model/transaction';

@Component({
  selector: 'app-transaction-list',
  templateUrl: './transaction-list.component.html',
  styleUrls: ['./transaction-list.component.css']
})
export class TransactionListComponent implements OnInit {

  transactions: Transaction[];

  constructor(private transactionService: TransactionService) { }

  ngOnInit() {
    this.transactionService.findAll().subscribe(result => {
      this.transactions = result;
    })
  }

  toggleShowDetails(i: number){
    this.transactions[i].showing=!this.transactions[i].showing;
  }

}
