# Demostración Formal de la Correctitud de `aplicarMovimientos` (Inducción Estructural)

## 4. Correctitud por Inducción Matemática

Queremos demostrar que la función `aplicarMovimientos` devuelve una **secuencia de estados** donde cada estado es el resultado correcto de aplicar secuencialmente una lista de movimientos a un estado inicial.

La propiedad que queremos demostrar es que, para una lista de movimientos `ms`, y un estado inicial `e0`, el resultado de `aplicarMovimientos(e0, ms)` es la secuencia:

```scala
List(e0, aplicarMovimiento(e0, ms(0)), aplicarMovimiento(...), ..., estadoFinal)
```

---

### Caso Base: `ms = Nil`

Si la lista de movimientos está vacía, entonces:

```scala
aplicarMovimientos(e0, Nil) = List(e0)
```

Esto es correcto por definición: no hay movimientos que aplicar, por lo tanto el único estado es el inicial.

---

### Paso Inductivo: `ms = m :: msTail`

Supongamos que la función funciona correctamente para listas de movimientos de tamaño `n`, es decir:

> Para una lista de movimientos de tamaño `n`, `aplicarMovimientos(e0, ms)` devuelve la secuencia correcta de estados intermedios.

Queremos demostrar que también funciona para una lista de tamaño `n + 1`.

#### Hipótesis de Inducción

Sea `msTail` una lista de tamaño `n`, y supongamos que:

```scala
aplicarMovimientos(e0, msTail) = List(e0, e1, ..., en)
```

donde cada `ei = aplicarMovimiento(e(i-1), msTail(i-1))`.

#### Queremos demostrar que:

```scala
aplicarMovimientos(e0, m :: msTail) = List(e0, e1, ..., en, aplicarMovimiento(en, m))
```

---

### Análisis de la Ejecución

La implementación de `aplicarMovimientos` es:

```scala
@tailrec
def aplicarMovimientosAux(movs: List[Movimiento], acc: List[Estado]): List[Estado] = movs match {
  case Nil => acc
  case head :: tail =>
    val nuevoEstado = aplicarMovimiento(acc.last, head)
    aplicarMovimientosAux(tail, acc :+ nuevoEstado)
}
```

Esto significa:

- Se parte del estado inicial `e0` en `acc`.
- En cada paso, se aplica `aplicarMovimiento` al último estado acumulado (`acc.last`) y se agrega el nuevo estado al acumulador.

Por construcción, cada nuevo estado es:

```scala
ei+1 = aplicarMovimiento(ei, m(i))
```

y se mantiene en orden secuencial.

---

### Conclusión

Por **inducción estructural sobre la lista de movimientos**, se cumple que:

- **Caso base**: Una lista vacía de movimientos produce solo el estado inicial.
- **Paso inductivo**: Al agregar un nuevo movimiento `m` al final de una secuencia válida, se genera un nuevo estado válido aplicando `aplicarMovimiento`.

**Por lo tanto, hemos demostrado que:**

```text
∀ lista de movimientos ms, aplicarMovimientos(e0, ms) devuelve la secuencia de estados correctos.
```

La función `aplicarMovimientos` es **correcta** con respecto a la aplicación secuencial de movimientos.
