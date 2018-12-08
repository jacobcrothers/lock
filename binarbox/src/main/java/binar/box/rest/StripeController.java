package binar.box.rest;

import binar.box.dto.payment.CardDTO;
import binar.box.dto.payment.ChargeRequest;
import binar.box.service.StripeService;
import binar.box.util.Constants;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(value = "Stripe API", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = Constants.API + "stripe")
public class StripeController {

    @Autowired
    private StripeService stripeService;

    @RequestMapping(value = "/cards", method = RequestMethod.GET)
    public List<CardDTO> getAllCards() {
        return stripeService.getAllCards();
    }

    @RequestMapping(value = "/payments/charge/{lockId}", method = RequestMethod.POST)
    public ResponseEntity charge(@RequestParam(name="lockId") Long lockId,
                                    @RequestBody ChargeRequest chargeRequest) {
        stripeService.createCharge(chargeRequest,lockId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
