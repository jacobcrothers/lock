import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {PrivacyRoutingModule} from './privacy-routing.module';

import {ContactModule} from '../contact/contact.module';

import {CookiesPolicyComponent} from './_components/cookies-policy/cookies-policy.component';
import {PrivacyPolicyComponent} from './_components/privacy-policy/privacy-policy.component';

@NgModule({
    imports: [
        CommonModule,
        PrivacyRoutingModule,
        ContactModule
    ],
    declarations: [
        CookiesPolicyComponent,
        PrivacyPolicyComponent
    ]
})
export class PrivacyModule {
}
