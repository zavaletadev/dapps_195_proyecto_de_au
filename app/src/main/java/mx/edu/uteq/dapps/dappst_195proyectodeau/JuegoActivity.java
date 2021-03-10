package mx.edu.uteq.dapps.dappst_195proyectodeau;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author zavaletamx
 */
public class JuegoActivity extends AppCompatActivity {

    //Sonido cuando encontramos una letra
    private MediaPlayer sonidoLetraEncontrada;

    //Sonido cuando No encontramos una letra
    private MediaPlayer sonidoLetraNoEncontrada;

    //Sonido cuando perdemos
    private MediaPlayer sonidoGameOver;

    //Atributo para manejar un temporizador y cronometrar el juego
    private Timer temporizador;

    //Contamos el tiempo (sin formato de cronómetro)
    private int tiempo = 0;

    private AlertDialog.Builder alerta;

    //Referenciamos el ImageView para cambiarle la imagen dependiendo
    //del número de errores
    private ImageView ivAhorcado;

    //Generamos un layout de tipo grid
    private GridLayout glTeclado;

    private final String LETRAS = "QWERTYUIOPASDFGHJKLÑZXCVBNM";

    //Creamos un arreglo de TextViews para simular botones
    private final TextView[] TECLAS = new TextView[LETRAS.length()];

    private TextView tvTiempo;

    private TextView tvPista;

    private TextView tvGuiones;

    //Creamos un arreglo para convertir la palabra a buscar en un
    //arreglo de caracteres
    private char[] arrLetras;

    //Creamos un arreglo para convertir le número de letras de la
    //palabra en guiones
    private char[] arrGuiones;

    /*
    Palabra fija
    TODO: Te toca que la palabra sea aleatoria y de la dificultad seleccionada
     */
    private String pista = "Músculo que está situado en el cuello y tiene la función de permitir el giro y la inclinación lateral de la cabeza";

    /*
    Pista fija
    TODO: Te toca que la pista corresponda a la palabra aleatoria
     */
    private String palabra = "ESTERNOCLEIDOMASTOIDEO";

    /*
    Iniciamos el número de errores
     */
    int errores = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        sonidoLetraEncontrada = MediaPlayer.create(JuegoActivity.this, R.raw.atinar);
        sonidoLetraNoEncontrada = MediaPlayer.create(JuegoActivity.this, R.raw.noatinar);
        sonidoGameOver = MediaPlayer.create(JuegoActivity.this, R.raw.gameover);

        ivAhorcado = findViewById(R.id.iv_ahorcado);
        tvPista = findViewById(R.id.tv_pista);
        tvGuiones = findViewById(R.id.tv_guiones);
        tvTiempo = findViewById(R.id.tv_tiempo);

        /*
        Inicializamos el temporizador
         */
        temporizador = new Timer();

        /*
        Creamos un hilo para inicializar el temporizador en segundo plano
         */
        temporizador.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tiempo++;
                                /*
                                TODO: Te toca darle formato de cronómetro (00:00:00)
                                 */
                                tvTiempo.setText(String.valueOf(tiempo));
                            }
                        });
                    }
                }
        , 1000, 1000);

        /*
        Convertimos la palabra a jugar en un arreglo de guiones
        Ejemplo:
        String "perro"
        char ['p', 'e', 'r', 'r', 'o']
         */
        arrLetras = palabra.toCharArray();

        /*
        Creamos un arreglo del mismo tamaño que la palabra, sustituimos cada letra
        por un guion

        Ejemplo
        char ['p', 'e', 'r', 'r', 'o']
        char ['_', '_', '_', '_', '_']
         */
        arrGuiones = new char[arrLetras.length];
        for(int i = 0; i < arrGuiones.length; i++) {
            //Agregamos un guion para cada letra
            arrGuiones[i] = '_';
        }

        /*
        Mostramos el contenido del arreglo de guiones en el TextView
        Donde iremos adivinando la palabra
         */
        tvGuiones.setText(String.valueOf(arrGuiones));

        //Pegamos el texto de la pista como HTML para que automaticamente
        //se ajuste el texto como un parrafo
        tvPista.setText(Html.fromHtml(pista));

        glTeclado = findViewById(R.id.gl_teclado);

        //Recorremos el arreglo de textviews que simulan botones
        for (int i = 0; i < TECLAS.length; i++) {
            //Creamos un textView por cada elemento del arreglo
            TextView tecla = new TextView(JuegoActivity.this);

            //Indicamos el tamaño de cada textview (ancho y alto)
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    75,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            //indicamos el margen
            params.setMargins(10, 10, 10, 10);

            //Aplicamos ancho y margen
            tecla.setLayoutParams(params);

            //Indicamos el texto de cada textview, que es la posición
            //del arreglo para tomar esa letra
            //Ejemplo
            //0 1 2 3 4 5 6 7 8 9 ... ETC
            //A B C D E F G H I J ... ETC
            tecla.setText(LETRAS.charAt(i)+"");

            //Indicamos el apdding de cada textview
            tecla.setPadding(20, 20, 20, 20);

            //indicamos el tamaño de la fuente
            tecla.setTextSize(16);

            //Indicamos que el elemento debe verse centrado
            tecla.setGravity(Gravity.CENTER);

            //Le ponemos un color de fondo a cada textview
            tecla.setBackgroundColor(Color.parseColor("#939292"));

            tecla.setOnClickListener(this::buscaLetra);

            //Agregamos cada textview con la letra correspondiente
            glTeclado.addView(tecla);
        }

        alerta = new AlertDialog.Builder(JuegoActivity.this);
        alerta.setTitle("DEMO")
                .setMessage("Este demo solo muestra la parte básica y funcional.\n\n" +
                        "<<<Para DE>>>\n" +
                        "- Aún no están implementadas las dificultades\n" +
                        "- El tiempo no tiene formato de cronómetro\n" +
                        "- El juego aún no define cuando pierdes\n" +
                        "- El juego aún no define cuando ganas\n" +
                        "- Aún no se implementa una puntuación\n\n" +
                        "<<<Para AU>>>\n" +
                        "Las palabras no vienen de la base de datos vía webservice\n" +
                        "No se guarda el nombre y puntuación de la partida en la base de datos vía webservice\n" +
                        "No se ve una tabla de clasificación con los mejores 50 scores")
                .setCancelable(false)
                .setNeutralButton("Continuar", null)
                .show();
    }

    /*
    Método que evalua si la letra seleccionada existe en la palabra
     */
    public void buscaLetra(View v) {
        /*
        Tomamos el textview de la letra seleccionada
         */
        TextView tvSeleccionado = (TextView) v;

        /*
        Tomamos la letra seleccionada y la convertimos de
        String a char
         */
        char letra = tvSeleccionado.getText().toString().charAt(0);

        /*
        Evaluamos si esa letra está en la palabra
         */
        if (encuentraLetra(letra)) {
            /*
            Si encontramos una letra agregamos un sonido
             */
            sonidoLetraEncontrada.start();
        }

        else {
            /*
            Aumentamos los errores
             */
            errores++;
            cambiaImagen(errores);
        }
        
    }

    /*
    Método que cambia la imagen dependiendo del número de errores
    Y ejecuta un sonido
     */
    public void cambiaImagen(int error) {
        switch (error) {
            case 0 :
                ivAhorcado.setImageResource(R.drawable.ahorcado0);
                sonidoLetraNoEncontrada.start();
                break;
            case 1 :
                ivAhorcado.setImageResource(R.drawable.ahorcado1);
                sonidoLetraNoEncontrada.start();
                break;

            case 2 :
                ivAhorcado.setImageResource(R.drawable.ahorcado2);
                sonidoLetraNoEncontrada.start();
                break;

            case 3 :
                ivAhorcado.setImageResource(R.drawable.ahorcado3);
                sonidoLetraNoEncontrada.start();
                break;

            case 4 :
                ivAhorcado.setImageResource(R.drawable.ahorcado4);
                sonidoLetraNoEncontrada.start();
                break;

            case 5 :
                ivAhorcado.setImageResource(R.drawable.ahorcado5);
                sonidoGameOver.start();
                break;
                
        }
    }

    /*
    Método para buscar una letra en la palabra
     */
    public boolean encuentraLetra(char letra) {

        //por defecto no encontramos nada
        boolean encontrado = false;

        /*
        Recorremos todas las letras de la palabra
         */
        for (int i = 0; i < arrLetras.length; i++) {
            //Evaluamos si la letra que se busca está en
            //la palabra
            if (letra == arrLetras[i]) {
                //Si es correcto, agregamos la letra
                //en los guiones para
                //"destaparla"
                arrGuiones[i] = letra;

                //si encontramos una sola letra
                //indicamos que hay una coincidencia
                encontrado = true;
            }
        }

        /*
        Actualizamos le contenido de los guiones en el textView
         */
        tvGuiones.setText(String.valueOf(arrGuiones));

        return encontrado;
    }

}