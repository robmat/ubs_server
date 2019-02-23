package edu.bator.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import edu.bator.model.AlertSubscription;
import edu.bator.model.CurrentPrice;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AlertsService {

    private Set<AlertSubscription> alertsDb = new ConcurrentSkipListSet<>();

    private final Exchange exchange;

    @Autowired
    public AlertsService(Exchange exchange) {
        this.exchange = exchange;
    }

    public CurrentPrice addAlert(CurrencyPair pair, BigDecimal limit) throws IOException {
        log.debug("addAlert([{}] [{}] [{}])", pair, limit);

        alertsDb.add(AlertSubscription.builder().pair(pair).limit(limit).build());
        return currentPrice(pair);
    }

    public CurrentPrice removeAlert(CurrencyPair pair, BigDecimal limit) throws IOException {
        log.debug("removeAlert([{}] [{}] [{}])", pair, limit);

        alertsDb.remove(AlertSubscription.builder().pair(pair).limit(limit).build());
        return currentPrice(pair);
    }

    private CurrentPrice currentPrice(CurrencyPair pair) throws IOException {
        return Optional.ofNullable(exchange.getMarketDataService().getTicker(pair))
                .map(ticker -> CurrentPrice.builder().price(ticker.getLast()).currencyPair(pair).build())
                .orElse(null);
    }

    Set<AlertSubscription> getAlertsDb() {
        return alertsDb;
    }

    public void removeAlert(AlertSubscription alertSubscription) {
        try {
            removeAlert(alertSubscription.getPair(), alertSubscription.getLimit());
        } catch (IOException e) {
            log.error("Unable to remove alert due to: ", e);
        }
    }
}
