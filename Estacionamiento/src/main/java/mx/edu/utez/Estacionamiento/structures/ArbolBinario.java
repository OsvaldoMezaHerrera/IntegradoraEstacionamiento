package mx.edu.utez.Estacionamiento.structures;

/**
 * Árbol Binario de Búsqueda (BST)
 * Permite búsqueda, inserción y eliminación eficiente O(log n) en promedio
 */
public class ArbolBinario<T extends Comparable<T>> {
    private NodoArbol<T> raiz;
    private int tamano;

    public ArbolBinario() {
        this.raiz = null;
        this.tamano = 0;
    }

    /**
     * Inserta un elemento en el árbol manteniendo la propiedad BST
     */
    public void insertar(T dato) {
        raiz = insertarRecursivo(raiz, dato);
        tamano++;
    }

    private NodoArbol<T> insertarRecursivo(NodoArbol<T> nodo, T dato) {
        if (nodo == null) {
            return new NodoArbol<>(dato);
        }

        if (dato.compareTo(nodo.getDato()) < 0) {
            nodo.setIzquierdo(insertarRecursivo(nodo.getIzquierdo(), dato));
        } else if (dato.compareTo(nodo.getDato()) > 0) {
            nodo.setDerecho(insertarRecursivo(nodo.getDerecho(), dato));
        }

        return nodo;
    }

    /**
     * Busca un elemento en el árbol
     * @return el dato encontrado o null si no existe
     */
    public T buscar(T dato) {
        return buscarRecursivo(raiz, dato);
    }

    private T buscarRecursivo(NodoArbol<T> nodo, T dato) {
        if (nodo == null) {
            return null;
        }

        if (dato.compareTo(nodo.getDato()) == 0) {
            return nodo.getDato();
        }

        if (dato.compareTo(nodo.getDato()) < 0) {
            return buscarRecursivo(nodo.getIzquierdo(), dato);
        } else {
            return buscarRecursivo(nodo.getDerecho(), dato);
        }
    }

    /**
     * Elimina un elemento del árbol
     */
    public boolean eliminar(T dato) {
        int tamanoAntes = tamano;
        raiz = eliminarRecursivo(raiz, dato);
        if (tamanoAntes > tamano) {
            tamano--;
            return true;
        }
        return false;
    }

    private NodoArbol<T> eliminarRecursivo(NodoArbol<T> nodo, T dato) {
        if (nodo == null) {
            return null;
        }

        if (dato.compareTo(nodo.getDato()) < 0) {
            nodo.setIzquierdo(eliminarRecursivo(nodo.getIzquierdo(), dato));
        } else if (dato.compareTo(nodo.getDato()) > 0) {
            nodo.setDerecho(eliminarRecursivo(nodo.getDerecho(), dato));
        } else {
            // Nodo a eliminar encontrado
            if (nodo.getIzquierdo() == null) {
                return nodo.getDerecho();
            } else if (nodo.getDerecho() == null) {
                return nodo.getIzquierdo();
            }

            // Nodo con dos hijos: encontrar el sucesor inorden (menor del subárbol derecho)
            T sucesor = encontrarMinimo(nodo.getDerecho());
            nodo.setDato(sucesor);
            nodo.setDerecho(eliminarRecursivo(nodo.getDerecho(), sucesor));
        }

        return nodo;
    }

    private T encontrarMinimo(NodoArbol<T> nodo) {
        while (nodo.getIzquierdo() != null) {
            nodo = nodo.getIzquierdo();
        }
        return nodo.getDato();
    }

    /**
     * Verifica si el árbol está vacío
     */
    public boolean estaVacio() {
        return raiz == null;
    }

    /**
     * Obtiene el tamaño del árbol
     */
    public int getTamano() {
        return tamano;
    }

    /**
     * Recorrido inorden (izquierdo, raíz, derecho) - ordenado
     */
    public void recorrerInorden() {
        recorrerInordenRecursivo(raiz);
        System.out.println();
    }

    private void recorrerInordenRecursivo(NodoArbol<T> nodo) {
        if (nodo != null) {
            recorrerInordenRecursivo(nodo.getIzquierdo());
            System.out.print(nodo.getDato() + " ");
            recorrerInordenRecursivo(nodo.getDerecho());
        }
    }

    /**
     * Obtiene todos los elementos en orden (inorden)
     */
    public java.util.List<T> obtenerTodos() {
        java.util.List<T> lista = new java.util.ArrayList<>();
        obtenerTodosRecursivo(raiz, lista);
        return lista;
    }

    private void obtenerTodosRecursivo(NodoArbol<T> nodo, java.util.List<T> lista) {
        if (nodo != null) {
            obtenerTodosRecursivo(nodo.getIzquierdo(), lista);
            lista.add(nodo.getDato());
            obtenerTodosRecursivo(nodo.getDerecho(), lista);
        }
    }

    /**
     * Limpia el árbol
     */
    public void limpiar() {
        raiz = null;
        tamano = 0;
    }
}

