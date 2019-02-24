package edu.bator.services;

import java.io.IOException;
import java.math.BigDecimal;

import edu.bator.model.AlertSubscription;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.knowm.xchange.currency.CurrencyPair.BTC_USD;

@ExtendWith(MockitoExtension.class)
class AlertsServiceTest {

    private static final String USER_NAME = "user";

    @Mock
    Exchange exchange;

    private AlertsService alertsService = new AlertsService(exchange);

    private SoftAssertions soft = new SoftAssertions();

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Should add new alert to new user")
    void addAlertNewUser() throws IOException {
        CurrencyPair pair = BTC_USD;
        BigDecimal limit = BigDecimal.ONE;

        alertsService.addAlert(pair, limit);

        AlertSubscription expected = AlertSubscription.builder().pair(pair).limit(limit).build();
        soft.assertThat(alertsService.getAlertsDb()).containsOnly(expected);
    }

    @Test
    @DisplayName("Should add new alert to existing user with alerts and sort ascending")
    void addAlertExistingUser() throws IOException {
        CurrencyPair pair = BTC_USD;
        BigDecimal limit = BigDecimal.ONE;
        BigDecimal secondLimit = BigDecimal.TEN;

        alertsService.addAlert(pair, secondLimit);
        alertsService.addAlert(pair, limit);

        AlertSubscription alertOne = AlertSubscription.builder().pair(pair).limit(limit).build();
        AlertSubscription alertTen = AlertSubscription.builder().pair(pair).limit(secondLimit).build();

        soft.assertThat(alertsService.getAlertsDb()).containsSequence(alertOne, alertTen);
    }

    @Test
    @DisplayName("Should remove existing alert")
    void removeAlert() throws IOException {
        CurrencyPair pair = BTC_USD;
        BigDecimal limit = BigDecimal.ONE;
        alertsService.addAlert(pair, limit);

        alertsService.removeAlert(pair, limit);

        soft.assertThat(alertsService.getAlertsDb()).isEmpty();
    }
}