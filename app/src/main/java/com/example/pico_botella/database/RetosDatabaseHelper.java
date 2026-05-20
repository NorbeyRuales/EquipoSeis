package com.example.pico_botella.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.pico_botella.model.Reto;
import java.util.ArrayList;
import java.util.List;

public class RetosDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "retos_db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_RETOS = "retos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DESCRIPCION = "descripcion";

    public RetosDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_RETOS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DESCRIPCION + " TEXT NOT NULL" + ")";
        db.execSQL(CREATE_TABLE);
        
        // Retos por defecto
        db.execSQL("INSERT INTO " + TABLE_RETOS + " (" + COLUMN_DESCRIPCION + ") VALUES ('Beso al de la derecha')");
        db.execSQL("INSERT INTO " + TABLE_RETOS + " (" + COLUMN_DESCRIPCION + ") VALUES ('Toma un shot')");
        db.execSQL("INSERT INTO " + TABLE_RETOS + " (" + COLUMN_DESCRIPCION + ") VALUES ('Baila sin música')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RETOS);
        onCreate(db);
    }

    public long agregarReto(Reto reto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPCION, reto.getDescripcion());
        long id = db.insert(TABLE_RETOS, null, values);
        db.close();
        return id;
    }

    public List<Reto> obtenerRetos() {
        List<Reto> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Criterio 6: ORDER BY id DESC para que el nuevo reto aparezca arriba
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RETOS + " ORDER BY " + COLUMN_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Reto reto = new Reto(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION))
                );
                lista.add(reto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    public int actualizarReto(Reto reto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPCION, reto.getDescripcion());
        return db.update(TABLE_RETOS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(reto.getId())});
    }

    public void eliminarReto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RETOS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
