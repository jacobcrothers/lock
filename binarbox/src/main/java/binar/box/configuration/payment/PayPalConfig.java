package binar.box.configuration.payment;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayPalConfig {
    @Value("${paypal.public.key}")
    private String publicKey;

    @Value("${paypal.private.key}")
    private String privateKey;

    @Value("${paypal.merchant.id}")
    private String merchantId;

    @Bean
    public BraintreeGateway createBraintreeGateway() {
        return new BraintreeGateway(
                Environment.SANDBOX,
                merchantId,
                publicKey,
                privateKey);
    }
}
