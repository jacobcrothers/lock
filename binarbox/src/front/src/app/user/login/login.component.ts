import {Component, OnInit} from '@angular/core';
import {UserService} from '../../_services/user.service';
import {Router} from '@angular/router';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

    constructor(
        private userService: UserService
    ) {
    }

    ngOnInit() {
    }

    public socialLogin(provider) {
        this.userService.loginSocial(provider);
    }

}
