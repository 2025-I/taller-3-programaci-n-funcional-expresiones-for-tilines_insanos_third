package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AplicarMovimientoTest extends AnyFunSuite {
  val objAplicarMovimiento = new AplicarMovimiento()

  val Movimiento: objAplicarMovimiento.Movimiento = objAplicarMovimiento.Uno(2)
  val Movimiento2: objAplicarMovimiento.Movimiento = objAplicarMovimiento.Dos(3)

  test("Test 2: Movimiento Uno(2) desde principal a uno") {
    val e1 = (List('a', 'b', 'c', 'd'), Nil, Nil)
    val e2 = objAplicarMovimiento.aplicarMovimiento(e1, Movimiento)
    assert(e2 == (List('a', 'b'), List('c', 'd'), Nil))
  }

  test("Test 3: Movimiento Dos(3) desde principal a dos") {
    val e2 = (List('a', 'b'), List('c', 'd'), Nil)
    val e3 = objAplicarMovimiento.aplicarMovimiento(e2, Movimiento2)
    assert(e3 == (Nil, List('c', 'd'), List('a', 'b')))
  }

  test("Test 4: Movimiento Dos(-1) desde dos a principal") {
    val e3 = (Nil, List('c', 'd'), List('a', 'b'))
    val e4 = objAplicarMovimiento.aplicarMovimiento(e3, objAplicarMovimiento.Dos(-1))
    assert(e4 == (List('a'), List('c', 'd'), List('b')))
  }

  test("Test 5: Movimiento Uno(-2) desde uno a principal") {
    val e4 = (List('a'), List('c', 'd'), List('b'))
    val e5 = objAplicarMovimiento.aplicarMovimiento(e4, objAplicarMovimiento.Uno(-2))
    assert(e5 == (List('a', 'c', 'd'), Nil, List('b')))
  }
}