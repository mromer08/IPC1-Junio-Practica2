package src;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Blockbuster {
    public static void main(String[] args) {
        Blockbuster programa = new Blockbuster();
    }
    final String [] PRONOMBRES = {"el", "la"};
    Scanner scanner = new Scanner(System.in);
    String [] nombreClientes, nombrePelis,categoriaPelis;
    int [][] peliculasPrestadas;
    int [] telefonoClientes, idClientes, idPelis, anioPelis;
    boolean [] clientePresta, peliDisponible;

    public Blockbuster(){
        menu();

    }
    public int pedirNumero(String solicitud, boolean femPronombre){
        int numero = 0;
        String pronombre = PRONOMBRES[1];
        if (!femPronombre) {
            pronombre = PRONOMBRES[0];
        }
        do {
            System.out.print("Ingrese " + pronombre + " " + solicitud + ": ");
            int ingresado = scanner.nextInt();
            if (ingresado > 0) {
                numero = ingresado;
                
            } else {
                System.out.println("No puede ingresar un valor negativo");  
            }
            
        } while (numero < 0);
        

        return numero;
    }
    /*
    public String pedirString(String solicitud, boolean femPronombre){
        String palabra, fem = "la";

    }
    */

    public void menu() {
        boolean salir = false;
        int opcion = 0;
        while(!salir){
            //mostrarle al usuario las opciones
            System.out.println("---------MENU PRINCIPAL---------");
            System.out.println("    \n1. Prestar pelicula" + "    \n2. Devolver pelicula" + "    \n3. Mostrar peliculas" 
                            + "    \n4. Ingresar peliculas" + "    \n5. Ordenar peliculas" + "    \n6. Ingresar nuevos clientes"
                            + "    \n7. Mostrar clientes" + "    \n8. Reportes" + "    \n9. Salir\n");
            try {
                System.out.print("Ingrese una opcion: ");
                opcion = scanner.nextInt();
                switch (opcion) {
                    case 1:
                        System.out.println("Quiere prestar una peli");
                        break;
                    case 2:
                        System.out.println("Quiere devolver una peli");
                        break;
                    case 3:
                        System.out.println("Le muestra las pelis");
                        break;
                    case 4:
                        System.out.println("quiere ingresar pelis");
                        break;
                    case 5:
                        System.out.println("Quiere ordenar las pelis");
                        break;
                    case 6:
                        System.out.println("Quire ingresar un nuevo cliente");
                        break;
                    case 7:
                        System.out.println("Le muestro clientes");
                        break;
                    case 8:
                        System.err.println("Le muestro reportes");
                        break;
                    case 9:
                        salir = true;
                        System.out.println("Vuelva pronto :)");
                    default:
                        System.out.println("Ups. Esa opción no existe.");
                        break;
                }
                
            } catch (InputMismatchException e) {
                System.out.println("\nERROR. Debes de ingresar un numero\n");
                scanner.next();
            }
        }    
    }

}