package o_o.service.messaging.service.ws.message

import o_o.service.messaging.base.exception.LoggedError
import o_o.service.messaging.dto.message.MessageCreateUpdateDTO
import o_o.service.messaging.dto.message.MessageDeleteDTO
import o_o.service.messaging.model.message.Message
import o_o.service.messaging.repo.MessageRepo
import o_o.service.messaging.service.ws.chat.IChatService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class MessageService(
  val mainRepo: MessageRepo,
  val chatService: IChatService,
): IMessageService {

  override fun createUpdate(dto: MessageCreateUpdateDTO): Message {
    if (dto.media == null && dto.message == null)
      throw LoggedError(IllegalArgumentException("Message or media must be provided"))

    // Create Reply message only
    dto.replies_to?.let {
      if (dto.replies_to == dto.id)
        throw LoggedError(IllegalArgumentException("Message can not reply to itself!"))

      val repliesTo = getById(it)
      val parent = repliesTo.parentMessageId?.let { parentId -> getById(parentId) }
        ?: repliesTo
      val replyingToParent = parent == repliesTo

      val message = reply(dto, replyingToParent)
      message.parentMessageId = parent.id
      parent.replies += message

      return message
    }

    var entity = dto.id?.let { getById(it) } ?: Message()
    entity.message = dto.message
    entity.media = dto.media
    entity.senderAccountId = dto.account_id

    val chat = chatService.getById(dto.chat_id)
    chat.messages += entity
    entity.chat = chat

    entity = mainRepo.save(entity)

    return entity
  }

  override fun getById(id: String) =
    mainRepo.findByIdAndDeletedFalse(id)

  override fun getAll(chatId: String) =
    mainRepo.findAllByChatIdAndDeletedFalse(chatId)

  override fun getAll(chatId: String, pageable: Pageable) =
    mainRepo.findAllByChatIdAndDeletedFalseOrderByIdDesc(chatId, pageable)

  override fun getAllReplied(repliesTo: String) =
    mainRepo.findAllByRepliesToIdAndDeletedFalse(repliesTo)

  override fun delete(dto: MessageDeleteDTO): Message? {
    val message = getById(dto.id)
    if (message.senderAccountId != dto.account_id)
      return null

    removeFromParent(message)
    removeReferenceFromRepliedMessages(message)

    delete(message, null)

    return message
  }

  override fun delete(message: Message, userId: String?) {
    message.deleted = true
    message.modifiedByUser = userId

    mainRepo.save(message)
  }

  private fun reply(dto: MessageCreateUpdateDTO, replyingToParent: Boolean): Message {
    var entity = dto.id?.let { getById(it) } ?: Message()
    entity.message = dto.message
    entity.media = dto.media
    entity.senderAccountId = dto.account_id
    entity.repliesTo = if (replyingToParent) null else dto.replies_to?.let { getById(it) }

    entity = mainRepo.save(entity)

    return entity
  }

  private fun removeFromParent(message: Message) {
    val parent = message.parentMessageId?.let { getById(it) }
    parent?.let {
      it.replies.remove(message)
      mainRepo.save(it)
    }
  }

  private fun removeReferenceFromRepliedMessages(message: Message) {
    val childMessages = getAllReplied(message.id).map {
      it.repliesTo = null
      it
    }
    mainRepo.saveAll(childMessages)
  }
}