package edu.bator.config.websockets;

import edu.bator.web.controllers.AlertsController;
import edu.bator.web.controllers.WebsocketController;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final String WEBSOCKET_ENDPOIINT = "/websocket-endpoint";

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(WebsocketController.TOPIC_PATH);
        config.setApplicationDestinationPrefixes(AlertsController.ALERTS_PATH);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint(WEBSOCKET_ENDPOIINT)
                .setAllowedOrigins("*")
                .withSockJS();
    }

}