package o_o.service.messaging.service.ws

import o_o.service.messaging.dto.ws.UserConnectionsAndLastActionMap
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet

//@Slf4j
@Component
class WSLimitHandler {

  @Value("\${websocket.maxConnections}")
  val maxConnections = 0
  @Value("\${websocket.maxConnections.user}")
  val maxConnectionsPerUser = 0
  @Value("\${websocket.messageTimeout}")
  val messageTimeout = 0L

  val connectionsMap = ConcurrentHashMap<String, UserConnectionsAndLastActionMap>()
  val sessionsUserMap = ConcurrentHashMap<String, String>()

//  val logger = Logger.getLogger(WSLimitHandler::class.java)

  // Increment active connections count (if successful connection)
  fun addConnection(userId: String, sessionId: String) {
    connectionsMap.compute(userId) { _, value ->
      value?.apply {
        connections.add(sessionId)
        lastActionTimestamp = System.currentTimeMillis()
      }
        ?: UserConnectionsAndLastActionMap(CopyOnWriteArraySet(listOf(sessionId)), System.currentTimeMillis())
    }

    sessionsUserMap[sessionId] = userId

    val activeConnections = connectionsMap.values.sumOf { it.connections.size }
    val userConnections = connectionsMap[userId]

//    logger.info("User Connected: $userId, User's active connection: ${userConnections?.connections?.size}")
//    logger.info("Active connections: $activeConnections")
  }

  fun isAllowedToSendMessage(userId: String): Boolean {
    val currentTimestamp = System.currentTimeMillis()
    val lastMessageTimestamp = connectionsMap[userId]?.lastActionTimestamp
    val allowed = lastMessageTimestamp != null && (currentTimestamp - lastMessageTimestamp > messageTimeout)
    if (!allowed) {
//      logger.warn("Rate limit exceeded for $userId")
      return false
    }
    connectionsMap.compute(userId) { _, value ->
      value?.apply { lastActionTimestamp = currentTimestamp }
    }
    return true
  }

  fun removeConnection(sessionId: String) {
    val userId = sessionsUserMap.remove(sessionId)
    val userConnections = connectionsMap[userId]

    userConnections?.let {
      it.connections.remove(sessionId)
      if (it.connections.size == 0) {
        connectionsMap.remove(userId)
      }
    }

    val activeConnections = connectionsMap.values.sumOf { it.connections.size }
//    logger.info("User Disconnect: $userId, User's active connection: ${userConnections?.connections?.size ?: 0}")
//    logger.info("Active connections: $activeConnections")
  }
}