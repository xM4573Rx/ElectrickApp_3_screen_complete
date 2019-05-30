package com.firebase.electrickapp_fb.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.firebase.electrickapp_fb.R;
import com.firebase.electrickapp_fb.transfer.ProyData;
import com.firebase.electrickapp_fb.transfer.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class Proyeccion extends AppCompatDialogFragment {

    TextInputLayout proye_layout;
    EditText editTextValue;
    Spinner spinner;
    private ExampleDialogListener listener;

    DatabaseReference mDataBaseReference = FirebaseDatabase.getInstance().getReference();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_proyeccion, null);

        editTextValue = (EditText) view.findViewById(R.id.edit_value);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        proye_layout = (TextInputLayout) view.findViewById(R.id.pro_layout);

        builder.setView(view).setTitle("Proyección")
            .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            })
            .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String value = editTextValue.getText().toString();
                    String selector = spinner.getSelectedItem().toString();

                    if (value.isEmpty()) {
                        proye_layout.setError("Se requiere un nombre válido");
                        editTextValue.requestFocus();
                        value = "0";
                        //return;
                    }

                    listener.applyText(selector, value);
                }
            });

        String [] options = {"$", "kWh"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_items, options);
        spinner.setAdapter(adapter);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener {
        void applyText(String selection, String number);
    }
}
