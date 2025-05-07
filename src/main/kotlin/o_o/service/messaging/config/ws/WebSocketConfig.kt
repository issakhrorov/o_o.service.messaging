package o_o.service.messaging.config.ws

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
  val authInterceptor: WSAuthInterceptor,
): WebSocketMessageBrokerConfigurer {

  override fun configureMessageBroker(config: MessageBrokerRegistry) {
    config.enableSimpleBroker("/topic", "/queue") // For group and private chats
    config.setApplicationDestinationPrefixes("/app") // Prefix for sending messages
  }

  override fun registerStompEndpoints(registry: StompEndpointRegistry) {
    registry
      .addEndpoint("/ws-native")
      .addInterceptors(authInterceptor)
      .setAllowedOrigins("*")

    registry
      .addEndpoint("/ws")
      .setAllowedOrigins("*")
      .addInterceptors(authInterceptor)
      .withSockJS()
  }
}
