import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(
    private http: Http
  ) { }

  chargeCard(token: string) {
    const headers = new Headers({'token': token, 'amount': 100});
    return this.http.post('http://localhost:8080/payment/charge', {}, {headers: headers});
  }
}
