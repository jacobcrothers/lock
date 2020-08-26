import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {PrivacyRoutingModule} from './privacy-routing.module';

import {CookiesPolicyComponent} from './_components/cookies-policy/cookies-policy.component';
import {PrivacyPolicyComponent} from './_components/privacy-policy/privacy-policy.component';
import { ContactDataComponent } from './_components/_common/contact-data/contact-data.component';

@NgModule({
    imports: [
        CommonModule,
        PrivacyRoutingModule
    ],
    declarations: [
        CookiesPolicyComponent,
        PrivacyPolicyComponent,
        ContactDataComponent
    ]
})
export class PrivacyModule {
}
