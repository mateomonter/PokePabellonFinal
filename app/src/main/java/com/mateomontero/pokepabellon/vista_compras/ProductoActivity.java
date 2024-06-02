package com.mateomontero.pokepabellon.vista_compras;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mateomontero.pokepabellon.MainActivity;
import com.mateomontero.pokepabellon.R;
import com.mateomontero.pokepabellon.adapters.ListAdapterCarrito;
import com.mateomontero.pokepabellon.adapters.ListAdapterDetallesProducto;
import com.mateomontero.pokepabellon.modelo.Carrito;
import com.mateomontero.pokepabellon.modelo.DatosProducto;
import com.mateomontero.pokepabellon.modelo.ItemCarrrito;
import com.mateomontero.pokepabellon.modelo.Producto;
import com.mateomontero.pokepabellon.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ProductoActivity extends AppCompatActivity {

    Uri uriD;
    Carrito carrito;
    Usuario usuario;
    int stock;
    Producto producto;
    ArrayList<Producto> datosProducto=new ArrayList<Producto>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        try {
            carrito = (Carrito) getIntent().getSerializableExtra("carrito");
            producto = (Producto) getIntent().getSerializableExtra("producto");
            usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        }catch (Exception e){

        }



        ImageView botonMain = (ImageView) findViewById(R.id.imageViewLogoProductoActivity);
        Button botonMas = (Button) findViewById(R.id.ProductoActivity_mas);
        Button botonMenos = (Button) findViewById(R.id.ProductoActivity_menos);
        Button botonCarrito = (Button) findViewById(R.id.ProductoActivity_Carrito);
        Button botonCancel = (Button) findViewById(R.id.buttonCancelarProducto);
        TextView Numero = (TextView) findViewById(R.id.ProductoActivity_editTextNumber);



        listar();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("productos");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String text = ds.getValue().toString();

                    if(producto.toString().equals(text)){
                        producto.setKey(ds.getKey());
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("stockProductos");
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String text = ds.getKey();
                    if(producto.getKey().equals(text)){
                        try {
                            stock= Integer.parseInt(ds.getValue().toString());
                        }catch (Exception e){

                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });


        botonMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int cantidad = Integer.parseInt(String.valueOf(Numero.getText()));
                    cantidad++;
                    Numero.setText("" + cantidad);
                } catch (Exception e) {

                }

            }
        });


        botonMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int cantidad = Integer.parseInt(String.valueOf(Numero.getText()));
                    if (cantidad > 0) {
                        cantidad--;
                        Numero.setText("" + cantidad);
                    }
                } catch (Exception e) {

                }
            }
        });


        botonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductoActivity.this, MainActivity.class);

                i.putExtra("carrito", carrito);
                i.putExtra("usuario",usuario);

                startActivity(i);
                finish();
            }
        });

        botonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductoActivity.this, MainActivity.class);

                i.putExtra("carrito", carrito);
                i.putExtra("usuario",usuario);

                startActivity(i);
                finish();
            }
        });


        botonCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent i = new Intent(ProductoActivity.this, MainActivity.class);
                    int cantidad = Integer.parseInt(String.valueOf(Numero.getText()));

                        if (cantidad > 0 && cantidad <= stock) {

                            boolean repetido = false;

                            for (ItemCarrrito itemCarrrito : carrito.getItems()) {

                                if (itemCarrrito.getProducto().getNombre().equalsIgnoreCase(producto.getNombre())) {
                                    repetido = true;
                                    ChangeNumber(itemCarrrito.getProducto(), cantidad);
                                }
                            }

                            carrito.addProducto(producto, cantidad, producto.getPrecio());
                            i.putExtra("usuario", usuario);
                            i.putExtra("carrito", carrito);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(ProductoActivity.this, "el numero debe ser mayor a cero o inferior o igual a " + stock, Toast.LENGTH_SHORT).show();
                        }
                    }
        });

    }

    private Uri cargarImagen(String path, ImageView imagen) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        // String path="mudkippeluche.jpg";
        uriD = null;
        storageRef.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                uriD = uri;
                Glide.with(ProductoActivity.this).load(uriD).into(imagen);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(ProductoActivity.this, "Fallo cargar Firebase(Fotos)", Toast.LENGTH_SHORT).show();
            }
        });
        return uriD;
    }

    public void ChangeNumber(Producto p, int nuevaCantidad) {
        carrito.modificarProducto(p, nuevaCantidad);
    }

    private void listar() {

        datosProducto=new ArrayList<Producto>();
        datosProducto.add(producto);

        ListAdapterDetallesProducto listAdapterDetallesProducto = new ListAdapterDetallesProducto(datosProducto, ProductoActivity.this);
        RecyclerView recyclerView = findViewById(R.id.listadetalles);
        ImageView productoimagen = (ImageView) findViewById(R.id.imageViewProductoctivityProducto);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductoActivity.this));
        recyclerView.setAdapter(listAdapterDetallesProducto);
        cargarImagen(producto.getNombreImagen().toString()+".jpg",productoimagen);
    }

}