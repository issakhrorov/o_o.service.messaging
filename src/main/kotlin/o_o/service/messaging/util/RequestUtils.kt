package o_o.service.messaging.util

import org.springframework.http.server.ServletServerHttpRequest

fun getTokenFromRequest(request: ServletServerHttpRequest) =
  request.headers.getFirst("Authorization")?.replace("Bearer ", "") ?: ""