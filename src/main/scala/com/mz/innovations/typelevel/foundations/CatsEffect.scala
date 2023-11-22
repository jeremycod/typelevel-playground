package com.mz.innovations.typelevel.foundations


import cats.effect.{IO, Resource}

import java.io.{File, FileWriter, PrintWriter}
import concurrent.duration.*
import scala.io.BufferedSource
import scala.util.Random
object CatsEffect {

  val firstIO: IO[Int] = IO.pure(42)
  val delayedIO: IO[Int] = IO.apply {
    // complex code
    println("I'm just about to produce the meaning of life")
    42
  }

  def evaluateIO[A](io: IO[A]): Unit = {
    import cats.effect.unsafe.implicits.global
    val meaningOfLife = delayedIO.unsafeRunSync()
    println(s"the result of the effect is $meaningOfLife")
  }

  // raise/catch errors
  val aFailure: IO[Int] = IO.raiseError(new RuntimeException("a problem"))
  val dealWithIt = aFailure.handleErrorWith {
    case _: RuntimeException => IO(println("I'm still here, no worries"))
  }

  private val delayedPrint = IO.sleep(1.second) *> IO(println(Random.nextInt(100)))


    val manyPrints = for {
      fib1 <- delayedPrint.start
      fib2 <- delayedPrint.start
      _ <- fib1.join
      _ <- fib2.join
    } yield ()

    // resources
    val readingResource = Resource.make(
      IO(scala.io.Source.fromFile("src/main/scala/com/mz/innovations/typelevel/foundations/CatsEffect.scala"))
    )(source => IO(println("closing source")) *> IO(source.close()))
    val readingEffect = readingResource.use(source => IO(source.getLines().foreach(println)))

  // compose resouces
  private val copiedFileResource = Resource.make(
    IO(new PrintWriter(new FileWriter(new File("src/main/resources/dumpedFile.scala"))))
  )(writer => IO(println("closing duplicated file")) *> IO(writer.close()))

  private val compositeResouce: Resource[IO, (BufferedSource, PrintWriter)] = for {
    source <- readingResource
    destination <- copiedFileResource
  } yield (source, destination)

  compositeResouce.use {
    case (source, destination) => IO(source.getLines().foreach(destination.println))
  }
  def main(args: Array[String]): Unit = evaluateIO(delayedIO)


}
