package mx.edu.uteq.dapps.dappst_195proyectodeau;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    /* MediaPlayer permite manejar multimedia (Audio y video) */
    private MediaPlayer cancionFondo;
    private AlertDialog.Builder alerta;
    private Button btnScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alerta = new AlertDialog.Builder(MainActivity.this);
        btnScores = findViewById(R.id.btn_scores);

        /*
        Creamos una carpeta dentro de res llamada raw donde agregaremos
        los audios a reproducir

        Inicializamos el atributo de mediaplayer haciendo referencia a un
        archivo dentro de raw
         */
        cancionFondo = MediaPlayer.create(MainActivity.this, R.raw.home);

        //Si quisieras bajar el volumen de fondo, agrega un valor flotante entre 0f y 1f
        //cancionFondo.setVolume(0.1f, 0.1f);

        //Indicamos que la canción siempre va a reproducirse
        cancionFondo.setLooping(true);

        //Iniciamos la reproducción
        cancionFondo.start();

        btnScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.setTitle("DEMO")
                        .setMessage("No hay Scores, te toca a ti... (>'')>")
                        .setCancelable(false)
                        .setPositiveButton("", null)
                        .setNegativeButton("", null)
                        .setNeutralButton("Que chafa eh...", null)
                        .show();

            }
        });

    }

    public void navJuego(View v) {
        /*
        Este ejemplo no tiene dificultades, todos los botones envian al mismo juego
        TODO: Agregar dificultades
         */
        alerta.setTitle("ESTO ES UN DEMO")
                .setMessage("No hay dificultades, te toca agregarlas")
                .setCancelable(false)
                .setPositiveButton("A jugar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Paramos el audio y lo regresamos al principio
                        cancionFondo.stop();
                        //Regresamos la canción al principio
                        try {
                            cancionFondo.prepare();
                            cancionFondo.seekTo(0);

                            //Vamos al juego
                            startActivity(new Intent(
                                    MainActivity.this,
                                    JuegoActivity.class
                            ));
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Otra dificultad", null)
                .setNeutralButton("", null)
                .show();
    }
}