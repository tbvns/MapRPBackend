package xyz.tbvns.rpmap;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Enables WebSocket message handling, backed by a message broker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple in-memory message broker
        // Messages with destinations starting with "/topic" or "/queue" will be routed to the broker
        config.enableSimpleBroker("/topic", "/queue");

        // Set the application destination prefixes.
        // Messages from clients to the server should be prefixed with "/app".
        // For example, a message sent to "/app/chat" will be routed to a @MessageMapping method.
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register a STOMP endpoint. This is the URL that clients will connect to.
        // The `withSockJS()` enables SockJS fallback options for browsers that don't support native WebSockets.
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}