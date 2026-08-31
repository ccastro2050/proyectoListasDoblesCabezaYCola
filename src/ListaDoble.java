/*
 * ENUNCIADO DEL PROBLEMA
 * ----------------------
 * Implementar una lista doblemente enlazada con referencias cabeza y cola
 * que almacene datos de clientes. Cada cliente tiene: código (String),
 * nombre (String), edad (int) y crédito (float).
 *
 * Aquí la clase Cliente ES el nodo de la lista (tiene los punteros
 * siguiente y anterior dentro de ella), por lo que no se necesita
 * una clase Nodo separada.
 *
 * Operaciones requeridas:
 *   - Insertar un nuevo cliente al final de la lista.
 *   - Mostrar todos los clientes de cabeza a cola (y de cola a cabeza).
 *   - Actualizar los datos de un cliente buscándolo por código.
 *   - Eliminar un cliente por código, ajustando cabeza y cola si es necesario.
 *   - Calcular el promedio de edades de todos los clientes.
 *   - Calcular el porcentaje de clientes cuyo crédito supera el promedio.
 *
 * ESTRUCTURA EN MEMORIA
 * ---------------------
 *   cabeza                                                   cola
 *     |                                                        |
 *     v                                                        v
 *   null <- [C001] <-> [C002] <-> [C003] <-> ... <-> [C00N] -> null
 *
 * La cabeza es el primer cliente y la cola el último. Tener AMBAS
 * referencias permite insertar al final en un solo paso y recorrer
 * la lista en los dos sentidos.
 */
public class ListaDoble {

    // Referencia al primer cliente de la lista (cabeza)
    private Cliente cabeza;

    // Referencia al último cliente de la lista (cola).
    // Tener la cola nos permite insertar al final sin recorrer toda la lista.
    private Cliente cola;

    // Cantidad de clientes guardados. Se mantiene actualizada en insertar()
    // y eliminar() para no tener que recorrer la lista solo para contar.
    private int tamanio;

    // Constructor: la lista inicia vacía, sin cabeza ni cola
    public ListaDoble() {
        this.cabeza  = null;
        this.cola    = null;
        this.tamanio = 0;
    }

    // =========================================================
    // CONSULTAS BÁSICAS
    // =========================================================

    /* La lista está vacía cuando no hay cabeza. */
    public boolean estaVacia() {
        return cabeza == null;
    }

    /* Cuántos clientes hay guardados. */
    public int tamanio() {
        return tamanio;
    }

    /*
     * BUSCAR
     * ------
     * Recorre la lista desde la cabeza y devuelve el cliente cuyo
     * código coincide, o null si no existe.
     *
     * Este método es la base de insertar(), actualizar() y eliminar():
     * escribir la búsqueda UNA sola vez evita repetir el mismo while
     * en tres lugares distintos.
     */
    public Cliente buscar(String codigo) {
        Cliente aux = cabeza;                 // puntero auxiliar: empieza en la cabeza
        while (aux != null) {                 // mientras queden clientes por revisar
            // equalsIgnoreCase() compara el CONTENIDO del texto sin distinguir
            // mayúsculas, así "c001" y "C001" son el mismo cliente.
            // (Con == se compararían direcciones de memoria, no las letras).
            if (aux.codigo.equalsIgnoreCase(codigo)) {
                return aux;                   // encontrado
            }
            aux = aux.siguiente;              // avanza un eslabón
        }
        return null;                          // recorrió todo y no lo encontró
    }

    // =========================================================
    // CREATE: Insertar un nuevo cliente al final de la lista
    // =========================================================
    /*
     * Devuelve true si lo insertó, y false si ya existía otro cliente
     * con el mismo código. El código es el identificador único: si se
     * permitieran repetidos, actualizar() y eliminar() no sabrían a
     * cuál de los dos se refiere el usuario.
     */
    public boolean insertar(Cliente nuevo) {

        if (buscar(nuevo.codigo) != null) {
            return false;   // código repetido: no se inserta
        }

        if (cabeza == null) {
            // Caso A - Lista vacía: el nuevo cliente es a la vez cabeza y cola
            cabeza = nuevo;
            cola   = nuevo;
        } else {
            // Caso B - Lista con al menos un cliente:
            // 1. La cola actual apunta hacia adelante al nuevo
            cola.siguiente = nuevo;
            // 2. El nuevo apunta hacia atrás a la cola actual
            nuevo.anterior = cola;
            // 3. El nuevo cliente pasa a ser la nueva cola
            cola = nuevo;
        }

        tamanio++;
        return true;
    }

    // =========================================================
    // READ: Mostrar todos los clientes de cabeza a cola
    // =========================================================
    public void mostrar() {
        if (estaVacia()) {
            System.out.println("Lista vacía.");
            return;
        }

        // "aux" es un puntero auxiliar que avanza por la lista
        // sin perder la referencia original de cabeza
        Cliente aux = cabeza;
        System.out.println("CABEZA");
        while (aux != null) {
            System.out.println("  v  " + aux);   // llama a toString() de Cliente
            aux = aux.siguiente;                 // avanza al siguiente
        }
        System.out.println("COLA   (" + tamanio + " cliente(s))");
    }

    // =========================================================
    // READ: Mostrar de cola a cabeza (recorrido inverso)
    // =========================================================
    /*
     * Esta es la ventaja concreta de una lista DOBLE: con los punteros
     * "anterior" se puede recorrer hacia atrás sin invertir nada.
     * En una lista simple habría que recorrerla entera para cada paso.
     */
    public void mostrarInverso() {
        if (estaVacia()) {
            System.out.println("Lista vacía.");
            return;
        }

        Cliente aux = cola;                      // ahora se empieza por el final
        System.out.println("COLA");
        while (aux != null) {
            System.out.println("  ^  " + aux);
            aux = aux.anterior;                  // retrocede un eslabón
        }
        System.out.println("CABEZA (" + tamanio + " cliente(s))");
    }

    // =========================================================
    // UPDATE: Buscar un cliente por código y modificar sus datos
    // =========================================================
    public boolean actualizar(String codigo, String nuevoNombre, int nuevaEdad, float nuevoCredito) {
        Cliente cliente = buscar(codigo);

        if (cliente == null) {
            return false;              // no se encontró el código
        }

        // El código NO se cambia: es la identidad del cliente.
        cliente.nombre  = nuevoNombre;
        cliente.edad    = nuevaEdad;
        cliente.credito = nuevoCredito;
        return true;                   // actualización exitosa
    }

    // =========================================================
    // DELETE: Eliminar el cliente con el código indicado
    // =========================================================
    /*
     * Eliminar en una lista enlazada NO es borrar memoria: es
     * "saltarse" el nodo, haciendo que sus vecinos se apunten
     * entre ellos. Al quedar sin nadie que lo referencie, Java
     * lo libera solo (recolector de basura).
     *
     * Hay 4 casos que deben tratarse por separado:
     *   1. Es el único cliente  -> la lista queda vacía
     *   2. Es la cabeza         -> la cabeza avanza
     *   3. Es la cola           -> la cola retrocede
     *   4. Está en medio        -> se unen sus dos vecinos
     */
    public boolean eliminar(String codigo) {
        Cliente aux = buscar(codigo);

        if (aux == null) {
            return false;              // no se encontró el código
        }

        // Caso 1: Es el único cliente -> la lista queda vacía
        if (aux == cabeza && aux == cola) {
            cabeza = null;
            cola   = null;
        }
        // Caso 2: Es la cabeza -> el siguiente pasa a ser la nueva cabeza
        else if (aux == cabeza) {
            cabeza          = cabeza.siguiente;
            cabeza.anterior = null;    // la nueva cabeza no tiene anterior
        }
        // Caso 3: Es la cola -> el anterior pasa a ser la nueva cola
        else if (aux == cola) {
            cola           = cola.anterior;
            cola.siguiente = null;     // la nueva cola no tiene siguiente
        }
        // Caso 4: Cliente intermedio -> sus vecinos se enlazan entre sí
        else {
            aux.anterior.siguiente = aux.siguiente;
            aux.siguiente.anterior = aux.anterior;
        }

        // El cliente eliminado se desconecta del todo. Si no se hiciera,
        // seguiría apuntando a nodos de la lista y quien conservara una
        // referencia a él podría "volver a entrar" a la lista por error.
        aux.siguiente = null;
        aux.anterior  = null;

        tamanio--;
        return true;                   // eliminación exitosa
    }

    // =========================================================
    // STAT: Calcular el promedio de edades de los clientes
    // =========================================================
    public double promedioEdades() {
        if (estaVacia()) return 0;     // sin clientes no hay promedio (evita dividir entre 0)

        Cliente aux = cabeza;
        int suma = 0;

        while (aux != null) {
            suma += aux.edad;          // acumular edades
            aux = aux.siguiente;
        }

        // Cast a double para que la división sea decimal, no entera.
        // Sin el cast, 25/2 daría 12 en vez de 12.5
        return (double) suma / tamanio;
    }

    // =========================================================
    // STAT: Calcular el promedio de crédito
    // =========================================================
    public double promedioCredito() {
        if (estaVacia()) return 0;

        Cliente aux = cabeza;
        double suma = 0;

        while (aux != null) {
            suma += aux.credito;
            aux = aux.siguiente;
        }

        return suma / tamanio;
    }

    // =========================================================
    // STAT: Porcentaje de clientes cuyo crédito supera el promedio
    // =========================================================
    /*
     * Se necesitan DOS recorridos y en este orden:
     *   1o) uno para conocer el promedio -> lo hace promedioCredito()
     *   2o) otro para contar cuántos lo superan
     * No se puede en uno solo, porque el promedio depende de TODOS
     * los clientes, incluidos los que aún no se han leído.
     */
    public double porcentajeCreditoMayorAlPromedio() {
        if (estaVacia()) return 0;

        double promedio = promedioCredito();   // 1er recorrido

        int mayores = 0;
        Cliente aux = cabeza;

        while (aux != null) {                  // 2do recorrido
            if (aux.credito > promedio) mayores++;
            aux = aux.siguiente;
        }

        // Porcentaje = (clientes con crédito mayor / total) x 100
        return (double) mayores / tamanio * 100;
    }
}
