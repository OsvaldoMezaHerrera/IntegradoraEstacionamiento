package mx.edu.utez.Estacionamiento.structures;

public class Cola <T>{
    private NodoCola<T> inicio;
    private NodoCola<T> fin;
    private int size;


    public Cola(){
        inicio=fin=null;
        size=0;
    }

    public boolean EstaVacia(){
        return inicio==null;
    }

    public void Agregar(T elemento){
        NodoCola<T> nuevo = new NodoCola<>(elemento);
        if (EstaVacia()){
            inicio= nuevo;
        }
        else{
            fin.siguiente= nuevo;
        }
        fin = nuevo;
        size++;
    }

    public T Quitar(){
        if(EstaVacia()){
            System.out.println("no hay elememntos");
            return null;
        }
        else {
            T aux = inicio.dato;
            inicio = inicio.siguiente;
            size--;
            return aux;
        }
    }

    public T VerInicio(){
        if(EstaVacia()){
            System.out.println("No hay elementos");
            return null;
        }
        return inicio.dato;
    }

    public T VerFin(){
        if(EstaVacia()){
            System.out.println("no hay elementos");
            return null;
        }
        return fin.dato;
    }

    public void BorrarCola(){
        inicio = null;
        fin = null;
        size = 0;
    }

    public void Mostrar(){
        if(EstaVacia()){
            System.out.println("No hay elementos");
            return;
        }
        System.out.print("cola: ");
        NodoCola<T> aux = inicio;
        while(aux != null){
            System.out.print(aux.dato + " -> ");
            aux=aux.siguiente;
        }
        System.out.println(" | ");
    }

    public int Tamano(){
        return size;
    }
}
