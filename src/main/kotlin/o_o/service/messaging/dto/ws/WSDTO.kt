package o_o.service.messaging.dto.ws

import java.util.concurrent.CopyOnWriteArraySet

data class UserConnectionsAndLastActionMap(
  var connections: CopyOnWriteArraySet<String>,
  var lastActionTimestamp: Long
)