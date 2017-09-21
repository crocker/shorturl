package rs.high.url.resource

import com.twitter.finagle.http.Status
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import rs.high.url.Server

class UrlResourceTest extends FeatureTest {
  override val server = new EmbeddedHttpServer(new Server)

  test("Shorten a URL") {
    server.httpFormPost(
      path = "/",
      params = Map(
        "url" -> "https://www.highrisegame.com"
      ),
      andExpect = Status.Created)
  }

  test("Handle invalid tokens") {
    server.httpGet(
      path = "/abc",
      andExpect = Status.NotFound)
  }

}