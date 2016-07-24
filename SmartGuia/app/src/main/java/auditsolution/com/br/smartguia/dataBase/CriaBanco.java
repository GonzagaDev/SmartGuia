package auditsolution.com.br.smartguia.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Win10-Home on 24/07/2016.
 */
public class CriaBanco extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "banco.db";
    private static final String TABELA = "serves";
    private static final String ID = "_id";
    private static final String MAC = "endereço Mac";
    private static final String NREFERENCIA = "nº de referencia";
    private static final String ENDERECO = "Rua";
    private static final int VERSAO = 1;

    public CriaBanco(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE" + TABELA + "("
                + ID + "integer primary key autoincrement,"
                + MAC + "text,"
                + NREFERENCIA + "text,"
                + ENDERECO + "text"
                + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABELA);
        onCreate(db);
    }
}

