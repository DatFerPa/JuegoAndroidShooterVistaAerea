package com.shooteraereo.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.shooteraereo.R;
import com.shooteraereo.gestores.CargadorGraficos;
import com.shooteraereo.graficos.Sprite;

import java.util.HashMap;

/**
 * Created by Fernando on 18/11/2017.
 */



public class Jugador  extends Modelo{

    public static final String PARADO_DERECHA = "Parado_derecha";
    public static final String PARADO_IZQUIERDA = "Parado_izquierda";
    public static final String CAMINANDO_DERECHA = "Caminando_derecha";
    public static final String CAMINANDO_IZQUIERDA = "Caminando_izquierda";
    public static final String DISPARANDO_DERECHA = "Disparando_derecha";
    public static final String DISPARANDO_IZQUIERDA = "Disparando_izquierda";

    public static final int DISPARO_COLDOWN_MAXIMO = 5;
    public static final int DISPARO_COLDOWN_BASE = 20;


    int tiempoActual;
    int tiempoParaDisparo;

    public static final int DAÑO_MAXIMO = 30;
    public static final int DAÑO_BASE = 5;
    int dañoAtaque;

    public static float VELOCIDAD = 5;
    double velocidadX;
    double velocidadY;

    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite> ();

    double xInicial;
    double yInicial;

    boolean disparando= false;

    private boolean bombaUsada = false;

    public static final int VIDA_MAX= 100;
    public int vida;
    public boolean golpeado = false;

    public Jugador(Context context, double xInicial, double yInicial) {
        super(context, 0, 0, 40, 40);

        // guardamos la posición inicial porque más tarde vamos a reiniciarlo
        this.xInicial = xInicial;
        this.yInicial = yInicial - altura/2;

        this.x =  this.xInicial;
        this.y =  this.yInicial;

        tiempoActual = DISPARO_COLDOWN_BASE;
        this.vida = VIDA_MAX;
        tiempoParaDisparo = DISPARO_COLDOWN_BASE;
        this.dañoAtaque = DAÑO_BASE;

        inicializar();
    }

    public void inicializar (){
        Sprite paradoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.jugador_andando_derecha),
                ancho, altura,
                13, 14, true);
        sprites.put(PARADO_DERECHA, paradoDerecha);

        Sprite paradoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.jugador_andando_derecha),
                ancho, altura,
                13, 14, true);
        sprites.put(PARADO_IZQUIERDA, paradoIzquierda);

        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.jugador_andando_derecha),
                ancho, altura,
                13, 14, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.jugador_andando_derecha),
                ancho, altura,
                13, 14, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);
        Sprite disparandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.jugador_andando_derecha),
                ancho, altura,
                13, 14, false);
        sprites.put(DISPARANDO_DERECHA, disparandoDerecha);

        Sprite disparandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.jugador_andando_derecha),
                ancho, altura,
                13, 14, false);
        sprites.put(DISPARANDO_IZQUIERDA, disparandoIzquierda);

        // animación actual
        sprite = paradoDerecha;
    }
    public void actualizar (long tiempo) {

        boolean finSprite = sprite.actualizar(tiempo);

        if(disparando && finSprite){
            disparando = false;
        }

        if(velocidadX > 0){
            sprite = sprites.get(CAMINANDO_DERECHA);
        }
        if(velocidadX < 0){
            sprite = sprites.get(PARADO_DERECHA);
        }

        if(disparando){

            if(velocidadX < 0){
                sprite = sprites.get(DISPARANDO_IZQUIERDA);
            }else if(velocidadX >= 0){
                sprite = sprites.get(DISPARANDO_DERECHA);
            }
        }
    }

    public boolean isbombaUsada(){
        return this.bombaUsada;
    }

    public void usarBomba(){
        this.bombaUsada = true;
    }

    public void restarVida(int valor){
        this.vida -= valor;
    }

    public void sumarVida(int valor){
        this.vida += valor;
        if(this.vida >VIDA_MAX){
            this.vida = VIDA_MAX;
        }
    }


    public void reducirCoolDown(int valor){
        this.tiempoParaDisparo -= valor;
        if(tiempoParaDisparo <DISPARO_COLDOWN_MAXIMO ){
            tiempoParaDisparo = DISPARO_COLDOWN_MAXIMO;
        }
    }

    public void aumentarDaño(int valor){
        dañoAtaque += valor;
        if(dañoAtaque > DAÑO_MAXIMO){
            dañoAtaque = DAÑO_MAXIMO;
        }
    }



    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX , (int) y - Nivel.scrollEjeY);
    }

    public void sumarTiempo(){
        ++tiempoParaDisparo;
    }


    public boolean posibleDisparo(){
        return (tiempoParaDisparo > tiempoActual)?true:false;
    }

    public void ponerTiempoaCero(){
        tiempoParaDisparo = 0;
    }

    public void procesarOrdenes(float posicionJugadorX, float posicionJugadorY,boolean disparar) {
        float vecX = (posicionJugadorX) / (float) ((Math.sqrt(Math.pow((double) posicionJugadorX, 2) + Math.pow((double) posicionJugadorY, 2))));
        float vecY = (posicionJugadorY) / (float) ((Math.sqrt(Math.pow((double) posicionJugadorX, 2) + Math.pow((double) posicionJugadorY, 2))));

        float posicionXCalculada = VELOCIDAD * vecX;
        float posicionYCalculada = VELOCIDAD * vecY;


        velocidadY = posicionYCalculada;
        velocidadX = posicionXCalculada * -1;

        //System.out.println("Velocidades : x: "+velocidadY+" - y: "+velocidadY);
        if (disparar && posibleDisparo()) {
            this.disparando = true;
            // preparar los sprites, no son bucles hay que reiniciarlos
            sprites.get(DISPARANDO_DERECHA).setFrameActual(0);
            sprites.get(DISPARANDO_IZQUIERDA).setFrameActual(0);

        }
    }





}
