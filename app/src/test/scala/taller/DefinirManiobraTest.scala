package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DefinirManiobraTest extends AnyFunSuite {

  val obj = new DefinirManiobra()
  import obj.{Movimiento, Uno, Dos, Estado, Tren}
  import obj.aplicarMovimientos
  import obj.definirManiobra

  // Pruebas para aplicarMovimientos
  test("Pruebas de juguete: 10 vagones y 10 movimientos") {
    val estadoInicial: Estado = (List.range(1, 11), Nil, Nil)
    val movimientos: List[Movimiento] = List.fill(5)(Uno(2)) ++ List.fill(5)(Dos(-1))
    val resultado = aplicarMovimientos(estadoInicial, movimientos)
    assert(resultado.size == 11)
    assert(resultado.head == estadoInicial)
    assert(resultado.last._1.size == 0) // Todos movidos a vías auxiliares
  }

  test("Pruebas pequeñas: 100 vagones y 100 movimientos") {
    val estadoInicial: Estado = (List.range(1, 101), Nil, Nil)
    val movimientos: List[Movimiento] = List.fill(50)(Uno(2)) ++ List.fill(50)(Dos(-1))
    val resultado = aplicarMovimientos(estadoInicial, movimientos)
    assert(resultado.size == 101)
    assert(resultado.head == estadoInicial)
    assert(resultado.last._1.size == 0)
  }

  test("Pruebas medianas: 500 vagones y 500 movimientos") {
    val estadoInicial: Estado = (List.range(1, 501), Nil, Nil)
    val movimientos: List[Movimiento] = List.fill(250)(Uno(2)) ++ List.fill(250)(Dos(-1))
    val resultado = aplicarMovimientos(estadoInicial, movimientos)
    assert(resultado.size == 501)
    assert(resultado.head == estadoInicial)
    assert(resultado.last._1.size == 0)
  }

  test("Pruebas grandes: 1000 vagones y 1000 movimientos") {
    val estadoInicial: Estado = (List.range(1, 1001), Nil, Nil)
    val movimientos: List[Movimiento] = List.fill(500)(Uno(2)) ++ List.fill(500)(Dos(-1))
    val resultado = aplicarMovimientos(estadoInicial, movimientos)
    assert(resultado.size == 1001)
    assert(resultado.head == estadoInicial)
    assert(resultado.last._1.size == 0)
  }

  // Pruebas para definirManiobra
  test("Definir maniobra para lista vacía") {
    val t1: Tren = Nil
    val t2: Tren = Nil
    val movimientos = definirManiobra(t1, t2)
    assert(movimientos.isEmpty)
  }

  test("Definir maniobra para lista con un elemento") {
    val t1: Tren = List(1)
    val t2: Tren = List(1)
    val movimientos = definirManiobra(t1, t2)
    assert(movimientos.isEmpty)
  }

  test("Definir maniobra genera movimientos correctos (caso simple)") {
    val t1: Tren = List(1, 2, 3)
    val t2: Tren = List(3, 2, 1)
    val movimientos = definirManiobra(t1, t2)
    val estados = aplicarMovimientos((t1, Nil, Nil), movimientos)
    assert(estados.last == (t2, Nil, Nil))
  }

  test("Definir maniobra genera movimientos correctos (caso complejo)") {
    val t1: Tren = List('a', 'b', 'c', 'd')
    val t2: Tren = List('d', 'b', 'c', 'a')
    val movimientos = definirManiobra(t1, t2)
    val estados = aplicarMovimientos((t1, Nil, Nil), movimientos)
    assert(estados.last == (t2, Nil, Nil))
  }

  test("Definir maniobra mantiene orden si ya está ordenado") {
    val t1: Tren = List(1, 2, 3)
    val t2: Tren = List(1, 2, 3)
    val movimientos = definirManiobra(t1, t2)
    val estados = aplicarMovimientos((t1, Nil, Nil), movimientos)
    assert(movimientos.isEmpty)
    assert(estados.last == (t2, Nil, Nil))
  }


  // Prueba de reversibilidad (si aplica)
  test("Maniobra reversible simple") {
    val t1 = List(1, 2, 3)
    val t2 = List(3, 2, 1)
    val m1 = definirManiobra(t1, t2)
    val e1 = aplicarMovimientos((t1, Nil, Nil), m1)
    val m2 = definirManiobra(t2, t1)
    val e2 = aplicarMovimientos(e1.last, m2)
    assert(e2.last == (t1, Nil, Nil))
  }
}