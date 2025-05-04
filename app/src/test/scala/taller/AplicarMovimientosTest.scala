package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AplicarMovimientosTest extends AnyFunSuite {
  val obj = new AplicarMovimientos()
  import obj.{Movimiento, Uno, Dos}

  // Tamaño 20
  test("Tamaño 20: 20 Uno(1)") {
    val e0 = (List.fill(20)('A'), Nil, Nil)
    val movs = List.fill(20)(Uno(1))
    val resultado = obj.aplicarMovimientos(e0, movs)
    assert(resultado.size == 21)
  }

  // Tamaño 100
  test("Tamaño 100: 50 Uno(2), 25 Dos(2)") {
    val e0 = (List.fill(100)('B'), Nil, Nil)
    val movs = List.fill(50)(Uno(2)) ++ List.fill(25)(Dos(2))
    val resultado = obj.aplicarMovimientos(e0, movs)
    assert(resultado.last._1.isEmpty)
  }

  // Tamaño 1,000
  test("Tamaño 1000: alternar Uno(1), Dos(1)") {
    val e0 = (List.fill(1000)('C'), Nil, Nil)
    val movs = List.tabulate(1000)(i => if (i % 2 == 0) Uno(1) else Dos(1))
    val resultado = obj.aplicarMovimientos(e0, movs)
    assert(resultado.size == 1001)
  }

  // Tamaño 5,000
  test("Tamaño 5000: 2500 Uno(2), 2500 Dos(2)") {
    val e0 = (List.fill(5000)('D'), Nil, Nil)
    val movs = List.fill(2500)(Uno(2)) ++ List.fill(2500)(Dos(2))
    val resultado = obj.aplicarMovimientos(e0, movs)
    assert(resultado.last._1.isEmpty)
  }

  // Tamaño 10,000
  test("Tamaño 10000: 10000 Dos(-1)") {
    val e0 = (List.fill(10000)('E'), Nil, Nil)
    val movs = List.fill(10000)(Dos(-1))
    val resultado = obj.aplicarMovimientos(e0, movs)
    assert(resultado.size == 10001)
  }

}
