package mx.edu.utez.Estacionamiento.structures;

public class NodoPila<T>{
    protected T valor;
    protected NodoPila<T> siguiente;

    public NodoPila(T valor){
        this.valor=valor;
        siguiente = null;
    }
}
