package mx.edu.utez.Estacionamiento.structures;

public class NodoListaDoble<T>{
    protected T dato;
    protected NodoListaDoble<T> siguente;
    protected NodoListaDoble<T> anterior;

    public NodoListaDoble(T dato){
        this.dato = dato;
        siguente= anterior = null;
    }
}
