package mx.edu.utez.Estacionamiento.structures;

/**
 * Arreglo Dinámico (similar a ArrayList)
 * Permite almacenar elementos con redimensionamiento automático
 */
public class ArregloDinamico<T> {
    private Object[] elementos;
    private int tamano;
    private static final int CAPACIDAD_INICIAL = 10;

    public ArregloDinamico() {
        this.elementos = new Object[CAPACIDAD_INICIAL];
        this.tamano = 0;
    }

    public ArregloDinamico(int capacidadInicial) {
        this.elementos = new Object[capacidadInicial];
        this.tamano = 0;
    }

    /**
     * Agrega un elemento al final del arreglo
     */
    public void agregar(T elemento) {
        if (tamano >= elementos.length) {
            redimensionar();
        }
        elementos[tamano++] = elemento;
    }

    /**
     * Inserta un elemento en una posición específica
     */
    public void insertar(int indice, T elemento) {
        if (indice < 0 || indice > tamano) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }

        if (tamano >= elementos.length) {
            redimensionar();
        }

        // Desplazar elementos hacia la derecha
        for (int i = tamano; i > indice; i--) {
            elementos[i] = elementos[i - 1];
        }

        elementos[indice] = elemento;
        tamano++;
    }

    /**
     * Obtiene un elemento por índice
     */
    @SuppressWarnings("unchecked")
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
        return (T) elementos[indice];
    }

    /**
     * Elimina un elemento por índice
     */
    @SuppressWarnings("unchecked")
    public T eliminar(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }

        T elementoEliminado = (T) elementos[indice];

        // Desplazar elementos hacia la izquierda
        for (int i = indice; i < tamano - 1; i++) {
            elementos[i] = elementos[i + 1];
        }

        elementos[--tamano] = null;
        return elementoEliminado;
    }

    /**
     * Busca un elemento y retorna su índice
     */
    public int buscarIndice(T elemento) {
        for (int i = 0; i < tamano; i++) {
            if (elementos[i] != null && elementos[i].equals(elemento)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Verifica si contiene un elemento
     */
    public boolean contiene(T elemento) {
        return buscarIndice(elemento) != -1;
    }

    /**
     * Obtiene el tamaño actual del arreglo
     */
    public int getTamano() {
        return tamano;
    }

    /**
     * Verifica si el arreglo está vacío
     */
    public boolean estaVacio() {
        return tamano == 0;
    }

    /**
     * Limpia el arreglo
     */
    public void limpiar() {
        for (int i = 0; i < tamano; i++) {
            elementos[i] = null;
        }
        tamano = 0;
    }

    /**
     * Redimensiona el arreglo cuando se llena
     */
    private void redimensionar() {
        int nuevaCapacidad = elementos.length * 2;
        Object[] nuevoArreglo = new Object[nuevaCapacidad];
        System.arraycopy(elementos, 0, nuevoArreglo, 0, tamano);
        elementos = nuevoArreglo;
    }

    /**
     * Convierte el arreglo a un array estándar de Java
     */
    @SuppressWarnings("unchecked")
    public T[] toArray(T[] arreglo) {
        if (arreglo.length < tamano) {
            arreglo = (T[]) java.lang.reflect.Array.newInstance(
                arreglo.getClass().getComponentType(), tamano);
        }
        System.arraycopy(elementos, 0, arreglo, 0, tamano);
        if (arreglo.length > tamano) {
            arreglo[tamano] = null;
        }
        return arreglo;
    }

    /**
     * Muestra todos los elementos del arreglo
     */
    public void mostrar() {
        System.out.print("[");
        for (int i = 0; i < tamano; i++) {
            System.out.print(elementos[i]);
            if (i < tamano - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
}

