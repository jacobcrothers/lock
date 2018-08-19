import {Injectable, OnInit} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {UserService} from '../_services/user.service';

@Injectable()
export class AuthGuard implements CanActivate, OnInit {

    private isLoggedIn: any = this.userService.isUserLoggedIn$;

    constructor(
        private router: Router,
        private userService: UserService) {
        this.userService.isUserLoggedIn$.subscribe((loggedIn) => {
            this.isLoggedIn = loggedIn;
        });
    }

    ngOnInit() {
    }

    canActivate() {
        if (this.isLoggedIn) {
            // logged in so return true
            return true;
        }

        // Redirect the user to the login page if not logged in
        this.router.navigate(['login']);
        return false;
    }
}
