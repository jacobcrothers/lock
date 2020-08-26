import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {HttpModule} from '@angular/http';
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

import {AuthServiceConfig, FacebookLoginProvider, SocialLoginModule} from 'angular5-social-login';
import {MessageComponent} from './message/message.component';
import {MessageService} from './_services/message.service';
import {LockDetailsComponent} from './user/dashboard/locks/lock-details/lock-details.component';
import {PanelsComponent} from './bridge/panels/panels.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {DragScrollModule} from 'ngx-drag-scroll';
import {PaymentService} from './_services/payment.service';
import {SafeHtmlPipe} from './_pipes/safe-html.pipe';
import {DeviceDetectorModule} from 'ngx-device-detector';
import {VgBufferingModule} from 'videogular2/buffering';
import {VgControlsModule} from 'videogular2/controls';
import {VgOverlayPlayModule} from 'videogular2/overlay-play';
import {VideoPlayerComponent} from './video-player/video-player.component';
import {VgCoreModule} from 'videogular2/core';
import {VgStreamingModule} from 'videogular2/streaming';

import {HeaderComponent} from './header/header.component';
import {FooterComponent} from './footer/footer.component';

// Configs
export function getAuthServiceConfigs() {
    const config = new AuthServiceConfig(
        [{
            id: FacebookLoginProvider.PROVIDER_ID,
            provider: new FacebookLoginProvider('227796454748370')
        }]);
    return config;
}

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
        HttpModule,
        AppRoutingModule,
        IconsModule,
        SocialLoginModule,
        NgbModule,
        DragScrollModule,
        BrowserModule,
        VgCoreModule,
        VgControlsModule,
        VgOverlayPlayModule,
        VgBufferingModule,
        VgStreamingModule,
        DeviceDetectorModule.forRoot()
    ],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: RequestInterceptor,
            multi: true
        }, {
            provide: AuthServiceConfig,
            useFactory: getAuthServiceConfigs
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
