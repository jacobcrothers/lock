import {Component, OnInit} from '@angular/core';

@Component({
    selector: 'app-locks',
    templateUrl: './locks.component.html',
    styleUrls: ['./locks.component.scss']
})
export class LocksComponent implements OnInit {
    public currentLock;
    public locks = [{
        id: 1,
        date: '06.03.1987',
        type: 'VIP'
    }, {
        id: 2,
        date: '27.07.1995',
        type: 'THE Lock'
    }, {
        id: 3,
        date: '06.06.2016',
        type: 'Padlock'
    }];

    // public locks;

    constructor() {
    }

    ngOnInit() {
    }

    selectLock(index) {
        this.currentLock = this.locks[index];

        if (this.locks && this.locks.length) {
            for (const i in this.locks) {
                if (this.locks.hasOwnProperty(i)) {
                    this.locks[i]['selected'] = false;
                }
            }
        }

        this.locks[index]['selected'] = true;

        console.log(this.locks);
    }

}
