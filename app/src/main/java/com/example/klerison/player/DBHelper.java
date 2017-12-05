package com.example.klerison.player;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {

    private static final String NOME_BASE = "MinhaMusica";
    private static final int VERSAO_BASE = 1;


    public DBHelper(Context context) {
        super(context, NOME_BASE, null, VERSAO_BASE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateTabelaMusica = "CREATE TABLE musica(" +
                "id INTEGER PRIMARY KEY)";
        String sqlCreateTabelaLegenda = "CREATE TABLE legenda(" +
                "id INTEGER PRIMARY KEY," +
                "texto VARCHAR)";
        db.execSQL(sqlCreateTabelaMusica);
        db.execSQL(sqlCreateTabelaLegenda);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sqlDropTabelaMusica = "DROP TABLE musica";
        db.execSQL(sqlDropTabelaMusica);
        String sqlDropTabelaLegenda = "DROP TABLE legenda";
        db.execSQL(sqlDropTabelaLegenda);

        onCreate(db);
    }

    public void insertMusica(Musica musica){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("id",musica.getId());

        db.insert("musica",null,cv );

        db.close();
    }

    public List<Musica> selectTodasAsMusicas(){
        List<Musica> listMusica = new ArrayList<Musica>();

        SQLiteDatabase db = getReadableDatabase();

        String sqlSelectTodasMusicas = "SELECT * FROM musica";

        Cursor c = db.rawQuery(sqlSelectTodasMusicas,null);

        if(c.moveToFirst()){
            do{
            Musica musica = new Musica();
            musica.setId(c.getInt(0));
            listMusica.add(musica);
            }while(c.moveToNext());
        }
        db.close();
        return listMusica;
    }
    public void insertLegenda(Legenda legenda){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("id",legenda.getId());
        cv.put("texto",legenda.getTexto());

        db.insert("legenda",null,cv );


        db.close();
    }

    public List<Legenda> selectTodasAsLegendas(){
        List<Legenda> listLegenda = new ArrayList<Legenda>();

        SQLiteDatabase db = getReadableDatabase();

        String sqlSelectTodasLegendas = "SELECT * FROM legenda";

        Cursor c = db.rawQuery(sqlSelectTodasLegendas,null);

        if(c.moveToFirst()){
            do{
                Legenda legenda = new Legenda();
                legenda.setId(c.getInt(0));
                legenda.setTexto(c.getString(1));

                listLegenda.add(legenda);
            }while(c.moveToNext());
        }
        db.close();
        return listLegenda;
    }
}
