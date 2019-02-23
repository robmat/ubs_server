package edu.bator.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import org.knowm.xchange.currency.CurrencyPair;

@Data
@Builder
public class CurrentPrice {
    private BigDecimal price;
    private CurrencyPair currencyPair;
}
