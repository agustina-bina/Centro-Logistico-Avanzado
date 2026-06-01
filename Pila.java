// Pila LIFO para movimientos (deshacer)
public class Pila {
    private Movimiento[] elementos;
    private int tope;
    private int capacidad;

    public Pila(int capacidad) {
        this.capacidad = capacidad;
        this.elementos = new Movimiento[capacidad];
        this.tope = -1;
    }

    // Apilar movimiento
    public boolean apilar(Movimiento movimiento) {
        if (tope >= capacidad - 1) {
            return false;
        }
        tope++;
        elementos[tope] = movimiento;
        return true;
    }

    // Desapilar movimiento
    public Movimiento desapilar() {
        if (tope < 0) {
            return null;
        }
        Movimiento movimiento = elementos[tope];
        elementos[tope] = null;
        tope--;
        return movimiento;
    }

    // Ver tope sin sacarlo
    public Movimiento verTope() {
        if (tope < 0) {
            return null;
        }
        return elementos[tope];
    }

    public boolean estaVacia() {
        return tope < 0;
    }

    public int getTamanio() {
        return tope + 1;
    }

    // Mostrar todos los movimientos
    public String mostrarPila() {
        if (tope < 0) {
            return "Pila vacia";
        }
        String resultado = "";
        for (int i = tope; i >= 0; i--) {
            resultado += elementos[i].toString() + "\n";
        }
        return resultado;
    }
}
