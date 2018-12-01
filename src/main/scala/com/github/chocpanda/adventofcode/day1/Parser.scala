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

import java.nio.file.Paths

import cats.effect.{ ContextShift, IO, Resource }
import fastparse.NoWhitespace._
import fastparse._
import fs2.{ io, text, Stream }

import scala.concurrent.ExecutionContextExecutorService

object Parser {

  /*_*/

  def parseNumber[_: P]: P[Int] = P(CharIn("0-9").rep(1).!.map(_.toInt))

  def parseOperator[_: P]: P[String] = P(CharIn("+\\-").!)

  def parser[_: P]: P[(String, Int)] = parseOperator ~ parseNumber ~ End

  def parseFile(fileName: String, resource: Resource[IO, ExecutionContextExecutorService])(
    implicit contextShift: ContextShift[IO]
  ): Stream[IO, Parsed[Operation]] = {
    implicit val ec: Resource[IO, ExecutionContextExecutorService] = resource
    Stream
      .resource(ec)
      .flatMap { ec =>
        io.file
          .readAll[IO](Paths.get(fileName), ec, 4096)
          .through(text.utf8Decode)
          .through(text.lines)
          .map(Parser.parseOperation)
      }
  }

  def parseOperation(line: String): Parsed[(String, Int)] = parse(line, parser(_))
}
