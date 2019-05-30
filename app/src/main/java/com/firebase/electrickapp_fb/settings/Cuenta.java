package com.firebase.electrickapp_fb.settings;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.electrickapp_fb.MainActivity;
import com.firebase.electrickapp_fb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Cuenta extends AppCompatActivity {

    public static final String DATO_KEY = "TELEFONO";
    public static final String DATO_KEY2 = "NOMBRE";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    EditText etidphone, etname;
    Button btregister;
    TextInputLayout celular_layout, nombre_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cuenta);

        etidphone = (EditText) findViewById(R.id.edit_id_phone);
        etname = (EditText) findViewById(R.id.edit_name);
        btregister = (Button) findViewById(R.id.bt_register);
        nombre_layout = (TextInputLayout) findViewById(R.id.name_layout);
        celular_layout = (TextInputLayout) findViewById(R.id.cel_layout);

        mAuth = FirebaseAuth.getInstance();

        btregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String numero = etidphone.getText().toString();
                String nombre = etname.getText().toString();

                if (numero.isEmpty() || numero.length() < 10) {
                    celular_layout.setError("Se requiere numero de celular válido");
                    etidphone.requestFocus();
                    return;
                }

                if (nombre.isEmpty()) {
                    nombre_layout.setError("Se requiere un nombre válido");
                    etname.requestFocus();
                    return;
                }

                String telefono = "+57" + numero;

                Intent intent = new Intent(getBaseContext(), Code.class);
                intent.putExtra(DATO_KEY, telefono);
                intent.putExtra(DATO_KEY2, nombre);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(Cuenta.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }
}
