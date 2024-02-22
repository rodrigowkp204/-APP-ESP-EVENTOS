package com.example.esp_eventos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FormCadastroEventoExterno extends BaseActivity {

    ImageView imageViewoff_ext, imageViewback_ext,
            imageViewData_ext, imageViewDataFinal_ext,
            imageViewHora_ext, imageViewHoraFinal_ext;
    private EditText edit_nome_evento_ext, edit_setor_resp_ext,
            edit_numeros_participantes_ext, edit_local_externo_ext,
            edit_desc_atv_ext,edit_obs_ext;
    CheckBox checkBoxSimEventoExterno, checkBoxNaoEventoExterno,
            checkBoxSimEventoTransmissaoExterno, checkBoxNaoEventoTransmissaoExterno, checkBoxtransmissaoExterno, checkBoxgravacaoExterno,
            checkBoxAmbosExterno, checkBoxPresencialExterno, checkBoxHibridoExterno, checkBoxonlineExterno,
            checkBoxSimEventoLinkExterno, checkBoxNaoEventoLinkExterno, checkBoxSimMaterialGraficoExterno, checkBoxNaoMaterialGraficoExterno;
    TextInputEditText edit_quais_equip_ext;
    TextView title_link_ext, title_solicitacao_material_grafico_ext, link_solicitacao_material_grafico_ext;
    String[] mensagens = {"Algo deu errado","Sucesso"};
    String eventoId = UUID.randomUUID().toString();
    Button bt_cadastrar_ext;
    @SuppressLint("StaticFieldLeak")
    static EditText edit_data_ext, edit_data_final_ext,
            edit_horas_ext, edit_horas_final_ext;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro_evento_externo);

        getSupportActionBar().hide();
        IniciarComponentes();

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.azul1));

        Calendar calendario = Calendar.getInstance();
        Integer dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH);
        Integer ano = calendario.get(Calendar.YEAR);
        Integer hora = calendario.get(Calendar.HOUR_OF_DAY);
        Integer minutos = calendario.get(Calendar.MINUTE);

        @SuppressLint("DefaultLocale")
        String dataFormatada = String.format("%02d/%02d/%04d", dia,mes+1,ano);
        edit_data_ext.setText(dataFormatada);

        @SuppressLint("DefaultLocale")
        String horaformatada = String.format("%02d:%02d",hora,minutos);
        edit_horas_ext.setText(horaformatada);

        imageViewback_ext.setOnClickListener(v -> {

            Intent intentVoltarFCEEX = new Intent(FormCadastroEventoExterno.this, PaginaPrincipal.class);
            startActivity(intentVoltarFCEEX);
            finish();
        });
        imageViewoff_ext.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();
            Intent intentFCEEX = new Intent(FormCadastroEventoExterno.this, Activity_Splash_Form_Login.class);
            startActivity(intentFCEEX);
            finish();
        });
        imageViewData_ext.setOnClickListener(v -> {
            DialogFragment dialogFragment = new DatePicker();
            dialogFragment.show(getSupportFragmentManager(),"data_ext");
        });
        imageViewDataFinal_ext.setOnClickListener(v -> {
            DialogFragment dialogFragment = new DatePicker();
            dialogFragment.show(getSupportFragmentManager(), "data_final_ext");
        });
        imageViewHora_ext.setOnClickListener(v -> {
            DialogFragment dialogFragment = new TimePicker();
            dialogFragment.show(getSupportFragmentManager(), "hora1");
        });
        imageViewHoraFinal_ext.setOnClickListener(v -> {
            DialogFragment dialogFragment = new TimePicker();
            dialogFragment.show(getSupportFragmentManager(), "hora_final");
        });
        bt_cadastrar_ext.setOnClickListener(v -> {
            String nome_ext = edit_nome_evento_ext.getText().toString();
            String datainicial_ext = edit_data_ext.getText().toString();
            String datafinal_ext = edit_data_final_ext.getText().toString();
            String horainicial_ext = edit_horas_ext.getText().toString();
            String horafinal_ext = edit_horas_final_ext.getText().toString();
            String local_ext = edit_local_externo_ext.getText().toString();
            String desc_ext = edit_desc_atv_ext.getText().toString();
            boolean checkboxequip_ext = checkBoxSimEventoExterno.isChecked();
            String equipamentosquais_ext = edit_quais_equip_ext.getText().toString().trim();
            boolean precisaTransmissao_ext = checkBoxSimEventoTransmissaoExterno.isChecked();
            boolean transmissao_ext = checkBoxtransmissaoExterno.isChecked();
            boolean gravacao_ext = checkBoxgravacaoExterno.isChecked();
            boolean ambas_ext = checkBoxAmbosExterno.isChecked();
            boolean presencial_ext = checkBoxPresencialExterno.isChecked();
            boolean hibrido_ext = checkBoxHibridoExterno.isChecked();
            boolean online_ext = checkBoxonlineExterno.isChecked();
            boolean linktransmissao_ext = checkBoxSimEventoLinkExterno.isChecked();
            boolean precisaMaterialGrafico_ext = checkBoxSimMaterialGraficoExterno.isChecked();

            if(nome_ext.isEmpty() || datainicial_ext.isEmpty() || datafinal_ext.isEmpty()
                || horainicial_ext.isEmpty() || horafinal_ext.isEmpty() || local_ext.isEmpty()
                || desc_ext.isEmpty() || !checkBoxSimEventoExterno.isChecked() && !checkBoxNaoEventoExterno.isChecked()
                || !checkBoxSimMaterialGraficoExterno.isChecked() && !checkBoxNaoMaterialGraficoExterno.isChecked()) {

                Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();

            }else if (checkboxequip_ext && equipamentosquais_ext.isEmpty()){
                showErrorAlertDilog(mensagens[0],"Preencha o campo dos equipamentos necessários");
            }else if(precisaTransmissao_ext && !transmissao_ext && !gravacao_ext && !ambas_ext){
                showErrorAlertDilog(mensagens[0],"Preencha o campo dos tipos de transmissão");
            } else if(hibrido_ext && !linktransmissao_ext){
                showErrorAlertDilog(mensagens[0],"Preencha o campo do link do evento");
            } else if(online_ext && !linktransmissao_ext){
                showErrorAlertDilog(mensagens[0],"Preencha o campo do link do evento");
            }
            else {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference colRef = db.collection("eventosExt");

                Map<String, Object> eventosExt = new HashMap<>();
                eventosExt.put("nomedoEventoExterno", nome_ext);
                eventosExt.put("dataInicialEventoExterno", datainicial_ext);
                eventosExt.put("dataFinalEventoExterno", datafinal_ext);
                eventosExt.put("horaInicialEventoExterno", horainicial_ext);
                eventosExt.put("horaFinalEventoExterno", horafinal_ext);
                eventosExt.put("localEventoExterno", local_ext);
                eventosExt.put("descricaoEventoExterno", desc_ext);
                eventosExt.put("eventosExtId", eventoId);
                eventosExt.put("dataAdicaoext", FieldValue.serverTimestamp());

                colRef.add(eventosExt).addOnSuccessListener(documentReference -> {

                    Toast.makeText(getApplicationContext(), "Agendamento cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    cadastrarinfosexternos();
                    Intent intentCEX = new Intent(FormCadastroEventoExterno.this, PaginaPrincipal.class);
                    startActivity(intentCEX);

                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Erro ao cadastrar agendamento", Toast.LENGTH_SHORT).show());
            }
        });
        edit_obs_ext.setOnTouchListener((v, event) -> {
            if(v.getId() == R.id.edit_obs_ext){
                v.getParent().requestDisallowInterceptTouchEvent(true);

                if((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP){
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                }
            }
            return false;
        });
        edit_desc_atv_ext.setOnTouchListener((v, event) -> {
            if(v.getId() == R.id.edit_desc_atv_ext){
                v.getParent().requestDisallowInterceptTouchEvent(true);

                if((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP){
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                }
            }
            return false;
        });
        checkBoxSimEventoExterno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxNaoEventoExterno.setChecked(false);
                    edit_quais_equip_ext.setVisibility(View.VISIBLE);
                    String quaisequipobrigatorio = edit_quais_equip_ext.getText().toString().trim();
                } else{
                    edit_quais_equip_ext.setVisibility(View.GONE);
                }
            }
        });
        checkBoxNaoEventoExterno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxSimEventoExterno.setChecked(false);
                    edit_quais_equip_ext.setVisibility(View.GONE);
                }
            }
        });
        checkBoxSimEventoTransmissaoExterno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxNaoEventoTransmissaoExterno.setChecked(false);
                    checkBoxtransmissaoExterno.setVisibility(View.VISIBLE);
                    checkBoxgravacaoExterno.setVisibility(View.VISIBLE);
                    checkBoxAmbosExterno.setVisibility(View.VISIBLE);
                } else{
                    checkBoxtransmissaoExterno.setVisibility(View.GONE);
                    checkBoxgravacaoExterno.setVisibility(View.GONE);
                    checkBoxAmbosExterno.setVisibility(View.GONE);
                }
            }
        });
        checkBoxNaoEventoTransmissaoExterno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxSimEventoTransmissaoExterno.setChecked(false);
                    checkBoxtransmissaoExterno.setVisibility(View.GONE);
                    checkBoxgravacaoExterno.setVisibility(View.GONE);
                    checkBoxAmbosExterno.setVisibility(View.GONE);
                }
            }
        });
        checkBoxtransmissaoExterno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxgravacaoExterno.setChecked(false);
                    checkBoxAmbosExterno.setChecked(false);
                }
            }
        });
        checkBoxgravacaoExterno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxtransmissaoExterno.setChecked(false);
                    checkBoxAmbosExterno.setChecked(false);
                }
            }
        });
        checkBoxAmbosExterno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxtransmissaoExterno.setChecked(false);
                    checkBoxgravacaoExterno.setChecked(false);
                }
            }
        });
        checkBoxPresencialExterno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxHibridoExterno.setChecked(false);
                    checkBoxonlineExterno.setChecked(false);

                    checkBoxonlineExterno.setEnabled(false);
                    checkBoxHibridoExterno.setEnabled(false);
                } else{
                    checkBoxonlineExterno.setEnabled(true);
                    checkBoxHibridoExterno.setEnabled(true);
                }
            }
        });
        checkBoxHibridoExterno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    title_link_ext.setVisibility(View.VISIBLE);
                    checkBoxSimEventoLinkExterno.setVisibility(View.VISIBLE);
                    checkBoxNaoEventoLinkExterno.setVisibility(View.VISIBLE);

                    checkBoxPresencialExterno.setChecked(false);
                    checkBoxonlineExterno.setChecked(false);

                    checkBoxonlineExterno.setEnabled(false);
                    checkBoxPresencialExterno.setEnabled(false);
                }else{
                    title_link_ext.setVisibility(View.GONE);
                    checkBoxSimEventoLinkExterno.setVisibility(View.GONE);
                    checkBoxNaoEventoLinkExterno.setVisibility(View.GONE);

                    checkBoxonlineExterno.setEnabled(true);
                    checkBoxPresencialExterno.setEnabled(true);
                }
            }
        });
        checkBoxonlineExterno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    title_link_ext.setVisibility(View.VISIBLE);
                    checkBoxSimEventoLinkExterno.setVisibility(View.VISIBLE);
                    checkBoxNaoEventoLinkExterno.setVisibility(View.VISIBLE);

                    checkBoxPresencialExterno.setChecked(false);
                    checkBoxHibridoExterno.setChecked(false);

                    checkBoxHibridoExterno.setEnabled(false);
                    checkBoxPresencialExterno.setEnabled(false);
                }else{
                    title_link_ext.setVisibility(View.GONE);
                    checkBoxSimEventoLinkExterno.setVisibility(View.GONE);
                    checkBoxNaoEventoLinkExterno.setVisibility(View.GONE);

                    checkBoxHibridoExterno.setEnabled(true);
                    checkBoxPresencialExterno.setEnabled(true);
                }
            }
        });
        checkBoxSimEventoLinkExterno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxNaoEventoLinkExterno.setChecked(false);
                }
            }
        });
        checkBoxNaoEventoLinkExterno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxSimEventoLinkExterno.setChecked(false);
                }
            }
        });
        checkBoxSimMaterialGraficoExterno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxNaoMaterialGraficoExterno.setChecked(false);
                    title_solicitacao_material_grafico_ext.setVisibility(View.VISIBLE);
                    link_solicitacao_material_grafico_ext.setVisibility(View.VISIBLE);
                    link_solicitacao_material_grafico_ext.setMovementMethod(LinkMovementMethod.getInstance());
                }else{
                    title_solicitacao_material_grafico_ext.setVisibility(View.GONE);
                    link_solicitacao_material_grafico_ext.setVisibility(View.GONE);
                }
            }
        });
        checkBoxNaoMaterialGraficoExterno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxSimMaterialGraficoExterno.setChecked(false);
                    title_solicitacao_material_grafico_ext.setVisibility(View.GONE);
                    link_solicitacao_material_grafico_ext.setVisibility(View.GONE);
                }
            }
        });

        Intent intentEdicao = getIntent();
        if(intentEdicao.hasExtra("eventosExtId")){
            String eventExtId = getIntent().getStringExtra("eventosExtId");
            eventExtId = intentEdicao.getStringExtra("eventosExtId");
            carregarDadosEvento(eventExtId);

            bt_cadastrar_ext.setText("Atualizar");
            String finalEventId = eventExtId;
            bt_cadastrar_ext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    atualizarEvento(finalEventId);
                }
            });
            imageViewback_ext.setOnClickListener(v -> {
                Intent intentVoltarFormCadastroEvento = new Intent(FormCadastroEventoExterno.this, FormVisualizacaoExterno.class);
                startActivity(intentVoltarFormCadastroEvento);
                finish();
            });
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onBackPressed(){
        Toast.makeText(this, "Utilize o botão de voltar no topo da tela", Toast.LENGTH_SHORT).show();
    }
    public static class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState){

            Calendar calendario = Calendar.getInstance();
            int dia = calendario.get(Calendar.DAY_OF_MONTH);
            int mes = calendario.get(Calendar.MONTH);
            int ano = calendario.get(Calendar.YEAR);

            return new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, ano,mes, dia);
        }
        @Override
        public void onDateSet(android.widget.DatePicker view, int ano, int mes, int dia) {

            String tagClicado = getTag();

            assert tagClicado != null;
            if(tagClicado.equals("data_ext")){
                @SuppressLint("DefaultLocale")
                String dataFormatada = String.format("%02d/%02d/%04d", dia, mes+1, ano);
                edit_data_ext.setText(dataFormatada);
            } else if (tagClicado.equals("data_final_ext")){
                @SuppressLint("DefaultLocale")
                String dataFormatadaFinal = String.format("%02d/%02d/%04d", dia, mes+1, ano);
                edit_data_final_ext.setText(dataFormatadaFinal);
            }
        }
    }
    public static class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState){

            Calendar calendario = Calendar.getInstance();
            int hora = calendario.get(Calendar.HOUR_OF_DAY);
            int minutos = calendario.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, hora, minutos,true);
        }
        @Override
        public void onTimeSet(android.widget.TimePicker view, int horas, int minutos) {

            String tagClicado = getTag();

            assert tagClicado != null;
            if(tagClicado.equals("hora1")){
                @SuppressLint("DefaultLocale")
                String horaformatada = String.format("%02d:%02d",horas,minutos);
                edit_horas_ext.setText(horaformatada);
            }else if (tagClicado.equals("hora_final")){
                @SuppressLint("DefaultLocale")
                String horaformatadafinal = String.format("%02d:%02d",horas,minutos);
                edit_horas_final_ext.setText(horaformatadafinal);
            }
        }
    }
    private void carregarDadosEvento(String eventId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Log.d("Debug", "EventoID passado: " + eventId);

        // Faz uma consulta para buscar o documento com base no campo eventosId
        Query query = db.collection("eventosExt").whereEqualTo("eventosExtId", eventId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();

                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    // Recupera o primeiro documento da consulta (deve haver apenas um)
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    String eventoId = documentSnapshot.getString("eventosExtId");

                    String nomeExt = documentSnapshot.getString("nomedoEventoExterno");
                    String descricaoExt = documentSnapshot.getString("descricaoEventoExterno");
                    String datainicialExt = documentSnapshot.getString("dataInicialEventoExterno");
                    String datafinalExt = documentSnapshot.getString("dataFinalEventoExterno");
                    String horainicialExt = documentSnapshot.getString("horaInicialEventoExterno");
                    String horafinalExt = documentSnapshot.getString("horaFinalEventoExterno");
                    String localExt = documentSnapshot.getString("localEventoExterno");

                    DocumentReference infoEventoRef = FirebaseFirestore.getInstance().collection("infoEventosExt").document(eventoId);
                    infoEventoRef.get().addOnCompleteListener(infoTask -> {
                        if (infoTask.isSuccessful()) {
                            DocumentSnapshot infoEventoSnapshot = infoTask.getResult();
                            if (infoEventoSnapshot.exists()) {
                                // Recupere informações adicionais do evento da coleção "infoEventosExt"
                                String observacoesExt = infoEventoSnapshot.getString("observacoesEventoExterno");
                                String participantesExt = infoEventoSnapshot.getString("participantesEventoExterno");
                                String setorExt = infoEventoSnapshot.getString("setorResponsavelEventoExterno");
                                String equipamentosExt = infoEventoSnapshot.getString("equipamentosEventoExterno");
                                String quaisequipExt = infoEventoSnapshot.getString("quaisequipamentosEventoExterno");
                                String temtransmissaoExt = infoEventoSnapshot.getString("temtransmissaoEventoExterno");
                                String transmissaotipoExt = infoEventoSnapshot.getString("temTransmissaoTipoEventoExterno");
                                String gravacaotipoExt = infoEventoSnapshot.getString("temGravacaoTipoEventoExterno");
                                String ambostipoExt = infoEventoSnapshot.getString("temAmbosTipoEventoExterno");
                                String presencialExt = infoEventoSnapshot.getString("presencialEventoExterno");
                                String hibridoExt = infoEventoSnapshot.getString("hibridoEventoExterno");
                                String onlineExt = infoEventoSnapshot.getString("onlineEventoExterno");
                                String linkExt = infoEventoSnapshot.getString("linkTransmissaoEventoExterno");
                                String materiaisgraficosExt = infoEventoSnapshot.getString("materiaisgraficosEventoExterno");

                                // Preencha os campos do formulário com os dados do evento e informações adicionais
                                edit_nome_evento_ext.setText(nomeExt);
                                edit_setor_resp_ext.setText(setorExt);
                                edit_numeros_participantes_ext.setText(participantesExt);
                                edit_data_ext.setText(datainicialExt);
                                edit_data_final_ext.setText(datafinalExt);
                                edit_horas_ext.setText(horainicialExt);
                                edit_horas_final_ext.setText(horafinalExt);
                                edit_local_externo_ext.setText(localExt);
                                checkBoxSimEventoExterno.setChecked("Sim".equals(equipamentosExt));
                                edit_quais_equip_ext.setText(quaisequipExt);
                                checkBoxNaoEventoExterno.setChecked("Não".equals(equipamentosExt));
                                checkBoxSimEventoTransmissaoExterno.setChecked("Sim".equals(temtransmissaoExt));
                                checkBoxtransmissaoExterno.setChecked("Sim".equals(transmissaotipoExt));
                                checkBoxgravacaoExterno.setChecked("Sim".equals(gravacaotipoExt));
                                checkBoxAmbosExterno.setChecked("Sim".equals(ambostipoExt));
                                checkBoxNaoEventoTransmissaoExterno.setChecked("Não".equals(temtransmissaoExt));
                                checkBoxPresencialExterno.setChecked("Sim".equals(presencialExt));
                                checkBoxHibridoExterno.setChecked("Sim".equals(hibridoExt));
                                checkBoxSimEventoLinkExterno.setChecked("Sim".equals(linkExt));
                                checkBoxNaoEventoLinkExterno.setChecked("Não".equals(linkExt));
                                checkBoxonlineExterno.setChecked("Sim".equals(onlineExt));
                                checkBoxSimMaterialGraficoExterno.setChecked("Sim".equals(materiaisgraficosExt));
                                checkBoxNaoMaterialGraficoExterno.setChecked("Não".equals(materiaisgraficosExt));
                                edit_desc_atv_ext.setText(descricaoExt);
                                edit_obs_ext.setText(observacoesExt);

                            } else {
                                Log.d("FormCadastroEvento", "Documento infoEventos não encontrado");
                            }
                        } else {
                            Log.e("FormCadastroEvento", "Erro ao obter dados do infoEventos: ", infoTask.getException());
                        }
                    });
                } else {
                    // Nenhum documento encontrado com o eventosId fornecido
                    Log.d("Debug", "Nenhum documento encontrado com o eventoId: " + eventId);
                    showErrorAlertDilog(mensagens[0], "Evento não encontrado");
                }
            } else {
                // Lidar com erros ao buscar o documento
                Log.d("Debug", "Erro ao buscar o evento: " + task.getException());
                showErrorAlertDilog(mensagens[0], "Erro ao buscar o evento");
            }
        });
    }
    private void atualizarEvento(String eventId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Log.d("Debug", "EventoID passado: " + eventId);
        String novoNomeExt = edit_nome_evento_ext.getText().toString();
        String novoSetorExt = edit_setor_resp_ext.getText().toString();
        String novoParticipanteExt = edit_numeros_participantes_ext.getText().toString();
        String novaDataInicialExt = edit_data_ext.getText().toString();
        String novaDataFinalExt = edit_data_final_ext.getText().toString();
        String novaHoraInicialExt = edit_horas_ext.getText().toString();
        String novaHoraFinalExt = edit_horas_final_ext.getText().toString();
        String novoLocalExt = edit_local_externo_ext.getText().toString();
        String novaDescricaoExt = edit_desc_atv_ext.getText().toString();
        String novaObservacaoExt = edit_obs_ext.getText().toString();
        boolean novoEquipamentosExt = checkBoxSimEventoExterno.isChecked();
        String novoQuaisequipamentosExt = novoEquipamentosExt ? edit_quais_equip_ext.getText().toString() : "Não";
        boolean novoprecisaTransmissaoExt = checkBoxSimEventoTransmissaoExterno.isChecked();
        boolean novoTransmissaoExt = checkBoxtransmissaoExterno.isChecked();
        boolean novoGravacaoExt = checkBoxgravacaoExterno.isChecked();
        boolean novoAmbasExt = checkBoxAmbosExterno.isChecked();
        boolean novoPresencialExt = checkBoxPresencialExterno.isChecked();
        boolean novoHibridoExt = checkBoxHibridoExterno.isChecked();
        boolean novoOnlineExt = checkBoxonlineExterno.isChecked();
        boolean novoLinktransmissaoExt = checkBoxSimEventoLinkExterno.isChecked();
        boolean novoPrecisaMaterialGraficoExt = checkBoxSimMaterialGraficoExterno.isChecked();

        // Atualize os dados na coleção "eventosExt"
        Query query = db.collection("eventosExt").whereEqualTo("eventosExtId", eventId);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();

                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                    // Atualize os campos relevantes diretamente no documento
                    documentSnapshot.getReference().update(
                            "nomedoEventoExterno", novoNomeExt,
                            "localEventoExterno", novoLocalExt,
                            "descricaoEventoExterno", novaDescricaoExt,
                            "horaFinalEventoExterno", novaHoraFinalExt,
                            "horaInicialEventoExterno", novaHoraInicialExt,
                            "dataInicialEventoExterno", novaDataInicialExt,
                            "dataFinalEventoExterno", novaDataFinalExt
                    ).addOnSuccessListener(aVoid -> {

                        Log.d("Debug", "Dados do evento atualizados com sucesso na coleção 'eventos'");
                        showsucessAlertDilogAtualizado(mensagens[1],"Dados do evento atualizados com sucesso!");

                        // Se necessário, atualize os dados na coleção "infoEventos"
                        DocumentReference infoEventoRef = db.collection("infoEventosExt").document(eventId);

                        Map<String, Object> updatesExt = new HashMap<>();
                        updatesExt.put("setorResponsavelEventoExterno", novoSetorExt);
                        updatesExt.put("participantesEventoExterno", novoParticipanteExt);
                        updatesExt.put("observacoesEventoExterno", novaObservacaoExt);
                        updatesExt.put("equipamentosEventoExterno", novoEquipamentosExt ? "Sim" : "Não");
                        updatesExt.put("quaisequipamentosEventoExterno", novoQuaisequipamentosExt);
                        updatesExt.put("temtransmissaoEventoExterno", novoprecisaTransmissaoExt ? "Sim" : "Não");
                        updatesExt.put("materiaisgraficosEventoExterno", novoPrecisaMaterialGraficoExt ? "Sim" : "Não");
                        if(novoprecisaTransmissaoExt && novoTransmissaoExt){
                            updatesExt.put("temTransmissaoTipoEventoExterno", novoTransmissaoExt ? "Sim" : "Não");
                            updatesExt.put("temGravacaoTipoEventoExterno", novoGravacaoExt ? "Sim" : "Não");
                            updatesExt.put("temAmbosTipoEventoExterno", novoAmbasExt ? "Sim" : "Não");
                        } else {
                            if(novoprecisaTransmissaoExt && novoGravacaoExt){
                                updatesExt.put("temTransmissaoTipoEventoExterno", novoTransmissaoExt ? "Sim" : "Não");
                                updatesExt.put("temGravacaoTipoEventoExterno", novoGravacaoExt ? "Sim" : "Não");
                                updatesExt.put("temAmbosTipoEventoExterno", novoAmbasExt ? "Sim" : "Não");
                            } else {
                                if(novoprecisaTransmissaoExt && novoAmbasExt){
                                    updatesExt.put("temTransmissaoTipoEventoExterno", novoTransmissaoExt ? "Sim" : "Não");
                                    updatesExt.put("temGravacaoTipoEventoExterno", novoGravacaoExt ? "Sim" : "Não");
                                    updatesExt.put("temAmbosTipoEventoExterno", novoAmbasExt ? "Sim" : "Não");
                                }
                                else{
                                    updatesExt.put("temtransmissaoEventoExterno", "Não");
                                }
                            }
                        }
                        if(novoPresencialExt){
                            updatesExt.put("presencialEventoExterno", novoPresencialExt ? "Sim" : "Não");
                            updatesExt.put("hibridoEventoExterno", novoHibridoExt ? "Sim" : "Não");
                            updatesExt.put("onlineEventoExterno", novoOnlineExt ? "Sim" : "Não");
                        } else if(novoHibridoExt && novoLinktransmissaoExt){
                            updatesExt.put("hibridoEventoExterno", novoHibridoExt ? "Sim" : "Não");
                            updatesExt.put("presencialEventoExterno", novoPresencialExt ? "Sim" : "Não");
                            updatesExt.put("onlineEventoExterno", novoOnlineExt ? "Sim" : "Não");
                            updatesExt.put("linkTransmissaoEventoExterno", novoLinktransmissaoExt ? "Sim" : "Não");
                        } else if(novoOnlineExt && novoLinktransmissaoExt){
                            updatesExt.put("onlineEventoExterno", novoOnlineExt ? "Sim" : "Não");
                            updatesExt.put("hibridoEventoExterno", novoHibridoExt ? "Sim" : "Não");
                            updatesExt.put("presencialEventoExterno", novoPresencialExt ? "Sim" : "Não");
                            updatesExt.put("linkTransmissaoEventoExterno", novoLinktransmissaoExt ? "Sim" : "Não");
                        }

                        infoEventoRef.update(updatesExt).addOnSuccessListener(aVoid1 -> {
                            Log.d("Debug", "Dados do evento atualizados com sucesso na coleção 'infoEventos'");
                            showsucessAlertDilogAtualizado(mensagens[1],"Dados do evento atualizados com sucesso!");
                        }).addOnFailureListener(e -> {
                            Log.e("FormCadastroEvento", "Erro ao atualizar dados na coleção 'infoEventos'", e);
                            showErrorAlertDilog(mensagens[0], "Erro ao atualizar os dados do evento");
                        });
                    }).addOnFailureListener(e -> {
                        Log.e("FormCadastroEvento", "Erro ao atualizar dados na coleção 'eventos'", e);
                        showErrorAlertDilog(mensagens[0], "Erro ao atualizar os dados do evento");
                    });
                } else {
                    Log.d("Debug", "Nenhum documento encontrado com o eventoId: " + eventId);
                    showErrorAlertDilog(mensagens[0], "Evento não encontrado");
                }
            } else {
                Log.d("Debug", "Erro ao buscar o evento: " + task.getException());
                showErrorAlertDilog(mensagens[0], "Erro ao buscar o evento");
            }
        });
    }
    private void cadastrarinfosexternos(){

        String setor_responsavel_ext = edit_setor_resp_ext.getText().toString();
        String participantes_ext = edit_numeros_participantes_ext.getText().toString();
        String obs_ext = edit_obs_ext.getText().toString();
        String documentoIdExt = eventoId;

        boolean precisaEquipamentoExt = checkBoxSimEventoExterno.isChecked();
        boolean precisaTransmissaoExt = checkBoxSimEventoTransmissaoExterno.isChecked();
        boolean transmissaoExt = checkBoxtransmissaoExterno.isChecked();
        boolean gravacaoExt = checkBoxgravacaoExterno.isChecked();
        boolean ambasExt = checkBoxAmbosExterno.isChecked();
        boolean presencialExt = checkBoxPresencialExterno.isChecked();
        boolean hibridoExt = checkBoxHibridoExterno.isChecked();
        boolean onlineExt = checkBoxonlineExterno.isChecked();
        boolean linktransmissaoExt = checkBoxSimEventoLinkExterno.isChecked();
        boolean precisaMaterialGraficoExt = checkBoxSimMaterialGraficoExterno.isChecked();

        String equipamentosExt = precisaEquipamentoExt ? edit_quais_equip_ext.getText().toString() : "Não";
        String materialgraficoExt = precisaMaterialGraficoExt ? "Sim" : "Não";

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference infoExterno = db.collection("infoEventosExt").document(documentoIdExt);

        Map<String, Object> infoeventosExt = new HashMap<>();
        infoeventosExt.put("setorResponsavelEventoExterno", setor_responsavel_ext);
        infoeventosExt.put("participantesEventoExterno",participantes_ext);
        infoeventosExt.put("observacoesEventoExterno", obs_ext);
        infoeventosExt.put("eventosExtId", eventoId);
        infoeventosExt.put("equipamentosEventoExterno", precisaEquipamentoExt ? "Sim" : "Não");
        infoeventosExt.put("quaisequipamentosEventoExterno", equipamentosExt);
        infoeventosExt.put("temtransmissaoEventoExterno", precisaTransmissaoExt ? "Sim" : "Não");
        infoeventosExt.put("materiaisgraficosEventoExterno", materialgraficoExt);
        if(precisaTransmissaoExt && transmissaoExt || gravacaoExt || ambasExt){
            infoeventosExt.put("temTransmissaoTipoEventoExterno", transmissaoExt ? "Sim" : "Não");
            infoeventosExt.put("temGravacaoTipoEventoExterno", gravacaoExt ? "Sim" : "Não");
            infoeventosExt.put("temAmbosTipoEventoExterno", ambasExt ? "Sim" : "Não");
        } else {
            infoeventosExt.put("temtransmissaoEventoExterno", "Não");
        }
        if(presencialExt){
            infoeventosExt.put("presencialEventoExterno", "Sim");
        } else if(hibridoExt && linktransmissaoExt){
            infoeventosExt.put("hibridoEventoExterno", "Sim");
            infoeventosExt.put("linkTransmissaoEventoExterno", linktransmissaoExt ? "Sim" : "Não");
        } else if(onlineExt && linktransmissaoExt){
            infoeventosExt.put("onlineEventoExterno", "Sim");
            infoeventosExt.put("linkTransmissaoEventoExterno", linktransmissaoExt ? "Sim" : "Não");
        }

        infoExterno.set(infoeventosExt).addOnSuccessListener(unused -> Log.d("Firestore", "Informações do evento cadastradas com sucesso!"))
                .addOnFailureListener(e -> Log.e("Firestore", "Erro ao cadastrar as informações do evento: " + e.getMessage()));
    }
    private void showErrorAlertDilog(String titulo,String erro){

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        View view = LayoutInflater.from(this).inflate(
                R.layout.layout_erro_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitleAlertDialog)).setText(titulo);
        ((TextView) view.findViewById(R.id.textMessage)).setText(erro);
        ((Button) view.findViewById(R.id.buttonAction)).setText("Ok");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.error);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    private void showsucessAlertDilogAtualizado(String titulo,String erro){

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        View view = LayoutInflater.from(this).inflate(
                R.layout.layout_sucess_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitleAlertDialog)).setText(titulo);
        ((TextView) view.findViewById(R.id.textMessage)).setText(erro);
        ((Button) view.findViewById(R.id.buttonAction)).setText("Ok");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.done);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent intentCE = new Intent(FormCadastroEventoExterno.this, FormVisualizacaoExterno.class);
                intentCE.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentCE);
            }
        });

        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    private void IniciarComponentes(){
        imageViewoff_ext = findViewById(R.id.imageViewoff_ext);
        imageViewback_ext = findViewById(R.id.imageViewback_ext);
        imageViewData_ext = findViewById(R.id.imageViewData_ext);
        imageViewDataFinal_ext = findViewById(R.id.imageViewDataFinal_ext);
        imageViewHora_ext = findViewById(R.id.imageViewHora_ext);
        imageViewHoraFinal_ext = findViewById(R.id.imageViewHoraFinal_ext);

        edit_data_ext = findViewById(R.id.edit_data_ext);
        edit_data_final_ext = findViewById(R.id.edit_data_final_ext);
        edit_horas_ext = findViewById(R.id.edit_horas_ext);
        edit_horas_final_ext = findViewById(R.id.edit_horas_final_ext);

        bt_cadastrar_ext = findViewById(R.id.bt_cadastrar_ext);

        //CheckBox no front-end
        checkBoxSimEventoExterno = findViewById(R.id.checkBoxSimEventoExterno);
        checkBoxNaoEventoExterno = findViewById(R.id.checkBoxNãoEventoExterno);
        checkBoxSimEventoTransmissaoExterno = findViewById(R.id.checkBoxSimEventoTransmissaoExterno);
        checkBoxNaoEventoTransmissaoExterno = findViewById(R.id.checkBoxNãoEventoTransmissaoExterno);
        checkBoxtransmissaoExterno = findViewById(R.id.checkBoxtransmissaoExterno);
        checkBoxgravacaoExterno = findViewById(R.id.checkBoxgravacaoExterno);
        checkBoxAmbosExterno = findViewById(R.id.checkBoxAmbosExterno);
        checkBoxPresencialExterno = findViewById(R.id.checkBoxPresencialExterno);
        checkBoxHibridoExterno = findViewById(R.id.checkBoxHibridoExterno);
        checkBoxonlineExterno = findViewById(R.id.checkBoxonlineExterno);
        checkBoxSimEventoLinkExterno = findViewById(R.id.checkBoxSimEventoLinkExterno);
        checkBoxNaoEventoLinkExterno = findViewById(R.id.checkBoxNãoEventoLinkExterno);
        checkBoxSimMaterialGraficoExterno = findViewById(R.id.checkBoxSimMaterialGraficoExterno);
        checkBoxNaoMaterialGraficoExterno = findViewById(R.id.checkBoxNãoMaterialGraficoExterno);
        edit_quais_equip_ext = findViewById(R.id.edit_quais_equip_ext);
        title_link_ext = findViewById(R.id.title_link_ext);
        title_solicitacao_material_grafico_ext = findViewById(R.id.title_solicitacao_material_grafico_ext);
        link_solicitacao_material_grafico_ext = findViewById(R.id.link_solicitacao_material_grafico_ext);

        edit_nome_evento_ext = findViewById(R.id.edit_nome_evento_ext);
        edit_setor_resp_ext = findViewById(R.id.edit_setor_resp_ext);
        edit_numeros_participantes_ext = findViewById(R.id.edit_numeros_participantes_ext);
        edit_local_externo_ext = findViewById(R.id.edit_local_externo_ext);
        edit_desc_atv_ext = findViewById(R.id.edit_desc_atv_ext);
        edit_obs_ext = findViewById(R.id.edit_obs_ext);

        //Desabilitar os campos de data e hora.
        edit_horas_ext.setEnabled(false);
        edit_horas_final_ext.setEnabled(false);
        edit_data_ext.setEnabled(false);
        edit_data_final_ext.setEnabled(false);
    }
}