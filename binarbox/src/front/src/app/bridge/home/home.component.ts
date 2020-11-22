import {Component, OnInit} from '@angular/core';
import {DeviceDetectorService} from 'ngx-device-detector';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

    public collapseMenu = false;
    private deviceInfo;
    public showMobileVideo: boolean;


    constructor(
        private deviceService: DeviceDetectorService,
        private domSanitizer: DomSanitizer
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

    getVideoTag(url) {
        return this.domSanitizer.bypassSecurityTrustHtml(
            `<video class="video-bg" autoplay loop muted playsinline>
                        <source src="${url}" type="video/mp4">No HTML5 supported.</source>
                    </video>`
        );
    }
}
