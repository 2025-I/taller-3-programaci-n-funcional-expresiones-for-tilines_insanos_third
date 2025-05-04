# Informe sobre la funcion `definirManiobra` en Scala

## Introduccion

Este informe analiza la función `definirManiobra`, escrita en Scala como parte de un taller de programación funcional. Su propósito es encontrar una secuencia de movimientos que transforme un tren inicial (`t1`) en una configuración deseada (`t2`), utilizando una búsqueda por anchura (BFS) funcional. Se detallan la descripción del algoritmo, el estado de la pila en cada iteración, una demostración por inducción matemática de su funcionamiento, y un informe de corrección.

## Codigo Fuente

```scala
package taller
import scala.annotation.tailrec

class DefinirManiobra extends AplicarMovimientos {

  def definirManiobra(t1: Tren, t2: Tren): Maniobra = {
    @tailrec
    def aux(pendientes: List[(Estado, Maniobra)], visitados: Set[Estado]): Maniobra = {
      pendientes match {
        case Nil => Nil
        case (estadoActual@(principal, uno, dos), maniobra) :: resto =>
          if (principal == t2 && uno.isEmpty && dos.isEmpty)
            maniobra.reverse
          else if (visitados.contains(estadoActual))
            aux(resto, visitados)
          else {
            val nuevos = for {
              mov <- Movimientos(estadoActual)
              nuevoEstado = aplicarMovimiento(estadoActual, mov)
              if !visitados.contains(nuevoEstado)
            } yield (nuevoEstado, mov :: maniobra)
            aux(resto ++ nuevos, visitados + estadoActual)
          }
      }
    }

    def Movimientos(estado: Estado): List[Movimiento] = {
      val (principal, uno, dos) = estado
      val desdePrincipal = for (n <- 1 to principal.length; m <- List(Uno(n), Dos(n))) yield m
      val desdeUno       = for (n <- 1 to uno.length) yield Uno(-n)
      val desdeDos       = for (n <- 1 to dos.length) yield Dos(-n)
      (desdePrincipal ++ desdeUno ++ desdeDos).toList
    }
    aux(List(((t1, Nil, Nil), Nil)), Set.empty)
  }
}
```

## Descripción y Funcionamiento

La clase `DefinirManiobra` extiende `AplicarMovimientos`, e implementa la función `definirManiobra`, que recibe dos trenes `t1` y `t2` como entrada y devuelve una `Maniobra`, es decir, una lista de movimientos necesarios para transformar `t1` en `t2`.

El algoritmo interno utiliza una función auxiliar recursiva `aux`, que mantiene:

* `pendientes`: una lista de estados por visitar junto con la maniobra acumulada hasta ese punto,
* `visitados`: un conjunto de estados ya explorados para evitar ciclos.

Cada `Estado` se representa como una tupla `(principal, uno, dos)` que describe la ubicación de los vagones en tres sectores.

La búsqueda genera movimientos válidos desde el estado actual mediante la función `Movimientos`, y filtra los que ya fueron visitados. La recursión termina cuando el `principal` coincide con `t2` y los sectores auxiliares están vacíos.

## Tabla de Estado de la Pila (Ejemplo Abstracto)

| Iteración | Estado Actual   | Maniobra Acumulada | Estados Pendientes | Estados Visitados |
| --------- | --------------- | ------------------ | ------------------ | ----------------- |
| 1         | (t1, \[], \[])  | \[]                | \[...]             | {}                |
| 2         | (e1, \[], \[a]) | \[mov1]            | \[...]             | {(t1,\[],\[])}    |
| 3         | (e2, \[a], \[]) | \[mov2,mov1]       | \[...]             | {...}             |
| ...       | ...             | ...                | ...                | ...               |
| n         | (t2, \[], \[])  | \[movN,...,mov1]   | \[...]             | {...}             |

**Nota**: Esta tabla es representativa. Los estados concretos dependen de la entrada `t1` y el conjunto de movimientos posibles.

## Inducción Matemática de Terminación

Vamos a demostrar que la función auxiliar `aux` termina para cualquier entrada finita de trenes `t1` y `t2`, utilizando inducción estructural sobre el tamaño de la lista `pendientes`.

**Base inductiva**:

Si `pendientes` está vacía (es decir, `pendientes == Nil`), la función retorna `Nil` inmediatamente. Por lo tanto, el caso base se cumple: `aux(Nil, visitados)` termina.

**Hipótesis inductiva**:

Supongamos que para cualquier lista de longitud `k`, la función `aux(pendientes_k, visitados)` termina. Es decir, si hay `k` estados pendientes de explorar, la función finaliza tras un número finito de llamadas recursivas.

**Paso inductivo**:

Consideremos una lista `pendientes` de longitud `k + 1`. Sea su cabeza `(estadoActual, maniobra)` y su cola `resto`, con longitud `k`. Analizamos los tres casos posibles:

1. Si `estadoActual` ya está en `visitados`, se descarta y se llama recursivamente a `aux(resto, visitados)`, que tiene longitud `k`. Por la hipótesis inductiva, esta llamada termina.

2. Si `estadoActual` cumple la condición de objetivo (`principal == t2 && uno.isEmpty && dos.isEmpty`), se retorna la maniobra invertida. La función termina inmediatamente.

3. Si `estadoActual` es nuevo, se generan una cantidad finita de nuevos estados (gracias a que la cantidad de movimientos posibles es finita). Estos nuevos estados se agregan al final de la lista `pendientes` (ahora de longitud `k + n`, con `n` finito), y se invoca recursivamente a `aux`.

A su vez, en cada llamada recursiva se agrega el estado actual a `visitados`, lo que garantiza que nunca se vuelvan a visitar los mismos estados. Como el conjunto de estados posibles es finito (depende de todas las posibles combinaciones de vagones entre las tres zonas), el total de llamadas recursivas también lo es.

**Conclusión**:

Por lo tanto, cada llamada recursiva reduce efectivamente el espacio de estados no visitados, y eventualmente la lista de `pendientes` se vacía. Esto concluye la demostración por inducción de que `aux` siempre termina.

## Informe de Corrección

El algoritmo ha sido probado con una serie de tests unitarios definidos en `DefinirManiobraTest`, los cuales abarcan desde casos triviales hasta casos complejos. Se validan tanto la función `aplicarMovimientos` como `definirManiobra`, asegurando que esta última genera maniobras válidas para transformar el tren inicial en el deseado.

## Conjunto de Pruebas y Verificación

A continuación se resumen y explican los tests incluidos en el archivo `DefinirManiobraTest.scala`:

### Pruebas para `aplicarMovimientos`

| Test                  | Descripción                                         | Qué verifica                                                 |
| --------------------- | --------------------------------------------------- | ------------------------------------------------------------ |
| Juguete (10 vagones)  | Aplica 10 movimientos sobre un tren con 10 vagones. | Que se ejecuten todos los movimientos y el tren quede vacío. |
| Pequeño (100 vagones) | Aplica 100 movimientos (50 a Uno y 50 a Dos).       | Idem anterior a mayor escala.                                |
| Mediano (500 vagones) | Aplica 500 movimientos.                             | Escalabilidad intermedia.                                    |
| Grande (1000 vagones) | Aplica 1000 movimientos.                            | Escalabilidad alta.                                          |

Todos estos tests aseguran que la función `aplicarMovimientos` mantiene la coherencia del estado y respeta el número de movimientos.

### Pruebas para `definirManiobra`

| Test           | Descripción                                | Qué verifica                                      |
| -------------- | ------------------------------------------ | ------------------------------------------------- |
| Vacío          | Tren inicial y final vacíos.               | Maniobra vacía.                                   |
| Un elemento    | Un solo vagón, sin cambios.                | Maniobra vacía.                                   |
| Caso simple    | Invierte tres elementos.                   | Genera maniobra que logra la inversión.           |
| Caso complejo  | Reordena cuatro elementos arbitrariamente. | Generalidad del algoritmo.                        |
| Ya ordenado    | No requiere maniobras.                     | Correcta detección de estado objetivo.            |
| Reversibilidad | Aplica `definirManiobra` de ida y vuelta.  | Que se puede revertir el estado final al inicial. |

Estos tests validan que el algoritmo puede encontrar la secuencia correcta de movimientos, que detecta correctamente cuando no se necesitan y que puede revertir una maniobra válida.

En conjunto, estas pruebas constituyen una verificación sólida del correcto funcionamiento de `definirManiobra`, incluyendo exactitud, completitud y reversibilidad.

**Invariante de la funcion `aux`**:

* Todos los estados en `pendientes` no están en `visitados`.
* `maniobra.reverse` transforma `t1` en el estado actual.
* Cada nuevo estado generado es alcanzable desde `t1` mediante los movimientos acumulados.

**Corrección parcial**: Si se encuentra un estado donde `principal == t2` y los sectores auxiliares están vacíos, la función retorna una maniobra válida.

**Corrección total**: Como el espacio de estados es finito y se evita la revisita, la función termina siempre. Dado que se usan todos los movimientos posibles en cada paso, se garantiza que si existe una maniobra, se encontrará.

## Conclusiones

La función `definirManiobra` está diseñada como una búsqueda funcional por anchura sobre un grafo de estados de trenes. Aprovecha estructuras funcionales y evita ciclos mediante conjuntos de visitados. Su corrección y terminación están fundamentadas matemáticamente mediante una inducción sobre la longitud de `pendientes`. La función es una solución elegante y segura para hallar maniobras mínimas en un espacio de estados finito.
