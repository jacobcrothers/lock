import {Component, ElementRef, HostListener, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AddLockService} from '../../../_services/add-lock.service';
import {Location} from '@angular/common';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
    selector: 'app-add-lock',
    templateUrl: './add-lock.component.html',
    styleUrls: ['./add-lock.component.scss']
})
export class AddLockComponent implements OnInit {
    @ViewChild('closeModal') closeModal: ElementRef;

    public addLockMessageForm: FormGroup;

    public lockType: string;
    public lockId = 0;
    public locks: Array<any> = [];
    public selectedLock = {
        filesDTO: undefined
    };
    public selectedLockCategory:any = {};
    public selectedLockInfo = {
        url: ''
    };
    public lockCategories: any;
    public pageParams: any;
    public isEllipticText: boolean;
    // public predefinedMessages: Array<any> = [];

    private lockInfo: Object;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private addLockService: AddLockService,
        private location: Location,
        private formBuilder: FormBuilder
    ) {
    }

    ngOnInit() {
        localStorage.removeItem('lockInfo');
        this.createForm();

        this.getCategories();
        this.route.params.subscribe((params) => {
            if (params) {
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
            let fonts1 = ["blue", "blueviolet", "violet", "yellow", "greenyellow", "yellowgreen"];
            for (let i = 0; i < this.lockCategories[0]["lockTypeTemplate"].length; i ++) {
                if (this.lockCategories[0]["lockTypeTemplate"][i].fontsDTO.length > 0) {
                    this.lockCategories[0]["lockTypeTemplate"][i].fontsDTO[0].fontColor = fonts1[i];
                } else {
                    this.lockCategories[0]["lockTypeTemplate"][i].fontsDTO.push({ "fontColor": fonts1[i]});
                }
            }

            let fonts2 = ["cyan", "mediumpurple", "tan"];
            for (let i = 0; i < this.lockCategories[1]["lockTypeTemplate"].length; i ++) {
                if (this.lockCategories[1]["lockTypeTemplate"][i].fontsDTO.length > 0) {
                    this.lockCategories[1]["lockTypeTemplate"][i].fontsDTO[0].fontColor = fonts2[i];
                } else {
                    this.lockCategories[1]["lockTypeTemplate"][i].fontsDTO.push({ "fontColor": fonts2[i]});
                }
            }

            let fonts3 = ["black", "#1e7e34", "white", "#2d9b96", "#8ba136"];
            for (let i = 0; i < this.lockCategories[2]["lockTypeTemplate"].length; i ++) {
                if (this.lockCategories[2]["lockTypeTemplate"][i].fontsDTO.length > 0) {
                    this.lockCategories[2]["lockTypeTemplate"][i].fontsDTO[0].fontColor = fonts3[i];
                } else {
                    this.lockCategories[2]["lockTypeTemplate"][i].fontsDTO.push({ "fontColor": fonts3[i]});
                }
            }
            
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
        });
    }

    setLockFromParams(category, lockId) {
        category['lockTypeTemplate'].forEach(template => {
            if (template['id'].toString() === lockId) {
                this.selectedLock = template;
            }
            this.location.replaceState(`/locks/add-lock/${this.lockType}/${this.selectedLock['id']}`);
        });
    }

    displayLocks() {
        this.locks = this.selectedLockCategory['lockTypeTemplate'];

    }

    chooseCategory(lockCategory) {
        this.selectedLockInfo = lockCategory;
        this.selectedLockCategory = lockCategory;
        this.lockType = lockCategory.category;
        this.location.replaceState(`/locks/add-lock/${this.selectedLockCategory['category']}`);
        this.displayLocks();
    }

    chooseLock(lock) {
        this.selectedLock = lock;

        this.lockId = lock.id;
        this.location.replaceState(`/locks/add-lock/${this.lockType}/${this.selectedLock['id']}`);
    }

    /**
     * Insert LINE_END after first line in order to be able to generate the lock in the text on backend
     * Input: Messsage on line 1 /n Message on line 2
     * Output: Message on line 1 {LINE_END} Message on line 2
     * @param {String} message
     * @return {String} formattedMessage
     * */
    formatMessageWithLineDelimitator(message: String) {
        return message.replace(/(\r\n|\n|\r)/gm, '{LINE_END}');
    }

    saveLock() {
        if (!this.addLockMessageForm.valid) {
            return;
        }

        const formValue = this.addLockMessageForm.value;
        const message = this.formatMessageWithLineDelimitator(formValue.message || '');
        const createdLock = {
            'message': encodeURIComponent(message),
            'lockTemplate': this.selectedLock['id'],
            'privateLock': formValue.privateLock
        };
        this.addLockService.saveLock(createdLock).subscribe(data => {
            this.lockInfo = data;
            this.selectedLockInfo = Object.assign(this.selectedLockInfo, data);
            this.selectedLockInfo.url = this.selectedLock.filesDTO[0].urlToFile;
            localStorage.setItem("lockInfo", JSON.stringify(this.selectedLockInfo));
            this.addLockService.setLockId(data);
            this.router.navigate([`/panels`]).then();
        });
    }

    // saveMessage(formValue) {
    //     this.predefinedMessages.forEach(msg => {
    //         if (formValue['messageSelect'] === msg.id) {
    //             this.selectedMessage = msg.value;
    //             this.closeModal.nativeElement.click();
    //         }
    //     });
    // }

    goBack(path) {
        if (path === 'lockType') {
            this.location.replaceState('/locks/add-lock');
            this.lockType = '';
        } else if (path === 'lockId') {
            this.location.replaceState(`/locks/add-lock/${this.lockType}`);
            this.lockId = 0;
        }
    }

    isEllipsisActive(element: HTMLHeadingElement) {
        this.isEllipticText = element.offsetWidth < element.scrollWidth;
    }

    public setColor(color: string): string {
        return color ? color.replace('_', '').toLowerCase() : 'lightgray';
    }

    public clearForm() {
        this.addLockMessageForm.reset();
    }

    private createForm() {
        this.addLockMessageForm = this.formBuilder.group({
            message: ['', Validators.required],
            privateLock: [false]
        });
    }
}
