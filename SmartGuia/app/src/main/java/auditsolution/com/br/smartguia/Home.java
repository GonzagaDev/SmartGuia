package auditsolution.com.br.smartguia;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Home extends AppCompatActivity {
    public static int ENABLE_BLUETOOTH = 1;
    public static int SELECT_PAIRED_DEVICE = 2;
    public static int SELECT_DISCOVERED_DEVICE = 3;

    static TextView statusMessage;
    TextToSpeech roboSintetizador;
    EditText lb_text;
    Button botaoEnviar;
    String msgSaudacao, verificaBluetooh, verificaSeBluetoothEstaAtivo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lb_text = (EditText) findViewById(R.id.editText);
        botaoEnviar = (Button) findViewById(R.id.bt_sintetizar);

        roboSintetizador = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    roboSintetizador.setLanguage(Locale.getDefault());
                }
            }
        });
        /*** REPRODUZ MENSAGEM DE BOAS VINDAS ***/
        msgSaudacao = "";
        msgSaudacao = "Olá, eu sou SmartGuia seu assistente";

        String toSpeakSaudacao = msgSaudacao;
        Toast.makeText(getApplicationContext(), toSpeakSaudacao, Toast.LENGTH_SHORT).show();
        roboSintetizador.speak(toSpeakSaudacao, TextToSpeech.QUEUE_FLUSH, null);


        /**** VERIFICA SE O DISPOSITIVO SUPORTA BLUETOOTH ****/
        verificaBluetooh = "";

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            verificaBluetooh = "Que pena! seu Hardware Bluetooth não está funcionando.";
            return;
        } else {
            verificaBluetooh = "Ótimo! seu Hardware Bluetooth está funcionando.";
        }
        String toSpeakValidatebt = verificaBluetooh;
        Toast.makeText(getApplicationContext(), toSpeakValidatebt, Toast.LENGTH_SHORT).show();
        roboSintetizador.speak(toSpeakValidatebt, TextToSpeech.QUEUE_FLUSH, null);

        /*** CASO O BLUETOOTH NÃO ESTEJA ATIVO SOLICITA SUA ATIVAÇÃO ***/


        verificaSeBluetoothEstaAtivo = "";
        if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH);
            verificaSeBluetoothEstaAtivo = "Solicitando ativação do Bluetooth";
        } else {
            verificaSeBluetoothEstaAtivo = "Bluetooth já ativado!";
            statusMessage.setText("Olá eu sou seu Guia, Ative minhas funções para que eu possa lhe ajudar!");
        }

        String toSpeakValidateBluetoothAtivo = verificaSeBluetoothEstaAtivo;
        Toast.makeText(getApplicationContext(), toSpeakValidateBluetoothAtivo, Toast.LENGTH_SHORT).show();
        roboSintetizador.speak(toSpeakValidateBluetoothAtivo, TextToSpeech.QUEUE_FLUSH, null);
    }

    /*** VERIFICA SE O USUÁRIO ATIVOU O BLUETOOTH CONFORME SOLICITAÇÃO ***/

    /***
     * VERIFICA SE O USUÁRIO ATIVOU O BLUETOOTH CONFORME SOLICITAÇÃO
     *****/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        verificaSeBluetoothEstaAtivo = "";
        if (requestCode == ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                verificaSeBluetoothEstaAtivo = "Bluetooth ativado!";

            } else {
                verificaSeBluetoothEstaAtivo = "Bluetooth não ativado!";
            }
        }
        /*asdsaasdas*/


        botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = lb_text.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                roboSintetizador.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

}
