package com.example.klerison.player;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class teste {

    ListView lista;
    String[] items;
    ArrayAdapter<String> adp;
    ArrayList<File> mySongs;
    int i=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);


        lista = (ListView) findViewById(R.id.lista);

        mySongs = findSongs(Environment.getExternalStorageDirectory());


        DBHelper dbHelper = new DBHelper(this);

        List<Musica> listaMusicas = dbHelper.selectTodasAsMusicas();

        items = new String [ listaMusicas.size() ];


        for(Musica m : listaMusicas) {
                i++;
                items[i] = mySongs.get(m.getId()).getName().toString().replace(".mp3", "").replace(".wav", "");

        }

        adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.song_layout,R.id.textView, items);
        lista.setAdapter(adp);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), Player.class).putExtra("pos",position).putExtra("songlist", mySongs));
            }
        });

    }
    public ArrayList<File> findSongs(File root){
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        for(File singleFile: files) {
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                al.addAll(findSongs(singleFile));
            }
            else{
                if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                    al.add(singleFile);
                }
            }
        }
        return al;
    }

}
