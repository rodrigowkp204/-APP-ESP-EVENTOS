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

public class FormVisualizacaoExterno extends BaseActivity {
    ImageView imageViewbackFormVisuExt;
    ImageView searchIcon;
    ImageView closeButton;
    Drawable drawable1, drawable2;
    EditText searchedittext;
    ImageView imageViewoffFormVisuExt;
    ImageView adicionarfiltroFormVisuExt;
    ImageView retirarfiltroFormVisuExt;
    FloatingActionButton adicionarnovoeventoexterno;
    RecyclerView recyclerViewExt;
    androidx.appcompat.widget.SearchView searchView;
    private List<EventosExterno> listaEventosExt;
    private List<String> listIdsExt;
    private VisuAdapterExt visuAdapterExt;

    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_visualizacao_externo);

        getSupportActionBar().hide();
        IniciarComponentes();

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.azul1));

        recyclerViewExt.setHasFixedSize(true);
        recyclerViewExt.setLayoutManager(new LinearLayoutManager(this));

        listaEventosExt = new ArrayList<>();
        listIdsExt = new ArrayList<>();
        visuAdapterExt = new VisuAdapterExt(listaEventosExt, listIdsExt);
        recyclerViewExt.setAdapter(visuAdapterExt);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("eventosExt").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                    EventosExterno eventosExterno = documentSnapshot.toObject(EventosExterno.class);
                    String eventoExtId = documentSnapshot.getString("eventosExtId");

                    Timestamp dataAdicaoext = documentSnapshot.getTimestamp("dataAdicaoext");

                    assert eventoExtId != null;
                    DocumentReference infoEventoExtRef = FirebaseFirestore.getInstance().collection("infoEventosExt").document(eventoExtId);
                    infoEventoExtRef.get().addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()) {
                            DocumentSnapshot infoEventoExtSnapshot = task1.getResult();
                            if(infoEventoExtSnapshot.exists()) {
                                String observacoesEventoExterno = infoEventoExtSnapshot.getString("observacoesEventoExterno");
                                String participantesEventoExterno = infoEventoExtSnapshot.getString("participantesEventoExterno");
                                String setorResponsavelEventoExterno = infoEventoExtSnapshot.getString("setorResponsavelEventoExterno");

                                String equipamentosEventoExterno = infoEventoExtSnapshot.getString("equipamentosEventoExterno");
                                String quaisequipamentosEventoExterno = infoEventoExtSnapshot.getString("quaisequipamentosEventoExterno");
                                String temtransmissaoEventoExterno = infoEventoExtSnapshot.getString("temtransmissaoEventoExterno");
                                String temtransmissaotipoEventoExterno = infoEventoExtSnapshot.getString("temTransmissaoTipoEventoExterno");
                                String temGravacaoTipoEventoExterno = infoEventoExtSnapshot.getString("temGravacaoTipoEventoExterno");
                                String temAmbosTipoEventoExterno = infoEventoExtSnapshot.getString("temAmbosTipoEventoExterno");
                                String presencialEventoExterno = infoEventoExtSnapshot.getString("presencialEventoExterno");
                                String hibridoEventoExterno = infoEventoExtSnapshot.getString("hibridoEventoExterno");
                                String onlineEventoExterno = infoEventoExtSnapshot.getString("onlineEventoExterno");
                                String linktransmissaoEventoExterno = infoEventoExtSnapshot.getString("linkTransmissaoEventoExterno");
                                String materialgraficosEventoExterno = infoEventoExtSnapshot.getString("materiaisgraficosEventoExterno");

                                if(Objects.equals(equipamentosEventoExterno, "Sim")){
                                    eventosExterno.setQuaisequipamentosEventoExterno(quaisequipamentosEventoExterno);
                                }
                                if(Objects.equals(temtransmissaoEventoExterno, "Sim")){
                                    if(Objects.equals(temtransmissaotipoEventoExterno,"Sim")){
                                        eventosExterno.setTemTransmissaoTipoEventoExterno(temtransmissaotipoEventoExterno);
                                    } else if(Objects.equals(temGravacaoTipoEventoExterno, "Sim")){
                                        eventosExterno.setTemGravacaoTipoEventoExterno(temGravacaoTipoEventoExterno);
                                    } else if(Objects.equals(temAmbosTipoEventoExterno, "Sim")) {
                                        eventosExterno.setTemAmbosTipoEventoExterno(temAmbosTipoEventoExterno);
                                    }
                                }else{
                                    eventosExterno.setTemtransmissaoEventoExterno(temtransmissaoEventoExterno);
                                }
                                if(Objects.equals(presencialEventoExterno, "Sim")){
                                    eventosExterno.setPresencialEventoExterno(presencialEventoExterno);
                                }else if(Objects.equals(hibridoEventoExterno, "Sim") && Objects.equals(linktransmissaoEventoExterno, "Sim")){
                                    eventosExterno.setHibridoEventoExterno(hibridoEventoExterno);
                                    eventosExterno.setLinkTransmissaoEventoExterno(linktransmissaoEventoExterno);
                                }else if(Objects.equals(onlineEventoExterno, "Sim") && Objects.equals(linktransmissaoEventoExterno, "Sim")){
                                    eventosExterno.setOnlineEventoExterno(onlineEventoExterno);
                                    eventosExterno.setLinkTransmissaoEventoExterno(linktransmissaoEventoExterno);
                                }

                                eventosExterno.setObservacoesEventoExterno(observacoesEventoExterno);
                                eventosExterno.setParticipantesEventoExterno(participantesEventoExterno);
                                eventosExterno.setSetorResponsavelEventoExterno(setorResponsavelEventoExterno);
                                eventosExterno.setEquipamentosEventoExterno(equipamentosEventoExterno);
                                eventosExterno.setMateriaisgraficosEventoExterno(materialgraficosEventoExterno);
                                eventosExterno.setDataAdicaoext(dataAdicaoext);
                            } else {
                                Log.d("FormVisualizacao", "O documento infoEventos não existe para o evento com ID: " + eventoExtId);
                            }
                        } else {
                            Log.e("FormVisualizacao", "Erro ao obter dados do infoEventos: " + task1.getException());
                        }
                    });
                    listaEventosExt.add(eventosExterno);
                    listIdsExt.add(eventoExtId);
                }
                //visuAdapterExt.setListaEventosExt(listaEventosExt);
                //visuAdapterExt.setListIds(listIdsExt);
                //visuAdapterExt.notifyDataSetChanged();
                visuAdapterExt.ordenarPorDataMaisRecente();
            }else {
                Toast.makeText(getApplicationContext(), "Nenhum Evento cadastrado", Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Erro ao ler dados do Firestore: " + task.getException());
            }
        });

        imageViewbackFormVisuExt.setOnClickListener(v -> {
            Intent intentFVEX = new Intent(FormVisualizacaoExterno.this, PaginaPrincipal.class);
            startActivity(intentFVEX);
            finish();
        });

        imageViewoffFormVisuExt.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();
            Intent intentOffFVEX = new Intent(FormVisualizacaoExterno.this, Activity_Splash_Form_Login.class);
            startActivity(intentOffFVEX);
            finish();
        });

        adicionarnovoeventoexterno.setOnClickListener(v -> {
            Intent intentaddFVEX = new Intent(FormVisualizacaoExterno.this, FormCadastroEventoExterno.class);
            startActivity(intentaddFVEX);
            finish();
        });
        adicionarfiltroFormVisuExt.setOnClickListener(v ->{
            showSortDialog("Ordenação dos Eventos");
        });
        retirarfiltroFormVisuExt.setOnClickListener(v ->{
            visuAdapterExt.ordenarPorDataMaisRecente();
            mostrarTodososEventos();
            retirarfiltroFormVisuExt.setVisibility(View.GONE);
            adicionarfiltroFormVisuExt.setVisibility(View.VISIBLE);
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
                fileListExt(newText);
                return true;
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onBackPressed(){
        Toast.makeText(this, "Utilize o botão de voltar no topo da tela", Toast.LENGTH_SHORT).show();
    }
    public void mostrarTodososEventos(){
        visuAdapterExt.setListaEventosExt(listaEventosExt);
        visuAdapterExt.setListIds(listIdsExt);
        visuAdapterExt.notifyDataSetChanged();
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
                    visuAdapterExt.ordenarPorEventosHoje();
                    visuAdapterExt.ordenarPorDataInicialProxima();
                    retirarfiltroFormVisuExt.setVisibility(View.VISIBLE);
                    adicionarfiltroFormVisuExt.setVisibility(View.GONE);
                }else if(checkBox7dias.isChecked()){
                    visuAdapterExt.ordenarPorEventosProximos7Dias();
                    visuAdapterExt.ordenarPorDataInicialProxima();
                    retirarfiltroFormVisuExt.setVisibility(View.VISIBLE);
                    adicionarfiltroFormVisuExt.setVisibility(View.GONE);
                }else if(checkBox30dias.isChecked()){
                    visuAdapterExt.ordenarPorEventosProximos30Dias();
                    visuAdapterExt.ordenarPorDataInicialProxima();
                    retirarfiltroFormVisuExt.setVisibility(View.VISIBLE);
                    adicionarfiltroFormVisuExt.setVisibility(View.GONE);
                }
            }
        });

        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    private void fileListExt(String text) {

        String lowercaseText = text.toLowerCase();

        if(lowercaseText.isEmpty()){
            visuAdapterExt.setFilteredListExt(listaEventosExt,listIdsExt);
        } else{

            List<EventosExterno> filteredList = new ArrayList<>();
            List<String> filteredListIdsExt = new ArrayList<>();

            for (int i = 0; i < listaEventosExt.size(); i++) {
                EventosExterno eventosExterno = listaEventosExt.get(i);
                String eventoExtId = listIdsExt.get(i);

                if (eventosExterno.getNomedoEventoExterno().toLowerCase().contains(lowercaseText)
                        || eventosExterno.getDataInicialEventoExterno().toLowerCase().contains(lowercaseText)
                        || eventosExterno.getDataFinalEventoExterno().toLowerCase().contains(lowercaseText)
                        || eventosExterno.getHoraInicialEventoExterno().toLowerCase().contains(lowercaseText)
                        || eventosExterno.getHoraFinalEventoExterno().toLowerCase().contains(lowercaseText)
                        || eventosExterno.getLocalEventoExterno().toLowerCase().contains(lowercaseText)) {
                    filteredList.add(eventosExterno);
                    filteredListIdsExt.add(eventoExtId);
                    Log.d("FormVisualizacao", "Evento adicionado à lista filtrada: " + eventosExterno.getNomedoEventoExterno());
                }
            }
            Log.d("FormVisualizacao", "Texto de pesquisa: " + text);
            Log.d("FormVisualizacao", "Tamanho da lista de eventos completa: " + listaEventosExt.size());
            Log.d("FormVisualizacao", "Tamanho da lista filtrada: " + filteredList.size());
            if (filteredList.isEmpty()) {
                Toast.makeText(this, "Sem resultado", Toast.LENGTH_SHORT).show();
            }
            visuAdapterExt.setFilteredListExt(filteredList, filteredListIdsExt);
        }
    }
    private void IniciarComponentes(){
        imageViewbackFormVisuExt = findViewById(R.id.imageViewbackFormVisuExt);
        imageViewoffFormVisuExt = findViewById(R.id.imageViewoffFormVisuExt);
        adicionarnovoeventoexterno = findViewById(R.id.adicionarnovoeventoexterno);
        recyclerViewExt = findViewById(R.id.recyclerViewFormVisuExt);
        searchView = findViewById(R.id.pesquisaFormVisuExt);
        adicionarfiltroFormVisuExt = findViewById(R.id.adicionarfiltroFormVisuExt);
        retirarfiltroFormVisuExt = findViewById(R.id.retirarfiltroFormVisuExt);

        searchedittext = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchedittext.setTextColor(getResources().getColor(R.color.black));
        searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        drawable1 = searchIcon.getDrawable();
        drawable1.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        drawable2 = closeButton.getDrawable();
        drawable2.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
    }
}