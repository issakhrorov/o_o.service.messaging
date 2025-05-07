package o_o.service.messaging.controller.ws

import o_o.service.messaging.dto.message.*
import o_o.service.messaging.service.ws.WSLimitHandler
import o_o.service.messaging.service.ws.message.IMessageService
import o_o.service.messaging.service.ws.message.MessageFacadeService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class WSChatController(
  val limitHandler: WSLimitHandler,
  val messageFacade: MessageFacadeService,
  val messageService: IMessageService
) {

//  private val logger = Logger.getLogger(WSChatController::class.java)

  @MessageMapping("/group_chat/send/{groupChatId}")
  @SendTo("/topic/messages.{groupChatId}")
  fun sendMessage(
    @Payload dto: MessageCreateUpdateDTO
  ): MessageInfoDTO? {
    if (!limitHandler.isAllowedToSendMessage(dto.account_id.toString()))
      // Reject the message if the rate limit is exceeded
      return null

    val result = try {
      messageFacade.createAndNotify(dto, null)
    } catch (ex: Exception) {
//      logger.error("Message wasn't sent to chat ${dto.chat_id ?: dto.chat_group_id}: ${ex.message}")
      null
    }

    return result?.let {
      if (dto.replies_to != null) {
        ChildMessageInfoDTO().toDTO(it).also { child ->
          child.replies_to_parent = result.parentMessageId
        }
      }
      else ParentMessageInfoDTO().toDTO(it)
    }
  }

  @MessageMapping("/group_chat/delete/{groupChatId}")
  @SendTo("/topic/messages/delete.{groupChatId}")
  fun deleteMessage(
    @Payload dto: MessageDeleteDTO
  ): MessageDeleteInfoDTO? {
    if (!limitHandler.isAllowedToSendMessage(dto.account_id.toString()))
    // Reject the message if the rate limit is exceeded
      return null

    return try {
      val message = messageService.delete(dto)
      message?.let { MessageDeleteInfoDTO(it.parentMessageId, it.id) }
    } catch (ex: Exception) {
//      logger.error("Couldn't delete message: #$dto.id, ${ex.message}")
      null
    }
  }

}