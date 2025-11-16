package mx.edu.utez.Estacionamiento.structures;

public class ListaDoble<T> {
    protected NodoListaDoble<T> head;
    protected NodoListaDoble<T> tail;
    protected int tamano = 0;

    public ListaDoble() {
        head = null;
        tail = null;
    }

    public boolean vacia() {
        return head == null;
    }

    public void insertarAlInicio(T dato) {
        NodoListaDoble<T> nuevo = new NodoListaDoble<>(dato);
        if (vacia()) {
            head = tail = nuevo;
        } else {
            nuevo.siguente = head;
            head.anterior = nuevo;
            head = nuevo;
        }
        tamano++;
    }

    public void insertarAlFinal(T dato) {
        NodoListaDoble<T> nuevo = new NodoListaDoble<>(dato);
        if (vacia()) {
            head = tail = nuevo;
        } else {
            tail.siguente = nuevo;
            nuevo.anterior = tail;
            tail = nuevo;
        }
        tamano++;
    }

    public void mostrar() {
        NodoListaDoble<T> actual = head;
        if (vacia()) {
            System.out.println("La lista está vacía");
            return;
        }
        while (actual != null) {
            System.out.print(actual.dato + " <--> ");
            actual = actual.siguente;
        }
        System.out.println("null");
    }

    public int buscarIndice(T dato) {
        NodoListaDoble<T> actual = head;
        int index = 0;
        while (actual != null) {
            if ((dato == null && actual.dato == null) ||
                    (dato != null && dato.equals(actual.dato))) {
                return index;
            }
            actual = actual.siguente;
            index++;
        }
        return -1;
    }

    public boolean eliminarPorValor(T dato) {
        if (vacia()) return false;
        if ((dato == null && head.dato == null) ||
                (dato != null && dato.equals(head.dato))) {
            if (head == tail) {
                head = tail = null;
            } else {
                head = head.siguente;
                head.anterior = null;
            }
            tamano--;
            return true;
        }
        NodoListaDoble<T> actual = head.siguente;
        while (actual != null) {
            if ((dato == null && actual.dato == null) ||
                    (dato != null && dato.equals(actual.dato))) {
                if (actual == tail) {
                    tail = actual.anterior;
                    tail.siguente = null;
                } else {
                    actual.anterior.siguente = actual.siguente;
                    actual.siguente.anterior = actual.anterior;
                }
                tamano--;
                return true;
            }
            actual = actual.siguente;
        }
        return false;
    }

    public boolean eliminarPorPosicion(int posicion) {
        if (posicion < 0 || posicion >= tamano || vacia()) return false;
        if (posicion == 0) {
            if (head == tail) {
                head = tail = null;
            } else {
                head = head.siguente;
                head.anterior = null;
            }
            tamano--;
            return true;
        }
        if (posicion == tamano - 1) {
            tail = tail.anterior;
            tail.siguente = null;
            tamano--;
            return true;
        }
        NodoListaDoble<T> actual = head;
        for (int i = 0; i < posicion; i++) {
            actual = actual.siguente;
        }
        actual.anterior.siguente = actual.siguente;
        actual.siguente.anterior = actual.anterior;
        tamano--;
        return true;
    }

    public void ordenarLista() {
        if (vacia() || head.siguente == null) return;
        boolean cambiado;
        do {
            cambiado = false;
            NodoListaDoble<T> actual = head;
            while (actual.siguente != null) {
                String a = actual.dato.toString();
                String b = actual.siguente.dato.toString();
                if (a.compareTo(b) > 0) {
                    T temp = actual.dato;
                    actual.dato = actual.siguente.dato;
                    actual.siguente.dato = temp;
                    cambiado = true;
                }
                actual = actual.siguente;
            }
        } while (cambiado);
    }

    public int getTamano() {
        return tamano;
    }
}
