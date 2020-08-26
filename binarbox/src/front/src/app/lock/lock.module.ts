import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {LockRoutingModule} from './lock-routing.module';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

import {SeeAllLocksComponent} from './_components/see-all-locks/see-all-locks.component';
import {AddLockComponent} from './_components/add-lock/add-lock.component';
import {FormsModule} from '@angular/forms';

@NgModule({
    imports: [
        CommonModule,
        LockRoutingModule,

        NgbModule,
        FormsModule,
    ],
    declarations: [
        SeeAllLocksComponent,
        AddLockComponent
    ]
})
export class LockModule {
}
