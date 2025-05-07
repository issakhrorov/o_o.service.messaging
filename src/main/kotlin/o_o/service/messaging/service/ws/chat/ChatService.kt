package o_o.service.messaging.service.ws.chat

import o_o.service.messaging.dto.chat.ChatCreateUpdateDTO
import o_o.service.messaging.external.auth.AuthService
import o_o.service.messaging.model.chat.Chat
import o_o.service.messaging.repo.ChatRepo
import o_o.service.messaging.util.getTokenFromRequest
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.stereotype.Service

@Service
class ChatService(
  val mainRepo: ChatRepo,
  val authService: AuthService
) : IChatService {

  override fun createUpdate(dto: ChatCreateUpdateDTO, request: ServletServerHttpRequest): Chat {
    val token = getTokenFromRequest(request)

    dto.id?.let { return mainRepo.findByIdAndDeletedFalse(it) }

    val entity = Chat()
    entity.accountOneId = authService.getByToken(token).id
    entity.accountTwoId = dto.user_id

    return mainRepo.save(entity)
  }

  override fun getById(id: String): Chat {
    return mainRepo.findByIdAndDeletedFalse(id)
  }

  override fun getAllByUser(userId: Long) =
    mainRepo.findAllByAccountOneIdOrAccountTwoId(userId, userId).filter { !it.deleted }
      .filter { !it.deleted }

  override fun delete(entity: Chat, userId: String?) {
    entity.deleted = true
    entity.modifiedByUser = userId

    mainRepo.save(entity)
  }
}