import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {LoginComponent} from './user/login/login.component';
import {AuthGuard} from './_guards/auth.guard';
import {HomeComponent} from './bridge/home/home.component';
import {RegisterComponent} from './user/register/register.component';

const routes = [{
        path: '',
        component: HomeComponent
    }, {
        path: 'login',
        component: LoginComponent
    }, {
        path: 'register',
        component: RegisterComponent
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
