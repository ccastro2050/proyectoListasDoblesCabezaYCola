/*
 * CLASE Cliente
 * -------------
 * Representa a un cliente y también funciona como nodo de la lista doble.
 *
 * Cada objeto Cliente tiene dos partes:
 *   1. Sus DATOS: código, nombre, edad y crédito.
 *   2. Sus ENLACES: dos punteros que lo conectan con el cliente
 *      anterior y el siguiente dentro de la lista.
 *
 * Visualmente, cada objeto Cliente es un eslabón de la cadena:
 *
 *   ┌──────────┬──────────────────────────┬───────────┐
 *   │ anterior │  codigo, nombre,         │ siguiente │
 *   │  (apunta │  edad, credito           │  (apunta  │
 *   │ al previo│                          │ al próximo│
 *   └──────────┴──────────────────────────┴───────────┘
 *
 * Cuando "anterior" es null → este cliente es el primero (cabeza).
 * Cuando "siguiente" es null → este cliente es el último (cola).
 */
public class Cliente {

    // --- Datos del cliente ---
    String codigo;   // identificador único, ej. "C001"
    String nombre;   // nombre completo, ej. "Ana López"
    int    edad;     // número entero, ej. 30
    float  credito;  // número con decimales, ej. 1500.50

    // --- Punteros de la lista doble ---
    // Apuntan al cliente anterior y siguiente en la cadena.
    // Son null cuando no hay vecino en esa dirección.
    Cliente siguiente;
    Cliente anterior;

    /*
     * Constructor
     * -----------
     * Se llama al escribir: new Cliente("C001", "Ana", 30, 1500.50f)
     * Asigna los datos y deja los punteros en null porque el cliente
     * recién creado todavía no está enlazado a ningún vecino.
     */
    public Cliente(String codigo, String nombre, int edad, float credito) {
        this.codigo    = codigo;
        this.nombre    = nombre;
        this.edad      = edad;
        this.credito   = credito;
        this.siguiente = null;  // sin vecino derecho todavía
        this.anterior  = null;  // sin vecino izquierdo todavía
    }

    /*
     * toString()
     * ----------
     * En Java, TODAS las clases heredan un método llamado toString()
     * que convierte el objeto en texto. El problema es que la versión
     * que trae Java por defecto muestra algo así:
     *
     *      Cliente@3a5b7c8d   ← dirección de memoria, inútil para nosotros
     *
     * Con @Override le decimos a Java: "no uses tu versión, usa LA NUESTRA".
     * Así cuando hacemos System.out.println(unCliente), Java llama a
     * nuestro toString() y muestra algo legible como:
     *
     *      [Cod: C001 | Nombre: Ana López | Edad: 30 | Crédito: 1500.5]
     *
     * Esto se usa en el método mostrar() de ListaDoble con: System.out.println(aux)
     * Java detecta que "aux" es un objeto y llama a toString() automáticamente.
     */
    @Override
    public String toString() {
        return "[Codigo: " + codigo + " | Nombre: " + nombre
             + " | Edad: " + edad + " | Crédito: " + credito + "]";
    }
}
