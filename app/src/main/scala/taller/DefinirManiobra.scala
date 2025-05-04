package taller
import scala.annotation.tailrec

class DefinirManiobra extends AplicarMovimientos {

  def definirManiobra(t1: Tren, t2: Tren): Maniobra = {
    @tailrec
    def aux(pendientes: List[(Estado, Maniobra)], visitados: Set[Estado]): Maniobra = {
      pendientes match {
        case Nil => Nil
        case (estadoActual@(principal, uno, dos), maniobra) :: resto =>
          if (principal == t2 && uno.isEmpty && dos.isEmpty)
            maniobra.reverse
          else if (visitados.contains(estadoActual))
            aux(resto, visitados)
          else {
            val nuevos = for {
              mov <- Movimientos(estadoActual)
              nuevoEstado = aplicarMovimiento(estadoActual, mov)
              if !visitados.contains(nuevoEstado)
            } yield (nuevoEstado, mov :: maniobra)
            aux(resto ++ nuevos, visitados + estadoActual)
          }
      }
    }

    def Movimientos(estado: Estado): List[Movimiento] = {
      val (principal, uno, dos) = estado
      val desdePrincipal = for (n <- 1 to principal.length; m <- List(Uno(n), Dos(n))) yield m
      val desdeUno       = for (n <- 1 to uno.length) yield Uno(-n)
      val desdeDos       = for (n <- 1 to dos.length) yield Dos(-n)
      (desdePrincipal ++ desdeUno ++ desdeDos).toList
    }
    aux(List(((t1, Nil, Nil), Nil)), Set.empty)
  }
}







