import {Component} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PaymentService } from '../../../_services/payment.service';

@Component({
    selector: 'app-payment',
    templateUrl: './payment.component.html',
    styleUrls: ['./payment.component.scss']
})
export class PaymentComponent {

    strikeCheckout:any = null;
    constructor(private paymentService: PaymentService, private http: HttpClient) {}
    token:string = '';

    ngOnInit() {
        this.stripePaymentGateway();
    }   

    checkout(amount){
        const strikeCheckout = (<any>window).StripeCheckout.configure({
            key: 'pk_test_EykuMGqVFU5B3sqMAw73hnlO',
            locale: 'auto',
            token: (stripeToken: any) => {
                // PaymentComponent.token = stripeToken.id;
                this.charge(stripeToken.id, amount);
            }
        });
        strikeCheckout.open({
            name: 'RemoteStack',
            description: 'payment widgets',
            amount: amount * 100
        });
    }
    stripePaymentGateway(){
        if(!window.document.getElementById('stripe-script')){
            const scr = window.document.createElement("script");
            scr.id = "stripe-script";
            scr.type = "text/javascript";
            scr.src = "https://checkout.stripe.com/checkout.js";
            
            scr.onload = () => {
                this.strikeCheckout = (<any>window).StripeCheckout.configure({
                    key: 'pk_test_EykuMGqVFU5B3sqMAw73hnlO',
                    locale: 'auto',
                    token: function(token: any){
                        alert('Payment via stripe sucessfull');
                    }
                });
            }
            window.document.body.appendChild(scr);
        }
    }

    charge(stripeToken, amount) {
        console.log("++++++");
        const chargeUrl = "https://api.stripe.com/v1/charges";
        const headers = new HttpHeaders()
            .set('Authorization', 'Bearer sk_test_yj5vdPXDBxmL9X4Sy17nRa51')
            .set('Content-Type', 'application/x-www-form-urlencoded');

        let body = new URLSearchParams();
        body.set('source', stripeToken);
        body.set('amount', amount);
        body.set('currency', 'usd');
        body.set('description', 'RAMA test');

        this.http.post(chargeUrl, body, {headers: headers}).subscribe(res => {
            console.log(res);
        });
    };
}