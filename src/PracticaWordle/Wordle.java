/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PracticaWordle;

import java.util.ArrayList;
import java.util.Scanner;

import javax.rmi.ssl.SslRMIClientSocketFactory;

import PracticaWordle.Exepciones.JugadorExcepcion;
import java.io.Serializable;

/**
 *
 * @author USER
 */
public class Wordle implements Serializable {

    // Atributes
    private ArrayList<Partida> listaPartidas;
    private ArrayList<Jugador> listaJugadores;
    private static final int PALABRAS_POR_DEFECTO = 1;
    private static final String USUARIO = "admin";
    private static final String PASSWD = "12345";

    // Constructors
    public Wordle() {
        listaPartidas = new ArrayList<Partida>();
        listaJugadores = new ArrayList<Jugador>();
    }

    // Getters
    public ArrayList<Partida> getListaPartidas() {
        return listaPartidas;
    }

    public ArrayList<Jugador> getListaJugadores() {
        return listaJugadores;
    }

    // Methods
    public void registrarJugador(Jugador j) throws JugadorExcepcion {
        if (!existeJugador(j.getNombre())) {
            listaJugadores.add(j);
            System.out.println(j.getNombre() + " has sido registrado con éxito!");
        } else {
            throw new JugadorExcepcion("El jugador ya está registrado actualmente.");
        }
    }

    public void eliminarJugador(Jugador j) throws JugadorExcepcion {
        if (existeJugador(j.getNombre())) {
            listaJugadores.remove(j);
        }
    }

    private String[] meterPalabras(Partida p) {
        int numPalabras = p.getNumPalabras() * 2;
        String[] palabrasPartida = new String[numPalabras];
        OperacionesFicheros of = new OperacionesFicheros();
        String[] palabrasObtenidas = of.obtenerPalabras();
        for (int i = 0; i < numPalabras; i++) {
            int aleatorio = (int) (Math.random() * of.getNUM_PALABRAS());
            palabrasPartida[i] = palabrasObtenidas[aleatorio];
        }
        return palabrasPartida;
    }

    public void iniciarPartida(Jugador j1, Jugador j2, int numPalabras, Scanner s) throws JugadorExcepcion {
        // Verifico si los jugadores con los que se quiere iniciar la partida están
        // registrados
        // Jugador jug1 = registrarJugador(j1);
        Partida p;
        // Creo la partida con los jugadores y el numero de palabras que se me indique
        if (j2 != null) {
            // Jugador jug2 = registrarJugador(j2);
            p = new Partida(j1, j2, numPalabras);
        } else
            p = new Partida(j1, numPalabras);

        // Se obtienen las palabras de manera aleatoria accediendo a un fichero con
        // miles de palabras
        String[] palabras = meterPalabras(p);
        // Se crean todas las partidasPalabras y finalmente se juega la partida
        p.crearPartidasPalabra(palabras);
        p.jugarPartida(s);
        // Se añade la partida a la lista de partidas
        anadirPartida(p);
        System.out.println("Fin de partida");
    }

    public void anadirPartida(Partida p) {
        if (!listaPartidas.contains(p)) {
            listaPartidas.add(p);
            System.out.println("La partida se ha guardado correctamente.");
        } else
            System.err.println("Error: Esta partida ya se ha guardado anteriormente.");
    }

    private boolean existeJugador(String nombre) {
        Jugador jugador = new Jugador(nombre);
        return listaJugadores.contains(jugador);
    }

    private Jugador encontrarJugador(Jugador j){
        if (existeJugador(j.getNombre())){
            int i = 0;
            Jugador aux = new Jugador("");
            boolean encontrado = false;
            while(i < getListaJugadores().size() && !encontrado){
                aux = getListaJugadores().get(i);
                encontrado = j.equals(aux);
                i++;
            }
            return aux;
        }
        else{
            return j;
        }
    }

    public void rankingJugadores(ArrayList<Jugador> list) {
        ArrayList<Jugador> aux = list;
        aux.sort(new ComparadorJugador());
        OperacionesFicheros f = new OperacionesFicheros();
        f.crearRanking(aux);
    }

    public void rankingAlfabetico(ArrayList<Jugador> list) {
        ArrayList<Jugador> aux = list;
        aux.sort(new ComparadorAlfabetico());
        OperacionesFicheros f = new OperacionesFicheros();
        f.crearRankingAlfabetico(aux);
    }

    /*
     * public String partidas(ArrayList<Partida> part) {
     * return imprimirListaP(part);
     * }
     * 
     * 
     * private String imprimirListaP(ArrayList<Partida> list) {
     * String aux = "";
     * for (int i = 0; i <= (list.size() - 1); i++) {
     * aux += list.get(i).toString();
     * }
     * return aux;
     * }
     */

    public void mostrarEstadisticas(Scanner s) {
        System.out.println("Escriba el nombre del jugador: ");
        String res = s.next();
        System.out.println("\n\n");
        if (existeJugador(res)) {
            Jugador j = new Jugador(res);
            Jugador j2 = encontrarJugador(j);
            System.out.println(j2.toString());
        } else {
            System.out.println("El jugador no existe actualmente.\n");
        }
    }

    public void mostrarMenu(Scanner s) throws JugadorExcepcion {
        String eleccion;
        do {
            leyendaInicio();
            eleccion = s.next();
            menu(s, eleccion);
        } while (!eleccion.equals("4"));

    }

    private void menu(Scanner s, String opcion) throws JugadorExcepcion {
        int numPalabras = PALABRAS_POR_DEFECTO;
        OperacionesFicheros of = new OperacionesFicheros();
        switch (opcion) {
            case "1":
                MostrarMenuPartida();
                opcion = s.next();
                if (opcion.equals("1")) {
                    System.out.print("Jugador: ");
                    Jugador j1 = new Jugador(s.next());
                    iniciarPartida(j1, null, numPalabras, s);
                }

                else if (opcion.equals("2")) {
                    System.out.print("Jugador 1: ");
                    Jugador j1 = new Jugador(s.next());
                    System.out.print("\nJugador 2: ");
                    Jugador j2 = new Jugador(s.next());
                    iniciarPartida(j1, j2, numPalabras, s);
                }

                else if (opcion.equals("3"))
                    System.out.println("Volviendo al menú principal...");

                break;

            case "2":
                mostrarMenuEstadisticas();
                opcion = s.next();
                if (opcion.equals("1")) { //Mostrar estadísticas del jugador
                    mostrarEstadisticas(s);
                }

                else if (opcion.equals("2")) {//Ver partidas vs otro jugador

                }

                else if (opcion.equals("3")) {//ver ranking
                    do {
                       System.out.println("Elija el orden del ranking: \n\n"
                        + "1. Mayor a menor puntuación.\n"
                        + "2. Orden alfabético.\n"
                        );
                        opcion = s.next();
                        if (opcion.equals("1")) {
                            rankingJugadores(listaJugadores);
                        }

                        else if (opcion.equals("2")) {
                            rankingAlfabetico(listaJugadores);
                        }    
                    } while (!(opcion.equals("1")) && !(opcion.equals("2")));                                
                }

                else if (opcion.equals("4"))
                    System.out.println("Volviendo al menú principal...");
                break;

            case "3":
                if (comprobarAdministrador(s)) {
                    ejecutarOpciones(s, opcion, numPalabras);
                }
                break;

            case "4":
                System.out.println("Saliendo...");
                of.guardarPartidas(this);
                of.guardarJugadores(this);
                break;
        }
    }

    private void leyendaInicio() {
        System.out.println("Hola! Bienvenido a Wordle.\n"
                + "Wordle es un juego de palabras en el que deberás adivinar la palabra oculta.\n\n"
                + "Selecciona una opción:\n\n"
                + "1. Jugar.\n"
                + "2. Consultar Estadísticas.\n"
                + "3. Configurar Opciones.\n"
                + "4. Salir.\n");
    }

    private void MostrarMenuPartida() {
        System.out.println("Seleccione modo de juego:\n\n"
                + "1. Modo entrenamiento.\n"
                + "2. 1 contra 1.\n"
                + "3. Salir.\n");
    }

    private void mostrarMenuConfigurarOpciones() {
        System.out.println("Seleccione una opción:\n\n"
                + "1. Modificar número de palabras en las partidas.\n"
                + "2. Registrar jugador.\n"
                + "3. Eliminar jugador.\n"
                + "4. Salir.\n");
    }

    private void mostrarMenuEstadisticas() {
        System.out.println("Seleccione una opción:\n\n"
                + "1. Consultar mis estadísticas.\n"
                + "2. Ver partidas contra un jugador.\n"                
                + "3. Ver ranking.\n"                
                + "4. Salir\n");
    }

    private boolean comprobarAdministrador(Scanner s) {
        System.out.println("Usuario: ");
        String aux = s.next();
        System.out.println("\nContrasena: ");
        String aux2 = s.next();

        boolean b1 = aux.equals(USUARIO);
        boolean b2 = aux2.equals(PASSWD);

        if (b1 && b2)
            return true;
        else {
            System.out.println("\nUsuario o contrasena incorrectos.\n");
            return false;
        }
    }

    private void ejecutarOpciones (Scanner s, String opcion, int numPalabras) throws JugadorExcepcion {
        mostrarMenuConfigurarOpciones();
        opcion = s.next();
                    if (opcion.equals("1")) {
                        do {
                            System.out.print("Inserte un numero entre 1 y 10.\n"
                                    + "Número de palabras por partida: ");
                            numPalabras = s.nextInt();                            
                            System.out.println();
                        } while ((numPalabras < 1) && (numPalabras > 10));

                    } else if (opcion.equals("2")) {
                        System.out.println("Introduzca el nombre: ");
                        registrarJugador(new Jugador(s.next()));
                    } else if (opcion.equals("3")) {
                        String nombreJugador = null;
                        do {
                            System.out.println("\nPulse 0 si desea salir. 1 para continuar.");
                            opcion = s.next();
                            if (!opcion.equals("0")) {
                                System.out.println("Introduzca el nombre: ");
                                nombreJugador = s.next();
                                if (existeJugador(nombreJugador)) {
                                    System.out.println("Seguro que desea eliminar a " + nombreJugador);
                                    System.out.println("1. Sí.\n"
                                            + "2. No.\n");
                                    opcion = s.next();
                                    if (opcion.equals("1")) {
                                        eliminarJugador(new Jugador(nombreJugador));
                                        System.out.println("El jugador \'" + nombreJugador + "\' ha sido eliminado.");
                                    }

                                    System.out.println("Desea eliminar a otro jugador?\n\n"
                                            + "1. Sí.\n"
                                            + "2. No.\n");

                                    opcion = s.next();
                                } else {
                                    System.out.println("El jugador \'" + nombreJugador + "\' no existe.\n"
                                            + "Inserte un nombre válido.\n\n");
                                }
                            }
                        } while ((opcion.equals("1")));
                    } else if (opcion.equals("4"))
                        System.out.println("Volviendo al menu principal...");
    }

}
