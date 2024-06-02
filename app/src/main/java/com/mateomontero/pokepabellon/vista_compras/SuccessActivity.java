package com.mateomontero.pokepabellon.vista_compras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mateomontero.pokepabellon.MainActivity;
import com.mateomontero.pokepabellon.R;
import com.mateomontero.pokepabellon.SplashActivity;
import com.mateomontero.pokepabellon.modelo.Carrito;
import com.mateomontero.pokepabellon.modelo.Usuario;

public class SuccessActivity extends AppCompatActivity {

    Usuario usuario;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        TextView keyPedido=(TextView) findViewById(R.id.textViewKeyPedido);
        Button b_main=(Button) findViewById(R.id.buttonVolverInicioSucces);

        b_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SuccessActivity.this, MainActivity.class);

                i.putExtra("usuario", usuario);

                startActivity(i);
                finish();
            }
        });

        try {
            usuario = (Usuario) getIntent().getSerializableExtra("usuario");
            key= (String) getIntent().getSerializableExtra("keyPedido");
        }catch (Exception e){

        }

        keyPedido.setText("Token del pedido: "+key);
        long time = 250000;

        //retardo de tiempo


        new Handler().postDelayed(() -> {
            Intent i = new Intent(SuccessActivity.this, MainActivity.class);

            i.putExtra("usuario", usuario);

            startActivity(i);
            finish();
        }, time);


    }

}