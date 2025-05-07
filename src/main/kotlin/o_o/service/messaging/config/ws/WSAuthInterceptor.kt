package o_o.service.messaging.config.ws

import o_o.service.messaging.base.exception.LoggedError
import o_o.service.messaging.constant.ConnectionConstants.WEB_SOCKET_AUTH_HEADER_KEY
import o_o.service.messaging.constant.ConnectionConstants.WEB_SOCKET_AUTH_HEADER_KEY_MOBILE
import o_o.service.messaging.external.auth.AuthService
import o_o.service.messaging.service.ws.WSLimitHandler
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeFailureException
import org.springframework.web.socket.server.HandshakeInterceptor

@Component
class WSAuthInterceptor(
  val authService: AuthService,
  val limitHandler: WSLimitHandler
): HandshakeInterceptor {

  override fun beforeHandshake(
    request: ServerHttpRequest,
    response: ServerHttpResponse,
    wsHandler: WebSocketHandler,
    attributes: MutableMap<String, Any>
  ): Boolean {
    if (limitHandler.connectionsMap.size >= limitHandler.maxConnections){
      throw LoggedError(HandshakeFailureException("Too many connections"))
    }

    val token = getToken(request, response)

    try {
      val userId = authService.getByToken(token).toString()
      val currentTimestamp = System.currentTimeMillis()

      val userConnection = limitHandler.connectionsMap[userId]
      val lastMessageTimestamp = userConnection?.lastActionTimestamp
      val connections = userConnection?.connections

      // Reject the connection if the rate limit is exceeded
      if (lastMessageTimestamp != null && (currentTimestamp.minus(lastMessageTimestamp ?: 0L) < limitHandler.messageTimeout))
        throw LoggedError(HandshakeFailureException("Too many connections"))

      // Reject the connection limit per user is exceeded
      if (connections != null && connections.size >= limitHandler.maxConnectionsPerUser)
        throw LoggedError(HandshakeFailureException("Too many connections"))

      authService.getById(userId.toLong())
    } catch (e: Exception) {
      throw LoggedError(HandshakeFailureException("Invalid token"))
    }

    return true
  }

  override fun afterHandshake(
    request: ServerHttpRequest,
    response: ServerHttpResponse,
    wsHandler: WebSocketHandler,
    exception: Exception?
  ) {
    // Do nothing
  }

  private fun getToken(request: ServerHttpRequest, response: ServerHttpResponse): String {
    request.headers.getFirst(WEB_SOCKET_AUTH_HEADER_KEY_MOBILE)?.trim()?.let {
      return it
    }
    request.headers.getFirst(WEB_SOCKET_AUTH_HEADER_KEY)?.trim()?.let {
      // set same header on response manually
      setHeaderOnWebConnectionResponse(request, response)
      return it
    }

    throw LoggedError(HandshakeFailureException("Missing token"))
  }

  private fun setHeaderOnWebConnectionResponse(request: ServerHttpRequest, response: ServerHttpResponse) {
    val headers = request.headers
    val protocols = headers[WEB_SOCKET_AUTH_HEADER_KEY]

    if (!protocols.isNullOrEmpty()) {
      response.headers.add(WEB_SOCKET_AUTH_HEADER_KEY, protocols.first()) // Echo back protocol
    }
  }
}