package com.ugb.controlesbasicos;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class lista_productos extends AppCompatActivity {
    Bundle paramatros = new Bundle();
    DB db;
    ListView lts;
    //RecyclerView lts;
    //ViewPager lts;

    Cursor cProductos;
    final ArrayList<productos> alProductos = new ArrayList<>();
    final ArrayList<productos> alProductosCopy = new ArrayList<>();
    productos datosProductos;
    FloatingActionButton btn;
    JSONArray datosJSON; //para los datos que vienen del servidor.
    JSONObject jsonObject;
    // obtenerDatosServidor datosServidor;
    detectarInternet di;
    int posicion = 0;
    DatabaseReference databaseReference;
    String miToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_productos);
        lts = findViewById(R.id.ltsProductos);
        db = new DB(getApplicationContext(), "", null, 1);
        btn = findViewById(R.id.fabAgregarProductos);
        btn.setOnClickListener(view -> {
            paramatros.putString("accion", "nuevo");
            abrirActividad(paramatros);
        });

        di = new detectarInternet(getApplicationContext());
        if (di.hayConexionInternet()) { // online
            obtenerDatosProductosServidor();
            // sincronizar();
        } else { // offline
            mostrarMsg("No hay conexion, datos en local");
            obtenerProductos();
        }
        buscarProductos();
        mostrarChats();
    }
    private void mostrarChats(){
        lts.setOnItemClickListener((parent, view, position, id) -> {
            try{
                Bundle bundle = new Bundle();
                bundle.putString("marca", datosJSON.getJSONObject(position).getString("marca") );
                bundle.putString("to", datosJSON.getJSONObject(position).getString("to") );
                bundle.putString("from", datosJSON.getJSONObject(position).getString("from") );
                bundle.putString("urlCompletaFoto", datosJSON.getJSONObject(position).getString("urlCompletaFoto") );
                bundle.putString("urlFotoProductoFirestore", datosJSON.getJSONObject(position).getString("urlFotoProductoFirestore") );

                Intent intent = new Intent(getApplicationContext(), chats.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }catch (Exception ex){
                mostrarMsg(ex.getMessage());
            }
        });
    }


    private void sincronizar() {
        try {
            cProductos = db.pendientesActualizar();
            if (cProductos.moveToFirst()) { // hay registros pendientes de sincronizar con el servidor
                mostrarMsg("Sincronizando...");
                jsonObject = new JSONObject();

                do {
                    if (!cProductos.getString(0).isEmpty() && !cProductos.getString(1).isEmpty()) {
                        jsonObject.put("_id", cProductos.getString(0));
                        jsonObject.put("_rev", cProductos.getString(1));
                    }

                    jsonObject.put("idProducto", cProductos.getString(0));
                    jsonObject.put("marca", cProductos.getString(1));
                    jsonObject.put("descripcion", cProductos.getString(2));
                    jsonObject.put("presentacion", cProductos.getString(3));
                    jsonObject.put("stock", cProductos.getString(4));
                    jsonObject.put("precio", cProductos.getString(5));
                    jsonObject.put("urlCompletaFoto", cProductos.getString(6));
                    jsonObject.put("actualizado", "si");

                    enviarDatosServidor objGuardarDatosServidor = new enviarDatosServidor(getApplicationContext());
                    String respuesta = objGuardarDatosServidor.execute(jsonObject.toString()).get();

                    JSONObject respuestaJSONObject = new JSONObject(respuesta);
                    if (respuestaJSONObject.getBoolean("ok")) {
                        DB db = new DB(getApplicationContext(), "", null, 1);
                        String[] datos = new String[]{
                                jsonObject.getString("idProducto"),
                                jsonObject.getString("marca"),
                                jsonObject.getString("descripcion"),
                                jsonObject.getString("presentacion"),
                                jsonObject.getString("stock"),
                                jsonObject.getString("precio"),
                                jsonObject.getString("urlCompletaFoto"),
                                jsonObject.getString("actualizado")
                        };
                        respuesta = db.administrar_productos("modificar", datos);
                        if (!respuesta.equals("ok")) {
                            mostrarMsg("Error al guardar la actualizacion en local " + respuesta);
                        }
                    } else {
                        mostrarMsg("Error al sincronizar datos en el servidor " + respuesta);
                    }
                } while (cProductos.moveToNext());
                mostrarMsg("Sincronizacion completa.");
            }
        } catch (Exception e) {
            mostrarMsg("Error al sincronizar " + e.getMessage());
        }
    }

    private void obtenerDatosProductosServidor() {
        try {
            databaseReference = FirebaseDatabase.getInstance().getReference("productos");
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(tarea -> {
                if (!tarea.isSuccessful()) return;
                miToken = tarea.getResult();
                if (miToken != null && miToken.length() > 1) {
                    databaseReference.orderByChild("token").equalTo(miToken).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                if (snapshot.getChildrenCount() <= 0) {
                                    mostrarMsg("No estás registrado.");
                                    paramatros.putString("accion", "nuevo");
                                    abrirActividad(paramatros);
                                }
                            } catch (Exception e) {
                                mostrarMsg("Error al buscar nuestro registro: " + e.getMessage());
                                paramatros.putString("accion", "nuevo");
                                abrirActividad(paramatros);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            mostrarMsg("Error de base de datos: " + error.getMessage());
                        }
                    });
                }
            });

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        datosJSON = new JSONArray();
                        alProductos.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            productos producto = dataSnapshot.getValue(productos.class);
                            if (producto != null) {
                                jsonObject = new JSONObject();
                                jsonObject.put("idProducto", producto.getIdProducto());
                                jsonObject.put("marca", producto.getMarca());
                                jsonObject.put("descripcion", producto.getDescripcion());
                                jsonObject.put("presentacion", producto.getPresentacion());
                                jsonObject.put("stock", producto.getStock());
                                jsonObject.put("precio", producto.getPrecio());
                                jsonObject.put("costo", producto.getCosto());
                                jsonObject.put("urlCompletaFoto", producto.getUrlFotoProducto());
                                jsonObject.put("urlFotoProductoFirestore", producto.getUrlFotoProductoFirestore());
                                jsonObject.put("to", producto.getToken());
                                jsonObject.put("from", miToken );
                                datosJSON.put(jsonObject);

                                datosJSON.put(jsonObject);
                                alProductos.add(producto);
                            }
                        }
                        mostrarDatosProductos();

                    } catch (Exception e) {
                        mostrarMsg("Error al obtener los productos: " + e.getMessage());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    mostrarMsg("Error de base de datos: " + error.getMessage());
                }
            });
        } catch (Exception e) {
            mostrarMsg("Error al obtener datos productos del servidor: " + e.getMessage());
        }
    }

    private void mostrarDatosProductos() {
        try {
            if (datosJSON.length() > 0) {
                lts = findViewById(R.id.ltsProductos);
                alProductos.clear();
                alProductosCopy.clear();

                for (int i = 0; i < datosJSON.length(); i++) {
                    JSONObject misDatosJSONObject = datosJSON.getJSONObject(i);
                    datosProductos = new productos(
                            misDatosJSONObject.getString("idProducto"),
                            misDatosJSONObject.getString("marca"),
                            misDatosJSONObject.getString("descripcion"),
                            misDatosJSONObject.getString("presentacion"),
                            misDatosJSONObject.getString("stock"),
                            misDatosJSONObject.getString("precio"),
                            misDatosJSONObject.getString("costo"),
                            misDatosJSONObject.getString("urlCompletaFoto"),
                            misDatosJSONObject.optString("urlFotoProductoFirestore"),
                            misDatosJSONObject.optString("to")
                    );
                    alProductos.add(datosProductos);
                }
                alProductosCopy.addAll(alProductos);

                adaptadorImagenes adImagenes = new adaptadorImagenes(getApplicationContext(), alProductos);
                lts.setAdapter(adImagenes);

                registerForContextMenu(lts);
            } else {
                mostrarMsg("No hay datos que mostrar");
            }
        } catch (Exception e) {
            mostrarMsg("Error al mostrar datos: " + e.getMessage());
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu, menu);
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            posicion = info.position;
            menu.setHeaderTitle("Que deseas hacer con " + datosJSON.getJSONObject(posicion).getJSONObject("value").getString("marca"));
        } catch (Exception e) {
            mostrarMsg("Error al mostrar el menu aqui: " + e.getMessage());
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try {
            int itemId = item.getItemId();
            if (itemId == R.id.mnxAgregar) {
                paramatros.putString("accion", "nuevo");
                abrirActividad(paramatros);
            } else if (itemId == R.id.mnxModificar) {
                paramatros.putString("accion", "modificar");
                paramatros.putString("productos", datosJSON.getJSONObject(posicion).toString());
                abrirActividad(paramatros);
            } else if (itemId == R.id.mnxEliminar) {
                eliminarProducto();
            }
            return true;
        } catch (Exception e) {
            mostrarMsg("Error al seleccionar el item: " + e.getMessage());
            return super.onContextItemSelected(item);
        }
    }

    private void eliminarProducto() {
        try {
            AlertDialog.Builder confirmar = new AlertDialog.Builder(lista_productos.this);
            confirmar.setTitle("Esta seguro de eliminar a: ");
            confirmar.setMessage(datosJSON.getJSONObject(posicion).getJSONObject("value").getString("marca"));
            confirmar.setPositiveButton("SI", (dialogInterface, i) -> {
                try {
                    String respuesta = db.administrar_productos("eliminar", new String[]{"", "", datosJSON.getJSONObject(posicion).getJSONObject("value").getString("idProducto")});
                    if (respuesta.equals("ok")) {
                        mostrarMsg("Producto eliminado con exito");
                        obtenerProductos();
                    } else {
                        mostrarMsg("Error al eliminar el producto: " + respuesta);
                    }
                } catch (Exception e) {
                    mostrarMsg("Error al eliminar datos: " + e.getMessage());
                }
            });
            confirmar.setNegativeButton("NO", (dialogInterface, i) -> dialogInterface.dismiss());
            confirmar.create().show();
        } catch (Exception e) {
            mostrarMsg("Error al eliminar: " + e.getMessage());
        }
    }

    private void buscarProductos() {
        TextView tempVal = findViewById(R.id.txtBuscarProductos);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    alProductos.clear();
                    String valor = tempVal.getText().toString().trim().toLowerCase();
                    if (valor.length() <= 0) {
                        alProductos.addAll(alProductosCopy);
                    } else {
                        for (productos producto : alProductosCopy) {
                            String marca = producto.getMarca();
                            String descripcion = producto.getDescripcion();
                            String presentacion = producto.getPresentacion();
                            String stock = producto.getStock();
                            String precio = producto.getPrecio();
                            String costo = producto.getCosto();
                            if (marca.trim().toLowerCase().contains(valor) ||
                                    descripcion.trim().toLowerCase().contains(valor) ||
                                    presentacion.trim().contains(valor) ||
                                    stock.trim().toLowerCase().contains(valor) ||
                                    precio.trim().toLowerCase().contains(valor) ||
                                    costo.trim().toLowerCase().contains(valor)) {
                                alProductos.add(producto);
                            }
                        }
                        adaptadorImagenes adImagenes = new adaptadorImagenes(getApplicationContext(), alProductos);
                        lts.setAdapter(adImagenes);
                    }
                } catch (Exception e) {
                    mostrarMsg("Error al buscar: " + e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void abrirActividad(Bundle parametros) {
        Intent abrirActividad = new Intent(getApplicationContext(), MainActivity.class);
        abrirActividad.putExtras(parametros);
        startActivity(abrirActividad);
    }

    @SuppressLint("Range")
    private void obtenerProductos() { // offline
        try {
            cProductos = db.obtener_productos();
            if (cProductos.moveToFirst()) {
                datosJSON = new JSONArray();
                do {
                    jsonObject = new JSONObject();
                    JSONObject jsonObjectValue = new JSONObject();

                    jsonObject.put("idProducto", cProductos.getString(cProductos.getColumnIndex("idProducto")));
                    jsonObject.put("marca", cProductos.getString(cProductos.getColumnIndex("marca")));
                    jsonObject.put("descripcion", cProductos.getString(cProductos.getColumnIndex("descripcion")));
                    jsonObject.put("presentacion", cProductos.getString(cProductos.getColumnIndex("presentacion")));
                    jsonObject.put("stock", cProductos.getString(cProductos.getColumnIndex("stock")));
                    jsonObject.put("precio", cProductos.getString(cProductos.getColumnIndex("precio")));
                    jsonObject.put("costo", cProductos.getString(cProductos.getColumnIndex("costo")));
                    jsonObject.put("urlCompletaFoto", cProductos.getString(cProductos.getColumnIndex("foto")));
                    jsonObject.put("actualizado", cProductos.getString(cProductos.getColumnIndex("actualizado")));
                    jsonObjectValue.put("value", jsonObject);

                    datosJSON.put(jsonObjectValue);
                    datosJSON.put(jsonObject);
                } while (cProductos.moveToNext());
                mostrarDatosProductos();
            } else {
                paramatros.putString("accion", "nuevo");
                abrirActividad(paramatros);
                mostrarMsg("No hay Datos de productos.");
            }
        } catch (Exception e) {
            mostrarMsg("Error al obtener los productos: " + e.getMessage());
        }
    }

    private void mostrarMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}

