import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';

import {AuthGuard} from '../_guards/auth.guard';
import {AddLockComponent} from './_components/add-lock/add-lock.component';
import {SeeAllLocksComponent} from './_components/see-all-locks/see-all-locks.component';

const routes = [
    {
        path: '',
        component: SeeAllLocksComponent
    }, {
        path: 'add-lock',
        component: AddLockComponent,
        canActivate: [AuthGuard]
    }, {
        path: 'add-lock/:type',
        component: AddLockComponent,
        canActivate: [AuthGuard],
    }, {
        path: 'add-lock/:type/:id',
        component: AddLockComponent,
        canActivate: [AuthGuard]
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
export class LockRoutingModule {
}
