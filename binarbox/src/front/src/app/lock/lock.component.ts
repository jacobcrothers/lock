import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
    selector: 'app-lock',
    templateUrl: './lock.component.html',
    styleUrls: ['./lock.component.css']
})
export class LockComponent implements OnInit {

    public lockType: string;
    public lockId = 0;
    public location = 0;
    public tempLocation = 0; // Used to allow the user to change their mind regarding the location they want to choose.
    public locks: Array<any> = [];
    public isLockType = false;
    public selectedLock = {};
    public selectedLockType = {};
    public params = {};
    public lockTypes = [{
        id: 1,
        price: 12,
        type: 'Regular',
        key: 'regular'
    }, {
        id: 2,
        price: 6,
        type: 'VIP',
        key: 'vip'
    }, {
        id: 3,
        price: 3,
        type: 'Premium',
        key: 'premium'
    }];

    constructor(
        private route: ActivatedRoute,
        private router: Router
    ) {
    }

    ngOnInit() {
        this.route.params.subscribe((params) => {
            this.updateParams(params);
        });
    }

    updateParams (parameters) {
        if (parameters['type']) {
            this.lockType = parameters['type'];
            this.isLockType = true;
            this.displayLocks();
        }
        if (parameters['id']) {
            this.lockId = parameters['id'];
        }

        if (parameters['location']) {
            this.location = parameters['location'];
        }
    }

    displayLocks() {
        this.locks = [
            {
                id: 1,
                name: 'lock1'
            },
            {
                id: 2,
                name: 'lock2'
            },
            {
                id: 3,
                name: 'lock3'
            },
        ]
    }

    chooseCategory(lockType) {
        this.selectedLockType = lockType;
        this.router.navigate([`/add-lock/${this.selectedLockType['key']}`], { skipLocationChange: true } );
    }
    
    chooseLock(lock) {
        console.log('selected lock type--', this.selectedLockType);
        this.selectedLock = lock;
        this.router.navigate([`/add-lock/${this.selectedLockType['key']}/${this.selectedLock['id']}`], { skipLocationChange: true } );
    }
}
