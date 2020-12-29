import {Component, OnInit, Input} from '@angular/core';

@Component({
    selector: 'app-lock-details',
    templateUrl: './lock-details.component.html',
    styleUrls: ['./lock-details.component.scss']
})
export class LockDetailsComponent implements OnInit {
    @Input() currentLock;

    constructor() {
    }

    ngOnInit() {
    }

}
