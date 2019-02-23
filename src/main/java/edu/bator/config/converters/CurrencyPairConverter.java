package edu.bator.config.converters;

import javax.annotation.PostConstruct;

import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CurrencyPairConverter implements Converter<String, CurrencyPair> {

    @Override
    public CurrencyPair convert(String source) {
        if (source.contains("-")) {
            String[] split = source.split("-");
            return new CurrencyPair(split[0], split[1]);
        } else {
            throw new IllegalArgumentException("unrecognized currency pair: " + source);
        }
    }
}