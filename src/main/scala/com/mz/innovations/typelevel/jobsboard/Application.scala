package com.mz.innovations.typelevel.jobsboard

import cats.Monad
import cats.effect.*
import cats.implicits.toSemigroupKOps
import com.mz.innovations.typelevel.jobsboard.config.EmberConfig
import com.mz.innovations.typelevel.jobsboard.config.syntax.*
import com.mz.innovations.typelevel.jobsboard.http.routes.HealthRoutes
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.*
import pureconfig.ConfigReader.Result
import pureconfig.ConfigSource

object Application extends IOApp.Simple {
  /*
        1 - add a plain health endpoint to our app
        2 - add minimal configuration
        3 - basic http server layout
   */
  val configSource: Result[EmberConfig] = ConfigSource.default.load[EmberConfig]


  override def run: IO[Unit] =

    ConfigSource.default.loadF[IO, EmberConfig].flatMap { config =>

      EmberServerBuilder
        .default[IO]
        .withHost(config.host)
        .withPort(config.port)
        .withHttpApp {
          HealthRoutes[IO].routes.orNotFound
        }
        .build
        .use(_ => IO.println("Server ready 2") *> IO.never)
    }
}
