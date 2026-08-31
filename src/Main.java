/*
 * CLASE Main
 * ----------
 * Es el punto de entrada del programa: Java busca el método main()
 * aquí para empezar a ejecutar.
 *
 * Muestra un menú en bucle hasta que el usuario elija "Salir".
 * Cada opción llama al método correspondiente de la lista.
 *
 * IDEA CLAVE DE LA LECTURA DE DATOS
 * ---------------------------------
 * Todo se lee con nextLine() (siempre una línea completa) y después
 * se convierte a número con Integer.parseInt() / Float.parseFloat().
 *
 * ¿Por qué no usar nextInt() y nextFloat() directamente? Porque esos
 * métodos dejan el salto de línea "\n" sin consumir en el buffer, y el
 * siguiente nextLine() leería una línea vacía. Leyendo siempre líneas
 * completas ese problema clásico simplemente no existe.
 */

// Scanner es la clase de Java que permite leer lo que escribe el usuario
import java.util.Scanner;

public class Main {

    // Scanner conectado a System.in (el teclado). Es static para que
    // los métodos de ayuda de abajo puedan usarlo sin recibirlo por parámetro.
    private static final Scanner sc = new Scanner(System.in);

    // Se pone en true cuando ya no hay más entrada que leer
    // (el usuario pulsó Ctrl+Z / Ctrl+D, o el programa se alimentó
    // desde un archivo que se terminó). Sirve para salir sin errores.
    private static boolean finDeEntrada = false;

    public static void main(String[] args) {

        // Se crea la lista doble vacía donde se guardarán los clientes
        ListaDoble lista = new ListaDoble();

        int opcion = 0;

        /*
         * Bucle do-while: ejecuta el cuerpo AL MENOS una vez.
         * Repite mientras la opción NO sea 8 (Salir) y siga habiendo entrada.
         * Es ideal para menús porque siempre queremos mostrar las opciones
         * antes de revisar si el usuario quiere salir.
         */
        do {
            System.out.println();
            System.out.println("--- LISTA DOBLE DE CLIENTES ---");
            System.out.println("1. Insertar cliente");
            System.out.println("2. Mostrar lista (cabeza -> cola)");
            System.out.println("3. Mostrar lista (cola -> cabeza)");
            System.out.println("4. Actualizar cliente");
            System.out.println("5. Eliminar cliente");
            System.out.println("6. Promedio de edades");
            System.out.println("7. % clientes con crédito mayor al promedio");
            System.out.println("8. Salir");

            opcion = leerEntero("Opción: ");
            if (finDeEntrada) break;   // ya no hay nada que leer: terminar

            // switch evalúa "opcion" y salta directo al case que coincide
            switch (opcion) {

                case 1: // ---------- INSERTAR ----------
                    insertarCliente(lista);
                    break;              // break evita que el código "caiga" al siguiente case

                case 2: // ---------- MOSTRAR ADELANTE ----------
                    lista.mostrar();
                    break;

                case 3: // ---------- MOSTRAR ATRÁS ----------
                    lista.mostrarInverso();
                    break;

                case 4: // ---------- ACTUALIZAR ----------
                    actualizarCliente(lista);
                    break;

                case 5: // ---------- ELIMINAR ----------
                    eliminarCliente(lista);
                    break;

                case 6: // ---------- PROMEDIO DE EDADES ----------
                    if (lista.estaVacia()) {
                        System.out.println("No hay clientes registrados.");
                    } else {
                        // %.2f formatea el double con 2 decimales
                        System.out.printf("Promedio de edades: %.2f años%n", lista.promedioEdades());
                    }
                    break;

                case 7: // ---------- % CRÉDITO MAYOR AL PROMEDIO ----------
                    if (lista.estaVacia()) {
                        System.out.println("No hay clientes registrados.");
                    } else {
                        System.out.printf("Crédito promedio: %.2f%n", lista.promedioCredito());
                        // %% imprime el símbolo "%" literal (un solo % es especial en printf)
                        System.out.printf("Clientes con crédito mayor al promedio: %.2f%%%n",
                                lista.porcentajeCreditoMayorAlPromedio());
                    }
                    break;

                case 8: // ---------- SALIR ----------
                    System.out.println("Saliendo...");
                    break;

                default:
                    // Se ejecuta si el usuario escribe un número fuera del 1-8
                    System.out.println("Opción no válida. Elige un número del 1 al 8.");
            }

        } while (opcion != 8 && !finDeEntrada);

        sc.close();   // buena práctica: liberar el recurso Scanner al terminar
    }

    // =========================================================
    // ACCIONES DEL MENÚ
    // =========================================================
    /*
     * Cada opción que pide varios datos vive en su propio método.
     * Así el switch de main() queda corto y se lee de un vistazo.
     */

    private static void insertarCliente(ListaDoble lista) {
        String codigo = leerTextoNoVacio("Código: ");
        String nombre = leerTextoNoVacio("Nombre: ");
        int    edad    = leerEnteroEnRango("Edad: ", 0, 120);
        float  credito = leerCredito("Crédito: ");
        if (finDeEntrada) return;                 // se acabó la entrada a mitad: no insertar

        // Se crea el objeto Cliente con los datos ingresados y se inserta.
        // insertar() devuelve false si el código ya estaba registrado.
        if (lista.insertar(new Cliente(codigo, nombre, edad, credito))) {
            System.out.println("Cliente insertado.");
        } else {
            System.out.println("Ya existe un cliente con el código " + codigo + ".");
        }
    }

    private static void actualizarCliente(ListaDoble lista) {
        String codigo = leerTextoNoVacio("Código a buscar: ");
        if (finDeEntrada) return;

        // Se avisa antes de pedir los datos nuevos, para no hacer
        // escribir tres campos que después no se van a usar.
        if (lista.buscar(codigo) == null) {
            System.out.println("Código no encontrado.");
            return;
        }

        String nombre  = leerTextoNoVacio("Nuevo nombre: ");
        int    edad    = leerEnteroEnRango("Nueva edad: ", 0, 120);
        float  credito = leerCredito("Nuevo crédito: ");
        if (finDeEntrada) return;

        lista.actualizar(codigo, nombre, edad, credito);
        System.out.println("Cliente actualizado.");
    }

    private static void eliminarCliente(ListaDoble lista) {
        String codigo = leerTextoNoVacio("Código a eliminar: ");
        if (finDeEntrada) return;

        // eliminar() devuelve true si encontró y desenlazó al cliente
        if (lista.eliminar(codigo)) {
            System.out.println("Cliente eliminado.");
        } else {
            System.out.println("Código no encontrado.");
        }
    }

    // =========================================================
    // MÉTODOS DE AYUDA PARA LEER DATOS
    // =========================================================
    /*
     * Todos siguen el mismo patrón: preguntan una y otra vez hasta
     * recibir un dato válido. Así la lista nunca guarda basura y el
     * programa nunca se rompe por una entrada mal escrita.
     */

    /* Lee una línea de texto. Devuelve "" si ya no hay más entrada. */
    private static String leerLinea(String mensaje) {
        System.out.print(mensaje);

        // hasNextLine() responde false cuando la entrada se terminó.
        // Sin esta comprobación, nextLine() lanzaría NoSuchElementException.
        if (!sc.hasNextLine()) {
            finDeEntrada = true;
            System.out.println();
            return "";
        }

        return sc.nextLine().trim();   // trim() quita espacios sobrantes
    }

    /* Repite la pregunta hasta que el usuario escriba algo. */
    private static String leerTextoNoVacio(String mensaje) {
        while (!finDeEntrada) {
            String texto = leerLinea(mensaje);
            if (finDeEntrada) break;
            if (!texto.isEmpty()) return texto;
            System.out.println("  * No puede quedar vacío.");
        }
        return "";
    }

    /* Repite la pregunta hasta que el usuario escriba un número entero. */
    private static int leerEntero(String mensaje) {
        while (!finDeEntrada) {
            String texto = leerLinea(mensaje);
            if (finDeEntrada) break;
            try {
                // parseInt convierte el texto "30" en el número 30.
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                // Si el texto no es un número (por ejemplo "hola"),
                // parseInt lanza esta excepción y aquí la atrapamos
                // para volver a preguntar en vez de romper el programa.
                System.out.println("  * Escribe un número entero válido.");
            }
        }
        return 0;
    }

    /* Igual que leerEntero, pero además exige que esté entre min y max. */
    private static int leerEnteroEnRango(String mensaje, int min, int max) {
        while (!finDeEntrada) {
            int valor = leerEntero(mensaje);
            if (finDeEntrada) break;
            if (valor >= min && valor <= max) return valor;
            System.out.println("  * Debe estar entre " + min + " y " + max + ".");
        }
        return min;
    }

    /* Lee un crédito: número con decimales y que no sea negativo. */
    private static float leerCredito(String mensaje) {
        while (!finDeEntrada) {
            String texto = leerLinea(mensaje);
            if (finDeEntrada) break;
            // Se acepta tanto "1500.50" como "1500,50" cambiando la coma por punto,
            // porque Java solo entiende el punto como separador decimal.
            texto = texto.replace(',', '.');
            try {
                float valor = Float.parseFloat(texto);
                if (valor >= 0) return valor;
                System.out.println("  * El crédito no puede ser negativo.");
            } catch (NumberFormatException e) {
                System.out.println("  * Escribe un número válido (ejemplo: 1500.50).");
            }
        }
        return 0f;
    }
}
