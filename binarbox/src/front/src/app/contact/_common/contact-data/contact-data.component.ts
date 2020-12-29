import {Component, Input, OnInit} from '@angular/core';

@Component({
    selector: 'app-contact-data',
    templateUrl: './contact-data.component.html',
    styleUrls: ['../../../privacy/_components/_common/privacy.scss', './contact-data.component.scss']
})
export class ContactDataComponent implements OnInit {

    @Input() showTitle = true;
    @Input() withCol = false;

    constructor() {
    }

    ngOnInit() {
    }

}
