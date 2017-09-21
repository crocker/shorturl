package rs.high.url

import com.twitter.finagle.http.filter.Cors
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter
import rs.high.url.module.{CacheModule, ConfigurationModule}
import rs.high.url.resource.UrlResource

object ServerMain extends Server

class Server extends HttpServer {

  override val modules = Seq(
    ConfigurationModule,
    CacheModule
  )

  override def configureHttp(router: HttpRouter) {
    val corsFilter = new Cors.HttpFilter(Cors.UnsafePermissivePolicy)

    router
      .filter(corsFilter, beforeRouting = true)
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[UrlResource]
  }
}