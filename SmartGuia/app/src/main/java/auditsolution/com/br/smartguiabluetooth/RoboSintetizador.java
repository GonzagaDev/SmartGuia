package auditsolution.com.br.smartguiabluetooth;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Win10-Home on 27/07/2016.
 */
public class RoboSintetizador {

    /****
     * ESTE TRCHO DO CÓDIGO SINTETIZA UMA STRING
     ********/
    public void sintetizar(Context contextApp, TextToSpeech roboSintetizador, String texto) {
        String msgSaudacao = texto;

        final TextToSpeech finalRoboSintetizador = roboSintetizador;
        roboSintetizador = new TextToSpeech(contextApp, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    finalRoboSintetizador.setLanguage(Locale.getDefault());
                }
            }
        });
        String toSpeakSaudacao = msgSaudacao;
        Toast.makeText(contextApp, toSpeakSaudacao, Toast.LENGTH_SHORT).show();
        roboSintetizador.speak(toSpeakSaudacao, TextToSpeech.QUEUE_FLUSH, null);

    }
    /************************************************************************/
}

