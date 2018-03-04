package com.agateownz.testapplication.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.agateownz.testapplication.SQLiteDatabaseHelper;
import com.agateownz.testapplication.model.Pessoa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luisg on 04/03/2018.
 */

public class PessoaRepository implements IRepository<Pessoa, Long> {
    private static final String TABLE_NAME = "pessoa";
    private static final String KEY_ID = "id";
    private static final String KEY_NOME = "nome";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_TELEFONE = "telefone";
    private static final String KEY_ENDERECO = "endereco";
    private static final String KEY_OBSERVACAO = "observacoes";
    private static final String[] COLUMNS = {
        KEY_ID, KEY_NOME, KEY_EMAIL, KEY_TELEFONE, KEY_ENDERECO, KEY_OBSERVACAO
    };

    private SQLiteDatabaseHelper dbHelper;

    public PessoaRepository(Context context) {
        dbHelper = new SQLiteDatabaseHelper(context);
    }

    @Override
    public void saveOrUpdate(Pessoa object) {
        if (object.getId() == -1) {
            save(object);
        } else {
            update(object);
        }
    }


    private void update(Pessoa pessoa) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOME, pessoa.getNome());
        values.put(KEY_EMAIL, pessoa.getEmail());
        values.put(KEY_ENDERECO, pessoa.getEndereco());
        values.put(KEY_TELEFONE, pessoa.getTelefone());
        values.put(KEY_OBSERVACAO, pessoa.getObservacao());
        db.update(TABLE_NAME, values, "id = ?", new String[] { String.valueOf(pessoa.getId())});
        db.close();
    }

    private void save(Pessoa pessoa) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOME, pessoa.getNome());
        values.put(KEY_EMAIL, pessoa.getEmail());
        values.put(KEY_ENDERECO, pessoa.getEndereco());
        values.put(KEY_TELEFONE, pessoa.getTelefone());
        values.put(KEY_OBSERVACAO, pessoa.getObservacao());
        long entityId = db.insert(TABLE_NAME, null, values);
        pessoa.setId(entityId);
        db.close();
    }

    @Override
    public void delete(Pessoa object) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[] { String.valueOf(object.getId()) });
        db.close();
    }

    @Override
    public Pessoa getById(Long id) {
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        Pessoa pessoa = null;
        Cursor cursor = db.query(
                TABLE_NAME,
                COLUMNS,
                " id = ?",
                new String[] { String.valueOf(id)},
                null,
                null,
                null,
                null);

        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            pessoa = new Pessoa();
            pessoa.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_ID))));
            pessoa.setNome(cursor.getString(cursor.getColumnIndex(KEY_NOME)));
            pessoa.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
            pessoa.setTelefone(cursor.getString(cursor.getColumnIndex(KEY_TELEFONE)));
            pessoa.setEndereco(cursor.getString(cursor.getColumnIndex(KEY_ENDERECO)));
            pessoa.setObservacao(cursor.getString(cursor.getColumnIndex(KEY_OBSERVACAO)));
        }
        cursor.close();
        db.close();
        return pessoa;
    }

    @Override
    public List<Pessoa> getAll() {
        List<Pessoa> pessoas = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Pessoa pessoa = null;

        if (cursor.moveToFirst()) {
            do {
                pessoa = new Pessoa();
                pessoa.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                pessoa.setNome(cursor.getString(cursor.getColumnIndex(KEY_NOME)));
                pessoa.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
                pessoa.setTelefone(cursor.getString(cursor.getColumnIndex(KEY_TELEFONE)));
                pessoa.setEndereco(cursor.getString(cursor.getColumnIndex(KEY_ENDERECO)));
                pessoa.setObservacao(cursor.getString(cursor.getColumnIndex(KEY_OBSERVACAO)));
                pessoas.add(pessoa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return pessoas;
    }
}