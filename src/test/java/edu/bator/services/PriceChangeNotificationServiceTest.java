package edu.bator.services;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.bator.model.AlertNotification;
import edu.bator.model.AlertSubscription;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.knowm.xchange.currency.CurrencyPair.BTC_USD;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceChangeNotificationServiceTest {

    private PriceChangeNotificationService priceChangeNotificationService;

    @Mock
    private AlertsService alertsService;

    @Mock
    private SimpMessagingTemplate template;

    @BeforeEach
    void setUp() {
        priceChangeNotificationService = new PriceChangeNotificationService(alertsService, template);
    }

    @Test
    @DisplayName("Should send two notification for two limits passed ")
    void checkAndNotifyClients() {
        //with
        Set<AlertSubscription> alertSubscriptions = new TreeSet<>();
        alertSubscriptions.add(AlertSubscription.builder().pair(BTC_USD).limit(ZERO).build());
        alertSubscriptions.add(AlertSubscription.builder().pair(BTC_USD).limit(TEN).build());
        alertSubscriptions.add(AlertSubscription.builder().pair(BTC_USD).limit(ONE).build());
        when(alertsService.getAlertsDb()).thenReturn(alertSubscriptions);

        //when
        Ticker ticker = new Ticker.Builder().last(new BigDecimal("1.5")).currencyPair(BTC_USD).build();
        priceChangeNotificationService.checkAndNotifyClients(ticker);

        //then
        ArgumentCaptor<AlertNotification> alertNotification = ArgumentCaptor.forClass(AlertNotification.class);
        verify(template, times(2)).convertAndSend(eq("/topic/alerts"), alertNotification.capture());
        assertThat(alertNotification.getAllValues()).hasSize(2);
        assertThat(alertNotification.getAllValues().get(0).getCurrencyPair()).isEqualTo(BTC_USD);
        assertThat(alertNotification.getAllValues().get(1).getCurrencyPair()).isEqualTo(BTC_USD);
        assertThat(alertNotification.getAllValues().get(0).getLimit()).isEqualTo(ZERO);
        assertThat(alertNotification.getAllValues().get(1).getLimit()).isEqualTo(ONE);
    }
}
