/*
 * Copyright 2018 com.github.chocpanda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.chocpanda.adventofcode

import fastparse._
import NoWhitespace._
import cats.effect.{ ExitCode, IO, IOApp, Resource }
import cats.implicits._
import fs2.{ io, text, Stream }
import java.nio.file.Paths
import java.util.concurrent.Executors

import scala.concurrent.ExecutionContext

object Day1 extends IOApp {

  private def parseNumber[_: P]: P[Int] = P(CharIn("0-9").rep(1).!.map(_.toInt))

  private def parseOperator[_: P]: P[String] = P(CharIn("+\\-").!)

  private def parser[_: P]: P[(String, Int)] = parseOperator ~ parseNumber ~ End

  private val blockingExecutionContext =
    Resource.make(IO(ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))))(ec => IO(ec.shutdown()))

  /*_*/

  private def parseFile(fileName: String): Stream[IO, Parsed[(String, Int)]] =
    Stream
      .resource(blockingExecutionContext)
      .flatMap { blockingEC =>
        io.file
          .readAll[IO](Paths.get(fileName), blockingEC, 4096)
          .through(text.utf8Decode)
          .through(text.lines)
          .map(line => parse(line, parser(_)))
      }

  def eval: (Int, Parsed[(String, Int)]) => Int = {
    case (curr, Parsed.Success(("+", operand), _)) => curr + operand
    case (curr, Parsed.Success(("-", operand), _)) => curr - operand
    case (curr, _)                                 => curr
  }

  def run(args: List[String]): IO[ExitCode] =
    parseFile("F:\\Workspace\\Github\\AdventOfCode\\src\\main\\resources\\input.txt")
      .fold(0)(eval)
      .evalMap(res => IO(println(res)))
      .compile
      .drain
      .as(ExitCode.Success)
}
