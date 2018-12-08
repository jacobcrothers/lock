package binar.box.service;

import binar.box.converter.StripePaymentConverter;
import binar.box.domain.Lock;
import binar.box.domain.User;
import binar.box.dto.payment.CardDTO;
import binar.box.dto.payment.ChargeRequest;
import binar.box.repository.LockRepository;
import binar.box.repository.UserRepository;
import binar.box.util.Exceptions.PaymentException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StripeService {

    public static final BigDecimal TO_CENT = new BigDecimal(100);
    @Value("${stripe.secret.key}")
    private String secretKey;

    @Autowired
    private UserService userService;

    @Autowired
    private StripePaymentConverter stripePaymentConverter;

    @Autowired
    private LockRepository lockRepository;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    //TODO remove  e.printStackTrace()  in production and replace it with a log
    public Customer createCustomer(String stripeToken, String description, String customerEmail) {
        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("source", stripeToken);
        customerParams.put("email", customerEmail);
        customerParams.put("description", description);
        try {
            return Customer.create(customerParams);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new PaymentException("Exception on creating customer for email " + customerEmail);
        }
    }

    //this will charge from the default customer card
    public Charge charge(String customerId, int amount, String currency, boolean capture, String description) {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("customer", customerId);
        chargeParams.put("amount", amount);
        chargeParams.put("currency", currency);
        chargeParams.put("capture", capture);
        chargeParams.put("description", description);
        try {
            return Charge.create(chargeParams);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new PaymentException("Exception on charging card for customerId " + customerId);
        }
    }

    public Charge chargeCard(String customerId, String cardId, int amount, String currency, boolean capture, String description) {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("customer", customerId);
        chargeParams.put("amount", amount);
        chargeParams.put("currency", currency);
        chargeParams.put("capture", capture);
        chargeParams.put("description", description);
        chargeParams.put("source", cardId);
        try {
            return Charge.create(chargeParams);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new PaymentException("Exception on charging card for customerId " + customerId);
        }
    }

    public Refund refund(String chargeId) {
        Map<String, Object> refundParams = new HashMap<>();
        refundParams.put("charge", chargeId);
        try {
            return Refund.create(refundParams);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new PaymentException("Exception on charging card for chargeId " + chargeId);
        }
    }

    public Customer retrieveCustomer(String customerId) {
        try {
            return Customer.retrieve(customerId);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new PaymentException("Exception on retrieving customer for customerId " + customerId);
        }
    }

    public Token retrieveToken(String stripeToken) {
        try {
            return Token.retrieve(stripeToken);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new PaymentException("Exception on retrieving token for stripeToken " + stripeToken);
        }
    }

    private ExternalAccountCollection getAllCards(String customerId) {
        Map<String, Object> cardParams = new HashMap<String, Object>();
        cardParams.put("object", "card");
        try {
            return Customer.retrieve(customerId).getSources().list(cardParams);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new PaymentException("Exception on retrieving all cards for customerId " + customerId);
        }
    }

    public void addCardToCustomer(Customer customer, String stripeToken) {
        Map<String, Object> params = new HashMap<>();
        params.put("source", stripeToken);
        try {
            customer.getSources().create(params);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new PaymentException("Exception on adding new card to customerId " + customer.getId());
        }
    }

    public void deleteCard(Customer customer, String cardId) {
        try {
            customer.getSources().retrieve(cardId).delete();
        } catch (StripeException e) {
            e.printStackTrace();
            throw new PaymentException("Exception on deleting card from customerId " + customer.getId());
        }
    }

    public List<CardDTO> getAllCards() {
        String stripeCustomerId = userService.getAuthenticatedUser().getStripeCustomerId();

        if (Objects.isNull(stripeCustomerId))
            return new ArrayList<>();
        return stripePaymentConverter.toDTOList(getAllCards(stripeCustomerId));
    }

    private void createInitCharge(User user, ChargeRequest chargeRequest, Lock lock) {
        Customer customer = createCustomer(chargeRequest.getStripeToken(), user.getFirstName() + " " + user.getLastName(), user.getEmail());

        charge(customer.getId(), chargeRequest.getAmount(), chargeRequest.getCurrency().name(), true, "Payment for lock number");
        if (!chargeRequest.getSaveCard()) { //remove card
            deleteCard(customer, chargeRequest.getCardId());
        }

        user.setStripeCustomerId(customer.getId());
        userRepository.save(user);
    }

    public void createCharge(ChargeRequest chargeRequest, Long lockId) {
        User user = userService.getAuthenticatedUser();

        Lock lock = lockRepository.getOne(lockId);

        chargeRequest.setAmount(lock.getLockTemplate().getPrice().getPrice().multiply(TO_CENT).intValue());

        if (Objects.isNull(user.getStripeCustomerId()))
            createInitCharge(user, chargeRequest, lock);
        else {
            Customer customer = retrieveCustomer(user.getStripeCustomerId());
            Token token = retrieveToken(chargeRequest.getStripeToken());

            boolean newCard = false;
            String cardId = hasCard(customer, token); // cardId is null if user doesn't have card
            if (cardId == null) {
                newCard = true;
                addCardToCustomer(customer, token.getId());
                cardId = selectCardIdByFingerprint(customer, token.getCard().getFingerprint());
            }
            chargeCard(customer.getId(), cardId,
                    chargeRequest.getAmount(), chargeRequest.getCurrency().name(),
                    true, "Payment for lock with number:" + lock.getId());

            if (!chargeRequest.getSaveCard() && newCard) {
                deleteCard(customer, chargeRequest.getCardId());
            }
        }
        lock.setPaid(true);
        lockRepository.save(lock);
    }

    //if user generate a new payment token check the card if it's a new one
    //same cards have different ids but have the same fingerprint
    //return the cardId if fingerprint matches else return null
    private String hasCard(Customer customer, Token token) {
        List<CardDTO> cardDTOS = getAllCards(customer.getId()).getData()
                .stream()
                .map(externalAccount -> stripePaymentConverter.toDTO(externalAccount))
                .collect(Collectors.toList());
        for (CardDTO cardDTO : cardDTOS) {
            if (cardDTO.getFingerprint().equals(token.getCard().getFingerprint())) {
                return cardDTO.getId();
            }
        }
        return null;
    }

    private String selectCardIdByFingerprint(Customer customer, String findgerprint) {
        List<CardDTO> cardDTOS = getAllCards(customer.getId()).getData()
                .stream()
                .map(externalAccount -> stripePaymentConverter.toDTO(externalAccount))
                .collect(Collectors.toList());
        for (CardDTO cardDTO : cardDTOS) {
            if (cardDTO.getFingerprint().equals(findgerprint)) {
                return cardDTO.getId();
            }
        }
        return null;
    }
}
