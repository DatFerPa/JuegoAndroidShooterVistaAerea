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

    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite> ();

    double xInicial;
    double yInicial;

    public Jugador(Context context, double xInicial, double yInicial) {
        super(context, 0, 0, 60, 60);

        // guardamos la posición inicial porque más tarde vamos a reiniciarlo
        this.xInicial = xInicial;
        this.yInicial = yInicial - altura/2;

        this.x =  this.xInicial;
        this.y =  this.yInicial;

        inicializar();
    }

    public void inicializar (){
        Sprite prueba = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.jugador_andando_derecha),
                ancho, altura,
                13, 14, true);
        sprites.put(PARADO_DERECHA, prueba);


// animación actual
        sprite = prueba;
    }
    public void actualizar (long tiempo) {
        sprite.actualizar(tiempo);
    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x  , (int) y );
    }


}
