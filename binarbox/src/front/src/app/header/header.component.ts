import {Component, DoCheck, ElementRef, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild} from '@angular/core';
import {UserService} from '../_services/user.service';
import {Router} from '@angular/router';
import {DeviceDetectorService} from 'ngx-device-detector';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

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
        private deviceService: DeviceDetectorService,
        private modalService: NgbModal
    ) { }

    ngOnInit() {
        this.userService.isUserLoggedIn$.subscribe((loggedIn) => {
            this.loggedIn = loggedIn;
            if (this.loggedIn) {
                this.modalService.dismissAll();
            }
        });
        this.isMobilePlatform = this.deviceService.isTablet() || this.deviceService.isMobile();
    }

    public logout() {
        localStorage.removeItem('token');
        localStorage.removeItem('lockInfo');
        this.router.navigate([`/`]).then();
        this.loggedIn = false;
    }

    open(content: TemplateRef<any>) {

        this.modalService.open(content, {centered: true}).result.then((result) => {}, (reason) => {});

    }
}
