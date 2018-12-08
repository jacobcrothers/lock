package binar.box.converter;

import binar.box.dto.payment.CardDTO;
import binar.box.util.Exceptions.PaymentException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.ExternalAccount;
import com.stripe.model.ExternalAccountCollection;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StripePaymentConverter {
    public CardDTO toDTO(ExternalAccount externalAccount) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> externalAccountMap;
        try {
            externalAccountMap = mapper.readValue(externalAccount.toJson(), new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            e.printStackTrace(); //TODO throw proper exception
            throw new PaymentException("Account list error");
        }

        CardDTO cardDTO = new CardDTO();
        cardDTO.setId(externalAccountMap.get("id").toString());
        cardDTO.setExpMonth(externalAccountMap.get("exp_month").toString());
        cardDTO.setExpYear(externalAccountMap.get("exp_year").toString());
        cardDTO.setName(externalAccountMap.get("name") != null ? externalAccountMap.get("name").toString() : "");
        cardDTO.setLast4(externalAccountMap.get("last4").toString());
        cardDTO.setFingerprint(externalAccountMap.get("fingerprint").toString());

        return cardDTO;
    }

    public List<CardDTO> toDTOList(ExternalAccountCollection allCards){
        return allCards.getData().stream().map(this::toDTO).collect(Collectors.toList());
    }
}
