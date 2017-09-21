name := "shorturl"

version := "0.4"

scalaVersion := "2.11.11"

// dependency versions
lazy val versions = new {
  val finatra = "2.13.0"
  val logback = "1.2.3"
  val typesafe = "1.3.1"
  val storehaus = "0.15.0"
  val guice = "4.0"
  val mockito = "1.9.5"
  val scalatest = "3.0.0"
  val scalacheck = "1.13.4"
  val specs2 = "2.4.17"
  val junit = "4.12"
}

// dependencies
libraryDependencies ++= Seq(
  // rest
  "com.twitter" %% "finatra-http" % versions.finatra,

  // logging
  "ch.qos.logback" % "logback-classic" % versions.logback,

  // cache
  "com.twitter" %% "storehaus-dynamodb" % versions.storehaus,

  // configuration
  "com.typesafe" % "config" % versions.typesafe,

  // testing
  "com.twitter" %% "finatra-http" % versions.finatra % "test",
  "com.twitter" %% "inject-server" % versions.finatra % "test",
  "com.twitter" %% "inject-app" % versions.finatra % "test",
  "com.twitter" %% "inject-core" % versions.finatra % "test",
  "com.twitter" %% "inject-modules" % versions.finatra % "test",
  "com.google.inject.extensions" % "guice-testlib" % versions.guice % "test",

  "com.twitter" %% "finatra-http" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-server" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-app" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-core" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-modules" % versions.finatra % "test" classifier "tests",

  "junit" % "junit" % versions.junit % "test",

  "org.mockito" % "mockito-core" % versions.mockito % "test",
  "org.scalacheck" %% "scalacheck" % versions.scalacheck % "test",
  "org.scalatest" %% "scalatest" % versions.scalatest % "test",
  "org.specs2" %% "specs2-mock" % versions.specs2 % "test"
)

// packaging
javaOptions in Universal ++= Seq(
  "-env=production",
  "-Dconfig.file=/etc/shorturl/production.conf",
  "-Dlog.access.output=/var/log/shorturl/access.log",
  "-Dlog.service.output=/var/log/shorturl/service.log"
)
packageName in Docker := "shorturl"
dockerExposedPorts := Seq(8888)
dockerUpdateLatest := true

enablePlugins(JavaAppPackaging)