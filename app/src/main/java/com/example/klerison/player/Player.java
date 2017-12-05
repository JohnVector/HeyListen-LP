package com.example.klerison.player;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Player implements View.OnClickListener {
    static MediaPlayer mp;
    ArrayList<File> mySongs;
    int minutes=0, seconds=0, tminutes=0, tseconds=0;
    static int position;
    SeekBar sb;
    Button btPlay, btFF, btFB, btNxt, btPv, btnAdicionarLegenda;
    Uri u;
    Thread updateSeekBar;
    TextView txtTime, txtTotalTime, txtName, txtTracinho, Legenda;
    String zeroSeconds, zeroMinutes, zeroTSeconds, zeroTMinutes;
    int dois;
    String str="";
    DBHelper dbHelper;
    List<Legenda> listaLegendas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btPlay = (Button) findViewById(R.id.btPlay);
        btFF = (Button) findViewById(R.id.btFF);
        btFB = (Button) findViewById(R.id.btFB);
        btNxt = (Button) findViewById(R.id.btNxt);
        btPv = (Button) findViewById(R.id.btPv);
        btnAdicionarLegenda = (Button) findViewById(R.id.btnAdicionarLegenda);

        txtTime = (TextView) findViewById(R.id.txtTime);
        txtTotalTime = (TextView) findViewById(R.id.txtTotalTime);
        txtName = (TextView) findViewById(R.id.txtName);
        txtTracinho = (TextView) findViewById(R.id.txtTracinho);
        Legenda = (TextView) findViewById(R.id.Legenda);
        txtTracinho.setText("-");

        btPlay.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btFB.setOnClickListener(this);
        btNxt.setOnClickListener(this);
        btPv.setOnClickListener(this);

        sb = (SeekBar) findViewById(R.id.seekBar);
        updateSeekBar = new Thread(){
            @Override
            public void run(){
                int totalDuration = mp.getDuration();
                int currentPosition= 0;
                while (currentPosition < totalDuration){
                    try{
                        sleep(500);
                        currentPosition = mp.getCurrentPosition();
                        sb.setProgress(currentPosition);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    //super.run();
                }
            }
        };


        if(mp!=null){
            mp.stop();
            mp.release();
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songlist");
        position = b.getInt("pos",0);




        u = Uri.parse( mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(),u);

        dbHelper = new DBHelper(this);

        listaLegendas = dbHelper.selectTodasAsLegendas();

            for (Legenda l : listaLegendas) {
                //esse l.getId() sempre é 0
                if ((l.getId())== position) {
                    str = l.getTexto();
                    break;
                }

            }
        Legenda.setText(str);
        Legenda.setMovementMethod(new ScrollingMovementMethod());

        dois = mySongs.get(position).toString().lastIndexOf("/");
        txtName.setText(mySongs.get(position).toString().substring(dois));

        mp.start();
        sb.setMax(mp.getDuration());

        tseconds = mp.getDuration()/1000;
        tminutes = tseconds / 60;
        tseconds = tseconds % 60;
        if(tseconds<10)
            zeroTSeconds = "0";
        else
            zeroTSeconds = "";

        if(tminutes<10)
            zeroTMinutes = "0";
        else
            zeroTMinutes = "";
        txtTotalTime.setText(zeroTMinutes+tminutes+":"+zeroTSeconds+tseconds);

        updateSeekBar.start();


        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seconds = i/1000;
                minutes = seconds / 60;
                seconds = seconds % 60;

                if(seconds<10)
                    zeroSeconds = "0";
                else
                    zeroSeconds = "";

                if(minutes<10)
                    zeroMinutes = "0";
                else
                    zeroMinutes = "";


                txtTime.setText(zeroMinutes+minutes+":"+zeroSeconds+seconds);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });
        btnAdicionarLegenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AdicionarLegenda.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        switch(id){
            case R.id.btPlay:
                if(mp.isPlaying()){
                    mp.pause();
                }
                else mp.start();
                break;
            case R.id.btFF:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.btFB:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.btNxt:
                mp.stop();
                mp.release();
                position = (position+1)%mySongs.size();
                Uri u = Uri.parse( mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                tseconds = mp.getDuration()/1000;
                tminutes = tseconds / 60;
                tseconds = tseconds % 60;
                if(tseconds<10)
                    zeroTSeconds = "0";
                else
                    zeroTSeconds = "";

                if(tminutes<10)
                    zeroTMinutes = "0";
                else
                    zeroTMinutes = "";
                txtTotalTime.setText(zeroTMinutes+tminutes+":"+zeroTSeconds+tseconds);

                dois = mySongs.get(position).toString().lastIndexOf("/");
                txtName.setText(mySongs.get(position).toString().substring(dois));
                break;
            case R.id.btPv:
                mp.stop();
                mp.release();
                position = (position-1<0)? mySongs.size()-1 : position-1;
                /*if(position-1 < 0){
                    position = mySongs.size()-1;
                }else{
                    position = position - 1;
                }*/
                u = Uri.parse( mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                tseconds = mp.getDuration()/1000;
                tminutes = tseconds / 60;
                tseconds = tseconds % 60;
                if(tseconds<10)
                    zeroTSeconds = "0";
                else
                    zeroTSeconds = "";

                if(tminutes<10)
                    zeroTMinutes = "0";
                else
                    zeroTMinutes = "";
                txtTotalTime.setText(zeroTMinutes+tminutes+":"+zeroTSeconds+tseconds);

                dois = mySongs.get(position).toString().lastIndexOf("/");
                txtName.setText(mySongs.get(position).toString().substring(dois));
                break;
        }
    }

}
