package auditsolution.com.br.smartguia.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public class BancoController {

        private SQLiteDatabase db;
        private CriaBanco banco;

        public BancoController(Context context) {
            banco = new CriaBanco(context);
        }

        public String insereDado(String titulo, String autor, String editora) {
            ContentValues valores;
            long resultado;

            db = banco.getWritableDatabase();
            valores = new ContentValues();
            valores.put(CriaBanco.MAC, titulo);
            valores.put(CriaBanco.NREFERENCIA, autor);
            valores.put(CriaBanco.ENDERECO, editora);

            resultado = db.insert(CriaBanco.TABELA, null, valores);
            db.close();

            if (resultado == -1)
                return "Erro ao inserir registro";
            else
                return "Registro Inserido com sucesso";

        }
        public Cursor carregaDados(){
            Cursor cursor;
            String[] campos =  {banco.ID,banco.NREFERENCIA};
            db = banco.getReadableDatabase();
            cursor = db.query(banco.TABELA, campos, null, null, null, null, null, null);

            if(cursor!=null){
                cursor.moveToFirst();
            }
            db.close();
            return cursor;
        }
    }

}

