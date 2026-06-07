// Clase que representa un pedido para expedicion
public class Pedido {
    private int id;
    private String[] productos;  // codigos de productos
    private int cantidadProductos;
    private String fecha;

    public Pedido(int id, String fecha) {
        this.id = id;
        this.fecha = fecha;
        this.productos = new String[100];  // maximo 100 productos por pedido
        this.cantidadProductos = 0;
    }

    public void agregarProducto(String codigoProducto) {
        if (cantidadProductos < productos.length) {
            productos[cantidadProductos] = codigoProducto;
            cantidadProductos++;
        }
    }

    public int getId() {
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public String[] getProductos() {
        return productos;
    }

    public int getCantidadProductos() {
        return cantidadProductos;
    }

    public String toString() {
        String lista = "";
        for (int i = 0; i < cantidadProductos; i++) {
            lista += productos[i];
            if (i < cantidadProductos - 1) {
                lista += ", ";
            }
        }
        return "Pedido #" + id + " [" + fecha + "] - Productos: " + lista;
    }
}
