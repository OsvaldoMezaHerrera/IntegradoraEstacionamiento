package mx.edu.utez.Estacionamiento.structures;

public class ColaCircular<T> {
    private int inicio;
    private int fin;
    private int size;
    private int cantidad;
    private T[] arrayCola;

    @SuppressWarnings("unchecked")
    public ColaCircular(int tamano) {
        size = tamano;
        inicio = 0;
        fin = size - 1;
        arrayCola = (T[]) new Object[size];  // Arreglo genérico
        cantidad = 0;
    }

    private int siguiente(int x) {
        return (x + 1) % size;
    }

    public boolean colaVacia() {
        return cantidad == 0;
    }

    public boolean colaLlena() {
        return cantidad == size;
    }

    public void insertar(T valor) {
        if (!colaLlena()) {
            fin = siguiente(fin);
            arrayCola[fin] = valor;
            cantidad++;
        } else {
            System.out.println("⚠️ Cola llena, no se agregó: " + valor);
        }
    }

    public T quitar() {
        if (colaVacia()) {
            System.out.println("⚠️ No puedo quitar. Cola vacía");
            return null;
        }
        T aux = arrayCola[inicio];
        inicio = siguiente(inicio);
        cantidad--;
        return aux;
    }

    public void borrarCola() {
        inicio = 0;
        fin = size - 1;
        cantidad = 0;
        System.out.println("✅ Cola borrada correctamente");
    }

    public T inicioCola() {
        if (colaVacia()) {
            System.out.println("⚠️ La cola está vacía");
            return null;
        }
        return arrayCola[inicio];
    }

    // Nueva función: ver el último elemento sin quitarlo
    public T peekFinal() {
        if (colaVacia()) {
            System.out.println("⚠️ La cola está vacía");
            return null;
        }
        return arrayCola[fin];
    }

    public int obtenerTamano() {
        return cantidad;
    }

    public void imprimir() {
        if (colaVacia()) {
            System.out.println("Cola vacía");
            return;
        }
        System.out.print("Cola: [ ");
        int i = inicio;
        int contador = 0;
        while (contador < cantidad) {
            System.out.print(arrayCola[i] + " ");
            i = siguiente(i);
            contador++;
        }
        System.out.println("]");
    }
}
