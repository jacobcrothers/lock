import {Injectable} from '@angular/core';
import {Subject} from 'rxjs/Subject';

@Injectable()
export class MessageService {
    messages = new Subject();

    constructor() {
    }

    postMessage(message, type = 'success') {
        this.messages.next({
            message: message,
            type: type
        });
    }
}
