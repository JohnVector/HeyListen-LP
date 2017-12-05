package com.example.klerison.player;

import android.app.AlertDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AdicionarLegenda {

    EditText txtLegenda;
    Button btnSalvar, btnCancelar;
    DBHelper dbHelper;
    Legenda legenda;
    Player pegaPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_legenda);

        txtLegenda = (EditText) findViewById(R.id.txtLegenda);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);

        txtLegenda.setMovementMethod(new ScrollingMovementMethod());

        pegaPosition = new Player();


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adicionar();
            }
        });
    }
    public void adicionar(){
        AlertDialog alerta = new AlertDialog().Builder(this).create();
        String str="";
        if(txtLegenda.getText().toString().equals("")) {
            alerta.setMessage("Nenhum texto adicionado");
            alerta.show();
        }else{
            str = txtLegenda.getText().toString();
            dbHelper = new DBHelper(this);
            legenda = new Legenda(pegaPosition.position, str);
            dbHelper.insertLegenda(legenda);
            alerta.setMessage("Legenda adicionada");
            alerta.show();
            finish();
        }
    }
}
