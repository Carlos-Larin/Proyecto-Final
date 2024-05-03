package com.ugb.controlesbasicos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class usuarios extends AppCompatActivity {
    Button btn;
    TextView tempVal;
    DatabaseReference mDatabase;
    String miToken;
    FloatingActionButton fab;
    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        obtenerToken();

        fab = findViewById(R.id.fabguardarUsuarios);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActividad();
            }
        });

        fab = findViewById(R.id.fabinterfaz);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirInterfaz();
            }
        });

    }

    private void obtenerToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if( !task.isSuccessful() ){
                    return;
                }
                miToken = task.getResult();
                Toast.makeText(getApplicationContext(), miToken, Toast.LENGTH_LONG).show();

            }
        });
    }

    private void mostrarMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    private void abrirActividad(){
        Intent abrirActividad = new Intent(getApplicationContext(), lista_usuarios.class);
        startActivity(abrirActividad);
    }
    private void abrirInterfaz(){
        Intent abrirActividad = new Intent(getApplicationContext(), lista_usuarios.class);
        startActivity(abrirActividad);
    }
}