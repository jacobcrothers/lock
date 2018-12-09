package binar.box.rest;

import binar.box.dto.payment.CardDTO;
import binar.box.dto.payment.StripeDTO;
import binar.box.service.payment.StripeService;
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

import java.util.List;

@Api(value = "Stripe API", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = Constants.API + "stripe")
public class StripeController {

    @Autowired
    private StripeService stripeService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token value", dataType = "string", paramType = "header") })
    @ApiOperation(value = "Get all user cards from stripe", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/cards", method = RequestMethod.GET)
    public List<CardDTO> getAllCards() {
        return stripeService.getAllCards();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token value", dataType = "string", paramType = "header") })
    @ApiOperation(value = "Stripe new payment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/charge", method = RequestMethod.POST)
    public ResponseEntity charge(@RequestBody StripeDTO stripeDTO) {
        return new ResponseEntity<>(stripeService.createCharge(stripeDTO), HttpStatus.CREATED);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token value", dataType = "string", paramType = "header") })
    @ApiOperation(value = "Stripe refund payment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public ResponseEntity refund(@RequestBody StripeDTO stripeDTO) {
        stripeService.refundCharge(stripeDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
