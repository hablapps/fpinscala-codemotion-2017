package org.hablapps.typeclasses
package section2
package code

import org.scalatest._

class MonoidSpec extends FunSpec with Matchers {

  object Step1 {
    // 1. So we are happy with our adapters and ready to conquer the world.
    // Well, let's prove it with another example.
    //
    // This time we want to implement a function with the following signature:
    //
    // def collapse[A](l: List[A]): A

    // 2. We quickly realize that without further info we can't do it, but wait,
    // we have our marvellous adapters. If I know how to combine to elements
    // I can collapse the whole list no matter how long it is.

    trait Monoid[A] {
      def combine(other: A): A
      def zero: A
    }

    implicit class IntMonoidAdapter(unwrap: Int) extends Monoid[Int] {
      def combine(other: Int) = unwrap + other
      def zero = 0
    }

    def collapse[A <% Monoid[A]](l: List[A]): A = l match {
      case h :: t => h combine collapse(t)
      case Nil => ??? // we can't access zero without having a value
    }
  }

  // 3. Oops! With adapters we can't make use of static values! There's no way
  // we can implement this function with adapters. Gentlemen, I present you
  // the pattern of patterns... Typeclass!
  // The problem with adapters is that it's coupled/attached with a value,
  // so we can define members that are independent of the concrete instance.
  // Let's fix this

  trait Monoid[A] {
    def combine(a1: A, a2: A): A
    def zero: A
  }

  object Step2 {
    // 4. Now that we have decoupled the functionality from the value, we can
    // implement the `collapse` method without issues

    def collapse[A](l: List[A])(ev: Monoid[A]): A = l match {
      case h :: t => ev.combine(h, collapse(t)(ev))
      case Nil => ev.zero
    }

    // 5. As we did with adapters, now we have to give instances for the types
    // we are interested on. These instances are not going to be classes but
    // values/objects, because they are no longer tied with a value.

    val intMonoid = new Monoid[Int] {
      def combine(a1: Int, a2: Int) = a1 + a2
      def zero = 0
    }

    // 6. Last thing is to put all together and run the function with the instance
    it("collapse should work") {
      collapse(1 :: 2 :: 3 :: 4 :: Nil)(intMonoid) shouldBe 10
    }
  }

  // 7. Again, same as with adapters, type classes have its own sugar, and it's
  // very similar. Let's start by using implicits.

  implicit val intMonoid = new Monoid[Int] {
    def combine(a1: Int, a2: Int) = a1 + a2
    def zero = 0
  }

  object Step3 {
    def collapse[A](l: List[A])(implicit ev: Monoid[A]): A = l match {
      case h :: t => ev.combine(h, collapse(t)(ev))
      case Nil => ev.zero
    }
  }

  it("collapse should work") {
    collapse(1 :: 2 :: 3 :: 4 :: Nil) shouldBe 10
  }

  // 8. We can also give syntax to access to the type class methods directly
  // bypassing the evidence. This allows us to use context bounds.
  def combine[A](a1: A, a2: A)(implicit ev: Monoid[A]) = ev.combine(a1, a2)
  def zero[A](implicit ev: Monoid[A]) = ev.zero

  object Step4 {
    def collapse[A: Monoid](l: List[A]): A = l match {
      case h :: t => combine(h, collapse(t))
      case Nil => zero[A]
    }
  }

  // 9. We can also use adapters to add operator syntax where we want. For
  // instance, give an infix operator `|+|` for method `combine`
  implicit class MonoidOps[A](a1: A)(implicit ev: Monoid[A]) {
    def |+|(a2: A) = ev.combine(a1, a2)
  }
  def collapse[A: Monoid](l: List[A]): A = l match {
    case h :: t => h |+| collapse(t) // combine(h, collapse(t))
    case Nil => zero[A]
  }

}
