package auditsolution.com.br.smartguiabluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import auditsolution.com.br.smartguiabluetooth.dao.DaoTransmissor;
import auditsolution.com.br.smartguiabluetooth.model.Transmissor;


public class MainBluetoothActivity extends ActionBarActivity {

    public static int ENABLE_BLUETOOTH = 1;
    public static int SELECT_PAIRED_DEVICE = 2;
    public static int SELECT_DISCOVERED_DEVICE = 3;

    static TextView statusMessage;
    static TextView textSpace;
    static TextView messageBox;
    static Button btSend;
    static Button bt_clear;
    static Button bt_buscar, bt_connect, bt_dispositivoPareados, bt_buscadispotivos;
    static Button bt_wait;
    static Button bt_ficarVisivel;
    static MenuItem menuUser;
    static MenuItem menuTest;


    TextToSpeech roboSintetizador;

    ConnectionThread connect;
    DaoTransmissor dao = new DaoTransmissor(App.context);

    private String MSG_DB_0001 = "RETORNO BANCO DE DADOS: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bluetooth);


        statusMessage = (TextView) findViewById(R.id.statusMessage);
        textSpace = (TextView) findViewById(R.id.textSpace);
        btSend = (Button) findViewById(R.id.button_Send);
        bt_clear = (Button) findViewById(R.id.bt_clear);
        messageBox = (TextView) findViewById(R.id.editText_MessageBox);
        bt_buscar = (Button) findViewById(R.id.bt_consultar);
        bt_connect = (Button) findViewById(R.id.bt_connect);
        bt_wait = (Button) findViewById(R.id.button_WaitConnection);
        bt_ficarVisivel = (Button) findViewById(R.id.button_Visibility);
        bt_ficarVisivel = (Button) findViewById(R.id.button_Visibility);
        bt_dispositivoPareados = (Button) findViewById(R.id.button_PairedDevices);
        bt_buscadispotivos = (Button) findViewById(R.id.button_DiscoveredDevices);
        menuUser = (MenuItem) findViewById(R.id.action_user);
        menuTest = (MenuItem) findViewById(R.id.action_test);

        // CHAMA SINTETIZADOR
        String msgInicio = "Smart Guia Iniciado!";


        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        String msgAdapter = "";
        if (btAdapter == null) {
            msgAdapter = "Que pena! Hardware Bluetooth não está funcionando :(";
            statusMessage.setText(msgAdapter);

        } else {
            msgAdapter = "Ótimo! Hardware Bluetooth está funcionando :)";
            statusMessage.setText(msgAdapter);

            if (!btAdapter.isEnabled()) {
                msgAdapter = "Ativando Bluetooth";
                /**
                 * ATIVA O BLUETOOTH AUTOMATICAMENTE
                 */

                btAdapter.enable();

            } else {
                msgAdapter = "Bluetooth já ativado :)";
                statusMessage.setText(msgAdapter);

            }
        }
        sintezar(msgInicio + msgAdapter);
        invisibleBotoes();

        ConnectaArduino();


    }

    public void bt_connect(View View) {
        ConnectaArduino();
    }

    public void ConnectaArduino() {

        statusMessage.setText("Buscando conexão!");
        connect = new ConnectionThread("20:16:04:18:29:29");
        connect.start();
    }

    /**
     * METODO QUE SINTETIZA A STRING
     */


    public void sintezar(final String texto) {
        //roboSintetizador = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

        /**
         *  CHAMAR METODO APP.context PARA OBTER O CONTEXTO DA APLICAÇÃO
         */

        roboSintetizador = new TextToSpeech(App.context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = roboSintetizador.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Este idioma não é Suportado");
                    } else {
                        //  speakOut();
                        roboSintetizador.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
                    }
                } else {
                    Log.e("TTS", "A inicialização Falhou!");
                }
            }

            public void speakOut(String msg) {
                //   String text = txtText.getText().toString();
                roboSintetizador.speak(msg, TextToSpeech.QUEUE_ADD, null);

            }
        });
    }

    /**
     * MÉTODO QUE DEIXA OS COMPONENTES INVISIVEIS
     */

    public static void invisibleBotoes() {
        btSend.setVisibility(View.INVISIBLE);
        bt_clear.setVisibility(View.INVISIBLE);
        bt_buscar.setVisibility(View.INVISIBLE);
        bt_wait.setVisibility(View.INVISIBLE);
        bt_ficarVisivel.setVisibility(View.INVISIBLE);
        statusMessage.setVisibility(View.INVISIBLE);
        textSpace.setVisibility(View.VISIBLE);
        messageBox.setVisibility(View.INVISIBLE);
        bt_buscadispotivos.setVisibility(View.INVISIBLE);
        bt_dispositivoPareados.setVisibility(View.INVISIBLE);


    }

    /**
     * MÉTODO QUE DEIXA OS COMPONENTES VISIVEIS
     */

    public static void visibleBotoes() {
        btSend.setVisibility(View.VISIBLE);
        bt_clear.setVisibility(View.VISIBLE);
        bt_buscar.setVisibility(View.VISIBLE);
        bt_wait.setVisibility(View.VISIBLE);
        bt_ficarVisivel.setVisibility(View.VISIBLE);
        statusMessage.setVisibility(View.VISIBLE);
        textSpace.setVisibility(View.VISIBLE);
        messageBox.setVisibility(View.VISIBLE);
        bt_dispositivoPareados.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_bluetooth, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        String msgMenu = "";
        if (id == R.id.action_test) {
            //AÇÃO AO CLICAR NO MENU
            msgMenu = "Modo teste Ativado!";
            Toast.makeText(getApplicationContext(), msgMenu, Toast.LENGTH_LONG).show();
            visibleBotoes();
            messageBox.setText("1;1;10000.");

            // HABILITA BOTOES

            return true;
        }
        if (id == R.id.action_user) {
            msgMenu = "Modo Usuário Ativado!";
            Toast.makeText(getApplicationContext(), msgMenu, Toast.LENGTH_LONG).show();
            invisibleBotoes();
            messageBox.setText("");
            return true;
        }
        sintezar(msgMenu);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                statusMessage.setText("Bluetooth ativado!");
            } else {
                statusMessage.setText("Bluetooth não ativado :(");
            }
        } else if (requestCode == SELECT_PAIRED_DEVICE || requestCode == SELECT_DISCOVERED_DEVICE) {
            if (resultCode == RESULT_OK) {
               /* statusMessage.setText("Você selecionou " + data.getStringExtra("btDevName") + "\n"
                        + data.getStringExtra("btDevAddress"));*/

                statusMessage.setText("Você selecionou " + data.getStringExtra("btDevName"));


                connect = new ConnectionThread(data.getStringExtra("btDevAddress"));

                connect.start();
            } else {
                statusMessage.setText("Nenhum dispositivo selecionado :(");
            }
        }
        sintezar(statusMessage.getText() + "");

    }


    public void searchPairedDevices(View view) {
        Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);


    }

    public void discoverDevices(View view) {

        Intent searchPairedDevicesIntent = new Intent(this, DiscoveredDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_DISCOVERED_DEVICE);
    }

    public void enableVisibility(View view) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30);
        startActivity(discoverableIntent);
    }

    public void waitConnection(View view) {
        connect = new ConnectionThread();
        connect.start();
    }

    public void sendMessage(View view) {
        EditText messageBox = (EditText) findViewById(R.id.editText_MessageBox);
        String messageBoxString = messageBox.getText().toString();
        byte[] data = messageBoxString.getBytes();
        connect.write(data);
        if (messageBoxString.equals("")) {

        } else {
            textSpace.setText("Enviado: " + messageBox.getText() + "\n    ");
            messageBox.setText("");
        }
    }

    public static void habilitaBotoes() {
        btSend.setEnabled(true);
        messageBox.setEnabled(true);
    }


    /**
     * METODO QUE BUSCA NO BANCO E RETORNA OS DADOS
     *
     * @param id ID DE CONSULTA NO BANCO (IDENTIFICADOR UNICO DO ARDUINO)
     * @return RETORNA OS DADOS DO BANCO PARA SINTETIZAR
     */
    public String buscarDb(String id) {
        String dados = "";

        int filtroSql = 0;
        try {
            filtroSql = Integer.parseInt(id);
        } catch (Exception e) {
            filtroSql = 0;
        }

        /**
         * EFETUA A BUSCA CONFORME O RETORNO DO MODAL   */
        Transmissor retorno = null;

        try {
            retorno = dao.buscarTransmissorPorId(filtroSql);
        } catch (Exception e) {
            textSpace.setText(MSG_DB_0001 + " Ocorreu um erro ao consutar no base de dados");
            Log.e(MSG_DB_0001, "Ocorreu um erro ao consutar no base de dados:" + e.getMessage());

        }

        if (retorno == null) {
            textSpace.setText(MSG_DB_0001 + " Atenção! registro não localizado");
            Log.i(MSG_DB_0001, "Atenção! registro não localizado");
            dados = null;
        } else {
            dados = "Conectado ao semáforo: " + retorno.getDevName() + " localizado na " + "\n" + retorno.getRua()
                    // + "\n" + retorno.getComplemento()
                    + "\n" + retorno.getCruzamento();

            textSpace.setText(dados);
        }

        return (dados);

    }

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            MainBluetoothActivity sintetize = new MainBluetoothActivity();

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date hora = Calendar.getInstance().getTime(); // Ou qualquer outra forma que tem
            String dataFormatada = sdf.format(hora);


            Bundle bundle = msg.getData();
            byte[] data = bundle.getByteArray("data");
            String dataString = new String(data);
            String dados = new String(data);

            if (dataString == null) {
                statusMessage.setText("Data String esta vazio");
                sintetize.sintezar("Data String esta vazio");
            }
            if (dataString.equals("---N")) {

                statusMessage.setText("Atenção! Ocorreu um erro durante a conexão.");

                sintetize.sintezar("Atenção! Ocorreu um erro durante a conexão.");


            } else if (dataString.equals("---S")) {
                statusMessage.setText("Conectado :D");
                // sintetize.sintezar("conectado!");

                habilitaBotoes();
            } else {

                // InterpretaDados.lerDados(dados);
                String[] arrayDeDados;
                /** POSIÇÕES DA LISTA
                 * POSIÇÃO 0 ID (iDENTIFICADOR DO ARDUINO)
                 * POSIÇÃO 1 STATUS SITUAÇÃO DO SEMÁFORO 0- SINAL VERDE PARA O PEDRESTRE 1- VERMELHO PARA O PEDESTRE
                 * POSIÇÃO 2 TIMER TIMER DO STATUS
                 */

                // PEGA APENAS APENAS O PRIMEIRO CONJUNTO DE BITS SENÃO NO ARDUINO NÃO RODA!!
                String info = "";
                int position = dados.lastIndexOf(".");
                int inicio = position - 9;
                int fim = position;
                if (position == -1) {

                }

                if (position > 8) {
                    info = dados.substring(inicio, fim);

                    // REMOVE O PONTO FINAL UTILIZADO COMO DELIMITADOR
                    info = info.replace(".", "");

                    List<String> list = Arrays.asList(info.split(";"));

                    arrayDeDados = new String[3];
                    int count = 0;
                    if (!info.equals("")) {

                        for (String letra : list) {
                            try {
                                arrayDeDados[count] = letra;
                                ++count;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    Integer timer = 0;

                    try {
                        timer = Integer.parseInt(arrayDeDados[2]);
                    } catch (Exception ex) {
                        timer = 0;
                    }
                    try {

                        /** IDENTIFICA O ARDUINDO COM BASE NO ID RECEBIDO **/
                        // if (arrayDeDados[0].equals("1")) {
                        String arduinomsg = "";//"Dados Recebidos!";
                        textSpace.append(arduinomsg + "\n");
                        if (sintetize.buscarDb(arrayDeDados[0]) == null) {
                            arduinomsg = arduinomsg + " Semáforo não encontrado!";

                        }


                        // sintetize.sintezar("Dados Recebidos do arduino nº: " + arrayDeDados[0] + "\n");

                        //  } else {
                        //      textSpace.append("Arduino  não encontrado" + "\n");
                        //      sintetize.sintezar("Arduino  não encontrado");
                        //  }
                        String situacaoSinal = "";

                        /** CASO O SINAL ESTEJA ABERTO OU
                         * O TIMER MINIMO NÃO SEJA EXCEDIDO ALERTA AO USUÁRIO
                         * QUE O SINAL ESTÁ VERDES **/
                        if (arrayDeDados[1].equals("0") & timer > 5000) {
                            situacaoSinal = "Siga em frente, sinal aberto para os pedestres";
                            textSpace.append(arduinomsg + situacaoSinal + "\n");
                            sintetize.sintezar(arduinomsg + situacaoSinal);
                        } else {
                            situacaoSinal = "Pare! Sinal fechado para os pedestres.";
                            if (sintetize.buscarDb(arrayDeDados[0]) == null) {
                                sintetize.sintezar(arduinomsg + situacaoSinal);
                                textSpace.append(arduinomsg + situacaoSinal);
                            } else {
                                sintetize.sintezar(arduinomsg + situacaoSinal + sintetize.buscarDb(arrayDeDados[0]));
                                textSpace.append(arduinomsg + situacaoSinal + sintetize.buscarDb(arrayDeDados[0]));
                            }

                        }

                        // textSpace.append(dados);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("POSICAO ENCONTRADA", "Erro a posição encontrada do delimitador '.' foi a: " + position);

                }

            }

        }

    };


    public void Bt_clear(View view) {
        textSpace.setText("");
    }


    String m_Text = "";

    public void bt_Buscar(View view) {


        /**
         * CRIA E CHAMA O MODAL DE IMPUT
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informe o ID da busca");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                sintezar(buscarDb(m_Text));

            }
        })
        ;
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    /**
     * METODO PARA MEDIR A POTÊNCIA DO SINAL
     * FONTE: http://abreai.net/4BFdC
     */
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                Toast.makeText(getApplicationContext(), " RSSI: " + rssi + "dBm", Toast.LENGTH_SHORT).show();
            }
        }
    };


}