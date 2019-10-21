package binar.box.rest.payment.smartbill;

import binar.box.rest.payment.smartbill.dto.SmartBillPayRequest;
import binar.box.rest.payment.smartbill.dto.SmartBillPayResponse;
import binar.box.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Api(value = "SmartBill API", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = Constants.API + "smartbill")
public class SmartBillController {

    private final SmartBillService smartBillService;

    @Autowired
    public SmartBillController(SmartBillService smartBillService) {
        this.smartBillService = smartBillService;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token value", dataType = "string", paramType = "header") })
    @ApiOperation(value = "SmartBill send payment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(path = "/payment")
    public @ResponseBody
    ResponseEntity<SmartBillPayResponse> createPayment(@RequestBody SmartBillPayRequest smartBillPayRequest) throws IOException {
        return new ResponseEntity<>(smartBillService.sendPaymentToSmartBill(smartBillPayRequest), HttpStatus.CREATED);
    }
}
