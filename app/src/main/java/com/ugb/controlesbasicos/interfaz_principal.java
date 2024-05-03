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

    Button btnimgProdN, btnimgUsuario, btnimgVerProd,imgUsuarioNuevo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interfaz_principal);

        // Inicializar los botones
        btnimgProdN = findViewById(R.id.btnimgProdN);
        btnimgUsuario = findViewById(R.id.btnimgUsuario);
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
        btnimgUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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


}