import {Component, OnInit} from '@angular/core';
import {UserService} from '../../_services/user.service';
import {Router} from '@angular/router';
import {SocialAuthService} from 'angularx-social-login';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

    constructor(
        private userService: UserService,
        private authService: SocialAuthService
    ) {
    }

    ngOnInit() {
    }

    public socialLogin(provider) {
        this.userService.loginSocial(provider);
    }

}
