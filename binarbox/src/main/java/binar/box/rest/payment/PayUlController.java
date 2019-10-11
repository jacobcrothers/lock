package binar.box.rest.payment;

import binar.box.dto.payment.payu.PaymentCallback;
import binar.box.dto.payment.payu.PaymentDetail;
import binar.box.service.payment.PayUService;
import binar.box.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api(value = "PayU API", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = Constants.API + "payu")
public class PayUlController {


    private final PayUService payUService;

    @Autowired
    public PayUlController(PayUService payUService) {
        this.payUService = payUService;
    }

//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "Authorization", value = "token value", dataType = "string", paramType = "header") })
//    @ApiOperation(value = "New PayPal payment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, code = 201)
//    @RequestMapping(value = "/charge", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(value = HttpStatus.CREATED)
//    public ResponseEntity<PaypalDTO> createPayPalPayment(@RequestBody PaypalDTO payPalDTO) {
//        return new ResponseEntity<>(payPalService.createTransaction(payPalDTO), HttpStatus.CREATED);
//    }
//
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "Authorization", value = "token value", dataType = "string", paramType = "header") })
//    @ApiOperation(value = "PayPal refund payment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @RequestMapping(value = "/refund", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(value = HttpStatus.NO_CONTENT)
//    public ResponseEntity refundTransaction(@RequestBody PaypalDTO payPalDTO) {
//        payPalService.refundTransaction(payPalDTO);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token value", dataType = "string", paramType = "header") })
    @ApiOperation(value = "PayU start payment -> redirect to 3rd party", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(path = "/payment-details")
    public @ResponseBody
    PaymentDetail proceedPayment(@RequestBody PaymentDetail paymentDetail){
        return payUService.proceedPayment(paymentDetail);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token value", dataType = "string", paramType = "header") })
    @ApiOperation(value = "PayU receive response from 3rd party", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(path = "/payment-response", method = RequestMethod.POST)
    public @ResponseBody String payuCallback(@RequestBody PaymentCallback paymentCallback){
        return payUService.payuCallback(paymentCallback);
    }
}
