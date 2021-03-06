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

    public static float VELOCIDAD = 3;
    double ultimaVel;

    double velocidadX;
    double velocidadY;

    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite> ();

    double xInicial;
    double yInicial;

    private boolean bombaUsada = false;

    public static final int VIDA_MAX= 100;
    public int vida;
    private boolean animDisparar;


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
                CargadorGraficos.cargarDrawable(context, R.drawable.jugador_quieto_derecha),
                ancho, altura,
                4, 4, true);
        sprites.put(PARADO_DERECHA, paradoDerecha);

        Sprite paradoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.jugador_quieto_izquierda),
                ancho, altura,
                4, 4, true);
        sprites.put(PARADO_IZQUIERDA, paradoIzquierda);

        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.jugador_andando_derecha),
                ancho, altura,
                13, 14, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.jugador_andando_izquierda),
                ancho, altura,
                13, 14, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);
        Sprite disparandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.jugador_dispara_derecha),
                ancho, altura,
                9, 9, true);
        sprites.put(DISPARANDO_DERECHA, disparandoDerecha);

        Sprite disparandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.jugador_dispara_izquierda),
                ancho, altura,
                9, 9, true);
        sprites.put(DISPARANDO_IZQUIERDA, disparandoIzquierda);

        // animación actual
        sprite = paradoDerecha;
    }
    public void actualizar (long tiempo) {

        boolean finSprite = sprite.actualizar(tiempo);





        if(animDisparar){

            if(velocidadX < 0){
                sprite = sprites.get(DISPARANDO_IZQUIERDA);
            }else{
                sprite = sprites.get(DISPARANDO_DERECHA);
            }
        }else{

            if(velocidadX == 0 && velocidadY == 0){
                if(ultimaVel>= 0){
                    sprite = sprites.get(PARADO_DERECHA);
                }else{
                    sprite = sprites.get(PARADO_IZQUIERDA);
                }
            }else {

                if (velocidadX >= 0) {
                    sprite = sprites.get(CAMINANDO_DERECHA);
                }
                if (velocidadX < 0) {
                    sprite = sprites.get(CAMINANDO_IZQUIERDA);
                }

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
        if(vida < 0){
            vida = 0;
        }
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

        System.out.println(posicionJugadorX+"  --  "+posicionJugadorY + " -- "+disparar);

        if(posicionJugadorX != 0 && posicionJugadorY != 0) {

            float vecX = (posicionJugadorX) / (float) ((Math.sqrt(Math.pow((double) posicionJugadorX, 2) + Math.pow((double) posicionJugadorY, 2))));
            float vecY = (posicionJugadorY) / (float) ((Math.sqrt(Math.pow((double) posicionJugadorX, 2) + Math.pow((double) posicionJugadorY, 2))));

            float posicionXCalculada = VELOCIDAD * vecX;
            float posicionYCalculada = VELOCIDAD * vecY;


            velocidadY = posicionYCalculada;
            velocidadX = posicionXCalculada * -1;

        }else{
            velocidadX = 0;
            velocidadY =  0;
        }
        //System.out.println(velocidadX+"  --  "+velocidadY);

        if(velocidadY != 0 && velocidadX != 0){
            ultimaVel = velocidadX;
        }
        animDisparar = disparar;
    }





}
