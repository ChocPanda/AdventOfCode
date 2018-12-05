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
import cats.Show
import com.github.chocpanda.utils.{ FileReader, Logger }

import scala.concurrent.{ ExecutionContext, ExecutionContextExecutorService }

object Part2 extends IOApp {

  private val blockingExecutionContext: Resource[IO, ExecutionContextExecutorService] =
    Resource.make(IO(ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))))(ec => IO(ec.shutdown()))

  private val file = "C:\\dev\\AdventOfCode\\src\\main\\resources\\day3\\input.txt"

  implicit def arrShow[T: Show]: Show[Array[T]] = arr => arr.toList.show

  def init: Array[Array[List[String]]] = Array.ofDim[List[String]](1000, 1000).map { arr =>
    arr.map { _ =>
      List.empty[String]
    }
  }

  def run(args: List[String]): IO[ExitCode] =
    FileReader
      .readFile(file, blockingExecutionContext)
      .map(Rectangle.parseRectangle)
      .fold(init) {
        case (acc, rec) =>
          rec.points.foreach { point =>
            acc(point.x)(point.y) = rec.id :: acc(point.x)(point.y)
          }
          acc
      }
      .evalMap[IO, Unit] { arr =>
        (1 to 1323)
          .find { id =>
            (0 to 999).forall { x =>
              (0 to 999).forall { y =>
                !(arr(x)(y).length > 1 && arr(x)(y).contains(id.toString))
              }
            }
          }
          .map(Logger.log(_))
          .getOrElse(Logger.log("Didn't find an Id"))
      }
      .compile
      .drain
      .as(ExitCode.Success)
}
