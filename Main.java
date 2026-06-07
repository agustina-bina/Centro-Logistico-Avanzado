import java.util.Scanner;

// Clase principal con el menu de consola
public class Main {
    // Estructuras de datos
    private static ArbolBinario tablaProductos = new ArbolBinario();
    private static ColaPrioridad colaStock = new ColaPrioridad(1000);
    private static Cola colaPedidos = new Cola(500);
    private static Pila pilaMovimientos = new Pila(1000);
    private static Grafo grafoDeposito = new Grafo(50);
    
    private static Scanner scanner = new Scanner(System.in);
    private static int contadorPedidos = 100;

    public static void main(String[] args) {
        cargarDatosPrueba();
        
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");
            procesarOpcion(opcion);
        } while (opcion != 0);
        
        System.out.println("Programa finalizado.");
        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n========================================");
        System.out.println("   CENTRO LOGISTICO DE DISTRIBUCION");
        System.out.println("========================================");
        System.out.println("1. Buscar producto por codigo");
        System.out.println("2. Dar de alta producto");
        System.out.println("3. Dar de baja producto");
        System.out.println("4. Modificar stock de producto");
        System.out.println("5. Ver productos con stock critico");
        System.out.println("6. Crear pedido");
        System.out.println("7. Despachar pedido (expedicion)");
        System.out.println("8. Ver cola de pedidos");
        System.out.println("9. Calcular ruta de recoleccion");
        System.out.println("10. Deshacer ultimo movimiento");
        System.out.println("11. Ver historial de movimientos");
        System.out.println("12. Ver mapa del deposito");
        System.out.println("13. Listar todos los productos");
        System.out.println("0. Salir");
        System.out.println("----------------------------------------");
    }

    private static void procesarOpcion(int opcion) {
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
                crearPedido();
                break;
            case 7:
                despacharPedido();
                break;
            case 8:
                verColaPedidos();
                break;
            case 9:
                calcularRuta();
                break;
            case 10:
                deshacerMovimiento();
                break;
            case 11:
                verHistorialMovimientos();
                break;
            case 12:
                verMapaDeposito();
                break;
            case 13:
                listarProductos();
                break;
            case 0:
                break;
            default:
                System.out.println("Opcion no valida.");
        }
    }

    // 1. Buscar producto
    private static void buscarProducto() {
        System.out.println("\n--- BUSCAR PRODUCTO ---");
        String codigo = leerTexto("Ingrese codigo del producto: ");
        
        Producto producto = tablaProductos.buscar(codigo);
        if (producto != null) {
            System.out.println("Producto encontrado:");
            System.out.println("  Codigo: " + producto.getCodigo());
            System.out.println("  Nombre: " + producto.getNombre());
            System.out.println("  Stock: " + producto.getStock());
            System.out.println("  Ubicacion: " + producto.getUbicacion());
        } else {
            System.out.println("Error: Producto con codigo '" + codigo + "' no encontrado.");
        }
    }

    // 2. Alta de producto
    private static void altaProducto() {
        System.out.println("\n--- ALTA DE PRODUCTO ---");
        String codigo = leerTexto("Ingrese codigo: ");
        
        if (tablaProductos.buscar(codigo) != null) {
            System.out.println("Error: Ya existe un producto con ese codigo.");
            return;
        }
        
        String nombre = leerTexto("Ingrese nombre: ");
        double stock = leerDecimal("Ingrese stock inicial: ");
        
        if (stock < 0) {
            System.out.println("Error: El stock no puede ser negativo.");
            return;
        }
        
        // Mostrar ubicaciones disponibles
        System.out.println("Ubicaciones disponibles: ");
        String[] ubics = grafoDeposito.getUbicaciones();
        for (int i = 0; i < grafoDeposito.getCantidadUbicaciones(); i++) {
            System.out.println("  - " + ubics[i]);
        }
        String ubicacion = leerTexto("Ingrese ubicacion: ");
        
        if (!grafoDeposito.existeUbicacion(ubicacion)) {
            System.out.println("Error: La ubicacion no existe en el deposito.");
            return;
        }
        
        Producto producto = new Producto(codigo, nombre, stock, ubicacion);
        tablaProductos.insertar(codigo, producto);
        colaStock.insertar(producto);
        
        // Registrar movimiento
        Movimiento mov = new Movimiento("ALTA", codigo, 0, producto);
        pilaMovimientos.apilar(mov);
        
        System.out.println("Producto dado de alta exitosamente.");
    }

    // 3. Baja de producto
    private static void bajaProducto() {
        System.out.println("\n--- BAJA DE PRODUCTO ---");
        String codigo = leerTexto("Ingrese codigo del producto a eliminar: ");
        
        Producto producto = tablaProductos.buscar(codigo);
        if (producto == null) {
            System.out.println("Error: Producto no encontrado.");
            return;
        }
        
        tablaProductos.eliminar(codigo);
        colaStock.eliminar(codigo);
        
        // Registrar movimiento
        Movimiento mov = new Movimiento("BAJA", codigo, producto.getStock(), producto);
        pilaMovimientos.apilar(mov);
        
        System.out.println("Producto eliminado exitosamente.");
    }

    // 4. Modificar stock
    private static void modificarStock() {
        System.out.println("\n--- MODIFICAR STOCK ---");
        String codigo = leerTexto("Ingrese codigo del producto: ");
        
        Producto producto = tablaProductos.buscar(codigo);
        if (producto == null) {
            System.out.println("Error: Producto no encontrado.");
            return;
        }
        
        System.out.println("Stock actual: " + producto.getStock());
        double nuevoStock = leerDecimal("Ingrese nuevo stock: ");
        
        if (nuevoStock < 0) {
            System.out.println("Error: El stock no puede ser negativo.");
            return;
        }
        
        double stockAnterior = producto.getStock();
        
        // Registrar movimiento ANTES de modificar
        Movimiento mov = new Movimiento("MODIFICACION", codigo, stockAnterior, null);
        pilaMovimientos.apilar(mov);
        
        producto.setStock(nuevoStock);
        colaStock.actualizarStock(codigo, nuevoStock);
        
        System.out.println("Stock modificado de " + stockAnterior + " a " + nuevoStock);
    }

    // 5. Ver stock critico
    private static void verStockCritico() {
        System.out.println("\n--- PRODUCTOS CON STOCK CRITICO ---");
        
        if (colaStock.estaVacia()) {
            System.out.println("No hay productos en el inventario.");
            return;
        }
        
        int cantidad = leerEntero("Cuantos productos mostrar? ");
        Producto[] criticos = colaStock.obtenerProductosCriticos(cantidad);
        
        System.out.println("Productos con menor stock:");
        for (int i = 0; i < criticos.length; i++) {
            if (criticos[i] != null) {
                System.out.println((i + 1) + ". " + criticos[i].getNombre() + 
                                   " - Stock: " + criticos[i].getStock() + 
                                   " (Ubicacion: " + criticos[i].getUbicacion() + ")");
            }
        }
    }

    // 6. Crear pedido
    private static void crearPedido() {
        System.out.println("\n--- CREAR PEDIDO ---");
        
        contadorPedidos++;
        Pedido pedido = new Pedido(contadorPedidos, java.time.LocalDate.now().toString());
        
        String continuar = "s";
        while (continuar.equalsIgnoreCase("s")) {
            String codigo = leerTexto("Ingrese codigo de producto: ");
            
            Producto producto = tablaProductos.buscar(codigo);
            if (producto == null) {
                System.out.println("Producto no encontrado.");
            } else {
                pedido.agregarProducto(codigo);
                System.out.println("Producto agregado: " + producto.getNombre());
            }
            
            continuar = leerTexto("Agregar otro producto? (s/n): ");
        }
        
        if (pedido.getCantidadProductos() > 0) {
            colaPedidos.encolar(pedido);
            System.out.println("Pedido #" + pedido.getId() + " creado y encolado.");
        } else {
            System.out.println("Pedido cancelado (sin productos).");
        }
    }

    // 7. Despachar pedido
    private static void despacharPedido() {
        System.out.println("\n--- DESPACHAR PEDIDO ---");
        
        if (colaPedidos.estaVacia()) {
            System.out.println("Error: No hay pedidos en la cola de expedicion.");
            return;
        }
        
        Pedido pedido = colaPedidos.desencolar();
        System.out.println("Pedido despachado: " + pedido.toString());
    }

    // 8. Ver cola de pedidos
    private static void verColaPedidos() {
        System.out.println("\n--- COLA DE PEDIDOS ---");
        System.out.println("Pedidos en espera: " + colaPedidos.getTamanio());
        System.out.println(colaPedidos.mostrarCola());
    }

    // 9. Calcular ruta
    private static void calcularRuta() {
        System.out.println("\n--- CALCULAR RUTA DE RECOLECCION ---");
        
        String origen = leerTexto("Ingrese ubicacion de origen: ");
        if (!grafoDeposito.existeUbicacion(origen)) {
            System.out.println("Error: Ubicacion de origen no existe.");
            return;
        }
        
        String[] destinos = new String[20];
        int cantDestinos = 0;
        
        String continuar = "s";
        while (continuar.equalsIgnoreCase("s") && cantDestinos < 20) {
            String codigo = leerTexto("Ingrese codigo de producto a recolectar: ");
            Producto producto = tablaProductos.buscar(codigo);
            
            if (producto == null) {
                System.out.println("Producto no encontrado.");
            } else {
                destinos[cantDestinos] = producto.getUbicacion();
                cantDestinos++;
                System.out.println("Ubicacion agregada: " + producto.getUbicacion());
            }
            
            continuar = leerTexto("Agregar otro producto? (s/n): ");
        }
        
        if (cantDestinos > 0) {
            String ruta = grafoDeposito.calcularRuta(origen, destinos, cantDestinos);
            System.out.println("\n" + ruta);
        } else {
            System.out.println("No se agregaron productos.");
        }
    }

    // 10. Deshacer movimiento
    private static void deshacerMovimiento() {
        System.out.println("\n--- DESHACER ULTIMO MOVIMIENTO ---");
        
        if (pilaMovimientos.estaVacia()) {
            System.out.println("Error: No hay movimientos para deshacer.");
            return;
        }
        
        Movimiento mov = pilaMovimientos.desapilar();
        System.out.println("Deshaciendo: " + mov.toString());
        
        if (mov.getTipo().equals("ALTA")) {
            // Deshacer alta = eliminar producto
            tablaProductos.eliminar(mov.getCodigoProducto());
            colaStock.eliminar(mov.getCodigoProducto());
            System.out.println("Producto eliminado (se deshizo el alta).");
            
        } else if (mov.getTipo().equals("BAJA")) {
            // Deshacer baja = restaurar producto
            Producto prod = mov.getProductoCompleto();
            tablaProductos.insertar(prod.getCodigo(), prod);
            colaStock.insertar(prod);
            System.out.println("Producto restaurado (se deshizo la baja).");
            
        } else if (mov.getTipo().equals("MODIFICACION")) {
            // Deshacer modificacion = restaurar stock anterior
            Producto prod = tablaProductos.buscar(mov.getCodigoProducto());
            if (prod != null) {
                prod.setStock(mov.getStockAnterior());
                colaStock.actualizarStock(mov.getCodigoProducto(), mov.getStockAnterior());
                System.out.println("Stock restaurado a " + mov.getStockAnterior());
            }
        }
    }

    // 11. Ver historial
    private static void verHistorialMovimientos() {
        System.out.println("\n--- HISTORIAL DE MOVIMIENTOS ---");
        System.out.println("Total de movimientos: " + pilaMovimientos.getTamanio());
        System.out.println(pilaMovimientos.mostrarPila());
    }

    // 12. Ver mapa
    private static void verMapaDeposito() {
        System.out.println("\n--- MAPA DEL DEPOSITO ---");
        System.out.println(grafoDeposito.mostrarGrafo());
    }

    // 13. Listar productos
    private static void listarProductos() {
        System.out.println("\n--- LISTADO DE PRODUCTOS ---");
        Producto[] productos = tablaProductos.obtenerTodos();
        
        if (productos.length == 0) {
            System.out.println("No hay productos registrados.");
            return;
        }
        
        System.out.println("Total: " + productos.length + " productos\n");
        for (Producto p : productos) {
            System.out.println("  [" + p.getCodigo() + "] " + p.getNombre() + 
                             " | Stock: " + p.getStock() + 
                             " | Ubicacion: " + p.getUbicacion());
        }
    }

    // Cargar datos de prueba
    private static void cargarDatosPrueba() {
        // Crear ubicaciones del deposito
        grafoDeposito.agregarUbicacion("Entrada");
        grafoDeposito.agregarUbicacion("Pasillo-A");
        grafoDeposito.agregarUbicacion("Pasillo-B");
        grafoDeposito.agregarUbicacion("Pasillo-C");
        grafoDeposito.agregarUbicacion("Almacen-1");
        grafoDeposito.agregarUbicacion("Almacen-2");
        
        // Crear conexiones (distancias en metros)
        grafoDeposito.agregarConexion("Entrada", "Pasillo-A", 10.5);
        grafoDeposito.agregarConexion("Entrada", "Pasillo-B", 8.0);
        grafoDeposito.agregarConexion("Entrada", "Pasillo-C", 12.75);
        grafoDeposito.agregarConexion("Pasillo-A", "Pasillo-B", 5.25);
        grafoDeposito.agregarConexion("Pasillo-B", "Pasillo-C", 6.5);
        grafoDeposito.agregarConexion("Pasillo-A", "Almacen-1", 7.0);
        grafoDeposito.agregarConexion("Pasillo-B", "Almacen-1", 9.3);
        grafoDeposito.agregarConexion("Pasillo-C", "Almacen-2", 4.25);
        grafoDeposito.agregarConexion("Almacen-1", "Almacen-2", 3.5);
        
        // Crear productos de prueba
        Producto p1 = new Producto("P001", "Tornillos", 120.0, "Pasillo-A");
        Producto p2 = new Producto("P002", "Harina", 3.75, "Almacen-1");
        Producto p3 = new Producto("P003", "Tuercas", 45.0, "Pasillo-B");
        Producto p4 = new Producto("P004", "Aceite", 7.5, "Almacen-2");
        Producto p5 = new Producto("P005", "Clavos", 200.0, "Pasillo-C");
        
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
        
        System.out.println("Datos de prueba cargados exitosamente.");
    }

    //auxi para leer entrada
    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        try {
            int valor = Integer.parseInt(scanner.nextLine().trim());
            return valor;
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un numero entero valido.");
            return leerEntero(mensaje);
        }
    }

    private static double leerDecimal(String mensaje) {
        System.out.print(mensaje);
        try {
            double valor = Double.parseDouble(scanner.nextLine().trim());
            return valor;
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un numero decimal valido.");
            return leerDecimal(mensaje);
        }
    }
}
