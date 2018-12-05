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

package com.github.chocpanda.adventofcode.day3

import java.util.concurrent.Executors

import cats.effect.{ ExitCode, IO, IOApp, Resource }
import cats.implicits._
import com.github.chocpanda.utils.{ FileReader, Logger }

import scala.concurrent.{ ExecutionContext, ExecutionContextExecutorService }

object Part1 extends IOApp {

  private val blockingExecutionContext: Resource[IO, ExecutionContextExecutorService] =
    Resource.make(IO(ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))))(ec => IO(ec.shutdown()))

  private val file = "C:\\dev\\AdventOfCode\\src\\main\\resources\\day3\\input.txt"

  def run(args: List[String]): IO[ExitCode] =
    FileReader
      .readFile(file, blockingExecutionContext)
      .map(Rectangle.parseRectangle)
      .fold(Array.ofDim[Int](1000, 1000)) {
        case (acc, rec) =>
          rec.points.foreach { point =>
            acc(point.x)(point.y) += 1
          }
          acc
      }
      .evalMap(
        outer =>
          Logger.log(outer.foldLeft(0) {
            case (acc, inner) => acc + inner.count(_ > 1)
          })
      )
      .compile
      .drain
      .as(ExitCode.Success)
}
