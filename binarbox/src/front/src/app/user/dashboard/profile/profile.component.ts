import {Component, OnInit} from '@angular/core';

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
    userData = {
        firstName: 'Tudor',
        lastName: 'Merlas',
        email: 'tudoreaux@yahoo.com',
        phone: '+40 743 187 287',
        address: 'str. Prapastiei, nr. 4, sc. 4',
        city: 'Prahova',
        country: 'RO'
    };

    constructor() {
    }

    ngOnInit() {
        // TODO: get user details if not already stored or just refresh them

    }

    changePassword(values) {
        console.log(this, 'values: ', values);
    }

}
