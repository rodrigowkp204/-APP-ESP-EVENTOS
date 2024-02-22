package com.example.esp_eventos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class FormCadastroEvento extends BaseActivity {
    private EditText edit_nome_evento,edit_setor_resp, edit_numeros_participantes, edit_desc_atv, edit_obs ;
    String[] locais = {"Isabel dos Santos","Paulo Freire","Multimídia 1","Multimídia 2","Multimídia 3","Tecnologias Educacionais",
            "Educação e Saúde", "Diretoria", "Pesquisa e Desenvolvimento", "Estágio", "Núcleo de Planejamento", "Sala de Reunião"};
    String[] mensagens = {"Algo deu errado","Sucesso"};
    CheckBox checkBoxSimEvento, checkBoxNaoEvento,
            checkBoxSimEventoTransmissao, checkBoxNaoEventoTransmissao, checkBoxtransmissao, checkBoxgravacao,
            checkBoxAmbos, checkBoxPresencial, checkBoxHibrido, checkBoxonline,
            checkBoxSimEventoLink, checkBoxNaoEventoLink, checkBoxSimMaterialGrafico, checkBoxNaoMaterialGrafico;
    TextInputEditText edit_quais_equip;
    TextView title_link, title_solicitacao_material_grafico, link_solicitacao_material_grafico;
    String eventoId = UUID.randomUUID().toString();
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterlocais;
    ImageView imageViewData; //Instanciando o que você deseja puxar
    ImageView imageViewDataFinal; //Instanciando o que você deseja puxar
    ImageView imageViewoff;  //Instanciando o que você deseja puxar
    ImageView imageViewHora; //Instanciando o que você deseja puxar
    ImageView imageViewHoraFinal; //Instanciando o que você deseja puxar
    ImageView imageViewback; //Instanciando o que você deseja puxar
    Button bt_cadastrar;
    private Context context;
    @SuppressLint("StaticFieldLeak")
    static EditText editTextData;
    @SuppressLint("StaticFieldLeak")
    static EditText editTextData2;
    @SuppressLint("StaticFieldLeak")
    static EditText editTextDataFinal;
    @SuppressLint("StaticFieldLeak")
    static EditText editTextDataFinal2;
    @SuppressLint("StaticFieldLeak")
    static EditText editTextHoraInicial;
    @SuppressLint("StaticFieldLeak")
    static EditText editTextHoraFinal;
    ScrollView scrollview;

    @SuppressLint({"MissingInflatedId", "CutPasteId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro_evento);

        FirebaseApp.initializeApp(this);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .build();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);

        getSupportActionBar().hide();
        IniciarComponentes();

        adapterlocais = new ArrayAdapter<>(this, R.layout.lista_item_local, locais);
        autoCompleteTxt.setAdapter(adapterlocais);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.azul1));

        //Pegar a data do celular
        Calendar calendario = Calendar.getInstance();
        Integer dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH);
        Integer ano = calendario.get(Calendar.YEAR);
        Integer hora = calendario.get(Calendar.HOUR_OF_DAY);
        Integer minutos = calendario.get(Calendar.MINUTE);

        @SuppressLint("DefaultLocale")
        String dataFormatada = String.format("%02d/%02d/%04d", dia, mes+1, ano);
        editTextData.setText(dataFormatada);
        editTextData2.setText(dataFormatada);
        @SuppressLint("DefaultLocale")
        String horaformatada = String.format("%02d:%02d",hora,minutos);
        editTextHoraInicial.setText(horaformatada);

        imageViewData.setOnClickListener(v -> {
            DialogFragment dialogFragment = new DatePicker();
            dialogFragment.show(getSupportFragmentManager(), "data1");
        });
        imageViewData.setOnClickListener(v -> {
            DialogFragment dialogFragment = new DatePicker();
            dialogFragment.show(getSupportFragmentManager(), "data2");
        });
        imageViewDataFinal.setOnClickListener(v -> {
            DialogFragment dialogFragment = new DatePicker();
            dialogFragment.show(getSupportFragmentManager(), "datafinal1");
        });
        imageViewDataFinal.setOnClickListener(v -> {
            DialogFragment dialogFragment = new DatePicker();
            dialogFragment.show(getSupportFragmentManager(), "datafinal2");
        });
        imageViewHora.setOnClickListener(v -> {
            DialogFragment dialogFragment = new TimePicker();
            dialogFragment.show(getSupportFragmentManager(), "hora1");
        });
        imageViewHoraFinal.setOnClickListener(v -> {
            DialogFragment dialogFragmentFinal = new TimePicker();
            dialogFragmentFinal.show(getSupportFragmentManager(),"hora2");
        });
        imageViewoff.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intentDeslogarFormCadastroEvento = new Intent(FormCadastroEvento.this, Activity_Splash_Form_Login.class);
            startActivity(intentDeslogarFormCadastroEvento);
            finish();
        });
        imageViewback.setOnClickListener(v -> {
            Intent intentVoltarFormCadastroEvento = new Intent(FormCadastroEvento.this, PaginaPrincipal.class);
            startActivity(intentVoltarFormCadastroEvento);
            finish();
        });
        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = editTextData2.getText().toString();
                String datafinal = editTextDataFinal2.getText().toString();
                String hora = editTextHoraInicial.getText().toString();
                String horafinal = editTextHoraFinal.getText().toString();
                String nome = edit_nome_evento.getText().toString();
                String local = autoCompleteTxt.getText().toString();
                String descricao = edit_desc_atv.getText().toString();
                String setor = edit_setor_resp.getText().toString();
                String participantes = edit_numeros_participantes.getText().toString();
                boolean checkboxequip = checkBoxSimEvento.isChecked();
                String equipamentosquais = edit_quais_equip.getText().toString().trim();
                boolean precisaTransmissao = checkBoxSimEventoTransmissao.isChecked();
                boolean transmissao = checkBoxtransmissao.isChecked();
                boolean gravacao = checkBoxgravacao.isChecked();
                boolean ambas = checkBoxAmbos.isChecked();
                boolean presencial = checkBoxPresencial.isChecked();
                boolean hibrido = checkBoxHibrido.isChecked();
                boolean online = checkBoxonline.isChecked();
                boolean linktransmissao = checkBoxSimEventoLink.isChecked();
                boolean precisaMaterialGrafico = checkBoxSimMaterialGrafico.isChecked();


                CollectionReference evento = db.collection("eventos");

                Query querytudoigual = evento.whereEqualTo("localdoEvento", local)
                        .whereEqualTo("datadoEvento", data)
                        .whereEqualTo("datadoEventoFinal", datafinal)
                        .whereEqualTo("horadoEventoInicial", hora)
                        .whereEqualTo("horaEventoFinal", horafinal);

                querytudoigual.get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        boolean isEmptyField = data.isEmpty() || datafinal.isEmpty() || hora.isEmpty() || horafinal.isEmpty()
                                || nome.isEmpty() || setor.isEmpty() || participantes.isEmpty()
                                || local.isEmpty() || descricao.isEmpty() || !checkBoxSimEvento.isChecked() && !checkBoxNaoEvento.isChecked()
                                || !checkBoxSimMaterialGrafico.isChecked() && !checkBoxNaoMaterialGrafico.isChecked();

                        QuerySnapshot querySnapshot = task.getResult();

                        if(querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Já existe um evento com as mesmas informações
                            // Realize as ações apropriadas, como exibir uma mensagem de erro ou retornar do método
                            showErrorAlertDilog(mensagens[0],"Já existe um evento com as mesmas informações");

                        } else {
                            // Não existe um evento com as mesmas informações, verifique a segunda restrição
                            Query secondQuery = evento.whereEqualTo("localdoEvento", local);

                            secondQuery.get().addOnCompleteListener(secondTask -> {
                                if(secondTask.isSuccessful()) {
                                    QuerySnapshot secondQuerySnapshot = secondTask.getResult();

                                    if(secondQuerySnapshot != null && !secondQuerySnapshot.isEmpty()) {
                                        // Já existe um evento com o mesmo local, verifique se a data está dentro do intervalo
                                        boolean isDataHoraWithinRange = false;

                                        for (DocumentSnapshot document : secondQuerySnapshot.getDocuments()) {
                                            String dataEvento = document.getString("datadoEvento");
                                            String dataFinalEvento = document.getString("datadoEventoFinal");
                                            String horaInicialEvento = document.getString("horadoEventoInicial");
                                            String horaFinalEvento = document.getString("horaEventoFinal");

                                            if (isDataHoraWithinRange(dataEvento, dataFinalEvento, horaInicialEvento, horaFinalEvento, data, datafinal, hora, horafinal)) {
                                                isDataHoraWithinRange = true;
                                                break;
                                            }
                                        }
                                        if (isDataHoraWithinRange) {
                                            // Já existe um evento com intervalo de tempo que contém o evento a ser cadastrado
                                            // Realize as ações apropriadas, como exibir uma mensagem de erro ou retornar do método
                                            showErrorAlertDilog(mensagens[0],"Já existe um agendamento para essa data e local ou conflito de data e hora");
                                        } else if (isEmptyField){
                                            showErrorAlertDilog(mensagens[0],"Preencha todos os campos");
                                        }else if (checkboxequip && equipamentosquais.isEmpty()){
                                            showErrorAlertDilog(mensagens[0],"Preencha o campo dos equipamentos necessários");
                                        } else if(precisaTransmissao && !transmissao && !gravacao && !ambas){
                                            showErrorAlertDilog(mensagens[0],"Preencha o campo dos tipos de transmissão");
                                        } else if(hibrido && !linktransmissao ){
                                            showErrorAlertDilog(mensagens[0],"Preencha o campo do link do evento");
                                        } else if(online && !linktransmissao ){
                                            showErrorAlertDilog(mensagens[0],"Preencha o campo do link do evento");
                                        }
                                        else {
                                            int capacidadeMaxima = 0; // Capacidade máxima inicializada com 0
                                            switch (local) {
                                                case "Isabel dos Santos":
                                                    capacidadeMaxima = 80;
                                                    break;
                                                case "Paulo Freire":
                                                    capacidadeMaxima = 150;
                                                    break;
                                                case "Multimídia 1":
                                                case "Multimídia 2":
                                                case "Multimídia 3":
                                                    capacidadeMaxima = 50;
                                                    break;
                                                case "Tecnologias Educacionais":
                                                    capacidadeMaxima = 8;
                                                    break;
                                                case "Educação e Saúde":
                                                    capacidadeMaxima = 8;
                                                    break;
                                                case "Diretoria":
                                                    capacidadeMaxima = 6;
                                                    break;
                                                case "Pesquisa e Desenvolvimento":
                                                    capacidadeMaxima = 15;
                                                    break;
                                                case "Estágio":
                                                    capacidadeMaxima = 8;
                                                    break;
                                                case "Núcleo de Planejamento":
                                                    capacidadeMaxima = 6;
                                                    break;
                                                case "Sala de Reunião":
                                                    capacidadeMaxima = 6;
                                                    break;

                                            }
                                            if (Integer.parseInt(participantes) > capacidadeMaxima){
                                                // Verificar se o número de participantes excede a capacidade máxima
                                                showErrorAlertDilog(mensagens[0], "A capacidade máxima para este local é de " + capacidadeMaxima + " participantes");

                                            } else {
                                                // Não existem eventos com intervalo de tempo que contém o evento a ser cadastrado
                                                // Proceda com o cadastro do novo evento
                                                Map<String, Object> eventos = new HashMap<>();
                                                eventos.put("datadoEvento", data);
                                                eventos.put("datadoEventoFinal", datafinal);
                                                eventos.put("horadoEventoInicial", hora);
                                                eventos.put("horaEventoFinal", horafinal);
                                                eventos.put("nomedoEvento", nome);
                                                eventos.put("localdoEvento", local);
                                                eventos.put("descricaodaAtividade", descricao);
                                                eventos.put("eventosId", eventoId);
                                                eventos.put("dataAdicao", FieldValue.serverTimestamp());

                                                evento.add(eventos).addOnSuccessListener(documentReference -> {
                                                    //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

                                                    //CalendarIntegration calendarIntegration = new CalendarIntegration(getApplicationContext());

                                                    //DateTime startDateTime = new DateTime(data + "T" + hora + ":00+00:00");
                                                    //DateTime endDateTime = new DateTime(datafinal + "T" + horafinal + ":00+00:00");

                                                    //calendarIntegration.createEvent(nome, descricao, local, data, datafinal, hora, horafinal);
                                                    cadastrarinfos();
                                                    showsucessAlertDilog(mensagens[1],"Agendamento cadastrado com sucesso");

                                                }).addOnFailureListener(e -> showErrorAlertDilog(mensagens[0],"Erro ao cadastrar agendamento"));
                                            }
                                        }
                                    } else {
                                        if(isEmptyField){
                                            showErrorAlertDilog(mensagens[0],"Preencha todos os campos");
                                        }else if (checkboxequip && equipamentosquais.isEmpty()){
                                            showErrorAlertDilog(mensagens[0],"Preencha o campo dos equipamentos necessários");
                                        } else if(precisaTransmissao && !transmissao && !gravacao && !ambas){
                                            showErrorAlertDilog(mensagens[0],"Preencha o campo dos tipos de transmissão");
                                        } else if(hibrido && !linktransmissao ){
                                            showErrorAlertDilog(mensagens[0],"Preencha o campo do link do evento");
                                        } else if(online && !linktransmissao ){
                                            showErrorAlertDilog(mensagens[0],"Preencha o campo do link do evento");
                                        } else {
                                            int capacidadeMaxima = 0; // Capacidade máxima inicializada com 0
                                            switch (local) {
                                                case "Isabel dos Santos":
                                                    capacidadeMaxima = 80;
                                                    break;
                                                case "Paulo Freire":
                                                    capacidadeMaxima = 150;
                                                    break;
                                                case "Multimídia 1":
                                                case "Multimídia 2":
                                                case "Multimídia 3":
                                                    capacidadeMaxima = 50;
                                                    break;
                                                case "Tecnologias Educacionais":
                                                    capacidadeMaxima = 8;
                                                    break;
                                                case "Educação e Saúde":
                                                    capacidadeMaxima = 8;
                                                    break;
                                                case "Diretoria":
                                                    capacidadeMaxima = 6;
                                                    break;
                                                case "Pesquisa e Desenvolvimento":
                                                    capacidadeMaxima = 15;
                                                    break;
                                                case "Estágio":
                                                    capacidadeMaxima = 8;
                                                    break;
                                                case "Núcleo de Planejamento":
                                                    capacidadeMaxima = 6;
                                                    break;
                                                case "Sala de Reunião":
                                                    capacidadeMaxima = 6;
                                                    break;
                                            }
                                            if(Integer.parseInt(participantes) > capacidadeMaxima){
                                                // Verificar se o número de participantes excede a capacidade máxima
                                                showErrorAlertDilog(mensagens[0], "A capacidade máxima para este local é de " + capacidadeMaxima + " participantes");
                                            } else {
                                                // Não existem eventos com o mesmo local
                                                // Proceda com o cadastro do novo evento
                                                Map<String, Object> eventos = new HashMap<>();
                                                eventos.put("datadoEvento", data);
                                                eventos.put("datadoEventoFinal", datafinal);
                                                eventos.put("horadoEventoInicial", hora);
                                                eventos.put("horaEventoFinal", horafinal);
                                                eventos.put("nomedoEvento", nome);
                                                eventos.put("localdoEvento", local);
                                                eventos.put("descricaodaAtividade", descricao);
                                                eventos.put("eventosId", eventoId);
                                                eventos.put("dataAdicao", FieldValue.serverTimestamp());

                                                evento.add(eventos).addOnSuccessListener(documentReference -> {
                                                    cadastrarinfos();
                                                    showsucessAlertDilog(mensagens[1],"Agendamento cadastrado com sucesso");
                                                }).addOnFailureListener(e -> showErrorAlertDilog(mensagens[0],"Erro ao cadastrar agendamento"));
                                            }
                                        }
                                    }
                                } else {
                                    // Lida com possíveis erros na consulta do Firestore
                                    showErrorAlertDilog(mensagens[0],"Erro ao verificar agendamento");
                                }
                            });
                        }
                    } else{
                        // Lida com possíveis erros na consulta do Firestore
                        showErrorAlertDilog(mensagens[0],"Erro ao verificar agendamento");
                    }
                });
            }
            private boolean isDataHoraWithinRange(String dataEvento, String dataFinalEvento, String horaInicialEvento, String horaFinalEvento, String data, String datafinal, String hora, String horafinal) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    Date eventoStartDateTime = sdf.parse(dataEvento + " " + horaInicialEvento);
                    Date eventoEndDateTime = sdf.parse(dataFinalEvento + " " + horaFinalEvento);
                    Date novoEventoStartDateTime = sdf.parse(data + " " + hora);
                    Date novoEventoEndDateTime = sdf.parse(datafinal + " " + horafinal);

                    return eventoStartDateTime != null && eventoEndDateTime != null && novoEventoStartDateTime != null && novoEventoEndDateTime != null &&
                            novoEventoStartDateTime.compareTo(eventoEndDateTime) <= 0 && novoEventoEndDateTime.compareTo(eventoStartDateTime) >= 0;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });
        edit_obs.setOnTouchListener((v, event) -> {
            if(v.getId() == R.id.edit_obs){
                v.getParent().requestDisallowInterceptTouchEvent(true);
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                }
            }
            return false;
        });
        edit_desc_atv.setOnTouchListener((v, event) -> {
            if(v.getId() == R.id.edit_desc_atv){
                v.getParent().requestDisallowInterceptTouchEvent(true);
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                }
            }
            return false;
        });
        checkBoxSimEvento.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxNaoEvento.setChecked(false);
                    edit_quais_equip.setVisibility(View.VISIBLE);
                    String quaisequipobrigatorio = edit_quais_equip.getText().toString().trim();
                } else{
                    edit_quais_equip.setVisibility(View.GONE);
                }
            }
        });
        checkBoxNaoEvento.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxSimEvento.setChecked(false);
                    edit_quais_equip.setVisibility(View.GONE);
                }
            }
        });
        checkBoxSimEventoTransmissao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxNaoEventoTransmissao.setChecked(false);
                    checkBoxtransmissao.setVisibility(View.VISIBLE);
                    checkBoxgravacao.setVisibility(View.VISIBLE);
                    checkBoxAmbos.setVisibility(View.VISIBLE);
                } else{
                    checkBoxtransmissao.setVisibility(View.GONE);
                    checkBoxgravacao.setVisibility(View.GONE);
                    checkBoxAmbos.setVisibility(View.GONE);
                }
            }
        });
        checkBoxNaoEventoTransmissao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxSimEventoTransmissao.setChecked(false);
                    checkBoxtransmissao.setVisibility(View.GONE);
                    checkBoxgravacao.setVisibility(View.GONE);
                    checkBoxAmbos.setVisibility(View.GONE);
                }
            }
        });
        checkBoxtransmissao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxgravacao.setChecked(false);
                    checkBoxAmbos.setChecked(false);
                }
            }
        });
        checkBoxgravacao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxtransmissao.setChecked(false);
                    checkBoxAmbos.setChecked(false);
                }
            }
        });
        checkBoxAmbos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxtransmissao.setChecked(false);
                    checkBoxgravacao.setChecked(false);
                }
            }
        });
        checkBoxPresencial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxHibrido.setChecked(false);
                    checkBoxonline.setChecked(false);

                    checkBoxonline.setEnabled(false);
                    checkBoxHibrido.setEnabled(false);
                } else{
                    checkBoxonline.setEnabled(true);
                    checkBoxHibrido.setEnabled(true);
                }
            }
        });
        checkBoxHibrido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    title_link.setVisibility(View.VISIBLE);
                    checkBoxSimEventoLink.setVisibility(View.VISIBLE);
                    checkBoxNaoEventoLink.setVisibility(View.VISIBLE);

                    checkBoxPresencial.setChecked(false);
                    checkBoxonline.setChecked(false);

                    checkBoxonline.setEnabled(false);
                    checkBoxPresencial.setEnabled(false);
                }else{
                    title_link.setVisibility(View.GONE);
                    checkBoxSimEventoLink.setVisibility(View.GONE);
                    checkBoxNaoEventoLink.setVisibility(View.GONE);

                    checkBoxonline.setEnabled(true);
                    checkBoxPresencial.setEnabled(true);
                }
            }
        });
        checkBoxonline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    title_link.setVisibility(View.VISIBLE);
                    checkBoxSimEventoLink.setVisibility(View.VISIBLE);
                    checkBoxNaoEventoLink.setVisibility(View.VISIBLE);

                    checkBoxPresencial.setChecked(false);
                    checkBoxHibrido.setChecked(false);

                    checkBoxHibrido.setEnabled(false);
                    checkBoxPresencial.setEnabled(false);
                }else{
                    title_link.setVisibility(View.GONE);
                    checkBoxSimEventoLink.setVisibility(View.GONE);
                    checkBoxNaoEventoLink.setVisibility(View.GONE);

                    checkBoxHibrido.setEnabled(true);
                    checkBoxPresencial.setEnabled(true);
                }
            }
        });
        checkBoxSimEventoLink.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxNaoEventoLink.setChecked(false);
                }
            }
        });
        checkBoxNaoEventoLink.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxSimEventoLink.setChecked(false);
                }
            }
        });
        checkBoxSimMaterialGrafico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxNaoMaterialGrafico.setChecked(false);
                    title_solicitacao_material_grafico.setVisibility(View.VISIBLE);
                    link_solicitacao_material_grafico.setVisibility(View.VISIBLE);
                    link_solicitacao_material_grafico.setMovementMethod(LinkMovementMethod.getInstance());
                }else{
                    title_solicitacao_material_grafico.setVisibility(View.GONE);
                    link_solicitacao_material_grafico.setVisibility(View.GONE);
                }
            }
        });
        checkBoxNaoMaterialGrafico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxSimMaterialGrafico.setChecked(false);
                    title_solicitacao_material_grafico.setVisibility(View.GONE);
                    link_solicitacao_material_grafico.setVisibility(View.GONE);
                }
            }
        });

        Intent intentEdicao = getIntent();
        if(intentEdicao.hasExtra("eventosId")){
            String eventId = getIntent().getStringExtra("eventosId");
            eventId = intentEdicao.getStringExtra("eventosId");
            carregarDadosEvento(eventId);

            bt_cadastrar.setText("Atualizar");
            String finalEventId = eventId;
            bt_cadastrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    atualizarEvento(finalEventId);
                }
            });
            imageViewback.setOnClickListener(v -> {
                Intent intentVoltarFormCadastroEvento = new Intent(FormCadastroEvento.this, FormVisualizacao.class);
                startActivity(intentVoltarFormCadastroEvento);
                finish();
            });
        }
    }
    private void carregarDadosEvento(String eventId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Log.d("Debug", "EventoID passado: " + eventId);

        // Faz uma consulta para buscar o documento com base no campo eventosId
        Query query = db.collection("eventos").whereEqualTo("eventosId", eventId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();

                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    // Recupera o primeiro documento da consulta (deve haver apenas um)
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    String eventoId = documentSnapshot.getString("eventosId");

                    String nome = documentSnapshot.getString("nomedoEvento");
                    String descricao = documentSnapshot.getString("descricaodaAtividade");
                    String datainicial = documentSnapshot.getString("datadoEvento");
                    String datafinal = documentSnapshot.getString("datadoEventoFinal");
                    String horainicial = documentSnapshot.getString("horadoEventoInicial");
                    String horafinal = documentSnapshot.getString("horaEventoFinal");
                    String local = documentSnapshot.getString("localdoEvento");

                    DocumentReference infoEventoRef = FirebaseFirestore.getInstance().collection("infoEventos").document(eventoId);
                    infoEventoRef.get().addOnCompleteListener(infoTask -> {
                        if (infoTask.isSuccessful()) {
                            DocumentSnapshot infoEventoSnapshot = infoTask.getResult();
                            if (infoEventoSnapshot.exists()) {
                                // Recupere informações adicionais do evento da coleção "info_eventos"
                                String observacoes = infoEventoSnapshot.getString("observacoes");
                                String participantes = infoEventoSnapshot.getString("numerosdeParticipantes");
                                String setor = infoEventoSnapshot.getString("setorResponsavel");
                                String equipamentos = infoEventoSnapshot.getString("equipamentos");
                                String quaisequip = infoEventoSnapshot.getString("quaisequipamentos");
                                String temtransmissao = infoEventoSnapshot.getString("temtransmissao");
                                String transmissaotipo = infoEventoSnapshot.getString("temTransmissaoTipo");
                                String gravacaotipo = infoEventoSnapshot.getString("temGravacaoTipo");
                                String ambostipo = infoEventoSnapshot.getString("temAmbosTipo");
                                String presencial = infoEventoSnapshot.getString("presencial");
                                String hibrido = infoEventoSnapshot.getString("hibrido");
                                String online = infoEventoSnapshot.getString("online");
                                String link = infoEventoSnapshot.getString("linkTransmissao");
                                String materiaisgraficos = infoEventoSnapshot.getString("materiaisgraficos");


                                // Preencha os campos do formulário com os dados do evento e informações adicionais
                                edit_nome_evento.setText(nome);
                                edit_setor_resp.setText(setor);
                                edit_numeros_participantes.setText(participantes);
                                editTextData.setText(datainicial);
                                editTextDataFinal.setText(datafinal);
                                editTextHoraInicial.setText(horainicial);
                                editTextHoraFinal.setText(horafinal);
                                autoCompleteTxt.setText(local);
                                checkBoxSimEvento.setChecked("Sim".equals(equipamentos));
                                edit_quais_equip.setText(quaisequip);
                                checkBoxNaoEvento.setChecked("Não".equals(equipamentos));
                                checkBoxSimEventoTransmissao.setChecked("Sim".equals(temtransmissao));
                                checkBoxtransmissao.setChecked("Sim".equals(transmissaotipo));
                                checkBoxgravacao.setChecked("Sim".equals(gravacaotipo));
                                checkBoxAmbos.setChecked("Sim".equals(ambostipo));
                                checkBoxNaoEventoTransmissao.setChecked("Não".equals(temtransmissao));
                                checkBoxPresencial.setChecked("Sim".equals(presencial));
                                checkBoxHibrido.setChecked("Sim".equals(hibrido));
                                checkBoxSimEventoLink.setChecked("Sim".equals(link));
                                checkBoxNaoEventoLink.setChecked("Não".equals(link));
                                checkBoxonline.setChecked("Sim".equals(online));
                                checkBoxSimMaterialGrafico.setChecked("Sim".equals(materiaisgraficos));
                                checkBoxNaoMaterialGrafico.setChecked("Não".equals(materiaisgraficos));
                                edit_desc_atv.setText(descricao);
                                edit_obs.setText(observacoes);
                                adapterlocais = new ArrayAdapter<>(this, R.layout.lista_item_local, locais);
                                autoCompleteTxt.setAdapter(adapterlocais);

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

        String novoNome = edit_nome_evento.getText().toString();
        String novoSetor = edit_setor_resp.getText().toString();
        String novoParticipante = edit_numeros_participantes.getText().toString();
        String novaDataInicial = editTextData.getText().toString();
        String novaDataFinal = editTextDataFinal.getText().toString();
        String novaHoraInicial = editTextHoraInicial.getText().toString();
        String novaHoraFinal = editTextHoraFinal.getText().toString();
        String novoLocal = autoCompleteTxt.getText().toString();
        String novaDescricao = edit_desc_atv.getText().toString();
        String novaObservacao = edit_obs.getText().toString();
        boolean novoEquipamentos = checkBoxSimEvento.isChecked();
        String novoQuaisequipamentos = novoEquipamentos ? edit_quais_equip.getText().toString() : "Não";
        boolean novoprecisaTransmissao = checkBoxSimEventoTransmissao.isChecked();
        boolean novoTransmissao = checkBoxtransmissao.isChecked();
        boolean novoGravacao = checkBoxgravacao.isChecked();
        boolean novoAmbas = checkBoxAmbos.isChecked();
        boolean novoPresencial = checkBoxPresencial.isChecked();
        boolean novoHibrido = checkBoxHibrido.isChecked();
        boolean novoOnline = checkBoxonline.isChecked();
        boolean novoLinktransmissao = checkBoxSimEventoLink.isChecked();
        boolean novoPrecisaMaterialGrafico = checkBoxSimMaterialGrafico.isChecked();

        CollectionReference evento = db.collection("eventos");

        Query querytudoigual = evento.whereEqualTo("localdoEvento", novoLocal)
                .whereEqualTo("datadoEvento", novaDataInicial)
                .whereEqualTo("datadoEventoFinal", novaDataFinal)
                .whereEqualTo("horadoEventoInicial", novaHoraInicial)
                .whereEqualTo("horaEventoFinal", novaHoraFinal)
                .whereNotEqualTo("eventosId", eventId);

        querytudoigual.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                boolean isEmptyField = novaDataInicial.isEmpty() || novaDataFinal.isEmpty() || novaHoraInicial.isEmpty() || novaHoraFinal.isEmpty()
                        || novoNome.isEmpty() || novoSetor.isEmpty() || novoParticipante.isEmpty()
                        || novoLocal.isEmpty() || novaDescricao.isEmpty() || !checkBoxSimEvento.isChecked() && !checkBoxNaoEvento.isChecked()
                        || !checkBoxSimMaterialGrafico.isChecked() && !checkBoxNaoMaterialGrafico.isChecked();

                QuerySnapshot querySnapshot = task.getResult();

                if(querySnapshot != null && !querySnapshot.isEmpty()) {
                    // Já existe um evento com as mesmas informações
                    // Realize as ações apropriadas, como exibir uma mensagem de erro ou retornar do método
                    showErrorAlertDilog(mensagens[0],"Já existe um evento com as mesmas informações");

                } else {
                    // Não existe um evento com as mesmas informações, verifique a segunda restrição
                    Query secondQuery = evento.whereEqualTo("localdoEvento", novoLocal).whereNotEqualTo("eventosId", eventId);

                    secondQuery.get().addOnCompleteListener(secondTask -> {
                        if(secondTask.isSuccessful()) {
                            QuerySnapshot secondQuerySnapshot = secondTask.getResult();

                            if(secondQuerySnapshot != null && !secondQuerySnapshot.isEmpty()) {
                                // Já existe um evento com o mesmo local, verifique se a data está dentro do intervalo
                                boolean isDataHoraWithinRange = false;

                                for (DocumentSnapshot document : secondQuerySnapshot.getDocuments()) {
                                    String dataEvento = document.getString("datadoEvento");
                                    String dataFinalEvento = document.getString("datadoEventoFinal");
                                    String horaInicialEvento = document.getString("horadoEventoInicial");
                                    String horaFinalEvento = document.getString("horaEventoFinal");

                                    if (isDataHoraWithinRange(dataEvento, dataFinalEvento, horaInicialEvento, horaFinalEvento, novaDataInicial, novaDataFinal, novaHoraInicial, novaHoraFinal)) {
                                        isDataHoraWithinRange = true;
                                        break;
                                    }
                                }
                                if (isDataHoraWithinRange) {
                                    // Já existe um evento com intervalo de tempo que contém o evento a ser cadastrado
                                    // Realize as ações apropriadas, como exibir uma mensagem de erro ou retornar do método
                                    showErrorAlertDilog(mensagens[0],"Já existe um agendamento para essa data e local ou conflito de data e hora");
                                } else if (isEmptyField){
                                    showErrorAlertDilog(mensagens[0],"Preencha todos os campos");
                                }else if (novoEquipamentos && novoQuaisequipamentos.isEmpty()){
                                    showErrorAlertDilog(mensagens[0],"Preencha o campo dos equipamentos necessários");
                                }else if(novoprecisaTransmissao && !novoTransmissao && !novoGravacao && !novoAmbas){
                                    showErrorAlertDilog(mensagens[0],"Preencha o campo dos tipos de transmissão");
                                } else if(novoHibrido && !novoLinktransmissao ){
                                    showErrorAlertDilog(mensagens[0],"Preencha o campo do link do evento");
                                } else if(novoOnline && !novoLinktransmissao ){
                                    showErrorAlertDilog(mensagens[0],"Preencha o campo do link do evento");
                                }
                                else {
                                    int capacidadeMaxima = 0; // Capacidade máxima inicializada com 0
                                    switch (novoLocal) {
                                        case "Isabel dos Santos":
                                            capacidadeMaxima = 80;
                                            break;
                                        case "Paulo Freire":
                                            capacidadeMaxima = 150;
                                            break;
                                        case "Multimídia 1":
                                        case "Multimídia 2":
                                        case "Multimídia 3":
                                            capacidadeMaxima = 50;
                                            break;
                                        case "Tecnologias Educacionais":
                                            capacidadeMaxima = 8;
                                            break;
                                        case "Educação e Saúde":
                                            capacidadeMaxima = 8;
                                            break;
                                        case "Diretoria":
                                            capacidadeMaxima = 6;
                                            break;
                                        case "Pesquisa e Desenvolvimento":
                                            capacidadeMaxima = 15;
                                            break;
                                        case "Estágio":
                                            capacidadeMaxima = 8;
                                            break;
                                        case "Núcleo de Planejamento":
                                            capacidadeMaxima = 6;
                                            break;
                                        case "Sala de Reunião":
                                            capacidadeMaxima = 6;
                                            break;

                                    }
                                    if (Integer.parseInt(novoParticipante) > capacidadeMaxima){
                                        // Verificar se o número de participantes excede a capacidade máxima
                                        showErrorAlertDilog(mensagens[0], "A capacidade máxima para este local é de " + capacidadeMaxima + " participantes");
                                    } else {
                                        // Atualize os dados na coleção "eventos"
                                        Query query = db.collection("eventos").whereEqualTo("eventosId", eventId);
                                        Log.d("Atualizacao", "Eventos" + eventId);
                                        query.get().addOnCompleteListener(task006 -> {
                                            if (task006.isSuccessful()) {
                                                QuerySnapshot querySnapshot12 = task006.getResult();

                                                if (querySnapshot12 != null && !querySnapshot12.isEmpty()) {
                                                    DocumentSnapshot documentSnapshot = querySnapshot12.getDocuments().get(0);

                                                    // Atualize os campos relevantes diretamente no documento
                                                    documentSnapshot.getReference().update(
                                                            "nomedoEvento", novoNome,
                                                            "localdoEvento", novoLocal,
                                                            "descricaodaAtividade", novaDescricao,
                                                            "horaEventoFinal", novaHoraFinal,
                                                            "horadoEventoInicial", novaHoraInicial,
                                                            "datadoEvento", novaDataInicial,
                                                            "datadoEventoFinal", novaDataFinal
                                                    ).addOnSuccessListener(aVoid -> {

                                                        Log.d("Debug", "Dados do evento atualizados com sucesso na coleção 'eventos'");
                                                        showsucessAlertDilogAtualizado(mensagens[1],"Dados do evento atualizados com sucesso!");

                                                        // Se necessário, atualize os dados na coleção "infoEventos"
                                                        DocumentReference infoEventoRef = db.collection("infoEventos").document(eventId);

                                                        Map<String, Object> updates = new HashMap<>();
                                                        updates.put("setorResponsavel", novoSetor);
                                                        updates.put("numerosdeParticipantes", novoParticipante);
                                                        updates.put("observacoes", novaObservacao);
                                                        updates.put("equipamentos", novoEquipamentos ? "Sim" : "Não");
                                                        updates.put("quaisequipamentos", novoQuaisequipamentos);
                                                        updates.put("temtransmissao", novoprecisaTransmissao ? "Sim" : "Não");
                                                        updates.put("materiaisgraficos", novoPrecisaMaterialGrafico ? "Sim" : "Não");
                                                        if(novoprecisaTransmissao && novoTransmissao){
                                                            updates.put("temTransmissaoTipo", novoTransmissao ? "Sim" : "Não");
                                                            updates.put("temGravacaoTipo", novoGravacao ? "Sim" : "Não");
                                                            updates.put("temAmbosTipo", novoAmbas ? "Sim" : "Não");
                                                        } else {
                                                            if(novoprecisaTransmissao && novoGravacao){
                                                                updates.put("temTransmissaoTipo", novoTransmissao ? "Sim" : "Não");
                                                                updates.put("temGravacaoTipo", novoGravacao ? "Sim" : "Não");
                                                                updates.put("temAmbosTipo", novoAmbas ? "Sim" : "Não");
                                                            } else {
                                                                if(novoprecisaTransmissao && novoAmbas){
                                                                    updates.put("temTransmissaoTipo", novoTransmissao ? "Sim" : "Não");
                                                                    updates.put("temGravacaoTipo", novoGravacao ? "Sim" : "Não");
                                                                    updates.put("temAmbosTipo", novoAmbas ? "Sim" : "Não");
                                                                }
                                                                else{
                                                                    updates.put("temtransmissao", "Não");
                                                                }
                                                            }
                                                        }
                                                        if(novoPresencial){
                                                            updates.put("presencial", novoPresencial ? "Sim" : "Não");
                                                            updates.put("hibrido", novoHibrido ? "Sim" : "Não");
                                                            updates.put("online", novoOnline ? "Sim" : "Não");
                                                        } else if(novoHibrido && novoLinktransmissao){
                                                            updates.put("hibrido", novoHibrido ? "Sim" : "Não");
                                                            updates.put("presencial", novoPresencial ? "Sim" : "Não");
                                                            updates.put("online", novoOnline ? "Sim" : "Não");
                                                            updates.put("linkTransmissao", novoLinktransmissao ? "Sim" : "Não");
                                                        } else if(novoOnline && novoLinktransmissao){
                                                            updates.put("online", novoOnline ? "Sim" : "Não");
                                                            updates.put("hibrido", novoHibrido ? "Sim" : "Não");
                                                            updates.put("presencial", novoPresencial ? "Sim" : "Não");
                                                            updates.put("linkTransmissao", novoLinktransmissao ? "Sim" : "Não");
                                                        }

                                                        infoEventoRef.update(updates).addOnSuccessListener(aVoid1 -> {
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
                                                Log.d("Debug", "Erro ao buscar o evento: " + task006.getException());
                                                showErrorAlertDilog(mensagens[0], "Erro ao buscar o evento");
                                            }
                                        });
                                    }
                                }
                            } else {
                                if(isEmptyField){
                                    showErrorAlertDilog(mensagens[0],"Preencha todos os campos");
                                }else if (novoEquipamentos && novoQuaisequipamentos.isEmpty()){
                                    showErrorAlertDilog(mensagens[0],"Preencha o campo dos equipamentos necessários");
                                }else if(novoprecisaTransmissao && !novoTransmissao && !novoGravacao && !novoAmbas){
                                    showErrorAlertDilog(mensagens[0],"Preencha o campo dos tipos de transmissão");
                                } else if(novoHibrido && !novoLinktransmissao ){
                                    showErrorAlertDilog(mensagens[0],"Preencha o campo do link do evento");
                                } else if(novoOnline && !novoLinktransmissao ){
                                    showErrorAlertDilog(mensagens[0],"Preencha o campo do link do evento");
                                } else {
                                    int capacidadeMaxima = 0; // Capacidade máxima inicializada com 0
                                    switch (novoLocal) {
                                        case "Isabel dos Santos":
                                            capacidadeMaxima = 80;
                                            break;
                                        case "Paulo Freire":
                                            capacidadeMaxima = 150;
                                            break;
                                        case "Multimídia 1":
                                        case "Multimídia 2":
                                        case "Multimídia 3":
                                            capacidadeMaxima = 50;
                                            break;
                                        case "Tecnologias Educacionais":
                                            capacidadeMaxima = 8;
                                            break;
                                        case "Educação e Saúde":
                                            capacidadeMaxima = 8;
                                            break;
                                        case "Diretoria":
                                            capacidadeMaxima = 6;
                                            break;
                                        case "Pesquisa e Desenvolvimento":
                                            capacidadeMaxima = 15;
                                            break;
                                        case "Estágio":
                                            capacidadeMaxima = 8;
                                            break;
                                        case "Núcleo de Planejamento":
                                            capacidadeMaxima = 6;
                                            break;
                                        case "Sala de Reunião":
                                            capacidadeMaxima = 6;
                                            break;
                                    }
                                    if(Integer.parseInt(novoParticipante) > capacidadeMaxima){
                                        // Verificar se o número de participantes excede a capacidade máxima
                                        showErrorAlertDilog(mensagens[0], "A capacidade máxima para este local é de " + capacidadeMaxima + " participantes");
                                    } else {
                                        // Atualize os dados na coleção "eventos"
                                        Query query = db.collection("eventos").whereEqualTo("eventosId", eventId);
                                        Log.d("Atualizacao 1", "Eventos" + eventId);
                                        query.get().addOnCompleteListener(task345 -> {
                                            if (task345.isSuccessful()) {
                                                QuerySnapshot querySnapshot13 = task345.getResult();

                                                if (querySnapshot13 != null && !querySnapshot13.isEmpty()) {
                                                    DocumentSnapshot documentSnapshot = querySnapshot13.getDocuments().get(0);

                                                    // Atualize os campos relevantes diretamente no documento
                                                    documentSnapshot.getReference().update(
                                                            "nomedoEvento", novoNome,
                                                            "localdoEvento", novoLocal,
                                                            "descricaodaAtividade", novaDescricao,
                                                            "horaEventoFinal", novaHoraFinal,
                                                            "horadoEventoInicial", novaHoraInicial,
                                                            "datadoEvento", novaDataInicial,
                                                            "datadoEventoFinal", novaDataFinal
                                                    ).addOnSuccessListener(aVoid -> {

                                                        Log.d("Debug", "Dados do evento atualizados com sucesso na coleção 'eventos'");
                                                        showsucessAlertDilogAtualizado(mensagens[1],"Dados do evento atualizados com sucesso!");

                                                        // Se necessário, atualize os dados na coleção "infoEventos"
                                                        DocumentReference infoEventoRef = db.collection("infoEventos").document(eventId);

                                                        Map<String, Object> updates = new HashMap<>();
                                                        updates.put("setorResponsavel", novoSetor);
                                                        updates.put("numerosdeParticipantes", novoParticipante);
                                                        updates.put("observacoes", novaObservacao);
                                                        updates.put("equipamentos", novoEquipamentos ? "Sim" : "Não");
                                                        updates.put("quaisequipamentos", novoQuaisequipamentos);
                                                        updates.put("temtransmissao", novoprecisaTransmissao ? "Sim" : "Não");
                                                        updates.put("materiaisgraficos", novoPrecisaMaterialGrafico ? "Sim" : "Não");
                                                        if(novoprecisaTransmissao && novoTransmissao){
                                                            updates.put("temTransmissaoTipo", novoTransmissao ? "Sim" : "Não");
                                                            updates.put("temGravacaoTipo", novoGravacao ? "Sim" : "Não");
                                                            updates.put("temAmbosTipo", novoAmbas ? "Sim" : "Não");
                                                        } else {
                                                            if(novoprecisaTransmissao && novoGravacao){
                                                                updates.put("temTransmissaoTipo", novoTransmissao ? "Sim" : "Não");
                                                                updates.put("temGravacaoTipo", novoGravacao ? "Sim" : "Não");
                                                                updates.put("temAmbosTipo", novoAmbas ? "Sim" : "Não");
                                                            } else {
                                                                if(novoprecisaTransmissao && novoAmbas){
                                                                    updates.put("temTransmissaoTipo", novoTransmissao ? "Sim" : "Não");
                                                                    updates.put("temGravacaoTipo", novoGravacao ? "Sim" : "Não");
                                                                    updates.put("temAmbosTipo", novoAmbas ? "Sim" : "Não");
                                                                }
                                                                else{
                                                                    updates.put("temtransmissao", "Não");
                                                                }
                                                            }
                                                        }
                                                        if(novoPresencial){
                                                            updates.put("presencial", novoPresencial ? "Sim" : "Não");
                                                            updates.put("hibrido", novoHibrido ? "Sim" : "Não");
                                                            updates.put("online", novoOnline ? "Sim" : "Não");
                                                        } else if(novoHibrido && novoLinktransmissao){
                                                            updates.put("hibrido", novoHibrido ? "Sim" : "Não");
                                                            updates.put("presencial", novoPresencial ? "Sim" : "Não");
                                                            updates.put("online", novoOnline ? "Sim" : "Não");
                                                            updates.put("linkTransmissao", novoLinktransmissao ? "Sim" : "Não");
                                                        } else if(novoOnline && novoLinktransmissao){
                                                            updates.put("online", novoOnline ? "Sim" : "Não");
                                                            updates.put("hibrido", novoHibrido ? "Sim" : "Não");
                                                            updates.put("presencial", novoPresencial ? "Sim" : "Não");
                                                            updates.put("linkTransmissao", novoLinktransmissao ? "Sim" : "Não");
                                                        }

                                                        infoEventoRef.update(updates).addOnSuccessListener(aVoid1 -> {
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
                                                Log.d("Debug", "Erro ao buscar o evento: " + task345.getException());
                                                showErrorAlertDilog(mensagens[0], "Erro ao buscar o evento");
                                            }
                                        });
                                    }
                                }
                            }
                        } else {
                            // Lida com possíveis erros na consulta do Firestore
                            //showErrorAlertDilog(mensagens[0],"Erro ao verificar agendamento");
                            String errormessage = "Erro ao verificar agendamento" + secondTask.getException().getMessage();
                            showErrorAlertDilog(mensagens[0],errormessage);
                            Log.d("tagerror", errormessage);
                        }
                    });
                 }
            } else{
                // Lida com possíveis erros na consulta do Firestore
                String errormessage = "Erro ao verificar agendamento" + task.getException().getMessage();
                showErrorAlertDilog(mensagens[0],errormessage);
                Log.d("tagerror", errormessage);
            }
        });
    }
    private boolean isDataHoraWithinRange(String dataEvento, String dataFinalEvento, String horaInicialEvento, String horaFinalEvento, String data, String datafinal, String hora, String horafinal) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Date eventoStartDateTime = sdf.parse(dataEvento + " " + horaInicialEvento);
            Date eventoEndDateTime = sdf.parse(dataFinalEvento + " " + horaFinalEvento);
            Date novoEventoStartDateTime = sdf.parse(data + " " + hora);
            Date novoEventoEndDateTime = sdf.parse(datafinal + " " + horafinal);

            return eventoStartDateTime != null && eventoEndDateTime != null && novoEventoStartDateTime != null && novoEventoEndDateTime != null &&
                    novoEventoStartDateTime.compareTo(eventoEndDateTime) <= 0 && novoEventoEndDateTime.compareTo(eventoStartDateTime) >= 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
    @Override
    public void onBackPressed(){
        Toast.makeText(this, "Utilize o botão de voltar no topo da tela", Toast.LENGTH_SHORT).show();
    }
    private void cadastrarinfos() {
        String setor = edit_setor_resp.getText().toString();
        String participantes = edit_numeros_participantes.getText().toString();
        String observacoes = edit_obs.getText().toString();
        String documentoId = eventoId;

        boolean precisaEquipamento = checkBoxSimEvento.isChecked();
        boolean precisaTransmissao = checkBoxSimEventoTransmissao.isChecked();
        boolean transmissao = checkBoxtransmissao.isChecked();
        boolean gravacao = checkBoxgravacao.isChecked();
        boolean ambas = checkBoxAmbos.isChecked();
        boolean presencial = checkBoxPresencial.isChecked();
        boolean hibrido = checkBoxHibrido.isChecked();
        boolean online = checkBoxonline.isChecked();
        boolean linktransmissao = checkBoxSimEventoLink.isChecked();
        boolean precisaMaterialGrafico = checkBoxSimMaterialGrafico.isChecked();

        String equipamentos = precisaEquipamento ? edit_quais_equip.getText().toString() : "Não";
        String materialgrafico = precisaMaterialGrafico ? "Sim" : "Não";
        //String precisaTransmissao1 = precisaTransmissao ? "Sim" : "Não";

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference info = db.collection("infoEventos").document(documentoId);

        Map<String, Object> eventos2 = new HashMap<>();
        eventos2.put("setorResponsavel", setor);
        eventos2.put("numerosdeParticipantes", participantes);
        eventos2.put("observacoes", observacoes);
        eventos2.put("eventosId", eventoId);
        eventos2.put("equipamentos", precisaEquipamento ? "Sim" : "Não");
        eventos2.put("quaisequipamentos", equipamentos);
        eventos2.put("temtransmissao", precisaTransmissao ? "Sim" : "Não");
        eventos2.put("materiaisgraficos", materialgrafico);
        if(precisaTransmissao && transmissao || gravacao || ambas){
            eventos2.put("temTransmissaoTipo", transmissao ? "Sim" : "Não");
            eventos2.put("temGravacaoTipo", gravacao ? "Sim" : "Não");
            eventos2.put("temAmbosTipo", ambas ? "Sim" : "Não");
        } else {
            eventos2.put("temtransmissao", "Não");
        }
        if(presencial){
            eventos2.put("presencial", "Sim");
        } else if(hibrido && linktransmissao){
            eventos2.put("hibrido", "Sim");
            eventos2.put("linkTransmissao", linktransmissao ? "Sim" : "Não");
        } else if(online && linktransmissao){
            eventos2.put("online", "Sim");
            eventos2.put("linkTransmissao", linktransmissao ? "Sim" : "Não");
        }

        info.set(eventos2).addOnSuccessListener(unused -> Log.d("Firestore", "Informações do evento cadastradas com sucesso!"))
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
    private void showsucessAlertDilog(String titulo,String erro){

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
                Intent intentCE = new Intent(FormCadastroEvento.this, PaginaPrincipal.class);
                intentCE.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentCE);
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
                Intent intentCE = new Intent(FormCadastroEvento.this, FormVisualizacao.class);
                intentCE.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentCE);
            }
        });

        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    public static class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){

            Calendar calendario = Calendar.getInstance();
            int dia = calendario.get(Calendar.DAY_OF_MONTH);
            int mes = calendario.get(Calendar.MONTH);
            int ano = calendario.get(Calendar.YEAR);

            return new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, ano, mes, dia);
        }
        @Override
        public void onDateSet(android.widget.DatePicker view, int ano, int mes, int dia) {

            String tagClicado = getTag();

            assert tagClicado != null;
            switch (tagClicado) {
                case "data1":
                    @SuppressLint("DefaultLocale")
                    String dataFormatada = String.format("%02d/%02d/%04d", dia, mes + 1, ano);
                    editTextData.setText(dataFormatada);
                    break;
                case "data2":
                    @SuppressLint("DefaultLocale")
                    String dataFormatada2 = String.format("%02d/%02d/%04d", dia, mes + 1, ano);
                    editTextData2.setText(dataFormatada2);
                    break;
                case "datafinal1":
                    @SuppressLint("DefaultLocale")
                    String dataFormatada3 = String.format("%02d/%02d/%04d", dia, mes + 1, ano);
                    editTextDataFinal.setText(dataFormatada3);
                    break;
                case "datafinal2":
                    @SuppressLint("DefaultLocale") String dataFormatada4 = String.format("%02d/%02d/%04d", dia, mes + 1, ano);
                    editTextDataFinal2.setText(dataFormatada4);
                    break;
            }
        }
    }
    public static class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @NonNull
        @Override
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
                editTextHoraInicial.setText(horaformatada);

            }else if (tagClicado.equals("hora2")){

                @SuppressLint("DefaultLocale")
                String horaformatadafinal = String.format("%02d:%02d",horas,minutos);
                editTextHoraFinal.setText(horaformatadafinal);
            }
        }
    }
    @SuppressLint({"WrongViewCast", "CutPasteId"})
    private void IniciarComponentes(){

        //Recuperar as informações que foram colocados no front-end.
        edit_nome_evento = findViewById(R.id.edit_nome_evento);
        edit_setor_resp = findViewById(R.id.edit_setor_resp);
        edit_numeros_participantes = findViewById(R.id.edit_numeros_participantes);
        edit_desc_atv = findViewById(R.id.edit_desc_atv);
        edit_obs = findViewById(R.id.edit_obs);
        editTextHoraInicial = findViewById(R.id.edit_horas);
        editTextHoraFinal = findViewById(R.id.edit_horas_final);
        editTextData = findViewById(R.id.edit_data);
        editTextData2 = findViewById(R.id.edit_data);
        editTextDataFinal = findViewById(R.id.edit_data_final);
        editTextDataFinal2 = findViewById(R.id.edit_data_final);
        imageViewHora = findViewById(R.id.imageViewHora);
        imageViewHoraFinal = findViewById(R.id.imageViewHoraFinal);
        imageViewData = findViewById(R.id.imageViewData);
        imageViewDataFinal = findViewById(R.id.imageViewDataFinal);
        imageViewoff = findViewById(R.id.imageViewoff);
        bt_cadastrar = findViewById(R.id.bt_cadastrar);
        imageViewback = findViewById(R.id.imageViewback);
        autoCompleteTxt = findViewById(R.id.auto_complete_txt);
        scrollview = findViewById(R.id.scrollView);

        //CheckBox no front-end
        checkBoxSimEvento = findViewById(R.id.checkBoxSimEvento);
        checkBoxNaoEvento = findViewById(R.id.checkBoxNãoEvento);
        checkBoxSimEventoTransmissao = findViewById(R.id.checkBoxSimEventoTransmissao);
        checkBoxNaoEventoTransmissao = findViewById(R.id.checkBoxNãoEventoTransmissao);
        checkBoxtransmissao = findViewById(R.id.checkBoxtransmissao);
        checkBoxgravacao = findViewById(R.id.checkBoxgravacao);
        checkBoxAmbos = findViewById(R.id.checkBoxAmbos);
        checkBoxPresencial = findViewById(R.id.checkBoxPresencial);
        checkBoxHibrido = findViewById(R.id.checkBoxHibrido);
        checkBoxonline = findViewById(R.id.checkBoxonline);
        checkBoxSimEventoLink = findViewById(R.id.checkBoxSimEventoLink);
        checkBoxNaoEventoLink = findViewById(R.id.checkBoxNãoEventoLink);
        checkBoxSimMaterialGrafico = findViewById(R.id.checkBoxSimMaterialGrafico);
        checkBoxNaoMaterialGrafico = findViewById(R.id.checkBoxNãoMaterialGrafico);
        edit_quais_equip = findViewById(R.id.edit_quais_equip);
        title_link = findViewById(R.id.title_link);
        title_solicitacao_material_grafico = findViewById(R.id.title_solicitacao_material_grafico);
        link_solicitacao_material_grafico = findViewById(R.id.link_solicitacao_material_grafico);

        //Desabilitar os campos de data e hora.
        editTextHoraInicial.setEnabled(false);
        editTextHoraFinal.setEnabled(false);
        editTextData.setEnabled(false);
        editTextData2.setEnabled(false);
        editTextDataFinal.setEnabled(false);
        editTextDataFinal2.setEnabled(false);
    }
}