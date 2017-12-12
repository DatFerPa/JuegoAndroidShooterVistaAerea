package com.shooteraereo.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.shooteraereo.R;
import com.shooteraereo.gestores.CargadorGraficos;
import com.shooteraereo.graficos.Sprite;

import java.util.HashMap;

/**
 * Created by Fernando on 26/11/2017.
 */

public class EnemigoDisparador extends  Modelo {

    public static final String CAMINANDO_DERECHA = "Caminando_derecha";
    public static final String CAMINANDO_IZQUIERDA = "caminando_izquierda";
    public static final String MUERTE_DERECHA = "muerte_derecha";
    public static final String MUERTE_IZQUIERDA = "muerte_izquierda";
    public static final String DISPARO_DERECHA = "disparo_derecha";
    public static final String DISPARO_IZQUIERDA = "disparo_izquierda";

    public int estado = ACTIVO;
    public static final int ACTIVO = 1;
    public static final int INACTIVO = 0;
    public static final int ELIMINAR = -1;
    protected Sprite sprite;
    protected HashMap<String,Sprite> sprites = new HashMap<String,Sprite> ();
    public static final double VEL_MAXIMA = 2;
    public double velocidadX = 0;
    public double velocidadY = 0;
    public int tiempoMovimiento = 0;

    public int orientacion;
    public static final int DERECHA = 1;
    public static final int IZQUIERDA = -1;

    public boolean seHaDisparado = false;


    public boolean disparando = false;
    public int tiempoDisparo = 50;
    public int tiempoActual = 0;

    private int radioAtaque = 700;
    private boolean radioParaDisparo;

    public EnemigoDisparador(Context context, double x, double y) {
        super(context, 0, 0, 40,40 );



        this.x = x;
        this.y = y - altura/2;

        orientacion = DERECHA;


        inicializar();
    }


    public void inicializar (){

        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_corre_derecha),
                ancho, altura,
                4, 4, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_corre_izquierda),
                ancho, altura,
                4, 4, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);

        Sprite muerteDerecha = new Sprite(

                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_derrota_derecha),

                ancho, altura,

                11, 11, false);

        sprites.put(MUERTE_DERECHA, muerteDerecha);

        Sprite muerteIzquierda = new Sprite(

                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_derrota_izquierda),

                ancho, altura,

                11, 11, false);

        sprites.put(MUERTE_IZQUIERDA, muerteIzquierda);

        Sprite disparoIzquierda = new Sprite(

                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_disparo_izquierda),

                ancho, altura,

                8, 8, true);

        sprites.put(DISPARO_IZQUIERDA, disparoIzquierda);

        Sprite disparoDerecha = new Sprite(

                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_disparo_derecha),

                ancho, altura,

                8, 8, true);

        sprites.put(DISPARO_DERECHA, disparoDerecha);

        sprite = caminandoDerecha;
    }

    @Override
    public void actualizar (long tiempo){
        super.actualizar(tiempo);
        boolean finSprite = sprite.actualizar(tiempo);

        if ( estado == INACTIVO && finSprite == true){
            estado = ELIMINAR;
        }
        if (estado == INACTIVO){
            if (velocidadX > 0)
                sprite = sprites.get(MUERTE_DERECHA);
            else
                sprite = sprites.get(MUERTE_IZQUIERDA);
        }else {

            if (radioParaDisparo) {

                if (velocidadX < 0) {
                    sprite = sprites.get(DISPARO_IZQUIERDA);
                } else if (velocidadX >= 0) {
                    sprite = sprites.get(DISPARO_DERECHA);
                }
            } else {
                if (velocidadX >= 0) {
                    orientacion = DERECHA;
                    sprite = sprites.get(CAMINANDO_DERECHA);
                }
                if (velocidadX < 0) {
                    orientacion = IZQUIERDA;
                    sprite = sprites.get(CAMINANDO_IZQUIERDA);
                }
            }
        }



    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX , (int) y - Nivel.scrollEjeY);
    }

    //Ejecutar en operaciones del jugador

    public void actualizarTiempoParaMoverse(){
        if(tiempoMovimiento%20==0){
            generarVelocidades();
        }

        tiempoMovimiento++;

    }

    public void generarVelocidades(){
        double modX = Math.random()*2-1;
        double modY = Math.random()*2-1;
        velocidadX = modX * VEL_MAXIMA;
        velocidadY = modY * VEL_MAXIMA;
    }

    //logica para el movimiento


    //logica para disparos
    public void jugadorEnRadioYDisparoPosible(double xJugador, double yJugador){
        double distancia = Math.sqrt(Math.pow(xJugador - x, 2) + Math.pow(yJugador - y, 2));
        //disparo cuando jugador dentro del radio y cuando tiempo para disparar

        radioParaDisparo = distancia < radioAtaque;

        if(radioParaDisparo && disparando == true){
            disparando = false;
            seHaDisparado = true;

        }else{
            seHaDisparado = false;

        }
    }

    public void actualizarTiempoDisparo(){
        ++tiempoActual;
        if(tiempoActual>= tiempoDisparo &&  this.estado ==ACTIVO){
            disparando = true;
            tiempoActual= 0;
        }
    }

}