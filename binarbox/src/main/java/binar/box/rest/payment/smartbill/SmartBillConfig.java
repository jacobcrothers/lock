package binar.box.rest.payment.smartbill;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class SmartBillConfig {


    @Bean
    SmartBillService gateway(SmartBillProperties properties) {
        return new SmartBillService(
                properties.getApiURL(),
                properties.getToken(),
                properties.getUsername()
        );
    }

    @Data
    @Component
    @ConfigurationProperties(prefix = "smartbill")
    private static class SmartBillProperties {
        private String username;
        private String token;
        private String apiURL;
    }
}

