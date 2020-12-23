import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {AppRoutingModule} from './app-routing.module';
import {AuthGuard} from './_guards/auth.guard';
import {UserService} from './_services/user.service';
import {AddLockService} from './_services/add-lock.service';
import {RequestInterceptor} from './_interceptors/requestInterceptor';
import {AppComponent} from './app.component';
import {HomeComponent} from './bridge/home/home.component';
import {LoginComponent} from './user/login/login.component';
import {DashboardComponent} from './user/dashboard/dashboard.component';
import {IconsModule} from './_icons/icons.module';
import {PaymentComponent} from './user/dashboard/payment/payment.component';
import {LocksComponent} from './user/dashboard/locks/locks.component';
import {SocialComponent} from './user/dashboard/social/social.component';

import {FacebookLoginProvider, SocialAuthServiceConfig, SocialLoginModule} from 'angularx-social-login';
import {MessageComponent} from './message/message.component';
import {MessageService} from './_services/message.service';
import {LockDetailsComponent} from './user/dashboard/locks/lock-details/lock-details.component';
import {PanelsComponent} from './bridge/panels/panels.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {PaymentService} from './_services/payment.service';
import {SafeHtmlPipe} from './_pipes/safe-html.pipe';
import {DeviceDetectorModule} from 'ngx-device-detector';
import {VideoPlayerComponent} from './video-player/video-player.component';

import {HeaderComponent} from './header/header.component';
import {FooterComponent} from './footer/footer.component';
import {VgCoreModule} from '@videogular/ngx-videogular/core';
import {VgStreamingModule} from '@videogular/ngx-videogular/streaming';

@NgModule({
    declarations: [
        AppComponent,
        LoginComponent,
        HomeComponent,
        DashboardComponent,
        PaymentComponent,
        LocksComponent,
        SocialComponent,
        MessageComponent,
        LockDetailsComponent,
        PanelsComponent,
        SafeHtmlPipe,
        VideoPlayerComponent,
        HeaderComponent,
        FooterComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpClientModule,
        AppRoutingModule,
        IconsModule,
        SocialLoginModule,
        NgbModule,
        BrowserModule,
        DeviceDetectorModule.forRoot(),
        VgCoreModule,
        VgStreamingModule
    ],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: RequestInterceptor,
            multi: true
        },     {
            provide: 'SocialAuthServiceConfig',
            useValue: {
                autoLogin: true,
                providers: [
                    {
                        id: FacebookLoginProvider.PROVIDER_ID,
                        provider: new FacebookLoginProvider('227796454748370')
                    }
                ]
            } as SocialAuthServiceConfig,
        },
        AuthGuard,
        UserService,
        AddLockService,
        MessageService,
        PaymentService
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
