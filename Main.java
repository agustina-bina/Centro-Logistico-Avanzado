git pullimport java.util.Scanner;

public class Main {

    private static ArbolBinario tablaProductos = new ArbolBinario();
    private static ColaPrioridad colaStock = new ColaPrioridad(1000);
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        cargarDatosPrueba();

        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    buscarProducto();
                    break;
                case 2:
                    altaProducto();
                    break;
                case 3:
                    bajaProducto();
                    break;
                case 4:
                    modificarStock();
                    break;
                case 5:
                    verStockCritico();
                    break;
                case 6:
                    verProductoMenorStock();
                    break;
                case 7:
                    listarProductos();
                    break;
                case 0:
                    System.out.println("Programa finalizado.");
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }

        } while (opcion != 0);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n=================================");
        System.out.println("      GESTION DE INVENTARIO");
        System.out.println("=================================");
        System.out.println("1. Buscar producto");
        System.out.println("2. Alta producto");
        System.out.println("3. Baja producto");
        System.out.println("4. Modificar stock");
        System.out.println("5. Ver productos con stock critico");
        System.out.println("6. Ver producto con menor stock");
        System.out.println("7. Listar productos");
        System.out.println("0. Salir");
        System.out.println("=================================");
    }

    private static void buscarProducto() {
        System.out.println("\n--- BUSCAR PRODUCTO ---");

        String codigo = leerTexto("Ingrese codigo: ");

        Producto producto = tablaProductos.buscar(codigo);

        if (producto != null) {
            System.out.println("Codigo: " + producto.getCodigo());
            System.out.println("Nombre: " + producto.getNombre());
            System.out.println("Stock: " + producto.getStock());
            System.out.println("Ubicacion: " + producto.getUbicacion());
        } else {
            System.out.println("Producto no encontrado.");
        }
    }

    private static void altaProducto() {
        System.out.println("\n--- ALTA PRODUCTO ---");

        String codigo = leerTexto("Ingrese codigo: ");

        if (tablaProductos.buscar(codigo) != null) {
            System.out.println("Ya existe un producto con ese codigo.");
            return;
        }

        String nombre = leerTexto("Ingrese nombre: ");
        double stock = leerDecimal("Ingrese stock: ");
        String ubicacion = leerTexto("Ingrese ubicacion: ");

        Producto producto = new Producto(codigo, nombre, stock, ubicacion);

        tablaProductos.insertar(codigo, producto);
        colaStock.insertar(producto);

        System.out.println("Producto agregado correctamente.");
    }

    private static void bajaProducto() {
        System.out.println("\n--- BAJA PRODUCTO ---");

        String codigo = leerTexto("Ingrese codigo: ");

        if (tablaProductos.buscar(codigo) == null) {
            System.out.println("Producto no encontrado.");
            return;
        }

        tablaProductos.eliminar(codigo);
        colaStock.eliminar(codigo);

        System.out.println("Producto eliminado correctamente.");
    }

    private static void modificarStock() {
        System.out.println("\n--- MODIFICAR STOCK ---");

        String codigo = leerTexto("Ingrese codigo: ");

        Producto producto = tablaProductos.buscar(codigo);

        if (producto == null) {
            System.out.println("Producto no encontrado.");
            return;
        }

        System.out.println("Stock actual: " + producto.getStock());

        double nuevoStock = leerDecimal("Ingrese nuevo stock: ");

        producto.setStock(nuevoStock);
        colaStock.actualizarStock(codigo, nuevoStock);

        System.out.println("Stock actualizado correctamente.");
    }

    private static void verStockCritico() {
        System.out.println("\n--- PRODUCTOS CON STOCK CRITICO ---");

        if (colaStock.estaVacia()) {
            System.out.println("No hay productos cargados.");
            return;
        }

        int cantidad = leerEntero("Cuantos productos desea mostrar? ");

        Producto[] criticos = colaStock.obtenerProductosCriticos(cantidad);

        for (int i = 0; i < criticos.length; i++) {
            if (criticos[i] != null) {
                System.out.println(
                        (i + 1) + ". " +
                        criticos[i].getCodigo() + " - " +
                        criticos[i].getNombre() +
                        " | Stock: " + criticos[i].getStock()
                );
            }
        }
    }

    private static void verProductoMenorStock() {
        System.out.println("\n--- PRODUCTO CON MENOR STOCK ---");

        Producto producto = colaStock.verMinimo();

        if (producto == null) {
            System.out.println("No hay productos cargados.");
            return;
        }

        System.out.println("Codigo: " + producto.getCodigo());
        System.out.println("Nombre: " + producto.getNombre());
        System.out.println("Stock: " + producto.getStock());
        System.out.println("Ubicacion: " + producto.getUbicacion());
    }

    private static void listarProductos() {
        System.out.println("\n--- LISTA DE PRODUCTOS ---");

        Producto[] productos = tablaProductos.obtenerTodos();

        if (productos.length == 0) {
            System.out.println("No hay productos cargados.");
            return;
        }

        for (Producto p : productos) {
            System.out.println(
                    "[" + p.getCodigo() + "] " +
                    p.getNombre() +
                    " | Stock: " + p.getStock() +
                    " | Ubicacion: " + p.getUbicacion()
            );
        }
    }

    private static void cargarDatosPrueba() {

        Producto p1 = new Producto("P001", "Tornillos", 120, "A1");
        Producto p2 = new Producto("P002", "Harina", 3.75, "B2");
        Producto p3 = new Producto("P003", "Tuercas", 45, "A3");
        Producto p4 = new Producto("P004", "Aceite", 7.5, "C1");
        Producto p5 = new Producto("P005", "Clavos", 200, "B1");

        tablaProductos.insertar(p1.getCodigo(), p1);
        tablaProductos.insertar(p2.getCodigo(), p2);
        tablaProductos.insertar(p3.getCodigo(), p3);
        tablaProductos.insertar(p4.getCodigo(), p4);
        tablaProductos.insertar(p5.getCodigo(), p5);

        colaStock.insertar(p1);
        colaStock.insertar(p2);
        colaStock.insertar(p3);
        colaStock.insertar(p4);
        colaStock.insertar(p5);
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private static double leerDecimal(String mensaje) {
        System.out.print(mensaje);
        return Double.parseDouble(scanner.nextLine().trim());
    }
}