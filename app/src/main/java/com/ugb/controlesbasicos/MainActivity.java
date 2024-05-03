package com.ugb.controlesbasicos;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    Button btn;
    FloatingActionButton fab;
    TextView tempVal;
    String accion = "nuevo";
    String id="", rev="", idProducto="";
    String urlCompletaFoto;
    String getUrlCompletaFotoFirestore;
    Intent tomarFotoIntent;
    ImageView img;
    utilidades utls;
    detectarInternet di;
    String miToken = "";
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase App Check
        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());

        di = new detectarInternet(getApplicationContext());
        utls = new utilidades();
        fab = findViewById(R.id.fabListarProductos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActividad();
            }
        });
        btn = findViewById(R.id.btnGuardarAgendaAmigos);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subirFotoFirestore();
            }
        });
        img = findViewById(R.id.btnImgProducto);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFotoAmigo();
            }
        });
        obtenerToken();
        mostrarDatosProductos();
    }
    private void subirFotoFirestore(){
        mostrarMsg("Subiendo Foto...");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        Uri file = Uri.fromFile(new File(urlCompletaFoto));
        final StorageReference reference = storageReference.child("foto/"+file.getLastPathSegment());

        final UploadTask tareaSubir = reference.putFile(file);
        tareaSubir.addOnFailureListener(e->{
            mostrarMsg("Error al subir la foto: "+ e.getMessage());
        });
        tareaSubir.addOnSuccessListener(tareaInstantanea->{
            mostrarMsg("Foto subida con exito.");
            Task<Uri> descargarUri = tareaSubir.continueWithTask(tarea->reference.getDownloadUrl()).addOnCompleteListener(tarea->{
                if( tarea.isSuccessful() ){
                    getUrlCompletaFotoFirestore = tarea.getResult().toString();
                    guardarAmigo();
                }else{
                    mostrarMsg("Error al descargar la ruta de la imagen");
                }
            });
        });
    }
    //revisar los tokens
    private void obtenerToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("Firebase", "Fetching FCM registration token failed", task.getException());
                    return;
                }

                // Get new FCM registration token
                String token = task.getResult();
                if (token != null) {
                    Log.d("Firebase", "Token: " + token);
                    miToken = token;
                } else {
                    Log.w("Firebase", "Token is null");
                }
            }
        });
    }
    private void guardarAmigo(){
        try {
            tempVal = findViewById(R.id.txtMarca);
            String marca = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtDescripcion);
            String descripcion = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtPresentacion);
            String presentacion = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtstock);
            String stock = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtPrecio);
            String precio= tempVal.getText().toString();

            tempVal = findViewById(R.id.txtcosto);
            String costo = tempVal.getText().toString();

            databaseReference = FirebaseDatabase.getInstance().getReference("productos");
            String key = databaseReference.push().getKey();

            if(miToken.equals("") || miToken==null){
                obtenerToken();
            }
            if( miToken!=null && miToken!="" ){
                productos producto = new productos(idProducto,marca,descripcion,presentacion,stock,precio,costo,urlCompletaFoto,getUrlCompletaFotoFirestore,miToken);
                if(key!=null){
                    databaseReference.child(key).setValue(producto).addOnSuccessListener(aVoid->{
                        mostrarMsg("producto registrado con exito.");
                        abrirActividad();
                    });
                }else{
                    mostrarMsg("Error nose pudo guardar en la base de datos");
                }
            }else {
                mostrarMsg("Tu dispositivo no soporta la aplicacion");
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void tomarFotoAmigo(){
        tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fotoProducto = null;
        try{
            fotoProducto= crearImagenAmigo();
            Uri uriFotoamigo = FileProvider.getUriForFile(MainActivity.this,
                    "com.ugb.controlesbasicos.fileprovider", fotoProducto);
            tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotoamigo);
            startActivityForResult(tomarFotoIntent, 1);
        }catch (Exception e){
            mostrarMsg("Error al abrir la camara: "+ e.getMessage());
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(requestCode==1 && resultCode==RESULT_OK){
                Bitmap imageBitmap = BitmapFactory.decodeFile(urlCompletaFoto);
                img.setImageBitmap(imageBitmap);
            }else{
                mostrarMsg("El usuario cancelo la toma de la foto");
            }
        }catch (Exception e){
            mostrarMsg("Error al obtener la foto de la camara");
        }
    }
    private File crearImagenAmigo() throws Exception{
        String fechaHoraMs = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),
                fileName = "imagen_"+ fechaHoraMs +"_";
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if( dirAlmacenamiento.exists()==false ){
            dirAlmacenamiento.mkdirs();
        }
        File imagen = File.createTempFile(fileName, ".jpg", dirAlmacenamiento);
        urlCompletaFoto = imagen.getAbsolutePath();
        return imagen;
    }
    private void mostrarDatosProductos(){
        try{
            Bundle parametros = getIntent().getExtras();//Recibir los parametros...
            accion = parametros.getString("accion");

            if(accion.equals("modificar")){
                JSONObject jsonObject = new JSONObject(parametros.getString("productos")).getJSONObject("value");
                id = jsonObject.getString("_id");
                rev = jsonObject.getString("_rev");
                idProducto = jsonObject.getString("idproducto");

                tempVal = findViewById(R.id.txtMarca);
                tempVal.setText(jsonObject.getString("marca"));

                tempVal = findViewById(R.id.txtDescripcion);
                tempVal.setText(jsonObject.getString("descripcion"));

                tempVal = findViewById(R.id.txtPresentacion);
                tempVal.setText(jsonObject.getString("presentacion"));

                tempVal = findViewById(R.id.txtstock);
                tempVal.setText(jsonObject.getString("stock"));

                tempVal = findViewById(R.id.txtPrecio);
                tempVal.setText(jsonObject.getString("precio"));

                tempVal = findViewById(R.id.txtcosto);
                tempVal.setText(jsonObject.getString("costo"));

                urlCompletaFoto = jsonObject.getString("urlCompletaFoto");
                Bitmap imageBitmap = BitmapFactory.decodeFile(urlCompletaFoto);
                img.setImageBitmap(imageBitmap);
            }else{//nuevo registro
                idProducto = utls.generarIdUnico();
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar datos: "+ e.getMessage());
        }
    }
    private void mostrarMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    private void abrirActividad(){
        Intent abrirActividad = new Intent(getApplicationContext(), interfaz_principal.class);
        startActivity(abrirActividad);
    }
}