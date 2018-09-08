import {Component, OnInit, ElementRef, ViewChild, AfterViewInit} from '@angular/core';
import {UserService} from './_services/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
    public collapseMenu = false;
    public loggedIn: any = false;
    @ViewChild('loginModal') loginModal: ElementRef;

    constructor(
        private userService: UserService
    ) {
    }

    ngOnInit() {
        this.userService.isUserLoggedIn$.subscribe((loggedIn) => {
            this.loggedIn = loggedIn;    
            console.log('user logged in', this.loggedIn);
        });
    }

    // ngAfterViewInit() {
    //     console.log('modal', this.loginModal);
    //     if(this.loggedIn) {
    //         this.loginModal.dismiss();
    //     }
    // }
}
