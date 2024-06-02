package com.mateomontero.pokepabellon.vista_usuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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

import java.util.ArrayList;

public class RegistroActivity extends AppCompatActivity {

    Carrito carrito;
    boolean carritoActivityComp;
    ArrayList<Usuario> usuariosS=new ArrayList<Usuario>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        try {
            carrito = (Carrito) getIntent().getSerializableExtra("carrito");
            carritoActivityComp = (boolean) getIntent().getSerializableExtra("carritoActivityComp");

            consultarUsuarios();
        } catch (Exception e) {

        }



        Button botonRegistro = (Button) findViewById(R.id.RegistroActivity_botonRegistro);
        Button botonCancelar = (Button) findViewById(R.id.buttonCancelarRegistro);
        EditText T_UsuarioR = (EditText) findViewById(R.id.RegistroActivity_TextoUsuarioR);
        EditText T_passwordR = (EditText) findViewById(R.id.RegistroActivity_TextoPasswordR);
        EditText T_passwordDosR = (EditText) findViewById(R.id.RegistroActivity_TextoPasswordDos2R);
        EditText T_correoR = (EditText) findViewById(R.id.RegistroActivity_TextoCorreoR);
        EditText pais = (EditText) findViewById(R.id.RegistroActivityeditTextTextPais);
        EditText ciudad = (EditText) findViewById(R.id.RegistroActivityeditTextTextCiudad);
        EditText cp = (EditText) findViewById(R.id.RegistroActivityeditTextTextCP);
        EditText calle = (EditText) findViewById(R.id.RegistroActivityeditTextTextCalle);
        EditText provincia = (EditText) findViewById(R.id.RegistroActivityeditTextTextProvincia);
        ImageView logo = (ImageView) findViewById(R.id.imageViewLogoRegistroActivity);


        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistroActivity.this, MainActivity.class);
                i.putExtra("carrito", carrito);
                startActivity(i);
                finish();
            }
        });

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistroActivity.this, UserActivity.class);
                i.putExtra("carrito", carrito);
                i.putExtra("carritoActivityComp", carritoActivityComp);
                startActivity(i);
                finish();
            }
        });

        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (T_correoR.getText().length()!=0 && T_UsuarioR.getText().length()!=0 && T_passwordR.getText().length()!=0 && T_passwordDosR.getText().length()!=0 && cp.getText().length()!=0 && ciudad.getText().length()!=0 && pais.getText().length()!=0 && calle.getText().length()!=0 && provincia.getText().length()!=0) {
                    if (T_correoR.getText().toString().contains("@") && T_correoR.getText().toString().contains(".")) {
                        String usario = T_UsuarioR.getText().toString();
                        String password = T_passwordR.getText().toString();
                        String passwordDos = T_passwordDosR.getText().toString();
                        String correo = T_correoR.getText().toString();
                        Usuario usuario = new Usuario(usario, correo, password);


                        if (password.length() >= 6) {
                            if (password.equals(passwordDos)) {
                                Usuario u = new Usuario(usario, correo, password);
                                Direccion direccion = new Direccion(ciudad.getText().toString(), cp.getText().toString(), pais.getText().toString(), provincia.getText().toString(), calle.getText().toString());

                                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference d2 = dbRef.push();
                                String key2 = d2.getKey();
                                dbRef.child("users").child(key2).setValue(u.toString());
                                dbRef.child("direcciones").child(key2).setValue(direccion.toString());

                                boolean existe=false;

                                for(Usuario usuario1:usuariosS){
                                    if (usuario1.getCorreo().equalsIgnoreCase(correo))   {
                                        existe=true;
                                    }
                                }
                                if (existe==false) {
                                    Intent i = new Intent(RegistroActivity.this, UserActivity.class);


                                    i.putExtra("carrito", carrito);
                                    i.putExtra("carritoActivityComp", carritoActivityComp);
                                    startActivity(i);

                                    finish();
                                }else {
                                    Toast.makeText(RegistroActivity.this, R.string.falloCorreoExistente, Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(RegistroActivity.this, R.string.falloPasswordEqual, Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(RegistroActivity.this, R.string.falloPasswordLength, Toast.LENGTH_SHORT).show();

                        }


                    }else {
                        Toast.makeText(RegistroActivity.this, R.string.falloCorreo, Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(RegistroActivity.this, R.string.falloCampos, Toast.LENGTH_SHORT).show();

                }
            }
        });


    }
    private void consultarUsuarios() {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String text = ds.getValue().toString();
                    Usuario usuario=new Usuario(text);
                    usuario.setKey(ds.getKey());
                    usuariosS.add(usuario);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
    }
}