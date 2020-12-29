import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(
    private http: HttpClient
  ) { }

  chargeCard(token: string) {
    const headers = new HttpHeaders({'token': token, 'amount': '100'});
    return this.http.post('http://localhost:8080/payment/charge', {}, {headers: headers});
  }
}
