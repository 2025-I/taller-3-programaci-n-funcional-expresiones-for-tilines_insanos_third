package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AplicarMovimientosTest extends AnyFunSuite {
  // Instancia de la clase que vamos a probar
  val obj = new AplicarMovimientos()

  // Importamos para facilitar la creación de movimientos y estados
  import obj.{Uno, Dos, Estado, Movimiento}

  test("Juguete 1: 10 vagones con 10 Uno(1)") {
    val e0 = (List.fill(10)('A'), Nil, Nil)
    val movs = List.fill(10)(Uno(1))
    val resultado = obj.aplicarMovimientos(e0, movs)
    assert(resultado.size == 11)
  }

  test("Juguete 2: 10 vagones con movimientos alternados Uno y Dos") {
    val e0 = (List.fill(10)('B'), Nil, Nil)
    val movs = List(Uno(5), Dos(5))
    val resultado = obj.aplicarMovimientos(e0, movs)
    assert(resultado.last == (Nil, List.fill(5)('B'), List.fill(5)('B')))
  }

  test("Pequeña 1: 100 vagones con 100 Uno(1)") {
    val e0 = (List.fill(100)('X'), Nil, Nil)
    val movs = List.fill(100)(Uno(1))
    val resultado = obj.aplicarMovimientos(e0, movs)
    assert(resultado.size == 101)
  }

  test("Pequeña 2: 100 vagones con 50 Uno(2) y 25 Dos(2)") {
    val e0 = (List.fill(100)('Y'), Nil, Nil)
    val movs = List.fill(50)(Uno(2)) ++ List.fill(25)(Dos(2))
    val resultado = obj.aplicarMovimientos(e0, movs)
    assert(resultado.size == 76)
  }

  test("Mediana 1: 500 vagones con 500 Dos(-1)") {
    val e0 = (List.fill(500)('M'), Nil, Nil)
    val movs = List.fill(500)(Dos(-1))
    val resultado = obj.aplicarMovimientos(e0, movs)
    assert(resultado.size == 501)
  }

  test("Mediana 2: 250 Uno(2) + 250 Dos(2) con 500 vagones") {
    val e0 = (List.fill(500)('N'), Nil, Nil)
    val movs = List.fill(250)(Uno(2)) ++ List.fill(250)(Dos(2))
    val resultado = obj.aplicarMovimientos(e0, movs)
    assert(resultado.size == 501)
  }

  test("Grande 1: 1000 vagones con 1000 Uno(1)") {
    val e0 = (List.fill(1000)('Z'), Nil, Nil)
    val movs = List.fill(1000)(Uno(1))
    val resultado = obj.aplicarMovimientos(e0, movs)
    assert(resultado.size == 1001)
  }

  test("Grande 2: 500 Uno(2) y 500 Dos(2)") {
    val e0 = (List.fill(1000)('W'), Nil, Nil)
    val movs = List.fill(500)(Uno(2)) ++ List.fill(500)(Dos(2))
    val resultado = obj.aplicarMovimientos(e0, movs)
    assert(resultado.size == 1001)
  }}