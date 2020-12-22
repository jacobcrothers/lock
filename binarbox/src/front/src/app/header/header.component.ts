import {Component, DoCheck, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {UserService} from '../_services/user.service';
import {Router} from '@angular/router';
import {DeviceDetectorService} from 'ngx-device-detector';

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
    public collapseMenu = false;
    public loggedIn: any = false;
    public isMobilePlatform: boolean;
    @ViewChild('closeModal') closeModal: ElementRef;

    constructor(
        private userService: UserService,
        private router: Router,
        private deviceService: DeviceDetectorService
    ) { }

    ngOnInit() {
        this.userService.isUserLoggedIn$.subscribe((loggedIn) => {
            this.loggedIn = loggedIn;
            if (this.loggedIn) {
                this.closeModal?.nativeElement?.click();
            }
        });
        this.isMobilePlatform = this.deviceService.isTablet() || this.deviceService.isMobile();
    }

    public logout() {
        localStorage.removeItem('token');
        this.router.navigate([`/`]).then();
        this.loggedIn = false;
    }
}
