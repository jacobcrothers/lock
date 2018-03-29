import {Component, OnInit} from '@angular/core';
import {UserService} from '../../_services/user.service';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

    constructor (private user: UserService) {
    }

    ngOnInit() {
    }

    public register(formValue) {
        this.user.register(formValue).subscribe(data => console.log(data));
    }
}
