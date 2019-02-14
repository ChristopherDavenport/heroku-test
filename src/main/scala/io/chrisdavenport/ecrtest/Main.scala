package io.chrisdavenport.ecrtest

import cats.implicits._
import cats.effect._
import org.http4s._
import org.http4s.dsl._
import org.http4s.implicits._
import _root_.io.chrisdavenport.ember.server._

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    Server.app[IO].use(_ => IO.never).as(ExitCode.Success)
  }
}

object Server {

  def app[F[_]: ConcurrentEffect: Timer: ContextShift] : Resource[F, Unit] = {
    val host = "0.0.0.0"
    val port = 8080
    EmberServer.impl[F](
        host,
        port,
        routes[F].orNotFound
    ).void
  }


  def routes[F[_]: Sync]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}; import dsl._
    HttpRoutes.of{
      case GET -> Root  => Ok("Hello there new3!")
      case GET -> Root / "hello" / name => Ok(s"Hello $name - new3")
    }
  }

}