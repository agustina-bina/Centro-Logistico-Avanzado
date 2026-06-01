// Cola de Prioridad (Monticulo Minimo) para productos con menor stock
public class ColaPrioridad {
    private Producto[] heap;
    private int tamanio;
    private int capacidad;

    public ColaPrioridad(int capacidad) {
        this.capacidad = capacidad;
        this.heap = new Producto[capacidad];
        this.tamanio = 0;
    }

    // Insertar producto
    public void insertar(Producto producto) {
        if (tamanio >= capacidad) {
            return;
        }
        heap[tamanio] = producto;
        subir(tamanio);
        tamanio++;
    }

    // Subir elemento para mantener propiedad de heap
    private void subir(int indice) {
        while (indice > 0) {
            int padre = (indice - 1) / 2;
            if (heap[indice].getStock() < heap[padre].getStock()) {
                Producto temp = heap[indice];
                heap[indice] = heap[padre];
                heap[padre] = temp;
                indice = padre;
            } else {
                break;
            }
        }
    }

    // Bajar elemento para mantener propiedad de heap
    private void bajar(int indice) {
        while (true) {
            int izq = 2 * indice + 1;
            int der = 2 * indice + 2;
            int menor = indice;

            if (izq < tamanio && heap[izq].getStock() < heap[menor].getStock()) {
                menor = izq;
            }
            if (der < tamanio && heap[der].getStock() < heap[menor].getStock()) {
                menor = der;
            }

            if (menor != indice) {
                Producto temp = heap[indice];
                heap[indice] = heap[menor];
                heap[menor] = temp;
                indice = menor;
            } else {
                break;
            }
        }
    }

    // Ver producto con menor stock (sin sacarlo)
    public Producto verMinimo() {
        if (tamanio == 0) {
            return null;
        }
        return heap[0];
    }

    // Sacar producto con menor stock
    public Producto extraerMinimo() {
        if (tamanio == 0) {
            return null;
        }
        Producto minimo = heap[0];
        heap[0] = heap[tamanio - 1];
        tamanio--;
        if (tamanio > 0) {
            bajar(0);
        }
        return minimo;
    }

    // Actualizar stock de un producto y reordenar
    public void actualizarStock(String codigo, double nuevoStock) {
        for (int i = 0; i < tamanio; i++) {
            if (heap[i].getCodigo().equals(codigo)) {
                double stockAnterior = heap[i].getStock();
                heap[i].setStock(nuevoStock);
                if (nuevoStock < stockAnterior) {
                    subir(i);
                } else {
                    bajar(i);
                }
                return;
            }
        }
    }

    // Eliminar producto por codigo
    public boolean eliminar(String codigo) {
        for (int i = 0; i < tamanio; i++) {
            if (heap[i].getCodigo().equals(codigo)) {
                heap[i] = heap[tamanio - 1];
                tamanio--;
                if (i < tamanio) {
                    bajar(i);
                    subir(i);
                }
                return true;
            }
        }
        return false;
    }

    // Mostrar los productos con menor stock
    public Producto[] obtenerProductosCriticos(int cantidad) {
        Producto[] criticos = new Producto[Math.min(cantidad, tamanio)];
        // Copiar heap temporalmente
        Producto[] copia = new Producto[tamanio];
        int tamanioOriginal = tamanio;
        for (int i = 0; i < tamanio; i++) {
            copia[i] = heap[i];
        }
        
        // Extraer los minimos
        for (int i = 0; i < criticos.length; i++) {
            criticos[i] = extraerMinimo();
        }
        
        // Restaurar heap
        heap = copia;
        tamanio = tamanioOriginal;
        
        return criticos;
    }

    public int getTamanio() {
        return tamanio;
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }
}
