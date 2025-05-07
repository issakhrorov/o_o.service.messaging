package o_o.service.messaging.repo

import o_o.service.messaging.model.message.Message
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepo: MongoRepository<Message, Long> {
  fun findByIdAndDeletedFalse(id: String): Message
  fun findAllByRepliesToIdAndDeletedFalse(repliesTo: String): List<Message>
  fun findAllByChatIdAndDeletedFalse(chatId: String): List<Message>
  fun findAllByChatIdAndDeletedFalseOrderByIdDesc(chatId: String, pageable: Pageable): Page<Message>
}