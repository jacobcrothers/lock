package binar.box.service.payment;

import binar.box.converter.StripePaymentConverter;
import binar.box.domain.Lock;
import binar.box.domain.User;
import binar.box.dto.payment.CardDTO;
import binar.box.dto.payment.StripeDTO;
import binar.box.repository.LockRepository;
import binar.box.repository.UserRepository;
import binar.box.service.UserService;
import binar.box.util.Exceptions.PaymentException;
import com.stripe.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class StripeService {

    private static final BigDecimal TO_CENT = new BigDecimal(100);

    @Autowired
    private UserService userService;

    @Autowired
    private StripePaymentConverter stripePaymentConverter;

    @Autowired
    private LockRepository lockRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StripeGateway stripeGateway;

    public StripeDTO createCharge(StripeDTO stripeDTO) {
        User user = userService.getAuthenticatedUser();

        Lock lock = lockRepository.getOne(stripeDTO.getLockId());

        stripeDTO.setAmount(lock.getLockTemplate().getPrice().getPrice().multiply(TO_CENT).intValue());

        String chargeId;
        if (Objects.isNull(user.getStripeCustomerId()))
            chargeId = createInitCharge(user, stripeDTO, lock);
        else {
            Customer customer = stripeGateway.retrieveCustomer(user.getStripeCustomerId());
            Token token = stripeGateway.retrieveToken(stripeDTO.getStripeToken());

            boolean newCard = false;
            String cardId = hasCard(customer, token); // cardId is null if user doesn't have card
            if (cardId == null) {
                newCard = true;
                stripeGateway.addCardToCustomer(customer, token.getId());
                cardId = selectCardIdByFingerprint(customer, token.getCard().getFingerprint());
            }
            chargeId = stripeGateway.chargeCard(customer.getId(), cardId,
                    stripeDTO.getAmount(), stripeDTO.getCurrency().name(),
                    true, "Payment for lock with number:" + lock.getId()).getId();

            if (!stripeDTO.getSaveCard() && newCard) {
                stripeGateway.deleteCard(customer, stripeDTO.getCardId());
            }
        }
        lock.setPaid(true);
        lockRepository.save(lock);

        return StripeDTO.builder()
                .amount(lock.getLockTemplate().getPrice().getPrice().intValue())
                .lockId(lock.getId())
                .chargeId(chargeId)
                .build();
    }

    public List<CardDTO> getAllCards() {
        String stripeCustomerId = userService.getAuthenticatedUser().getStripeCustomerId();

        if (Objects.isNull(stripeCustomerId))
            return new ArrayList<>();
        return stripePaymentConverter.toDTOList(stripeGateway.getAllCards(stripeCustomerId));
    }

    private String createInitCharge(User user, StripeDTO stripeDTO, Lock lock) {
        Customer customer = stripeGateway.createCustomer(stripeDTO.getStripeToken(), user.getFirstName() + " " + user.getLastName(), user.getEmail());

        String chargeId = stripeGateway.charge(customer.getId(), stripeDTO.getAmount(), stripeDTO.getCurrency().name(), true, "Payment for lock number" + lock.getId()).getId();
        if (!stripeDTO.getSaveCard()) { //remove card
            stripeGateway.deleteCard(customer, stripeDTO.getCardId());
        }

        user.setStripeCustomerId(customer.getId());
        userRepository.save(user);

        return chargeId;
    }

    //if user generate a new payment token check the card if it's a new one
    //same cards have different ids but have the same fingerprint
    //return the cardId if fingerprint matches else return null
    private String hasCard(Customer customer, Token token) {
        List<CardDTO> cardDTOS = stripePaymentConverter.toDTOList(stripeGateway.getAllCards(customer.getId()));
        Optional<CardDTO> result = cardDTOS.stream()
                .filter(c->c.getFingerprint().equals(token.getCard().getFingerprint())).findAny();

        return result.map(CardDTO::getId).orElse(null);
    }

    private String selectCardIdByFingerprint(Customer customer, String fingerprint) {
        List<CardDTO> cardDTOS = stripePaymentConverter.toDTOList(stripeGateway.getAllCards(customer.getId()));
        Optional<CardDTO> result = cardDTOS.stream()
                .filter(c->c.getFingerprint().equals(fingerprint)).findAny();

        return result.map(CardDTO::getId).orElse(null);
    }

    public void refundCharge(StripeDTO stripeDTO) {
        Lock lock = lockRepository.getOne(stripeDTO.getLockId());

        if (StringUtils.isEmpty(stripeDTO.getChargeId()) || Objects.isNull(lock.getLockTemplate())) {
            throw new PaymentException("Transaction id empty","transaction.id.empty");
        }
        stripeGateway.refund(stripeDTO.getChargeId());

        lock.setPaid(false);
        lockRepository.save(lock);
    }
}
