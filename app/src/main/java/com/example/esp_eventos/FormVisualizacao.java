package com.example.esp_eventos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FormVisualizacao extends BaseActivity {
    ImageView imageViewoff;
    ImageView searchIcon;
    ImageView closeButton;
    Drawable drawable1, drawable2;
    private VisuAdapter visuAdapter;
    private List<Eventos> listaEventos;
    androidx.appcompat.widget.SearchView searchView;
    RecyclerView recyclerView;
    private List<String> listIds;
    ImageView imageViewback1;
    ImageView adicionarfiltro;
    TextView testecalendario;
    FloatingActionButton adicionarnovoevento;
    ImageView retirarfiltro;
    EditText searchedittext;


    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_visualizacao);

        getSupportActionBar().hide();

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.azul1));

        IniciarComponentes();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaEventos = new ArrayList<>();
        listIds = new ArrayList<>();

        visuAdapter = new VisuAdapter(listaEventos, listIds);
        recyclerView.setAdapter(visuAdapter);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("eventos").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document1 : task.getResult()) {
                    Eventos event = document1.toObject(Eventos.class);
                    String eventoId = document1.getString("eventosId");
                    Log.d("ID", "EVENTOID" + eventoId);

                    // Defina a data de adição como a data atual
                    Timestamp dataAdicao = document1.getTimestamp("dataAdicao");

                    assert eventoId != null;
                    DocumentReference infoEventoRef = FirebaseFirestore.getInstance().collection("infoEventos").document(eventoId);
                    infoEventoRef.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            DocumentSnapshot infoEventoSnapshot = task1.getResult();
                            if (infoEventoSnapshot.exists()) {
                                String observacoes = infoEventoSnapshot.getString("observacoes");
                                String participantes = infoEventoSnapshot.getString("numerosdeParticipantes");
                                String setor = infoEventoSnapshot.getString("setorResponsavel");
                                String equipamentos = infoEventoSnapshot.getString("equipamentos");
                                String quaisequipamentos = infoEventoSnapshot.getString("quaisequipamentos");
                                String temtransmissao = infoEventoSnapshot.getString("temtransmissao");
                                String temtransmissaotipo = infoEventoSnapshot.getString("temTransmissaoTipo");
                                String temGravacaoTipo = infoEventoSnapshot.getString("temGravacaoTipo");
                                String temAmbosTipo = infoEventoSnapshot.getString("temAmbosTipo");
                                String presencial = infoEventoSnapshot.getString("presencial");
                                String hibrido = infoEventoSnapshot.getString("hibrido");
                                String online = infoEventoSnapshot.getString("online");
                                String linktransmissao = infoEventoSnapshot.getString("linkTransmissao");
                                String materialgraficos = infoEventoSnapshot.getString("materiaisgraficos");

                                if(Objects.equals(equipamentos, "Sim")){
                                    event.setQuaisequipamentos(quaisequipamentos);
                                }
                                if(Objects.equals(temtransmissao, "Sim")){
                                    if(Objects.equals(temtransmissaotipo,"Sim")){
                                        event.setTemTransmissaoTipo(temtransmissaotipo);
                                    } else if(Objects.equals(temGravacaoTipo, "Sim")){
                                        event.setTemGravacaoTipo(temGravacaoTipo);
                                    } else if(Objects.equals(temAmbosTipo, "Sim")) {
                                        event.setTemAmbosTipo(temAmbosTipo);
                                    }
                                }else{
                                    event.setTemtransmissao(temtransmissao);
                                }
                                if(Objects.equals(presencial, "Sim")){
                                    event.setPresencial(presencial);
                                }else if(Objects.equals(hibrido, "Sim") && Objects.equals(linktransmissao, "Sim")){
                                    event.setHibrido(hibrido);
                                    event.setLinkTransmissao(linktransmissao);
                                }else if(Objects.equals(online, "Sim") && Objects.equals(linktransmissao, "Sim")){
                                    event.setOnline(online);
                                    event.setLinkTransmissao(linktransmissao);
                                }
                                event.setObservacoes(observacoes);
                                event.setNumerosdeParticipantes(participantes);
                                event.setSetorResponsavel(setor);
                                event.setEquipamentos(equipamentos);
                                event.setMateriaisgraficos(materialgraficos);
                                event.setDataAdicao(dataAdicao);
                            } else {
                                Log.d("FormVisualizacao", "O documento infoEventos não existe para o evento com ID: " + eventoId);
                            }
                        } else {
                            Log.e("FormVisualizacao", "Erro ao obter dados do infoEventos: " + task1.getException());
                        }
                    });
                    listaEventos.add(event);
                    listIds.add(eventoId);
                }
                //visuAdapter.setListaEventos(listaEventos);
                //visuAdapter.setListIds(listIds);
                //visuAdapter.notifyDataSetChanged();
                visuAdapter.ordenarPorDataMaisRecente();
            } else {
                Toast.makeText(FormVisualizacao.this, "Nenhum Evento cadastrado", Toast.LENGTH_SHORT).show();
                Log.e("FormVisualizacao", "Erro ao ler dados do Firestore: " + task.getException());
            }
        });
        imageViewback1.setOnClickListener(v -> {

            Intent intentVoltarparaPaginaPrincipal = new Intent(FormVisualizacao.this, PaginaPrincipal.class);
            startActivity(intentVoltarparaPaginaPrincipal);
            finish();
        });
        imageViewoff.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();
            Intent intentDeslogarFormVisualizacao = new Intent(FormVisualizacao.this, Activity_Splash_Form_Login.class);
            startActivity(intentDeslogarFormVisualizacao);
            finish();
        });
        //testecalendario.setOnClickListener(v -> generateAndDownloadReport());
        adicionarnovoevento.setOnClickListener(v ->{
            Intent intentAdicionarEvento = new Intent(FormVisualizacao.this, FormCadastroEvento.class);
            startActivity(intentAdicionarEvento);
            finish();
        });
        adicionarfiltro.setOnClickListener(v ->{
            showSortDialog("Ordenação dos Eventos");
        });
        retirarfiltro.setOnClickListener(v ->{
            visuAdapter.ordenarPorDataMaisRecente();
            mostrarTodososEventos();
            retirarfiltro.setVisibility(View.GONE);
            adicionarfiltro.setVisibility(View.VISIBLE);
        });
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("FormVisualizacao", "Texto de pesquisa atualizado: " + newText);
                fileList(newText);
                return true;
            }
        });
    }
    @SuppressLint("NotifyDataSetChanged")
    public void mostrarTodososEventos(){
        visuAdapter.setListaEventos(listaEventos);
        visuAdapter.setListIds(listIds);
        visuAdapter.notifyDataSetChanged();
    }
    @SuppressLint("MissingInflatedId")
    public void showSortDialog(String titulo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        View view = LayoutInflater.from(this).inflate(
                R.layout.layout_ordenar_eventos,(ConstraintLayout)findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        CheckBox checkBoxHoje = view.findViewById(R.id.checkBoxHoje);
        CheckBox checkBox7dias = view.findViewById(R.id.checkBox7dias);
        //CheckBox checkBoxPersonalizado = view.findViewById(R.id.checkBoxPersonalizado);
        CheckBox checkBox30dias = view.findViewById(R.id.checkBox30dias);
        ((TextView) view.findViewById(R.id.textTitleAlertDialog)).setText(titulo);
        ((Button) view.findViewById(R.id.buttonAction)).setText("Ok");

        //Desabilitar os outros checkboxs e deixando apenas um habilitado.
        checkBoxHoje.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBox7dias.setChecked(false);
                    checkBox30dias.setChecked(false);
                }
            }
        });
        checkBox7dias.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxHoje.setChecked(false);
                    checkBox30dias.setChecked(false);
                }
            }
        });
        checkBox30dias.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBox7dias.setChecked(false);
                    checkBoxHoje.setChecked(false);
                }
            }
        });

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

                if(checkBoxHoje.isChecked()){
                    visuAdapter.ordenarPorEventosHoje();
                    //visuAdapter.ordenarPorDataInicialProxima();
                    retirarfiltro.setVisibility(View.VISIBLE);
                    adicionarfiltro.setVisibility(View.GONE);
                }else if(checkBox7dias.isChecked()){
                    visuAdapter.ordenarPorEventosProximos7Dias();
                    visuAdapter.ordenarPorDataInicialProxima();
                    retirarfiltro.setVisibility(View.VISIBLE);
                    adicionarfiltro.setVisibility(View.GONE);
                }else if(checkBox30dias.isChecked()){
                    visuAdapter.ordenarPorEventosProximos30Dias();
                    visuAdapter.ordenarPorDataInicialProxima();
                    retirarfiltro.setVisibility(View.VISIBLE);
                    adicionarfiltro.setVisibility(View.GONE);
                }
            }
        });

        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    @Override
    public void onBackPressed(){
        Toast.makeText(this, "Utilize o botão de voltar no topo da tela", Toast.LENGTH_SHORT).show();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void fileList(String text) {

            String lowercaseText = text.toLowerCase();

            if(lowercaseText.isEmpty()){
                visuAdapter.setFilteredList(listaEventos,listIds);
            }else{
                 List<Eventos> filteredList = new ArrayList<>();
                 List<String> filteredListIds = new ArrayList<>();

                for (int i = 0; i < listaEventos.size(); i++) {
                    Eventos evento = listaEventos.get(i);
                    String eventoId = listIds.get(i);

                    if (evento.getnomedoEvento().toLowerCase().contains(lowercaseText)
                        || evento.getdatadoEvento().toLowerCase().contains(lowercaseText)
                        || evento.getDatadoEventoFinal().toLowerCase().contains(lowercaseText)
                        || evento.getHoradoEventoInicial().toLowerCase().contains(lowercaseText)
                        || evento.getHoraEventoFinal().toLowerCase().contains(lowercaseText)
                        || evento.getlocaldoEvento().toLowerCase().contains(lowercaseText)) {
                        filteredList.add(evento);
                        filteredListIds.add(eventoId);
                        Log.d("FormVisualizacao", "Evento adicionado à lista filtrada: " + evento.getnomedoEvento());
                    }
                }
                Log.d("FormVisualizacao", "Texto de pesquisa: " + text);
                Log.d("FormVisualizacao", "Tamanho da lista de eventos completa: " + listaEventos.size());
                Log.d("FormVisualizacao", "Tamanho da lista filtrada: " + filteredList.size());
                if (filteredList.isEmpty()) {
                    Toast.makeText(this, "Sem resultado", Toast.LENGTH_SHORT).show();
                }
                visuAdapter.setFilteredList(filteredList, filteredListIds);
            }
    }
    private void IniciarComponentes(){
        searchView = findViewById(R.id.pesquisa);
        testecalendario = findViewById(R.id.txtTituloServicos);
        adicionarfiltro = findViewById(R.id.adicionarfiltro);
        imageViewback1 = findViewById(R.id.imageViewback1);
        imageViewoff = findViewById(R.id.imageViewoff);
        recyclerView = findViewById(R.id.recyclerView1);
        adicionarnovoevento = findViewById(R.id.adicionarnovoevento);
        retirarfiltro = findViewById(R.id.retirarfiltro);

        searchedittext = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchedittext.setTextColor(getResources().getColor(R.color.black));
        searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        drawable1 = searchIcon.getDrawable();
        drawable1.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        drawable2 = closeButton.getDrawable();
        drawable2.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

    }
    @SuppressLint("LongLogTag")
    protected void onDestroy(){
        super.onDestroy();
        Log.d("Formulario de Visualização Eventos", "Activity finalizada");
    }
}