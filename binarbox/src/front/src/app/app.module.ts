import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {JwtModule} from '@auth0/angular-jwt';
import {AppRoutingModule} from './app-routing.module';

import {AppComponent} from './app.component';
import {LoginComponent} from './user/login/login.component';
import { HomeComponent } from './bridge/home/home.component';
import {AuthGuard} from './_guards/auth.guard';
import {UserService} from './_services/user.service';
import { RegisterComponent } from './user/register/register.component';

export function tokenGetter() {
    return localStorage.getItem('access_token');
}

@NgModule({
    declarations: [
        AppComponent,
        LoginComponent,
        HomeComponent,
        RegisterComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpClientModule,
        AppRoutingModule,
        JwtModule.forRoot({
            config: {
                tokenGetter: tokenGetter
            }
        })
    ],
    providers: [AuthGuard, UserService],
    bootstrap: [AppComponent]
})
export class AppModule {
}
