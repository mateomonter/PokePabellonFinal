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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mateomontero.pokepabellon.MainActivity;
import com.mateomontero.pokepabellon.R;
import com.mateomontero.pokepabellon.modelo.Carrito;

public class UserRecuperarActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    Carrito carrito;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_recuperar);

        carrito = (Carrito) getIntent().getSerializableExtra("carrito");


        // below line is used to get the
        // instance of our FIrebase database.


        Button recuperar = (Button) findViewById(R.id.RecUserActivityRecuperar);
        Button login = (Button) findViewById(R.id.RecUserLoginButton);
        EditText Ecorreo = (EditText) findViewById(R.id.editTextRecuperarActivityCorreo);
        ImageView logo = (ImageView) findViewById(R.id.imageViewLogoRecUserActivity);

        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String correo = Ecorreo.getText().toString();
                //     auth.sendPasswordResetEmail(correo);
                auth.sendPasswordResetEmail(correo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserRecuperarActivity.this, R.string.ActPassword, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserRecuperarActivity.this, R.string.falloCorreoE, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserRecuperarActivity.this, UserActivity.class);
                i.putExtra("carrito", carrito);
                startActivity(i);
                finish();
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserRecuperarActivity.this, MainActivity.class);
                i.putExtra("carrito", carrito);
                startActivity(i);
                finish();
            }
        });
    }
}