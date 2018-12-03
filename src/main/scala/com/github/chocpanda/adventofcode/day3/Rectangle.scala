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

final case class Rectangle(id: String, topLeft: Position, height: Int, width: Int) {
  lazy val topRight: Position    = topLeft.copy(x = topLeft.x + width)
  lazy val bottomLeft: Position  = topLeft.copy(y = topLeft.y + height)
  lazy val bottomRight: Position = bottomLeft.copy(x = topLeft.x + width)
}

import fastparse._, SingleLineWhitespace._

object Rectangle {

  private def parseNumber[_: P]: P[Int] = P(CharIn("0-9").rep(1).!.map(_.toInt))

  private def parseId[_: P]: P[String] = P("#" ~ CharIn("0-9").rep(1).!)

  def parser[_: P]: P[Rectangle] =
    (parseId ~ "@" ~ Position.parser ~ ":" ~ parseNumber ~ "x" ~ parseNumber).map {
      case (id, pos, width, height) => apply(id, pos, width, height)
    }

  def parseRectangle(str: String): Rectangle = {
    val Parsed.Success(rectangle, _) = parse(str, parser(_)) // Todo don't do this
    rectangle
  }
}
