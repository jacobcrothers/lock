import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {CookiePolicyComponent} from "./components/cookie-policy/cookie-policy.component";
import {PrivacyPolicyComponent} from "./components/privacy-policy/privacy-policy.component";

@Component({
    selector: 'app-alert-dialog',
    templateUrl: './alert-dialog.component.html',
    styleUrls: ['./alert-dialog.component.scss']
})
export class AlertDialogComponent implements OnInit {


    @ViewChild('closeModal') closeModal: ElementRef;
    private component: CookiePolicyComponent;

    constructor(type: String) {
        switch (type) {
            case "COOKIE_POLICY":
                this.component = new CookiePolicyComponent();
                break;
            case "PRIVACY_POLICY":
                this.component = new PrivacyPolicyComponent();
                break;
            case "SOCIAL_LOGIN":
                this.component = new CookiePolicyComponent();
                break;
            default:
                this.component = null;

        }
    }

    ngOnInit() {
    }

    ngDoCheck() {
    }

}
