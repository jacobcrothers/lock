import {Component, OnInit} from '@angular/core';
// import { PaymentService } from 'src/app/_services/payment.service';
import { PaymentService } from '../../../_services/payment.service';

@Component({
    selector: 'app-payment',
    templateUrl: './payment.component.html',
    styleUrls: ['./payment.component.scss']
})
export class PaymentComponent implements OnInit {

    constructor(
        private paymentService: PaymentService
    ) {}

    ngOnInit() {
    }

}
