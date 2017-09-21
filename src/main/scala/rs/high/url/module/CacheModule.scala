package rs.high.url.module

import javax.inject.Singleton

import com.google.inject.Provides
import com.twitter.inject.{Logging, TwitterModule}
import com.twitter.storehaus.cache.MutableLRUCache
import com.twitter.storehaus.dynamodb.{DynamoStore, DynamoStringStore}
import com.typesafe.config.Config

object CacheModule extends TwitterModule with Logging {
  @Provides
  @Singleton
  def providesLocalCache(config: Config): MutableLRUCache[String, String] = {
    val size = config.getInt("token.cache")
    MutableLRUCache[String, String](size)
  }

  @Provides
  @Singleton
  def providesRemoteCache(config: Config): DynamoStringStore = {
    DynamoStringStore(
      awsAccessKey = config.getString("aws.access_key"),
      awsSecretKey = config.getString("aws.secret_key"),
      tableName = "url_map",
      primaryKeyColumn = "token",
      valueColumn = "url"
    )
  }
}
