package binar.box.rest;

import binar.box.dto.payment.PaypalDTO;
import binar.box.service.payment.PaypalService;
import binar.box.util.Constants;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "PayPal API", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = Constants.API + "paypal")
public class PaypalController {

    private final PaypalService payPalService;

    @Autowired
    public PaypalController(PaypalService payPalService) {
        this.payPalService = payPalService;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token value", dataType = "string", paramType = "header") })
    @ApiOperation(value = "New PayPal payment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, code = 201)
    @RequestMapping(value = "/charge", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<PaypalDTO> createPayPalPayment(@RequestBody PaypalDTO payPalDTO) {
        return new ResponseEntity<>(payPalService.createTransaction(payPalDTO), HttpStatus.CREATED);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token value", dataType = "string", paramType = "header") })
    @ApiOperation(value = "PayPal refund payment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/refund", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity refundTransaction(@RequestBody PaypalDTO payPalDTO) {
        payPalService.refundTransaction(payPalDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
