import {Component, OnInit} from '@angular/core';
import {UserService} from '../../../_services/user.service';
import {MessageService} from '../../../_services/message.service';

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
    userData = {
    // userData = {
    //     firstName: 'Tudor',
    //     lastName: 'Merlas',
    //     email: 'tudoreaux@yahoo.com',
    //     phone: '+40 743 187 287',
    //     address: 'str. Prapastiei, nr. 4, sc. 4',
    //     city: 'Prahova',
    //     country: 'RO'
    };

    constructor(private user: UserService,
                private message: MessageService) {
    }

    ngOnInit() {
        this.getUserData();
    }

    changePassword(passwordData) {
        if (passwordData.password === passwordData.confirmPassword) {
            this.user.changePassword(passwordData).subscribe(() => {
            });
        }
    }

    getUserData() {
        this.user.getUserData().subscribe((data) => {
            this.userData = data;
        });
    }

    setUserData(userData) {
        this.user.setUserData(userData).subscribe(() => {
            this.message.postMessage('Your data has been successfully updated.');
        });
    }


}
