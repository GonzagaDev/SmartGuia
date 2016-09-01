package auditsolution.com.br.smartguiabluetooth.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import auditsolution.com.br.smartguiabluetooth.model.Usuario;


/**
 *alterado
 */
public class UsuarioDAO {
    private DatabaseHelperExample databaseHelperExample;
    private SQLiteDatabase database;

    public UsuarioDAO(Context context) {
        databaseHelperExample = new DatabaseHelperExample(context);
    }

    private SQLiteDatabase getDatabase() {
        if (database == null) {
            database = databaseHelperExample.getWritableDatabase();
        }
        return database;
    }

    private Usuario criarUsuario(Cursor cursor) {
        Usuario model = new Usuario(
                cursor.getInt(cursor.getColumnIndex(DatabaseHelperExample.Usuarios._ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelperExample.Usuarios.NOME)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelperExample.Usuarios.LOGIN)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelperExample.Usuarios.SENHA)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelperExample.Usuarios.CREATED_AT))
        );

        return model;
    }

    public List<Usuario> listarUsuarios() {
        Cursor cursor = getDatabase().query(DatabaseHelperExample.Usuarios.TABELA,
                DatabaseHelperExample.Usuarios.COLUNAS, null, null, null, null, null);

        List<Usuario> usuarios = new ArrayList<Usuario>();
        while (cursor.moveToNext()) {
            Usuario model = criarUsuario(cursor);
            usuarios.add(model);
        }
        cursor.close();
        return usuarios;
    }

    public long salvarUsuario(Usuario usuario) {
        ContentValues valores = new ContentValues();
        valores.put(DatabaseHelperExample.Usuarios.NOME, usuario.getNome());
        valores.put(DatabaseHelperExample.Usuarios.LOGIN, usuario.getLogin());
        valores.put(DatabaseHelperExample.Usuarios.SENHA, usuario.getSenha());
        valores.put(DatabaseHelperExample.Usuarios.CREATED_AT, usuario.getCreated_at());

        if (usuario.get_id() != null) {
            return getDatabase().update(DatabaseHelperExample.Usuarios.TABELA, valores, "_id = ?", new String[]{usuario.get_id().toString().toString()});
        }
        return getDatabase().insert(DatabaseHelperExample.Usuarios.TABELA, null, valores);
    }

    public boolean removerUsuario(int id) {
        return getDatabase().delete(DatabaseHelperExample.Usuarios.TABELA, "_id = ?", new String[]{Integer.toString(id)}) > 0;
    }

    public Usuario buscarUsuarioPorId(int id) {
        Cursor cursor = getDatabase().query(DatabaseHelperExample.Usuarios.TABELA, DatabaseHelperExample.Usuarios.COLUNAS, "_id = ?", new String[]{Integer.toString(id)}, null, null, null);

        if (cursor.moveToNext()) {
            Usuario model = criarUsuario(cursor);
            cursor.close();
            return model;
        }

        return null;
    }

    public boolean logar(String usuario, String senha) {
        Cursor cursor = getDatabase().query(DatabaseHelperExample.Usuarios.TABELA, null, "LOGIN = ? AND SENHA = ?", new String[]{usuario, senha}, null, null, null);

        return cursor.moveToNext();
    }

    public void fechar() {
        databaseHelperExample.close();
        database = null;
    }
}
