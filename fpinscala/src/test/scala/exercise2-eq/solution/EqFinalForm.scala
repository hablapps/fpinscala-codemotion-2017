package org.hablapps.typeclasses
package exercise2
package solution

import org.scalatest._

/* Exercise 2
 *
 * Let's put into practice all the concepts we learned in this typeclass section,
 * for this purpose we're going to use another common typeclass, named `Eq` that
 * simply gives us the ability to compare wether two values are equal. People with
 * Java knowledge may thing of this as the `equals` method every `Object` has
 * due to inheritance. Using typeclasses we decouple this behavior, as we saw
 * in previous steps.
 */
class EqFinalFormSpec extends FunSpec with Matchers {

  // 1. Typeclass
  trait Eq[A] {
    def eq(a1: A, a2: A): Boolean
    def neq(a1: A, a2: A): Boolean = !eq(a1, a2)
  }

  object Eq {
    def apply[A](implicit E: Eq[A]) = E

    object syntax {
      implicit class EqOps[A](self: A)(implicit E: Eq[A]) {
        def =~=(other: A) = E.eq(self, other)
        def =!=(other: A) = E.neq(self, other)
      }
    }
  }

  // 2. Generic functions:
  //
  //    Define the following functions that make use of `Eq` typeclass.
  //    Using `==` in this section is not allowed :)
  import Eq.syntax._

  // 2.1. This function takes a list of elements of type `A` and a value of
  //      that type `A` and returns true if the list contains that value.
  def contains[A: Eq](l: List[A])(a: A): Boolean =
    l match {
      case h :: t =>
        if (h =~= a) true
        else contains(t)(a)
      case Nil => false
    }

  // 2.2. This function is similar to `contains` but instead of returning
  //      just true or false, it returns the position the value occupies in
  //      the list wrapped in a `Some`. It returns `None` if the value is not
  //      in the list
  //      > index(List(1, 2, 3, 4))(3) // Some(2)
  //      > index(List(1, 2, 3, 4))(7) // None
  def index[A: Eq](l: List[A])(a: A): Option[Int] = {
    def go(ll: List[A], res: Int): Option[Int] =
      ll match {
        case h :: t =>
          if (h =~= a) Some(res)
          else go(t, res+1)
        case Nil => None
      }
    go(l, 0)
  }

  // 3. Typeclass instances

  // 3.1. Give an instance for `Int`s
  //      > eq(5, 5) // true
  //      > eq(5, 7) // false
  implicit val intEq: Eq[Int] =
    new Eq[Int] {
      def eq(i1: Int, i2: Int): Boolean = i1 == i2
    }

  // 3.2. Give an instance for `String`s
  //      > eq("hello", "hello") // true
  //      > eq("hello", "bye") // false
  implicit val stringEq: Eq[String] =
    new Eq[String] {
      def eq(s1: String, s2: String): Boolean = s1 == s2
    }

  // 3.3. Give an instance for `Person`s
  //      > eq(Person("Alberto", 28), Person("Alberto", 28)) // true
  //      > eq(Person("Alberto", 28), Person("Alberto", 29)) // false
  //      > eq(Person("Alberto", 28), Person("Albert",  28)) // false
  case class Person(name: String, age: Int)
  implicit def personEq(implicit ES: Eq[String], EI: Eq[Int]): Eq[Person] =
    new Eq[Person] {
      def eq(p1: Person, p2: Person): Boolean =
        ES.eq(p1.name, p2.name) &&
        EI.eq(p1.age, p2.age)
    }

  // 4. Execution
  //
  //    To run the tests, replace `ignore` for `describe` and run them
  //    > testOnly package org.hablapps.typeclasses.exercise2.EqSpec
  describe("contains") {
    describe("int") {
      it("should work for non-empty list") {
        contains(List(1, 2, 3))(3) shouldBe true
        contains(List(1, 2, 3))(4) shouldBe false
      }
      it("should work for empty list") {
        contains(List.empty[Int])(3) shouldBe false
        contains(List.empty[Int])(4) shouldBe false
      }
    }
    describe("string") {
      it("should work for non-empty list") {
        contains(List("hello", ", ", "world!"))("hello") shouldBe true
        contains(List("hello", ", ", "world!"))("bye") shouldBe false
      }
      it("should work for empty list") {
        contains(List.empty[String])("hello") shouldBe false
        contains(List.empty[String])("bye") shouldBe false
      }
    }
    describe("person") {
      it("should work for non-empty list") {
        contains(List(
          Person("Ana", 28),
          Person("Berto", 38),
          Person("Carlos", 18)))(Person("Berto", 38)) shouldBe true
        contains(List(
          Person("Ana", 28),
          Person("Berto", 38),
          Person("Carlos", 18)))(Person("Carlos", 19)) shouldBe false
      }
      it("should work for empty list") {
        contains(List.empty[Person])(Person("Berto", 38)) shouldBe false
        contains(List.empty[Person])(Person("Carlos", 19)) shouldBe false
      }
    }
  }

  describe("index") {
    describe("int") {
      it("should work for non-empty list") {
        index(List(1, 2, 3))(3) shouldBe Option(2)
        index(List(1, 2, 3))(4) shouldBe Option.empty
      }
      it("should work for empty list") {
        index(List.empty[Int])(3) shouldBe Option.empty
        index(List.empty[Int])(4) shouldBe Option.empty
      }
    }
    describe("string") {
      it("should work for non-empty list") {
        index(List("hello", ", ", "world!"))("hello") shouldBe Option(0)
        index(List("hello", ", ", "world!"))("bye") shouldBe Option.empty
      }
      it("should work for empty list") {
        index(List.empty[String])("hello") shouldBe Option.empty
        index(List.empty[String])("bye") shouldBe Option.empty
      }
    }
    describe("person") {
      it("should work for non-empty list") {
        index(List(
          Person("Ana", 28),
          Person("Berto", 38),
          Person("Carlos", 18)))(Person("Berto", 38)) shouldBe Option(1)
        index(List(
          Person("Ana", 28),
          Person("Berto", 38),
          Person("Carlos", 18)))(Person("Carlos", 19)) shouldBe Option.empty
      }
      it("should work for empty list") {
        index(List.empty[Person])(Person("Berto", 38)) shouldBe Option.empty
        index(List.empty[Person])(Person("Carlos", 19)) shouldBe Option.empty
      }
    }
  }

}
