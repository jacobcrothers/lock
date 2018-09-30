import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
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
    public locks: Array<any> = [];
    public selectedLock = {};
    public selectedLockCategory = {};
    public params = {};
    public lockCategories: any;
    public predefinedMessages: Array<any> = [];
    public fontColors: Array<any> = [];
    public isCustomLockSaved = false;
    public selectedMessage: string;
    public pageParams: any;
    public formSubmitted: boolean;

    @ViewChild('closeModal') closeModal:ElementRef;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private addLockService: AddLockService,
        private location: Location
    ) {
    }

    ngOnInit() {
        this.getCategories();
        this.route.params.subscribe((params) => {
            console.log('params', params);
            if(params) {
                this.pageParams = params;
                this.updateParams(params);
            }
        });   
        // move this in chooseLock function
        this.displayOptionsToCustomize()
    }

    updateParams(parameters) {
        if (parameters['type']) {
            this.lockType = parameters['type'];
            this.getCategories();
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
            if(this.pageParams) {
                this.lockCategories.forEach(category => {
                    if(this.pageParams['type'] && category['type'] === this.pageParams['type']) {
                        this.selectedLockCategory = category;
                        this.displayLocks();
                        if(this.pageParams['id']) {
                            this.setLockFromParams(this.selectedLockCategory, this.pageParams['id']);
                        }
                    }
                });
            }
        });
    }

    setLockFromParams(category, lockId) {
        category['lockTypeTemplate'].forEach(template => {
            if(template['id'].toString() === lockId) {
                this.selectedLock = template;
            }
        });
    }

    displayLocks() {
        this.locks = this.selectedLockCategory['lockTypeTemplate'];

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
        this.location.replaceState(`/add-lock/${this.lockType}/${this.selectedLock['id']}`);
        // this.router.navigate([`/add-lock/${this.lockType}/${this.selectedLock['id']}`], { skipLocationChange: false } );
    }

    displayOptionsToCustomize() {
        // TO DO get what is needed here from BE - predefined messages, text colors, lock colors
        this.predefinedMessages = [
            {
                id: 1,
                value: "Awesome message"
            },
            {
                id: 2,
                value: "A simple message"
            },
            {
                id: 3,
                value: "Nice message"
            }
        ];
    }

    saveLock(formValue) {
        let createdLock = {
            "fontColor": this.selectedLock['fontColor'],
            "fontSize": this.selectedLock['fontSize'],
            "fontStyle": this.selectedLock['fontStyle'],
            "message": formValue['insertMessage'],
            "lockType": this.selectedLockCategory['id'],
            "lockTypeTemplate": this.selectedLock['id'],
            "lockPrivacy": formValue['privacy'] === "private" ? true : false
        };
        // TODO: send the created lock to BE and, when success -> display the panels
        //to display the panels create a new component and navigate to it with the created lock
        this.addLockService.saveLock(createdLock).subscribe(data => {
            console.log('lock data after save lock--', data);
            this.router.navigate([`/panels`] );
        })

    }

    saveMessage(formValue) {
        this.predefinedMessages.forEach(msg => {
            if (formValue['messageSelect'] == msg.id) {
                this.selectedMessage = msg.value;
                this.closeModal.nativeElement.click();
            } 
        });
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
