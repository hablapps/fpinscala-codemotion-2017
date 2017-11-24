package org.hablapps.typeclasses
package exercise1
package solution

import org.scalatest._

/* Exercise 1
 *
 * Let's put into practice all the concepts we learned in this typeclass section,
 * for this purpose we're going to use another common typeclass, named `Show` that
 * simply gives us the ability to convert a value to a `String`. People with
 * Java knowledge may thing of this as the `toString` method every `Object` has
 * due to inheritance. Using typeclasses we decouple this behavior, as we saw
 * in previous steps.
 */
class ShowFinalFormSpec extends FunSpec with Matchers {

  // 1. Typeclass
  trait Show[A] {
    def show(a: A): String
  }

  object Show {
    def apply[A](implicit S: Show[A]) = S

    object syntax {
      def show[A](a: A)(implicit S: Show[A]) = S.show(a)
      def show[A](implicit S: Show[A]): A => String = a => S.show(a)
    }
  }

  // 2. Generic functions:
  //
  //    Define the following functions that make use of `Show` typeclass.
  //    Using `toString` method in this section is not allowed :)
  import Show.syntax._

  // 2.1. This function takes a list of elements of type `A` and returns the
  //      third element in `String` format
  def thirdString[A: Show](l: List[A]): Option[String] =
    l.drop(2).headOption.map(show)

  // 2.2. This function takes two `Int`s and prints the sum in the following format:
  //      > sum(5, 8) // "5 + 8 = 13"
  def sum(i1: Int, i2: Int)(implicit S: Show[Int]): String =
    s"${show(i1)} + ${show(i2)} = ${show(i1+i2)}"

  // 3. Typeclass instances

  object Implicits {
    // 3.1. Give an instance for `Int`s
    //      > show(5) // "5"
    implicit val intShow: Show[Int] =
      new Show[Int] {
        def show(a: Int): String = a.toString
      }

    // 3.2. Give an instance for `String`s
    //      > show("hello") // "hello"
    implicit val stringShow: Show[String] =
      new Show[String] {
        def show(a: String): String = a
      }

    // 3.3. Give an instance for `Person`s
    //      > show(Person("Alberto", 28)) // "Alberto - 28"
    case class Person(name: String, age: Int)
    implicit def personShow(implicit SS: Show[String], IS: Show[Int]): Show[Person] =
      new Show[Person] {
        def show(a: Person): String = s"${SS.show(a.name)} - ${IS.show(a.age)}"
      }
  }

  import Implicits._

  // 4. Execution
  //
  //    To run the tests, replace `ignore` for `describe` and run them
  //    > testOnly package org.hablapps.typeclasses.exercise1.ShowSpec
  describe("thirdString") {
    describe("int") {
      it("should work for list with 3+ elements") {
        thirdString(List(1, 2, 3)) shouldBe Option("3")
      }
      it("should work for list with 2- elements") {
        thirdString(List(1, 2)) shouldBe Option.empty[String]
        thirdString(List.empty[Int]) shouldBe Option.empty[String]
      }
    }
    describe("string") {
      it("should work for list with 3+ elements") {
        thirdString(List("hello", ", ", "world!")) shouldBe Option("world!")
      }
      it("should work for list with 2- elements") {
        thirdString(List("hello", "world!")) shouldBe Option.empty[String]
        thirdString(List.empty[String]) shouldBe Option.empty[String]
      }
    }
    describe("person") {
      it("should work for list with 3+ elements") {
        thirdString(List(
          Person("Ana", 28),
          Person("Berto", 38),
          Person("Carlos", 18))) shouldBe Option("Carlos - 18")
      }
      it("should work for list with 2- elements") {
        thirdString(List(
          Person("Ana", 28),
          Person("Berto", 38))) shouldBe Option.empty[String]
        thirdString(List.empty[Person]) shouldBe Option.empty[String]
      }
    }
  }

  describe("sum") {
    it("should work") {
      sum(5, 8) shouldBe "5 + 8 = 13"
    }
  }

}
