package o_o.service.messaging.service.ws.message

import o_o.service.messaging.dto.message.MessageCreateUpdateDTO
import o_o.service.messaging.dto.message.MessageDeleteDTO
import o_o.service.messaging.model.message.Message
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IMessageService {
  fun createUpdate(dto: MessageCreateUpdateDTO): Message
  fun getById(id: String): Message

  fun getAll(chatId: String): List<Message>
  fun getAll(chatId: String, pageable: Pageable): Page<Message>

  fun getAllReplied(repliesTo: String): List<Message>

  fun delete(dto: MessageDeleteDTO): Message?
  fun delete(message: Message, userId: String?)
}