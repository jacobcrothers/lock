import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AddLockService } from '../_services/add-lock.service';
import { Location } from '@angular/common';

@Component({
    selector: 'app-lock',
    templateUrl: './lock.component.html',
    styleUrls: ['./lock.component.css']
})
export class LockComponent implements OnInit {

    public lockType: string;
    public lockId = 0;
    // public location = 0;
    // public tempLocation = 0; // Used to allow the user to change their mind regarding the location they want to choose.
    public locks: Array<any> = [];
    public selectedLock = {};
    public selectedLockCategory = {};
    public params = {};
    public lockCategories: Array<any> = [];

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private addLockService: AddLockService,
        private location: Location
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
        }
        if (parameters['id']) {
            this.lockId = parameters['id'];
        }

        // if (parameters['location']) {
        //     this.location = parameters['location'];
        // }
    }

    getCategories() {
        this.addLockService.getLockTypes().subscribe(data => {
            this.lockCategories = data;
        });
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
        this.lockType = lockCategory.type;
        // TO DO send category id to BE and display all lock templates for the selected category
        this.location.replaceState(`/add-lock/${this.selectedLockCategory['type']}`);
        // this.router.navigate([`/add-lock/${this.selectedLockCategory['type']}`], { skipLocationChange: false } );
        this.displayLocks()
    }
    
    chooseLock(lock) {
        this.selectedLock = lock;
        this.lockId = lock.id;
        console.log('locks----', this.locks, lock);
        this.location.replaceState(`/add-lock/${this.lockType}/${this.selectedLock['id']}`);
        // this.router.navigate([`/add-lock/${this.lockType}/${this.selectedLock['id']}`], { skipLocationChange: false } );

    }

    goBack(path) {
        if (path === 'lockType') {
            this.location.replaceState('/add-lock');
            this.lockType = '';
        } else if (path === 'lockId') {
            this.location.replaceState(`/add-lock/${this.lockType}`);
            this.lockId = 0;
        }
    }
}
