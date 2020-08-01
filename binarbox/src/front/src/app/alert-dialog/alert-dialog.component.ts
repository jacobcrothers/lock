import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {CookiesPolicyComponent} from '../cookies-policy/cookies-policy.component';
import {PrivacyPolicyComponent} from '../privacy-policy/privacy-policy.component';

@Component({
    selector: 'app-alert-dialog',
    templateUrl: './alert-dialog.component.html',
    styleUrls: ['./alert-dialog.component.scss']
})
export class AlertDialogComponent implements OnInit {


    @ViewChild('closeModal') closeModal: ElementRef;
    private component: CookiesPolicyComponent;

    constructor(type: String) {
        switch (type) {
            case 'COOKIE_POLICY':
                this.component = new CookiesPolicyComponent();
                break;
            case 'PRIVACY_POLICY':
                this.component = new PrivacyPolicyComponent();
                break;
            case 'SOCIAL_LOGIN':
                this.component = new CookiesPolicyComponent();
                break;
            default:
                this.component = null;

        }
    }

    ngOnInit() {
    }
}
