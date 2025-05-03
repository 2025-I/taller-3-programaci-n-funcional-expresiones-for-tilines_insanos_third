# Informe sobre la Función `aplicarMovimiento` en Scala

## **1. Introducción**
Este informe analiza la función `aplicarMovimiento` definida en la clase `AplicarMovimiento`. Su objetivo es modelar el movimiento de elementos (vagones[any]) entre tres listas que representan diferentes trenes: `principal`, `uno` y `dos`, según un tipo de movimiento especificado por la clase `Movimiento`. Se explican sus distintos casos de funcionamiento, se ilustran con ejemplos, se describe el estado de las listas en cada paso y se justifica su corrección mediante un análisis exhaustivo de patrones.

---

## **2. Función Principal**

La función `aplicarMovimiento` toma dos argumentos:
- `e`: una tripla `(Tren, Tren, Tren)` representando el estado actual de los tres trenes;
- `m`: una instancia de la jerarquía `Movimiento`, la cual puede ser `Uno(n)` o `Dos(n)` con `n ∈ ℤ`.

Dependiendo del valor y el signo de `n`, los elementos se trasladan entre `principal`, `uno` y `dos`.

```scala
package taller

import scala.annotation.tailrec


class  AplicarMovimiento() {
  type Vagon = Any
  type Tren = List[Vagon]
  type Estado = (Tren, Tren, Tren)

  trait Movimiento
  case class Uno(n: Int) extends Movimiento
  case class Dos(n: Int) extends Movimiento

  def aplicarMovimiento(e: Estado, m: Movimiento): Estado = {
    val (principal, uno, dos) = e  // Descomponemos el estado en sus partes (principal, uno y dos)
    m match {
      // Caso 1, Movimiento Uno(n) , n positivo
      case Uno(n) if n > 0 =>
        // Usamos una expresión for para generar los elementos a mover
        val movidos = for {
          i <- (principal.length - n) until principal.length  //Creamos un rango de índices(i) desde la longitud de principal - n hasta la longitud de principal -1
          if i >= 0 // Aseguramos que el índice no sea negativo
        } yield principal(i) // Para cada índice(i) en el rango, obtenemos el elemento correspondiente de principal
        (principal.take(principal.length - n), uno ++ movidos.toList, dos) // Devolvemos el nuevo estado, eliminamos los elementos movidos de principal y los añadimos a uno

      // Caso 2 Movimiento Uno(n) , n negativo
      case Uno(n) if n < 0 =>
        val movimientos = -n  // Convertimos los movimientos(n) a positivo
        // Usamos una expresión for para generar los elementos a mover
        val movidos = for {
          i <- 0 until math.min(movimientos, uno.length) // Creamos un rango de índices(i) desde 0 hasta la longitud de uno -1
        } yield uno(i) // Para cada índice(i) en el rango, obtenemos el elemento correspondiente de uno
        (principal ++ movidos.toList, uno.drop(math.min(movimientos, uno.length)), dos) // Devolvemos el nuevo estado, eliminamos los elementos movidos de uno y los añadimos a principal

      // Caso 3, Movimiento Dos(n) , n positivo
      case Dos(n) if n > 0 =>
        val movidos = for {
          i <- (principal.length - n) until principal.length //Creamos un rango de índices(i) desde la longitud de principal - n hasta la longitud de principal -1
          if i >= 0 // Aseguramos que el índice no sea negativo
        } yield principal(i) // Para cada índice(i) en el rango, obtenemos el elemento correspondiente de principal
        (principal.take(principal.length - n), uno, dos ++ movidos.toList) // Devolvemos el nuevo estado, eliminamos los elementos movidos de principal y los añadimos a dos

      // Caso 4, Movimiento Dos(n) , n negativo
      case Dos(n) if n < 0 =>
        val movimientos = -n  // Convertimos los movimientos(n) a positivo
        // Usamos una expresión for para generar los elementos a mover
        val movidos = for {
          i <- 0 until math.min(movimientos, dos.length) // Creamos un rango de índices(i) desde 0 hasta la longitud de dos -1
        } yield dos(i) // Para cada índice(i) en el rango, obtenemos el elemento correspondiente de dos
        (principal ++ movidos.toList, uno, dos.drop(math.min(movimientos, dos.length))) // Devolvemos el nuevo estado, eliminamos los elementos movidos de dos y los añadimos a principal

      // Caso 5, Movimiento Uno(0) o Dos(0)
      case Uno(0) => e // No se realizan movimientos, devolvemos el estado original

      // Caso 6, Movimiento Dos(0)
      case Dos(0) => e // No se realizan movimientos, devolvemos el estado original
    }
  }
```
## **3. Casos de Prueba**
```scala
class AplicarMovimientoTest extends AnyFunSuite {
  val objAplicarMovimiento = new AplicarMovimiento()

  val Movimiento: objAplicarMovimiento.Movimiento = objAplicarMovimiento.Uno(12)
  val Movimiento1: objAplicarMovimiento.Movimiento = objAplicarMovimiento.Dos(-17)
  val Movimiento2: objAplicarMovimiento.Movimiento = objAplicarMovimiento.Uno(-182)
  val Movimiento3: objAplicarMovimiento.Movimiento = objAplicarMovimiento.Dos(136)
  val Movimiento4: objAplicarMovimiento.Movimiento = objAplicarMovimiento.Uno(525)
  val Movimiento5: objAplicarMovimiento.Movimiento = objAplicarMovimiento.Dos(-576)
  val Movimiento6: objAplicarMovimiento.Movimiento = objAplicarMovimiento.Uno(-1235)
  val Movimiento7: objAplicarMovimiento.Movimiento = objAplicarMovimiento.Dos(1312)


  test("Test 1: Prueba Juguete Uno(12)") {
    val random = new Random(1) // Para que se repita la secuencia de números aleatorios
    val lista = (1 to 20).map(_ => random.nextInt(1000)).toList
    val lista2 = ('a' to 'z').map(_ => random.nextString(10)).toList
    val e0 = (List(1, 'a', 3, 'b', 89, 'c', 123, 'd', 'e', 'f', 82, 'g', 'h', 'i', 122323, 'j'), lista, lista2)
    val e1 = objAplicarMovimiento.aplicarMovimiento(e0, Movimiento)
    assert(e1 == (List(1, 'a', 3, 'b'), lista ++ List(89, 'c', 123, 'd', 'e', 'f', 82, 'g', 'h', 'i', 122323, 'j'), lista2))

  }

  test("Test 2: Prueba Juguete Dos(-17)") {
    val random = new Random(1) // Para que se repita la secuencia de números aleatorios
    val lista = (1 to 24).map(_ => random.nextInt(300)).toList
    val lista2 = ('a' to 'z').map(_ => random.nextString(21)).toList
    val e0 = (List(42, 'x', 7, 'm', 300, 'n', 888, 'p', 'q', 'r', 650, 's', 't', 'u', 99999L, 'v', 12, 'w', 73, 'y', 321, 'z'),lista2, lista)
    val e1 = objAplicarMovimiento.aplicarMovimiento(e0, Movimiento1)
    assert(e1 == (List(42, 'x', 7, 'm', 300, 'n', 888, 'p', 'q', 'r', 650, 's', 't', 'u', 99999L, 'v', 12, 'w', 73, 'y', 321, 'z') ++ lista.take(17), lista2, lista.drop(17)))

  }

  test("Test 3: Prueba Pequeña Uno(-182)") {
    val random = new Random(1) // Para que se repita la secuencia de números aleatorios
    val lista = (1 to 130).map(_ => random.nextInt(560)).toList
    val lista2 = (120 to 280).map(_ => random.nextString(200)).toList
    val lista3 = lista.take(112) ++ lista2.take(20)
    val e0 = (lista3, lista2, lista)
    val e1 = objAplicarMovimiento.aplicarMovimiento(e0, Movimiento2)
    assert(e1 == (lista3 ++ lista2.take(182), lista2.drop(182), lista))
  }

  test("Test 4: Prueba Pequeña Dos(136)") {
    val random = new Random(1) // Para que se repita la secuencia de números aleatorios
    val lista = (1 to 175).map(_ => random.nextInt(560)).toList
    val lista2 = (1 to 250).map(_ => random.nextString(230)).toList
    val lista3 = lista.take(76) ++ lista2.take(128)
    val e0 = (lista2, lista, lista3)
    val e1 = objAplicarMovimiento.aplicarMovimiento(e0, Movimiento3)
    assert(e1 == (lista2.dropRight(136), lista, lista3 ++ lista2.takeRight(136)))

  }

  test("Test 5: Prueba Mediana Uno(525)") {
    val random = new Random(1) // Para que se repita la secuencia de números aleatorios
    val lista = (1 to 653).map(_ => random.nextInt(1450)).toList
    val lista2 = (1 to 235).map(_ => random.nextString(230)).toList
    val lista3 = lista.take(133) ++ lista2.take(145) ++ lista.takeRight(279)
    val e0 = (lista, lista3, lista2)
    val e1 = objAplicarMovimiento.aplicarMovimiento(e0, Movimiento4)
    assert(e1 == (lista.dropRight(525), lista3 ++ lista.takeRight(525),lista2))

  }

  test("Test 6: Prueba Mediana Dos(-576)") {
    val random = new Random(1) // Para que se repita la secuencia de números aleatorios
    val lista = (1 to 754).map(_ => random.nextInt(1450)).toList
    val lista2 = (1 to 654).map(_ => random.nextString(230)).toList
    val lista3 = lista.take(64) ++ lista2.take(345) ++ lista.takeRight(79) ++ lista2.takeRight(233)
    val e0 = (lista2, lista, lista3)
    val e1 = objAplicarMovimiento.aplicarMovimiento(e0, Movimiento5)
    assert(e1 == (lista2 ++ lista3.take(576), lista ,lista3.drop(576)))

  }

  test("Test 7: Prueba Grande Uno(-1235)") {
    val random = new Random(1) // Para que se repita la secuencia de números aleatorios
    val lista = (1 to 2356).map(_ => random.nextInt(5000)).toList
    val lista2 = (1 to 1782).map(_ => random.nextString(230)).toList
    val lista3 = lista.take(212) ++ lista2.take(658) ++ lista.takeRight(964) ++ lista2.takeRight(653)
    val e0 = (lista3, lista2, lista)
    val e1 = objAplicarMovimiento.aplicarMovimiento(e0, Movimiento6)
    assert(e1 == (lista3 ++ lista2.take(1235), lista2.drop(1235), lista))

  }

  test("Test 8: Prueba Grande Dos(1312)") {
    val random = new Random(1) // Para que se repita la secuencia de números aleatorios
    val lista = (1 to 3242).map(_ => random.nextInt(7000)).toList
    val lista2 = (1 to 2342).map(_ => random.nextString(230)).toList
    val lista3 = lista.take(2343) ++ lista2.take(2121)
    val e0 = (lista2, lista, lista3)
    val e1 = objAplicarMovimiento.aplicarMovimiento(e0, Movimiento7)
    assert(e1 == (lista2.dropRight(1312), lista, lista3 ++ lista2.takeRight(1312)))

  }

  test("Caso Especial Uno(0)-Dos(0)") {
    val random = new Random(1) // Para que se repita la secuencia de números aleatorios
    val lista = (1 to 20).map(_ => random.nextInt(200)).toList
    val e0 = (List(1, 'a', 3, 'b', 89, 'c', 123), lista, Nil)
    val e1 = objAplicarMovimiento.aplicarMovimiento(e0, objAplicarMovimiento.Uno(0))
    val e2 = objAplicarMovimiento.aplicarMovimiento(e0, objAplicarMovimiento.Dos(0))
    assert(e1 == e0)
    assert(e2 == e0)
  }
}

```
---
## **4. Informe del proceso**

La función `aplicarMovimiento` sigue estos pasos:

1. **Descomposición del estado**: Se recibe una tupla `(principal, uno, dos)` que representa el estado del tren, donde cada componente es una lista de vagones. El segundo parámetro `m` representa el movimiento a aplicar, que puede ser `Uno(n)` o `Dos(n)`.

2. **Reconocimiento de patrones con `match`**: La función evalúa el tipo y valor de `m` con varios casos:

    * **Caso `Uno(n)` positivo**:

        * Se toman los últimos `n` elementos de `principal` (si existen) y se añaden al final de la lista `uno`.
        * Se reconstruye el estado eliminando esos elementos de `principal`.

    * **Caso `Uno(n)` negativo**:

        * Se toman los primeros `|n|` elementos de la lista `uno` (hasta un máximo de su longitud) y se añaden al final de `principal`.
        * Se actualiza `uno` eliminando esos elementos.

    * **Caso `Dos(n)` positivo**:

        * Similar a `Uno(n)` positivo, pero los elementos se mueven desde `principal` hacia `dos`.

    * **Caso `Dos(n)` negativo**:

        * Similar a `Uno(n)` negativo, pero los elementos se mueven desde `dos` hacia `principal`.

    * **Caso `Uno(0)` o `Dos(0)`**:

        * No se realiza ningún movimiento y el estado se devuelve sin cambios.

3. **Extracción de elementos con comprensión for**:

    * Se utiliza `for` junto con condiciones para seleccionar los índices válidos y extraer los vagones apropiados.
    * Esta técnica garantiza que no se acceda a índices negativos y que no se extraigan más elementos de los que existen.

4. **Mantenimiento del estado consistente**:

    * La función siempre devuelve un nuevo estado consistente, manteniendo el número total de vagones y su orden relativo, salvo por los movimientos realizados.

Esta función permite simular el comportamiento de una estación de tren con múltiples vías y movimientos de vagones de forma clara, modular y segura frente a errores de acceso a listas vacías o índices inválidos.

### **4.1. Ejemplo de Ejecución y Estado de Pila**

### **Caso de prueba: Prueba de Juguete Uno(12)**
```scala
  test("Test 1: Prueba Juguete Uno(12)") {
  val random = new Random(1) // Para que se repita la secuencia de números aleatorios
  val lista = (1 to 20).map(_ => random.nextInt(1000)).toList
  val lista2 = ('a' to 'z').map(_ => random.nextString(10)).toList
  val e0 = (List(1, 'a', 3, 'b', 89, 'c', 123, 'd', 'e', 'f', 82, 'g', 'h', 'i', 122323, 'j'), lista, lista2)
  val e1 = objAplicarMovimiento.aplicarMovimiento(e0, Movimiento)
  assert(e1 == (List(1, 'a', 3, 'b'), lista ++ List(89, 'c', 123, 'd', 'e', 'f', 82, 'g', 'h', 'i', 122323, 'j'), lista2))

}
```
### **4.1. Ejemplo de Ejecución**

#### **Caso de prueba: Prueba de Juguete Uno(12)**

| Elemento actual | List(1, 'a', 3, 'b', 89, 'c', 123, 'd', 'e', 'f', 82, 'g', 'h', 'i', 122323, 'j') | Lista generada para añadir a Uno                              | lista2 | Movimiento realizado                   |
|-----------------|-----------------------------------------------------------------------------------|---------------------------------------------------------------|--------|----------------------------------------|
|                 | [1, 'a', 3, 'b', 89, 'c', 123, 'd', 'e', 'f', 82, 'g', 'h', 'i', 122323, 'j']     |                                                               |        | Estado inicial                         |
| 'j'             | [1, 'a', 3, 'b', 89, 'c', 123, 'd', 'e', 'f', 82, 'g', 'h', 'i', 122323]          | ['j']                                                         | lista2 | Mueve 'j' desde `principal` a `uno`    |
| 122323          | [1, 'a', 3, 'b', 89, 'c', 123, 'd', 'e', 'f', 82, 'g', 'h', 'i']                  | [122323, 'j']                                                 | lista2 | Mueve 122323 desde `principal` a `uno` |
| 'i'             | [1, 'a', 3, 'b', 89, 'c', 123, 'd', 'e', 'f', 82, 'g', 'h']                       | ['i', 122323, 'j']                                            | lista2 | Mueve 'i' desde `principal` a `uno`    |
| 'h'             | [1, 'a', 3, 'b', 89, 'c', 123, 'd', 'e', 'f', 82, 'g']                            | ['h', 'i', 122323, 'j']                                       | lista2 | Mueve 'h' desde `principal` a `uno`    |
| 'g'             | [1, 'a', 3, 'b', 89, 'c', 123, 'd', 'e', 'f', 82]                                 | ['g', 'h', 'i', 122323, 'j']                                  | lista2 | Mueve 'g' desde `principal` a `uno`    |
| 82              | [1, 'a', 3, 'b', 89, 'c', 123, 'd', 'e', 'f']                                     | [82, 'g', 'h', 'i', 122323, 'j']                              | lista2 | Mueve 82 desde `principal` a `uno`     |
| 'f'             | [1, 'a', 3, 'b', 89, 'c', 123, 'd', 'e']                                          | ['f', 82, 'g', 'h', 'i', 122323, 'j']                         | lista2 | Mueve 'f' desde `principal` a `uno`    |
| 'e'             | [1, 'a', 3, 'b', 89, 'c', 123, 'd']                                               | ['e', 'f', 82, 'g', 'h', 'i', 122323, 'j']                    | lista2 | Mueve 'e' desde `principal` a `uno`    |
| 'd'             | [1, 'a', 3, 'b', 89, 'c', 123]                                                    | ['d', 'e', 'f', 82, 'g', 'h', 'i', 122323, 'j']               | lista2 | Mueve 'd' desde `principal` a `uno`    |
| 123             | [1, 'a', 3, 'b', 89, 'c']                                                         | [123, 'd', 'e', 'f', 82, 'g', 'h', 'i', 122323, 'j']          | lista2 | Mueve 123 desde `principal` a `uno`    |
| 'c'             | [1, 'a', 3, 'b', 89]                                                              | ['c', 123, 'd', 'e', 'f', 82, 'g', 'h', 'i', 122323, 'j']     | lista2 | Mueve 'c' desde `principal` a `uno`    |
| 89              | [1, 'a', 3, 'b']                                                                  | [89, 'c', 123, 'd', 'e', 'f', 82, 'g', 'h', 'i', 122323, 'j'] | lista2 | Mueve 89 desde `principal` a `uno`     |

Resultado final:

* `principal`: [1, a, 3, b]
* `uno`: lista ++ [89, c, 123, d, e, f, 82, g, h, i, 122323, j]
* `dos`: lista2

---
## **5. Informe de Corrección**

### **5.1. Inducción Matemática**

Para verificar la corrección de la función `aplicarMovimiento`, se puede utilizar una **verificación exhaustiva por casos**, ya que **no hay recursión** involucrada. La función simplemente aplica un movimiento `m` al estado `e` mediante una combinación de expresiones `for`, patrones y operaciones sobre listas. A continuación se justifican los casos principales:

---

**Caso base (sin movimiento):**

* Para `Uno(0)` y `Dos(0)`, la función retorna el estado sin cambios: `aplicarMovimiento(e, Uno(0)) == e` y `aplicarMovimiento(e, Dos(0)) == e`. Esto es correcto, ya que `0` implica que no hay elementos a mover.

---

**Casos Uno(n > 0):**

* Se trasladan `n` elementos desde el final de la lista `principal` a la lista `uno`. Se asegura que no se acceden índices negativos.
* La expresión `principal.take(principal.length - n)` elimina los últimos `n` elementos, y `uno ++ movidos.toList` los añade a `uno`.

---

**Casos Uno(n < 0):**

* Se mueven `-n` elementos desde el inicio de la lista `uno` al final de `principal`.
* Se garantiza que no se extraen más elementos que los disponibles.

---

**Casos Dos(n > 0):**

* Mismo principio que `Uno(n > 0)` pero mueve de `principal` a `dos`.

---

**Casos Dos(n < 0):**

* Mismo principio que `Uno(n < 0)` pero mueve de `dos` a `principal`.

---

En todos los casos:

* Se asegura la **inmutabilidad** de los datos.
* No se lanza ninguna excepción por acceso a índices inválidos.
* La operación es **determinista**: para una entrada fija `(e, m)`, siempre produce el mismo resultado.

Dado que no hay ninguna llamada recursiva en `aplicarMovimiento`, **la inducción matemática no es necesaria** para probar su corrección. La prueba por análisis exhaustivo de casos es suficiente.

### **Conclusión:**

La función `aplicarMovimiento` **no utiliza recursión**. Es una función de transformación de estado que se basa únicamente en:

* Expresiones `for`
* Reconocimiento de patrones (`match`)
* Operaciones sobre listas (`take`, `drop`, `++`)

Por tanto, su corrección puede demostrarse exhaustivamente analizando cada uno de los 6 casos posibles. Cada caso está correctamente definido y manipula las listas de manera segura, sin violar las condiciones establecidas en el Taller.

---

## **6. Conclusión General**

La función `aplicarMovimiento` modela correctamente el comportamiento de un sistema ferroviario en el que vagones son trasladados entre tres vías (`principal`, `uno`, `dos`).

Se destaca que:
* La implementación es clara, facilitando su comprensión.
* No usa recursión.
* Manipula listas usando expresiones funcionales simples.
* Está bien protegida contra n > nº de vagones.

