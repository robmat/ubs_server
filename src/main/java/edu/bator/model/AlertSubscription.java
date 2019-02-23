package edu.bator.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import org.knowm.xchange.currency.CurrencyPair;

@Data
@Builder
public class AlertSubscription implements Comparable<AlertSubscription> {
    private CurrencyPair pair;
    private BigDecimal limit;

    @Override
    public int compareTo(AlertSubscription o) {
        return this.limit.compareTo(o.limit);
    }
}
