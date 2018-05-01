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
import {DashboardComponent} from './user/dashboard/dashboard.component';
import {LogoutComponent} from './user/logout/logout.component';
import {IconsModule} from './_icons/icons.module';
import {ProfileComponent} from './user/dashboard/profile/profile.component';
import {PaymentComponent} from './user/dashboard/payment/payment.component';
import {LocksComponent} from './user/dashboard/locks/locks.component';
import {SocialComponent} from './user/dashboard/social/social.component';
import {ForgotPasswordComponent} from './user/forgot-password/forgot-password.component';
import {
    SocialLoginModule,
    AuthServiceConfig,
    FacebookLoginProvider,
} from 'angular5-social-login';
import { ConfirmEmailComponent } from './user/confirm-email/confirm-email.component';


// Configs
export function getAuthServiceConfigs() {
    const config = new AuthServiceConfig(
        [{
                id: FacebookLoginProvider.PROVIDER_ID,
                provider: new FacebookLoginProvider('415210252282694')
            }
        ]);
    return config;
}

@NgModule({
    declarations: [
        AppComponent,
        LoginComponent,
        HomeComponent,
        RegisterComponent,
        DashboardComponent,
        LogoutComponent,
        ProfileComponent,
        PaymentComponent,
        LocksComponent,
        SocialComponent,
        ForgotPasswordComponent,
        ConfirmEmailComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpClientModule,
        AppRoutingModule,
        IconsModule,
        SocialLoginModule
    ],
    providers: [{
        provide: HTTP_INTERCEPTORS,
        useClass: RequestInterceptor,
        multi: true
    }, {
        provide: AuthServiceConfig,
        useFactory: getAuthServiceConfigs
    },
        AuthGuard,
        UserService
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
