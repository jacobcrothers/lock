import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {UserService} from '../_services/user.service';

@Injectable()
export class AuthGuard implements CanActivate {

    constructor(
        private router: Router) {
    }

    canActivate() {
        if (UserService.isLoggedIn()) {
            // logged in so return true
            return true;
        }

        // Redirect the user to the login page if not logged in
        this.router.navigate(['login']);
        return false;
    }
}
