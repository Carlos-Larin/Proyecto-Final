package com.ugb.controlesbasicos;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class adaptadorImagenes extends BaseAdapter {
    Context context;
    ArrayList<productos> datosProductosArrayList;
    LayoutInflater layoutInflater;

    public adaptadorImagenes(Context context, ArrayList<productos> datosProductosArrayList) {
        this.context = context;
        this.datosProductosArrayList = datosProductosArrayList;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return datosProductosArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return datosProductosArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_imagenes, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.lblNombreProducto = convertView.findViewById(R.id.lblNombreProducto);
            viewHolder.lblPresentacion = convertView.findViewById(R.id.lblPresentacion);
            viewHolder.lblPrecioProducto = convertView.findViewById(R.id.lblPrecioProducto);
            viewHolder.imgFoto = convertView.findViewById(R.id.imgFoto);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {
            productos datosProductos = datosProductosArrayList.get(i);
            viewHolder.lblNombreProducto.setText(datosProductos.getMarca() != null ? datosProductos.getMarca() : "No disponible");
            viewHolder.lblPresentacion.setText(datosProductos.getPresentacion() != null ? datosProductos.getPresentacion() : "No disponible");
            viewHolder.lblPrecioProducto.setText(datosProductos.getPrecio() != null ? datosProductos.getPrecio() : "No disponible");

            if (datosProductos.getUrlFotoProducto() != null) {
                Bitmap imageBitmap = BitmapFactory.decodeFile(datosProductos.getUrlFotoProducto());
                viewHolder.imgFoto.setImageBitmap(imageBitmap);
            }


        } catch (Exception e) {
            Toast.makeText(context, "Error al mostrar los datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView lblNombreProducto;
        TextView lblPresentacion;
        TextView lblPrecioProducto;
        ImageView imgFoto;

    }
}
