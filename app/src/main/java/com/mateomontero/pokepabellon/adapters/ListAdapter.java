package com.mateomontero.pokepabellon.adapters;

import android.content.Context;
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
import com.mateomontero.pokepabellon.modelo.Producto;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    public Uri uriD;
    private Context context;
    private List<Producto> mData;
    private LayoutInflater mInflater;
    public ListAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Producto producto);
    }

    public ListAdapter(List<Producto> itemList, Context context, ListAdapter.OnItemClickListener listener) {
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
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_item_producto, parent,false);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));
    }

    public void setItems(List<Producto> items) {
        mData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView producto;
        TextView precio;
        ImageView producto1;

        public ViewHolder(View itemView) {
            super(itemView);
            producto = itemView.findViewById(R.id.textViewProductoLayout);
            precio = itemView.findViewById(R.id.textViewPrecioProductoLayout);
            producto1 = itemView.findViewById(R.id.imageViewProductoLayoutImagen);
        }

        public void bindData(final Producto item) {
            producto.setText(item.getNombre());
            precio.setText(item.getPrecio() + " €");

            this.producto1.setImageURI(cargarImagen(item.getNombreImagen() + ".jpg", producto1));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
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
