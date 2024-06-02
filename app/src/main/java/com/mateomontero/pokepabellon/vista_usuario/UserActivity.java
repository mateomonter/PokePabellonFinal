package com.mateomontero.pokepabellon.vista_usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.mateomontero.pokepabellon.modelo.Usuario;
import com.mateomontero.pokepabellon.vista_compras.CarritoActivity;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<Usuario>  usuario;
    EditText T_password;
    EditText T_correo;
    Carrito carrito;
    boolean carritoActivityComp;
    Usuario u;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        try {
            carrito = (Carrito) getIntent().getSerializableExtra("carrito");
            carritoActivityComp=(boolean) getIntent().getSerializableExtra("carritoActivityComp");
        } catch (Exception e) {

        }


        T_password = (EditText) findViewById(R.id.UserActivity_TextoPassword);
        T_correo = (EditText) findViewById(R.id.UserActivity_EditTextCorreo);
        Button botonLogin = (Button) findViewById(R.id.UserActivity_botonInicioSesion);
        Button botonRegistro = (Button) findViewById(R.id.UserActivity_botonRegistro);
        Button botonCancelar = (Button) findViewById(R.id.buttonCancelarLogin);

        ImageView logo = (ImageView) findViewById(R.id.imageViewLogoUserActivity);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        recuperarDatos();

        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correoD=T_correo.getText().toString();
                String passwordD=T_password.getText().toString();
                if (T_correo.length()!=0 && T_password.length()!=0) {


                        for (Usuario usuario1: usuario){
                            if (usuario1.getCorreo().equals(correoD) && usuario1.getPassward().equals(passwordD)) {
                                    u=usuario1;

                            }
                        }

                    if (u==null)
                        Toast.makeText(UserActivity.this, R.string.falloLogin, Toast.LENGTH_SHORT).show();
                    else{
                        setDireccion();
                        //ahora que tenemos todo decidimos a donde vamos
                        Intent i;
                        if (carritoActivityComp==true){
                            i=new Intent(UserActivity.this, CarritoActivity.class);
                        }else {
                             i = new Intent(UserActivity.this, MainActivity.class);
                        }
                        i.putExtra("usuario", u);
                        i.putExtra("carrito", carrito);
                        startActivity(i);
                    }

                }else{
                    Toast.makeText(UserActivity.this, R.string.falloCampos, Toast.LENGTH_SHORT).show();
                }
            }
        });


        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserActivity.this, MainActivity.class);
                i.putExtra("carrito", carrito);
                startActivity(i);
                finish();
            }
        });
  botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserActivity.this, MainActivity.class);
                i.putExtra("carrito", carrito);
                startActivity(i);
                finish();
            }
        });


        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserActivity.this, RegistroActivity.class);
                i.putExtra("carrito", carrito);
                i.putExtra("carritoActivityComp", carritoActivityComp);
                startActivity(i);
                finish();
            }
        });

    }


    /*
    * recuperar Datos,
    * @return null dopnt exigts usert
    * @return user if user extis
    * */
    public void recuperarDatos() {
        usuario=new ArrayList<Usuario>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              //  T_password = (EditText) findViewById(R.id.UserActivity_TextoPassword);
               // T_correo = (EditText) findViewById(R.id.UserActivity_EditTextCorreo);
                Usuario u2;

                boolean encontrado=false;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (encontrado==false)
                        try {
                            String txt = ds.getValue().toString();
                            u2 =    new Usuario(txt);
                            u2.setKey(ds.getKey());
                            usuario.add(u2);
                    } catch (Exception e) {

                    }



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }
        private void setDireccion(){

            DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("direcciones");
            ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Direccion direccion = null;
                        try {
                            String txt = ds.getValue().toString();
                            direccion = new Direccion(txt);
                            direccion.setKey(ds.getKey());
                        } catch (Exception e) {

                        }
                        if (direccion != null) {
                            if (direccion.getKey().equalsIgnoreCase(u.getKey())) {
                                u.setDireccion(direccion);
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


}
