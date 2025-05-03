package taller

class  AplicarMovimiento {
  type Vagon = Any
  type Tren = List[Vagon]
  type Estado = (Tren, Tren, Tren)

  trait Movimiento

  case class Uno(n: Int) extends Movimiento

  case class Dos(n: Int) extends Movimiento

  def aplicarMovimiento(e: Estado, m: Movimiento): Estado = {
    val (principal, uno, dos) = e // Descomponemos el estado en sus partes (principal, uno y dos)
    m match {
      // Caso 1, Movimiento Uno(n) , n positivo
      case Uno(n) if n > 0 =>
        // Usamos una expresión for para generar los elementos a mover
        val movidos = for {
          i <- (principal.length - n) until principal.length //Creamos un rango de índices(i) desde la longitud de principal - n hasta la longitud de principal -1
          if i >= 0 // Aseguramos que el índice no sea negativo
        } yield principal(i) // Para cada índice(i) en el rango, obtenemos el elemento correspondiente de principal
        (principal.take(principal.length - n), uno ++ movidos.toList, dos) // Devolvemos el nuevo estado, eliminamos los elementos movidos de principal y los añadimos a uno

      // Caso 2 Movimiento Uno(n) , n negativo
      case Uno(n) if n < 0 =>
        val movimientos = -n // Convertimos los movimientos(n) a positivo
        // Usamos una expresión for para generar los elementos a mover
        val movidos = for {
          i <- 0 until math.min(movimientos, uno.length) // Creamos un rango de índices(i) desde 0 hasta la longitud de uno -1
        } yield uno(i) // Para cada índice(i) en el rango, obtenemos el elemento correspondiente de uno
        (principal ++ movidos.toList, uno.drop(math.min(movimientos, uno.length)), dos) // Devolvemos el nuevo estado, eliminamos los elementos movidos de uno y los añadimos a principal

      // Caso 3, Movimiento Dos(n) , n positivo
      case Dos(n) if n > 0 =>
        val movidos = for {
          i <- (principal.length - n) until principal.length //Creamos un rango de índices(i) desde la longitud de principal - n hasta la longitud de principal -1
          if i >= 0 // Aseguramos que el índice no sea negativo
        } yield principal(i) // Para cada índice(i) en el rango, obtenemos el elemento correspondiente de principal
        (principal.take(principal.length - n), uno, dos ++ movidos.toList) // Devolvemos el nuevo estado, eliminamos los elementos movidos de principal y los añadimos a dos

      // Caso 4, Movimiento Dos(n) , n negativo
      case Dos(n) if n < 0 =>
        val movimientos = -n // Convertimos los movimientos(n) a positivo
        // Usamos una expresión for para generar los elementos a mover
        val movidos = for {
          i <- 0 until math.min(movimientos, dos.length) // Creamos un rango de índices(i) desde 0 hasta la longitud de dos -1
        } yield dos(i) // Para cada índice(i) en el rango, obtenemos el elemento correspondiente de dos
        (principal ++ movidos.toList, uno, dos.drop(math.min(movimientos, dos.length))) // Devolvemos el nuevo estado, eliminamos los elementos movidos de dos y los añadimos a principal

      // Caso 5, Movimiento Uno(0) o Dos(0)
      case Uno(0) => e // No se realizan movimientos, devolvemos el estado original

      // Caso 6, Movimiento Dos(0)
      case Dos(0) => e // No se realizan movimientos, devolvemos el estado original
    }
  }
}