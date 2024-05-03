package com.ugb.controlesbasicos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class interfaz_principal extends Activity {

    Button btnimgProdN, btnimgUsuariochat, btnimgVerProd,imgUsuarioNuevo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interfaz_principal);

        // Inicializar los botones
        btnimgProdN = findViewById(R.id.btnimgProdN);
        btnimgUsuariochat = findViewById(R.id.btnimgUsuariochat);
        btnimgVerProd = findViewById(R.id.btnimgVerProd);
        imgUsuarioNuevo = findViewById(R.id.imgUsuarioNuevo);

        // Configurar los OnClickListener para cada botón
        btnimgProdN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnimgProdN();
            }
        });

        btnimgVerProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnverproducto();
            }
        });
        btnimgUsuariochat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {btnimgUsuariochat();

            }
        });

    }

    private void btnimgProdN(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Cierra la actividad actual
    }

    private void btnverproducto(){
        Intent intent = new Intent(this, lista_productos.class);
        startActivity(intent);
        finish(); // Cierra la actividad actual
    }

    private void btnimgUsuariochat(){
        Intent intent = new Intent(this, chats.class);
        startActivity(intent);
        finish(); // Cierra la actividad actual
    }

}