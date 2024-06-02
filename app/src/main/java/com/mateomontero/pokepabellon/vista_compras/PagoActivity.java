package com.mateomontero.pokepabellon.vista_compras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mateomontero.pokepabellon.MainActivity;
import com.mateomontero.pokepabellon.R;
import com.mateomontero.pokepabellon.modelo.Carrito;
import com.mateomontero.pokepabellon.modelo.Direccion;
import com.mateomontero.pokepabellon.modelo.Pedido;
import com.mateomontero.pokepabellon.modelo.Usuario;
import com.mateomontero.pokepabellon.vista_usuario.UserActivity;

public class PagoActivity extends AppCompatActivity {


    Carrito carrito;
    Usuario usuario;
    String metodoPago;

    String direccionB;
    TextView direccionT;
    TextView direccionCiudad;
    TextView direccionProvincia;
    TextView direccionCP;
    TextView direccionCalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago);


        Button b_compra = (Button) findViewById(R.id.PagoActivityBotonCompra);
        Button b_cancel = (Button) findViewById(R.id.buttonCancel);
        TextView precio = (TextView) findViewById(R.id.PagoActivityPrecio2);
        direccionT = (TextView) findViewById(R.id.direccionPais);
        direccionCiudad=(TextView) findViewById(R.id.textViewCiudad);
        direccionProvincia=(TextView) findViewById(R.id.textViewProvincia);
        direccionCP=(TextView) findViewById(R.id.textViewCP);
        direccionCalle=(TextView) findViewById(R.id.textViewCalle);
        EditText ccv = (EditText) findViewById(R.id.editTextCCV);
        EditText fechaCaducidad = (EditText) findViewById(R.id.editTextTextFechaCaducidad);
        EditText numeroTarjeta = (EditText) findViewById(R.id.editTextTextNumeroTarjeta);
        ImageView logo=(ImageView) findViewById(R.id.imageViewLogoPagoActivity);
        RadioGroup metodo=(RadioGroup) findViewById(R.id.RadioGroupMetodo);
        RadioButton contrarrembolso=(RadioButton) findViewById(R.id.RadioButtoncontrarrembolso);
        RadioButton paypal=(RadioButton) findViewById(R.id.RadioButtonPaypal);
        RadioButton tarjeta=(RadioButton) findViewById(R.id.RadioButtonTarjeta);

        try {
            carrito = (Carrito) getIntent().getSerializableExtra("carrito");

            usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        }catch (Exception e){

        }
        ccv.setVisibility(View.INVISIBLE);
        fechaCaducidad.setVisibility(View.INVISIBLE);
        numeroTarjeta.setVisibility(View.INVISIBLE);
        consultarDireccion();
        precio.setText(carrito.getPrecio() + " €");


        metodo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                              @Override
                                              public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                 switch (checkedId) {
                                                     case R.id.RadioButtonTarjeta:
                                                         ccv.setVisibility(View.VISIBLE);
                                                        fechaCaducidad.setVisibility(View.VISIBLE);
                                                         numeroTarjeta.setVisibility(View.VISIBLE);
                                                         break;
                                                     case R.id.RadioButtoncontrarrembolso:
                                                         ccv.setVisibility(View.INVISIBLE);
                                                         fechaCaducidad.setVisibility(View.INVISIBLE);
                                                         numeroTarjeta.setVisibility(View.INVISIBLE);
                                                         break;

                                                     case R.id.RadioButtonPaypal:
                                                         ccv.setVisibility(View.INVISIBLE);
                                                         fechaCaducidad.setVisibility(View.INVISIBLE);
                                                         numeroTarjeta.setVisibility(View.INVISIBLE);
                                                         break;

                                                 }
                                              }
                                          });



        b_compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PagoActivity.this, SuccessActivity.class);

                String keyDireccion = usuario.getKey();
                boolean radio = false;
                boolean tarjetaDetalles = false;
                Pedido pedido = new Pedido(keyDireccion, carrito.getItems(), usuario, carrito.getPrecio());
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference d2 = dbRef.push();
                String key2 = d2.getKey();
                if (metodo.getCheckedRadioButtonId() == contrarrembolso.getId()) {
                    metodoPago = "contrarrembolso";
                    radio = true;
                }
                if (metodo.getCheckedRadioButtonId() == paypal.getId()) {
                    metodoPago = "Paypal";
                    radio = true;
                }
                if (metodo.getCheckedRadioButtonId() == tarjeta.getId()) {
                    if (numeroTarjeta.getText().length()!=0 && ccv.getText().length()!=0 && fechaCaducidad.getText().length()!=0) {
                        metodoPago = "tarjeta: " + "numero de tarjeta: " + numeroTarjeta.getText().toString() + " ccv: " + ccv.getText().toString() + " fecha de caducidad: " + fechaCaducidad.getText().toString();
                        tarjetaDetalles=false;
                        radio = true;
                    }else {
                        radio = false;
                        tarjetaDetalles=true;
                    }
                }
                if (radio == true) {
                    dbRef.child("pedidos").child(key2).setValue(pedido.toString());
                    dbRef.child("pago").child(key2).setValue(metodoPago);
                    dbRef.child("estado").child(key2).setValue("no entregado");

                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("stockProductos");
                    ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //tenemos una lista del carrito
                            //busamos la key
                            for (int i = 0; i < carrito.getItems().size(); i++) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String text = ds.getKey().toString();
                                    if (carrito.getKeyProducto(i).equalsIgnoreCase(text)) {
                                        //obtenemos el stock acual
                                        try {
                                            int stock = Integer.parseInt(ds.getValue().toString());
                                            stock = stock - carrito.getCantidadProducto(i);
                                            if (stock >= 0) {
                                                ds.getRef().setValue(stock);
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // ...
                        }
                    });

                    i.putExtra("usuario", usuario);
                    i.putExtra("keyPedido", key2);

                    startActivity(i);
                    finish();
                }else {
                    if(tarjetaDetalles==true){
                        Toast.makeText(PagoActivity.this, R.string.falloCampos, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(PagoActivity.this, R.string.falloPago, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        b_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PagoActivity.this, CarritoActivity.class);

                i.putExtra("usuario", usuario);
                i.putExtra("carrito", carrito);

                startActivity(i);
            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PagoActivity.this, MainActivity.class);

                i.putExtra("usuario", usuario);
                i.putExtra("carrito", carrito);

                startActivity(i);
            }
        });

    }
    private void consultarDireccion() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("direcciones");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String text = ds.getKey();
                    if (usuario.getKey().equals(text)) {
                        direccionB = ds.getValue().toString();
                        Direccion direccionC=new Direccion(direccionB);
                        direccionT.setText("pais: "+direccionC.getPais());
                        direccionCalle.setText("calle: "+direccionC.getCalle());
                        direccionCP.setText("codigo postal: "+direccionC.getCp());
                        direccionProvincia.setText("provincia: "+direccionC.getProvincia());
                        direccionCiudad.setText("ciudad: "+direccionC.getCiudad());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
    }
}