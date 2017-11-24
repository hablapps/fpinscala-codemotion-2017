package org.hablapps.typeclasses
package exercise3
package solution

import org.scalatest._

class OrderFinalFormSpec extends FunSpec with Matchers {

  // 1. Typeclass
  trait Order[A] {
    def compare(a1: A, a2: A): Int

    def gt(a1: A, a2: A): Boolean = compare(a1, a2) > 0
    def eq(a1: A, a2: A): Boolean = compare(a1, a2) == 0
    def lt(a1: A, a2: A): Boolean = compare(a1, a2) < 0
  }

  object Order {
    def apply[A](implicit O: Order[A]) = O

    object syntax {
      implicit class OrderOps[A](self: A)(implicit O: Order[A]) {
        def >(other: A) = O.gt(self, other)
        def ===(other: A) = O.eq(self, other)
        def <(other: A) = O.lt(self, other)
      }
    }
  }

  // 2. Generic function
  import Order.syntax._
  def greatest[A: Order](l: List[A]): Option[A] =
    l.foldLeft(Option.empty[A]) {
      case (Some(max), a) if a < max => Option(max)
      case (_, a) => Option(a)
    }

  def quicksort[A: Order](l: List[A]): List[A] =
    l match {
      case h :: t =>
        val (lower, greater) = t.partition(_ < h)
        quicksort(lower) ::: h :: quicksort(greater)
      case Nil => Nil
    }

  def greatest2[A: Order](l: List[A]): Option[A] =
    quicksort(l).reverse.headOption

  // 3. Typeclass instance
  case class Person(name: String, age: Int)

  implicit val personOrder: Order[Person] =
    new Order[Person] {
      def compare(p1: Person, p2: Person): Int = p1.age - p2.age
    }

  implicit val intOrder: Order[Int] =
    new Order[Int] {
      def compare(i1: Int, i2: Int): Int = i1 - i2
    }

  // 4. Execution
  describe("greatest") {
    describe("person") {
      it("should work for non-empty lists") {
        greatest(Person("Félix", 5) ::
                 Person("Alberto", 3) ::
                 Person("Alfredo", 7) ::
                 Person("Sergio", 2) :: Nil) shouldBe Option(Person("Alfredo", 7))
      }
      it("should work for empty lists") {
        greatest(List.empty[Person]) shouldBe None
      }
    }
    describe("int") {
      it("should work for non-empty lists") {
        greatest(5 :: 3 :: 7 :: 2 :: Nil) shouldBe Option(7)
      }
      it("should work for empty lists") {
        greatest(List.empty[Int]) shouldBe None
      }
    }
  }

  describe("quicksort") {
    describe("person") {
      it("should work for non-empty lists") {
        quicksort(
          Person("Félix", 5) ::
          Person("Alberto", 3) ::
          Person("Alfredo", 7) ::
          Person("Sergio", 2) :: Nil) shouldBe
            Person("Sergio", 2) ::
            Person("Alberto", 3) ::
            Person("Félix", 5) ::
            Person("Alfredo", 7) :: Nil
      }
      it("should work for empty lists") {
        quicksort(List.empty[Person]) shouldBe List.empty[Person]
      }
    }
    describe("int") {
      it("should work for non-empty lists") {
        quicksort(5 :: 3 :: 7 :: 2 :: Nil) shouldBe 2 :: 3 :: 5 :: 7 :: Nil
      }
      it("should work for empty lists") {
        quicksort(List.empty[Int]) shouldBe List.empty[Int]
      }
    }
  }

  describe("greatest2") {
    describe("person") {
      it("should work for non-empty lists") {
        greatest2(Person("Félix", 5) ::
                 Person("Alberto", 3) ::
                 Person("Alfredo", 7) ::
                 Person("Sergio", 2) :: Nil) shouldBe Option(Person("Alfredo", 7))
      }
      it("should work for empty lists") {
        greatest2(List.empty[Person]) shouldBe None
      }
    }
    describe("int") {
      it("should work for non-empty lists") {
        greatest2(5 :: 3 :: 7 :: 2 :: Nil) shouldBe Option(7)
      }
      it("should work for empty lists") {
        greatest2(List.empty[Int]) shouldBe None
      }
    }
  }

}
