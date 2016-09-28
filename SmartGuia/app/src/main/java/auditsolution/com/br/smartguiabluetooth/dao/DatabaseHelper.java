package auditsolution.com.br.smartguiabluetooth.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Win10-Home on 10/08/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String BANCO_DADOS = "smartDB";
    private static final int VERSAO = 2;

    public DatabaseHelper(Context context) {
        super(context, BANCO_DADOS, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //CRIA TABELA TRASNMISSOR
        db.execSQL("create table transmissor(" +
                "_id integer primary key autoincrement, " +
                "serial int not null, " +
                "rua text not null," +
                "cruzamento text not null," +
                "complemento text not null," +
                "devName text not null," +
                "devAddress text not null)");

        //CRIA O REGISTRO DO ARDUÍNO Nº1
        db.execSQL("insert into transmissor(serial, rua, cruzamento, complemento, devName, devAddress) values (1, 'Rua Gonçalves Ledo','Semáforo 1'," +
                "'Cruzamento com Avenida Centenário','HC-05','20:16:04:18:29:29')");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static class Transmissores {
        public static final String TABELA = "transmissor";
        public static final String _ID = "_id";
        public static final String SERIAL = "serial";
        public static final String RUA = "rua";
        public static final String CRUZAMENTO = "cruzamento";
        public static final String COMPLEMENTO = "complemento";
        public static final String DEVNAME = "devName";
        public static final String DEVADDRESS = "devAddress";

        public static final String[] COLUNAS = new String[]{
                _ID, SERIAL, RUA, CRUZAMENTO, COMPLEMENTO, DEVNAME, DEVADDRESS
        };
    }
}
