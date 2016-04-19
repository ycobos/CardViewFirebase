package com.augustosalazar.as_android_cardview;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ViewAdapter.RecyclerClickListner {

    private ViewAdapter viewAdapter;
    private RecyclerView mRecyclerView;
    public String BaseDatos= "listview00";
    public Firebase miFirebaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        miFirebaseRef = new Firebase("https://" +BaseDatos+ ".firebaseio.com/");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button btncrear = (Button) findViewById(R.id.crearBtn);
        btncrear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.show();

                final EditText mMessageEdit = (EditText) dialog.findViewById(R.id.DialogEditText);


                Button AgregarBtn = (Button) dialog.findViewById(R.id.CrearBtn);
                AgregarBtn.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        String message = mMessageEdit.getText().toString();
                        Map<String,String> values = new HashMap<>();
                        values.put("name", message);

                        Firebase mFirebaseRef = new Firebase("https://" + BaseDatos + ".firebaseio.com/");
                        mFirebaseRef.push().setValue(values);
                        mMessageEdit.setText("");

                    }
                });

                Button CancelarBtn = (Button) dialog.findViewById(R.id.CambiarBtn);
                CancelarBtn.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });


            }
        });

        miFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<String> names = new ArrayList<String>();
                ArrayList<String> keys = new ArrayList<String>();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    names.add(postSnapshot.child("name").getValue().toString());
                    keys.add(postSnapshot.getRef().getKey().toString());

                }
                String[] nombres = new String[names.size()];
                nombres = names.toArray(nombres);
                String[] llaves = new String[keys.size()];
                llaves = keys.toArray(llaves);

                viewAdapter = new ViewAdapter(MainActivity.this, nombres);
                //viewAdapter.setRecyclerClickListner(this);
                mRecyclerView.setAdapter(viewAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        Button CambiarDB = (Button) findViewById(R.id.CambiarBtn);
        CambiarDB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.show();

                final EditText mMessageEdit = (EditText) dialog.findViewById(R.id.DialogEditText);


                Button AgregarBtn = (Button) dialog.findViewById(R.id.CrearBtn);
                AgregarBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nombreBD = mMessageEdit.getText().toString();
                        BaseDatos = nombreBD;
                        miFirebaseRef = new Firebase("https://" + BaseDatos + ".firebaseio.com/");
                        miFirebaseRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                ArrayList<String> names = new ArrayList<String>();
                                ArrayList<String> keys = new ArrayList<String>();


                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                    names.add(postSnapshot.child("name").getValue().toString());
                                    keys.add(postSnapshot.getRef().getKey().toString());
                                }
                                String[] nombres = new String[names.size()];
                                nombres = names.toArray(nombres);
                                String[] llaves = new String[keys.size()];
                                llaves = keys.toArray(llaves);


                                viewAdapter = new ViewAdapter(MainActivity.this, nombres);
                                //viewAdapter.setRecyclerClickListner(this);
                                mRecyclerView.setAdapter(viewAdapter);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                        dialog.dismiss();

                    }
                });

            }
        });




    }

    @Override
    public void itemClick(View view, int position) {
        Log.d("Recyclerview", "Click position " + position);
    }
}
