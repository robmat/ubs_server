package edu.bator.web.controllers;

import edu.bator.model.AlertNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import static edu.bator.web.controllers.AlertsController.ALERTS_PATH;

@Controller
@Slf4j
public class WebsocketController {

    public static final String TOPIC_PATH = "/topic";

    @MessageMapping(ALERTS_PATH)
    @SendTo(TOPIC_PATH + ALERTS_PATH)
    public AlertNotification sendAlert() {
        log.debug("alert sent");
        return null;
    }
}
