/*
 * CLASE Main
 * ----------
 * Es el punto de entrada del programa. Java busca el método main()
 * aquí para empezar a ejecutar.
 *
 * Se muestra un menú en bucle hasta que el usuario elija "Salir".
 * Cada opción llama al método correspondiente de la lista.
 */

// Scanner es una clase de Java que nos permite leer lo que escribe el usuario
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Se crea la lista doble vacía donde se guardarán los clientes
        ListaDoble lista = new ListaDoble();

        // Scanner conectado a System.in (el teclado) para leer entradas del usuario.
        // Locale.US garantiza que el punto "." sea el separador decimal,
        // independientemente del idioma configurado en el sistema operativo.
        Scanner sc = new Scanner(System.in).useLocale(Locale.US);

        // Variable que guarda la opción elegida por el usuario en cada iteración
        int opcion = 0;

        /*
         * Bucle do-while: ejecuta el cuerpo AL MENOS una vez.
         * Sigue repitiendo mientras la opción NO sea 7 (Salir).
         * Es ideal para menús porque siempre queremos mostrar las opciones
         * antes de revisar si el usuario quiere salir.
         */
        do {
            // Se imprime el menú en cada vuelta del bucle
            System.out.println("\n--- LISTA DOBLE DE CLIENTES ---");
            System.out.println("1. Insertar cliente");
            System.out.println("2. Mostrar lista");
            System.out.println("3. Actualizar cliente");
            System.out.println("4. Eliminar cliente");
            System.out.println("5. Promedio de edades");
            System.out.println("6. % clientes con crédito mayor al promedio");
            System.out.println("7. Salir");
            System.out.print("Opción: ");

            /*
             * try-catch: bloque de seguridad.
             * Si el usuario escribe algo que no sea un número,
             * en vez de que el programa se "rompa" con un error,
             * el catch lo atrapa y muestra un mensaje amigable.
             */
            try {
                opcion = sc.nextInt(); // lee el número de opción
                /*
                 * sc.nextLine() DESPUÉS de nextInt() es necesario para "limpiar"
                 * el salto de línea que deja nextInt() en el buffer del teclado.
                 * Sin esto, el siguiente sc.nextLine() leería una línea vacía.
                 */
                sc.nextLine();

                // switch evalúa "opcion" y salta directo al case que coincide
                switch (opcion) {

                    case 1: // ---------- INSERTAR ----------
                        System.out.print("Código: ");
                        String codigo = sc.nextLine();   // lee texto completo
                        System.out.print("Nombre: ");
                        String nombre = sc.nextLine();
                        System.out.print("Edad: ");
                        int edad = sc.nextInt();          // lee entero
                        System.out.print("Crédito: ");
                        float credito = sc.nextFloat();   // lee decimal
                        sc.nextLine();                    // limpia el buffer

                        // Se crea un objeto Cliente con los datos ingresados
                        // y se inserta en la lista
                        lista.insertar(new Cliente(codigo, nombre, edad, credito));
                        System.out.println("Cliente insertado.");
                        break; // break evita que el código "caiga" al siguiente case

                    case 2: // ---------- MOSTRAR ----------
                        lista.mostrar();
                        break;

                    case 3: // ---------- ACTUALIZAR ----------
                        System.out.print("Código a buscar: ");
                        String codBuscar = sc.nextLine();
                        System.out.print("Nuevo nombre: ");
                        String nuevoNombre = sc.nextLine();
                        System.out.print("Nueva edad: ");
                        int nuevaEdad = sc.nextInt();
                        System.out.print("Nuevo crédito: ");
                        float nuevoCredito = sc.nextFloat();
                        sc.nextLine();

                        // actualizar() devuelve true si encontró el código, false si no
                        if (lista.actualizar(codBuscar, nuevoNombre, nuevaEdad, nuevoCredito))
                            System.out.println("Cliente actualizado.");
                        else
                            System.out.println("Código no encontrado.");
                        break;

                    case 4: // ---------- ELIMINAR ----------
                        System.out.print("Código a eliminar: ");
                        String codEliminar = sc.nextLine();

                        // eliminar() devuelve true si encontró y eliminó el nodo
                        if (lista.eliminar(codEliminar))
                            System.out.println("Cliente eliminado.");
                        else
                            System.out.println("Código no encontrado.");
                        break;

                    case 5: // ---------- PROMEDIO DE EDADES ----------
                        // %.2f formatea el double con 2 decimales
                        System.out.printf("Promedio de edades: %.2f años%n",
                                lista.promedioEdades());
                        break;

                    case 6: // ---------- % CRÉDITO MAYOR AL PROMEDIO ----------
                        // %% imprime el símbolo "%" literal (un solo % es especial en printf)
                        System.out.printf("Clientes con crédito mayor al promedio: %.2f%%%n",
                                lista.porcentajeCreditoMayorAlPromedio());
                        break;

                    case 7: // ---------- SALIR ----------
                        System.out.println("Saliendo...");
                        break;

                    default:
                        // Se ejecuta si el usuario escribe un número fuera del 1-7
                        System.out.println("Opción no válida.");
                }

            } catch (Exception e) {
                // Si el usuario escribe letras donde se espera un número,
                // se llega aquí. sc.nextLine() limpia lo que quedó en el buffer.
                System.out.println("Entrada no válida.");
                sc.nextLine();
            }

        } while (opcion != 7); // repite el menú mientras no se elija Salir

        sc.close(); // buena práctica: liberar el recurso Scanner al terminar
    }
}
