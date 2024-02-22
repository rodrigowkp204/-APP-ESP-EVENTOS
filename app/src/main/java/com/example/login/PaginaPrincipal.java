package com.example.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.esp_eventos.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.List;

public class PaginaPrincipal extends BaseActivity {
    ImageView imageViewoff;
    private RecyclerView recyclerView;
    private List<Photos> photoList = new ArrayList<>();

    // Initialize Firebase Auth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);

        FirebaseApp.initializeApp(this);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .build();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);

        imageViewoff = findViewById(R.id.imageViewoff);
        imageViewoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(PaginaPrincipal.this, Activity_Splash_Form_Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        photoList = getPhotoList();

        //Layout Manager -> configuração de layout, aparência, estrutura
        //adapter -> apadtador, intermediario entre o dataset e o layout
        //dataset -> dados

        //configurar o adapter
        PhotoAdapter photoAdapter = new PhotoAdapter(photoList);

        //configurar o recyclerview

        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setHasFixedSize(true); //lista dinamica tera um tamanho fixo
        recyclerView.setAdapter(photoAdapter);


    }

    public void onBackPressed(){

        new AlertDialog.Builder(this)
                .setMessage("Você deseja realmente sair do aplicativo ?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Não",null)
                .show();
    }

    private List<Photos> getPhotoList(){

        photoList.add(new Photos(1,R.drawable.logo_calendario_eventos_pagina_inicial,"Agendar Evento"));
        photoList.add(new Photos(2,R.drawable.logo_visualizacao_pagina_inicial, "Visualizar Agendados"));

        return photoList;
    }

    protected void onDestroy(){
        super.onDestroy();

        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);
        recyclerView = null;

        photoList.clear();
        photoList = null;

        Log.d("Pagina principal", "Activity finalizada");
    }

}