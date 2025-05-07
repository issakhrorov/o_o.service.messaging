package o_o.service.messaging.base.dto.pageable

import org.springframework.data.domain.Pageable

open class PageableContentWithHeadersDTO<T>(
  val headers: List<ColumnHeadersDTO>? = null,
  val params: HashMap<String, Any?>,
  val content: T,
  val pageable : Pageable,
  val total_elements : Long = 0,
  val last : Boolean,
  val total_pages : Int = 0
)


class ColumnHeadersDTO(
  val name: String? = null,
  val title: String? = null,
//  val type: ColumnHeaderType? = null,
  val options: List<Any?>? = null,
  val enableColumnFilter: Boolean = true
)
