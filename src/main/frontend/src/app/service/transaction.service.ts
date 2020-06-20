import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient } from '@angular/common/http';
import {Transaction} from '../model/Transaction'

@Injectable()
export class TransactionService {

  private transactionUrl: string;

  constructor(private http: HttpClient) { 
    this.transactionUrl = "http://localhost:8080/api/transactions"
  }

  public findAll(): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(this.transactionUrl);
  }

}
