import {Component, OnInit} from '@angular/core';
import {UserService} from './_services/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
    public collapseMenu = false;
    public loggedIn: any = false;

    constructor(
        private user: UserService
    ) {
    }

    ngOnInit() {
        this.user.isUserLoggedIn$.subscribe((loggedIn) => {
            this.loggedIn = loggedIn;
        });
    }
}
