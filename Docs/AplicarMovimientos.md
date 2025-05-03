# Informe sobre la función `aplicarMovimientos` en Scala

## 1. Introducción

Este informe describe el funcionamiento de la función `AplicarMovimientos`, implementada en Scala. Esta función está diseñada para aplicar de forma secuencial una lista de movimientos sobre una estructura de datos del tipo `(Tren, Tren, Tren)`, representando el estado de un sistema ferroviario simulado. La función se implementa de forma funcional pura, utilizando recursión de cola, y permite observar paso a paso cómo se transforma el estado del sistema a medida que se aplican los movimientos. Se analiza su ejecución con un ejemplo no trivial, el comportamiento de la pila y se extraen conclusiones a partir de las pruebas realizadas.

## 2. Función principal

La función `aplicarMovimientos` toma un estado inicial `e` y una lista de movimientos `movs`, y devuelve una lista con todos los estados intermedios (incluyendo el inicial), aplicando cada movimiento secuencialmente.

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

Esta función, definida en la clase base `AplicarMovimiento`, realiza la transformación de un estado individual mediante un solo `Movimiento`. Dependiendo del tipo de movimiento (`Uno(n)` o `Dos(n)`) y del signo de `n`, se mueven `n` vagones entre las listas `principal`, `uno` y `dos`.

```scala
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
```

## 4. Funcionamiento del algoritmo

**Caso de prueba:**

```scala
val estadoInicial: Estado = (List("A", "B", "C", "D", "E"), Nil, Nil)
val movimientos = List(Uno(2), Dos(1), Uno(-1), Dos(-1))
```

**Iteración y evolución del estado:**

| Iteración | Movimiento   | principal      | uno         | dos         |
|-----------|--------------|----------------|-------------|-------------|
| 0         | Inicial      | [A, B, C, D, E] | []          | []          |
| 1         | Uno(2)       | [A, B, C]       | [D, E]      | []          |
| 2         | Dos(1)       | [A, B]          | [D, E]      | [C]         |
| 3         | Uno(-1)      | [A, B, D]       | [E]         | [C]         |
| 4         | Dos(-1)      | [A, B, D, C]    | [E]         | []          |

## 5. Estado de la pila de llamadas

```scala
aplicarMovimientosAux([Uno(2), Dos(1), Uno(-1), Dos(-1)], [estado0])
└── aplicarMovimientosAux([Dos(1), Uno(-1), Dos(-1)], [estado0, estado1])
    └── aplicarMovimientosAux([Uno(-1), Dos(-1)], [estado0, estado1, estado2])
        └── aplicarMovimientosAux([Dos(-1)], [estado0, estado1, estado2, estado3])
            └── aplicarMovimientosAux([], [estado0, ..., estado4])
```

## 6. Conclusiones basadas en las pruebas de software

Las pruebas unitarias fueron implementadas usando `ScalaTest` y `JUnitRunner`. Se consideraron distintas categorías de tamaño y combinación de movimientos:

| Tipo de prueba | Descripción                                                  | Tamaño     | Observaciones                                                  |
|----------------|--------------------------------------------------------------|------------|----------------------------------------------------------------|
| Juguete 1      | 10 vagones, 10 `Uno(1)`                                      | 10 movs    | 11 estados generados. Flujo correcto.                         |
| Juguete 2      | 10 vagones, `Uno(5)` + `Dos(5)`                              | 2 movs     | Finaliza con principal vacío y mitad en uno y dos.            |
| Pequeña 1      | 100 vagones, 100 `Uno(1)`                                    | 100 movs   | 101 estados, comportamiento estable.                          |
| Pequeña 2      | 100 vagones, 50 `Uno(2)` + 25 `Dos(2)`                       | 75 movs    | Lista de estados crece correctamente hasta 76.                |
| Mediana 1      | 500 vagones, 500 `Dos(-1)`                                   | 500 movs   | Cada uno mueve un vagón del dos al principal sin errores.     |
| Mediana 2      | 250 `Uno(2)` + 250 `Dos(2)` con 500 vagones                  | 500 movs   | Transiciones correctas, sin pérdida de datos.                 |
| Grande 1       | 1000 vagones, 1000 `Uno(1)`                                  | 1000 movs  | Procesa sin desbordamientos, lista resultante de 1001.        |
| Grande 2       | 500 `Uno(2)` + 500 `Dos(2)` con 1000 vagones                 | 1000 movs  | Ejecución eficiente y sin fallos con lista resultante de 1001.|

**Verificaciones realizadas:**

- Se comprobó que el número de estados generados es correcto: `movs.length + 1`.
- Que los vagones se distribuyen coherentemente entre las listas.
- No hubo errores de ejecución ni desbordamientos en listas grandes.
- Todas las pruebas pasaron satisfactoriamente usando `assert(...)`.

## 7. Conclusión

La función `aplicarMovimientos` implementada en Scala cumple con todos los requerimientos funcionales:

- Utiliza recursión de cola correctamente, lo que permite escalabilidad.
- Respeta el paradigma funcional al evitar mutaciones.
- Es robusta frente a entradas variadas y extensas.
- Devuelve un historial completo de estados, lo que permite trazabilidad y depuración.

Las pruebas automatizadas demostraron que se comporta de forma correcta y eficiente para casos pequeños, medianos y grandes, incluyendo patrones repetitivos y combinaciones variadas de movimientos. El diseño es modular y extensible, apto para escenarios complejos de simulación ferroviaria.