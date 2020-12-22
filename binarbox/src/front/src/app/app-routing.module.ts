import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {AuthGuard} from './_guards/auth.guard';
import {HomeComponent} from './bridge/home/home.component';
import {DashboardComponent} from './user/dashboard/dashboard.component';
import {PaymentComponent} from './user/dashboard/payment/payment.component';
import {LocksComponent} from './user/dashboard/locks/locks.component';
import {SocialComponent} from './user/dashboard/social/social.component';
import {PanelsComponent} from './bridge/panels/panels.component';
import {VideoPlayerComponent} from './video-player/video-player.component';

const routes = [
    {
        path: '',
        component: HomeComponent
    }, {
        path: 'panels',
        component: PanelsComponent
    }, {
        path: 'video',
        component: VideoPlayerComponent
    }, {
        path: 'contact', loadChildren: () => import('./contact/contact.module').then(m => m.ContactModule)
    }, {
        path: 'locks', loadChildren: () => import('./lock/lock.module').then(m => m.LockModule)
    }, {
        path: 'privacy', loadChildren: () => import('./privacy/privacy.module').then(m => m.PrivacyModule)
    }, {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [AuthGuard],
        children: [
            {
                path: '',
                pathMatch: 'full',
                redirectTo: 'payment'
            }, {
                path: 'payment',
                component: PaymentComponent
            }, {
                path: 'locks',
                component: LocksComponent
            }, {
                path: 'video',
                component: VideoPlayerComponent
            }, {
                path: 'social',
                component: SocialComponent
            }
        ]
    }];

@NgModule({
    imports: [
        CommonModule,
        RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })
    ],
    exports: [RouterModule],
    declarations: []
})

export class AppRoutingModule {
}
