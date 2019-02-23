package edu.bator.services;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import edu.bator.model.AlertSubscription;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AlertsService {

    private Set<AlertSubscription> alertsDb = new ConcurrentSkipListSet<>();

    public void addAlert(Authentication authentication, CurrencyPair pair, BigDecimal limit) {
        log.debug("addAlert([{}] [{}] [{}])", authentication, pair, limit);

        alertsDb.add(AlertSubscription.builder().pair(pair).limit(limit).build());
    }

    public void removeAlert(Authentication authentication, CurrencyPair pair, BigDecimal limit) {
        log.debug("removeAlert([{}] [{}] [{}])", authentication, pair, limit);

        alertsDb.remove(AlertSubscription.builder().pair(pair).limit(limit).build());
    }

    Set<AlertSubscription> getAlertsDb() {
        return alertsDb;
    }
}
