import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {UserService} from '../../_services/user.service';

@Component({
    selector: 'app-confirm-email',
    templateUrl: './confirm-email.component.html',
    styleUrls: ['./confirm-email.component.css']
})
export class ConfirmEmailComponent implements OnInit {

    public token;
    public status = 'tokenless';

    constructor(private route: ActivatedRoute,
                private user: UserService) {
    }

    ngOnInit() {
        this.route.params.subscribe(params => {
            this.token = params['token'];
            if (this.token) {
                this.status = 'hasToken';

                this.confirmEmail({token: this.token});
            }
        });

        console.log(this.status);
    }

    confirmEmail(value) {
        this.user.confirmEmail(value).subscribe((data) => {
            if (data['status'] === '200') {
                this.status = 'confirmed';
            } else {
                this.status = 'tokenless';
            }
        });

        this.status = 'sending';
    }

}
