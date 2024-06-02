package com.mateomontero.pokepabellon.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mateomontero.pokepabellon.R;
import com.mateomontero.pokepabellon.modelo.Producto;

import java.util.List;

public class ListAdapterDetallesProducto extends RecyclerView.Adapter<ListAdapterDetallesProducto.ViewHolder> {
    public Uri uriD;
    private Context context;
    private List<Producto> mData;
    private LayoutInflater mInflater;




    public ListAdapterDetallesProducto(List<Producto> itemList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ListAdapterDetallesProducto.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_item_producto_detalles, parent,false);
        return new ListAdapterDetallesProducto.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapterDetallesProducto.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));
    }

    public void setItems(List<Producto> items) {
        mData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView detalleNombre;
        TextView detalleDesc;
        TextView detallePrecio;
        TextView detalleSize;

        public ViewHolder(View itemView) {
            super(itemView);
            detalleNombre = itemView.findViewById(R.id.textViewdetalleLayoutNombre);
            detalleDesc = itemView.findViewById(R.id.textViewdetalleLayoutDesc);
            detallePrecio = itemView.findViewById(R.id.textViewdetalleLayoutPrecio);
            detalleSize = itemView.findViewById(R.id.textViewdetalleLayoutSize);

        }

        public void bindData(final Producto item) {

            detalleNombre.setText(item.getNombre());
            detalleDesc.setText(item.getDescripcion());
            detallePrecio.setText("precio: "+item.getPrecio()+" €");
            detalleSize.setText("tamaño: "+item.getSize()+" cm");


        }
    }



}
