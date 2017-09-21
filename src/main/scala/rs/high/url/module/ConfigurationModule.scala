package rs.high.url.module

import javax.inject.Singleton

import com.google.inject.Provides
import com.twitter.inject.{Logging, TwitterModule}
import com.typesafe.config.{Config, ConfigFactory}

object ConfigurationModule extends TwitterModule with Logging {
  val envFlag = flag[String]("env", "local", "Environment to load configuration values for")

  @Provides
  @Singleton
  def providesEnvironmentConfiguration(): Config = {
    val baseConfig = ConfigFactory.load
    info("Environment: " + envFlag)

    envFlag.apply() match {
      case "local" =>
        // use local config files
        baseConfig.getConfig(envFlag()).withFallback(baseConfig)
      case _ =>
        // assume config file location is already provided
        baseConfig
    }
  }
}
