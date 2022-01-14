import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject} from 'rxjs';
import {Router} from '@angular/router';
import {
    FacebookLoginProvider, SocialAuthService
} from 'angularx-social-login';

@Injectable()
export class UserService {

    private socialLoginUrl      = 'authentication/facebook';
    private userUrl             = 'user';

    public isUserLoggedIn$ = new BehaviorSubject(Boolean(UserService.getUserToken()));

    constructor(
        private http: HttpClient,
        private router: Router,
        private socialAuthService: SocialAuthService
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

    static setUserId(value?) {
        if (value) {
            try {
                localStorage.setItem('sd', value);
            } catch (e) {
                return false;
            }
        } else {
            try {
                localStorage.removeItem('sd');
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

    static getUserId() {
        const userId = localStorage.getItem('sd');
        return userId ? userId : '';
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
                if (userData['authToken']) {
                    const loginBody = {
                        authToken: userData['authToken']
                    };
                    this.http.post(this.socialLoginUrl, loginBody).subscribe(() => {
                        UserService.setUserId(userData['id']);
                        if (UserService.setUserToken(userData['authToken'])) {
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
}
