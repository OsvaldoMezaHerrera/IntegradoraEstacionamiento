package mx.edu.utez.Estacionamiento.structures;

public class NodoListaSimple<T> {
    private T dato;
    private NodoListaSimple<T> enlace;

    public NodoListaSimple(T dato) {
        this.dato = dato;
        enlace = null;
    }

    public NodoListaSimple(T dato, NodoListaSimple<T> enlace) {
        this.dato = dato;
        this.enlace = enlace;
    }

    public T getDato() {
        return dato;
    }

    public NodoListaSimple<T> getEnlace() {
        return enlace;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public void setEnlace(NodoListaSimple<T> enlace) {
        this.enlace = enlace;
    }
}