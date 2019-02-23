package edu.bator.services;

import java.util.Objects;

import edu.bator.model.AlertNotification;
import edu.bator.model.AlertSubscription;
import edu.bator.web.controllers.WebsocketController;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static edu.bator.web.controllers.AlertsController.ALERTS_PATH;
import static edu.bator.web.controllers.WebsocketController.TOPIC_PATH;

@Service
@Slf4j
public class PriceChangeNotificationService {

    private final AlertsService alertsService;

    private final SimpMessagingTemplate template;

    @Autowired
    public PriceChangeNotificationService(AlertsService alertsService, SimpMessagingTemplate template) {
        this.alertsService = alertsService;
        this.template = template;
    }

    @Async
    public void checkAndNotifyClients(Ticker ticker) {
        log.debug("checkAndNotifyClients([{}])", ticker);
        for (AlertSubscription alertSubscription : alertsService.getAlertsDb()) {
            if (Objects.equals(ticker.getCurrencyPair(), alertSubscription.getPair())
                    && ticker.getLast().compareTo(alertSubscription.getLimit()) > 0) {
                log.debug("alert hit {}", alertSubscription);
                template.convertAndSend(TOPIC_PATH + ALERTS_PATH, new AlertNotification(alertSubscription));
                alertsService.removeAlert(alertSubscription);
            }
        }
    }
}
