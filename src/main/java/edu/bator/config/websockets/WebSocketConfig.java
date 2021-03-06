package edu.bator.config.websockets;

import edu.bator.web.controllers.AlertsController;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    public static final String WEBSOCKET_ENDPOIINT = "/websocket-endpoint";

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint(WEBSOCKET_ENDPOIINT)
                .setAllowedOrigins("*")
                .withSockJS();
    }

}