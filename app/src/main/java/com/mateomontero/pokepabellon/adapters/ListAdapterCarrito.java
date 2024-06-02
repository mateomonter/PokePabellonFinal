package com.mateomontero.pokepabellon.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mateomontero.pokepabellon.R;
import com.mateomontero.pokepabellon.modelo.ItemCarrrito;
import com.mateomontero.pokepabellon.vista_compras.CarritoActivity;
import com.mateomontero.pokepabellon.vista_compras.ProductoActivity;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterCarrito extends RecyclerView.Adapter<ListAdapterCarrito.ViewHolder> {
    public Uri uriD;
    private Context context;
    private List<ItemCarrrito> mData;
    private LayoutInflater mInflater;
    public ListAdapterCarrito.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ItemCarrrito itemCarrrito);
    }

    public ListAdapterCarrito(List<ItemCarrrito> itemList, Context context, ListAdapterCarrito.OnItemClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ListAdapterCarrito.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_item_carrito, parent,false);
        return new ListAdapterCarrito.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapterCarrito.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));
    }

    public void setItems(List<ItemCarrrito> items) {
        mData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView producto;
        TextView precio;
        TextView cantidad;
        ImageView producto1;
        ImageView basura;

        public ViewHolder(View itemView) {
            super(itemView);
            producto = itemView.findViewById(R.id.textViewProductoLayoutCarrito);
            precio = itemView.findViewById(R.id.textViewPrecioProductoLayoutCarrito);
            producto1 = itemView.findViewById(R.id.imageViewProductoLayoutImagenCarrito);
            cantidad = itemView.findViewById(R.id.textViewCantidadProductoCarrito);
            basura=itemView.findViewById(R.id.imageViewBasura);
        }

        public void bindData(final ItemCarrrito item) {
            producto.setText(item.getProducto().getNombre());
            precio.setText(item.getProducto().getPrecio() + " €");
            int c = item.getCantidad();
            cantidad.setText("" + c);


            basura.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    borrar(item);
                }
            });

            this.producto1.setImageURI(cargarImagen(item.getProducto().getNombreImagen() + ".jpg", producto1));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });


        }
    }

    private void borrar(ItemCarrrito item) {
                item.modificarProducto(item.getProducto(),0);
                mData.remove(item);
                notifyDataSetChanged();



    }

    private Uri cargarImagen(String path, ImageView producto) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        uriD = null;
        //path="mudkippeluche.jpg";


        //gs://pokepabellon-34828.appspot.com
        storageRef.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(producto);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
        return uriD;
    }


}
