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

package com.github.chocpanda.adventofcode.day1

import java.util.concurrent.Executors

import cats.effect.{ ExitCode, IO, IOApp, Resource }
import cats.implicits._
import com.github.chocpanda.utils.{ FileReader, Logger }
import fastparse.Parsed

import scala.concurrent.{ ExecutionContext, ExecutionContextExecutorService }

object Part1 extends IOApp {

  /*_*/

  private val blockingExecutionContext: Resource[IO, ExecutionContextExecutorService] =
    Resource.make(IO(ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))))(ec => IO(ec.shutdown()))

  private def eval: (Int, Parsed[Operation]) => Int = {
    case (curr, Parsed.Success(("+", operand), _)) => curr + operand
    case (curr, Parsed.Success(("-", operand), _)) => curr - operand
    case (curr, _)                                 => curr
  }

  def run(args: List[String]): IO[ExitCode] =
    FileReader
      .readFile("F:\\Workspace\\Github\\AdventOfCode\\src\\main\\resources\\day1\\input.txt", blockingExecutionContext)
      .map(Parser.parseOperation)
      .fold(0)(eval)
      .evalMap(Logger.log(_))
      .compile
      .drain
      .as(ExitCode.Success)
}
