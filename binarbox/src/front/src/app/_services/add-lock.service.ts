import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AddLockService {

  private lockTypesUrl     = 'lock/type';

  constructor(
    private http: HttpClient,
    private router: Router
  ) { }

  getLockTypes() {
      return this.http.get(this.lockTypesUrl, {});
  }
}