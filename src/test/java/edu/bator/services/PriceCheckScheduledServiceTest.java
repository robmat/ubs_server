package edu.bator.services;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.knowm.xchange.currency.CurrencyPair.BTC_USD;
import static org.knowm.xchange.currency.CurrencyPair.ETH_CNY;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceCheckScheduledServiceTest {

    PriceCheckScheduledService priceCheckScheduledService;

    @Mock
    Exchange exchange;

    @Mock
    PriceChangeNotificationService priceChangeNotificationService;

    @Mock
    MarketDataService marketDataService;

    @BeforeEach
    void setUp() {
        priceCheckScheduledService = new PriceCheckScheduledService(exchange, priceChangeNotificationService);
    }

    @Test
    @DisplayName("Notify PriceChangeNotificationService about new ticker on supported pair")
    void checkPriceForSupportedPair() throws IOException {
        when(exchange.getMarketDataService()).thenReturn(marketDataService);
        when(exchange.getExchangeSymbols()).thenReturn(List.of(BTC_USD));

        Ticker ticker = new Ticker.Builder().currencyPair(BTC_USD).build();
        when(marketDataService.getTicker(BTC_USD)).thenReturn(ticker);

        priceCheckScheduledService.checkPrice();

        verify(priceChangeNotificationService).checkAndNotifyClients(ticker);
    }

    @Test
    @DisplayName("Do not notify PriceChangeNotificationService about new ticker on unsupported pair")
    void checkPriceForUnsupportedPair() throws IOException {
        when(exchange.getMarketDataService()).thenReturn(marketDataService);
        when(exchange.getExchangeSymbols()).thenReturn(List.of(ETH_CNY));

        priceCheckScheduledService.checkPrice();

        verifyZeroInteractions(priceChangeNotificationService);
    }
}