/*
 * ENUNCIADO DEL PROBLEMA
 * ----------------------
 * Implementar una lista doblemente enlazada con referencias cabeza y cola
 * que almacene datos de clientes. Cada cliente tiene: código (String),
 * nombre (String), edad (int) y crédito (float).
 *
 * Ahora la clase Cliente ES el nodo de la lista (tiene los punteros
 * siguiente y anterior dentro de ella), por lo que no se necesita
 * una clase Nodo separada.
 *
 * Operaciones requeridas:
 *   - Insertar un nuevo cliente al final de la lista.
 *   - Mostrar todos los clientes de cabeza a cola.
 *   - Actualizar los datos de un cliente buscándolo por código.
 *   - Eliminar un cliente por código, ajustando cabeza y cola si es necesario.
 *   - Calcular el promedio de edades de todos los clientes.
 *   - Calcular el porcentaje de clientes cuyo crédito supera el promedio.
 */
public class ListaDoble {

    // Referencia al primer cliente de la lista (cabeza)
    private Cliente cabeza;

    // Referencia al último cliente de la lista (cola).
    // Tener la cola nos permite insertar al final sin recorrer toda la lista.
    private Cliente cola;

    // Constructor: la lista inicia vacía, sin cabeza ni cola
    public ListaDoble() {
        this.cabeza = null;
        this.cola   = null;
    }

    // =========================================================
    // CREATE: Insertar un nuevo cliente al final de la lista
    // =========================================================
    public void insertar(Cliente nuevo) {

        if (cabeza == null) {
            // Lista vacía: el nuevo cliente es a la vez cabeza y cola
            cabeza = nuevo;
            cola   = nuevo;
        } else {
            // Lista con al menos un cliente:
            // 1. La cola actual apunta al nuevo como su siguiente
            cola.siguiente = nuevo;
            // 2. El nuevo apunta hacia atrás a la cola actual
            nuevo.anterior = cola;
            // 3. El nuevo cliente pasa a ser la nueva cola
            cola = nuevo;
        }
    }

    // =========================================================
    // READ: Mostrar todos los clientes de cabeza a cola
    // =========================================================
    public void mostrar() {
        if (cabeza == null) {
            System.out.println("Lista vacía.");
            return;
        }

        // "aux" es un puntero auxiliar que avanza por la lista
        // sin perder la referencia original de cabeza
        Cliente aux = cabeza;
        System.out.println("Cabeza <->");
        while (aux != null) {
            System.out.println("  " + aux);   // llama a toString() de Cliente
            aux = aux.siguiente;              // avanza al siguiente
        }
        System.out.println("<-> Cola");
    }

    // =========================================================
    // UPDATE: Buscar un cliente por código y modificar sus datos
    // =========================================================
    public boolean actualizar(String codigo, String nuevoNombre, int nuevaEdad, float nuevoCredito) {
        Cliente aux = cabeza;

        while (aux != null) {
            // equals() compara el contenido del String, no la dirección en memoria
            if (aux.codigo.equals(codigo)) {
                aux.nombre  = nuevoNombre;
                aux.edad    = nuevaEdad;
                aux.credito = nuevoCredito;
                return true;  // actualización exitosa
            }
            aux = aux.siguiente;
        }

        return false; // no se encontró el código
    }

    // =========================================================
    // STAT: Calcular el promedio de edades de los clientes
    // =========================================================
    public double promedioEdades() {
        if (cabeza == null) return 0;

        Cliente aux = cabeza;
        int suma  = 0;
        int count = 0;

        while (aux != null) {
            suma  += aux.edad; // acumular edades
            count++;           // contar clientes
            aux = aux.siguiente;
        }

        // Cast a double para que la división sea decimal, no entera
        return (double) suma / count;
    }

    // =========================================================
    // STAT: Porcentaje de clientes cuyo crédito supera el promedio
    // =========================================================
    public double porcentajeCreditoMayorAlPromedio() {
        if (cabeza == null) return 0;

        // Primer recorrido: calcular el promedio de crédito
        Cliente aux = cabeza;
        double sumaCredito = 0;
        int count = 0;

        while (aux != null) {
            sumaCredito += aux.credito;
            count++;
            aux = aux.siguiente;
        }

        double promedio = sumaCredito / count;

        // Segundo recorrido: contar cuántos superan ese promedio
        int mayores = 0;
        aux = cabeza;

        while (aux != null) {
            if (aux.credito > promedio) mayores++;
            aux = aux.siguiente;
        }

        // Porcentaje = (clientes con crédito mayor / total) × 100
        return (double) mayores / count * 100;
    }

    // =========================================================
    // DELETE: Eliminar el cliente con el código indicado
    // =========================================================
    public boolean eliminar(String codigo) {
        if (cabeza == null) return false;

        Cliente aux = cabeza;

        while (aux != null) {
            if (aux.codigo.equals(codigo)) {

                // Caso 1: Es el único cliente → la lista queda vacía
                if (aux == cabeza && aux == cola) {
                    cabeza = null;
                    cola   = null;
                }
                // Caso 2: Es la cabeza → el siguiente pasa a ser la nueva cabeza
                else if (aux == cabeza) {
                    cabeza          = cabeza.siguiente;
                    cabeza.anterior = null;
                }
                // Caso 3: Es la cola → el anterior pasa a ser la nueva cola
                else if (aux == cola) {
                    cola           = cola.anterior;
                    cola.siguiente = null;
                }
                // Caso 4: Cliente intermedio → sus vecinos se enlazan entre sí
                else {
                    aux.anterior.siguiente = aux.siguiente;
                    aux.siguiente.anterior = aux.anterior;
                }

                return true; // eliminación exitosa
            }
            aux = aux.siguiente;
        }

        return false; // no se encontró el código
    }
}
