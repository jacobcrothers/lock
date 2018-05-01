import {Component, OnInit} from '@angular/core';
import {UserService} from '../../_services/user.service';
import {Router} from '@angular/router';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

    constructor(
        private user: UserService
    ) {
    }

    ngOnInit() {
    }

    public login(formValue) {
        if (!formValue || !(formValue['email'] && formValue['password'])) {
            return;
        }

        this.user.login(formValue);
    }

    public socialLogin(provider) {
        this.user.loginSocial(provider);
    }

}
