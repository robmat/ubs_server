package edu.bator.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knowm.xchange.currency.CurrencyPair;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlertNotification {
    private CurrencyPair currencyPair;
    private BigDecimal limit;
    LocalDateTime time = LocalDateTime.now();

    public AlertNotification(AlertSubscription alertSubscription) {
        currencyPair = alertSubscription.getPair();
        limit = alertSubscription.getLimit();
    }
}
