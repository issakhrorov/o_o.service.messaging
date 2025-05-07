package o_o.service.messaging.service.ws.chat

import o_o.service.messaging.dto.chat.ChatCreateUpdateDTO
import o_o.service.messaging.model.chat.Chat
import org.springframework.http.server.ServletServerHttpRequest

interface IChatService {
  fun createUpdate(dto: ChatCreateUpdateDTO, request: ServletServerHttpRequest): Chat
  fun getById(id: String): Chat
  fun getAllByUser(userId: Long): List<Chat>
  fun delete(entity: Chat, userId: String?)
}