import {Component, OnInit} from '@angular/core';
import {UserService} from './_services/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
    public collapseMenu = false;
    public isLoggedIn = false;

    constructor() {
    }

    ngOnInit() {
        this.isLoggedIn = UserService.isLoggedIn();
    }
}
