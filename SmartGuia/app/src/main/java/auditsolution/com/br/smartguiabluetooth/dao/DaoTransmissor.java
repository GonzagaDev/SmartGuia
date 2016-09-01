package auditsolution.com.br.smartguiabluetooth.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import auditsolution.com.br.smartguiabluetooth.model.Tarefa;
import auditsolution.com.br.smartguiabluetooth.model.Transmissor;

/**
 * Created by Win10-Home on 10/08/2016.
 */
public class DaoTransmissor {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public DaoTransmissor(Context context) {
        databaseHelper = new DatabaseHelper(context);

    }

    private SQLiteDatabase getDatabase() {
        if (database == null) {
            database = databaseHelper.getWritableDatabase();
        }
        return database;
    }

    private Transmissor addTransmissor(Cursor cursor) {
        Transmissor model = new Transmissor(
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Transmissores._ID)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Transmissores.SERIAL)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Transmissores.RUA)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Transmissores.CRUZAMENTO)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Transmissores.COMPLEMENTO)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Transmissores.DEVNAME)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Transmissores.DEVADDRESS))
        );

        return model;
    }

    public List<Transmissor> listarTrasmissores() {
        Cursor cursor = getDatabase().query(DatabaseHelper.Transmissores.TABELA,
                DatabaseHelper.Transmissores.COLUNAS, null, null, null, null, null, null);


        List<Transmissor> transmissores = new ArrayList<Transmissor>();
        while (cursor.moveToNext()) {
            Transmissor model = addTransmissor(cursor);
            transmissores.add(model);
        }
        cursor.close();
        return transmissores;
    }

    public List<Transmissor> listarTarefas() {
        Cursor cursor = getDatabase().query(DatabaseHelperExample.Tarefas.TABELA,
                DatabaseHelperExample.Tarefas.COLUNAS, null, null, null, null, null);

        List<Transmissor> transmissores = new ArrayList<Transmissor>();
        while (cursor.moveToNext()) {
            Transmissor model = addTransmissor(cursor);
            transmissores.add(model);
        }
        cursor.close();
        return transmissores;
    }

    public long salvarTransmissor(Transmissor model) {
        ContentValues valores = new ContentValues();
        valores.put(DatabaseHelper.Transmissores.DEVADDRESS, model.getDevAddress());

        if (model.get_id() != null) {
            return getDatabase().update(DatabaseHelper.Transmissores.TABELA, valores, "_id = ?", new String[]{model.get_id().toString().toString()});
        }
        return getDatabase().insert(DatabaseHelper.Transmissores.TABELA, null, valores);
    }

    public boolean removerTransmissores(int id) {
        return getDatabase().delete(DatabaseHelper.Transmissores.TABELA, "_id = ?", new String[]{Integer.toString(id)}) > 0;
    }


    public Transmissor buscarTransmissorPorId(int id) {
        Cursor cursor = getDatabase().query(DatabaseHelper.Transmissores.TABELA, DatabaseHelper.Transmissores.COLUNAS, "_id = ?", new String[]{Integer.toString(id)}, null, null, null);

        if (cursor.moveToNext()) {
            Transmissor model = addTransmissor(cursor);
            cursor.close();
            return model;
        }

        return null;
    }

    public void fechar() {
        databaseHelper.close();
        database = null;
    }


}
