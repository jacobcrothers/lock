import {Component, OnInit} from '@angular/core';
import {DeviceDetectorService} from 'ngx-device-detector';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

    public collapseMenu = false;
    private deviceInfo;
    public showMobileVideo: boolean;

    constructor(private deviceService: DeviceDetectorService
    ) {
    }

    ngOnInit() {
        this.isMobilePlatform();
    }

    // move this to reusable component
    isMobilePlatform() {
        this.deviceInfo = this.deviceService.getDeviceInfo();
        this.showMobileVideo = this.deviceService.isMobile() || this.deviceService.isTablet();
    }

}
