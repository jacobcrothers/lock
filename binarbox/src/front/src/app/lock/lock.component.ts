import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

@Component({
    selector: 'app-lock',
    templateUrl: './lock.component.html',
    styleUrls: ['./lock.component.css']
})
export class LockComponent implements OnInit {

    public lockId = 0;
    public location = 0;
    public tempLocation = 0; // Used to allow the user to change their mind regarding the location they want to choose.
    public locks = [{
        id: 1,
        price: 12,
        type: 'VIP'
    }, {
        id: 2,
        price: 6,
        type: 'THE Lock'
    }, {
        id: 3,
        price: 3,
        type: 'Padlock'
    }];

    constructor(
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.route.params.subscribe((params) => {
            this.updateParams(params);
        });
    }

    updateParams (parameters) {
        if (parameters['id']) {
            this.lockId = parameters['id'];
        }

        if (parameters['location']) {
            this.location = parameters['location'];
        }
    }

}
