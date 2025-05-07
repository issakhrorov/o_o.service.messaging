package o_o.service.messaging.service.ws.message

import o_o.service.messaging.NotificationType
import o_o.service.messaging.base.exception.CustomBadRequestException
import o_o.service.messaging.base.exception.LoggedError
import o_o.service.messaging.dto.message.MessageCreateUpdateDTO
import o_o.service.messaging.external.auth.AuthService
import o_o.service.messaging.external.notification.NotificationCreateDTO
import o_o.service.messaging.external.notification.ActiveUsersService
import o_o.service.messaging.external.notification.NotificationService
import o_o.service.messaging.external.notification.NotificationSettingsService
import o_o.service.messaging.model.message.Message
import o_o.service.messaging.service.ws.WSLimitHandler
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class MessageFacadeService(
  private val messageService: IMessageService,
  private val asyncComponent: MessageFacadeServiceComponent,
) {
  fun createAndNotify(dto: MessageCreateUpdateDTO, principal: Principal?): Message {
    val message = messageService.createUpdate(dto)
    asyncComponent.notify(message, dto.account_id)

    return message
  }
}

@Component
class MessageFacadeServiceComponent(
  private val activeUserService: ActiveUsersService,
  private val messageService: IMessageService,
  private val notificationService: NotificationService,
  private val wsLimitHandler: WSLimitHandler,
  private val notificationSettingsService: NotificationSettingsService,
  private val authService: AuthService,
) {
  @Async("threadPoolTaskExecutor")
  fun notify(message: Message, senderId: Long){
    val notifications = prepareNotifications(message, senderId)
    notifications.let { (sseNotification, firebaseNotification) ->
      notificationService.sendToFirebase(firebaseNotification)
      notificationService.sendViaSSE(sseNotification)
    }
  }

  private fun prepareNotifications(message: Message, senderId: Long): NotificationsToSend {
    val notificationBody = prepareNotificationBody(message)
    val chatId = getChatIdFromMessage(message)

    val currentActiveUsers = activeUserService.getAllActiveUsers()
    val sseNotificationReceivers = notificationBody.receivers
      ?.filter { it !in currentActiveUsers }
      ?: emptyList()
    val sseNotification = NotificationCreateDTO(
      notificationBody.title,
      message.message ?: message.media!!,
      sseNotificationReceivers,
      chatId,
      NotificationType.CHAT_MESSAGE
    )

    val firebaseNotificationReceivers = notificationSettingsService.getAllNotMutedByUsersAndComponent(
      sseNotificationReceivers,
      NotificationType.CHAT_MESSAGE,
      chatId
    )

    val firebaseNotification = sseNotification.apply {
      user_ids = user_ids.filter { it in firebaseNotificationReceivers }
    }

    return NotificationsToSend(sseNotification, firebaseNotification)
  }

  private fun getChatIdFromMessage(message: Message): String {
    val parentMessage = message.parentMessageId?.let { messageService.getById(it) }

    return message.chat?.id
      ?: parentMessage?.chat?.id
      ?: ""
  }

  private fun prepareNotificationBody(message: Message): NotificationBody {
    var receivers: List<Long>?
    val title: String

    val chat = message.chat
    val receiverId = if (chat?.accountOneId == message.senderAccountId) chat.accountTwoId else chat?.accountOneId
      ?: throw LoggedError(CustomBadRequestException("Chat not found"))

    val receiver = authService.getById(receiverId)
    title = receiver.fullname
    receivers = listOf(receiverId)

    val activeUsers = wsLimitHandler.connectionsMap.keys().toList()
    receivers = receivers.filter { it.toString() !in activeUsers }

    return NotificationBody(title, receivers)
  }

  data class NotificationsToSend(
    val sseNotification: NotificationCreateDTO,
    val firebaseNotification: NotificationCreateDTO
  )

  data class NotificationBody(
    val title: String,
    val receivers: List<Long>?
  )
}