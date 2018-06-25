import {Component, OnInit, Input} from '@angular/core';

@Component({
    selector: 'app-lock-details',
    templateUrl: './lock-details.component.html',
    styleUrls: ['./lock-details.component.css']
})
export class LockDetailsComponent implements OnInit {
    @Input() currentLock;

    constructor() {
    }

    ngOnInit() {
    }

}
