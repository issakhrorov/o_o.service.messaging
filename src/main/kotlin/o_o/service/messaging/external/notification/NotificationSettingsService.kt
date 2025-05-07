package o_o.service.messaging.external.notification

import o_o.service.messaging.NotificationType
import org.springframework.stereotype.Component

@Component
class NotificationSettingsService {
  fun getAllNotMutedByUsersAndComponent(
    userIds: List<Long>,
    notificationType: NotificationType,
    componentId: String
  ): List<Long> {
    return emptyList()
  }
}