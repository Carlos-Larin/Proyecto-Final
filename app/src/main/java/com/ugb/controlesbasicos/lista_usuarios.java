package com.ugb.controlesbasicos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class lista_usuarios extends AppCompatActivity {

    FloatingActionButton fab;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);
        fab = findViewById(R.id.fabAgregarUsuarios);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActividad();
            }
        });
    }

    private void abrirActividad(){
       Intent abrirActividad = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(abrirActividad);
    }
}