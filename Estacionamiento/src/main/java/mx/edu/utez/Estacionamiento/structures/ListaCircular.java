package mx.edu.utez.Estacionamiento.structures;

public class ListaCircular<T> {
    private NodoListaCircular<T> lc;
    private int tamano = 0;

    public ListaCircular() {
        lc = null;
    }

    public boolean vacia() {
        return lc == null;
    }

    public void insertarAlInicio(T dato) {
        NodoListaCircular<T> nuevo = new NodoListaCircular<>(dato);
        if (vacia()) {
            lc = nuevo;
        } else {
            nuevo.enlace = lc.enlace;
            lc.enlace = nuevo;
        }
        tamano++;
    }

    public void insertarAlFinal(T dato) {
        NodoListaCircular<T> nuevo = new NodoListaCircular<>(dato);
        if (vacia()) {
            lc = nuevo;
        } else {
            nuevo.enlace = lc.enlace;
            lc.enlace = nuevo;
            lc = nuevo;
        }
        tamano++;
    }

    public void mostrar() {
        if (vacia()) {
            System.out.println("Lista vacía");
            return;
        }
        NodoListaCircular<T> actual = lc.enlace;
        do {
            System.out.print(actual.dato + " --> ");
            actual = actual.enlace;
        } while (actual != lc.enlace);
        System.out.println("⟳");
    }

    public int buscarIndice(T dato) {
        if (vacia()) return -1;
        NodoListaCircular<T> actual = lc.enlace;
        int index = 0;
        do {
            if ((dato == null && actual.dato == null) ||
                    (dato != null && dato.equals(actual.dato))) {
                return index;
            }
            actual = actual.enlace;
            index++;
        } while (actual != lc.enlace);
        return -1;
    }

    public boolean eliminarPorValor(T dato) {
        if (vacia()) return false;
        NodoListaCircular<T> actual = lc.enlace;
        NodoListaCircular<T> anterior = lc;

        do {
            if ((dato == null && actual.dato == null) ||
                    (dato != null && dato.equals(actual.dato))) {

                if (actual == lc && actual == lc.enlace) {
                    lc = null;
                } else {
                    anterior.enlace = actual.enlace;
                    if (actual == lc) lc = anterior;
                }
                tamano--;
                return true;
            }
            anterior = actual;
            actual = actual.enlace;
        } while (actual != lc.enlace);
        return false;
    }

    public boolean eliminarPorPosicion(int posicion) {
        if (vacia() || posicion < 0 || posicion >= tamano) return false;

        NodoListaCircular<T> actual = lc.enlace;
        NodoListaCircular<T> anterior = lc;

        for (int i = 0; i < posicion; i++) {
            anterior = actual;
            actual = actual.enlace;
        }

        if (actual == lc && actual == lc.enlace) {
            lc = null;
        } else {
            anterior.enlace = actual.enlace;
            if (actual == lc) lc = anterior;
        }
        tamano--;
        return true;
    }

    public void ordenarLista() {
        if (vacia() || lc.enlace == lc) return;
        boolean cambiado;
        do {
            cambiado = false;
            NodoListaCircular<T> actual = lc.enlace;
            do {
                NodoListaCircular<T> siguiente = actual.enlace;
                String a = actual.dato.toString();
                String b = siguiente.dato.toString();
                if (a.compareTo(b) > 0) {
                    T temp = actual.dato;
                    actual.dato = siguiente.dato;
                    siguiente.dato = temp;
                    cambiado = true;
                }
                actual = actual.enlace;
            } while (actual != lc);
        } while (cambiado);
    }


    public int getTamano() {
        return tamano;
    }
}
