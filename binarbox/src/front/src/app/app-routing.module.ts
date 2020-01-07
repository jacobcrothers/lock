import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router'
import {AuthGuard} from './_guards/auth.guard';
import {HomeComponent} from './bridge/home/home.component';
import {DashboardComponent} from './user/dashboard/dashboard.component';
import {PaymentComponent} from './user/dashboard/payment/payment.component';
import {LocksComponent} from './user/dashboard/locks/locks.component';
import {SocialComponent} from './user/dashboard/social/social.component';
import {LockComponent} from './lock/lock.component';
import {PanelsComponent} from './bridge/panels/panels.component';
import {VideoPlayerComponent} from "./video-player/video-player.component";

const routes = [{
    path: '',
    component: HomeComponent
}, {
    path: 'add-lock',
    component: LockComponent,
    canActivate: [AuthGuard]
}, {
    path: 'add-lock/:type',
    component: LockComponent,
    canActivate: [AuthGuard],
}, {
    path: 'add-lock/:type/:id',
    component: LockComponent,
    canActivate: [AuthGuard]
}, {
    path: 'panels',
    component: PanelsComponent
}, {
    path: 'video',
    component: VideoPlayerComponent
}, {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard],
    children: [{
        path: '',
        pathMatch: 'full',
        redirectTo: 'payment'
    }, {
        path: 'payment',
        component: PaymentComponent
    },
        {
        path: 'locks',
        component: LocksComponent
        },
        {
            path: 'video',
            component: VideoPlayerComponent
        },
        {
            path: 'social',
            component: SocialComponent
        }]
}
];

@NgModule({
    imports: [
        CommonModule,
        RouterModule.forRoot(routes)
    ],
    exports: [RouterModule],
    declarations: []
})

export class AppRoutingModule {
}
