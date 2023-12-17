package com.mz.innovations.typelevel.jobsboard.http.routes

import cats.Monad
import org.http4s.*
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.server.Router

class HealthRoutes[F[_] : Monad] extends Http4sDsl[F] {
  private val healthRoute: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl.*
    HttpRoutes.of[F] { case GET -> Root =>
      Ok("All going great")
    }
  }

  val routes: HttpRoutes[F] = Router(
    "health" -> healthRoute
  )
}

object HealthRoutes {
  def apply[F[_] : Monad] = new HealthRoutes[F]
}
