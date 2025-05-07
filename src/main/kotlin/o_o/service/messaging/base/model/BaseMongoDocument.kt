package o_o.service.messaging.base.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.*
import java.util.*


open class BaseMongoDocument {
  @Id
  var id: String = ObjectId.get().toHexString()

  @CreatedDate
  var createdDate: Date? = null

  @LastModifiedDate
  var lastModifiedDate: Date? = null

  @CreatedBy
  var createdByUser: String? = null

  @LastModifiedBy
  var modifiedByUser: String? = null

  var deleted = false
}
