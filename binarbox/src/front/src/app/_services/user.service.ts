import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class UserService {
    private baseUrl = 'http://localhost:6060/api/v0/';
    private authenticateUrl = this.baseUrl + 'authentication/login';

    constructor(private http: HttpClient) {
    }

    public isLoggedIn() {
        return true;
    }

    login (data) {
        return this.http.post(this.authenticateUrl, data, {
            responseType: 'json'
        });
    }
}
