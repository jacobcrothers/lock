import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';
import {AppRoutingModule} from './app-routing.module';

import {AuthGuard} from './_guards/auth.guard';
import {UserService} from './_services/user.service';
import {RequestInterceptor} from './_interceptors/requestInterceptor';

import {AppComponent} from './app.component';
import {HomeComponent} from './bridge/home/home.component';
import {LoginComponent} from './user/login/login.component';
import {RegisterComponent} from './user/register/register.component';
import { DashboardComponent } from './user/dashboard/dashboard.component';
import { LogoutComponent } from './user/logout/logout.component';

@NgModule({
    declarations: [
        AppComponent,
        LoginComponent,
        HomeComponent,
        RegisterComponent,
        DashboardComponent,
        LogoutComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpClientModule,
        AppRoutingModule
    ],
    providers: [
        AuthGuard,
        UserService,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: RequestInterceptor,
            multi: true
        }],
    bootstrap: [AppComponent]
})
export class AppModule {
}
