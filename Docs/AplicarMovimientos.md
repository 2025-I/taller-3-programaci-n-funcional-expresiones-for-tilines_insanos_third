# Informe sobre la función `aplicarMovimientos` en Scala

## 1. Introducción

Este informe describe el funcionamiento de la función `aplicarMovimientos`, implementada en Scala. Dicha función aplica secuencialmente una lista de movimientos sobre una estructura de tipo `(Tren, Tren, Tren)`, que representa el estado de un sistema ferroviario simulado. Se implementa siguiendo principios de programación funcional pura, utilizando recursión de cola, y permite observar paso a paso la transformación del estado del sistema. Además, se detallan pruebas unitarias desarrolladas con `ScalaTest` y `JUnit`, que validan la robustez y escalabilidad de la función.

## 2. Función principal

La función `aplicarMovimientos` toma un estado inicial `e` y una lista de movimientos `movs`, devolviendo una lista de todos los estados intermedios (incluido el inicial), aplicando cada movimiento en orden:

```scala
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
```

## 3. Función auxiliar: `aplicarMovimiento`

Esta función transforma un estado a partir de un solo movimiento. Según el tipo (`Uno(n)` o `Dos(n)`) y el signo de `n`, se mueven `n` vagones desde o hacia las pistas auxiliares.

```scala
def aplicarMovimiento(e: Estado, m: Movimiento): Estado = {
  val (principal, uno, dos) = e
  m match {
    case Uno(n) if n > 0 =>
      val movidos = principal.takeRight(n)
      (principal.dropRight(n), uno ++ movidos, dos)

    case Uno(n) if n < 0 =>
      val movimientos = -n
      val movidos = uno.take(movimientos)
      (principal ++ movidos, uno.drop(movimientos), dos)

    case Dos(n) if n > 0 =>
      val movidos = principal.takeRight(n)
      (principal.dropRight(n), uno, dos ++ movidos)

    case Dos(n) if n < 0 =>
      val movimientos = -n
      val movidos = dos.take(movimientos)
      (principal ++ movidos, uno, dos.drop(movimientos))

    case Uno(0) | Dos(0) => e
  }
}
```

## 4. Ejemplo de funcionamiento

**Caso:**  
Estado inicial: `(List("A", "B", "C", "D", "E"), Nil, Nil)`  
Movimientos: `List(Uno(2), Dos(1), Uno(-1), Dos(-1))`

**Estados intermedios:**

| Iteración | Movimiento   | principal      | uno         | dos         |
|-----------|--------------|----------------|-------------|-------------|
| 0         | Inicial      | [A, B, C, D, E] | []          | []          |
| 1         | Uno(2)       | [A, B, C]       | [D, E]      | []          |
| 2         | Dos(1)       | [A, B]          | [D, E]      | [C]         |
| 3         | Uno(-1)      | [A, B, D]       | [E]         | [C]         |
| 4         | Dos(-1)      | [A, B, D, C]    | [E]         | []          |

## 5. Estado de la pila

Gracias al uso de recursión de cola, la función `aplicarMovimientos` evita el crecimiento excesivo de la pila. Todas las llamadas se ejecutan de forma lineal en espacio constante para la pila:

```text
aplicarMovimientosAux(movs, List(e0))
→ aplicarMovimientosAux(tail, acc :+ nuevoEstado)
→ …
```

## 6. Pruebas unitarias

Las siguientes pruebas fueron ejecutadas usando `ScalaTest` con `JUnitRunner`. Se utilizaron listas con caracteres repetidos para facilitar la verificación estructural:

| Nombre de prueba             | Descripción                                       | Tamaño    | Resultado esperado                            |
|-----------------------------|---------------------------------------------------|-----------|-----------------------------------------------|
| Tamaño 20                   | 20 veces `Uno(1)`                                 | 20 movs   | 21 estados generados                          |
| Tamaño 100                  | 50 `Uno(2)` + 25 `Dos(2)`                         | 75 movs   | Estado final: principal vacío                 |
| Tamaño 1000                 | Alternancia de `Uno(1)` y `Dos(1)`                | 1000 movs | 1001 estados generados                        |
| Tamaño 5000                 | 2500 `Uno(2)` + 2500 `Dos(2)`                     | 5000 movs | Estado final: principal vacío                 |
| Tamaño 10000                | 10000 veces `Dos(-1)`                             | 10000 movs| 10001 estados generados                       |

**Verificaciones realizadas:**

- Conteo de estados: `movs.length + 1`
- Estado final coherente: sin pérdida de elementos
- Distribución correcta entre pistas
- Comportamiento estable con grandes volúmenes de datos
- No se produjeron errores ni desbordamientos

## 7. Conclusión

La función `aplicarMovimientos` es:

- **Escalable:** gracias a la recursión de cola, maneja listas de hasta 10,000 elementos sin problemas.
- **Funcionalmente pura:** no usa mutaciones ni efectos secundarios.
- **Correcta y trazable:** cada estado intermedio queda registrado.
- **Robusta:** pasó todas las pruebas automatizadas con distintos patrones de entrada.

Esto la hace ideal para simulaciones de sistemas ferroviarios o similares que requieren un historial completo de transformación de estados.
