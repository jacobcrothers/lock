import {Component, OnInit, ElementRef, ViewChild, DoCheck} from '@angular/core';
import {UserService} from './_services/user.service';
import {Router} from '@angular/router';
import {DeviceDetectorService} from "ngx-device-detector";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, DoCheck {
    public collapseMenu = false;
    public loggedIn: any = false;
    public isMobilePlatform: boolean;
    @ViewChild('closeModal') closeModal: ElementRef;

    constructor(
        private userService: UserService,
        private router: Router,
        private deviceService: DeviceDetectorService
    ) {
    }

    ngOnInit() {
        this.userService.isUserLoggedIn$.subscribe((loggedIn) => {
            this.loggedIn = loggedIn;
        });
        this.isMobilePlatform = this.deviceService.isTablet() || this.deviceService.isMobile();
    }

    ngDoCheck() {
        if (this.loggedIn) {
            this.closeModal.nativeElement.click();
        }
    }

    logout() {
        localStorage.removeItem('token');
        this.router.navigate([`/`]);
        this.loggedIn = false;
    }

}
