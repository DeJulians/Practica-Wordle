/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PracticaWordle;

/**
 *
 * @author USER
 */
public class Partida {
    // Atributes
    private int numPalabras; // Numero de palabras que se juega
    private Jugador jugador1, jugador2, ganador;
    private int ganadosJug1, ganadosJug2;
    private PartidaPalabra[][] listaPPalabras;
    private final static int MAXPALABRAS = 10;
    private final static int NUMJUGADORES = 2;
    private int puntosJug1, puntosJug2;
    private int cont1, cont2;

    // Constructores
    public Partida(Jugador j1, Jugador j2, int n) { // Modo multijugador
        this(j1, n);
        if (j2 != null)
            jugador2 = j2;
    }

    public Partida(Jugador j, int n) { // Modo entrenamiento
        listaPPalabras = new PartidaPalabra[NUMJUGADORES][MAXPALABRAS];
        numPalabras = n;
        if (j != null)
            jugador1 = j;
    }

    // Getters
    public Jugador getJugador1() {
        return jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public Jugador getGanador() {
        return ganador;
    }

    public int getNumPalabras() {
        return numPalabras;
    }

    public int getGanadosJug1() {
        return ganadosJug1;
    }

    public int getGanadosJug2() {
        return ganadosJug2;
    }

    public PartidaPalabra[][] getListaPPalabras() {
        return listaPPalabras;
    }

    public int getPuntosJ1(){
        return puntosJug1;
    }

    public int getPuntosJ2(){
        return puntosJug2;
    }

    public int getCont1(){
        return cont1;
    }

    public int getCont2(){
        return cont2;
    }

    public void setNumPalabras(int n) {
        numPalabras = n;
    }

    public void setGanadosJug1() {
        ganadosJug1++;
    }

    public void setGanadosJug2() {
        ganadosJug2++;
    }

    public void setPuntosJ1(int n){
        puntosJug1 = n;
    }

    public void setPuntosJ2(int n){
        puntosJug2 = n;
    }

    public void setCont1(int n){
        cont1 = n;
    }

    public void setCont2(int n){
        cont2 = n;
    }

    public void setGanador(Jugador g) {
        ganador = g;
    }

    // Methods
    public void jugarPartida(Jugador j, Palabra p) {
        PartidaPalabra nueva = null;
        if (j.equals(getJugador1()) && (getCont1() < numPalabras-1)){ 
            nueva = new PartidaPalabra(j, p); //Se crea para jugador1
            if (!partidaEncontrada(0, nueva))            
                listaPPalabras[0][cont1++] = nueva;
        }
        else if (j.equals(getJugador2()) && (getCont2() < numPalabras-1)){
            nueva = new PartidaPalabra(j, p); //Se crea para jugador2
            if (!partidaEncontrada(1, nueva))            
                listaPPalabras[1][cont2++] = nueva;
            
        } else if (j.equals(null)) {
            System.out.println("Seleccione un jugador registrado en esta partida: \n"  
            + "1. " +getJugador1().toString() + "\n2. " + getJugador2().toString());
        }

        nueva.resolver();
    }    

    private void actualizarDatos(){
        //Acualizar datos de esta clase        
        int i = 0; //Actualizar para jugador1
        for (int j = 0; j <= getNumPalabras()-1; j++) {
            if (listaPPalabras[i][j].isGanada()) setGanadosJug1();
            setPuntosJ1(listaPPalabras[i][j].getPuntos() + getPuntosJ1());
        }
        i = 1; //Actualizar para jugador2
        for (int j = 0; j <= getNumPalabras()-1; j++) {
            if (listaPPalabras[i][j].isGanada()) setGanadosJug2();
            setPuntosJ2(listaPPalabras[i][j].getPuntos() + getPuntosJ2());
        }
        elegirGanador(); //Se elige el ganador de esta partida.
        actualizarJugadores(getJugador1(), getJugador2()); //Luego actualiza los datos en la clase Jugador
        
    }

    private void actualizarJugadores(Jugador j1, Jugador j2) {
        //Actualizar los atributos de la clase Jugador.

        if (getGanador().equals(j1)) {
            getJugador1().incrementarGanadas();
            getJugador2().incrementarPerdidas();            
        } else if (getGanador().equals(j2)) {
            getJugador2().incrementarGanadas();
            getJugador1().incrementarPerdidas();
        } else {
            getJugador1().incrementarEmpatadas();
            getJugador2().incrementarEmpatadas();
        }
        getJugador1().setPuntos(getJugador1().getPuntos() + getPuntosJ1());
        getJugador2().setPuntos(getJugador2().getPuntos() + getPuntosJ2());     
    }

    private boolean partidaEncontrada(int n, PartidaPalabra nueva) {
        boolean encontrado = false;
        int i=0;
        while(i < getCont1()-1 && !encontrado){
            encontrado = nueva.equals(listaPPalabras[n][i]);
            if (!encontrado)
            i++;
        }

        return encontrado;        
    }

    private void jugarPartida(Jugador j, PartidaPalabra partida) {
        partida.resolver();            
    }

    private void elegirGanador() {
        if (getJugador1().getGanadas() > getJugador2().getGanadas())        
            setGanador(getJugador1());
        else if (getJugador1().getGanadas() < getJugador2().getGanadas())
            setGanador(getJugador2());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Partida p = (Partida) o;
        return ((getJugador1().equals(p.getJugador1())) && (getJugador2().equals(p.getJugador2())) &&
                (getNumPalabras() == p.getNumPalabras()) && (getGanadosJug1() == p.getGanadosJug1()) &&
                (getGanadosJug2() == p.getGanadosJug2()) && (getListaPPalabras().equals(p.getListaPPalabras())));
    }

}