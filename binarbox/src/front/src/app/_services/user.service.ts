import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class UserService {
    private authenticateUrl = 'authentication/login';
    private registrationUrl = 'authentication/register';

    constructor(private http: HttpClient) {
    }

    public isLoggedIn() {
        return true;
    }

    setUserToken(value) {
        try {
            localStorage.setItem('token', value);
        } catch (e) {
            return false;
        }

        return true;
    }

    getUserToken() {
        const token = localStorage.getItem('token');
        return token ? token : '';
    }

    login(data) {
        return this.http.post(this.authenticateUrl, data, {
            responseType: 'json'
        });
    }

    register(data) {
        console.log('inside');
        return this.http.post(this.registrationUrl, data, {
            responseType: 'json'
        });
    }
}
