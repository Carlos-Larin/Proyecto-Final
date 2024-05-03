package com.ugb.controlesbasicos;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends Activity {
    private EditText editUsuario;
    private EditText editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // buscamos
        editUsuario = findViewById(R.id.editUsuario);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        // cuando haga click
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String user = editUsuario.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Check if the username is "joyas" and the password is "12345"
        if ("joyas".equals(user) && "12345".equals(password)) {
            // Correct credentials, start the MainActivity
            Intent intent = new Intent(this, interfaz_principal.class);
            startActivity(intent);
            finish(); // Close the login activity
        } else {
            // Incorrect credentials, show an error message
            Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
        }
    }


}