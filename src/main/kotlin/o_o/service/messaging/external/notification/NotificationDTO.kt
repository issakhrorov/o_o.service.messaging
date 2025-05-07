package o_o.service.messaging.external.notification

import o_o.service.messaging.NotificationType

data class NotificationCreateDTO(
  val title: String,
  val content: String,
  var user_ids: List<Long>,
  val component_id: String? = null,
  val type: NotificationType
)