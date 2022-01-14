
import {throwError as observableThrowError, Observable} from 'rxjs';

import {catchError} from 'rxjs/operators';
import {Injectable} from '@angular/core';
import {HttpEvent, HttpInterceptor, HttpHandler, HttpRequest} from '@angular/common/http';


import {UserService} from '../_services/user.service';
import {MessageService} from '../_services/message.service';

const BASE_URL = 'https://api.lockbridges.com/api/v0';

@Injectable()
export class RequestInterceptor implements HttpInterceptor {
    constructor(
        private message: MessageService
    ) {}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        const token = UserService.getUserToken();

        // Clone the request to add the authentication token header.
        var url = "";
        if (req.url.indexOf("stripe") != -1) {
            url = `${req.url}`;
        } else {
            url = `${BASE_URL}/${req.url}`;
        }
        const authReq = req.clone({
            headers: req.headers.set('token', token),
            url: url
        });

        return next.handle(authReq).pipe(
            catchError((error, caught) => {
                let message = `
                    Whoops! Something broke!
                    Try again in a few minutes and if the problem persists contact someone from our team using the contact form.`;

                if (error['status'] === 0) {
                    message = 'Seems like the server is down. Sorry about that. Check back in a few moments.';
                } else if (error['error'] && error['error']['message']) {
                    message = error['error']['message'];
                }

                this.message.postMessage(message, 'error');
                return observableThrowError(error);
            })) as any;
    }
}
