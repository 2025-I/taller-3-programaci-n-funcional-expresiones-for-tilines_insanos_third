
# Taller 3 - Programación Funcional en Scala

## Introducción

Este taller tiene como objetivo aplicar los conceptos fundamentales de la programación funcional en Scala a través de la simulación de maniobras de trenes. Se trabaja con tres vías (principal, uno y dos) y una serie de movimientos que simulan el traslado de vagones entre estas vías.

El enfoque funcional implica:
- **Inmutabilidad** de los datos.
- **Transformaciones puras**, sin efectos secundarios.
- Uso de **expresiones `for`** en lugar de bucles imperativos.
- Aplicación de **recursión de cola** para evitar desbordamientos de pila.
- Modelado del dominio con **tipos algebraicos** (traits y case classes).

El propósito es entrenar el pensamiento declarativo y mejorar la capacidad para resolver problemas con funciones puras y estructuras inmutables.

---

## Código Fuente y Explicación

### Clase `AplicarMovimiento`

Esta clase define el tipo de estado del sistema, los tipos de movimiento, y la función principal `aplicarMovimiento` que transforma un estado según un movimiento.

```scala
package taller

class AplicarMovimiento {
  type Vagon = Any
  type Tren = List[Vagon]
  type Estado = (Tren, Tren, Tren)
  type Maniobra = List[Movimiento]

  trait Movimiento
  case class Uno(n: Int) extends Movimiento
  case class Dos(n: Int) extends Movimiento

  def aplicarMovimiento(e: Estado, m: Movimiento): Estado = {
    val (principal, uno, dos) = e
    m match {
      case Uno(n) if n > 0 =>
        val movidos = for {
          i <- (principal.length - n) until principal.length
          if i >= 0
        } yield principal(i)
        (principal.take(principal.length - n), uno ++ movidos.toList, dos)

      case Uno(n) if n < 0 =>
        val movimientos = -n
        val movidos = for {
          i <- 0 until math.min(movimientos, uno.length)
        } yield uno(i)
        (principal ++ movidos.toList, uno.drop(math.min(movimientos, uno.length)), dos)

      case Dos(n) if n > 0 =>
        val movidos = for {
          i <- (principal.length - n) until principal.length
          if i >= 0
        } yield principal(i)
        (principal.take(principal.length - n), uno, dos ++ movidos.toList)

      case Dos(n) if n < 0 =>
        val movimientos = -n
        val movidos = for {
          i <- 0 until math.min(movimientos, dos.length)
        } yield dos(i)
        (principal ++ movidos.toList, uno, dos.drop(math.min(movimientos, dos.length)))

      case Uno(0) => e
      case Dos(0) => e
    }
  }
}
```

#### Explicación de Funcionamiento

##### `Uno(n)` positivo
- Mueve `n` vagones del final de la vía principal a la vía uno.
- Se usa una expresión `for` para recolectar los últimos `n` elementos.
- Se reconstruyen las listas sin modificar las originales (`take`, `++`).

##### `Uno(n)` negativo
- Mueve `-n` vagones desde el inicio de la vía uno al final de la principal.
- Se usa `drop` para eliminar los elementos que se movieron de la vía uno.

##### `Dos(n)` positivo
- Mismo proceso que `Uno(n)` positivo, pero moviendo los vagones a la vía dos.

##### `Dos(n)` negativo
- Mueve `-n` vagones desde la vía dos al final de la vía principal.

##### `Uno(0)` y `Dos(0)`
- Casos neutros, el estado se mantiene igual.

---

### Clase `AplicarMovimientos`

Esta clase extiende `AplicarMovimiento` y permite aplicar múltiples movimientos en secuencia, acumulando los estados intermedios.

```scala
package taller
import scala.annotation.tailrec

class AplicarMovimientos extends AplicarMovimiento {

  def aplicarMovimientos(e: Estado, movs: List[Movimiento]): List[Estado] = {
    @tailrec
    def aplicarMovimientosAux(movs: List[Movimiento], acc: List[Estado]): List[Estado] = movs match {
      case Nil => acc
      case head :: tail =>
        val nuevoEstado = aplicarMovimiento(acc.last, head)
        aplicarMovimientosAux(tail, acc :+ nuevoEstado)
    }

    aplicarMovimientosAux(movs, List(e))
  }
}
```

#### Explicación de Funcionamiento

- Se define una función recursiva auxiliar `aplicarMovimientosAux` que va acumulando los estados resultantes de aplicar cada movimiento.
- Usa `@tailrec` para asegurar optimización de recursión.
- Cada movimiento se aplica al **último estado calculado** (`acc.last`).
- El resultado es una **lista de estados** que muestra la evolución completa del sistema.

---

## Tabla de Integrantes

| Nombre Completo          | Código        |
|--------------------------|---------------|
| [Jorman Alexis Botero]   | [Código aquí] |
| [Joseph Gabriel Lerma]   | [Código aquí] |
| [Jhon Sebastian Londoño] | [2359589]     |

*Rellena esta tabla con los nombres y códigos de los integrantes del grupo.*

---

## Conclusiones

Este taller permitió reforzar el uso de programación funcional en Scala mediante:

- El diseño de estructuras de datos inmutables para representar estados y movimientos.
- El uso de expresiones `for` para manipular listas sin usar bucles imperativos.
- El modelado de la lógica de negocio usando `match` y `case class`.
- La implementación de funciones puras, predecibles y fácilmente testeables.
- La aplicación de recursión de cola para procesos iterativos, como en la aplicación de múltiples movimientos.

El ejercicio combina teoría y práctica funcional, promoviendo un enfoque claro, declarativo y seguro para resolver problemas de manipulación de datos en Scala.
