package rs.high.url.model

case class RestResponse(
  errors: Seq[String] = Seq.empty,
  data: Any = None
)
