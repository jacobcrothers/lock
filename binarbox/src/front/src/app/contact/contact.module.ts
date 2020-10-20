import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ContactRoutingModule} from './contact-routing.module';

import {ContactComponent} from './contact/contact.component';
import {ContactDataComponent} from './_common/contact-data/contact-data.component';

@NgModule({
    imports: [
        CommonModule,
        ContactRoutingModule
    ],
    exports: [
        ContactDataComponent
    ],
    declarations: [
        ContactComponent,
        ContactDataComponent
    ]
})
export class ContactModule {
}
