package com.shooteraereo.modelos;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.shooteraereo.GameView;
import com.shooteraereo.R;
import com.shooteraereo.gestores.CargadorGraficos;
import com.shooteraereo.gestores.Utilidades;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Fernando on 18/11/2017.
 */

public class Nivel {
    private Context context = null;
    private int numeroNivel;
    private Fondo fondo;
    private Tile[][] mapaTiles;
    private Jugador jugador;

    public static int scrollEjeX = 0;
    public static int scrollEjeY = 0;

    //orinetaciones
    //jugador
    public float posicionJugadorX;
    public float posicionJugadorY;
    //disparos
    public float posicionDisparoX;
    public float posicionDisparoY;
    public boolean botonDisparando = false;

    //listas de modelos
    private List<DisparoJugador> disparosJugador;


    public boolean inicializado;

    public Nivel(Context context, int numeroNivel) throws Exception {
        inicializado = false;

        this.context = context;
        this.numeroNivel = numeroNivel;
        inicializar();

        inicializado = true;
    }

    public void inicializar() throws Exception {
        scrollEjeX = 0;
        scrollEjeY = 0;
        fondo = new Fondo(context, CargadorGraficos.cargarBitmap(context, R.drawable.background), 0);
        disparosJugador = new LinkedList<DisparoJugador>();
        inicializarMapaTiles();
    }


    public void actualizar(long tiempo) {
        if (inicializado) {
            jugador.sumarTiempo();

            jugador.procesarOrdenes(posicionJugadorX, posicionJugadorY, botonDisparando);
            boolean disparado = jugador.posibleDisparo();

            for (DisparoJugador disparoJugador : disparosJugador) {
                disparoJugador.actualizar(tiempo);
            }

            if (botonDisparando && disparado) {
                System.out.println("Comenzamos el disparo");
                jugador.disparando = false;
                jugador.ponerTiempoaCero();
                botonDisparando = false;
                disparosJugador.add(new DisparoJugador(context, jugador.x, jugador.y, posicionDisparoX, posicionDisparoY));
                System.out.println("Se ha disparado");

            }

            jugador.actualizar(tiempo);
            aplicarReglasMovimiento();
        }
    }


    public void dibujar(Canvas canvas) {
        if (inicializado) {
            fondo.dibujar(canvas);
            dibujarTiles(canvas);
            jugador.dibujar(canvas);

            for (DisparoJugador disparoJugador : disparosJugador) {
                disparoJugador.dibujar(canvas);
            }

        }
    }

    private void dibujarTiles(Canvas canvas) {

        // Calcular que tiles serán visibles en la pantalla
        // La matriz de tiles es más grande que la pantalla
        int tileXJugador = (int) jugador.x / Tile.ancho;
        int izquierda = (int) (tileXJugador - tilesEnDistanciaX(jugador.x - scrollEjeX));
        izquierda = Math.max(0, izquierda); // Que nunca sea < 0, ej -1

        int tileYJugador = (int) jugador.y / Tile.altura;
        int abajo = (int) (tileYJugador - tilesEnDistanciaX(jugador.y - scrollEjeY));
        abajo = Math.max(0, abajo);

        if (jugador.x < anchoMapaTiles() * Tile.ancho - GameView.pantallaAncho * 0.3)
            if (jugador.x - scrollEjeX > GameView.pantallaAncho * 0.7) {
                fondo.mover((int) (jugador.x - GameView.pantallaAncho * 0.7 - scrollEjeX));
                scrollEjeX = (int) ((jugador.x) - GameView.pantallaAncho * 0.7);

            }

        if (jugador.y < altoMapaTiles() * Tile.altura - GameView.pantallaAlto * 0.3) {
            if (jugador.y - scrollEjeY > GameView.pantallaAlto * 0.7) {
                scrollEjeY = (int) ((jugador.y) - GameView.pantallaAlto * 0.7);
            }
        }

        if (jugador.y > GameView.pantallaAlto * 0.3) {
            if (jugador.y - scrollEjeY < GameView.pantallaAlto * 0.3) {
                scrollEjeY = (int) (jugador.y - GameView.pantallaAlto * 0.3);
            }
        }


        if (jugador.x > GameView.pantallaAncho * 0.3)
            if (jugador.x - scrollEjeX < GameView.pantallaAncho * 0.3) {
                fondo.mover((int) (jugador.x - GameView.pantallaAncho * 0.3 - scrollEjeX));
                scrollEjeX = (int) (jugador.x - GameView.pantallaAncho * 0.3);
            }


        int derecha = izquierda +
                GameView.pantallaAncho / Tile.ancho + 1;

        // el ultimo tile visible, no puede superar el tamaño del mapa
        derecha = Math.min(derecha, anchoMapaTiles() - 1);


        for (int y = 0; y < altoMapaTiles(); y++) {
            for (int x = 0; x < anchoMapaTiles(); x++) {
                if (mapaTiles[x][y].imagen != null) {
                    // Calcular la posición en pantalla correspondiente
                    // izquierda, arriba, derecha , abajo

                    mapaTiles[x][y].imagen.setBounds(
                            (x * Tile.ancho) - scrollEjeX,
                            (y * Tile.altura) - scrollEjeY,
                            (x * Tile.ancho) + Tile.ancho - scrollEjeX,
                            (y * Tile.altura) + Tile.altura - scrollEjeY);

                    mapaTiles[x][y].imagen.draw(canvas);
                }
            }
        }
    }

    private float tilesEnDistanciaX(double distanciaX) {
        return (float) distanciaX / Tile.ancho;
    }

    private float tilesEnDistanciaY(double distanciaY) {
        return (float) distanciaY / Tile.altura;
    }

    public int anchoMapaTiles() {
        return mapaTiles.length;
    }

    public int altoMapaTiles() {

        return mapaTiles[0].length;
    }


    private void inicializarMapaTiles() throws Exception {
        InputStream is = context.getAssets().open(numeroNivel + ".txt");
        int anchoLinea;

        List<String> lineas = new LinkedList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        {
            String linea = reader.readLine();
            anchoLinea = linea.length();
            while (linea != null) {
                lineas.add(linea);
                if (linea.length() != anchoLinea) {
                    Log.e("ERROR", "Dimensiones incorrectas en la línea");
                    throw new Exception("Dimensiones incorrectas en la línea.");
                }
                linea = reader.readLine();
            }
        }

        // Inicializar la matriz
        mapaTiles = new Tile[anchoLinea][lineas.size()];
        // Iterar y completar todas las posiciones
        for (int y = 0; y < altoMapaTiles(); ++y) {
            for (int x = 0; x < anchoMapaTiles(); ++x) {
                char tipoDeTile = lineas.get(y).charAt(x);//lines[y][x];
                mapaTiles[x][y] = inicializarTile(tipoDeTile, x, y);
            }
        }
    }

    private Tile inicializarTile(char codigoTile, int x, int y) {
        switch (codigoTile) {
            case '.':
                // en blanco, sin textura
                return new Tile(null, Tile.PASABLE);
            case '#':
                // bloque que nose puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.piedra), Tile.SOLIDO);
            case 'D':
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tierra),Tile.DESTRUCTIBLE);

            case '1':
                // Jugador
                // Posicion centro abajo
                int xCentroAbajoTile = x * Tile.ancho + Tile.ancho / 2;
                int yCentroAbajoTile = y * Tile.altura + Tile.altura;
                jugador = new Jugador(context, xCentroAbajoTile, yCentroAbajoTile);

                return new Tile(null, Tile.PASABLE);
            default:
                //cualquier otro caso
                return new Tile(null, Tile.PASABLE);
        }
    }

    private void aplicarReglasMovimiento() {
        int tileXJugadorIzquierda
                = (int) (jugador.x - (jugador.ancho / 2 - 1)) / Tile.ancho;
        int tileXJugadorDerecha
                = (int) (jugador.x + (jugador.ancho / 2 - 1)) / Tile.ancho;

        int tileYJugadorInferior
                = (int) (jugador.y + (jugador.altura / 2 - 1)) / Tile.altura;
        int tileYJugadorCentro
                = (int) jugador.y / Tile.altura;
        int tileYJugadorSuperior
                = (int) (jugador.y - (jugador.altura / 2 - 1)) / Tile.altura;

        //reglas jugador
        // derecha o parado
        if (jugador.velocidadX > 0) {
            // Tengo un tile delante y es PASABLE
            // El tile de delante está dentro del Nivel
            if (tileXJugadorDerecha + 1 <= anchoMapaTiles() - 1 &&
                    tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                jugador.x += jugador.velocidadX;

                // No tengo un tile PASABLE delante
                // o es el FINAL del nivel o es uno SOLIDO
            } else if (tileXJugadorDerecha <= anchoMapaTiles() - 1 &&
                    tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                // Si en el propio tile del jugador queda espacio para
                // avanzar más, avanzo
                int TileJugadorBordeDerecho = tileXJugadorDerecha * Tile.ancho + Tile.ancho;
                double distanciaX = TileJugadorBordeDerecho - (jugador.x + jugador.ancho / 2);

                if (distanciaX > 0) {
                    double velocidadNecesaria = Math.min(distanciaX, jugador.velocidadX);
                    jugador.x += velocidadNecesaria;
                } else {
                    // Opcional, corregir posición
                    jugador.x = TileJugadorBordeDerecho - jugador.ancho / 2;
                }
            }
        }


        // izquierda
        if (jugador.velocidadX <= 0) {
            // Tengo un tile detrás y es PASABLE
            // El tile de delante está dentro del Nivel
            if (tileXJugadorIzquierda - 1 >= 0 &&
                    tileYJugadorInferior < altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                jugador.x += jugador.velocidadX;

                // No tengo un tile PASABLE detrás
                // o es el INICIO del nivel o es uno SOLIDO
            } else if (tileXJugadorIzquierda >= 0 && tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorCentro].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior].tipoDeColision
                            == Tile.PASABLE) {

                // Si en el propio tile del jugador queda espacio para
                // avanzar más, avanzo
                int TileJugadorBordeIzquierdo = tileXJugadorIzquierda * Tile.ancho;
                double distanciaX = (jugador.x - jugador.ancho / 2) - TileJugadorBordeIzquierdo;

                if (distanciaX > 0) {
                    double velocidadNecesaria = Utilidades.proximoACero(-distanciaX, jugador.velocidadX);
                    jugador.x += velocidadNecesaria;
                } else {
                    // Opcional, corregir posición
                    jugador.x = TileJugadorBordeIzquierdo + jugador.ancho / 2;
                }
            }
        }


        // arriba
        if (jugador.velocidadY <= 0) {
            if (tileYJugadorSuperior - 1 >= 0
                    && mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior - 1].tipoDeColision == Tile.PASABLE
                    && mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior - 1].tipoDeColision == Tile.PASABLE
                    && mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior].tipoDeColision == Tile.PASABLE
                    && mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior].tipoDeColision == Tile.PASABLE) {

                jugador.y += jugador.velocidadY;
            } else if (tileYJugadorSuperior >= 0 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior].tipoDeColision == Tile.PASABLE
                    && mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior].tipoDeColision == Tile.PASABLE) {
                int tileJugadorBordeSuperior = tileYJugadorSuperior * Tile.altura;
                double distanciaY = (jugador.y - jugador.altura / 2) - tileJugadorBordeSuperior;
                if (distanciaY > 0) {
                    double velocidadNecesaria = Utilidades.proximoACero(-distanciaY, jugador.velocidadY);
                    jugador.y += velocidadNecesaria;
                } else {
                    jugador.y = tileJugadorBordeSuperior + jugador.altura / 2;
                }
            }
        }

        //abajo
        if (jugador.velocidadY > 0) {
            if (tileYJugadorInferior + 1 <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior + 1].tipoDeColision == Tile.PASABLE
                    && mapaTiles[tileXJugadorDerecha][tileYJugadorInferior + 1].tipoDeColision == Tile.PASABLE
                    && mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior].tipoDeColision == Tile.PASABLE
                    && mapaTiles[tileXJugadorDerecha][tileYJugadorInferior].tipoDeColision == Tile.PASABLE) {
                jugador.y += jugador.velocidadY;
            } else if (tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior].tipoDeColision == Tile.PASABLE
                    && mapaTiles[tileXJugadorDerecha][tileYJugadorInferior].tipoDeColision == Tile.PASABLE) {
                int tileJugadorBordeInferior = tileYJugadorInferior * Tile.altura + Tile.altura;
                double distanciaY = tileJugadorBordeInferior - (jugador.y + jugador.altura / 2);
                if (distanciaY > 0) {
                    double velocidadNecesaria = Math.min(distanciaY, jugador.velocidadY);
                    jugador.y += velocidadNecesaria;
                } else {
                    jugador.y = tileJugadorBordeInferior - jugador.altura / 2;
                }
            }
        }
//fin reglas jugador

        for (Iterator<DisparoJugador> iterator = disparosJugador.iterator(); iterator.hasNext(); ) {
            DisparoJugador disparoJugador = iterator.next();

            //variables para colisiones en X
            int tileXDisparo = (int) disparoJugador.x / Tile.ancho;
            int tileYDisparoInferior =
                    (int) (disparoJugador.y + disparoJugador.cAbajo) / Tile.altura;
            int tileYDisparoSuperior =
                    (int) (disparoJugador.y - disparoJugador.cArriba) / Tile.altura;

            //variables para colisiones en Y
            int tileYDisparo = (int) disparoJugador.y / Tile.altura;
            int tileXDisparoDerecha = (int) (disparoJugador.x + disparoJugador.cDerecha) / Tile.ancho;
            int tileXDisparoIzquierda = (int) (disparoJugador.x - disparoJugador.cIzquierda) / Tile.ancho;




            //derecha
            if (disparoJugador.velocidadX > 0) {
                // Tiene delante un tile pasable, puede avanzar.
                if (tileXDisparo + 1 <= anchoMapaTiles() - 1 &&
                        mapaTiles[tileXDisparo + 1][tileYDisparoInferior].tipoDeColision
                                == Tile.PASABLE &&
                        mapaTiles[tileXDisparo + 1][tileYDisparoSuperior].tipoDeColision
                                == Tile.PASABLE) {
                    disparoJugador.x += disparoJugador.velocidadX;
                } else if (tileXDisparo <= anchoMapaTiles() - 1) {
                    int TileDisparoBordeDerecho = tileXDisparo * Tile.ancho + Tile.ancho;
                    double distanciaX =
                            TileDisparoBordeDerecho - (disparoJugador.x + disparoJugador.cDerecha);
                    if (distanciaX > 0) {
                        double velocidadNecesaria =
                                Math.min(distanciaX, disparoJugador.velocidadX);
                        disparoJugador.x += velocidadNecesaria;
                    } else {
                        if( mapaTiles[tileXDisparo + 1][tileYDisparoInferior].tipoDeColision == Tile.DESTRUCTIBLE){
                            mapaTiles[tileXDisparo + 1][tileYDisparoInferior].tipoDeColision = Tile.PASABLE;
                            mapaTiles[tileXDisparo + 1][tileYDisparoInferior].imagen = CargadorGraficos.cargarDrawable(context,
                                    R.drawable.tile_transparente);
                        }
                        if(mapaTiles[tileXDisparo + 1][tileYDisparoSuperior].tipoDeColision == Tile.DESTRUCTIBLE){
                            mapaTiles[tileXDisparo + 1][tileYDisparoSuperior].tipoDeColision = Tile.PASABLE;
                            mapaTiles[tileXDisparo + 1][tileYDisparoSuperior].imagen = CargadorGraficos.cargarDrawable(context,
                                    R.drawable.tile_transparente);
                        }
                        iterator.remove();
                        continue;
                    }
                }
            }
            // izquierda
            if (disparoJugador.velocidadX <= 0) {
                if (tileXDisparo - 1 >= 0 &&
                        tileYDisparoSuperior < altoMapaTiles() - 1 &&
                        mapaTiles[tileXDisparo - 1][tileYDisparoSuperior].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXDisparo - 1][tileYDisparoInferior].tipoDeColision ==
                                Tile.PASABLE) {
                    disparoJugador.x += disparoJugador.velocidadX;
                    // No tengo un tile PASABLE detras
                    // o es el INICIO del nivel o es uno SOLIDO
                } else if (tileXDisparo >= 0) {
                    // Si en el propio tile del jugador queda espacio para
                    // avanzar más, avanzo
                    int TileDisparoBordeIzquierdo = tileXDisparo * Tile.ancho;
                    double distanciaX =
                            (disparoJugador.x - disparoJugador.cIzquierda) - TileDisparoBordeIzquierdo;
                    if (distanciaX > 0) {
                        if(mapaTiles[tileXDisparo - 1][tileYDisparoSuperior].tipoDeColision == Tile.DESTRUCTIBLE){
                            mapaTiles[tileXDisparo - 1][tileYDisparoSuperior].tipoDeColision = Tile.PASABLE;
                            mapaTiles[tileXDisparo - 1][tileYDisparoSuperior].imagen = CargadorGraficos.cargarDrawable(context,
                                    R.drawable.tile_transparente);
                        }
                        if(mapaTiles[tileXDisparo - 1][tileYDisparoInferior].tipoDeColision == Tile.DESTRUCTIBLE){
                            mapaTiles[tileXDisparo - 1][tileYDisparoInferior].tipoDeColision = Tile.PASABLE;
                            mapaTiles[tileXDisparo - 1][tileYDisparoInferior].imagen = CargadorGraficos.cargarDrawable(context,
                                    R.drawable.tile_transparente);
                        }
                        double velocidadNecesaria =
                                Utilidades.proximoACero(-distanciaX, disparoJugador.velocidadX);
                        disparoJugador.x += velocidadNecesaria;
                    } else {
                        iterator.remove();
                        continue;
                    }
                }
            }
            //arriba
            if(disparoJugador.velocidadY <= 0){
                if(tileYDisparo -1 >= 0 && tileXDisparoIzquierda < anchoMapaTiles() - 1
                        && mapaTiles[tileXDisparoIzquierda][tileYDisparo - 1].tipoDeColision == Tile.PASABLE
                &&mapaTiles[tileXDisparoDerecha][tileYDisparo - 1].tipoDeColision == Tile.PASABLE){
                    disparoJugador.y += disparoJugador.velocidadY;
                }else if(tileYDisparo >= 0){
                    int TileDisparoBordeArriba = tileYDisparo * Tile.altura;
                    double distanciaY = (disparoJugador.y - disparoJugador.cArriba) - TileDisparoBordeArriba;
                    if(distanciaY > 0){
                        double velocidadNecesaria = Utilidades.proximoACero(-distanciaY,disparoJugador.velocidadY);
                        disparoJugador.y += velocidadNecesaria;
                    }else{
                        if(mapaTiles[tileXDisparoIzquierda][tileYDisparo - 1].tipoDeColision == Tile.DESTRUCTIBLE){
                            mapaTiles[tileXDisparoIzquierda][tileYDisparo - 1].tipoDeColision = Tile.PASABLE;
                            mapaTiles[tileXDisparoIzquierda][tileYDisparo - 1].imagen = CargadorGraficos.cargarDrawable(context,
                                    R.drawable.tile_transparente);
                        }
                        if(mapaTiles[tileXDisparoDerecha][tileYDisparo - 1].tipoDeColision == Tile.DESTRUCTIBLE){
                            mapaTiles[tileXDisparoDerecha][tileYDisparo - 1].tipoDeColision = Tile.PASABLE;
                            mapaTiles[tileXDisparoDerecha][tileYDisparo - 1].imagen = CargadorGraficos.cargarDrawable(context,
                                    R.drawable.tile_transparente);
                        }
                        iterator.remove();
                        continue;
                    }
                }
            }
            //abajo
            if(disparoJugador.velocidadY > 0){
                if(tileYDisparo + 1 <= altoMapaTiles()-1 &&
                        mapaTiles[tileXDisparoDerecha][tileYDisparo + 1].tipoDeColision == Tile.PASABLE &&
                        mapaTiles[tileXDisparoIzquierda][tileYDisparo + 1].tipoDeColision == Tile.PASABLE){
                    disparoJugador.y += disparoJugador.velocidadY;
                }else if(tileYDisparo <= altoMapaTiles()-1){
                        int TileDisparoBordeAbajo = tileYDisparo * Tile.altura +Tile.altura;
                        double distanciaY = TileDisparoBordeAbajo - (disparoJugador.y + disparoJugador.cAbajo);
                    if(distanciaY > 0){
                        double velocidadNecesaria = Math.min(distanciaY,disparoJugador.velocidadY);
                        disparoJugador.y += velocidadNecesaria;
                    }else{
                        if( mapaTiles[tileXDisparoDerecha][tileYDisparo + 1].tipoDeColision == Tile.DESTRUCTIBLE){
                            mapaTiles[tileXDisparoDerecha][tileYDisparo + 1].tipoDeColision = Tile.PASABLE;
                            mapaTiles[tileXDisparoDerecha][tileYDisparo + 1].imagen = CargadorGraficos.cargarDrawable(context,
                                    R.drawable.tile_transparente);
                        }
                        if(mapaTiles[tileXDisparoIzquierda][tileYDisparo + 1].tipoDeColision == Tile.DESTRUCTIBLE){
                            mapaTiles[tileXDisparoIzquierda][tileYDisparo + 1].tipoDeColision = Tile.PASABLE;
                            mapaTiles[tileXDisparoIzquierda][tileYDisparo + 1].imagen = CargadorGraficos.cargarDrawable(context,
                                    R.drawable.tile_transparente);
                        }
                        iterator.remove();
                        continue;
                    }

                }
            }



        }
        //fin reglas disparos jugador


    }//fin aplicar reglas de movimiento


}//fin clase nivel