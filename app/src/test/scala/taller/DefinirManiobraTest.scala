package taller


import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DefinirManiobraTest extends AnyFunSuite {

  val maniobra = new DefinirManiobra()

  test("Prueba de juguete: Inversión de tren simple") {
    val entrada = List('a', 'b')
    val salida = List('b', 'a')
    val resultado = maniobra.definirManiobra(entrada, salida)
    val estados = maniobra.aplicarMovimientos((entrada, Nil, Nil), resultado)
    assert(estados.last._1 == entrada)
  }


  test("Prueba pequeña: Reorganización de 100 vagones") {
    val entrada = (1 to 100).toList
    val salida = entrada.reverse
    val resultado = maniobra.definirManiobra(entrada, salida)
    val estados = maniobra.aplicarMovimientos((entrada, Nil, Nil), resultado)
    assert(estados.last._1 == entrada)
  }

  test("Prueba mediana: Reorganización de 500 vagones") {
    val entrada = (1 to 500).toList
    val salida = entrada.reverse
    val resultado = maniobra.definirManiobra(entrada, salida)
    val estados = maniobra.aplicarMovimientos((entrada, Nil, Nil), resultado)
    assert(estados.last._1 == entrada)
  }

  test("Prueba grande: Reorganización de 1000 vagones") {
    val entrada = (1 to 1000).toList
    val salida = entrada.reverse
    val resultado = maniobra.definirManiobra(entrada, salida)
    val estados = maniobra.aplicarMovimientos((entrada, Nil, Nil), resultado)
    assert(estados.last._1 == entrada)
  }
}