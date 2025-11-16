package mx.edu.utez.Estacionamiento.structures;

public class NodoCola<T>{
    protected T dato;
    protected NodoCola<T> siguiente;

    public NodoCola(T dato){
        this.dato=dato;
        siguiente=null;
    }

}
