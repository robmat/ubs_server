package edu.bator.config.exchanges;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.gemini.v1.GeminiExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeConfig {

    @Bean
    public Exchange exchange() {
        return ExchangeFactory.INSTANCE.createExchange(GeminiExchange.class.getName());
    }
}
