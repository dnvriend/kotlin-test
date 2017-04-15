/*
 * Copyright 2017 Dennis Vriend
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

package com.github.dnvriend

import scalaz.Scalaz._
import scalaz._

object PersonValidation {
  def validateNonEmpty(fieldName: String, value: String): ValidationNel[String, String] =
    Option(value).filter(_.trim.nonEmpty).toSuccessNel(s"Field '$fieldName' must not be empty")

  def validateName(name: String): ValidationNel[String, String] =
    validateNonEmpty("name", name)

  def validateGt(fieldName: String, maxValue: Int, value: Int): ValidationNel[String, Int] =
    Option(value).filter(_ > maxValue).toSuccessNel(s"Field '$fieldName' with value '$value' must be greater than '$maxValue'")

  def validateLt(fieldName: String, maxValue: Int, value: Int): ValidationNel[String, Int] =
    Option(value).filter(_ < maxValue).toSuccessNel(s"Field '$fieldName' with value '$value' must be less than '$maxValue'")

  def validateAge(age: Int): ValidationNel[String, Int] =
    validateGt("age", -1, age) *> validateLt("age", 140, age)

  def validate(name: String, age: Int): ValidationNel[String, Person] =
    (validateName(name) |@| validateAge(age))((name, age) => new Person(name, age))
}

object HelloWorld extends App {
  val result: Disjunction[NonEmptyList[String], Person] = for {
    einstein <- PersonValidation.validate("Albert Einstein", 42).disjunction
    fibonacci <- PersonValidation.validate("Fibonacci", 135).disjunction
    pythagoras <- PersonValidation.validate("Pythagoras", 1764).disjunction
  } yield pythagoras

  result match {
    case DLeft(messages) =>
      println(messages)
    case DRight(person) =>
      println(person)
  }
}