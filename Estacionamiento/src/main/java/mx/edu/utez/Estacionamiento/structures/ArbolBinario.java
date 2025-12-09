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
     * Limpia el árbol usando recursividad
     */
    public void limpiar() {
        limpiarRecursivo(raiz);
        raiz = null;
        tamano = 0;
    }

    private void limpiarRecursivo(NodoArbol<T> nodo) {
        if (nodo != null) {
            limpiarRecursivo(nodo.getIzquierdo());
            limpiarRecursivo(nodo.getDerecho());
            nodo.setIzquierdo(null);
            nodo.setDerecho(null);
        }
    }

    /**
     * Recorrido preorden (raíz, izquierdo, derecho) - RECURSIVO
     */
    public void recorrerPreorden() {
        recorrerPreordenRecursivo(raiz);
        System.out.println();
    }

    private void recorrerPreordenRecursivo(NodoArbol<T> nodo) {
        if (nodo != null) {
            System.out.print(nodo.getDato() + " ");
            recorrerPreordenRecursivo(nodo.getIzquierdo());
            recorrerPreordenRecursivo(nodo.getDerecho());
        }
    }

    /**
     * Recorrido postorden (izquierdo, derecho, raíz) - RECURSIVO
     */
    public void recorrerPostorden() {
        recorrerPostordenRecursivo(raiz);
        System.out.println();
    }

    private void recorrerPostordenRecursivo(NodoArbol<T> nodo) {
        if (nodo != null) {
            recorrerPostordenRecursivo(nodo.getIzquierdo());
            recorrerPostordenRecursivo(nodo.getDerecho());
            System.out.print(nodo.getDato() + " ");
        }
    }

    /**
     * Calcula la altura del árbol usando recursividad
     * @return altura del árbol (número de niveles)
     */
    public int obtenerAltura() {
        return obtenerAlturaRecursivo(raiz);
    }

    private int obtenerAlturaRecursivo(NodoArbol<T> nodo) {
        if (nodo == null) {
            return -1; // Árbol vacío tiene altura -1
        }
        int alturaIzquierdo = obtenerAlturaRecursivo(nodo.getIzquierdo());
        int alturaDerecho = obtenerAlturaRecursivo(nodo.getDerecho());
        return Math.max(alturaIzquierdo, alturaDerecho) + 1;
    }

    /**
     * Calcula la profundidad de un nodo usando recursividad
     * @param dato el dato a buscar
     * @return profundidad del nodo o -1 si no existe
     */
    public int obtenerProfundidad(T dato) {
        return obtenerProfundidadRecursivo(raiz, dato, 0);
    }

    private int obtenerProfundidadRecursivo(NodoArbol<T> nodo, T dato, int nivel) {
        if (nodo == null) {
            return -1;
        }
        if (dato.compareTo(nodo.getDato()) == 0) {
            return nivel;
        }
        int profundidadIzquierdo = obtenerProfundidadRecursivo(nodo.getIzquierdo(), dato, nivel + 1);
        if (profundidadIzquierdo != -1) {
            return profundidadIzquierdo;
        }
        return obtenerProfundidadRecursivo(nodo.getDerecho(), dato, nivel + 1);
    }

    /**
     * Cuenta el número de hojas (nodos sin hijos) usando recursividad
     * @return número de hojas en el árbol
     */
    public int contarHojas() {
        return contarHojasRecursivo(raiz);
    }

    private int contarHojasRecursivo(NodoArbol<T> nodo) {
        if (nodo == null) {
            return 0;
        }
        if (nodo.getIzquierdo() == null && nodo.getDerecho() == null) {
            return 1;
        }
        return contarHojasRecursivo(nodo.getIzquierdo()) + contarHojasRecursivo(nodo.getDerecho());
    }

    /**
     * Cuenta el número de nodos internos (con al menos un hijo) usando recursividad
     * @return número de nodos internos
     */
    public int contarNodosInternos() {
        return contarNodosInternosRecursivo(raiz);
    }

    private int contarNodosInternosRecursivo(NodoArbol<T> nodo) {
        if (nodo == null) {
            return 0;
        }
        if (nodo.getIzquierdo() == null && nodo.getDerecho() == null) {
            return 0; // Es una hoja
        }
        return 1 + contarNodosInternosRecursivo(nodo.getIzquierdo()) + 
                   contarNodosInternosRecursivo(nodo.getDerecho());
    }

    /**
     * Encuentra el valor mínimo en el árbol usando recursividad
     * @return el valor mínimo o null si el árbol está vacío
     */
    public T encontrarMinimo() {
        if (raiz == null) {
            return null;
        }
        return encontrarMinimoRecursivo(raiz);
    }

    private T encontrarMinimoRecursivo(NodoArbol<T> nodo) {
        if (nodo.getIzquierdo() == null) {
            return nodo.getDato();
        }
        return encontrarMinimoRecursivo(nodo.getIzquierdo());
    }

    /**
     * Encuentra el valor máximo en el árbol usando recursividad
     * @return el valor máximo o null si el árbol está vacío
     */
    public T encontrarMaximo() {
        if (raiz == null) {
            return null;
        }
        return encontrarMaximoRecursivo(raiz);
    }

    private T encontrarMaximoRecursivo(NodoArbol<T> nodo) {
        if (nodo.getDerecho() == null) {
            return nodo.getDato();
        }
        return encontrarMaximoRecursivo(nodo.getDerecho());
    }

    /**
     * Verifica si el árbol es un BST válido usando recursividad
     * @return true si es un BST válido, false en caso contrario
     */
    public boolean esBSTValido() {
        return esBSTValidoRecursivo(raiz, null, null);
    }

    private boolean esBSTValidoRecursivo(NodoArbol<T> nodo, T min, T max) {
        if (nodo == null) {
            return true;
        }
        if ((min != null && nodo.getDato().compareTo(min) <= 0) ||
            (max != null && nodo.getDato().compareTo(max) >= 0)) {
            return false;
        }
        return esBSTValidoRecursivo(nodo.getIzquierdo(), min, nodo.getDato()) &&
               esBSTValidoRecursivo(nodo.getDerecho(), nodo.getDato(), max);
    }

    /**
     * Obtiene todos los elementos en orden preorden usando recursividad
     * @return lista con elementos en preorden
     */
    public java.util.List<T> obtenerPreorden() {
        java.util.List<T> lista = new java.util.ArrayList<>();
        obtenerPreordenRecursivo(raiz, lista);
        return lista;
    }

    private void obtenerPreordenRecursivo(NodoArbol<T> nodo, java.util.List<T> lista) {
        if (nodo != null) {
            lista.add(nodo.getDato());
            obtenerPreordenRecursivo(nodo.getIzquierdo(), lista);
            obtenerPreordenRecursivo(nodo.getDerecho(), lista);
        }
    }

    /**
     * Obtiene todos los elementos en orden postorden usando recursividad
     * @return lista con elementos en postorden
     */
    public java.util.List<T> obtenerPostorden() {
        java.util.List<T> lista = new java.util.ArrayList<>();
        obtenerPostordenRecursivo(raiz, lista);
        return lista;
    }

    private void obtenerPostordenRecursivo(NodoArbol<T> nodo, java.util.List<T> lista) {
        if (nodo != null) {
            obtenerPostordenRecursivo(nodo.getIzquierdo(), lista);
            obtenerPostordenRecursivo(nodo.getDerecho(), lista);
            lista.add(nodo.getDato());
        }
    }

    /**
     * Obtiene información detallada del árbol usando recursividad
     * @return mapa con estadísticas del árbol
     */
    public java.util.Map<String, Object> obtenerEstadisticas() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("tamano", tamano);
        stats.put("altura", obtenerAltura());
        stats.put("hojas", contarHojas());
        stats.put("nodosInternos", contarNodosInternos());
        stats.put("esBSTValido", esBSTValido());
        stats.put("minimo", encontrarMinimo());
        stats.put("maximo", encontrarMaximo());
        stats.put("estaVacio", estaVacio());
        return stats;
    }
}

