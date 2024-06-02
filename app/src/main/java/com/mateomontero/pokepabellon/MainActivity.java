package com.mateomontero.pokepabellon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mateomontero.pokepabellon.modelo.Carrito;
import com.mateomontero.pokepabellon.modelo.ItemCarrrito;
import com.mateomontero.pokepabellon.modelo.Producto;
import com.mateomontero.pokepabellon.modelo.Usuario;
import com.mateomontero.pokepabellon.adapters.ListAdapter;
import com.mateomontero.pokepabellon.vista_compras.CarritoActivity;
import com.mateomontero.pokepabellon.vista_compras.ProductoActivity;
import com.mateomontero.pokepabellon.vista_usuario.UserActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    Uri uriD;
    ArrayList<Producto> productos = new ArrayList<Producto>();
    ArrayList<ItemCarrrito> interiorCarrito = new ArrayList<ItemCarrrito>();
    ArrayList<String> listaLlaves=new ArrayList<String>();
    ArrayList<Producto> pnonulo;
    Carrito carrito;
    int cantidad;
    Usuario usuario;
    int numeroProductos;
    EditText filtro;
    boolean carritoActivityComp;
    ArrayList<Producto> filtrado=new ArrayList<Producto>();
    Producto p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView texto_usuario = (TextView) findViewById(R.id.MaintextViewUsuario);
        TextView texto_numeroCarrito = (TextView) findViewById(R.id.textViewNumeroProductosMainActivity);
        Button b_busqueda = (Button) findViewById(R.id.MainActitity_botonBusqueda);
        Button b_ascend = (Button) findViewById(R.id.MainActitity_botonAscend);
        Button b_descend = (Button) findViewById(R.id.MainActitity_botonDescend);
        filtro=(EditText) findViewById(R.id.MainActivity_TextBusqueda);
        ImageView ilogin = (ImageView) findViewById(R.id.imageViewLoginMainActivity);
        ImageView ilogout = (ImageView) findViewById(R.id.imageViewLogoutMainActivity);
        ImageView icarrito = (ImageView) findViewById(R.id.imageViewCarritoMainActivity);
        ImageView ilogut = (ImageView) findViewById(R.id.imageViewLogoutMainActivity);
        GifImageView loadingLogo = (GifImageView) findViewById(R.id.gifImageLoadingMain);
        carritoActivityComp=false;
        //cobnexiones
try {
    carrito = (Carrito) getIntent().getSerializableExtra("carrito");
    usuario = (Usuario) getIntent().getSerializableExtra("usuario");
}catch (Exception e){

}

        //recuperamos datos -> usuario y carrito
        if (usuario == null) {
            texto_usuario.setText(R.string.invitado);
            ilogut.setVisibility(View.INVISIBLE);
            ilogin.setVisibility(View.VISIBLE);
        } else {
            /*user.getEmail().toString()*/
            texto_usuario.setText(usuario.getNombre());
            ilogut.setVisibility(View.VISIBLE);
            ilogin.setVisibility(View.INVISIBLE);
        }
      numeroProductos=0;

        if (carrito != null) {
            numeroProductos = carrito.size();
        }else {
            carrito = new Carrito();
        }


        b_ascend.setVisibility(View.INVISIBLE);
        b_descend.setVisibility(View.INVISIBLE);
        loadingLogo.setVisibility(View.VISIBLE);
        consultar();
        consultarLLaves();
        new Handler().postDelayed(() -> {
            adaptar();
            loadingLogo.setVisibility(View.INVISIBLE);
            b_descend.setVisibility(View.VISIBLE);
        }, 3500);

        texto_numeroCarrito.setText("" + numeroProductos);


        b_ascend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filtrado.size()==0) {
                    consultarOrdenPrecioAscendente();
                }else {
                    consultarOrdenPrecioAscendenteFiltro();
                }
                b_ascend.setVisibility(View.INVISIBLE);
                b_descend.setVisibility(View.VISIBLE);
            }
        });

        b_descend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filtrado.size()==0) {
                    consultarOrdenPrecioDescendente();
                }else {
                    consultarOrdenPrecioDescendenteFiltro();
                }
                b_descend.setVisibility(View.INVISIBLE);
                b_ascend.setVisibility(View.VISIBLE);
            }
        });

        b_busqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filtro.getText().toString()!=null) {
                    productos = new ArrayList<Producto>();
                    consultarFiltro(filtro.getText().toString().toLowerCase());
                }
            }
        });


        ilogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UserActivity.class);
                i.putExtra("carrito", carrito);
                i.putExtra("carritoActivityComp",carritoActivityComp);
                startActivity(i);
                //  finish();
            }
        });

        ilogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MainActivity.class);


                startActivity(i);
                finish();
            }
        });

        icarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CarritoActivity.class);

                i.putExtra("usuario", usuario);
                i.putExtra("carrito", carrito);
                startActivity(i);
                //finish();
            }
        });

    }


    public void moveToDescription(Producto producto) {
        Intent intent = new Intent(this, ProductoActivity.class);
        intent.putExtra("producto", producto);
        intent.putExtra("carrito", carrito);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
        finish();
    }


 private void consultarLLaves(){
         listaLlaves=new ArrayList<String>();
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("stockProductos");
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String text = ds.getValue().toString();


                 //   listaLlaves.add(ds.getKey());
                  String key= ds.getKey();
                  if (text.equals("0")){

                  }else {
                      try {
                          listaLlaves.add(key);
                      } catch (Exception e) {

                      }
                  }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

    }

    private void consultar() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("productos");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String text = ds.getValue().toString();
                        p=new Producto(text);
                        p.setKey(ds.getKey());
                        productos.add(p);
                    }catch (Exception e){

                    }

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

    }


    private void adaptar() {

        pnonulo=new ArrayList<Producto>();


                    for (String llave: listaLlaves) {
                        for (Producto producto:productos) {
                            String key=producto.getKey();
                            if (key.equals(llave)) {
                                try {
                                    pnonulo.add(producto);
                                }catch (Exception e){

                                }

                            }
                        }

                    }




                ListAdapter listAdapter = new ListAdapter(pnonulo, MainActivity.this, new ListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Producto producto) {
                        moveToDescription(producto);
                    }
                });
                RecyclerView recyclerView = findViewById(R.id.lista);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(listAdapter);
            }




    private void consultarFiltro(String filtra) {

       filtrado=new ArrayList<Producto>();

        for (Producto p:pnonulo){
            if (p.getNombre().toLowerCase().contains(filtra)){
                filtrado.add(p);
            }
        }


                ListAdapter listAdapter = new ListAdapter(filtrado, MainActivity.this, new ListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Producto producto) {
                        moveToDescription(producto);
                    }
                });
                RecyclerView recyclerView = findViewById(R.id.lista);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(listAdapter);



    }

    private void consultarOrdenPrecioAscendente() {

        ArrayList<Producto> precioProductosA=new ArrayList<Producto>();

        for (Producto p:pnonulo){


                precioProductosA.add(p);


        }
        Collections.sort(precioProductosA, new Comparator<Producto>() {
            @Override
            public int compare(Producto o1, Producto o2) {
                return Integer.valueOf(o1.getPrecio()).compareTo(Integer.valueOf(o2.getPrecio()));
            }
        });



        ListAdapter listAdapter = new ListAdapter(precioProductosA, MainActivity.this, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Producto producto) {
                moveToDescription(producto);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.lista);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(listAdapter);



    }
    private void consultarOrdenPrecioAscendenteFiltro() {

        ArrayList<Producto> precioProductosAF=new ArrayList<Producto>();

        for (Producto p:filtrado){


            precioProductosAF.add(p);


        }
        Collections.sort(precioProductosAF, new Comparator<Producto>() {
            @Override
            public int compare(Producto o1, Producto o2) {
                return Integer.valueOf(o1.getPrecio()).compareTo(Integer.valueOf(o2.getPrecio()));
            }
        });



        ListAdapter listAdapter = new ListAdapter(precioProductosAF, MainActivity.this, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Producto producto) {
                moveToDescription(producto);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.lista);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(listAdapter);



    }

    private void consultarOrdenPrecioDescendente() {

        ArrayList<Producto> precioProductosA=new ArrayList<Producto>();

        for (Producto p:pnonulo){


            precioProductosA.add(p);


        }
        Collections.sort(precioProductosA, new Comparator<Producto>() {
            @Override
            public int compare(Producto o1, Producto o2) {
                return Integer.valueOf(o2.getPrecio()).compareTo(Integer.valueOf(o1.getPrecio()));
            }
        });



        ListAdapter listAdapter = new ListAdapter(precioProductosA, MainActivity.this, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Producto producto) {
                moveToDescription(producto);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.lista);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(listAdapter);



    }
    private void consultarOrdenPrecioDescendenteFiltro() {

        ArrayList<Producto> precioProductosA=new ArrayList<Producto>();

        for (Producto p:filtrado){


            precioProductosA.add(p);


        }
        Collections.sort(precioProductosA, new Comparator<Producto>() {
            @Override
            public int compare(Producto o1, Producto o2) {
                return Integer.valueOf(o2.getPrecio()).compareTo(Integer.valueOf(o1.getPrecio()));
            }
        });



        ListAdapter listAdapter = new ListAdapter(precioProductosA, MainActivity.this, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Producto producto) {
                moveToDescription(producto);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.lista);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(listAdapter);



    }





}