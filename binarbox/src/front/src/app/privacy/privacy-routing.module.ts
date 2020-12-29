import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';

import {PrivacyPolicyComponent} from './_components/privacy-policy/privacy-policy.component';
import {CookiesPolicyComponent} from './_components/cookies-policy/cookies-policy.component';

const routes = [
    {
        path: 'privacy-policy',
        component: PrivacyPolicyComponent
    }, {
        path: 'cookies-policy',
        component: CookiesPolicyComponent
    }
];

@NgModule({
    imports: [
        CommonModule,
        RouterModule.forChild(routes)
    ],
    exports: [RouterModule],
    declarations: []
})
export class PrivacyRoutingModule {
}
