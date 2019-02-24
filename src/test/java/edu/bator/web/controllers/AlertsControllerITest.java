package edu.bator.web.controllers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;

import edu.bator.model.AlertSubscription;
import edu.bator.services.AlertsService;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.knowm.xchange.currency.CurrencyPair.BTC_USD;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(AlertsController.class)
class AlertsControllerITest {

    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final String AUTH = new String(Base64.getEncoder().encode("ubs:ubs_passwd".getBytes(UTF_8)), UTF_8);

    @Autowired
    private MockMvc mvc;

    @MockBean
    private Exchange exchange;

    @Mock
    private MarketDataService marketDataService;

    @Autowired
    private AlertsService alertsService;

    private SoftAssertions soft = new SoftAssertions();

    @BeforeEach
    void setUp() throws IOException {
        when(exchange.getMarketDataService()).thenReturn(marketDataService);
        when(exchange.getExchangeSymbols()).thenReturn(List.of(BTC_USD));
        when(marketDataService.getTicker(BTC_USD)).thenReturn(new Ticker.Builder().currencyPair(BTC_USD).last(TEN).build());
    }

    @AfterEach
    void tearDown() {
        alertsService.getAlertsDb().clear();
    }

    @Test
    void addAlert() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .put("/alert")
                .param(AlertsController.PAIR_QUERY_PARAM, "BTC_USD")
                .param(AlertsController.LIMIT_QUERY_PARAM, "10")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + AUTH)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        soft.assertThat(alertsService.getAlertsDb()).isNotEmpty();
        soft.assertThat(alertsService.getAlertsDb()).containsOnly(AlertSubscription.builder().limit(TEN).pair(BTC_USD).build());
    }

    @Test
    void removeAlert() throws Exception {
        alertsService.getAlertsDb().add(AlertSubscription.builder().limit(TEN).pair(BTC_USD).build());

        mvc.perform(MockMvcRequestBuilders
                .delete("/alert")
                .param(AlertsController.PAIR_QUERY_PARAM, "BTC_USD")
                .param(AlertsController.LIMIT_QUERY_PARAM, "10")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + AUTH)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        soft.assertThat(alertsService.getAlertsDb()).isEmpty();
    }
}