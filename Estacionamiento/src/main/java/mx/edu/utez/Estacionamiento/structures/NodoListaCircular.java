package mx.edu.utez.Estacionamiento.structures;

public class NodoListaCircular<T> {
    protected T dato;
    protected NodoListaCircular<T> enlace;

    public NodoListaCircular(T dato){
        this.dato=dato;
        enlace = this;
    }


}
