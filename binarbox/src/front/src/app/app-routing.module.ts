import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {LoginComponent} from './user/login/login.component';
import {AuthGuard} from './_guards/auth.guard';
import {HomeComponent} from './bridge/home/home.component';
import {RegisterComponent} from './user/register/register.component';
import {DashboardComponent} from './user/dashboard/dashboard.component';
import {LogoutComponent} from './user/logout/logout.component';
import {ProfileComponent} from './user/dashboard/profile/profile.component';
import {PaymentComponent} from './user/dashboard/payment/payment.component';
import {LocksComponent} from './user/dashboard/locks/locks.component';
import {SocialComponent} from './user/dashboard/social/social.component';
import {ForgotPasswordComponent} from './user/forgot-password/forgot-password.component';

const routes = [{
        path: '',
        component: HomeComponent
    }, {
        path: 'login',
        component: LoginComponent
    }, {
        path: 'forgot-password',
        component: ForgotPasswordComponent
    }, {
        path: 'logout',
        component: LogoutComponent
    }, {
        path: 'register',
        component: RegisterComponent
    }, {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [AuthGuard],
        children: [{
            path: '',
            pathMatch: 'full',
            redirectTo: 'profile'
        }, {
            path: 'profile',
            component: ProfileComponent
        }, {
            path: 'payment',
            component: PaymentComponent
        }, {
            path: 'locks',
            component: LocksComponent
        }, {
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
