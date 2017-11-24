package org.hablapps.typeclasses
package section1

import org.scalatest._

package object templates extends OrderSpec

class OrderSpec extends FunSpec with Matchers {

  // 1. We want to implement a function greatest, that given a `List` of items
  // of any kind, it returns the greatest among them, wrapped in an `Option`.
  // If the list is empty, it returns `None`. This is its signature:
  //
  // def greatest[A](l: List[A]): Option[A]

  // 2. Of course, we can't give an implementation of this function because
  // we know nothing of the type `A`, hence we don't know which one is greater
  // than the other. We can use inheritance and restrict those `A` to extend
  // an interface Order. (Comparable in Java)
  //
  // trait Order[A]

  // 3. Now that we have the interface ready to use, let's try again and implement
  // the function greatest.
  //
  // def greatest[A <: Order[A]](l: List[A]): Option[A]

  // 4. Yay! It seems we got it! Let's try it with any custom type we want
  //
  // case class Person(name: String, age: Int)

  // describe("greatest using inheritance") {
  //   it("should work for non-empty lists") {
  //     greatest(Person("Félix", 5) ::
  //              Person("Alberto", 3) ::
  //              Person("Alfredo", 7) ::
  //              Person("Sergio", 2) :: Nil) shouldBe Option(Person("Alfredo", 7))
  //   }
  //   it("should work for empty lists") {
  //     greatest(List.empty[Person]) shouldBe None
  //   }
  // }

  // 5. Ok, so far so good, what if we want to sort lists of `Int`s?

  // 6. Oops! we don't have control over the type `Int` so we didn't
  // progress too much. For those types out of our scope, we can make
  // good use of another OOP pattern... Adapters to the rescue!
  //
  // case class IntOrder(unwrap: Int) extends Order[Int]

  // 7. But know our old `greatest` function doesn't fit, we need to modify it again :(
  //
  // def greatest[A](l: List[A])(wrap: A => Order[A]): Option[A]

  // 8. Finally, we got it! we have a modular function `greatest` that works for
  // every type we can give an order.

  // describe("greatest using adapter") {
  //   it("should work for non-empty lists") {
  //     greatest(Person("Félix", 5) ::
  //              Person("Alberto", 3) ::
  //              Person("Alfredo", 7) ::
  //              Person("Sergio", 2) :: Nil) shouldBe Option(Person("Alfredo", 7))
  //     greatest(5 :: 3 :: 7 :: 2 :: Nil) shouldBe Option(7)
  //   }
  //   it("should work for empty lists") {
  //     greatest(List.empty[Person]) shouldBe None
  //     greatest(List.empty[Int]) shouldBe None
  //   }
  // }

}
