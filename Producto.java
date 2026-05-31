// Clase que representa un producto del inventario
public class Producto {
    private String codigo;
    private String nombre;
    private double stock;
    private String ubicacion;

    public Producto(String codigo, String nombre, double stock, String ubicacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.stock = stock;
        this.ubicacion = ubicacion;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String toString() {
        return "Producto[codigo=" + codigo + ", nombre=" + nombre + 
               ", stock=" + stock + ", ubicacion=" + ubicacion + "]";
    }
}
