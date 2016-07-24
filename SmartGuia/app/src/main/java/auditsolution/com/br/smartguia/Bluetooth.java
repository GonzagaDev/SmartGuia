package auditsolution.com.br.smartguia;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by Cristiano Gonzaga on 23/07/2016.
 */
public class Bluetooth {
    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    /****
     * VERIFICA SE O DISPOSITIVO SUPORTA BLUETOOTH
     ****/

    public boolean verificaSeSuportaBluetooth() {
        if (btAdapter == null) {
            return (false);
        } else {
            return (true);
        }
    }

    /***
     * CASO O BLUETOOTH NÃO ESTEJA ATIVO SOLICITA SUA ATIVAÇÃO
     ***/
    public boolean VerificaSeOBluetoothEstaAtivo() {
        if (!btAdapter.isEnabled()) {

            return false;
        } else {
            return true;
        }
    }
    /**** VERIFICA SE O USUÁRIO ATIVOU O BLUETOOTH CONFORME SOLICITAÇÃO *****/

    /**********************************************************************/
}


