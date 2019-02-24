package edu.bator.services;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import edu.bator.model.AlertSubscription;
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

    public void addAlert(CurrencyPair pair, BigDecimal limit) {
        log.debug("addAlert([{}] [{}])", pair, limit);

        alertsDb.add(AlertSubscription.builder().pair(pair).limit(limit).build());
    }

    public void removeAlert(CurrencyPair pair, BigDecimal limit) {
        log.debug("removeAlert([{}] [{}])", pair, limit);

        alertsDb.remove(AlertSubscription.builder().pair(pair).limit(limit).build());
    }

    public Set<AlertSubscription> getAlertsDb() {
        return alertsDb;
    }

    public void removeAlert(AlertSubscription alertSubscription) {
        removeAlert(alertSubscription.getPair(), alertSubscription.getLimit());
    }
}
