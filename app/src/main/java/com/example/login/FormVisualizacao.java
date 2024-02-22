package com.example.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.esp_eventos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FormVisualizacao extends BaseActivity {
    ImageView imageViewoff;
    private VisuAdapter visuAdapter;
    private List<Eventos> listaEventos;
    ImageView imageViewback1;
    ImageView imageadd;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_visualizacao);

        //imageadd = findViewById(R.id.adicionarevento);
        imageViewback1 = findViewById(R.id.imageViewback1);
        imageViewoff = findViewById(R.id.imageViewoff);
        RecyclerView recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaEventos = new ArrayList<>();
        visuAdapter = new VisuAdapter(listaEventos);
        recyclerView.setAdapter(visuAdapter);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("eventos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Eventos event = document.toObject(Eventos.class);
                        listaEventos.add(event);
                    }
                        visuAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Nenhum Evento cadastrado", Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "Erro ao ler dados do Firestore: " + task.getException());
                }
            }
        });

        imageViewback1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentFV = new Intent(FormVisualizacao.this, PaginaPrincipal.class);
                intentFV.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentFV);
                finish();
            }
        });

        imageViewoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(FormVisualizacao.this, Activity_Splash_Form_Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        imageadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FormVisualizacao.this, FormCadastroEvento.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        SearchView searchView = findViewById(R.id.pesquisa);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
         @Override
         public boolean onQueryTextSubmit(String query) {
             return false;
         }

          @Override
         public boolean onQueryTextChange(String newText) {
            fileList(newText);
           return true;
         }
        });

    }

    @Override
    public void onBackPressed(){
        Toast.makeText(this, "Utilize o botão de voltar no topo da tela", Toast.LENGTH_SHORT).show();
    }

    private void fileList(String text) {
        List<Eventos> filteredList = new ArrayList<>();
        for (Eventos eventos : listaEventos){
            if(eventos.getnomedoEvento().toLowerCase().contains(text.toLowerCase()) ||
               eventos.getdatadoEvento().toLowerCase().contains(text.toLowerCase()) ||
               eventos.getHoradoEventoInicial().toLowerCase().contains(text.toLowerCase()) ||
               eventos.getlocaldoEvento().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(eventos);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "Sem resultado", Toast.LENGTH_SHORT).show();
        }else{
            visuAdapter.setFilteredList(filteredList);
        }
    }
    protected void onDestroy(){
        super.onDestroy();
        Log.d("Pagina principal", "Activity finalizada");
    }
}