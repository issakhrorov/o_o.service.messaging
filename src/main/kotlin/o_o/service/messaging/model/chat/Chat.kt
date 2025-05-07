package o_o.service.messaging.model.chat

import o_o.service.messaging.base.model.BaseMongoDocument
import o_o.service.messaging.model.message.Message
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Chat: BaseMongoDocument() {
  var accountOneId: Long = 0

  var accountTwoId: Long = 0

  var messages: List<Message> = emptyList()
}