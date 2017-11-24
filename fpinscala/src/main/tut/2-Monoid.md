# Problem 2: Monoid

1. So we are happy with our adapters and ready to conquer the world.
Well, let's prove it with another example. We want to implement a function `collapse`, that given a `List` of items of any type, it returns the result of reducing all those elements in an associative way. This problem is very related to Big Data and Map/Reduce Philosophy. This is its signature:

```scala
def collapse[A](l: List[A]): A

scala> collapse(List(1, 2, 3, 4))
res0: Int = 10

scala> collapse(List("hello", ", ", "world!"))
res1: String = hello, world!
```

2. Similar to what happened in Problem 1, we can not implement this function right away. However, we've already seen that adapters may be useful in this situations, so let's try it.

3. The interface we need requires 2 members:
* Combine: To combine two elements.
* Empty: A default value to return in case the `List` is empty.

This interface is called `Monoid` in maths, so we'll use that name, but don't worry about it, you know it's only a structure that reduces collections.

```tut:silent
trait Monoid[A] {
  def empty: A
  def combine(other: A): A
}

def collapse[A](l: List[A])(wrap: A => Monoid[A]): A =
  l.foldLeft[A](???)((a1, a2) => wrap(a1).combine(a2))
```

5. Houston, we have a problem. We can't provide an initial value for the `foldLeft`, because we don't have any value to "adapt". In order to call the `empty` member we need a value `A` beforehand, so that we can call `wrap` function. If you think a little bit about it, it's nonsense, the `empty` member should be static, but there is one for every instance of `A`. This is a serious limitation, adapters are coupled to values, so we can't define static members.

6. Ok, it seems what we need is an interface, but one that is not coupled to values, but a completely independent module.

```tut:silent
trait Monoid[A] {
  val empty: A
  def combine(a1: A, a2: A): A
}
```

7. We just discovered Typeclasses! As you can see, it's just an interface, same as before, but this interface is not meant to be inherited. Instead it's meant to live by its own. Let's try to use this typeclass to implement `collapse`.

```tut:silent
def collapse[A](l: List[A])(monoid: Monoid[A]): A =
  l.foldLeft(monoid.empty)(monoid.combine)
```

8. As we can see, in this case we don't need an `A` to access `Monoid` members, because `monoid` value is already an instance of the interface.

9. As an aditional advantage is that this solution is more efficient than adapters, as it only contains functions and values, no intermediate objects are created.

10. Same as adapters, we have to give implementations for the different types (`Int`, `String`)

```tut:silent
val intSumMonoid: Monoid[Int] =
  new Monoid[Int] {
    val empty: Int = 0
    def combine(i1: Int, i2: Int): Int = i1 + i2
  }

val intMulMonoid: Monoid[Int] =
  new Monoid[Int] {
    val empty: Int = 1
    def combine(i1: Int, i2: Int): Int = i1 * i2
  }

val stringMonoid: Monoid[String] =
  new Monoid[String] {
    val empty: String = ""
    def combine(s1: String, s2: String): String = s1 + s2
  }
```

11. We can have different instances for the same type, there is no issue with that. Only thing left is run the generic functions with these instances.

```tut
collapse(List(1, 2, 3, 4))(intSumMonoid)
collapse(List(1, 2, 3, 4))(intMulMonoid)
collapse(List("hello", ", ", "world!"))(stringMonoid)
```

12. By the way, it's not a coincidence that typeclass `Monoid` fits so well with `foldLeft` function, they are very related.

13. There are still a lot of syntacic improvements for typeclasses, but they are out of the scope of the course. These techniques include implicits, context bounds, syntax, derived instances, etc.

| [<< Prev: 1-Order.md](1-Order.md) | [Next: 3-Exercise1-Show.md >>](3-Show.md) |
| :--- | ---: |
