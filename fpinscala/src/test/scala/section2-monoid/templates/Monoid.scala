package org.hablapps.typeclasses
package section2

import org.scalatest._

package object templates extends MonoidSpec

class MonoidSpec extends FunSpec with Matchers {

  // 1. So we are happy with our adapters and ready to conquer the world.
  // Well, let's prove it with another example.
  //
  // This time we want to implement a function with the following signature:
  //
  // def collapse[A](l: List[A]): A

  // 2. We quickly realize that without further info we can't do it, but wait,
  // we have our marvellous adapters. If I know how to combine to elements
  // I can collapse the whole list no matter how long it is.

  // trait Monoid[A]
  //
  // implicit class IntMonoidAdapter(unwrap: Int) extends Monoid[Int]
  //
  // def collapse[A](l: List[A])(wrap: A => Monoid[A]): A =

  // 3. Oops! With adapters we can't make use of static values! There's no way
  // we can implement this function with adapters. Gentlemen, I present you
  // the pattern of patterns... Typeclass!
  // The problem with adapters is that it's coupled/attached with a value,
  // so we can define members that are independent of the concrete instance.
  // Let's fix this

  // 4. Now that we have decoupled the functionality from the value, we can
  // implement the `collapse` method without issues

  // def collapse[A](l: List[A]): A

  // 5. As we did with adapters, now we have to give instances for the types
  // we are interested on. These instances are not going to be classes but
  // values/objects, because they are no longer tied with a value.

  // val intMonoid: Monoid[Int]

  // 6. Last thing is to put all together and run the function with the instance
  it("collapse should work") {
    // collapse(1 :: 2 :: 3 :: 4 :: Nil)(intMonoid) shouldBe ???
  }

}
