package binar.box.rest;

import binar.box.dto.PaymentDTO;
import binar.box.dto.UserDTO;
import binar.box.service.PaymentService;
import binar.box.util.Constants;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(value = "PayPal API", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = Constants.API + "/paypal")
public class PaymentController {

    @Autowired
    private PaymentService payPalService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token value", dataType = "string", paramType = "header") })
    @ApiOperation(value = "New PayPal payment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, code = 201, response = UserDTO.class, notes = "Response is wrapped as 'item'")
    @RequestMapping(value = "/new", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Map<String, String> createPayPalPayment(@RequestBody PaymentDTO payPalDTO) {
        return payPalService.createTransaction(payPalDTO);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token value", dataType = "string", paramType = "header") })
    @ApiOperation(value = "PayPal refound payment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, code = 201, response = UserDTO.class, notes = "Response is wrapped as 'item'")
    @RequestMapping(value = "/refund", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void refundTransaction(@RequestBody PaymentDTO payPalDTO) {
        payPalService.refundTransaction(payPalDTO);
    }

}
