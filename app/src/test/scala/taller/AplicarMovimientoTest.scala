package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import scala.util.Random

@RunWith(classOf[JUnitRunner])
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



