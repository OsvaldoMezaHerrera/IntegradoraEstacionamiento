package mx.edu.utez.Estacionamiento.structures;

public class NodoCola<T>{
    protected T dato;
    protected NodoCola<T> siguiente;

    public NodoCola(T dato){
        this.dato=dato;
        siguiente=null;
    }

    public T getDato() {
        return dato;
    }

    public NodoCola<T> getSiguiente() {
        return siguiente;
    }
}
