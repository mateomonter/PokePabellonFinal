package com.mateomontero.pokepabellon.vista_compras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.mateomontero.pokepabellon.MainActivity;
import com.mateomontero.pokepabellon.R;
import com.mateomontero.pokepabellon.modelo.Carrito;
import com.mateomontero.pokepabellon.modelo.ItemCarrrito;
import com.mateomontero.pokepabellon.modelo.Pedido;
import com.mateomontero.pokepabellon.modelo.Usuario;
import com.mateomontero.pokepabellon.modelo.Producto;
import com.mateomontero.pokepabellon.adapters.ListAdapterCarrito;
import com.mateomontero.pokepabellon.vista_usuario.UserActivity;


import java.util.ArrayList;

public class CarritoActivity extends AppCompatActivity {
    Usuario usuario;
    Carrito carrito;

    boolean carritoActivityComp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);
        try {
            usuario = (Usuario) getIntent().getSerializableExtra("usuario");
            carrito = (Carrito) getIntent().getSerializableExtra("carrito");
        }catch (Exception e){

        }

        listar();

        ImageView b_main = (ImageView) findViewById(R.id.CarritoActivity_botonMain);
        Button b_compra = (Button) findViewById(R.id.CarritoActivityBotonCompra);
        Button b_cancel=(Button) findViewById(R.id.buttonCancelarCarrito) ;


        b_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CarritoActivity.this, MainActivity.class);
                i.putExtra("carrito", carrito);
                i.putExtra("cantidad", 0);
                i.putExtra("usuario", usuario);
                startActivity(i);
                //finish();
            }
        });

        b_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CarritoActivity.this, MainActivity.class);
                i.putExtra("carrito", carrito);
                i.putExtra("cantidad", 0);
                i.putExtra("usuario", usuario);
                startActivity(i);
                //finish();
            }
        });

        b_compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CarritoActivity.this, PagoActivity.class);

                if (usuario != null) {

                    i.putExtra("usuario", usuario);
                    i.putExtra("carrito", carrito);

                    if (carrito.size()!=0) {
                        startActivity(i);

                        finish();
                    }else {
                        Toast.makeText(CarritoActivity.this, R.string.falloSize, Toast.LENGTH_SHORT).show();

                    }
                }else{

                    i=new Intent(CarritoActivity.this, UserActivity.class);
                    carritoActivityComp=true;

                    i.putExtra("carrito", carrito);
                    i.putExtra("carritoActivityComp", carritoActivityComp);

                        startActivity(i);

                        finish();
                }

            }
        });

    }

    private void listar() {

        ListAdapterCarrito listAdapterCarrito = new ListAdapterCarrito(carrito.getItems(), CarritoActivity.this, new ListAdapterCarrito.OnItemClickListener() {
            @Override
            public void onItemClick(ItemCarrrito itemCarrrito) {
                moveToDescription(itemCarrrito.getProducto());

            }

        });


        RecyclerView recyclerView = findViewById(R.id.listaP);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CarritoActivity.this));
        recyclerView.setAdapter(listAdapterCarrito);
    }


    public void moveToDescription(Producto producto) {
        Intent intent = new Intent(this, ProductoActivity.class);
        intent.putExtra("producto", producto);
        intent.putExtra("carrito", carrito);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
        finish();
    }


}