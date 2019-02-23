package edu.bator.services;

import java.io.IOException;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static org.knowm.xchange.currency.CurrencyPair.BTC_USD;

@Service
@Slf4j
public class PriceCheckScheduledService {

    private Set<CurrencyPair> supportedPairs = Set.of(BTC_USD);

    private final Exchange exchange;

    private final PriceChangeNotificationService priceChangeNotificationService;

    @Autowired
    public PriceCheckScheduledService(Exchange exchange,
                                      PriceChangeNotificationService priceChangeNotificationService) {
        this.exchange = exchange;
        this.priceChangeNotificationService = priceChangeNotificationService;
    }

    @Scheduled(fixedRateString = "${check.price.rate}")
    public void checkPrice() throws IOException {
        log.debug("checkPrice() starts");

        MarketDataService marketDataService = exchange.getMarketDataService();

        for (CurrencyPair currencyPair : supportedPairs) {
            if (exchange.getExchangeSymbols().contains(currencyPair)) {
                Ticker ticker = marketDataService.getTicker(currencyPair);
                priceChangeNotificationService.checkAndNotifyClients(ticker);
            } else {
                log.warn("exchange [{}] does not support [{}]", exchange, currencyPair);
            }
        }

        log.debug("checkPrice() ends");
    }
}
