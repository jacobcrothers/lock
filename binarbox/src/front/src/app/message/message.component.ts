import {Component, OnInit} from '@angular/core';
import {MessageService} from '../_services/message.service';

@Component({
    selector: 'app-message',
    templateUrl: './message.component.html',
    styleUrls: ['./message.component.css']
})
export class MessageComponent implements OnInit {
    messages = [];

    constructor(
        private messageService: MessageService
    ) {
    }

    ngOnInit() {
        this.messageService.messages.subscribe((value) => {
            this.postMessage(value['message'], value['type']);
        });
    }

    removeMessage(index) {
        this.messages.splice(index, 1);
    }

    postMessage(message, type = 'success') {
        this.messages.push({
            text: message,
            type: type
        });
    }

    // Wrapper
    addSuccess(message) {
        this.postMessage(message, 'success');
    }

    addError(message) {
        this.postMessage(message, 'error');
    }
}
