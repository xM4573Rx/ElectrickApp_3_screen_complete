package com.firebase.electrickapp_fb.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.electrickapp_fb.MainActivity;
import com.firebase.electrickapp_fb.R;
import com.firebase.electrickapp_fb.transfer.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

public class Code extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference mDataBaseReference = FirebaseDatabase.getInstance().getReference();

    private String telefono;
    private String nombre;
    private String verificationId;

    EditText etcode;
    TextView tel_number;
    ProgressBar progressCode;
    Button ver_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_code);

        etcode = (EditText) findViewById(R.id.edit_code);
        tel_number = (TextView) findViewById(R.id.txt_telefono);
        progressCode = (ProgressBar) findViewById(R.id.progress_code);
        ver_button = (Button) findViewById(R.id.verify_button);

        mAuth = FirebaseAuth.getInstance();

        if (getIntent() != null) {
            telefono = this.getIntent().getStringExtra(Cuenta.DATO_KEY);
            nombre = this.getIntent().getStringExtra(Cuenta.DATO_KEY2);
        }

        tel_number.setText(telefono);

        enviarCodigodeVerificacion(telefono);

        ver_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = etcode.getText().toString();

                if(code.isEmpty() || code.length() < 6) {
                    etcode.setError("Ingrese el código");
                    etcode.requestFocus();
                    return;
                }

                verificarCodigo(code);
            }
        });

        //Toast.makeText(this, "El string es: " + telefono, Toast.LENGTH_SHORT).show();
    }

    private void verificarCodigo(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    User u = new User();
                    u.setUid(telefono);
                    u.setProyeccion("0.1");
                    u.setWatt("0.1");
                    u.setWattHora("0.1");
                    u.setNombre(nombre);
                    mDataBaseReference.child("User").child(u.getUid()).setValue(u);

                    try {
                        OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput("User.txt", Activity.MODE_PRIVATE));

                        archivo.write(telefono);
                        archivo.write("%");
                        archivo.write(nombre);
                        archivo.write("&");
                        archivo.flush();
                        archivo.close();
                    } catch (IOException e) {

                    }

                    Intent intent = new Intent(Code.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


                    startActivity(intent);
                }
            }
        });
    }

    private void enviarCodigodeVerificacion( String number) {

        progressCode.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                etcode.setText(code);
                verificarCodigo(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Code.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}
