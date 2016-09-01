package auditsolution.com.br.smartguiabluetooth.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import auditsolution.com.br.smartguiabluetooth.model.Tarefa;


/**
 *
 */
public class TarefaDAO {
    private DatabaseHelperExample databaseHelperExample;
    private SQLiteDatabase database;

    public TarefaDAO(Context context) {
        databaseHelperExample = new DatabaseHelperExample(context);

    }

    private SQLiteDatabase getDatabase() {
        if (database == null) {
            database = databaseHelperExample.getWritableDatabase();
        }
        return database;
    }

    private Tarefa criarTarefa(Cursor cursor) {
        Tarefa model = new Tarefa(
                cursor.getInt(cursor.getColumnIndex(DatabaseHelperExample.Tarefas._ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelperExample.Tarefas.TAREFA)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelperExample.Tarefas.DT_CRIACAO)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelperExample.Tarefas.DT_COMPLETADO))
        );

        return model;
    }

    public List<Tarefa> listarTarefas() {
        Cursor cursor = getDatabase().query(DatabaseHelperExample.Tarefas.TABELA,
                DatabaseHelperExample.Tarefas.COLUNAS,  null,null, null, null, null);

        List<Tarefa> tarefas = new ArrayList<Tarefa>();
        while (cursor.moveToNext()) {
            Tarefa model = criarTarefa(cursor);
            tarefas.add(model);
        }
        cursor.close();
        return tarefas;
    }

    public long salvarTarefa(Tarefa model) {
        ContentValues valores = new ContentValues();
        valores.put(DatabaseHelperExample.Tarefas.TAREFA, model.getTarefa());

        if (model.get_id() != null) {
            return getDatabase().update(DatabaseHelperExample.Tarefas.TABELA, valores, "_id = ?", new String[]{model.get_id().toString().toString()});
        }
        return getDatabase().insert(DatabaseHelperExample.Tarefas.TABELA, null, valores);
    }

    public boolean removerTarefa(int id) {
        return getDatabase().delete(DatabaseHelperExample.Tarefas.TABELA, "_id = ?", new String[]{Integer.toString(id)}) > 0;
    }


    public Tarefa buscarTarefaPorId(int id) {
        Cursor cursor = getDatabase().query(DatabaseHelperExample.Tarefas.TABELA, DatabaseHelperExample.Tarefas.COLUNAS, "_id = ?", new String[]{Integer.toString(id)}, null, null, null);

        if (cursor.moveToNext()) {
            Tarefa model = criarTarefa(cursor);
            cursor.close();
            return model;
        }

        return null;
    }

    public void fechar() {
        databaseHelperExample.close();
        database = null;
    }

}
