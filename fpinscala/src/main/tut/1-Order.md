# Problema 1: Order

1. We want to implement a function greatest, that given a `List` of items of any kind, it returns the greatest among them, wrapped in an `Option`. If the list is empty, it returns `None`. This is its signature:

```scala
def greatest[A](l: List[A]): Option[A]

scala> greatest(List(Person("Ana", 28), Person("Berto", 35), Person("Carlos", 18)))
res0: Option[Person] = Some(Person(Berto,35))

scala> greatest(List(28, 35, 1))
res1: Option[Int] = Some(35)

scala> greatest(List("Berto", "Carlos", "Ana"))
res2: Option[String] = Some("Carlos")
```

2. As you may find out, we can't give an implementation of this function, because we know nothing about type `A`. In traditional OOP world, we may use an interface to adquire some extra info from `A` so that we can implement it. Let's see...

3. We'll begin with an old good OOP interface that allows us to compare/order elements. Interfaces may be partially (or totally) implemented in Scala.

```tut:silent
trait Order[A] {
  def compare(other: A): Int

  def >(other: A): Boolean = compare(other) > 0
  def ===(other: A): Boolean = compare(other) == 0
  def <(other: A): Boolean = compare(other) < 0
}
```

4. Let's try to implement now the function `greatest`, asking the list's elements to be subtype of `Order` interface.

```tut:silent
def greatest[A <: Order[A]](l: List[A]): Option[A] =
  l.foldLeft(Option.empty[A]) {
    case (Some(max), a) if a < max => Option(max)
    case (_, a) => Option(a)
  }
```

5. Let's check that this function is correct, to do so we will create a type `Person` implementing `Order` interface.

```tut:silent
case class Person(name: String, age: Int) extends Order[Person] {
  def compare(other: Person) = age - other.age
}
```

6. So we have all pieces of the jigsaw, we just need to put them in place, i.e., run the function with a list of `Person`s and see if the result is as expected.

```tut
greatest(List(Person("Ana", 28), Person("Berto", 35), Person("Carlos", 18)))
```

7. Good, it seems it's working, but... What happens if we can not make our type to be subtype of the interface? This happens when the type is out of our control, due to it being in another third-party library.

```tut:fail
greatest(List(2, 3, 1))
```

8. There is another OOP design pattern to solve this issue, the adapter pattern, also called wrapper pattern. Let's explore that solution by defining the function `greatest` in terms of an adapter.

```tut:silent
def greatest[A](l: List[A])(wrap: A => Order[A]): Option[A] =
  l.foldLeft(Option.empty[A]) {
    case (Some(max), a) if wrap(a) < max => Option(max)
    case (_, a) => Option(a)
  }
```

9. Only thing left is to give an adapter for `Int`s and run the function with it.

```tut
case class IntOrder(unwrap: Int) extends Order[Int] {
  def compare(other: Int) = unwrap - other
}
greatest(List(2, 3, 1))(IntOrder(_))
```

10. We can still run the previous example with `Person` by passing the `identity` function to the adapter.

```tut
greatest(List(Person("Ana", 28), Person("Berto", 35), Person("Carlos", 18)))(identity)
```

11. This pattern has an efficiency issue given that we need to create intermediate objects, one for every element in the `List`. But we'll not focus on it for now. The problem is solved, `greatest` function is generic and we can use it for any type for which we can implement the function `compare`.

| [^^ Up: README.md](README.md) | [Next: 2-Monoid.md >>](2-Monoid.md) |
| :--- | ---: |
