import {Component, OnInit} from '@angular/core';
import {UserService} from '../../_services/user.service';
import {Router} from '@angular/router';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

    constructor (
        private user: UserService,
        private router: Router) {
    }

    ngOnInit() {
    }

    public register(formValue) {
        this.user.register(formValue).subscribe(data => {
            if (data) {
                if (data['token']) {
                    UserService.setUserToken(data['token']);
                    this.router.navigate(['/']);
                }
            }
        });
    }
}
