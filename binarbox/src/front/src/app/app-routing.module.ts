import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {LoginComponent} from './user/login/login.component';
import {AuthGuard} from './_guards/auth.guard';
import {HomeComponent} from './bridge/home/home.component';
import {RegisterComponent} from './user/register/register.component';
import {DashboardComponent} from './user/dashboard/dashboard.component';
import {LogoutComponent} from './user/logout/logout.component';

const routes = [{
        path: '',
        component: HomeComponent
    }, {
        path: 'login',
        component: LoginComponent
    }, {
        path: 'logout',
        component: LogoutComponent
    }, {
        path: 'register',
        component: RegisterComponent
    }, {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [AuthGuard]
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
