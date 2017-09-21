package rs.high.url.resource.request

import com.twitter.finatra.request.QueryParam
import com.twitter.finatra.validation.NotEmpty

object url {

  case class ShortenRequest(
    @QueryParam @NotEmpty url: String
  )

  case class ExpandRequest(
    @QueryParam @NotEmpty token: String
  )

}