package auditsolution.com.br.smartguiabluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
    static Button bt_buscar;
    static Button bt_wait;
    static Button bt_ficarVisivel;
    static MenuItem menuUser;
    static MenuItem menuTest;

    ConnectionThread connect;
    DaoTransmissor dao = new DaoTransmissor(this);

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
        bt_wait = (Button) findViewById(R.id.button_WaitConnection);
        bt_ficarVisivel = (Button) findViewById(R.id.button_Visibility);
        menuUser = (MenuItem) findViewById(R.id.action_user);
        menuTest = (MenuItem) findViewById(R.id.action_test);

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            statusMessage.setText("Que pena! Hardware Bluetooth não está funcionando :(");
        } else {
            statusMessage.setText("Ótimo! Hardware Bluetooth está funcionando :)");
            if (!btAdapter.isEnabled()) {
                /**
                 * SOLICITA QUE O BLUETOOTH SEJA HABILITADO
                 * Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                 startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH);
                 statusMessage.setText("Solicitando ativação do Bluetooth...");
                 */

                /**
                 * ATIVA O BLUETOOTH AUTOMATICAMENTE
                 */
                btAdapter.enable();
            } else {
                statusMessage.setText("Bluetooth já ativado :)");
            }
        }
        invisibleBotoes();
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
        if (id == R.id.action_test) {
            //AÇÃO AO CLICAR NO MENU
            Toast.makeText(getApplicationContext(), "Modo teste Ativado!", Toast.LENGTH_LONG).show();
            visibleBotoes();

            // HABILITA BOTOES

            return true;
        }
        if (id == R.id.action_user) {
            Toast.makeText(getApplicationContext(), "Modo Usuário Ativado!", Toast.LENGTH_LONG).show();
            invisibleBotoes();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                statusMessage.setText("Bluetooth ativado :D");
            } else {
                statusMessage.setText("Bluetooth não ativado :(");
            }
        } else if (requestCode == SELECT_PAIRED_DEVICE || requestCode == SELECT_DISCOVERED_DEVICE) {
            if (resultCode == RESULT_OK) {
                statusMessage.setText("Você selecionou " + data.getStringExtra("btDevName") + "\n"
                        + data.getStringExtra("btDevAddress"));

                connect = new ConnectionThread(data.getStringExtra("btDevAddress"));
                connect.start();
            } else {
                statusMessage.setText("Nenhum dispositivo selecionado :(");
            }
        }
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


    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date hora = Calendar.getInstance().getTime(); // Ou qualquer outra forma que tem
            String dataFormatada = sdf.format(hora);


            Bundle bundle = msg.getData();
            byte[] data = bundle.getByteArray("data");
            String dataString = new String(data);
            String dados = new String(data);

            if (dataString == null) {
                statusMessage.setText("Data String esta vazio");
            }
            if (dataString.equals("---N"))
                statusMessage.setText("Ocorreu um erro durante a conexão D:");
            else if (dataString.equals("---S")) {
                statusMessage.setText("Conectado :D");
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
                        if (arrayDeDados[0].equals("1")) {
                            textSpace.append("Dados Recebidos do arduino nº: " + arrayDeDados[0] + "\n");
                        } else {
                            textSpace.append("Arduino  não encontrado" + "\n");
                        }

                        /** CASO O SINAL ESTEJA ABERTO OU
                         * O TIMER MINIMO NÃO SEJA EXCEDIDO ALERTA AO USUÁRIO
                         * QUE O SINAL ESTÁ VERDES **/
                        if (arrayDeDados[1].equals("0") & timer > 5000) {
                            textSpace.append("Siga em frente sinal verde para os pedestres" + "\n");
                        } else {
                            textSpace.append("Pare! O sinal não esta verde para os pedestres" + "\n");
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

    public void bt_Buscar(View view) {
        Transmissor retorno = null;
        try {
            retorno = dao.buscarTransmissorPorId(1);
        } catch (Exception e) {
            Log.e("ERRO:", "Ocorreu um erro ao consutar no base de dados:" + e.getMessage());
        }

        if (retorno == null) {
            Log.i("RETORNO BANCO DE DADOS", "Atenção! registro não localizado");
        } else {
            textSpace.setText("O dispositivo encontrado no banco foi: " + retorno.getDevName());
        }

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