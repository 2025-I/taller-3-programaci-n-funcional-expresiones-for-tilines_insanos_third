package taller
import scala.annotation.tailrec

  class AplicarMovimientos extends AplicarMovimiento {

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
  }
