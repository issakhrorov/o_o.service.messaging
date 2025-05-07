package o_o.service.messaging.service.ws

import o_o.service.messaging.base.exception.CustomBadRequestException
import o_o.service.messaging.base.exception.LoggedError
import org.springframework.context.ApplicationListener
import org.springframework.context.event.EventListener
import org.springframework.messaging.Message
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class WebSocketPresenceTracker(
  val limitHandler: WSLimitHandler
): ApplicationListener<SessionConnectedEvent> {

  override fun onApplicationEvent(event: SessionConnectedEvent) {
    val sessionId = event.message.headers["simpSessionId"] as String
    val userId = getUserIdFromEvent(event)
    limitHandler.addConnection(userId, sessionId)
  }

  @EventListener
  fun onDisconnect(event: SessionDisconnectEvent) {
    val sessionId = event.message.headers["simpSessionId"] as String
    limitHandler.removeConnection(sessionId)
  }

  private fun getUserIdFromEvent(event: AbstractSubProtocolEvent): String {
    val accessor = StompHeaderAccessor.wrap(event.message)
    val accessorHeaders = accessor.getHeader("simpConnectMessage") as Message<Any?>
    val nativeHeaders = accessorHeaders.headers["nativeHeaders"] as Map<String, Any?>
    val userIdAsList = nativeHeaders["userId"] as List<String>

    return userIdAsList.firstOrNull()
      ?: throw LoggedError(CustomBadRequestException("User ID is not present in session attributes"))
  }

  private fun updateUserLastSeen(username: String) {
    // Implement logic to update the user's last seen timestamp in your database
    println("Updated last seen for $username")
  }
}