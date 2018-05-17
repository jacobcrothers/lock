import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Router} from '@angular/router';
// import {
//     AuthService,
//     FacebookLoginProvider,
//     GoogleLoginProvider
// } from 'angular5-social-login';

@Injectable()
export class UserService {

    private authenticateUrl = 'authentication/login';
    private registrationUrl = 'authentication/register';
    // private socialLoginUrl = 'authentication/social';
    private resetPasswordUrl = 'user/request/reset/password';
    private confirmEmailUrl = '/user/email/confirm';

    public isUserLoggedIn$ = new BehaviorSubject(Boolean(UserService.getUserToken()));

    constructor(
        private http: HttpClient,
        private router: Router,
        // private socialAuthService: AuthService
    ) {
    }

    static setUserToken(value?) {
        if (value) {
            try {
                localStorage.setItem('token', value);
            } catch (e) {
                return false;
            }
        } else {
            try {
                localStorage.removeItem('token');
            } catch (e) {
                return false;
            }
        }

        return true;
    }

    static getUserToken() {
        const token = localStorage.getItem('token');
        return token ? token : '';
    }

    setLoginData(responseData) {
        if (responseData) {
            if (responseData['token']) {
                if (UserService.setUserToken(responseData['token'])) {
                    this.isUserLoggedIn$.next(true);
                    setTimeout(() => {
                        this.router.navigate(['/']);
                    }, 50);
                }
            }
        }
    }

    logout() {
        UserService.setUserToken();
        this.isUserLoggedIn$.next(false);
    }

    login(data) {
        const $request = this.http.post(this.authenticateUrl, data, {
            responseType: 'json'
        });

        $request.subscribe(
            requestData => this.setLoginData.call(this, requestData)
        );

        return $request;
    }

    loginSocial(provider) {
    //     let socialPlatformProvider;
    //
    //     if (provider === 'facebook') {
    //         socialPlatformProvider = FacebookLoginProvider.PROVIDER_ID;
    //     } else if (provider === 'google') {
    //         socialPlatformProvider = GoogleLoginProvider.PROVIDER_ID;
    //     }
    //
    //     this.socialAuthService.signIn(socialPlatformProvider).then(
    //         (userData) => {
    //             if (userData['token']) {
    //                 const $request = this.http.post(this.socialLoginUrl, userData, {
    //                     responseType: 'json'
    //                 });
    //
    //                 $request.subscribe(requestData => this.setLoginData.call(this, requestData));
    //             }
    //         });
    }

    register(data) {
        const $request = this.http.post(this.registrationUrl, data, {
            responseType: 'json'
        });

        $request.subscribe(requestData => this.setLoginData.call(this, requestData));

        return $request;
    }

    forgotPassword(data) {
        return this.http.post(this.resetPasswordUrl, data, {
            responseType: 'json'
        });
    }

    confirmEmail(data) {
        return this.http.post(this.confirmEmailUrl, data, {
            responseType: 'json'
        });
    }
}
