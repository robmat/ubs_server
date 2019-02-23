package edu.bator.services;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Set;

import edu.bator.model.AlertSubscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowm.xchange.currency.CurrencyPair;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.knowm.xchange.currency.CurrencyPair.BTC_USD;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertsServiceTest {

    private static final String USER_NAME = "user";

    private AlertsService alertsService = new AlertsService();

    @Mock
    Authentication authentication;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Should add new alert to new user")
    void addAlertNewUser() {
        //with
        CurrencyPair pair = BTC_USD;
        BigDecimal limit = BigDecimal.ONE;

        //when
        alertsService.addAlert(authentication, pair, limit);

        //then
        AlertSubscription expected = AlertSubscription.builder().pair(pair).limit(limit).build();
        assertThat(alertsService.getAlertsDb()).containsOnly(expected);
    }

    @Test
    @DisplayName("Should add new alert to existing user with alerts and sort ascending")
    void addAlertExistingUser() {
        //with
        CurrencyPair pair = BTC_USD;
        BigDecimal limit = BigDecimal.ONE;
        BigDecimal secondLimit = BigDecimal.TEN;

        //when
        alertsService.addAlert(authentication, pair, secondLimit);
        alertsService.addAlert(authentication, pair, limit);

        //then
        AlertSubscription alertOne = AlertSubscription.builder().pair(pair).limit(limit).build();
        AlertSubscription alertTen = AlertSubscription.builder().pair(pair).limit(secondLimit).build();

        assertThat(alertsService.getAlertsDb()).containsSequence(alertOne, alertTen);
    }

    @Test
    @DisplayName("Should remove existing alert")
    void removeAlert() {
        //with
        CurrencyPair pair = BTC_USD;
        BigDecimal limit = BigDecimal.ONE;
        alertsService.addAlert(authentication, pair, limit);

        //when
        alertsService.removeAlert(authentication, pair, limit);

        //then
        assertThat(alertsService.getAlertsDb()).isEmpty();
    }
}