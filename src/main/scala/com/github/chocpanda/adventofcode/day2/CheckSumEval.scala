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

import cats.Show

final case class CheckSumEval(hasDouble: Boolean, hasTriple: Boolean)

object CheckSumEval {
  def eval(str: String): CheckSumEval = {
    val numChars = str.groupBy(identity).mapValues(_.length)
    new CheckSumEval(numChars.exists { case (_, num) => num == 2 }, numChars.exists { case (_, num) => num == 3 })
  }

  implicit val showCheckSumEval: Show[CheckSumEval] = c => c.toString
}
