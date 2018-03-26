import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class UserService {
    private authenticateUrl = '/login';

    constructor(private http: HttpClient) {
    }

    public isLoggedIn() {
        return true;
    }

    authenticateUser(data) {
        return this.http.post(this.authenticateUrl, data, {
            responseType: 'json'
        });
    }
}
