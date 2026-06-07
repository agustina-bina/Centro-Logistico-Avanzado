// Tabla Hash para buscar productos por codigo en O(1)
public class TablaHash {
    private String[] claves;
    private Producto[] valores;
    private int capacidad;
    private int cantidad;

    public TablaHash(int capacidad) {
        this.capacidad = capacidad;
        this.claves = new String[capacidad];
        this.valores = new Producto[capacidad];
        this.cantidad = 0;
    }

    // Funcion hash simple
    private int hash(String clave) {
        int suma = 0;
        for (int i = 0; i < clave.length(); i++) {
            suma += clave.charAt(i);
        }
        return suma % capacidad;
    }

    // Insertar producto
    public boolean insertar(String codigo, Producto producto) {
        if (buscar(codigo) != null) {
            return false;  // ya existe
        }
        
        int indice = hash(codigo);
        
        // Buscar posicion libre
        while (claves[indice] != null) {
            indice = (indice + 1) % capacidad;
        }
        
        claves[indice] = codigo;
        valores[indice] = producto;
        cantidad++;
        return true;
    }

    // Buscar producto por codigo
    public Producto buscar(String codigo) {
        int indice = hash(codigo);
        int intentos = 0;
        
        while (claves[indice] != null && intentos < capacidad) {
            if (claves[indice].equals(codigo)) {
                return valores[indice];
            }
            indice = (indice + 1) % capacidad;
            intentos++;
        }
        return null;
    }

    // Eliminar producto
    public boolean eliminar(String codigo) {
        int indice = hash(codigo);
        int intentos = 0;
        
        while (claves[indice] != null && intentos < capacidad) {
            if (claves[indice].equals(codigo)) {
                claves[indice] = null;
                valores[indice] = null;
                cantidad--;
                return true;
            }
            indice = (indice + 1) % capacidad;
            intentos++;
        }
        return false;
    }
}
