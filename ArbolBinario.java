// Arbol Binario de Busqueda para almacenar productos ordenados por codigo.
public class ArbolBinario {

    // Nodo interno del arbol
    private class Nodo {
        String codigo;
        Producto producto;
        Nodo izq;
        Nodo der;

        Nodo(String codigo, Producto producto) {
            this.codigo = codigo;
            this.producto = producto;
            this.izq = null;
            this.der = null;
        }
    }

    private Nodo raiz;
    private int cantidad;

    public ArbolBinario() {
        this.raiz = null;
        this.cantidad = 0;
    }

    // Insertar producto. Devuelve false si el codigo ya existe.
    public boolean insertar(String codigo, Producto producto) {
        if (buscar(codigo) != null) {
            return false;  // ya existe
        }
        raiz = insertarRec(raiz, codigo, producto);
        cantidad++;
        return true;
    }

    // Insercion recursiva: baja por el arbol comparando codigos
    private Nodo insertarRec(Nodo nodo, String codigo, Producto producto) {
        if (nodo == null) {
            return new Nodo(codigo, producto);
        }
        int comp = codigo.compareTo(nodo.codigo);
        if (comp < 0) {
            nodo.izq = insertarRec(nodo.izq, codigo, producto);
        } else if (comp > 0) {
            nodo.der = insertarRec(nodo.der, codigo, producto);
        }
        return nodo;
    }

    // Buscar producto por codigo. Devuelve null si no existe.
    public Producto buscar(String codigo) {
        Nodo actual = raiz;
        while (actual != null) {
            int comp = codigo.compareTo(actual.codigo);
            if (comp == 0) {
                return actual.producto;
            } else if (comp < 0) {
                actual = actual.izq;
            } else {
                actual = actual.der;
            }
        }
        return null;
    }

    // Eliminar producto por codigo. Devuelve true si se elimino.
    public boolean eliminar(String codigo) {
        if (buscar(codigo) == null) {
            return false;
        }
        raiz = eliminarRec(raiz, codigo);
        cantidad--;
        return true;
    }

    // Eliminacion recursiva con los 3 casos clasicos:
    // sin hijos, un hijo, dos hijos (se reemplaza por el sucesor in-orden)
    private Nodo eliminarRec(Nodo nodo, String codigo) {
        if (nodo == null) {
            return null;
        }
        int comp = codigo.compareTo(nodo.codigo);
        if (comp < 0) {
            nodo.izq = eliminarRec(nodo.izq, codigo);
        } else if (comp > 0) {
            nodo.der = eliminarRec(nodo.der, codigo);
        } else {
            // Encontre el nodo a eliminar
            if (nodo.izq == null) {
                return nodo.der;
            } else if (nodo.der == null) {
                return nodo.izq;
            } else {
                // Dos hijos: lo reemplazo con el menor del subarbol derecho
                Nodo sucesor = minimo(nodo.der);
                nodo.codigo = sucesor.codigo;
                nodo.producto = sucesor.producto;
                nodo.der = eliminarRec(nodo.der, sucesor.codigo);
            }
        }
        return nodo;
    }

    // Devuelve el nodo con menor codigo de un subarbol
    private Nodo minimo(Nodo nodo) {
        while (nodo.izq != null) {
            nodo = nodo.izq;
        }
        return nodo;
    }

    // Obtener todos los productos ordenados por codigo (recorrido in-orden)
    public Producto[] obtenerTodos() {
        Producto[] resultado = new Producto[cantidad];
        int[] indice = { 0 };
        inOrden(raiz, resultado, indice);
        return resultado;
    }

    private void inOrden(Nodo nodo, Producto[] arr, int[] indice) {
        if (nodo == null) return;
        inOrden(nodo.izq, arr, indice);
        arr[indice[0]++] = nodo.producto;
        inOrden(nodo.der, arr, indice);
    }

    public int getCantidad() {
        return cantidad;
    }
}