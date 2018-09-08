import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Router} from '@angular/router';
import {
    AuthService,
    FacebookLoginProvider
} from 'angular5-social-login';

@Injectable()
export class UserService {

    private authenticateUrl     = 'authentication/login';
    private registrationUrl     = 'authentication/register';
    private socialLoginUrl      = 'authentication/facebook';
    private resetPasswordUrl    = 'user/request/reset/password';
    private confirmEmailUrl     = 'user/email/confirm';
    private changePasswordUrl   = 'user/change/password';
    private userUrl             = 'user';

    public isUserLoggedIn$ = new BehaviorSubject(Boolean(UserService.getUserToken()));

    constructor(
        private http: HttpClient,
        private router: Router,
        private socialAuthService: AuthService
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

    logout() {
        UserService.setUserToken();
        this.isUserLoggedIn$.next(false);
    }

    loginSocial(provider) {
        let socialPlatformProvider;

        if (provider === 'facebook') {
            socialPlatformProvider = FacebookLoginProvider.PROVIDER_ID;
        }

        this.socialAuthService.signIn(socialPlatformProvider).then(
            (userData) => {
                if (userData['token']) {
                    let loginBody = {
                        token: userData['token']
                    }
                    this.http.post(this.socialLoginUrl, loginBody).subscribe(() => {
                        if (UserService.setUserToken(userData['token'])) {
                            this.isUserLoggedIn$.next(true);
                            setTimeout(() => {
                                this.router.navigate(['/']);
                            }, 50);
                        }
                    }, (error) => {
                        console.log('login request failed', error);
                    });
                }
            });
    }

    forgotPassword(data) {
        return this.http.post(this.resetPasswordUrl, data, {
            responseType: 'json'
        });
    }

    changePassword(data) {
        return this.http.post(this.changePasswordUrl, data);
    }

    confirmEmail(data) {
        return this.http.post(this.confirmEmailUrl, data);
    }

    getUserData() {
        return this.http.get(this.userUrl, {});
    }

    setUserData(data) {
        return this.http.put(this.userUrl, data);
    }
}
