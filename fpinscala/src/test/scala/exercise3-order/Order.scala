package org.hablapps.typeclasses
package exercise3

import org.scalatest._

/* Exercise 3
 *
 * Take the `Order` interface we created in section 1 and transform it
 * into a typeclass. Tasks include:
 *   - Modify the interface to make it a typeclass
 *   - Modify the functions `greatest`, `quicksort` & `greatest2`
 *   - Modify the instances for `Person` & `Int`
 *   - Modify the tests to use the new functions & instances
 */
class OrderSpec extends FunSpec with Matchers {

  // 1. Typeclass
  trait Order[A] {
    def compare(other: A): Int

    def >(other: A): Boolean = compare(other) > 0
    def ===(other: A): Boolean = compare(other) == 0
    def <(other: A): Boolean = compare(other) < 0
  }

  // 2. Generic function
  def greatest[A](l: List[A])(wrap: A => Order[A]): Option[A] =
    l.foldLeft(Option.empty[A]) {
      case (Some(max), a) if wrap(a) < max => Option(max)
      case (_, a) => Option(a)
    }

  def quicksort[A](l: List[A])(wrap: A => Order[A]): List[A] =
    l match {
      case h :: t =>
        val (lower, greater) = t.partition(a => wrap(a) < h)
        quicksort(lower)(wrap) ::: h :: quicksort(greater)(wrap)
      case Nil => Nil
    }

  def greatest2[A](l: List[A])(wrap: A => Order[A]): Option[A] =
    quicksort(l)(wrap).reverse.headOption

  // 3. Typeclass instance
  case class Person(name: String, age: Int) extends Order[Person] {
    def compare(other: Person) = this.age - other.age
  }

  case class IntOrder(unwrap: Int) extends Order[Int] {
    def compare(other: Int) = unwrap - other
  }

  // 4. Execution
  describe("greatest") {
    describe("person") {
      it("should work for non-empty lists") {
        greatest(Person("Félix", 5) ::
                 Person("Alberto", 3) ::
                 Person("Alfredo", 7) ::
                 Person("Sergio", 2) :: Nil)(identity) shouldBe Option(Person("Alfredo", 7))
      }
      it("should work for empty lists") {
        greatest(List.empty[Person])(identity) shouldBe None
      }
    }
    describe("int") {
      it("should work for non-empty lists") {
        greatest(5 :: 3 :: 7 :: 2 :: Nil)(IntOrder(_)) shouldBe Option(7)
      }
      it("should work for empty lists") {
        greatest(List.empty[Int])(IntOrder(_)) shouldBe None
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
          Person("Sergio", 2) :: Nil)(identity) shouldBe
            Person("Sergio", 2) ::
            Person("Alberto", 3) ::
            Person("Félix", 5) ::
            Person("Alfredo", 7) :: Nil
      }
      it("should work for empty lists") {
        quicksort(List.empty[Person])(identity) shouldBe List.empty[Person]
      }
    }
    describe("int") {
      it("should work for non-empty lists") {
        quicksort(5 :: 3 :: 7 :: 2 :: Nil)(IntOrder(_)) shouldBe 2 :: 3 :: 5 :: 7 :: Nil
      }
      it("should work for empty lists") {
        quicksort(List.empty[Int])(IntOrder(_)) shouldBe List.empty[Int]
      }
    }
  }

  describe("greatest2") {
    describe("person") {
      it("should work for non-empty lists") {
        greatest2(Person("Félix", 5) ::
                 Person("Alberto", 3) ::
                 Person("Alfredo", 7) ::
                 Person("Sergio", 2) :: Nil)(identity) shouldBe Option(Person("Alfredo", 7))
      }
      it("should work for empty lists") {
        greatest2(List.empty[Person])(identity) shouldBe None
      }
    }
    describe("int") {
      it("should work for non-empty lists") {
        greatest2(5 :: 3 :: 7 :: 2 :: Nil)(IntOrder(_)) shouldBe Option(7)
      }
      it("should work for empty lists") {
        greatest2(List.empty[Int])(IntOrder(_)) shouldBe None
      }
    }
  }

}
