package taller

class DefinirManiobra extends AplicarMovimientos {
  // Función recursiva que construye la maniobra
  def definirManiobra(t1: Tren, t2: Tren): Maniobra = {

    def mover(t1: Tren, uno: Tren, dos: Tren, salida: Tren, objetivo: Tren, maniobra: Maniobra): Maniobra = {
      objetivo match {
        case Nil => maniobra // Ya se armó todo t2
        case cabezaObjetivo :: colaObjetivo =>
          // Caso 1: Está al frente de principal (t1)
          t1 match {
            case `cabezaObjetivo` :: cola =>
              mover(cola, uno, dos, salida :+ cabezaObjetivo, colaObjetivo, maniobra :+ Uno(1))
            case _ =>
              // Buscar en uno
              uno.indexOf(cabezaObjetivo) match {
                case -1 =>
                  // Buscar en dos
                  dos.indexOf(cabezaObjetivo) match {
                    case -1 =>
                      // No está ni en uno ni en dos, mover de principal a uno hasta encontrarlo
                      val (antes, despues) = t1.span(_ != cabezaObjetivo)
                      val movs1 = if (antes.nonEmpty) List(Uno(antes.length)) else Nil
                      val movs2 = List(Uno(-1))
                      mover(despues.tail, antes.reverse ++ uno, dos, salida :+ cabezaObjetivo, colaObjetivo, maniobra ++ movs1 ++ movs2)
                    case i =>
                      // Está en dos, mover i+1 de dos a principal, tomar el primero
                      val movs = List.fill(i + 1)(Dos(-1))
                      mover(t1, uno, dos.drop(i + 1), salida :+ cabezaObjetivo, colaObjetivo, maniobra ++ movs)
                  }
                case i =>
                  // Está en uno, mover i+1 de uno a principal, tomar el primero
                  val movs = List.fill(i + 1)(Uno(-1))
                  mover(t1, uno.drop(i + 1), dos, salida :+ cabezaObjetivo, colaObjetivo, maniobra ++ movs)
              }
          }
      }
    }

    mover(t1, Nil, Nil, Nil, t2, Nil)
  }
}