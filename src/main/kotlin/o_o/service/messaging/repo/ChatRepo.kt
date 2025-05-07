package o_o.service.messaging.repo

import o_o.service.messaging.model.chat.Chat
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRepo: MongoRepository<Chat, Long> {
  fun findByIdAndDeletedFalse(id: String): Chat
  fun findAllByAccountOneIdOrAccountTwoId(userOneId: Long, userTwoId: Long): List<Chat>
}