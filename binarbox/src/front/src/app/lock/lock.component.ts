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
    // public isLockType = false;
    public selectedLock = {};
    public selectedLockCategory = {};
    public params = {};
    public lockCategories: Array<any> = [];

    constructor(
        private route: ActivatedRoute,
        private router: Router
    ) {
    }

    ngOnInit() {
        this.route.params.subscribe((params) => {
            this.updateParams(params);
        });      
        this.getCategories(); 
    }

    updateParams (parameters) {
        if (parameters['type']) {
            this.lockType = parameters['type'];
            this.displayLocks()
        }
        if (parameters['id']) {
            this.lockId = parameters['id'];
        }

        if (parameters['location']) {
            this.location = parameters['location'];
        }
    }

    getCategories() {
        //TO DO get all available categories and display them
        this.lockCategories = [{
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
    }

    displayLocks() {
        //TO DO get all available locks for selected category and display them
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

    chooseCategory(lockCategory) {
        this.selectedLockCategory = lockCategory;
        // console.log('gggggg--', this.selectedLockCategory, this.lockType, this.lockId);
        // TO DO send category id to BE and display all lock templates for the selected category
        this.router.navigate([`/add-lock/${this.selectedLockCategory['key']}`], { skipLocationChange: false } );
        // this.displayLocks();
    }
    
    chooseLock(lock) {
        this.selectedLock = lock;
        console.log('locks----', this.locks, lock);
        this.router.navigate([`/add-lock/${this.lockType}/${this.selectedLock['id']}`], { skipLocationChange: false } );

    }
}
