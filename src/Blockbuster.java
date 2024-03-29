package src;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Blockbuster {
    public static void main(String[] args) {
        Blockbuster memorabilia = new Blockbuster();
    }

    Scanner scanner = new Scanner(System.in);
    String[][] clientes, peliculas, categorias, prestamoPeliculas; //[fila][columna]
    boolean[] estadoClientes, estadoPelis;

    public Blockbuster() {
        final int CANTIDAD_TOTAL = 30; //CAMBIAR A 30 LA CANTIDAD
        clientes = new String [CANTIDAD_TOTAL][3];
        estadoClientes = new boolean [CANTIDAD_TOTAL];
        peliculas = new String [CANTIDAD_TOTAL][5];
        estadoPelis = new boolean [CANTIDAD_TOTAL];
        prestamoPeliculas = new String [CANTIDAD_TOTAL][3];
        categorias = new String[CANTIDAD_TOTAL][2];
        menu();
    }
    
    public boolean verificarDisponibilidad(boolean[] estados, boolean disponible){
        boolean hay = false;
        int contador = 0;
        if ((peliculas[0][0] != null) || (clientes[0][0] != null)) {
            for (int i = 0; i < estados.length; i++) {
                if (estados[i] == disponible) {
                    contador++;
                }
            }
        }
        if (contador > 0) {
            hay = true;
        }
        return hay;
    }

    public String [][] ordenarNumerico(String[][] datos, int columna, boolean[] estados, boolean tieneEstados){
        if (datosNetos(datos) > 1) {
            for (int i = 0; i < datosNetos(datos) - 1; i++) {
                for (int j = 0; j < datosNetos(datos) - 1 - i; j++) {
                    int primero = Integer.valueOf(datos[j+1][columna]);
                    int segundo = Integer.valueOf(datos[j][columna]);
    
                    if (segundo > primero) {
                        String[] filaTemp = datos[j];
                        datos[j] = datos[j+1];
                        datos[j+1] = filaTemp;

                        if (tieneEstados == true) {
                            boolean estadoAux = estados[j];
                            estados[j] = estados[j+1];
                            estados[j+1] = estadoAux; 
                        }
                    }
                }
            }
        }

        return datos;
    }

    public String [][] ordenarAlfabetico(String[][] datos, int columna, boolean[] estados, boolean tieneEstados){
        if (datosNetos(datos) > 2) {
            for (int i = 0; i < datosNetos(datos) - 1; i++) {
                for (int j = 0; j < datosNetos(datos) - 1 - i; j++) {
    
                    if (datos[j+1][columna].compareToIgnoreCase(datos[j][columna]) < 0) {
                        String[] filaTemp = datos[j];
                        datos[j] = datos[j+1];
                        datos[j+1] = filaTemp;
                        if (tieneEstados == true) {
                            boolean estadoAux = estados[j];
                            estados[j] = estados[j+1];
                            estados[j+1] = estadoAux;  
                        }
                    }
                }
            }
        }

        return datos;
    }

    public String[][] agregarPrestamo(String[][] arreglo){
        int posicion = 0;
        if (arreglo[0][0] != null) {
            while ((posicion < arreglo.length) && (arreglo[posicion][0] != null)) {
                posicion ++;
            }
        }
        if (posicion < arreglo.length) {
            System.out.println("\n--------CLIENTES DISPONIBLES PARA PRESTAMO---------");
            mostrarClientes(clientes, false, true);
            int clientePesta = pedirID(clientes, true, "el ID del cliente que prestara");
            System.out.println();
            System.out.println("--------PELICULAS DISPONIBLES PARA PRESTAMO---------");
            mostrarPelis(peliculas, false, true, false);
            System.out.println();
            int peliPrestada = pedirID(peliculas, true, "el ID de la pelicula que prestara");
            System.out.println();
            int diasPrestado = pedirNumero("los dias que prestara la pelicula");
            System.out.println("\nEl cliente: " + obtenerDatoArreglo(clientes, 1, clientePesta) + " , prestara la pelicula: "
                                + obtenerDatoArreglo(peliculas, 1, peliPrestada) + " por " + diasPrestado + " dias.");
            System.out.println("\nEsta seguro de que quiere realizar el prestamo? \n1. Si \n2. No");
            int opcion = pedirNumero("una opcion");
            if (opcion == 1) {

                arreglo[posicion][0] = Integer.toString(clientePesta);
                arreglo[posicion][1] = Integer.toString(peliPrestada);
                arreglo[posicion][2] = Integer.toString(diasPrestado);
                

                estadoClientes[posicionDatoID(clientePesta, clientes)] = false;
                estadoPelis[posicionDatoID(peliPrestada, peliculas)] = false;
                int contador = Integer.parseInt(peliculas[posicionDatoID(peliPrestada, peliculas)][4]) + 1;
                peliculas[posicionDatoID(peliPrestada, peliculas)][4] = Integer.toString(contador);
                System.out.println("Se ha prestado la pelicula");
            }else{
                System.out.println("\nVen a prestar una peli cuando quieras");
            }
            
        } else {
            System.out.println("Lo sentimos, no se pueden realizar mas prestamos :("); 
        }
        return arreglo;
    }

    public String [][] quitarPrestamo (String[][] arreglo, int idPeli, int idCliente){
        int indice = posicionDatoID(idCliente, arreglo);
        if((indice >= 0) && (indice < arreglo.length)){
            arreglo[indice][0] = null;
            arreglo[indice][1] = null;
            arreglo[indice][2] = null;
            eliminarVacios(arreglo);
            estadoClientes[posicionDatoID(idCliente, clientes)] = true;
            estadoPelis[posicionDatoID(idPeli, peliculas)] = true;
        }
        return arreglo;
    }

    public String [][] eliminarVacios (String [][] arreglo){
        for (int i = 0; i < arreglo.length; i++) {
            if (arreglo[i][0] != null) {
                int indice = i-1;
                if (indice >= 0) {
                    String[] filaAux = arreglo[i];
                    arreglo[i] = arreglo[indice];
                    arreglo[indice] = filaAux;
                    //borrarFila(arreglo, i-1);
                }
            }
        }
        return arreglo;
    }

    public void imprimirDeudores(){
        if (prestamoPeliculas[0][0] != null) {
            int contador = datosNetos(prestamoPeliculas);
            for (int i = 0; i < contador; i++) {
                int idPeliDeudora = Integer.parseInt(prestamoPeliculas[i][1]);
                int idClienteDeudor = Integer.parseInt(prestamoPeliculas[i][0]);
                System.out.println("ID Pelicula: " + prestamoPeliculas[i][1]);
                System.out.println("Pelicula: " + obtenerDatoArreglo(peliculas, 1, idPeliDeudora));
                System.out.println("Prestada por: " + obtenerDatoArreglo(clientes, 1, idClienteDeudor));
                System.out.println("ID prestador: " + prestamoPeliculas[i][0]);
                System.out.println("Dias que solicito la pelicula: " + prestamoPeliculas[i][2] + "\n");
            }
        } else {
            System.out.println("\nNinguna pelicula ha sido prestada");
        }

    }

    public String obtenerDatoArreglo(String[][] datos, int columna, int id){
        boolean encontrado = buscarID(id, datos, 0);
        int indice = posicionDatoID(id, datos);
        String datoObtenido = "";
        if (encontrado == true) {
            datoObtenido = datos[indice][columna];
        } else {
            
        }
        return datoObtenido;
    }

    public String[][] agregarPeli(String[][] arreglo){
        int posicion = 0;
        
        while ((posicion < arreglo.length) && (arreglo[posicion][0] != null)) {
            posicion ++;
        }
        if (posicion < arreglo.length) {
            arreglo[posicion][0] = Integer.toString(pedirID(arreglo, false, "el ID de la pelicula"));
            arreglo[posicion][1] = pedirString("el nombre de la pelicula");
            arreglo[posicion][2] = Integer.toString(pedirNumero("el anio de la pelicula"));
            String categoriaPeli = pedirString("una categoria para la pelicula").toLowerCase();
            arreglo[posicion][3] = categoriaPeli;
            arreglo[posicion][4] = "0";
            estadoPelis[posicion] = true;

            categorias = agregarCategoria(categorias, categoriaPeli);
            
            
        } else {
            System.out.println("Lo sentimos, no se pueden ingresar mas peliculas :("); 
        }
        return arreglo;
    }

    public void mostrarPelis(String [][] arreglo, boolean todas, boolean estado, boolean contador){
        int posicion = 0;
        if (arreglo[0][0] == null) {
            System.out.println("No hay nada para mostrar por ahora");
        } else {
            while ((posicion < arreglo.length) && (arreglo[posicion][0] != null)) {
                posicion ++;
            }
            
            for (int i = 0; i < posicion; i++) {
                if (todas == true) {
                imprimirPeli(arreglo, i, contador);
                    
                } else{
                    if (estadoPelis[i] == estado) {
                        imprimirPeli(arreglo, i, contador);
                    }
                }
            }
        }
    }

    public void imprimirPeli(String[][] arreglo, int fila, boolean contador){
        String mensaje = "\nID: " + arreglo[fila][0] + "\nNombre: " + arreglo[fila][1];
        String extra;
        if (contador == false) {
            extra = "\nAnio: " + arreglo[fila][2] + "\nCategoria: " + arreglo[fila][3] + "\nEstado: " + imprimirEstado(estadoPelis[fila]);
        }else{
            extra = "\nPrestada: " + arreglo[fila][4] + " veces";
        }
        System.out.println(mensaje + extra);
    }

    public String imprimirEstado(boolean estado){
        String estadoEscrito;
        if (estado == true) {
            estadoEscrito = "Disponible";
        } else {
            estadoEscrito = "No disponible";
        }
        return estadoEscrito;
    }

    public int datosNetos(String[][] datos){
        int contador = 0;
        while ((contador < datos.length) && (datos[contador][0] != null)) {
            contador++;
        }
        return contador;
    }

    public int pedirID(String [][] datos, boolean existe, String mensaje){
        int idPedido;
        boolean valido = existe;

        if (datos[0][0] != null) {
            do {
                idPedido = pedirNumero(mensaje);
                valido = buscarID(idPedido, datos, 0);
                if (valido != existe) {
                    if (existe == false) {
                        System.out.println("\nEse ID ya se encuentra en uso");
                    } else {
                        System.out.println("\nEse ID no se encuentra registrado");
                    }
                }
                
            } while (valido != existe);
        }else{
            idPedido = pedirNumero(mensaje);
        }

        return idPedido;
    }

    public boolean buscarID(int id, String[][] datos, int columna){
        boolean correcto = false;
        if (datos[0][0] != null) {
            for (int i = 0; i < datosNetos(datos); i++) {
                if(Integer.valueOf(datos[i][columna]) == id){
                    correcto = true;
                }
            }
        }
        return correcto;
    }

    public int posicionDatoID(int id, String [][] datos){
        int contador = 0;
        if (datos[0][0] != null) {
            while((contador < datosNetos(datos)) && (Integer.parseInt(datos[contador][0]) != id)){
                contador++;
            }
        }
        return contador;
    }

    public String[][] agregarCliente(String[][] arreglo){
        int posicion = 0;
        
        while ((posicion < arreglo.length) && (arreglo[posicion][0] != null)) {
            posicion ++;
        }
        if (posicion < arreglo.length) {
            arreglo[posicion][0] = Integer.toString(pedirID(arreglo, false, "el ID del cliente"));
            arreglo[posicion][1] = pedirString("el nombre del cliente");
            arreglo[posicion][2] = Integer.toString(pedirNumero("el telefono del cliente"));
            estadoClientes[posicion] = true;

        } else {
            System.out.println("Lo sentimos, no se pueden ingresar mas clientes :(");
            
        }
        return arreglo;
    }

    public void mostrarClientes(String [][] arreglo, boolean todos, boolean estado){
        int posicion = 0;
        if (arreglo[0][0] == null) {
            System.out.println("No hay nada para mostrar por ahora");
        } else {
            while ((posicion < arreglo.length) && (arreglo[posicion][0] != null)) {
                posicion ++;
            }
            for (int i = 0; i < posicion; i++) {
                if (todos == true) {
                    imprimirCliente(arreglo, i);
                } else {
                    if (estadoClientes[i] == estado) {
                        imprimirCliente(arreglo, i);
                    } 
                }
            }
        }
    }

    public void imprimirCliente(String[][] arreglo, int fila){
        System.out.println("\nID: " + arreglo[fila][0]);
        System.out.println("Nombre: " + arreglo[fila][1]);
        System.out.println("Telefono: " + arreglo[fila][2]);
    }

    public int pedirNumero(String solicitud) {
        int numero = 0;
        
        do {
            try {
                System.out.print("Ingrese " + " " + solicitud + ": ");
                int ingresado = scanner.nextInt();
                if (ingresado < 0) {
                    System.out.println("\nNo puede ingresar un valor negativo");
                }
                numero = ingresado;

            } catch (InputMismatchException e) {
                System.out.println("ERROR. Debes de ingresar un numero");
                scanner.nextLine();
                numero = -1;
            }
        } while (numero < 0);
        return numero;
    }

    public String pedirString(String solicitud) {
        System.out.print("Ingrese " + " " + solicitud + ": ");
        String palabra = scanner.nextLine();
        palabra = scanner.nextLine().toLowerCase().trim();
        return palabra;
    }

    public void menu() {
        boolean salir = false;
        int opcion = 0;
        while (!salir) {
            // mostrarle al usuario las opciones
            System.out.println("\n~~~~[MEMORABILIA-ALQUILER DE PELICULAS]~~~");
            System.out.println("---------------MENU PRINCIPAL-------------");
            System.out.println("    \n1. Prestar pelicula" + "    \n2. Devolver pelicula" + "    \n3. Mostrar peliculas"
                    + "    \n4. Ingresar peliculas" + "    \n5. Ordenar peliculas" + "    \n6. Ingresar nuevos clientes"
                    + "    \n7. Mostrar clientes" + "    \n8. Reportes" + "    \n9. Salir\n");
            try {
                System.out.print("Ingrese una opcion: ");
                opcion = scanner.nextInt();
                switch (opcion) {
                    case 1:
                        System.out.println("\n----------PRESTAMO DE PELICULAS-----------\n");
                        boolean disponiblePelis = verificarDisponibilidad(estadoPelis, true);
                        boolean disponibleClientes = verificarDisponibilidad(estadoClientes, true);
                        if ((disponiblePelis == true) && (disponibleClientes == true)) {
                            prestamoPeliculas = agregarPrestamo(prestamoPeliculas);
                        }else{
                            System.out.println("No hay peliculas o clientes disponibles");
                        }
                        
                        break;
                    case 2:
                        System.out.println("\n------------DEVOLUCION DE PELICULAS-------------\n");
                        boolean pelisPrestadas = verificarDisponibilidad(estadoPelis, false);
                        boolean clientesPrestados = verificarDisponibilidad(estadoClientes, false);

                        if ((pelisPrestadas == true) && (clientesPrestados == true) && (prestamoPeliculas[0][0] != null)) {
                            System.out.println("------------LISTA DE PELICULAS PRESTADAS----------\n");
                            imprimirDeudores();
                            System.out.println();
                            int clienteIDDevolver = pedirID(clientes, true, "el ID del cliente que devuelve");
                            String idPeli = obtenerDatoArreglo(prestamoPeliculas, 1, clienteIDDevolver).trim();
                            int peliIDDevolver = Integer.parseInt(idPeli);
                            prestamoPeliculas = quitarPrestamo(prestamoPeliculas, peliIDDevolver, clienteIDDevolver);
                            System.out.println("\nSe ha devuelto la pelicula");
                        }else{
                            System.out.println("\nNo hay peliculas prestadas");
                        }
                        break;
                    case 3:
                        System.out.println("\n-----LISTA DE PELICULAS REGISTRADAS-------\n");
                        mostrarPelis(peliculas, true, true, false);
                        
                        break;
                    case 4:
                        System.out.println("\n----------AGREGANDO UNA PELICULA-----------\n");
                        peliculas = agregarPeli(peliculas);
                        
                        break;
                    case 5:
                        System.out.println("\n-----LISTA DE PELICULAS ORDENADAS (A-Z)-----");
                        String [][] ordenados = ordenarAlfabetico(peliculas, 1, estadoPelis, true);
                        mostrarPelis(ordenados, true, true, false);
                        break;
                    case 6:
                        System.out.println("\n----------AGREGANDO UN CLIENTE-----------\n");
                        agregarCliente(clientes);
                        break;
                    case 7:
                        System.out.println("\n-------LISTA DE CLIENTES REGISTRADOS-------");
                        mostrarClientes(clientes, true, true);
                        break;
                    case 8:
                        menuReportes();
                        break;
                    case 9:
                        salir = true;
                        System.out.println("Vuelva pronto :)\n");
                        break;
                    default:
                        System.out.println("Ups. Esa opción no existe.\n");
                        break;
                }

            } catch (InputMismatchException e) {
                System.out.println("\nERROR. Debes de ingresar un numero\n");
                scanner.nextLine();
            }
        }
    }

    public void menuReportes(){
        boolean salir = false;
        int opcion = 0;
        while (!salir) {
            System.out.println("\n------------REPORTES-------------\n");
            System.out.println("1. Cantidad de peliculas por categoria \n2. Mostrar peliculas por categoria \n3. Reporte de prestamos de peliculas" 
                                + "\n4. Peliculas mas y menos prestadas \n5. Regresar\n");
            opcion = pedirNumero("una opcion");
            switch (opcion) {
                case 1:
                    System.out.println("\n----------CANTIDAD DE PELICULAS POR CATEGORIAS---------\n");
                    if (categorias[0][0] != null) {
                        mostrarCategorias(categorias, false);
                        System.out.println("\nTOTAL DE PELICULAS REGISTRADAS: " + datosNetos(peliculas));
                    }else{
                        System.out.println("No hay categorias registradas\n");
                    }
                    break;
                case 2:
                    menuCategorias();
                    break;
                case 3:
                    System.out.println("\n---------REPORTE DE PRESTAMOS DE PELICULAS------------\n");
                    mostrarPelis(peliculas, true, true, true);
                    break;
                case 4:
                    if (peliculas[0][0] != null) {
                        System.out.println("\n--------------PELICULA MAS PRESTADA------------");
                        peliculas = ordenarNumerico(peliculas, 4, estadoPelis, true);
                        imprimirPeli(peliculas, (datosNetos(peliculas)-1), true);
                        System.out.println("\n-------------PELICULA MENOS PRESTADA-----------");
                        imprimirPeli(peliculas, 0, true);
                    } else {
                        System.out.println("\nNo hay peliculas registradas");
                    }
                    break;
                case 5:
                    salir = true;
                    break;
            
                default:
                    System.out.println("Ups, esa opcion no existe");
                    break;
            }
        }
    }
    
    public void menuCategorias(){
        System.out.println("\n------------CATEGORIAS REGISTRADAS------------\n");
        boolean salir = false;
        if (categorias[0][0] != null) {
            while(!salir){
                mostrarCategorias(categorias, true);
                System.out.println((datosNetos(categorias)+1) + ". Salir");
                int opcion = pedirNumero("un numero de la lista mostrada");
                if (opcion == (datosNetos(categorias)+1)) {
                    salir = true;
                }else{
                    if (opcion <= datosNetos(categorias)) {
                        mostrarPorCategoria(categorias[(opcion-1)][0]);
                        salir = true;
                    }
                }
            }
        }
    }

    public void mostrarPorCategoria(String categoriaAB){
        System.out.println("\n-------PELICULAS DE " + categoriaAB.toUpperCase() + "-------\n");
        for (int i = 0; i < datosNetos(peliculas); i++) {
            if (categoriaAB.equalsIgnoreCase(peliculas[i][3])) {
                imprimirPeli(peliculas, i, false);
            }
            
        }

    }

    public String[][] agregarCategoria(String[][] arreglo, String categoria){
        int posicion = 0;
        
        while ((posicion < arreglo.length) && (arreglo[posicion][0] != null)) {
            posicion ++;
        }
        if (posicion < arreglo.length) {
            if (posicion > 0) {
                if (categoria.equalsIgnoreCase(arreglo[posicion-1][0])) {
                    int contador = Integer.parseInt(arreglo[posicion-1][1]) + 1;
                    arreglo[posicion-1][1] = Integer.toString(contador);
                    
                } else {
                    arreglo[posicion][0] = categoria;
                    arreglo[posicion][1] = "1";
                }
            }else{
                arreglo[posicion][0] = categoria;
                arreglo[posicion][1] = "1";
            }
        } else {
            System.out.println("Lo sentimos, no se pueden ingresar mas categorias :(");
            
        }
        return arreglo;
    }

    public void mostrarCategorias(String [][] arreglo, boolean enLista){
        int posicion = 0;
        if (arreglo[0][0] == null) {
            System.out.println("No hay nada para mostrar por ahora");
        } else {
            arreglo = ordenarAlfabetico(arreglo, 0, estadoPelis, false);
            while ((posicion < arreglo.length) && (arreglo[posicion][0] != null)) {
                posicion ++;
            }
            for (int i = 0; i < posicion; i++) {
                if (enLista==true) {
                    System.out.println((i+1) + ". " + arreglo[i][0]);
                } else {
                    System.out.println("\nCategoria: " + arreglo[i][0]);
                    System.out.println("Cantidad de peliculas: " + arreglo[i][1]);
                }
            }
        }
    }
}