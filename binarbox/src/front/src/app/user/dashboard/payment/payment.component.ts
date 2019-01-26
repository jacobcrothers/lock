import {Component, OnInit} from '@angular/core';
// import { PaymentService } from 'src/app/_services/payment.service';
import { PaymentService } from '../../../_services/payment.service';

@Component({
    selector: 'app-payment',
    templateUrl: './payment.component.html',
    styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {

    constructor(
        private paymentService: PaymentService
    ) {}

    ngOnInit() {
    }

    chargeCreditCard(formValue) {
        (<any>window).Stripe.card.createToken({
          number: formValue.cardNumber,
          exp_month: formValue.expMonth,
          exp_year: formValue.expYear,
          cvc: formValue.cvc
        }, (status: number, response: any) => {
          if (status === 200) {
            let token = response.id;
            this.chargeCard(token);
          } else {
            console.log(response.error.message);
          }
        });
      }

    chargeCard(token: string) {
        this.paymentService.chargeCard(token).subscribe(resp => {
            console.log('response---', resp);
        })
    }

}
