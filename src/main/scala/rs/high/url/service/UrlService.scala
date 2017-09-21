package rs.high.url.service

import javax.inject.{Inject, Singleton}

import com.twitter.finagle.http.Status
import com.twitter.finatra.http.exceptions.HttpException
import com.twitter.inject.conversions.future._
import com.twitter.storehaus.cache.MutableLRUCache
import com.twitter.storehaus.dynamodb.DynamoStringStore
import com.twitter.util.Future
import com.typesafe.config.Config
import rs.high.url.model.ShortUrl

import scala.collection.JavaConversions._
import scala.util.Random

@Singleton
class UrlService @Inject()(
  config: Config,
  cache: MutableLRUCache[String, String],
  store: DynamoStringStore
) {

  // constants
  private val TokenLength = config.getInt("token.length")
  private val TokenRetry = config.getInt("token.retry")
  private val TokenPrefix = config.getString("token.prefix")

  private val InvalidTokenException = HttpException(Status.NotFound, errors = Seq("Invalid url token"):_*)
  private val DuplicateTokenException = HttpException(Status.BadRequest, errors = Seq("Failed to generate a unique token"):_*)

  /**
    * Shortens a full length url
    * @param url the url to be shortened
    * @return an object containing a prefix, token and short url
    */
  def shorten(url: String): Future[ShortUrl] = {
    val cached = cache.m.find(_._2 == url)

    val token = cached match {
      case Some(existing) =>
        cache.hit(existing._1)
        Future(existing._1)
      case _ =>
        generateToken().flatMap { tkn =>
          cache += (tkn, url)
          store.put((tkn, Option(url))).map(_ => tkn)
        }
    }

    // convert to short url
    token.map { tkn =>
      ShortUrl(
        prefix = TokenPrefix,
        token = tkn,
        url = s"${TokenPrefix}/${tkn}"
      )
    }


  }

  /**
    * Expand a short url to its original form
    *
    * @param token the token assigned to the full url
    * @return an expanded full url
    */
  def expand(token: String): Future[String] = {
    cache.hit(token) match {
      case Some(existing) =>
        Future(existing)
      case _ =>
        store.get(token)
          .map(_.getOrElse(throw InvalidTokenException))
    }
  }

  private def generateToken(): Future[String] = {
    // generate multiple tokens in case of a collision
    val potentialTokens = (1 to TokenRetry).map { _ =>
      Random.alphanumeric.take(TokenLength).mkString
    }

    // check for existence of potential tokens
    val ptResponse = store.multiGet(potentialTokens.toSet).map { kv =>
      kv._2.map(v => kv._1 -> v)
    }

    // return the first available token or fail
    val token = Future.collect(ptResponse.toSeq)
      .filterInner(_._2.isEmpty)
      .map(_.headOption.map(_._1))

    token.map(_.getOrElse(throw DuplicateTokenException))
  }
}
