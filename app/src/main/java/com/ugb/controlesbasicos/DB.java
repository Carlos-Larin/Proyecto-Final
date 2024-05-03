package com.ugb.controlesbasicos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    private static final String dbname = "productos";
    private static final int v =1;
    private static final String SQLdb = "CREATE TABLE productos( idProducto text, " +
            "marca text, descripcion text, presentacion text, stock text,precio text, foto text, actualizado text)";
    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbname, factory, v);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQLdb);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //cambiar estructura de la BD
    }
    public String administrar_productos(String accion, String[] datos){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "";
            switch (accion) {
                case "nuevo":
                    sql = "INSERT INTO productos(idProducto,marca,descripcion,presentacion,stock,precio,foto,actualizado) VALUES('" + datos[0] + "','" + datos[1] + "','" + datos[2] + "', '" +
                            datos[3] + "', '" + datos[4] + "','" + datos[5] + "','" + datos[6] + "', '" + datos[7] + "' )";
                    break;
                case "modificar":
                    sql = "UPDATE productos SET marca='" + datos[1] + "', descripcion='" + datos[2] + "', presentacion='" + datos[3] + "', stock=" +
                            "'" + datos[4] + "', precio='" + datos[5] + "', foto='" + datos[6] + "', actualizado='" + datos[7] + "' WHERE idProducto='" + datos[0] + "'";
                    break;
                case "eliminar":
                    sql = "DELETE FROM productos WHERE idProducto='" + datos[0] + "'";
                    break;
            }
            db.execSQL(sql);
            return "ok";
        }catch (Exception e){
            return e.getMessage();
        }
    }
    public Cursor obtener_productos(){
        Cursor cursor;
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM productos ORDER BY marca", null);
        return cursor;
    }
    public Cursor pendientesActualizar(){
        Cursor cursor;
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM productos WHERE actualizado='no'", null);
        return cursor;
    }
}