package edu.bator.web.controllers;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.ws.rs.Path;
import java.io.IOException;
import java.math.BigDecimal;

import edu.bator.model.CurrentPrice;
import edu.bator.services.AlertsService;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Validated
@RequestMapping(value = AlertsController.ALERT_PATH)
public class AlertsController {

    static final String ALERT_PATH = "/alert";
    public static final String ALERTS_PATH = "/alerts";
    private static final String PAIR_QUERY_PARAM = "pair";
    private static final String LIMIT_QUERY_PARAM = "limit";
    public static final String TOPIC_PATH = "/topic";

    private final AlertsService alertsService;

    @Autowired
    public AlertsController(AlertsService alertsService) {
        this.alertsService = alertsService;
    }

    @CrossOrigin
    @PutMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CurrentPrice addAlert(@RequestParam(name = PAIR_QUERY_PARAM) CurrencyPair pair,
                                 @Valid @Positive @RequestParam(name = LIMIT_QUERY_PARAM) BigDecimal limit) throws IOException {
        log.debug("addAlert([{}] [{}])", pair, limit);
        return alertsService.addAlert(pair, limit);
    }

    @CrossOrigin
    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CurrentPrice removeAlert(@RequestParam(name = PAIR_QUERY_PARAM) CurrencyPair pair,
                            @Valid @Positive @RequestParam(name = LIMIT_QUERY_PARAM) BigDecimal limit) throws IOException {
        log.debug("removeAlert([{}] [{}])", pair, limit);
        return alertsService.removeAlert(pair, limit);
    }
}
