package rs.high.url.resource

import javax.inject.Inject

import com.twitter.finatra.http.Controller
import rs.high.url.model.RestResponse
import rs.high.url.resource.request.url.{ExpandRequest, ShortenRequest}
import rs.high.url.service.UrlService

class UrlResource @Inject()(urlService: UrlService) extends Controller {

  post("/") { request: ShortenRequest =>
    val res = urlService.shorten(request.url)
    res.map { url =>
      // build response
      val resp = RestResponse(
        data = url
      )

      response.created(resp)
    }
  }

  get("/:token") { request: ExpandRequest =>
    val res = urlService.expand(request.token)
    res.map { url =>
      response.movedPermanently.location(url)
    }

  }
}