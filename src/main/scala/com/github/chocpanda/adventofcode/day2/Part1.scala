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

package com.github.chocpanda.adventofcode.day2

import java.util.concurrent.Executors

import cats.effect.{ ExitCode, IO, IOApp, Resource }
import cats.implicits._
import com.github.chocpanda.utils.{ FileReader, Logger }

import scala.concurrent.{ ExecutionContext, ExecutionContextExecutorService }

object Part1 extends IOApp {

  private val blockingExecutionContext: Resource[IO, ExecutionContextExecutorService] =
    Resource.make(IO(ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))))(ec => IO(ec.shutdown()))

  val file = "F:\\Workspace\\Github\\AdventOfCode\\src\\main\\resources\\day2\\input.txt"

  def run(args: List[String]): IO[ExitCode] =
    FileReader
      .readFile(file, blockingExecutionContext)
      .map(CheckSumEval.eval)
      .fold((0, 0)) {
        case ((accDoubles, accTriples), CheckSumEval(true, true))   => (accDoubles + 1, accTriples + 1)
        case ((accDoubles, accTriples), CheckSumEval(false, false)) => (accDoubles, accTriples)
        case ((accDoubles, accTriples), CheckSumEval(false, true))  => (accDoubles, accTriples + 1)
        case ((accDoubles, accTriples), CheckSumEval(true, false))  => (accDoubles + 1, accTriples)
      }
      .map(res => res._1 * res._2)
      .evalMap(Logger.log(_))
      .compile
      .drain
      .as(ExitCode.Success)

}
