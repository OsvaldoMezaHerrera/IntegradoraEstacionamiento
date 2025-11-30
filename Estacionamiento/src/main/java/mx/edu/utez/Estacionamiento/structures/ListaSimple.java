package mx.edu.utez.Estacionamiento.structures;

public class ListaSimple<T> {

    private NodoListaSimple<T> head;
    protected int tamano = 0;

    public ListaSimple() {
        this.head = null;
    }

    public NodoListaSimple<T> getHead() {
        return head;
    }

    public void insertarAlInicio(T dato) {
        head = new NodoListaSimple<>(dato, head);
        tamano++;
    }

    public void insertarAlFinal(T dato) {
        NodoListaSimple<T> nuevo = new NodoListaSimple<>(dato);
        if (head == null) {
            head = nuevo;
        } else {
            NodoListaSimple<T> actual = head;
            // Usamos getter
            while (actual.getEnlace() != null) {
                actual = actual.getEnlace();
            }
            // Usamos setter
            actual.setEnlace(nuevo);
        }
        tamano++;
    }

    public void mostrar() {
        NodoListaSimple<T> actual = head;
        if (actual == null) {
            System.out.println("La lista está vacía");
            return;
        }
        while (actual != null) {
            // Usamos getter
            System.out.print(actual.getDato() + " --> ");
            actual = actual.getEnlace();
        }
        System.out.println("null");
    }

    public int buscarIndice(T dato) {
        NodoListaSimple<T> actual = head;
        int index = 0;
        while (actual != null) {
            // Usamos getter
            if ((dato == null && actual.getDato() == null) ||
                    (dato != null && dato.equals(actual.getDato()))) {
                return index;
            }
            actual = actual.getEnlace();
            index++;
        }
        return -1;
    }

    public boolean eliminarPorValor(T dato) {
        if (head == null) return false;
        // Usamos getter
        if ((dato == null && head.getDato() == null) ||
                (dato != null && dato.equals(head.getDato()))) {
            head = head.getEnlace();
            tamano--;
            return true;
        }
        NodoListaSimple<T> anterior = head;
        NodoListaSimple<T> actual = head.getEnlace();
        while (actual != null) {
            // Usamos getter
            if ((dato == null && actual.getDato() == null) ||
                    (dato != null && dato.equals(actual.getDato()))) {
                // Usamos getter y setter
                anterior.setEnlace(actual.getEnlace());
                tamano--;
                return true;
            }
            anterior = actual;
            actual = actual.getEnlace();
        }
        return false;
    }

    public boolean eliminarPorPosicion(int posicion) {
        // Corregí tu validación, debe ser >= tamano
        if (posicion < 0 || posicion >= tamano || head == null) return false;

        if (posicion == 0) {
            head = head.getEnlace();
            tamano--;
            return true;
        }
        NodoListaSimple<T> anterior = head;
        for (int i = 0; i < posicion - 1; i++) {
            anterior = anterior.getEnlace();
        }
        // Usamos getter y setter
        anterior.setEnlace(anterior.getEnlace().getEnlace());
        tamano--;
        return true;
    }

    public void ordenarLista() {
        if (head == null || head.getEnlace() == null) return;
        boolean cambiado;
        do {
            cambiado = false;
            NodoListaSimple<T> actual = head;
            // Usamos getter
            while (actual.getEnlace() != null) {
                // Usamos getter
                String a = actual.getDato().toString();
                String b = actual.getEnlace().getDato().toString();

                if (a.compareTo(b) > 0) {
                    // Usamos getter y setter
                    T temp = actual.getDato();
                    actual.setDato(actual.getEnlace().getDato());
                    actual.getEnlace().setDato(temp);
                    cambiado = true;
                }
                actual = actual.getEnlace();
            }
        } while (cambiado);
    }

    public int getTamano() {
        return tamano;
    }

    /**
     * Limpia la lista completamente
     */
    public void limpiar() {
        head = null;
        tamano = 0;
    }
}