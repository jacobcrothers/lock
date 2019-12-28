import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AddLockService } from '../_services/add-lock.service';
import { Location } from '@angular/common';

@Component({
    selector: 'app-lock',
    templateUrl: './lock.component.html',
    styleUrls: ['./lock.component.scss']
})
export class LockComponent implements OnInit {

    public lockType: string;
    public lockId = 0;
    public locks: Array<any> = [];
    public selectedLock = {
        filesDTO: undefined
    };
    public selectedLockCategory = {};
    public params = {};
    public lockCategories: any;
    public predefinedMessages: Array<any> = [];
    public fontColors: Array<any> = [];
    public isCustomLockSaved = false;
    public selectedMessage: string;
    public pageParams: any;
    public formSubmitted: boolean;
    public lockPrivacy: any;
    private lockInfo: Object;

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
            if(params) {
                this.pageParams = params;
                this.updateParams(params);
            }
        });   
    }

    updateParams(parameters) {
        if (parameters['type']) {
            this.lockType = parameters['type'];
        }
        if (parameters['id']) {
            this.lockId = parameters['id'];
        }
    }

    getCategories() {
        this.addLockService.getLockTypes().subscribe(data => {
            this.lockCategories = data;
            this.lockCategories[0].url = 'assets/images/lock1.png';
            this.lockCategories[1].url = 'assets/images/lock2.png';
            this.lockCategories[2].url = 'assets/images/lock3.png';
            if (this.pageParams) {
                if (this.pageParams['type']) {
                    this.selectedLockCategory = this.lockCategories.find(category => {
                        return category.category === this.pageParams['type'];
                    });
                    this.chooseCategory(this.selectedLockCategory);
                    if (this.pageParams['id']) {
                        this.setLockFromParams(this.selectedLockCategory, this.pageParams['id']);
                    }
                }
            }
            console.log(this.lockCategories);
        });
    }

    setLockFromParams(category, lockId) {
        category['lockTypeTemplate'].forEach(template => {
            if(template['id'].toString() === lockId) {
                this.selectedLock = template;
                console.log('selected lock from params', this.selectedLock);
            }
            this.location.replaceState(`/add-lock/${this.lockType}/${this.selectedLock['id']}`);
        });
    }

    displayLocks() {
        this.locks = this.selectedLockCategory['lockTypeTemplate'];

    }

    chooseCategory(lockCategory) {
        this.selectedLockCategory = lockCategory;
        this.lockType = lockCategory.category;
        this.location.replaceState(`/add-lock/${this.selectedLockCategory['category']}`);
        this.displayLocks()
    }
    
    chooseLock(lock) {
        this.selectedLock = lock;

        // console.log('selected lock---', this.selectedLock);
        this.lockId = lock.id;
        this.location.replaceState(`/add-lock/${this.lockType}/${this.selectedLock['id']}`);
    }

    /**
     * Insert LINE_END after first line in order to be able to generate the lock in the text on backend
     * Input: Messsage on line 1 /n Message on line 2
     * Output: Message on line 1 {LINE_END} Message on line 2
     * @param {String} message
     * @return {String} formattedMessage
     * */
    formatMessageWithLineDelimitator(message: String) {
        return message.replace(/(\r\n|\n|\r)/gm,"{LINE_END}");
    }

    saveLock(formValue) {
        const message = this.formatMessageWithLineDelimitator(formValue['insertMessage'] || "");
        let createdLock = {
            "message": encodeURIComponent(message),
            "lockTemplate": this.selectedLock['id'],
            "privateLock": formValue['privacy'] === "private"
        };
        console.log(this.formatMessageWithLineDelimitator(message));
        this.addLockService.saveLock(createdLock).subscribe(data => {
            this.lockInfo = data;
            this.addLockService.setLockId(data);
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
