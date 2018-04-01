import {Injectable, Injector} from '@angular/core';
import {HttpEvent, HttpInterceptor, HttpHandler, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/catch';
import {UserService} from '../_services/user.service';

const BASE_URL = 'http://localhost:6060/api/v0';

@Injectable()
export class RequestInterceptor implements HttpInterceptor {
    constructor() {
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        const token = UserService.getUserToken();

        // Clone the request to add the authentication token header.
        const authReq = req.clone({
            headers: req.headers.set('token', token),
            url: `${BASE_URL}/${req.url}`
        });

        return next.handle(authReq)
            .catch((error, caught) => {
                return Observable.throw(error);
            }) as any;
    }
}
