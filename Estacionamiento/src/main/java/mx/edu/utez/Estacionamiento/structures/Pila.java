package mx.edu.utez.Estacionamiento.structures;

public class Pila <T>{
    private NodoPila<T> cima;
    private int size;

    public Pila(){
        cima=null;
        size=0;
    }

    public void Insertar(T valor){
        NodoPila<T> nuevo = new NodoPila<>(valor);
        nuevo.siguiente=cima;
        cima=nuevo;
        size++;
    }

    public boolean PilaVacia(){
        return cima==null;
    }
    public void Quitar(){
        if(PilaVacia()){
            System.out.println("Pila vacia");
        }else {
        T aux= cima.valor;
        cima = cima.siguiente;
        size--;
        System.out.println("valor quitado: "+ aux);}
    }

    public T CimaPila(){
        if(PilaVacia()){
            System.out.println("Pila Vacia");
            return null;
        }
        else {
            return (T) cima.valor;
        }
    }

    public void LimpiarPila(){
        System.out.println("La pila esta vacia");
        cima = null;
        size=0;
    }

    public int TamanioPila(){
        if(PilaVacia()){
            System.out.println("Pila vacia");
            return 0 ;
        }
        return size;
    }

    public NodoPila<T> getCima() {
        return cima;
    }

    public void MostrarPila(){
        if(PilaVacia()){
            System.out.println("Pila Vacia");
        }
        else {
            NodoPila<T> aux = cima;
            System.out.print("|");
            while (aux!= null){
                System.out.print(aux.valor+"");
                aux=aux.siguiente;
            }
            System.out.println();
        }
    }
}
