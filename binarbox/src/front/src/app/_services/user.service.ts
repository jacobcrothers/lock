import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class UserService {
    private authenticateUrl = 'authentication/login';
    private registrationUrl = 'authentication/register';

    constructor(private http: HttpClient) {
    }

    static isLoggedIn() {
        const token = localStorage.getItem('token');

        return Boolean(token);
    }

    static setUserToken(value) {
        try {
            localStorage.setItem('token', value);
        } catch (e) {
            return false;
        }

        return true;
    }

    static getUserToken() {
        const token = localStorage.getItem('token');
        return token ? token : '';
    }

    static logout () {
        localStorage.removeItem('token');
    }

    login(data) {
        return this.http.post(this.authenticateUrl, data, {
            responseType: 'json'
        });
    }

    register(data) {
        return this.http.post(this.registrationUrl, data, {
            responseType: 'json'
        });
    }
}
